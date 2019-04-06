package vecinos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;
/**
 * 
 * @author Marco Caballero
 * 
 *Class that extracts tuples of a specific type
 */
public class Extract {
	protected Set<String> objects; 
	protected String File;
	int limit;
	/**
	 * 
	 * @param FileIn: Entry file
	 * @param limit: Limit of the search
	 */
	public Extract(String FileIn, int limit){
		objects = new TreeSet<>();
		this.File=FileIn;
		this.limit = limit;
	}
	
	/**
	 * Method that looks for tuples with a type within the limit
	 * @param OutFile: File where the results will be saved  
	 * @param ObjectType: Class or type to be searched
	 * @throws IOException: File handling exceptions
	 */
	public void FindObjectes (String OutFile,String ObjectType) throws IOException{
		int teplimit=this.limit;
		InputStream in = null;
		in = new FileInputStream(this.File);
		in = new GZIPInputStream(in);
		NxParser nxp = new NxParser();
		nxp.parse(in);
		
		for (Node[] nx : nxp){
			if(this.limit>0){   //If the limit is 0,  means that  should search in all wikidata.
				teplimit--;
				if(teplimit<=0){break;}
			}
			if(nx[1].toString().equals("<http://www.wikidata.org/prop/direct/P31>") && nx[2].toString().equals(ObjectType)){
				this.objects.add(nx[0].toString());
			}
			
		}
		in.close();
		this.writeFile(OutFile);
	}
    
	/**
	 * 
	 * @param OutFile: File where the tuples will be saved, with a format  (s \t p \t o)
	 * @throws IOException: File handling exceptions
	 */
	private void writeFile(String OutFile) throws IOException {
		int templimit = this.limit;
		InputStream in = null;
		FileOutputStream out = null;
		in = new FileInputStream(this.File);
		out = new  FileOutputStream(OutFile);
		in = new GZIPInputStream(in);
		NxParser nxp = new NxParser();
		nxp.parse(in);
		
		for (Node[] nx : nxp){
			if(this.limit>0){ //If the limit is 0,  means that  should search in all wikidata
				templimit--;
				if(templimit<=0){break;}
			}
			if(this.objects.contains(nx[0].toString())){
				 
				 out.write(nx[0].toString().getBytes());
				 out.write('\t');
				 out.write(nx[1].toString().getBytes());
				 out.write('\t');
				 out.write(nx[2].toString().getBytes());
				 out.write('\n');
				 
				 
			}
			
		}
		
		
		in.close();
		out.close();
	}
	
	
}
