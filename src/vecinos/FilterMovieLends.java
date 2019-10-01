package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

/**
 * Clase para extraer todas las calificaciones de la películas seleccionadas. 
 * @author marco
 *
 */
public class FilterMovieLends {
	protected String File_rankings;
	protected String File_enlace;
	protected String Directorio;
	protected Set<String> pelis;
	/**
	 * 
	 * @param rankings: Archivo con las calificaciones de movielends.
	 * @param enlace: Archivo para enlazar las base de datos.
	 * @param direct:  Directorio a guardar el filtro.
	 */
	public FilterMovieLends(String rankings, String enlace,String direct){
		this.File_rankings= rankings;
		this.File_enlace= enlace;
		this.Directorio = direct;
		this.pelis= new TreeSet<String>();
	}
	
	public void filter() throws IOException{
		File enlace = new File(this.File_enlace);
		FileReader fr = new FileReader(enlace);
		BufferedReader br = new BufferedReader(fr);
		String linea;
		while((linea=br.readLine())!=null){
			String[] data = linea.split(",");
			this.pelis.add(data[2]);
		}
		br.close();
		fr.close();
		File ranking = new File(this.File_rankings);
		fr = new FileReader(ranking);
		br = new BufferedReader(fr);
		FileWriter archivo_out = new FileWriter(this.Directorio+"/"+"rankings_filter.txt");
		PrintWriter  pw = new PrintWriter(archivo_out);
		while((linea=br.readLine())!=null){
			String[] data = linea.split(",");
			String movie = data[1];
			if(this.pelis.contains(movie)){
				pw.println(linea);
			}
		}
		pw.close();
		br.close();
	}

}
