package vecinos;

import java.io.IOException;

public class Main_movielends {

	public static void main(String[] args) throws IOException{
		if(args.length!=6){System.out.println("Debe ingresar: Diccionario, Rantings,Links, Directorio de trabajo,extract y número de películas."); System.exit(-1);}
		String Diccionario = args[0];
		String Rantigs = args[1];
		String Links = args[2];
		String directorio = args[3];
		String result = args[4];
		Integer cantidad = Integer.valueOf(args[5]);
		Extract_MovieLends extractor = new Extract_MovieLends(Diccionario,Links,directorio);
		extractor.Extract(cantidad);
		String enlace = directorio + "/"+"enlace.txt";
		FilterMovieLends filtro = new FilterMovieLends(Rantigs,enlace,directorio);
		filtro.filter();
		Filter_extract filtro1= new Filter_extract(result,enlace,directorio);
		filtro1.filter();
		
	}
}
