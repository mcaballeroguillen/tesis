package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class Main1 {

	public static void main(String[] args) throws IOException{
		if(args.length!=3){System.out.println("Debe ingresar archivo de intersecciones y de vecinos, y directorio");}
		File archivo = new File (args[0]);
		FileReader fr = new FileReader (archivo);
		BufferedReader br = new BufferedReader(fr);
		String linea=br.readLine();
		Count_contents contador = new Count_contents(args[1]);
		FileOutputStream out = null;
		out = new  FileOutputStream(args[2]+"/conteo.txt");
		while(linea!=null){
			linea= linea.replace("(", "");
			linea= linea.replace(")", "");
			String[] pars= linea.split(",");
			long conteo = contador.count(pars[1]);
			linea=br.readLine();
			out.write(pars[0].getBytes());
			out.write('\t');
			out.write((int) conteo);
			out.write('\n');
		}
		br.close();
		out.close();
	}
}
