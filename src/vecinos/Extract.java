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

public class Extract {
	protected Set<String> objects; 
	protected String File;
	int limit;
	public Extract(String FileIn, int limit){
		objects = new TreeSet<>();
		this.File=FileIn;
		this.limit = limit;
	}
	
	
	public void FindObjectes (String OutFile,String ObjectType) throws IOException{
		int teplimit=this.limit;
		InputStream in = null;
		in = new FileInputStream(this.File);
		in = new GZIPInputStream(in);
		NxParser nxp = new NxParser();
		nxp.parse(in);
		for (Node[] nx : nxp){
			teplimit--;
			
			
			if(nx[1].toString().equals("<http://www.wikidata.org/prop/direct/P31>") && nx[2].toString().equals(ObjectType)){
				this.objects.add(nx[0].toString());
			}
			if(teplimit<=0){break;}
		}
		in.close();
		this.writeFile(OutFile);
	}

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
			templimit--;
			if(this.objects.contains(nx[0].toString())){
				 templimit--;
				 out.write(nx[0].toString().getBytes());
				 out.write('\t');
				 out.write(nx[1].toString().getBytes());
				 out.write('\t');
				 out.write(nx[2].toString().getBytes());
				 out.write('\n');
				 
				 
			}
			if(templimit<=0){break;}
		}
		
		
		in.close();
		out.close();
	}
	
	
}
