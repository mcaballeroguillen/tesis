package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
/**
 * Clase para convertir el resultado de simcoseno a imdb
 * @author Marco Caballero
 *
 */
public class Movielend_convert {
	private HashMap<String,String>  links = new HashMap<String,String>();
	private String Link_file ;
	private String simcoseno_file;
	private String directorio;
	/**
	 * 
	 * @param link: Archivo de Movilends con los imdb a cada película.
	 * @param simcoseno: Archivo final de calcular simcoseno.
	 * @param direc: Directorio de trabajo. 
	 */
	public Movielend_convert(String link,String simcoseno, String direc){
		this.Link_file = link;
		this.simcoseno_file= simcoseno;
		this.directorio= direc;
		
		
	}
	
	public void convert() throws IOException{
		File Linkfile = new File(this.Link_file);
	    FileReader fr = new FileReader(Linkfile);
	    BufferedReader br = new BufferedReader(fr);
	    String linea;
	    Integer cont=0;
	    while((linea=br.readLine())!=null){
	    	if(cont==0){cont=cont+1;continue;}
	    	String[] datos = linea.split(",");
	    	this.links.put(datos[0], datos[1]);
	    }
	    br.close();
	    File simcoseno= new File(this.simcoseno_file);
	    fr = new FileReader(simcoseno);
	    br = new BufferedReader(fr);
	    FileWriter archivo_out = new FileWriter(this.directorio +"/simcoseno_imdb.txt");
        PrintWriter pw = new PrintWriter(archivo_out);
        while((linea=br.readLine())!=null){
        	linea = linea.replaceAll("[()]","");
        	String[] datos = linea.split(",");
        	String value = datos[0];
        	String[] sujetos = datos[1].split("##");
        	String s1 = sujetos[0];
        	String s2 = sujetos[1];
        	String s1imdb= this.links.get(s1);
        	String s2imdb= this.links.get(s2);
        	String new_line = value +","+ s1imdb +"##" + s2imdb;
        	pw.println(new_line);
        };
        pw.close();
        br.close();
	}
}
