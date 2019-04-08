package vecinos;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		try{
			if(args.length==0){
				System.out.println("Debe ingresar un límete, de no tener ingrese 0");
				System.exit(-1);
			}
			System.out.println("Star");
			Extract extractor = new  Extract("/u/m/mag/2017/mcaballe/WIKIDATA/wikidata-20181123-truthy-BETA.nt.gz",Integer.valueOf(Integer.valueOf(args[0])));
			extractor.FindObjectes("/u/m/mag/2017/mcaballe/WIKIDATA/result.txt","<http://www.wikidata.org/entity/Q11424>");
			CountNeightbor contador = new CountNeightbor("/u/m/mag/2017/mcaballe/WIKIDATA/result.txt");
			contador.count();
			System.out.println("End");
		}catch (Throwable e){
		e.printStackTrace();
		System.exit(-1);}
	}
}
