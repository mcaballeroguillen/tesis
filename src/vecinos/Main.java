package vecinos;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		try{
			if(args.length!=4){
				System.out.println("Debe ingresar: Dirección WIKIDATA, Directorio de Trabajo, Tipo de la busqueda,límite de tuplas.");
				System.exit(-1);
			}
			System.out.println("Star");
			Extrac_Especific extractor = new  Extrac_Especific(args[0],Integer.valueOf(args[3]),Integer.valueOf(args[4]));
			extractor.FindObjectes(args[1]+"/result.txt",args[2]);
					}catch (Throwable e){
		e.printStackTrace();
		System.exit(-1);}
	}
}
