package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WebGraph_traslate {
	private static ArrayList<String> entidades;
	private static String result_file;
	private static String output_directory;
	
	public static void traslate() throws IOException{
		File archivo_in = new File(result_file);
		FileReader fr = new FileReader(archivo_in);
		BufferedReader br = new BufferedReader(fr);
		FileWriter archivo_out = new FileWriter(output_directory+"/"+"webgraph.txt");
		PrintWriter pw = new PrintWriter(archivo_out);
		String linea;
		
		while((linea=br.readLine())!=null){
						
			String[] data = linea.split("\\s+");
			if(data.length!=3){
				
				System.out.println("Problema en split");
				System.out.println(linea);
				System.out.println(data.length);
				System.exit(-1);
			}
			System.out.print(data.length+"\n");
			String ent1= data[0];
			String ent2= data[2];
			if(entidades.contains(ent1)==false){
				if(entidades.contains(ent2)==false){
					entidades.add(ent1);
					entidades.add(ent2);
					String line0= ent1 +"->"+ent1+" "+"1.0";
					String line1= ent2 +"->"+ent2+" "+ "1.0";
					String line2= ent1+"->"+ent2+" "+"1.0";
					String line3= ent2+"->"+ent1+" "+"1.0";
					pw.println(line0);
					pw.println(line1);
					pw.println(line2);
					pw.println(line3);
				}else{
					entidades.add(ent1);
					String line0= ent1 +"->"+ent1+" "+"1.0";
					String line2= ent1+"->"+ent2+" "+"1.0";
					pw.println(line0);
					pw.println(line2);
				}
			}else{
				if(entidades.contains(ent2)==false){
					entidades.add(ent2);
					String line1= ent2 +"->"+ent2+" "+ "1.0";
					String line3= ent2+"->"+ent1+" "+"1.0";
					pw.println(line1);
					pw.println(line3);
				}else{
					continue;
				}
			}
		}
		br.close();
		pw.close();
	}
	
	public static void save_entitys() throws IOException{
		FileWriter archivo_out = new FileWriter(output_directory+"/"+"entetys.txt");
		PrintWriter pw = new PrintWriter(archivo_out);
		for(String entity: entidades){
			pw.println(entity);
		}
		pw.close();
	}
	
	public static void main(String[] args) throws IOException{
		entidades= new ArrayList<String>();
		if(args.length!=2){
			System.out.println("Debe ingresar result.txt y directorio de salida");
			System.exit(-1);
		}
		result_file= args[0];
		output_directory=args[1];
		traslate();
		save_entitys();
		
		
		
	}
}
