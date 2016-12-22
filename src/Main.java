import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {

		//String intervaloData = "FLUXOS;01/01/2016;31/01/2016";

		try {

			//lerArquivo("FLUXOS;01/01/2016;31/12/2016");
			if(args != null){
				lerArquivo(args[0]);
			}
			else{
				System.out.println("nenhum registro foi extraido...");
			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}



	}
	private static void extrairDadosUnicom(String[] info) throws FileNotFoundException, IOException {


		/*ExtrairDados extrair = new ExtrairDados (info);

		Date dt1 = new Date();

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
		String dateInicio = df.format(dt1);
		System.out.println(dateInicio);
		try {
			extrair.consultarCarregamento();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		Date dt2 = new Date();
		String dateFinal = df.format(dt2);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dt2.getTime() - dt1.getTime());

		System.out.println(dateInicio);
		System.out.println(dateFinal);

		//System.out.println( new SimpleDateFormat( "HH:mm:ss" ).format(calendar.getTime()));

		try {
			OracleConnection.closeConnection();
		} catch (SQLException e) {

			e.printStackTrace();
		}*/
	}


	private static void extrairDadosFluxos(String[] info) throws FileNotFoundException, IOException {


		ExtrairDadosFluxos extrair = new ExtrairDadosFluxos (info);

		Date dt1 = new Date();

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
		String dateInicio = df.format(dt1);
		System.out.println(dateInicio);

		try {
			extrair.consultarFluxos();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		Date dt2 = new Date();
		String dateFinal = df.format(dt2);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dt2.getTime() - dt1.getTime());

		System.out.println(dateInicio);
		System.out.println(dateFinal);

		//System.out.println( new SimpleDateFormat( "HH:mm:ss" ).format(calendar.getTime()));

		try {
			OracleConnection.closeConnection();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}


	private static void lerArquivo(String arquivo) throws ClassNotFoundException, FileNotFoundException, IOException {


		if(!arquivo.toString().equals("")) {

			String[] info = arquivo.split(";");

			if(info[0].equals("ROTAS")){
				//ExtrairDadosRotas dadosRotas = new ExtrairDadosRotas(); 
				//dadosRotas.ExtrairRotas();
			}
			if(info[0].equals("FLUXOS")) {

				extrairDadosFluxos(info);
			}
			if(info.length == 2) {

				extrairDadosCte(info);
			}
		}


	}
	private static void extrairDadosCte(String[] info) throws FileNotFoundException, IOException {

		ExtrairDadosCarregamentoCte extrair = new ExtrairDadosCarregamentoCte (info);

		Date dt1 = new Date();

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
		String dateInicio = df.format(dt1);
		System.out.println(dateInicio);

		try {
			extrair.consultarCarregamento();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		Date dt2 = new Date();
		String dateFinal = df.format(dt2);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dt2.getTime() - dt1.getTime());

		System.out.println(dateInicio);
		System.out.println(dateFinal);

		//System.out.println( new SimpleDateFormat( "HH:mm:ss" ).format(calendar.getTime()));

		try {
			OracleConnection.closeConnection();
		} catch (SQLException e) {

			e.printStackTrace();
		}


	}
}


