package vecinos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


import scala.Tuple2;
import scala.Tuple3;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class Prueba {

 public static void main(String[] args) throws IOException{
	 InputStream in = null;
	 FileOutputStream out = null;
	 ArrayList<Node[]> movies = new ArrayList<Node[]>();
	 in = new FileInputStream("/home/marco/WIKIDATA/wikidata-20181123-truthy-BETA.nt.gz");
	 out = new  FileOutputStream("/home/marco/WIKIDATA/result.txt");
	 in = new GZIPInputStream(in);
	 NxParser nxp = new NxParser();
	 nxp.parse(in);
	 
	 String master = "local[*]";
	 
	 SparkConf conf = new SparkConf()
				.setAppName(Prueba.class.getName())
				.setMaster(master);
		JavaSparkContext context = new JavaSparkContext(conf);
	 
	 System.out.println("Vamos a ver");
	 int x=0;
	 for (Node[] nx : nxp){
		 if(nx[1].toString().equals("<http://www.wikidata.org/prop/direct/P31>")  && nx[2].toString().equals("<http://www.wikidata.org/entity/Q11424>") ){
			 movies.add(nx);x++;
			 out.write(nx[0].toString().getBytes());
			 out.write('\t');
			 out.write(nx[1].toString().getBytes());
			 out.write('\t');
			 out.write(nx[2].toString().getBytes());
			 out.write('\n');
			 }
		 if(x>=100){break;}
	 }
	 in.close();
	 out.close();
	 System.out.println(movies.size());
	 JavaRDD<String> inputRDD = context.textFile("/home/marco/WIKIDATA/result.txt");
	 JavaRDD<Tuple3<String,String,String>> moviesnt = inputRDD.map(
			 line -> new Tuple3<String,String,String>(
					 line.split("\t")[0],line.split("\t")[1],line.split("\t")[2]
					 )
			 
			 );
	 
	 JavaPairRDD<String,String> pardepelis= moviesnt.mapToPair(
			 tripleta -> new Tuple2<String,String>(tripleta._1(),tripleta._3())
			 );
	 
	 JavaPairRDD<String, Iterable<String>> pars =  pardepelis.groupByKey();
			 
	 pardepelis.saveAsTextFile("/home/marco/WIKIDATA/result");
	 context.close();
 }
	
}
