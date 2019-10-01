package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


import scala.Tuple3;
/**
 * Clase crea un archivo con las llaves de wikidata,movielend y imdb.
 * @author marco
 *
 */
public class Extract_MovieLends {
	protected String file_Dicc;
	protected String file_links;
	protected String directory;
	protected HashMap<String,String> link_objects; 
	protected ArrayList<Tuple3<String,String,String>> finall;
	/**
	 * 
	 * @param Dicc: Diccionario de wikidadta a imdb
	 * @param links:  Archivo de movielends con imdb.
	 * @param Ranting: Archivo de movielends con calificaciones.
	 * @param director: Directorio a guardar info. 
	 */
	public Extract_MovieLends(String Dicc, String links, String director){
		this.file_Dicc = Dicc;
		this.file_links = links;
		this.directory = director;
		this.link_objects = new HashMap<String,String>();
		this.finall = new ArrayList<Tuple3<String,String,String>>();
	}
	/**
	 * Extraer un cantidad especifíca de películas.
	 * @param num: numero de películas
	 * @throws IOException: Errores al abrir el archivos.
	 */
	public void Extract(Integer num) throws IOException{
		File links = new File(this.file_links);
		FileReader fr = new FileReader(links);
		BufferedReader br = new BufferedReader(fr);
		Integer index = 0;
		String linea;
		while((linea=br.readLine())!=null){
			if(index==0){index=index+1;
				continue;
			}
			String[] data = linea.split(",");
			this.link_objects.put("tt"+data[1], data[0]);
			
		}
		br.close();
		fr.close();
		index =  (int) (Math.random()*1000 +1);
		File Dicc = new File(this.file_Dicc);
		fr = new FileReader(Dicc);
		br = new BufferedReader(fr);
		Integer count =0;
		while((linea=br.readLine())!=null){
			index = index-1;
			if(index<=0){
				String[] data = linea.split(",");
				String imd = data[1];
				if(this.link_objects.keySet().contains(imd)){
					 this.finall.add(new Tuple3<String,String,String>(imd,data[0],this.link_objects.get(imd)));
					 count = count+1;
					 if(count>= num){break;}
					 index =  (int) (Math.random()*1000 +1);
				}
			}
		}
		br.close();
		fr.close();
		FileWriter archivo_out = new FileWriter(this.directory+"/"+"enlace.txt");
		PrintWriter  pw = new PrintWriter(archivo_out);
		for (Tuple3<String,String,String> triple : this.finall){
			String newline = triple._1()+","+ triple._2()+","+triple._3();
			pw.println(newline);
		}
		pw.close();
	}
}
