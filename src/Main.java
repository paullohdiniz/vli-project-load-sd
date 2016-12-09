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

//		try {
//
//			lerArquivo("01/01/2016;01/01/2016");
//
//		} catch (ClassNotFoundException e) {
//			
//			e.printStackTrace();
//		}

		for(int i = 0; i < args.length; i++) {

			System.out.println(args[i]);

			String intervaloData = args[i];

			try {

				lerArquivo(intervaloData);

			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}

		}

	}
	private static void extrairDadosUnicom(String[] info) throws FileNotFoundException, IOException {


		ExtrairDados extrair = new ExtrairDados (info);

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

		System.out.println( new SimpleDateFormat( "HH:mm:ss" ).format(calendar.getTime()));

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


	public static long getPegaDataAtual(){

		// data = new Date(System.currentTimeMillis());  
		Calendar calendar = Calendar.getInstance();

		return calendar.getTimeInMillis();
	}

	public static Date getDiferencaData(long dtInicial, long dtFinal) {
		//Subtraindo os milisegundos, para obter a diferença
		long milisecondResult = dtFinal - dtInicial;
		return new Date(milisecondResult);
	}

	public static Date getDiferencaData(Date dtInicial, Date dtFinal) {
		//Convertendo as datas para milisegundos
		long milisecondBegin = dtInicial.getTime();
		long milisecondEnd = dtFinal.getTime();
		//Subtraindo os milisegundos, para obter a diferença
		long milisecondResult = milisecondBegin - milisecondEnd;
		return new Date(milisecondResult);
	}



}


