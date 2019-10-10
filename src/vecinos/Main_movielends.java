package vecinos;

import java.io.IOException;

public class Main_movielends {

	public static void main(String[] args) throws IOException{
		if(args.length!=4){System.out.println("Debe ingresar: Diccionario,links,simcoseno y direcorio"); 
		 for(String ss: args){
			 System.out.println(ss);
		 }
		System.exit(-1);}
		String  Diccionario = args[0];
		String links = args[1];
		String simcoseno =args[2];
		String directorio = args[3];
		Movielend_convert convert = new Movielend_convert(links,simcoseno,directorio);
		convert.convert();
		Convert_imdb convert1= new Convert_imdb(Diccionario,directorio+"/simcoseno_imdb.txt",directorio);
		convert1.convert();
		
		
		
	}
}
