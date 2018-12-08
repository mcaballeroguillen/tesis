package vecinos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class Prueba {

 public static void main(String[] args) throws IOException{
	 InputStream in = null;
	 in = new FileInputStream("/home/marco/WIKIDATA/wikidata-20181123-truthy-BETA.nt.gz");
	 in = new GZIPInputStream(in);
	 NxParser nxp = new NxParser();
	 nxp.parse(in);
	 int x=0;
	 for (Node[] nx : nxp)
		  // prints the subject, eg. <http://example.org/>
		  System.out.println(nx[0]);
	      if(x==0){return;}
	      x=x+1;
	 
 }
	
}
