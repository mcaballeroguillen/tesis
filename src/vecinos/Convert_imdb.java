package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
/**
 * Clase para covertir imdb a wikidata
 * @author Marco Caballero
 *
 */
public class Convert_imdb {
	private HashMap<String,String> dicc = new HashMap<String,String>();
	private String dicc_file;
	private String sim_imdb_file;
	private String Directiorio;
	/**
	 * 
	 * @param diccionario: Diccionario de wikidata a imdb.
	 * @param file_imdb: Archivo de simcoseno con imdb
	 * @param direc: Directorio
	 */
	public Convert_imdb(String diccionario, String file_imdb, String direc){
		this.dicc_file= diccionario;
		this.sim_imdb_file = file_imdb;
		this.Directiorio=direc;
	}
	
	public void convert() throws IOException{
		File Dicc = new File(this.dicc_file);
	    FileReader fr = new FileReader(Dicc);
	    BufferedReader br = new BufferedReader(fr);
	    String linea;
	    Integer cont=0;
	    while((linea=br.readLine())!=null){
	    	if(cont==0){cont=cont+1;continue;}
	    	String[] datos = linea.split(",");
	    	String wikidata_link = datos[0];
	    	String imdb_id = datos[1];
	    	String wikidata_id = wikidata_link.split("/")[4];
	    	this.dicc.put(imdb_id, wikidata_id);
	    }
	    br.close();
	    File simcoseno= new File(this.sim_imdb_file);
	    fr = new FileReader(simcoseno);
	    br = new BufferedReader(fr);
	    FileWriter archivo_out = new FileWriter(this.Directiorio +"/simcoseno_wiki.txt");
        PrintWriter pw = new PrintWriter(archivo_out);
        while((linea=br.readLine())!=null){
        	String[] data = linea.split(",");
        	String value = data[0];
        	String[] sujetos = data[1].split("##");
        	String s1_imdb= "tt" + sujetos[0];
        	String s2_imdb= "tt" + sujetos[1];
        	String new_line = value + ","+ this.dicc.get(s1_imdb)+"##"+ this.dicc.get(s2_imdb);
        	pw.println(new_line);
        }
        pw.close();
        br.close();
	}
}
