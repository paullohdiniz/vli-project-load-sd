
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;



public class ExtrairDadosFluxos {

	public enum TipoExcecao {
		ERROR, WARNING;
	}

	String  info_inicio;
	String	info_fim;

	private static String nomeLogLoadSD = "vli_FluxosHeader_SD_";
	private static String nomeExcecaoLoadSD	= "vli_Excecao_FluxosHeader_SD_";

	private static PrintWriter pwOutPut = null;
	private static PrintWriter pwException = null;

	public ExtrairDadosFluxos (String[] info)
	{
		try {

			this.info_inicio = info[1].toString();
			this.info_fim    = info[2].toString();

			String sufixoArquivo = this.info_fim.replace("/", "_") + ".txt";

			pwOutPut 	= new PrintWriter(new FileOutputStream(nomeLogLoadSD + sufixoArquivo , true), true);
			pwException = new PrintWriter(new FileOutputStream(nomeExcecaoLoadSD + sufixoArquivo, true), true);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

	private void cosultarPrecosContratos(Connection conn, String contrato, String fluxo) throws FileNotFoundException, IOException, SQLException{

		String query = Constantes.queryCosultarPrecosContratos;

		OraclePreparedStatement stmtInsert	= null;
		OraclePreparedStatement stmt 		= null;
		OracleResultSet rs			 		= null;

		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setStringAtName("fluxo", fluxo);

			rs = (OracleResultSet) stmt.executeQuery();

			boolean entrou = false;

			stmtInsert = (OraclePreparedStatement) conn.prepareStatement(Constantes.queryInserirPrecosContratos);

			while (rs.next()) {
				try {

					entrou = true;

					inserirPrecosContratos(stmtInsert, conn, contrato, fluxo, rs);
				}
				catch (SQLException e) {

					logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir preco de contrato - " + e.getMessage() , fluxo);
				}
			}

			stmtInsert.executeBatch();


			if(!entrou)
			{
				logExceptionWarning(TipoExcecao.WARNING, "Não retornou dados preco de contrato - ", fluxo);
			}


		} catch (SQLException e) {
			logExceptionWarning(TipoExcecao.ERROR, "Erro ao consultar preco de contrato - " + e.getMessage() , fluxo);
		}
		finally
		{
			try{
				if(stmtInsert != null){
					stmtInsert.close();
				}
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
			} catch (SQLException e) {
				System.out.println("Erro ao inserir preco de contrato : contrato  " + contrato + " fluxo : " + fluxo);
				logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir preco de contrato : contrato  " + contrato + " fluxo : " + fluxo , fluxo);
			}
		}
	}
	private void inserirPrecosContratos(OraclePreparedStatement stmt, Connection conn, String contrato, String fluxo, OracleResultSet rs) throws SQLException{
		//String query = Constantes.queryInserirPrecosContratos;

		//OraclePreparedStatement stmt = null;

		try{

			//stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setString 		(1	, contrato);
			stmt.setInt 		(2	, rs.getInt("ITEM_CONTRATO"));
			stmt.setString 		(3	, rs.getString("TIPO_PESO"));
			stmt.setString 		(4	, rs.getString("COD_SERVICO"));
			stmt.setString 		(5	, rs.getString("DESCRICAO_SERVICO"));
			stmt.setDate 		(6	, rs.getDate("INICIO_VIGENCIA"));
			stmt.setDate 		(7	, rs.getDate("FIM_VIGENCIA"));
			stmt.setString 		(8	, rs.getString("SERIE_VAGAO"));
			stmt.setString 		(9	, rs.getString("VAGAO_DE"));
			stmt.setString 		(10	, rs.getString("VAGAO_ATE"));
			stmt.setDouble 		(11	, rs.getDouble("VALOR"));
			stmt.setDouble 		(12	, rs.getDouble("PESO_MINIMO_FIXO"));
			stmt.setDouble 		(13	, rs.getDouble("PESO_MAXIMO"));
			stmt.setString 		(14	, rs.getString("UNIDADE"));
			stmt.setString 		(15	, rs.getString("INICIO_ESCALA"));
			stmt.setString 		(16	, rs.getString("FIM_ESCALA"));
			stmt.setString 		(17	, rs.getString("INICIO_FAIXA_VOLUME"));
			stmt.setString 		(18	, rs.getString("FIM_FAIXA_VOLUME"));
			stmt.setString 		(19	, rs.getString("CODIGO_FLUXO"));
			stmt.setDate 		(20	, rs.getDate("DATA_CRIACAO"));

			stmt.addBatch();

		} catch (SQLException e) {

			logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir preco de contrato - " + e.getMessage() , fluxo);
		}
		finally
		{
			//stmt.close();
		}

	}

	private void cosultarTarifasFluxos(Connection conn, String fluxo) throws FileNotFoundException, IOException, SQLException{

		String query = Constantes.queryCosultarTarifasFluxos;

		OraclePreparedStatement stmtInsert = null;
		OraclePreparedStatement stmt = null;
		OracleResultSet rs			 = null;

		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setStringAtName("fluxo", fluxo);

			rs = (OracleResultSet) stmt.executeQuery();

			boolean entrou = false;

			stmtInsert = (OraclePreparedStatement) conn.prepareStatement(Constantes.queryInserirTarifasFluxos);

			while (rs.next()) {
				try {

					entrou = true;

					inserirTarifasFluxos(stmtInsert, conn, fluxo, rs);
				}
				catch (SQLException e) {
					logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir tarifa de fluxo - " + e.getMessage() , fluxo);
				}
			}

			stmtInsert.executeBatch();

			if(!entrou)
			{
				logExceptionWarning(TipoExcecao.WARNING, "Não retornou dados tarifa do fluxo - ", fluxo);
			}


		} catch (SQLException e) {
			logExceptionWarning(TipoExcecao.ERROR, "Erro ao consultar tarifa de fluxo - " + e.getMessage() , fluxo);
		}
		finally
		{
			if(stmtInsert != null){
				stmtInsert.close();
			}
			if(rs != null){
				rs.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}

	private void inserirTarifasFluxos(OraclePreparedStatement stmt, Connection conn, String fluxo, OracleResultSet rs)throws SQLException{
		//String query = Constantes.queryInserirTarifasFluxos;

		//OraclePreparedStatement stmt = null;

		try{

			//stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setString 		(1	, rs.getString("CODIGO_FLUXO"));
			stmt.setString 		(2	, rs.getString("TIPO_PESO"));
			stmt.setString 		(3	, rs.getString("CODIGO_SERVICO"));
			stmt.setString 		(4	, rs.getString("DESCRICAO_SERVICO"));
			stmt.setDate 		(5	, rs.getDate("INICIO_VIGENCIA"));
			stmt.setDate 		(6	, rs.getDate("FIM_VIGENCIA"));
			stmt.setDouble 		(7	, rs.getDouble("VALOR"));
			stmt.setDouble 		(8	, rs.getDouble("PESO_MINIMO_FIXO"));
			stmt.setString 		(9	, rs.getString("UNIDADE"));
			stmt.setString 		(10	, rs.getString("INICIO_ESCALA"));
			stmt.setString 		(11	, rs.getString("FIM_ESCALA"));
			stmt.setString 		(12	, rs.getString("INICIO_FAIXA_VOLUME"));
			stmt.setString 		(13	, rs.getString("FIM_FAIXA_VOLUME"));
			stmt.setDate 		(14	, rs.getDate("DATA_CRIACAO"));
			stmt.setString 		(15	, rs.getString("COD_FERROVIA_SERVICO"));

			//stmt.execute();
			stmt.addBatch();

		} catch (SQLException e) {

			logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir tarifa de fluxo - " + e.getMessage() , fluxo);
		}
		finally
		{
			//stmt.close();
		}

	}

	private void cosultarFluxosHeader(Connection conn) throws FileNotFoundException, IOException, SQLException{

		String query = Constantes.queryCosultarFluxosHeader;

		OraclePreparedStatement stmt = null;
		OracleResultSet rs			 = null;

		String fluxo = null;
		String contrato = null;

		try {

			logOutPut("INICIO DA CARGA....", null);

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setStringAtName("dtInicio", info_inicio);
			stmt.setStringAtName("dtFim", info_fim);

			logOutPut("Iniciando a consulta de Fluxos Header....", null);

			rs = (OracleResultSet) stmt.executeQuery();

			logOutPut("Termino a consulta de Fluxos Header ....", null);


			while (rs.next()) {
				try {

					contrato = 	rs.getString("COD_CONTRATO");
					fluxo = 	rs.getString("CODIGO_FLUXO");

					inserirFluxosHeader(conn, rs);
					cosultarPrecosContratos(conn, contrato, fluxo);
					cosultarTarifasFluxos(conn, fluxo); 

				}
				catch (SQLException e) {
					logExceptionWarning(TipoExcecao.ERROR, "Erro no processamento do fluxo - " + e.getMessage() , fluxo);
				}
			}

			logOutPut("TERMINO DA CARGA....", null);

		} catch (SQLException e) {
			logExceptionWarning(TipoExcecao.ERROR, "Erro no processamento do fluxo - " + e.getMessage(), null);
		}
		finally
		{
			try{
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}

			} catch (SQLException e) {
				System.out.println("Erro  Fluxo : " + fluxo + " Contrato : " + contrato);
			}

		}
	}

	private void inserirFluxosHeader(Connection conn, OracleResultSet rs)throws SQLException{

		String query = Constantes.queryInserirFluxosHeader;

		OraclePreparedStatement stmt = null;

		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setString 		(1	, rs.getString("CODIGO_FERROVIA"));
			stmt.setString  	(2	, rs.getString("COD_CONTRATO"));
			stmt.setString  	(3	, rs.getString("CODIGO_FLUXO"));
			stmt.setString  	(4	, rs.getString("SITUACAO"));
			stmt.setString  	(5	, rs.getString("COD_ORIGEM"));
			stmt.setString  	(6	, rs.getString("COD_DESTINO"));
			stmt.setString  	(7	, rs.getString("COD_TERMINAL_CARGA"));
			stmt.setString  	(8	, rs.getString("COD_TERMINAL_DESCARGA"));
			stmt.setDate    	(9	, rs.getDate  ("INICIO_VIGENCIA"));
			stmt.setDate  		(10	, rs.getDate("FIM_VIGENCIA"));
			stmt.setString  	(11	, rs.getString("CNPJ_CLIENTE"));
			stmt.setString  	(12	, rs.getString("COD_NOP"));
			stmt.setString  	(13	, rs.getString("CODIGO_GRUPOFLUXO"));
			stmt.setString  	(14	, rs.getString("COD_ROTA"));
			stmt.setString  	(15	, rs.getString("TIPO_SERVICO"));
			stmt.setString  	(16	, rs.getString("AGRUPAMENTO"));
			stmt.setString  	(17	, rs.getString("CNPJ_EMISSOR"));
			stmt.setString  	(18	, rs.getString("EXPORTACAO"));
			stmt.setString  	(19	, rs.getString("COD_MERCADORIA_LEGADO"));
			stmt.setString  	(20	, rs.getString("CODIGO_ANTT"));
			stmt.setString  	(21	, rs.getString("CONDICAO_PAGAMENTO"));
			stmt.setString  	(22	, rs.getString("COD_PRODUTO"));
			stmt.setString  	(23	, rs.getString("CU2INDVO"));
			stmt.setString  	(24	, rs.getString("CU2INDTA"));

			stmt.execute();

			logOutPut("Fluxo inserido - ", rs.getString("CODIGO_FLUXO"));

		} catch (SQLException e) {

			logExceptionWarning(TipoExcecao.ERROR, "Erro na insercao do fluxo - " + e.getMessage() , rs.getString("CODIGO_FLUXO"));
		}
		finally
		{
			if(stmt != null){
				stmt.close();
			}
		}
	}

	public void consultarFluxos() throws FileNotFoundException, IOException, SQLException
	{	
		Connection conn = null;
		try {

			conn = OracleConnection.getConnection();


		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		cosultarFluxosHeader(conn);

		pwOutPut.flush();
		pwOutPut.close();

		pwException.flush();
		pwException.close();

		if (!conn.isClosed())
		{	
			conn.close();
		}
	}




	private void logOutPut(String linha, String fluxo) {

		String data = "dd/MM/yyyy";
		String hora = "hh:mm:ss";
		String data1, hora1;

		java.util.Date agora = new java.util.Date();
		SimpleDateFormat formata = new SimpleDateFormat(data);
		data1 = formata.format(agora);
		formata = new SimpleDateFormat(hora);
		hora1 = formata.format(agora);

		if(fluxo != null)
			pwOutPut.write(data1 + " " + hora1 + " - [INFO] " + linha + " - " + fluxo);
		else
			pwOutPut.write(data1 + " " + hora1 + " - [INFO] " + linha);

		pwOutPut.write("\r\n");


	}
	private void logExceptionWarning(Enum tipoExcecao, String linha, String fluxo) {

		String data = "dd/MM/yyyy";
		String hora = "hh:mm:ss";
		String data1, hora1;

		java.util.Date agora = new java.util.Date();
		SimpleDateFormat formata = new SimpleDateFormat(data);
		data1 = formata.format(agora);
		formata = new SimpleDateFormat(hora);
		hora1 = formata.format(agora);

		if(fluxo != null)
			pwException.write(data1 + " " + hora1 + " - [" + tipoExcecao.name() + "] " + linha + " - " + fluxo);
		else
			pwException.write(data1 + " " + hora1 + " - [" + tipoExcecao.name() + "] " + linha);

		pwException.write("\r\n");

	}

}
