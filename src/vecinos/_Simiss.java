package vecinos;

import pt.tumba.links.WebGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import pt.tumba.links.SimRank;
public class _Simiss {
private static WebGraph ss; 
private static SimRank s1;
private static String directorio;

public _Simiss(){
	ss = new WebGraph();
}

public static void prueba(){
	ss = new WebGraph();
	ss.addLink("A", "B", 1.0);
	ss.addLink("B", "C", 1.0);
	ss.addLink("C", "A", 1.0);
	ss.addLink("C", "D", 1.0);
	
	
	ss.addLink("A", "A", 1.0);
	ss.addLink("B", "B", 1.0);
	ss.addLink("C", "C", 1.0);
	ss.addLink("D", "D", 1.0);
	System.out.println(ss.numNodes());
	SimRank simi = new SimRank(ss);
	simi.computeSimRank();
	System.out.print(simi.simRank("A", "C"));
}

public static void create_file(String file) throws FileNotFoundException, IOException{
	File archivo_in = new File(file);
	ss = new WebGraph();
	FileReader fr = new FileReader(archivo_in);
	BufferedReader br = new BufferedReader(fr);
	String linea;
	
	while((linea=br.readLine())!=null){
	String[] data0 = linea.split("->");
	String[] data1 = data0[1].split(" ");
	String ent0= data0[0];
	String ent1 = data1[0];
	Double weg = Double.valueOf(data1[1]);
	ss.addLink(ent0, ent1, weg);
	}
	br.close();
}


public static void calculate(){
	s1 = new SimRank(ss);
	s1.computeSimRank();
}

public static void print_sim(String entitys_file) throws IOException{
	File archivo_in = new File(entitys_file);
	FileReader fr = new FileReader(archivo_in);
	BufferedReader br = new BufferedReader(fr);
	FileWriter archivo_out = new FileWriter(directorio+"/"+"simrank.txt");
	PrintWriter  pw = new PrintWriter(archivo_out);
	String linea;
	while((linea=br.readLine())!=null){
		String[] data = linea.split("->");
		String id= data[1];
		Map<Integer,Double> values = s1.simRank(id);
		for(Integer id_i: values.keySet()){
			String id2 = ss.IdentifyerToURL(id_i);
			pw.println(id+"##"+id2+"->"+values.get(id_i).toString());
		}
	}
	br.close();
	pw.close();
}
public static void main(String[] args) throws FileNotFoundException, IOException{
	if (args.length != 3){
		System.out.println("Debe Ingresar archivo webgraph, entitys y directorio final");
		System.exit(-1);
	}
	directorio= args[2];
	create_file(args[0]);
	System.out.println("Numero de nodos ingresados: "+ss.numNodes());
	calculate();
	print_sim(args[1]);
	
}

}
