
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class ExtrairDados {

	String  info_inicio;
	String	info_fim;

	private String nomeLogLoadSD;
	private String nomeExcecaoLoadSD;
	
	

	private static Map<String,String> mapaDescEstados;

	private static Integer idCarregamento;

	private final static String sequenceCarregamento = "SELECT CARREGAMENTO_HEADER_SEQ.NEXTVAL FROM DUAL ";
	private final static String sequenceVagao = "SELECT CTE_VAGAO_SEQ.NEXTVAL FROM DUAL ";



	public ExtrairDados (String[] info)
	{
		this.info_inicio = info[0].toString();
		this.info_fim    = info[1].toString();
		carregarMapDescEstado();
		nomeLogLoadSD = "vli_load_SD_" + info_inicio.replace("/", "_") + "_" + info_fim.replace("/", "_") + ".txt";
		nomeExcecaoLoadSD = "vli_excecao_SD_" + info_inicio.replace("/", "_") + "_" + info_fim.replace("/", "_") + ".txt";

	}	


	public void consultarCarregamento() throws FileNotFoundException, IOException, SQLException
	{	
		Connection conn = null;
		try {

			conn = OracleConnection.getConnection();


		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		this.consultar(conn);

		if (!conn.isClosed())
		{	
			conn.close();
		}
	}



	private void consultar(Connection conn) throws SQLException {


		String query = Constantes.queryConsultarCarregamento;

		OraclePreparedStatement stmt = null;
		OracleResultSet rs			 = null;

		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

						stmt.setString(1, info_inicio);
						stmt.setString(2, info_fim);

			log("Iniciando a consulta de carga CTE....", nomeLogLoadSD);
			rs = (OracleResultSet) stmt.executeQuery();
			log("Termino a consulta de carga CTE ....", nomeLogLoadSD);
			int cont = 0;


			while (rs.next()) {

				//String 	dataExecuçãoResult 	= info_inicio.replace("/", "").substring(2,8);
				String 	dataExecuçãoResult 	= "122016";
				String serie				= rs.getString("SERIE_CARREGAMENTO");
				int numero					= rs.getInt("NUMERO_CARREGAMENTO");
				String versao_carregamento 	= rs.getString("VERSAO_CARREGAMENTO");
				Long   valorServicos     	= rs.getLong("VALOR_SERVICOS");
				String unidadeMedida	  	= rs.getString("UNIDADE_MEDIDA");

				/*
				 * Verificação se o carregamento na versão zero tem um crédito total ou substituição. Neste caso o carregamento não será processado
				 * TODO Verificar se a proxima versao será credito e portanto não será processada
				 */
				if(versao_carregamento.equals("0") && !isDocCargaValido(serie,numero,valorServicos,conn))
				{
					log("Carregamento não extraidos :  Serie: "+ serie + " Numero : " + numero , nomeExcecaoLoadSD);

					continue;
				}



				boolean isVOF = (rs.getString("IND_EMPRESA_CONTRATADA").equals("VF"));
				boolean isGenerico = (rs.getString("IND_NIVEL_ESP_PERFIL_VAG").equals("G"));


				idCarregamento = getSequence(conn, sequenceCarregamento);

				try {

					inserirCarregamentos(conn,  idCarregamento,  dataExecuçãoResult, isVOF, rs);
					inserirServicos(conn, idCarregamento, serie, numero, versao_carregamento, unidadeMedida);
					inserirVagoes(conn, idCarregamento, serie, numero, versao_carregamento, unidadeMedida, isGenerico);

					cont++;
				}
				catch (SQLException e) {
					//System.out.println(" Serie : "+  serie + " Numero Carregamento " + numero + " VersaoCarregamento" + versao_carregamento + " DataEmissao " + dataExecuçãoResult);
					log("Carregamento não extraidos :  Serie: "+ serie + " Numero : " + numero + " Msg erro: " + e.getMessage(), nomeExcecaoLoadSD);
					e.printStackTrace();
				}

				if((cont % 200) == 0){

					//System.out.println("foram commitados "+ cont + " da Data :" + rs.getString("DATA_EMISSAO"));

					conn.commit();
					log("foram commitados "+ cont + " da Data :" + rs.getString("DATA_EMISSAO"), nomeLogLoadSD);
					Date dt1 = new Date();

					SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
					String dateInicio = df.format(dt1);

					System.out.println("Horas Commit:" + dateInicio);
					System.out.println("Numero de carregamentos commitados" + cont);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			rs.close();
			stmt.close();
		}
	}

	private Integer getSequence(Connection conn, String stringSequence)throws SQLException{

		OraclePreparedStatement stmtSeq	= null;
		OracleResultSet rset 			= null;
		Integer idSequence 				= null;

		try{

			stmtSeq = (OraclePreparedStatement)conn.prepareStatement(stringSequence);

			rset = (OracleResultSet)stmtSeq.executeQuery();

			if ( rset.next() ) {
				if( rset.getObject( 1 ) != null ) {
					idSequence = rset.getInt( 1 );
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			rset.close();
			stmtSeq.close();
		}
		return idSequence;
	}


	private void inserirCarregamentos(Connection conn, Integer idCarregamento, String dataExecuçãoResult, boolean isVOF, OracleResultSet rs)throws SQLException{


		String query = Constantes.queryInserirCarregamento;

		OraclePreparedStatement stmt = null;

		String serie = null;
		String numCarregamento = null;
		String versaoCarregamento = null;
		//Date dataEmissao = null;

		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			String orgId = rs.getString("ORG_ID") != null ? rs.getString("ORG_ID").toString() : null;


			DadosExpedidorRecebedor dadosExpedidorRecebedor = new DadosExpedidorRecebedor();
			DadosDocumentoAnterior  dadosDocumentoAnterior = new DadosDocumentoAnterior();

			String cod_terminal_carga = rs.getString("COD_TERMINAL_CARGA") != null ? rs.getString("COD_TERMINAL_CARGA").toString() : null;

			String cod_terminal_descarga = rs.getString("COD_TERMINAL_DESCARGA") != null ? rs.getString("COD_TERMINAL_DESCARGA").toString() : null;

			String cod_estacao_origem = rs.getString("ID_ORIGEM") != null ? rs.getString("ID_ORIGEM").toString() : null;

			String cod_estacao_destino = rs.getString("ID_DESTINO") != null ? rs.getString("ID_DESTINO").toString() : null;

			String tipoCliente = rs.getString("TIPO_CLIENTE_REMETENTE") != null ? rs.getString("TIPO_CLIENTE_REMETENTE").toString() : null;

			boolean isComplementar = rs.getString("TIPO_CARREGAMENTO").equals("SUP");

			String tipoDocFiscal = rs.getString("TIPO_DOC_FISCAL");

			stmt.setInt 		(1	, idCarregamento		   ) ;
			stmt.setString  	(2	, rs.getString("CODIGO_FERROVIA"));

			serie = rs.getString("SERIE_CARREGAMENTO");
			numCarregamento = rs.getString("NUMERO_CARREGAMENTO");
			versaoCarregamento = rs.getString("VERSAO_CARREGAMENTO");
			//Date dateEmissao = rs.getDate  ("DATA_EMISSAO");


			if(!isVOF)
			{
				stmt.setString  (	3	, rs.getString("SERIE_CARREGAMENTO"));
				stmt.setInt     (	4	, rs.getInt   ("NUMERO_CARREGAMENTO"));
				stmt.setString  (	9	, rs.getString("CODIGO_FLUXO"));

			}
			else
			{
				stmt.setString  (	3	, rs.getString("SERIE_CARREGAMENTO_ASSOC"));
				stmt.setInt    (	4	, rs.getInt("NUMERO_CARREGAMENTO_ASSOC"));
				stmt.setString  (	9	, rs.getString("NUMERO_FLUXO_ASSOC"));
			}

			stmt.setString  (	5	, rs.getString("VERSAO_CARREGAMENTO"));
			stmt.setDate    (	6	, rs.getDate  ("DATA_EMISSAO"));
			stmt.setString  (	7	, rs.getString("TIPO_CARREGAMENTO"));
			stmt.setString  (	8	, rs.getString("SITUACAO_CARREGAMENTO"));

			stmt.setString  (	10	, rs.getString("CODIGO_ORIGEM"));
			stmt.setString  (	11	, rs.getString("CODIGO_DESTINO"));
			stmt.setString  (	12	, rs.getString("CODIGO_MERCADORIA"));
			stmt.setString  (	13	, rs.getString("RAZAO_SOCIAL_EMITENTE"));
			stmt.setString  (	14	, rs.getString("ENDERECO_EMITENTE"));
			stmt.setString  (	15	, rs.getString("CEP_EMITENTE"));
			stmt.setString  (	16	, rs.getString("BAIRRO_EMITENTE"));
			stmt.setString  (	17	, rs.getString("CIDADE_EMITENTE"));
			stmt.setString  (	18	, rs.getString("IBGE_EMITENTE"));
			stmt.setString  (	19	, mapaDescEstados.get(rs.getString("COD_ESTADO_EMITENTE")));
			stmt.setString  (	20	, rs.getString("COD_ESTADO_EMITENTE"));
			stmt.setString  (	21	, rs.getString("CNPJ_EMITENTE"));
			stmt.setString  (	22	, rs.getString("INSC_MUNICIPAL_EMITENTE"));
			stmt.setString  (	23	, rs.getString("INSC_ESTADUAL_EMITENTE"));
			stmt.setString  (	24	, rs.getString("SERIE_FATURA"));
			stmt.setString  (	25	, rs.getString("NUMERO_FATURA"));
			stmt.setDate    (	26	, rs.getDate  ("DATA_FATURA"));
			stmt.setString  (	27	, rs.getString("TIPO_SERVICO_CTE"));
			stmt.setString  (	28	, rs.getString("STATUS_FATURA"));
			stmt.setString  (	29	, rs.getString("CHAVE_CTE"));
			stmt.setString  (	30	, rs.getString("RAZAO_SOCIAL_REMETENTE"));
			stmt.setString  (	31	, rs.getString("ENDERECO_REMETENTE"));
			stmt.setString  (	32	, rs.getString("CEP_REMETENTE"));
			stmt.setString  (	33	, rs.getString("BAIRRO_REMETENTE"));
			stmt.setString  (	34	, rs.getString("CIDADE_REMETENTE"));
			stmt.setString  (	35	, rs.getString("IBGE_REMETENTE"));
			stmt.setString  (	36	, mapaDescEstados.get(rs.getString("COD_ESTADO_REMETENTE")));
			stmt.setString  (	37	, rs.getString("COD_ESTADO_REMETENTE"));
			stmt.setString  (	38	, rs.getString("CNPJ_REMETENTE"));
			stmt.setString  (	39	, rs.getString("INSC_MUNICIPAL_REMETENTE"));
			stmt.setString  (	40	, rs.getString("INSC_ESTADUAL_REMETENTE"));
			stmt.setString  (	41	, rs.getString("RAZAO_SOCIAL_DESTINATARIO"));
			stmt.setString  (	42	, rs.getString("ENDERECO_DESTINATARIO"));
			stmt.setString  (	43	, rs.getString("CEP_DESTINATARIO"));
			stmt.setString  (	44	, rs.getString("BAIRRO_DESTINATARIO"));
			stmt.setString  (	45	, rs.getString("CIDADE_DESTINATARIO"));
			stmt.setString  (	46	, rs.getString("IBGE_DESTINATARIO"));
			stmt.setString  (	47	, mapaDescEstados.get(rs.getString("COD_ESTADO_DESTINATARIO")));
			stmt.setString  (	48	, rs.getString("COD_ESTADO_DESTINATARIO"));
			stmt.setString  (	49	, rs.getString("CNPJ_DESTINATARIO"));
			stmt.setString  (	50	, rs.getString("INSC_MUNICIPAL_DESTINATARIO"));
			stmt.setString  (	51	, rs.getString("INSC_ESTADUAL_DESTINATARIO"));

			/*
			 * Paulo Diniz
			 * Projeto SAP carga SD
			 * Criado m[etodo à parte para obter dados do Expedidor
			 * 30-11-2016 
			 * 
			 */
			obterDadosExpedidorRecebedor(conn, orgId, dadosExpedidorRecebedor,
					cod_terminal_carga, cod_estacao_origem, tipoCliente, false);

			stmt.setString		(52,		dadosExpedidorRecebedor.getRazaoSocialExpedidorRecebedor());
			stmt.setString		(53,		dadosExpedidorRecebedor.getEnderecoExpedidorRecebedor());
			stmt.setString		(54,		dadosExpedidorRecebedor.getCepExpedidorRecebedor());
			stmt.setString		(55,		dadosExpedidorRecebedor.getBairroExpedidorRecebedor());
			stmt.setString		(56,		dadosExpedidorRecebedor.getCidadeExpedidorRecebedor());
			stmt.setString		(57,		dadosExpedidorRecebedor.getIbgeCodMunicipioExpedidorRecebedor());
			stmt.setString		(58,		dadosExpedidorRecebedor.getIbgeDescEstadoExpedidorRecebedor());
			stmt.setString		(59,		dadosExpedidorRecebedor.getEstadoExpedidorRecebedor());
			stmt.setString		(60,		dadosExpedidorRecebedor.getCnpjExpedidorRecebedor());
			stmt.setString		(61,		dadosExpedidorRecebedor.getInscrMunicipalExpedidorRecebedor());
			stmt.setString		(62,		dadosExpedidorRecebedor.getInscrEstadualExpedidorRecebedor());

			/*
			 * Paulo Diniz
			 * Projeto SAP carga SD
			 * Criado m[etodo à parte para obter dados do Recebedor
			 * 30-11-2016 
			 * 
			 */
			obterDadosExpedidorRecebedor(conn, orgId, dadosExpedidorRecebedor,
					cod_terminal_descarga, cod_estacao_destino, tipoCliente, true);

			stmt.setString		(63,		dadosExpedidorRecebedor.getRazaoSocialExpedidorRecebedor());
			stmt.setString		(64,		dadosExpedidorRecebedor.getEnderecoExpedidorRecebedor());
			stmt.setString		(65,		dadosExpedidorRecebedor.getCepExpedidorRecebedor());
			stmt.setString		(66,		dadosExpedidorRecebedor.getBairroExpedidorRecebedor());
			stmt.setString		(67,		dadosExpedidorRecebedor.getCidadeExpedidorRecebedor());
			stmt.setString		(68,		dadosExpedidorRecebedor.getIbgeCodMunicipioExpedidorRecebedor());
			stmt.setString		(69,		dadosExpedidorRecebedor.getIbgeDescEstadoExpedidorRecebedor());
			stmt.setString		(70,		dadosExpedidorRecebedor.getEstadoExpedidorRecebedor());
			stmt.setString		(71,		dadosExpedidorRecebedor.getCnpjExpedidorRecebedor());
			stmt.setString		(72,		dadosExpedidorRecebedor.getInscrMunicipalExpedidorRecebedor());
			stmt.setString		(73,		dadosExpedidorRecebedor.getInscrEstadualExpedidorRecebedor());


			stmt.setString  (	74	, rs.getString("RAZAO_SOCIAL_TOMADOR"));
			stmt.setString  (	75	, rs.getString("ENDERECO_TOMADOR"));
			stmt.setString  (	76	, rs.getString("CEP_TOMADOR"));
			stmt.setString  (	77	, rs.getString("BAIRRO_TOMADOR"));
			stmt.setString  (	78	, rs.getString("CIDADE_TOMADOR"));
			stmt.setString  (	79	, rs.getString("IBGE_TOMADOR"));
			stmt.setString  (	80	, mapaDescEstados.get(rs.getString("COD_ESTADO_TOMADOR")));
			stmt.setString  (	81	, rs.getString("COD_ESTADO_TOMADOR"));
			stmt.setString  (	82	, rs.getString("CNPJ_TOMADOR"));
			stmt.setString  (	83	, rs.getString("INSC_MUNICIPAL_TOMADOR"));
			stmt.setString  (	84	, rs.getString("INSC_ESTADUAL_TOMADOR"));

			/*
			 * Paulo Diniz
			 * Projeto SAP carga SD
			 * Criado metodo à parte para obter dados Documento Anterior
			 * 06-12-2016 
			 * 
			 */
			obterDadosDocumentoAnterior(conn, serie, numCarregamento, versaoCarregamento, tipoDocFiscal, dadosDocumentoAnterior);

			stmt.setString  (	85	, dadosDocumentoAnterior.getChaveDocAnterior());
			stmt.setString  (	86	, dadosDocumentoAnterior.getSerieDocAnterior());
			stmt.setString  (	87	, dadosDocumentoAnterior.getNumCarregamentoDocAnterior());
			stmt.setString  (	88	, dadosDocumentoAnterior.getTipoDocAnterior());
			stmt.setString  (	89	, dadosDocumentoAnterior.getDataEmissaoDocAnterior());
			stmt.setString  (	90	, rs.getString("RAZAO_SOCIAL_EMISSOR"));
			stmt.setString  (	91	, rs.getString("ENDERECO_EMISSOR"));
			stmt.setString  (	92	, rs.getString("CEP_EMISSOR"));
			stmt.setString  (	93	, rs.getString("BAIRRO_EMISSOR"));
			stmt.setString  (	94	, rs.getString("CIDADE_EMISSOR"));
			stmt.setString  (	95	, rs.getString("IBGE_EMISSOR"));
			stmt.setString  (	96	, mapaDescEstados.get(rs.getString("COD_ESTADO_EMISSOR")));
			stmt.setString  (	97	, rs.getString("COD_ESTADO_EMISSOR"));
			stmt.setString  (	98	, rs.getString("CNPJ_EMISSOR"));
			stmt.setString  (	99	, rs.getString("INSC_MUNICIPAL_EMISSOR"));
			stmt.setString  (	100	, rs.getString("INSC_ESTADUAL_EMISSOR"));


			if(isComplementar)
			{
				if(!isVOF)
				{
					stmt.setString  (	101	, rs.getString("SERIE_CARREGAMENTO"));
					stmt.setInt    (	102	, rs.getInt   ("NUMERO_CARREGAMENTO"));
					stmt.setDate  (	103	, rs.getDate("DATA_EMISSAO"));
				}
				else
				{
					stmt.setString  (	101	, rs.getString("SERIE_CARREGAMENTO_ASSOC"));
					stmt.setInt  (	102	, rs.getInt   ("NUMERO_CARREGAMENTO_ASSOC"));
					stmt.setDate  (	103	, rs.getDate("DATA_EMISSAO"));
				}


			}
			else
			{
				stmt.setString  (	101	, rs.getString("CTE_SERIE"));
				stmt.setString  (	102	, rs.getString("CTE_NUMERO"));
				stmt.setDate  (	103	, rs.getDate("CTE_EMISSAO"));

			}
			stmt.setString  (	104	, dataExecuçãoResult);
			stmt.setString  (	105	, "N");


			stmt.execute();

		} catch (SQLException e) {

			//System.out.println(" Serie : "+  serie + " Numero Carregamento " + numCarregamento + " VersaoCarregamento" + versaoCarregamento + " DataEmissao " + dataEmissao);
			e.printStackTrace();
			throw e;
		}
		finally
		{
			stmt.close();
		}

	}

	private void obterDadosDocumentoAnterior(Connection conn, String serie,
			String numCarregamento,
			String versaoCarregamento, String tipoDoc, DadosDocumentoAnterior dadosDocumentoAnterior) throws SQLException {

		String query = Constantes.queryConsultarDadosDocAnterior;

		OraclePreparedStatement stmt = null;
		OracleResultSet rs = null;
		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());
			stmt.setString(1, versaoCarregamento);
			stmt.setString(2, serie);
			stmt.setString(3, numCarregamento);

			rs = (OracleResultSet) stmt.executeQuery();

			if ( rs.next() ) 
			{	
				dadosDocumentoAnterior.setChaveDocAnterior(rs.getString("CHAVE_CTE_ANTERIOR"));
				dadosDocumentoAnterior.setSerieDocAnterior(rs.getString("SERIE_ANTERIOR"));
				dadosDocumentoAnterior.setNumCarregamentoDocAnterior(rs.getString("DOCUMENTO_ANTERIOR"));
				dadosDocumentoAnterior.setTipoDocAnterior(tipoDoc);
				dadosDocumentoAnterior.setDataEmissaoDocAnterior(rs.getString("DATA_EMISSAO_DOC_ANTERIOR"));

			}


		}
		catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{	
			rs.close();
			stmt.close();
		}
	}
	private void obterDadosExpedidorRecebedor(Connection conn, String orgId,
			DadosExpedidorRecebedor dadosExpedidorRecebedor,
			String codTerminalCargaDescarga, String codEstacaoCargaDescarga, String tipoCliente, boolean isDestino)
					throws SQLException {


		/*
		 * Verificaçao se for cliente exterior, não carregará dados do recebedor
		 */
		if(isDestino && tipoCliente != null && tipoCliente.toString().equals("3"))
		{
			return;
		}
		else
		{
			if(codTerminalCargaDescarga != null)
			{
				obterDadosExpedidorRecebedorTerminal(conn, dadosExpedidorRecebedor, codTerminalCargaDescarga, orgId); // IBGE REMETENTE

				if(dadosExpedidorRecebedor.getCnpjExpedidorRecebedor() == null)
				{
					obterDadosExpedidorRecebedorEstacao(conn, dadosExpedidorRecebedor, codEstacaoCargaDescarga, orgId);
				}
			}
			else
			{
				obterDadosExpedidorRecebedorEstacao(conn, dadosExpedidorRecebedor, codEstacaoCargaDescarga, orgId);
			}

		}
	}

	private void inserirVagoes(Connection conn, Integer idCarregamento, String serie, int numero, String versao, String unidadeMedida, boolean isGenerico) throws SQLException {


		String queryVagao	= Constantes.queryConsultarVagoes;

		OraclePreparedStatement stmt = null;
		OracleResultSet rs = null;
		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(queryVagao.toString());
			stmt.setString	(1, serie);
			stmt.setInt		(2, numero);
			stmt.setString	(3, versao);

			rs = (OracleResultSet) stmt.executeQuery();

			boolean entrou = false;

			while (rs.next()){
				entrou = true;
				inserirVagao(conn,  idCarregamento, rs, unidadeMedida, isGenerico);
			} 

			if(!entrou){
				log("Carregamento não extraidos :  Serie: "+ serie + " Numero : " + numero  + " Não foi encontrado registros na cte_vagao", nomeExcecaoLoadSD);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{	
			rs.close();
			stmt.close();
		}

	}

	private void inserirVagao(Connection conn, Integer idCarregamento, OracleResultSet rs, String unidadeMedida, boolean isGenerico)throws SQLException{

		String insert_vagao = Constantes.queryInserirVagoes; 

		OraclePreparedStatement stmt = null;
		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(insert_vagao.toString());

			Integer idVagao = getSequence(conn, sequenceVagao);

			stmt.setInt		(1, 	idCarregamento);
			stmt.setString	(2, 	rs.getString("SERIE_VAGAO"));
			stmt.setString	(3, 	rs.getString("CODIGO_VAGAO"));
			stmt.setString	(4, 	unidadeMedida);
			stmt.setDouble		(5, 	rs.getDouble("PESO_REAL"));
			if(unidadeMedida.equals("TO") || unidadeMedida.equals("UN") ||unidadeMedida.equals("CN"))
				stmt.setDouble		(6, 	rs.getDouble("PESO_CALCULADO"));
			else
				stmt.setNull		(6, 	0);
			stmt.setString	(7, 	rs.getString("NUM_ROMANEIO"));
			stmt.setString	(8, 	rs.getString("NUM_PEDIDO"));
			stmt.setString	(9, 	rs.getString("MODELO_NOTA"));
			stmt.setString	(10, 	rs.getString("SERIE_NOTA"));
			stmt.setString	(11, 	rs.getString("NUMERO_NOTA"));
			stmt.setDate	(12, 	rs.getDate("DATA_EMISSAO"));
			stmt.setDouble		(13, 	rs.getDouble("VALOR_BASE_ICMS"));
			stmt.setDouble		(14, 	rs.getDouble("VALOR_TOTAL_ICMS"));
			stmt.setDouble		(15, 	rs.getDouble("VALOR_BASE_IPI"));
			stmt.setDouble		(16, 	rs.getDouble("VALOR_TOTAL_IPI"));
			stmt.setDouble		(17, 	rs.getDouble("VALOR_TOTAL_PRODUTOS"));
			stmt.setDouble		(18, 	rs.getDouble("VALOR_NF"));
			stmt.setString	(19, 	rs.getString("CFOP_PREDOMINANTE"));
			stmt.setDouble		(20, 	rs.getDouble("PESO_RATEADO"));
			stmt.setString	(21, 	rs.getString("PIN_SUFRAMA"));
			stmt.setDate	(22, 	rs.getDate("DT_PREV_ENTREGA"));
			stmt.setInt		(23, 	idVagao);

			if(unidadeMedida.equals("M3"))
				stmt.setDouble		(24, 	rs.getDouble("PESO_CALCULADO"));
			else
				stmt.setNull		(24, 	0);

			stmt.setString	(25, 	rs.getString("TIPO_NOTA_FISCAL_MERCADORIA"));
			stmt.setString	(26, 	rs.getString("CHAVE_MERCADORIA"));
			stmt.setString	(27, 	isGenerico ? "PR" : "PM");

			//TODO colocar peso minimo
			stmt.setNull	(28, 	0);

			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			stmt.close();
		}
	}

	private void inserirServico(Connection conn, Integer idCarregamento, OracleResultSet rs, String unidadeMedida)throws SQLException{


		String insert_servico = Constantes.queryInserirServicos;

		OraclePreparedStatement stmt = null;

		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(insert_servico.toString());


			stmt.setInt(1, idCarregamento);
			stmt.setString(2, rs.getString("COD_SERVICO"));
			stmt.setString(3,  rs.getString("DESCRICAO"));
			stmt.setString(4, unidadeMedida);
			stmt.setDouble(5, rs.getDouble("PESO_CALCULO"));
			stmt.setDouble(6, rs.getDouble("TARIFA_UNIT_S_IMPOSTO"));
			stmt.setDouble(7, rs.getDouble("VALOR_SERVICO"));
			stmt.setDouble(8, rs.getDouble("ALIQUOTA_ICMS"));
			stmt.setDouble(9, rs.getDouble("VALOR_BASE_ICMS"));
			stmt.setDouble(10, rs.getDouble("VALOR_ICMS"));
			stmt.setDouble(11, rs.getDouble("ALIQUOTA_PIS"));
			stmt.setDouble(12, rs.getDouble("VALOR_BASE_PIS"));
			stmt.setDouble(13, rs.getDouble("VALOR_PIS"));
			stmt.setDouble(14, rs.getDouble("ALIQUOTA_COFINS"));
			stmt.setDouble(15, rs.getDouble("VALOR_BASE_COFINS"));
			stmt.setDouble(16, rs.getDouble("VALOR_COFINS"));
			stmt.setDouble(17, rs.getDouble("ALIQUOTA_ISS"));
			stmt.setDouble(18, rs.getDouble("VALOR_BASE_ISS"));
			stmt.setDouble(19, rs.getDouble("VALOR_ISS"));
			stmt.setString(20, rs.getString("FERROVIA_SERVICO"));

			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			stmt.close();
		}
	}

	private void inserirServicos(Connection conn, Integer idCarregamento, String serie, int numero, String versao, String unidadeMedida) throws SQLException {


		String consultar_servico = Constantes.queryConsultarServicos;

		String consultar_servico_2 = Constantes.queryConsultarServicos_2;

		String consultar_servico_3 = Constantes.queryConsultarServicos_3;

		OraclePreparedStatement stmt = null;
		OracleResultSet rs = null;
		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(consultar_servico.toString());
			stmt.setStringAtName("serie", serie);
			stmt.setIntAtName("carregamento", numero);
			stmt.setStringAtName("versao", versao);

			rs = (OracleResultSet) stmt.executeQuery();

			boolean entrou = false;

			while (rs.next()) {

				entrou = true;
				inserirServico(conn,  idCarregamento, rs, unidadeMedida);
			}

			if(!entrou)
			{
				stmt = (OraclePreparedStatement) conn.prepareStatement(consultar_servico_2.toString());
				stmt.setStringAtName("serie", serie);
				stmt.setIntAtName("carregamento", numero);
				stmt.setStringAtName("versao", versao);

				rs = (OracleResultSet) stmt.executeQuery();


				while (rs.next()) {

					entrou = true;

					inserirServico(conn,  idCarregamento, rs, unidadeMedida);

				}

			}

			if(!entrou)
			{
				stmt = (OraclePreparedStatement) conn.prepareStatement(consultar_servico_3.toString());
				stmt.setStringAtName("serie", serie);
				stmt.setIntAtName("carregamento", numero);
				stmt.setStringAtName("versao", versao);

				rs = (OracleResultSet) stmt.executeQuery();


				while (rs.next()) {

					entrou = true;

					inserirServico(conn,  idCarregamento, rs, unidadeMedida);

				}
				if(!entrou)
					log("Carregamento não extraidos :  Serie: "+ serie + " Numero : " + numero  + " Não foi encontrado registros na cte_servico", nomeExcecaoLoadSD);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			rs.close();
			stmt.close();
		}

	}

	private void obterDadosExpedidorRecebedorEstacao(Connection conn, DadosExpedidorRecebedor dadosExpedidorRecebedor,
			String codEstacaoCargaDescarga, String orgId) throws SQLException 
			{

		String cnpjClien = "";
		String cnpjEstab = "";
		String cnpjDigit = "";

		String descrLocalizacao = "";
		String unidLocalizacao = "";


		//		if(idLocalidade != null)
		//		{
		StringBuilder query_2 = new StringBuilder();

		query_2.append("    select complementoLocalFerroviario.CUDNUSEQ, complementoLocalFerroviario.CUDCDLOC, complementoLocalFerroviario.CUDEMAIL, complementoLocalFerroviario.CUDINDTO from CUDCOLFT complementoLocalFerroviario ");
		query_2.append("    where complementoLocalFerroviario.CUDNUSEQ = :codEstacaoCargaDescarga ");

		OraclePreparedStatement stmt = null;
		OracleResultSet rs_2 = null;
		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query_2.toString());
			stmt.setStringAtName("codEstacaoCargaDescarga", codEstacaoCargaDescarga);

			rs_2 = (OracleResultSet) stmt.executeQuery();

			if ( rs_2.next() ) 
			{

				StringBuilder query_est = new StringBuilder();

				query_est.append(" SELECT R3ALOCAT.R3ADESCR, R3ALOCAT.R3AUNIDF ");
				query_est.append("  FROM  R3ALOCAT R3ALOCAT ");
				query_est.append(" WHERE  R3ALOCAT.R3ACDLOC = :codEstacaoCargaDescarga ");

				OraclePreparedStatement stmt_01 = null;
				OracleResultSet rs_01 = null;
				try{

					stmt_01 = (OraclePreparedStatement) conn.prepareStatement(query_est.toString());
					stmt_01.setStringAtName("codEstacaoCargaDescarga", codEstacaoCargaDescarga);

					rs_01 = (OracleResultSet) stmt_01.executeQuery();

					if ( rs_01.next() ) 
					{
						if( rs_01.getObject( 1 ) != null ) 
						{
							descrLocalizacao = 	rs_01.getString( 1 );
							unidLocalizacao = 	rs_01.getString( 2 );

							dadosExpedidorRecebedor.setIbgeDescEstadoExpedidorRecebedor(descrLocalizacao);
							dadosExpedidorRecebedor.setIbgeCodMunicipioExpedidorRecebedor(unidLocalizacao);

						}
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}

				finally
				{
					stmt_01.close();
					rs_01.close();
				}
				StringBuilder query_ao = new StringBuilder();

				query_ao.append(" SELECT NVL(AREA_OPERACIONAL_UNICOM.EP_CGC_EMP,AREA_OPERACIONAL_UNICOM.EP_CPF_EMP) ");
				query_ao.append("  FROM AREA_OPERACIONAL_UNICOM AREA_OPERACIONAL_UNICOM, ");
				query_ao.append("       RJSUCLGV                RJSUCLGV, ");
				query_ao.append("       RIEUCLGV                RIEUCLGV ");
				query_ao.append(" WHERE AREA_OPERACIONAL_UNICOM.AO_ID_AO = RJSUCLGV.RJSNSQAO ");
				query_ao.append("   AND RIEUCLGV.RIENUSEQ = RJSUCLGV.RJSNUSEQ ");
				query_ao.append("   AND RIEUCLGV.RIENUSEQ = :codEstacaoCargaDescarga ");


				OraclePreparedStatement stmt_2 = null;
				OracleResultSet rs = null;

				try {

					stmt_2 = (OraclePreparedStatement) conn.prepareStatement(query_ao.toString());
					stmt_2.setStringAtName("codEstacaoCargaDescarga", codEstacaoCargaDescarga);

					rs = (OracleResultSet) stmt_2.executeQuery();

					if ( rs.next() ) 
					{
						if( rs.getObject( 1 ) != null ) 
						{
							cnpjClien = rs.getString( 1 ).substring(0,8);
							cnpjEstab = rs.getString( 1 ).substring(8,12); 
							cnpjDigit = rs.getString( 1 ).substring(12,14); 
						}
					}
				}


				catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
				finally
				{
					rs.close();
					stmt_2.close();
				}

				if(cnpjClien != null && cnpjEstab != null && cnpjDigit != null)
				{
					StringBuilder query_3 = new StringBuilder();

					query_3.append("    select CLI_RAZSOC RAZAO_SOCIAL_EXPEDIDOR,CLI_ENDERE ENDERECO_EXPEDIDOR,CLI_NUMCEP CEP_EXPEDIDOR,CLI_BAIRRO BAIRRO_EXPEDIDOR,CLI_CIDADE CIDADE_EXPEDIDOR,CLI_ESTADO COD_ESTADO_EXPEDIDOR, CLI_NUMERO || CLI_ESTABE || CLI_DIGITO CNPJ_EXPEDIDOR, CLI_INSEST INSC_ESTADUAL_EXPEDIDOR from hz_locations_unicom ");
					query_3.append("    where hz_locations_unicom.cli_numero = :cnpj ");
					query_3.append("    and   hz_locations_unicom.cli_estabe = :estab ");
					query_3.append("    and   hz_locations_unicom.cli_digito = :digit ");
					query_3.append("    and   hz_locations_unicom.cli_orgid  = :orgId ");

					OraclePreparedStatement stmt_3 = null;
					OracleResultSet rs_3 = null;
					try {

						stmt_3 = (OraclePreparedStatement) conn.prepareStatement(query_3.toString());
						stmt_3.setStringAtName("cnpj", cnpjClien);
						stmt_3.setStringAtName("estab", cnpjEstab);
						stmt_3.setStringAtName("digit", cnpjDigit);
						stmt_3.setStringAtName("orgId", orgId);

						rs_3 = (OracleResultSet) stmt_3.executeQuery();

						if ( rs_3.next() ) 
						{
							if( rs_3.getObject( 1 ) != null ) 
							{
								dadosExpedidorRecebedor.setRazaoSocialExpedidorRecebedor(rs_3.getString("RAZAO_SOCIAL_EXPEDIDOR"));
								dadosExpedidorRecebedor.setEnderecoExpedidorRecebedor(rs_3.getString("ENDERECO_EXPEDIDOR"));
								dadosExpedidorRecebedor.setCepExpedidorRecebedor(rs_3.getString("CEP_EXPEDIDOR"));
								dadosExpedidorRecebedor.setBairroExpedidorRecebedor(rs_3.getString("BAIRRO_EXPEDIDOR"));
								dadosExpedidorRecebedor.setCidadeExpedidorRecebedor(rs_3.getString("CIDADE_EXPEDIDOR"));
								dadosExpedidorRecebedor.setEstadoExpedidorRecebedor(rs_3.getString("COD_ESTADO_EXPEDIDOR"));
								dadosExpedidorRecebedor.setCnpjExpedidorRecebedor(rs_3.getString("CNPJ_EXPEDIDOR"));
								dadosExpedidorRecebedor.setInscrEstadualExpedidorRecebedor(rs_3.getString("INSC_ESTADUAL_EXPEDIDOR"));
							}
						}
					}
					catch (SQLException e) {
						e.printStackTrace();
						throw e;
					}
					finally
					{
						rs_3.close();
						stmt_3.close();
					}

				}

			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			rs_2.close();
			stmt.close();
		}
			}
	private void obterDadosExpedidorRecebedorTerminal(Connection conn, DadosExpedidorRecebedor dadosExpedidorRecebedor,
			String cod_terminal_carga_descarga, String orgId) throws SQLException {


		String cnpjClien = "";
		String cnpjEstab = "";
		String cnpjDigit = "";

		StringBuilder query_ao = new StringBuilder();

		query_ao.append(" SELECT NVL(AREA_OPERACIONAL_UNICOM.EP_CGC_EMP,AREA_OPERACIONAL_UNICOM.EP_CPF_EMP) ");
		query_ao.append("  FROM AREA_OPERACIONAL_UNICOM AREA_OPERACIONAL_UNICOM, ");
		query_ao.append("       RJSUCLGV                RJSUCLGV, ");
		query_ao.append("       RIEUCLGV                RIEUCLGV ");
		query_ao.append(" WHERE AREA_OPERACIONAL_UNICOM.AO_ID_AO = RJSUCLGV.RJSNSQAO ");
		query_ao.append("   AND RIEUCLGV.RIENUSEQ = RJSUCLGV.RJSNUSEQ ");
		query_ao.append("   AND RIEUCLGV.RIENUSEQ = :cod_terminal_carga_descarga ");


		OraclePreparedStatement stmt = null;
		OracleResultSet rs = null;
		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query_ao.toString());

			stmt.setStringAtName("cod_terminal_carga_descarga", cod_terminal_carga_descarga);

			rs = (OracleResultSet) stmt.executeQuery();

			if ( rs.next() ) 
			{
				if( rs.getObject( 1 ) != null ) 
				{
					cnpjClien = rs.getString( 1 ).substring(0,8);
					cnpjEstab = rs.getString( 1 ).substring(8,12); 
					cnpjDigit = rs.getString( 1 ).substring(12,14); 
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			rs.close();
			stmt.close();
		}

		if(cnpjClien != null && cnpjEstab != null && cnpjDigit != null)
		{	

			StringBuilder query_est = new StringBuilder();

			query_est.append(" SELECT R3ALOCAT.R3ADESCR, R3ALOCAT.R3AUNIDF ");
			query_est.append("  FROM  R3ALOCAT R3ALOCAT ");
			query_est.append(" WHERE  R3ALOCAT.R3ACDLOC = :cod_terminal_carga_descarga ");

			OraclePreparedStatement stmt_01 = null;
			OracleResultSet rs_01 = null;

			try {

				stmt_01 = (OraclePreparedStatement) conn.prepareStatement(query_est.toString());
				stmt_01.setStringAtName("cod_terminal_carga_descarga", cod_terminal_carga_descarga);

				rs_01 = (OracleResultSet) stmt_01.executeQuery();

				if ( rs_01.next() ) 
				{
					if( rs_01.getObject( 1 ) != null ) 
					{
						dadosExpedidorRecebedor.setIbgeDescEstadoExpedidorRecebedor(rs_01.getString( 1 ));
						dadosExpedidorRecebedor.setIbgeCodMunicipioExpedidorRecebedor(rs_01.getString( 2 ));

					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}

			finally
			{
				rs_01.close();
				stmt_01.close();
			}


			StringBuilder query_2 = new StringBuilder();

			query_2.append("    select CLI_RAZSOC RAZAO_SOCIAL_EXPEDIDOR,CLI_ENDERE ENDERECO_EXPEDIDOR,CLI_NUMCEP CEP_EXPEDIDOR,CLI_BAIRRO BAIRRO_EXPEDIDOR,CLI_CIDADE CIDADE_EXPEDIDOR,CLI_ESTADO COD_ESTADO_EXPEDIDOR, CLI_NUMERO || CLI_ESTABE || CLI_DIGITO CNPJ_EXPEDIDOR, CLI_INSEST INSC_ESTADUAL_EXPEDIDOR from hz_locations_unicom ");
			query_2.append("    where hz_locations_unicom.cli_numero = :cnpj ");
			query_2.append("    and   hz_locations_unicom.cli_estabe = :estab ");
			query_2.append("    and   hz_locations_unicom.cli_digito = :digit ");
			query_2.append("    and   hz_locations_unicom.cli_orgid  = :orgId ");

			OracleResultSet rs_2 = null;
			try {

				stmt = (OraclePreparedStatement) conn.prepareStatement(query_2.toString());
				stmt.setStringAtName("cnpj", cnpjClien);
				stmt.setStringAtName("estab", cnpjEstab);
				stmt.setStringAtName("digit", cnpjDigit);
				stmt.setStringAtName("orgId", orgId);

				rs_2 = (OracleResultSet) stmt.executeQuery();

				if ( rs_2.next() ) 
				{
					if( rs_2.getObject( 1 ) != null ) 
					{
						dadosExpedidorRecebedor.setRazaoSocialExpedidorRecebedor(rs_2.getString("RAZAO_SOCIAL_EXPEDIDOR"));
						dadosExpedidorRecebedor.setEnderecoExpedidorRecebedor(rs_2.getString("ENDERECO_EXPEDIDOR"));
						dadosExpedidorRecebedor.setCepExpedidorRecebedor(rs_2.getString("CEP_EXPEDIDOR"));
						dadosExpedidorRecebedor.setBairroExpedidorRecebedor(rs_2.getString("BAIRRO_EXPEDIDOR"));
						dadosExpedidorRecebedor.setCidadeExpedidorRecebedor(rs_2.getString("CIDADE_EXPEDIDOR"));
						dadosExpedidorRecebedor.setEstadoExpedidorRecebedor(rs_2.getString("COD_ESTADO_EXPEDIDOR"));
						dadosExpedidorRecebedor.setCnpjExpedidorRecebedor(rs_2.getString("CNPJ_EXPEDIDOR"));
						dadosExpedidorRecebedor.setInscrEstadualExpedidorRecebedor(rs_2.getString("INSC_ESTADUAL_EXPEDIDOR"));
					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			finally
			{
				rs_2.close();
				stmt.close();
			}
		}

	}
	private boolean isDocCargaValido(String serie, int numero, Long valorServicos, Connection conn) throws SQLException {

		String query = Constantes.queryIsDocCargaValida;



		OraclePreparedStatement stmt	= null;
		OracleResultSet rs 				= null;
		boolean cargaValida 			= true;

		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setString      (1, serie);
			stmt.setInt         (2, numero);

			rs = (OracleResultSet) stmt.executeQuery();

			while (rs.next()){
				Long lValorServico =  rs.getLong("VALOR_SERVICO");

				if(lValorServico != null && Math.abs(lValorServico) == valorServicos){
					cargaValida = false;
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();

		}
		finally
		{      
			rs.close();
			stmt.close();
		}
		return cargaValida;
	}

	private void carregarMapDescEstado() {

		mapaDescEstados = new HashMap<String, String>();

		mapaDescEstados.put("AC", "Acre");
		mapaDescEstados.put("AL", "Alagoas");
		mapaDescEstados.put("AP", "Amapá");
		mapaDescEstados.put("AM", "Amazonas");
		mapaDescEstados.put("BA", "Bahia");
		mapaDescEstados.put("CE", "Ceará");
		mapaDescEstados.put("ES", "Espírito Santo");
		mapaDescEstados.put("GO", "Goiás");
		mapaDescEstados.put("MA", "Maranhão");
		mapaDescEstados.put("MT", "Mato Grosso");
		mapaDescEstados.put("MS", "Mato Grosso do Sul");
		mapaDescEstados.put("MG", "Minas Gerais");
		mapaDescEstados.put("PA", "Pará");
		mapaDescEstados.put("PB", "Paraíba");
		mapaDescEstados.put("PR", "Paraná");
		mapaDescEstados.put("PE", "Pernambuco");
		mapaDescEstados.put("PI", "Piauí");
		mapaDescEstados.put("RJ", "Rio de Janeiro");
		mapaDescEstados.put("RN", "Rio Grande do Norte");
		mapaDescEstados.put("RS", "Rio Grande do Sul");
		mapaDescEstados.put("RO", "Rondônia");
		mapaDescEstados.put("RR", "Roraima");
		mapaDescEstados.put("SC", "Santa Catarina");
		mapaDescEstados.put("SP", "São Paulo");
		mapaDescEstados.put("SE", "Sergipe");
		mapaDescEstados.put("TO", "Tocantins");
		mapaDescEstados.put("DF", "Distrito Federal");
		mapaDescEstados.put("EX", "Exterior");

	}

	private void log(String linha, String nomeArquivo) {

		try {

			PrintWriter pw = new PrintWriter(new FileOutputStream(nomeArquivo, true), true);

			String data = "dd/MM/yyyy";
			String hora = "hh:mm:ss";
			String data1, hora1;

			java.util.Date agora = new java.util.Date();
			SimpleDateFormat formata = new SimpleDateFormat(data);
			data1 = formata.format(agora);
			formata = new SimpleDateFormat(hora);
			hora1 = formata.format(agora);

			pw.write(data1 + " " + hora1 + " - " + linha);
			pw.write("\r\n");
			pw.flush();
			pw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


}
