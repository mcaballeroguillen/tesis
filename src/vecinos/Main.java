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
			CountNeightbor contador = new CountNeightbor(args[1]);
			contador.count();
			
			System.out.println("End");
		}catch (Throwable e){
		e.printStackTrace();
		System.exit(-1);}
	}
}
