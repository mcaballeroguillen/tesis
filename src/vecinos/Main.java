package vecinos;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		System.out.println("Star");
		Extract extractor = new  Extract("/home/marco/WIKIDATA/wikidata-20181123-truthy-BETA.nt.gz",50000);
		extractor.FindObjectes("/home/marco/WIKIDATA/result.txt","<http://www.wikidata.org/entity/Q11424>");
		System.out.println("End");
	}
}
