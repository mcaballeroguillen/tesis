package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class Main_kendalls {
	
	

	public static void main(String[] args) throws IOException{
		if(args.length!=3){System.out.println("Debe ingresar los dos rankisn y directorio final");
		System.exit(-1);}
		
		Kandalls_coef calcultor = new Kandalls_coef(args[2]);
		
		calcultor.compare_rank(args[0], args[1]);
		
		

	}
}
