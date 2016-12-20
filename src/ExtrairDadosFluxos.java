
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

	private static String nomeLogLoadSD = "vli_FluxosHeader_SD.txt";
	private static String nomeExcecaoLoadSD	= "vli_Excecao_FluxosHeader_SD.txt";

	private static PrintWriter pwOutPut = null;
	private static PrintWriter pwException = null;

	public ExtrairDadosFluxos ()
	{
		try {

			pwOutPut = new PrintWriter(new FileOutputStream(nomeLogLoadSD, true), true);
			pwException = new PrintWriter(new FileOutputStream(nomeExcecaoLoadSD, true), true);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

	private void cosultarPrecosContratos(Connection conn, String fluxo) throws FileNotFoundException, IOException, SQLException{

		String query = Constantes.queryCosultarPrecosContratos;

		OraclePreparedStatement stmt = null;
		OracleResultSet rs			 = null;

		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setStringAtName("fluxo", fluxo);

			rs = (OracleResultSet) stmt.executeQuery();

			if(rs.getRow() > 0)
			{
				while (rs.next()) {
					try {

						inserirPrecosContratos(conn, fluxo, rs);
					}
					catch (SQLException e) {

						logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir preco de contrato - " + e.getStackTrace() , fluxo);
					}
				}
			}
			else
			{
				logExceptionWarning(TipoExcecao.WARNING, "Erro ao inserir preco de contrato - ", fluxo);
			}

		} catch (SQLException e) {
			logExceptionWarning(TipoExcecao.ERROR, "Erro ao consultar preco de contrato - " + e.getStackTrace() , fluxo);
		}
		finally
		{
			rs.close();
			stmt.close();
		}
	}
	private void inserirPrecosContratos(Connection conn, String fluxo, OracleResultSet rs) throws SQLException{
		String query = Constantes.queryInserirPrecosContratos;

		OraclePreparedStatement stmt = null;

		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setString 		(1	, rs.getString("CODIGO_CONTRATO"));
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

			stmt.execute();

		} catch (SQLException e) {

			logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir preco de contrato - " + e.getStackTrace() , fluxo);
		}
		finally
		{
			stmt.close();
		}

	}

	private void cosultarTarifasFluxos(Connection conn, String fluxo) throws FileNotFoundException, IOException, SQLException{

		String query = Constantes.queryCosultarTarifasFluxos;

		OraclePreparedStatement stmt = null;
		OracleResultSet rs			 = null;

		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setStringAtName("fluxo", fluxo);

			rs = (OracleResultSet) stmt.executeQuery();

			if(rs.getRow() > 0)
			{
				while (rs.next()) {
					try {

						inserirTarifasFluxos(conn, fluxo, rs);
					}
					catch (SQLException e) {
						logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir tarifa de fluxo - " + e.getStackTrace() , fluxo);
					}
				}

			}

			else
			{
				logExceptionWarning(TipoExcecao.WARNING, "Erro ao inserir tarifa do fluxo - ", fluxo);
			}
			
		} catch (SQLException e) {
			logExceptionWarning(TipoExcecao.ERROR, "Erro ao consultar tarifa de fluxo - " + e.getStackTrace() , fluxo);
		}
		finally
		{
			rs.close();
			stmt.close();
		}
	}

	private void inserirTarifasFluxos(Connection conn, String fluxo, OracleResultSet rs)throws SQLException{
		String query = Constantes.queryInserirTarifasFluxos;

		OraclePreparedStatement stmt = null;

		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

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
			//stmt.setString 		(14	, rs.getString("FIM_FAIXA_VOLUME"));

			stmt.execute();

		} catch (SQLException e) {

			logExceptionWarning(TipoExcecao.ERROR, "Erro ao inserir tarifa de fluxo - " + e.getStackTrace() , fluxo);
		}
		finally
		{
			stmt.close();
		}

	}

	private void cosultarFluxosHeader(Connection conn) throws FileNotFoundException, IOException, SQLException{

		String query = Constantes.queryCosultarFluxosHeader;

		OraclePreparedStatement stmt = null;
		OracleResultSet rs			 = null;

		try {

			logOutPut("INICIO DA CARGA....", null);

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			logOutPut("Iniciando a consulta de Fluxos Header....", null);

			rs = (OracleResultSet) stmt.executeQuery();

			logOutPut("Termino a consulta de Fluxos Header ....", null);

			String fluxo = null;

			while (rs.next()) {
				try {

					fluxo = rs.getString("CODIGO_FLUXO");

					inserirFluxosHeader(conn, rs);
					cosultarPrecosContratos(conn, fluxo);
					cosultarTarifasFluxos(conn, fluxo); 

				}
				catch (SQLException e) {
					logExceptionWarning(TipoExcecao.ERROR, "Erro no processamento do fluxo - " + e.getStackTrace() , fluxo);
				}
			}

			logOutPut("TERMINO DA CARGA....", null);

		} catch (SQLException e) {
			logExceptionWarning(TipoExcecao.ERROR, "Erro no processamento do fluxo - " + e.getStackTrace(), null);
		}
		finally
		{
			rs.close();
			stmt.close();
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

			logExceptionWarning(TipoExcecao.ERROR, "Erro na insercao do fluxo - " + e.getStackTrace() , rs.getString("CODIGO_FLUXO"));
		}
		finally
		{
			stmt.close();
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
