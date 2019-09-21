package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

public class Format {

	public static void main(String[] args) throws IOException{
		File archivo_in = null;
	    FileReader fr = null;
	    BufferedReader br = null;
	    FileWriter archivo_out = null;
        PrintWriter pw = null;
        Set<String> objects = new TreeSet<>();
		if (args.length != 3){
			System.out.println("Debe Ingresar archivo,tipo y directorio final");
			System.exit(-1);
		}
		if (Integer.valueOf(args[1])==1){
			System.out.println("Archivo tipo1");
			try{
			archivo_in = new File(args[0]);
			fr = new FileReader(archivo_in);
			br = new BufferedReader(fr);
			archivo_out = new FileWriter(args[2]+"rank"+ args[1]+".txt");
            pw = new PrintWriter(archivo_out);
			String linea;
			Integer pos=0;
			while((linea=br.readLine())!=null){
				String s1 = linea.split(",")[1];
				String s2 = linea.split(",")[2];
				s1 = s1.replaceAll("[()]","");
				s2 = s2.replaceAll("[()]","");
				s1 = s1.replaceAll(">", "");
				s2 = s2.replaceAll(">", "");
				Integer v1 = Integer.valueOf(s1.replaceAll("Q", ""));
				Integer v2 = Integer.valueOf(s2.replaceAll("Q", ""));
				String par;
				if(v1<v2){
					 par = s1+"##" +s2;
				}else{
					par = s2 +"##"+ s1;
				}
				if(objects.contains(par)){continue;};
				String newline = par+','+ pos.toString();
				objects.add(par);
				pw.println(newline);
				pos = pos+1;
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
		if (Integer.valueOf(args[1])==2){
			System.out.println("Archivo tipo2");
			try{
			archivo_in = new File(args[0]);
			fr = new FileReader(archivo_in);
			br = new BufferedReader(fr);
			archivo_out = new FileWriter(args[2]+"rank"+ args[1]+".txt");
            pw = new PrintWriter(archivo_out);
			String linea;
			Integer pos=0;
			while((linea=br.readLine())!=null){
				String ss = linea.split(",")[1];
				
				ss = ss.replaceAll("[()]","");
				
				ss = ss.replaceAll(">", "");
				String s1 = ss.split("##")[0];
				String s2 = ss.split("##")[1];
				String par;
				Integer v1 = Integer.valueOf(s1.replaceAll("Q", ""));
				Integer v2 = Integer.valueOf(s2.replaceAll("Q", ""));
				if(v1<v2){
					 par = s1+"##" +s2;
				}else{
					par = s2 +"##"+ s1;
				}
				String newline = par +','+ pos.toString();
				pw.println(newline);
				pos = pos+1;
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
}
