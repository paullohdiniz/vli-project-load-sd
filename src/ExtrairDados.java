
import java.io.FileNotFoundException;
import java.io.IOException;
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

	private static Map<String,String> mapaDescEstados;

	private static Integer idCarregamento;

	private final static String sequenceCarregamento = "SELECT CARREGAMENTO_HEADER_SEQ.NEXTVAL FROM DUAL ";
	private final static String sequenceVagao = "SELECT CTE_VAGAO_SEQ.NEXTVAL FROM DUAL ";



	public ExtrairDados (String[] info)
	{
		this.info_inicio = info[0].toString();
		this.info_fim    = info[1].toString();
		carregarMapDescEstado();

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

		consultar(conn);

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


			rs = (OracleResultSet) stmt.executeQuery();
			int cont = 0;


			while (rs.next()) {

				String 	dataExecuçãoResult 	= info_inicio.replace("/", "").substring(2,8);
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
					System.out.println(" Serie : "+  serie + " Numero Carregamento " + numero + " VersaoCarregamento" + versao_carregamento + " DataEmissao " + dataExecuçãoResult); 
					e.printStackTrace();
				}

				if((cont % 200) == 0){

					System.out.println("foram commitados "+ cont + " da Data :" + rs.getString("DATA_EMISSAO"));
					conn.commit();
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
			}
			else
			{
				stmt.setString  (	3	, rs.getString("SERIE_CARREGAMENTO_ASSOC"));
				stmt.setInt    (	4	, rs.getInt("NUMERO_CARREGAMENTO_ASSOC"));
			}

			stmt.setString  (	5	, rs.getString("VERSAO_CARREGAMENTO"));
			stmt.setDate    (	6	, rs.getDate  ("DATA_EMISSAO"));
			stmt.setString  (	7	, rs.getString("TIPO_CARREGAMENTO"));
			stmt.setString  (	8	, rs.getString("SITUACAO_CARREGAMENTO"));
			stmt.setString  (	9	, rs.getString("CODIGO_FLUXO"));
			stmt.setString  (	10	, rs.getString("CODIGO_ORIGEM"));
			stmt.setString  (	11	, rs.getString("CODIGO_DESTINO"));
			stmt.setString  (	12	, rs.getString("CODIGO_MERCADORIA"));
			stmt.setString  (	13	, rs.getString("RAZAO_SOCIAL_EMITENTE"));
			stmt.setString  (	14	, rs.getString("ENDERECO_EMITENTE"));
			stmt.setString  (	15	, rs.getString("CEP_EMITENTE"));
			stmt.setString  (	16	, rs.getString("BAIRRO_EMITENTE"));
			stmt.setString  (	17	, rs.getString("CIDADE_EMITENTE"));
			stmt.setString  (	18	, rs.getString("IBGE_EMITENTE"));
			stmt.setString  (	19	, rs.getString("DESC_ESTADO_EMITENTE"));
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
			stmt.setString  (	36	, rs.getString("DESC_ESTADO_REMETENTE"));
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
			stmt.setString  (	47	, rs.getString("DESC_ESTADO_DESTINATARIO"));
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
			stmt.setString  (	80	, rs.getString("DESC_ESTADO_TOMADOR"));
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
			stmt.setString  (	96	, rs.getString("DESC_ESTADO_EMISSOR"));
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

			while (rs.next()){

				inserirVagao(conn,  idCarregamento, rs, unidadeMedida, isGenerico);

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
			stmt.setInt		(5, 	rs.getInt("PESO_REAL"));
			if(unidadeMedida.equals("TO") || unidadeMedida.equals("UN") ||unidadeMedida.equals("CN"))
				stmt.setDouble		(6, 	rs.getInt("PESO_CALCULADO"));
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
			stmt.setInt		(20, 	rs.getInt("PESO_RATEADO"));
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


		OraclePreparedStatement stmt = null;
		OracleResultSet rs = null;
		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(consultar_servico.toString());
			stmt.setStringAtName("serie", serie);
			stmt.setIntAtName("carregamento", numero);
			stmt.setStringAtName("versao", versao);

			rs = (OracleResultSet) stmt.executeQuery();

			while (rs.next()) {

				inserirServico(conn,  idCarregamento, rs, unidadeMedida);


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

		StringBuilder query_ao = new StringBuilder();

		query_ao.append(" SELECT emp.ep_raz_scl RAZAO_SOCIAL_EXPEDIDOR, emp.ep_dsc_log  || ' ' || emp.ep_num_end ENDERECO_EXPEDIDOR, emp.ep_cep_emp CEP_EXPEDIDOR, emp.ep_dsc_bro BAIRRO_EXPEDIDOR, m.mn_dsc_mnc CIDADE_EXPEDIDOR, ");
		query_ao.append(" es.es_sgl_est COD_ESTADO_EXPEDIDOR,es.es_dsc_est DESC_ESTADO_EXPEDIDOR, NVL(emp.ep_cgc_emp,emp.ep_cpf_emp) CNPJ_EXPEDIDOR, replace(replace(emp.ep_ins_est,'.',''),'-','') INSC_ESTADUAL_EXPEDIDOR, emp.ep_ins_mnp INSC_MUNICIPAL_EXPEDIDOR, m.mn_cod_ibg COD_IBGE ");
		query_ao.append(" FROM rieuclgv locfer, rjsuclgv trad, EMPRESA emp, AREA_OPERACIONAL ao, municipio m, ESTADO es ");
		query_ao.append(" WHERE trad.rjsnuseq = locfer.rienuseq ");
		query_ao.append(" AND trad.rjsnsqao = ao.AO_ID_AO ");
		query_ao.append(" AND emp.EP_ID_EMP = ao.ep_id_emp_opr ");
		query_ao.append(" AND emp.mn_id_mnc = m.mn_id_mnc ");
		query_ao.append(" AND m.es_id_est = es.es_id_est ");
		query_ao.append(" AND RIENUSEQ = :codEstacaoCargaDescarga ");


		OraclePreparedStatement stmt = null;
		OracleResultSet rs = null;
		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query_ao.toString());

			stmt.setStringAtName("codEstacaoCargaDescarga", codEstacaoCargaDescarga);

			rs = (OracleResultSet) stmt.executeQuery();

			if ( rs.next() ) 
			{
				dadosExpedidorRecebedor.setRazaoSocialExpedidorRecebedor(rs.getString("RAZAO_SOCIAL_EXPEDIDOR"));
				dadosExpedidorRecebedor.setEnderecoExpedidorRecebedor(rs.getString("ENDERECO_EXPEDIDOR"));
				dadosExpedidorRecebedor.setCepExpedidorRecebedor(rs.getString("CEP_EXPEDIDOR"));
				dadosExpedidorRecebedor.setBairroExpedidorRecebedor(rs.getString("BAIRRO_EXPEDIDOR"));
				dadosExpedidorRecebedor.setCidadeExpedidorRecebedor(rs.getString("CIDADE_EXPEDIDOR"));
				dadosExpedidorRecebedor.setEstadoExpedidorRecebedor(rs.getString("COD_ESTADO_EXPEDIDOR"));
				dadosExpedidorRecebedor.setCnpjExpedidorRecebedor(rs.getString("CNPJ_EXPEDIDOR"));
				dadosExpedidorRecebedor.setInscrEstadualExpedidorRecebedor(rs.getString("INSC_ESTADUAL_EXPEDIDOR"));
				dadosExpedidorRecebedor.setInscrMunicipalExpedidorRecebedor(rs.getString("INSC_MUNICIPAL_EXPEDIDOR"));
				dadosExpedidorRecebedor.setIbgeDescEstadoExpedidorRecebedor(rs.getString("DESC_ESTADO_EXPEDIDOR"));
				dadosExpedidorRecebedor.setIbgeCodMunicipioExpedidorRecebedor(rs.getString("COD_IBGE"));

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
	private void obterDadosExpedidorRecebedorTerminal(Connection conn, DadosExpedidorRecebedor dadosExpedidorRecebedor,
			String cod_terminal_carga_descarga, String orgId) throws SQLException {


		StringBuilder query_ao = new StringBuilder();

		query_ao.append(" SELECT emp.ep_raz_scl RAZAO_SOCIAL_EXPEDIDOR, emp.ep_dsc_log  || ' ' || emp.ep_num_end ENDERECO_EXPEDIDOR, emp.ep_cep_emp CEP_EXPEDIDOR, emp.ep_dsc_bro BAIRRO_EXPEDIDOR, m.mn_dsc_mnc CIDADE_EXPEDIDOR, ");
		query_ao.append(" es.es_sgl_est COD_ESTADO_EXPEDIDOR,es.es_dsc_est DESC_ESTADO_EXPEDIDOR, NVL(emp.ep_cgc_emp,emp.ep_cpf_emp) CNPJ_EXPEDIDOR, replace(replace(emp.ep_ins_est,'.',''),'-','') INSC_ESTADUAL_EXPEDIDOR, emp.ep_ins_mnp INSC_MUNICIPAL_EXPEDIDOR, m.mn_cod_ibg COD_IBGE ");
		query_ao.append(" FROM rieuclgv locfer, rjsuclgv trad, EMPRESA emp, AREA_OPERACIONAL ao, municipio m, ESTADO es ");
		query_ao.append(" WHERE trad.rjsnuseq = locfer.rienuseq ");
		query_ao.append(" AND trad.rjsnsqao = ao.AO_ID_AO ");
		query_ao.append(" AND emp.EP_ID_EMP = ao.ep_id_emp_opr ");
		query_ao.append(" AND emp.mn_id_mnc = m.mn_id_mnc ");
		query_ao.append(" AND m.es_id_est = es.es_id_est ");
		query_ao.append(" AND RIENUSEQ = :cod_terminal_carga_descarga ");


		OraclePreparedStatement stmt = null;
		OracleResultSet rs = null;
		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query_ao.toString());

			stmt.setStringAtName("cod_terminal_carga_descarga", cod_terminal_carga_descarga);

			rs = (OracleResultSet) stmt.executeQuery();

			if ( rs.next() ) 
			{
				
				dadosExpedidorRecebedor.setRazaoSocialExpedidorRecebedor(rs.getString("RAZAO_SOCIAL_EXPEDIDOR"));
				dadosExpedidorRecebedor.setEnderecoExpedidorRecebedor(rs.getString("ENDERECO_EXPEDIDOR"));
				dadosExpedidorRecebedor.setCepExpedidorRecebedor(rs.getString("CEP_EXPEDIDOR"));
				dadosExpedidorRecebedor.setBairroExpedidorRecebedor(rs.getString("BAIRRO_EXPEDIDOR"));
				dadosExpedidorRecebedor.setCidadeExpedidorRecebedor(rs.getString("CIDADE_EXPEDIDOR"));
				dadosExpedidorRecebedor.setEstadoExpedidorRecebedor(rs.getString("COD_ESTADO_EXPEDIDOR"));
				dadosExpedidorRecebedor.setCnpjExpedidorRecebedor(rs.getString("CNPJ_EXPEDIDOR"));
				dadosExpedidorRecebedor.setInscrEstadualExpedidorRecebedor(rs.getString("INSC_ESTADUAL_EXPEDIDOR"));
				dadosExpedidorRecebedor.setInscrMunicipalExpedidorRecebedor(rs.getString("INSC_MUNICIPAL_EXPEDIDOR"));
				dadosExpedidorRecebedor.setIbgeDescEstadoExpedidorRecebedor(rs.getString("DESC_ESTADO_EXPEDIDOR"));
				dadosExpedidorRecebedor.setIbgeCodMunicipioExpedidorRecebedor(rs.getString("COD_IBGE"));

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

		mapaDescEstados.put("MG","MINAS GERAIS");
		mapaDescEstados.put("BA","BAHIA");
		mapaDescEstados.put("GO","GOIÁS");
		mapaDescEstados.put("ES","ESPÍRITO SANTO");
		mapaDescEstados.put("MT","MATO GROSSO");
		mapaDescEstados.put("SP","SÃO PAULO");
		mapaDescEstados.put("MA", "MARANHÃO");
		mapaDescEstados.put("MS", "MATO GROSSO DO SUL");
		//		mapaDescEstados.put("", "");
		//		mapaDescEstados.put("", "");
		//		mapaDescEstados.put("", "");
		//		mapaDescEstados.put("", "");
		//		mapaDescEstados.put("", "");
		//		


	}


}
