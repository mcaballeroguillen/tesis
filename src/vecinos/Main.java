package vecinos;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		try{
			if(args.length!=4){
				System.out.println("Debe ingresar: Dirección WIKIDATA, Directorio de Trabajo, Tipo de la busqueda y límite");
				System.exit(-1);
			}
			System.out.println("Star");
			//Extract extractor = new  Extract(args[0],Integer.valueOf(Integer.valueOf(args[3])));
			//extractor.FindObjectes(args[1]+"/result.txt",args[2]);
			//System.out.println("Vecinos O(N)");
			//ExtractNeightbor extract1 = new ExtractNeightbor(args[1]);
			//extract1.extract();
			System.out.println("Vecinos O(N^2)");
			ExtractInterseption extract2 = new ExtractInterseption(args[1]);
			extract2.extract();
			System.out.println("Vecinos O(N^3)");
			Extract3Interseption extract3 = new Extract3Interseption(args[1]);
			extract3.extract();
			
			System.out.println("End");
		}catch (Throwable e){
		e.printStackTrace();
		System.exit(-1);}
	}
}
