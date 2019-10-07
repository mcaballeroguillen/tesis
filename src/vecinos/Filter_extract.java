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
 * Clase que extrae todas los triples (s,p,o) de las películas seleccionadas.
 * @author marco
 *
 */
public class Filter_extract {
	protected String extract;
	protected String enlace;
	protected String directorio;
	protected Set<String> pelis;
	/**
	 * 
	 * @param extract: Archivo resultante de buscar tuplas de un tipo especifíco.
	 * @param enlace: Archivo para elazar las bases de datos.
	 * @param direc: Directorio de trabajo.
	 */
	public Filter_extract(String extract,String enlace,String direc){
		this.extract=extract;
		this.enlace= enlace;
		this.directorio = direc;
		this.pelis = new TreeSet<String>();
	}
	
	public void filter() throws IOException{
		File enlace = new File(this.enlace);
		FileReader fr = new FileReader(enlace);
		BufferedReader br = new BufferedReader(fr);
		String linea;
		while((linea=br.readLine())!=null){
			String[] data = linea.split(",");
			this.pelis.add(data[1]);
		}
		br.close();
		fr.close();
		File extract = new File(this.extract);
		fr = new FileReader(extract);
		br = new BufferedReader(fr);
		FileWriter archivo_out = new FileWriter(this.directorio+"/"+"extract_filter.txt");
		PrintWriter  pw = new PrintWriter(archivo_out);
		while((linea=br.readLine())!=null){
			String[] data = linea.split("\t");
			String link_sujeto = data[0];
			link_sujeto =link_sujeto.replaceAll("[()]","");
			link_sujeto =link_sujeto.replaceAll(">", "");
			String pelicula = link_sujeto.split("/")[4];
			if(this.pelis.contains(pelicula)){
				pw.println(linea);
			}
		pw.close();
		br.close();
		}
	}

}
