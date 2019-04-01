package vecinos;

import java.util.ArrayList;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;
/**
 * 
 * @author Marco Caballero 
 *  class that counts the common neighbors
 *
 */

 
public class CountNeightbor {
	protected String File;
	/**
	 * 
	 * @param File: File to search
	 */
	public CountNeightbor(String File){
		this.File=File;
	}
/**
 * Count
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
			
			JavaPairRDD<String,String> pardepelisinver= pardepelis.mapToPair(f->f.swap());
			/*
			 * Agrupate by neighbor1
			 */
			JavaPairRDD<String, Iterable<String>> pars =  pardepelisinver.groupByKey();
			
			
			JavaPairRDD<Tuple2<String,String>,Integer> trip1 = pars.flatMapToPair(
					parss ->{
						ArrayList<Tuple2<Tuple2<String,String>,Integer>> s= new ArrayList<Tuple2<Tuple2<String,String>,Integer>> ();  
						for(String v1: parss._2){
							for(String v2: parss._2){
							Tuple2<String,String>s1 = new Tuple2<String,String>(v1,v2);
							Tuple2<Tuple2<String,String>,Integer> resp = new Tuple2<Tuple2<String,String>,Integer>(s1,1);
							s.add(resp);
							}
							
						}
						return s.iterator();
					}
					
					
					);	
			
			JavaPairRDD<Tuple2<String,String>,Integer> trip2 = trip1.reduceByKey(
					(a,b)-> a+b
					
					);
			
			JavaPairRDD<Tuple2<String,String>,Integer> trip3 = trip2.filter(f->f._1._1.equals(f._1._2)== false);
			
			JavaPairRDD<Integer,Tuple2<String,String>> trip4 = trip3.mapToPair(f->f.swap());
			
			JavaPairRDD<Integer,Tuple2<String,String>> trip5 = trip4.sortByKey(false);
			
			
			trip5.saveAsTextFile("/home/marco/WIKIDATA/result");
			context.close();
			
			
			
					
	}
	
	
}
