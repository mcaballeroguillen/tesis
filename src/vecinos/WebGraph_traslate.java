package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;


public class WebGraph_traslate {
	private static HashMap<String,String> entidades;
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
			String[] data = linea.split("\\t+");
			if(data.length!=3){
				
				System.out.println("Problema en split");
				System.out.println(linea);
				System.out.println(data.length);
				continue;
			}
			String ent1= data[0];
			String ent2= data[2];
			if(check_key(ent1)==false){
				if(check_key(ent2)==false){
					String id1 = get_ideti(ent1);
					String id2 = get_ideti(ent2);
					String line0= id1 +"->"+id1+" "+"1.0";
					String line1= id2 +"->"+id2+" "+ "1.0";
					String line2= id1+"->"+id2+" "+"1.0";
					String line3= id2+"->"+id1+" "+"1.0";
					pw.println(line0);
					pw.println(line1);
					pw.println(line2);
					pw.println(line3);
				}else{
					String id1 = get_ideti(ent1);
					String id2 = get_ideti(ent2);
					String line0= id1+"->"+id1+" "+"1.0";
					String line2= id1+"->"+id2+" "+"1.0";
					String line3= id2+"->"+id1+" "+"1.0";
					pw.println(line0);
					pw.println(line2);
					pw.println(line3);
				}
			}else{
				if(entidades.containsKey(ent2)==false){
					String id1 = get_ideti(ent1);
					String id2 = get_ideti(ent2);
					String line1= id2 +"->"+id2+" "+ "1.0";
					String line2= id1+"->"+id2+" "+"1.0";
					String line3= id2+"->"+id1+" "+"1.0";
					pw.println(line1);
					pw.println(line3);
					pw.println(line2);
				}else{
				continue;
				}
			}
			
		}
		br.close();
		pw.close();
	}
	
	public static boolean check_key(String entity){
		return entidades.keySet().contains(entity);
	}
	
	public static String get_ideti(String entity){
		if(entidades.keySet().contains(entity)){
			return entidades.get(entity);
		}else{
			Integer max = entidades.size();
			max=max+1;
			entidades.put(entity, max.toString());
			return max.toString();
		}
		
	}
	public static void save_entitys() throws IOException{
		FileWriter archivo_out = new FileWriter(output_directory+"/"+"entetys.txt");
		PrintWriter pw = new PrintWriter(archivo_out);
		for(String entity: entidades.keySet()){
			pw.println(entity+"->"+entidades.get(entity));
		}
		pw.close();
	}
	
	public static void main(String[] args) throws IOException{
		entidades= new  HashMap<String,String>();
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
