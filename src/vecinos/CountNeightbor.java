package vecinos;

import java.util.ArrayList;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;
/*
 * class that counts the common neighbors
 */
 
public class CountNeightbor {
	protected String File;
	public CountNeightbor(String File){
		this.File=File;
	}
	/*
	 * count
	 */
	public void count(){
		String master = "local[*]";
		 
		 SparkConf conf = new SparkConf()
					.setAppName(CountNeightbor.class.getName())
					.setMaster(master);
			JavaSparkContext context = new JavaSparkContext(conf);
			
			JavaRDD<String> inputRDD = context.textFile(this.File);
			/*
			 * Read tuples
			 */
			JavaRDD<Tuple3<String,String,String>> moviesnt = inputRDD.map(
					 line -> new Tuple3<String,String,String>(
							 line.split("\t")[0],line.split("\t")[1],line.split("\t")[2]
							 )
					 
					 );
			/*
			 * Create pair (neighbor1, neighbor2)
			 */
			JavaPairRDD<String,String> pardepelis= moviesnt.mapToPair(
					 tripleta -> new Tuple2<String,String>(tripleta._1(),tripleta._3())
					 );
			/*
			 * Agrupate by neighbor1
			 */
			JavaPairRDD<String, Iterable<String>> pars =  pardepelis.groupByKey();
			
			
			JavaPairRDD<Tuple2<String,String>,Integer> trip1 = pars.flatMapToPair(
					parss ->{
						ArrayList<Tuple2<Tuple2<String,String>,Integer>> s= new ArrayList<Tuple2<Tuple2<String,String>,Integer>> ();  
						for(String v1: parss._2){
							Tuple2<String,String>s1 = new Tuple2<String,String>(parss._1,v1);
							Tuple2<Tuple2<String,String>,Integer> resp = new Tuple2<Tuple2<String,String>,Integer>(s1,1);
							s.add(resp);
							
							
						}
						return s.iterator();
					}
					
					
					);	
			
			JavaPairRDD<Tuple2<String,String>,Integer> trip2 = trip1.reduceByKey(
					(a,b)-> a+b
					
					);
			
			JavaPairRDD<Integer,Tuple2<String,String>> trip3 = trip2.mapToPair(f->f.swap());
			
			JavaPairRDD<Integer,Tuple2<String,String>> trip4 = trip3.sortByKey(false);
			
			
			trip4.saveAsTextFile("/home/marco/WIKIDATA/result");
			context.close();
			
			
			
					
	}
	
	
}
