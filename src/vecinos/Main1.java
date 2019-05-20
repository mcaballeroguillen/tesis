package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class Main1 {

	public static void main(String[] args) throws IOException{
		if(args.length!=3){System.out.println("Debe ingresar archivo de intersecciones y de vecinos, y directorio");}
		Count_contents contador = new Count_contents(args[1],args[2]);
		contador.count(args[0]);
	}
}
