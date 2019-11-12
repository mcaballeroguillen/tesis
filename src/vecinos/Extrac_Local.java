package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Extrac_Local {
	protected static String entidad;
	protected static String directorio;
	protected static String Rankinh_global;
	protected static Integer tipo;
	
	
	
	public static void extract (String Entidad, String Ranking_global, String directorior, Integer tipor) throws IOException{
		entidad=Entidad;
		Rankinh_global = Ranking_global;
		directorio = directorior;
		tipo=tipor;
		
		File archivo_in = new File(Rankinh_global);
		FileReader fr = new FileReader(archivo_in);
		BufferedReader br = new BufferedReader(fr);
		FileWriter archivo_out = new FileWriter(directorio+"/"+"rank_"+entidad+".txt");
		PrintWriter pw = new PrintWriter(archivo_out);
		
		if(tipo==1){
			try{
			String linea;
			while((linea=br.readLine())!=null){
				String s1 = linea.split(",")[1];
				String s2 = linea.split(",")[2];
				s1 = s1.replaceAll("[()]","");
				s2 = s2.replaceAll("[()]","");
				s1 = s1.replaceAll(">", "");
				s2 = s2.replaceAll(">", "");
				if(s1.equals(entidad)|| s2.equals(entidad)){
					pw.println(linea);
				}
				
			}
			}catch (Exception e){
				e.printStackTrace();
			} finally{
				 if( null != fr ){   
		               fr.close();     
		            } 
				 if (null != archivo_out)
					 archivo_out.close();
			}
		}else{
			System.out.println("Archivo tipo2");
			try{
			archivo_in = new File(Rankinh_global);
			fr = new FileReader(archivo_in);
			br = new BufferedReader(fr);
			archivo_out = new FileWriter(directorio+"/"+"rank_"+entidad+".txt");
            pw = new PrintWriter(archivo_out);
			String linea;
			
			while((linea=br.readLine())!=null){
				String ss = linea.split(",")[1];
				ss = ss.replaceAll("[()]","");
				ss = ss.replaceAll(">", "");
				String s1 = ss.split("##")[0];
				String s2 = ss.split("##")[1];
				
				if(s1.equals(entidad)|| s2.equals(entidad)){
					pw.println(linea);
				}
				
			}
			}catch (Exception e){
				e.printStackTrace();
			} finally{
				 if( null != fr ){   
		               fr.close();     
		            } 
				 if (null != archivo_out)
					 archivo_out.close();
			}
			
		}
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException{
		if(args.length==0){
			System.out.println("Debe ingresar ranking global,tipo, directorio de salida, cantidad de entidades, entidades_0");
			System.exit(-1);
		}
		Integer Cantidad = Integer.valueOf(args[3]);
		if((args.length-Cantidad)!=4){
			System.out.println("Debe ingresar ranking global,tipo, directorio de salida, cantidad de entidades, entidades_1"+args.length+"-"+Cantidad);
			System.exit(-1);
		}
		Integer x=0;
		for(x=0;x<Cantidad;x++){
			extract(args[4+x],args[0],args[2],Integer.valueOf(args[1]));
			
		}
	}

}
