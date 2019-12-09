package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class WebGraph_inverTraslate {
protected static String entitys_file;
protected static HashMap<String,String> dic_entytis;
protected static String rank_file;
protected static String directorio;

public static boolean check_entity(String line){
	return line.contains("entity");
}

public static boolean check_cont(String value){
	return dic_entytis.keySet().contains(value);
}
public static void load_dic() throws IOException{
	File archivo_in = new File(entitys_file);
	FileReader fr = new FileReader(archivo_in);
	BufferedReader br = new BufferedReader(fr);
	String linea;
	
	while((linea=br.readLine())!=null){
		String[] data = linea.split("->");
		String entety= data[0];
		String id= data[1];
		if (check_entity(entety)){
			dic_entytis.put(id, entety);
		}
		
	}
	br.close();
}

public static void transform() throws IOException{
	File archivo_in = new File(rank_file);
	FileReader fr = new FileReader(archivo_in);
	BufferedReader br = new BufferedReader(fr);
	FileWriter archivo_out = new FileWriter(directorio+"/"+"simrank_wiki.txt");
	PrintWriter pw = new PrintWriter(archivo_out);
	String linea;
	
	while((linea=br.readLine())!=null){
		String[] data = linea.split("->");
		String par = data[0];
		String value= data[1];
		String[] ids = par.split("##");
		String id1= ids[0];
		String id2= ids[1];
		if(check_cont(id1) & check_cont(id2)){
			String newline = dic_entytis.get(id1)+ "##"+dic_entytis.get(id2) +"," + value;
			pw.println(newline);
		}
		
}
	br.close();
	pw.close();
}

public static void main(String[] args) throws IOException{
	if(args.length!=3){
		System.out.println("Debe Ingresar archivo simrank, entitys y directorio final");
		System.exit(-1);
	}
	dic_entytis  = new HashMap<String,String>();
	rank_file = args[0];
	entitys_file=args[1];
	directorio = args[2];
	load_dic();
	transform();
	Sort_values sorted = new Sort_values(directorio);
	sorted.sort(directorio+"/"+"simrank_wiki.txt");
	
}
}
