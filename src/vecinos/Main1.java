package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class Main1 {
	
	public static String createInput(String file) throws IOException{
		File ffile = new File(file);
		BufferedReader br = new BufferedReader(new FileReader(ffile)); 
		String line;
		String resp =br.readLine();
		while((line=br.readLine())!=null){
			resp=resp+", "+line;
		}
		br.close();
		return resp;
		
	}

	public static void main(String[] args) throws IOException{
		if(args.length!=3){System.out.println("Debe ingresar archivo de intersecciones y de vecinos, y directorio"); System.exit(-1);}
		
		
		Count_contents2 contador = new Count_contents2(args[1],args[2]);
		contador.count(args[0]);
	}
}
