package vecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Reduce_extract {

	public static void main(String[] args) throws IOException{
		File archivo_in = null;
	    FileReader fr = null;
	    BufferedReader br = null;
	    FileWriter archivo_out = null;
        PrintWriter pw = null;
        HashMap<String,Integer> conteo = new HashMap<String,Integer>();
       
		if (args.length != 4){
			System.out.println("Debe Ingresar archivo y dirección final,limite de sujetos y limite de tuplas");
			System.exit(-1);
		}
		Integer limit_subject = Integer.valueOf(args[2]);
		Integer limit_tuples = Integer.valueOf(args[3]); 
		try{
			archivo_in = new File(args[0]);
			fr = new FileReader(archivo_in);
			br = new BufferedReader(fr);
			archivo_out = new FileWriter(args[1]+"/result"+ args[2]+"_"+args[3]+".txt");
            pw = new PrintWriter(archivo_out);
            String linea;
			while((linea=br.readLine())!=null){
				String[] values = linea.split("\t");
				if(conteo.containsKey(values[0])){
					Integer cont = conteo.get(values[0]);
					if(cont>=limit_tuples){continue;}
					cont = cont +1;
					conteo.put(values[0],cont);
					pw.println(linea);
				}else{
					if(conteo.keySet().size()>=limit_subject){
						continue;
					}else{
						conteo.put(values[0], 0);
						pw.println(linea);
					}
					
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