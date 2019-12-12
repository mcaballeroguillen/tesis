package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

public class filter_entitys {
protected static Set<String> base;
protected static String directorio;
protected static String file;

public static void load_base(String file_base) throws IOException{
	File archivo_in = new File(file_base);
	FileReader fr = new FileReader(archivo_in);
	BufferedReader br = new BufferedReader(fr);
	String linea;
	while((linea=br.readLine())!=null){
		String s = "<" + linea +">";
		base.add(s);
	}
	br.close();
}

public static void filter() throws IOException{
	File archivo_in = new File(file);
	FileReader fr = new FileReader(archivo_in);
	BufferedReader br = new BufferedReader(fr);
	FileWriter archivo_out = new FileWriter(directorio+"/"+"entitys_filter.txt");
	PrintWriter pw = new PrintWriter(archivo_out);
	String linea;
	while((linea=br.readLine())!=null){
		String[] data = linea.split("->");
		String entity = data[0];
		if(base.contains(entity)){
			pw.println(linea);
		}
	}
	br.close();
	pw.close();
}

public static void main(String[] args) throws IOException{
	if(args.length!=3){
		System.out.println("Debe Ingresar archivo base, entitys y directorio final");
		System.exit(-1);
	}
	base = new TreeSet<String>();
	file= args[1];
	directorio= args[2];
	load_base(args[0]);
	filter();
}
}
