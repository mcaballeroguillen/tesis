package vecinos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class Prueba {

 public static void main(String[] args) throws IOException{
	 InputStream in = null;
	 ArrayList<Node[]> movies = new ArrayList<Node[]>();
	 in = new FileInputStream("/u/m/mag/2017/mcaballe/Descargas/wikidata-20181123-truthy-BETA.nt.gz");
	 in = new GZIPInputStream(in);
	 NxParser nxp = new NxParser();
	 nxp.parse(in);
	 
	 System.out.println("Vamos a ver");
	 for (Node[] nx : nxp){
		 if(nx[1].equals("<http://www.wikidata.org/prop/direct/P31>") ){
			 movies.add(nx);
		 }
	
	 }
	 System.out.println(movies.size()); 	  
 }
	
}
