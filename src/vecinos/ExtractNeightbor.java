package vecinos;

import java.util.ArrayList;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;

public class ExtractNeightbor {
	protected String directorio;
	
	public ExtractNeightbor(String direct){
		this.directorio=direct;
	}
	
	public void extract(){
		String master = "local[*]";
		
		SparkConf conf = new SparkConf()
				.setAppName(CountNeightbor.class.getName())
				.setMaster(master);
		
     	 JavaSparkContext context = new JavaSparkContext(conf);
		
		
		JavaRDD<String> inputRDD = context.textFile(this.directorio+"/result.txt");
		/*
		 * Read tuples
		 */
		
		JavaRDD<Tuple3<String,String,String>> moviesnt = inputRDD.map(
				 line -> new Tuple3<String,String,String>(
						 line.split("\t")[0],line.split("\t")[1],line.split("\t")[2]
						 )
				 
				 );
		/*
		 * Create pair (?s,{?p,?o})
		 */
		JavaPairRDD<String,Tuple2<String,String>> pardepelis= moviesnt.mapToPair(
				 tripleta -> new Tuple2<String,Tuple2<String,String>>(tripleta._1(),new Tuple2<String,String>(tripleta._2(),tripleta._3()))
				 );
		/*
		 * Agrupar por sujeto
		 */
		JavaPairRDD<String, Iterable<Tuple2<String, String>>> agrupation = pardepelis.groupByKey();
		/*
		 * Crear tupla (id,vecinos)
		 * id del sujeto
		 * vecinos es un string donde se concatenan todos los vecinos
		 */
		JavaPairRDD<String,String> count= agrupation.flatMapToPair(
				tuple->{
					ArrayList<Tuple2<String,String>> list= new ArrayList<Tuple2<String,String>>();
					String concatenation= "";
					String datas[]= tuple._1.split("/");
					String idsujeto= datas[4];
					for(Tuple2<String,String>po: tuple._2()){
						
						concatenation=concatenation+po._1()+po._2()+"##";
					}
					Tuple2<String,String>resp= new Tuple2<String,String>(idsujeto.replace(",","" ),concatenation.replace(",","" ));
					list.add(resp);
					
					return list.iterator();
				}
				);
		
		
		count.saveAsTextFile(this.directorio+"/vecinos");
		context.close();
	}
}
