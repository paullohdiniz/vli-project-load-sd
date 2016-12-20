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

		//try {
			
			extrairDadosFluxos();
			//lerArquivo("311;8463");
			//100 840160 145812
			//102 7256
			//200 306919 NOK 302394
			//311 8844 NOK
			//005 64446 64594

		//} catch (ClassNotFoundException e) {
			
		//	e.printStackTrace();
		//}

/*		for(int i = 0; i < args.length; i++) {

			System.out.println(args[i]);

			String intervaloData = args[i];

			try {

				lerArquivo(intervaloData);

			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}

		}*/

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

	
	private static void extrairDadosFluxos() throws FileNotFoundException, IOException {


		ExtrairDadosFluxos extrair = new ExtrairDadosFluxos ();

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

			if(info.length == 2) {

				extrairDadosUnicom(info);
			}
		}


	}
}


