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
	protected int limit_subjects;
	protected int limit_tuples;
	/**
	 * 
	 * @param FileIn: Entry file
	 * @param limit_subjects: Limit of subjectes to find.
	 * @param limit_tuples: Limit of tuples with have subjects selcted. 
	 */
	public Extract(String FileIn, int limit_subjects, int limit_tuples){
		objects = new TreeSet<>();
		this.File=FileIn;
		this.limit_subjects = limit_subjects;
		this.limit_tuples = limit_tuples;
	}
	
	/**
	 * Method that looks for tuples with a type within the limit
	 * @param OutFile: File where the results will be saved  
	 * @param ObjectType: Class or type to be searched
	 * @throws IOException: File handling exceptions
	 */
	public void FindObjectes (String OutFile,String ObjectType) throws IOException{
		int teplimit=this.limit_subjects;
		InputStream in = null;
		in = new FileInputStream(this.File);
		in = new GZIPInputStream(in);
		NxParser nxp = new NxParser();
		nxp.parse(in);
		
		for (Node[] nx : nxp){
			if(nx[1].toString().equals("<http://www.wikidata.org/prop/direct/P31>") && nx[2].toString().equals(ObjectType)){
				if(this.limit_subjects>0){   //If the limit is 0,  means that  should search in all wikidata.
					teplimit--;
					if(teplimit<=0){break;}
				}
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
		int templimit = this.limit_tuples;
		InputStream in = null;
		FileOutputStream out = null;
		in = new FileInputStream(this.File);
		out = new  FileOutputStream(OutFile);
		in = new GZIPInputStream(in);
		NxParser nxp = new NxParser();
		nxp.parse(in);
		
		for (Node[] nx : nxp){
			if(this.objects.contains(nx[0].toString())){
				 
				 out.write(nx[0].toString().getBytes());
				 out.write('\t');
				 out.write(nx[1].toString().getBytes());
				 out.write('\t');
				 out.write(nx[2].toString().getBytes());
				 out.write('\n');
				 if(this.limit_tuples>0){ //If the limit is 0,  means that  should search in all wikidata
						templimit--;
						if(templimit<=0){break;}
					}
				 
			}
			
		}
		
		
		in.close();
		out.close();
	}
	
	
}
