package vecinos;

import java.util.ArrayList;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;

public class Count_subjects {
protected String directorio;
	
	public Count_subjects(String direct){
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
			
			JavaPairRDD<Tuple2<String, String>, String> pardepelisinver= pardepelis.mapToPair(f->f.swap());
			/*
			 * Change subject for integer 1. 
			 */
			JavaPairRDD<Tuple2<String,String>,Integer> po_value = pardepelisinver.mapValues(f->1);
			/*
			 * Count number of subjects that have this po
			 * ({?p,?o},n)
			 */
			JavaPairRDD<Tuple2<String,String>,Integer> po_count = po_value.reduceByKey((a,b)->a+b);
			/*
			 * swap
			 * (n,{?p,?o})
			 */
			JavaPairRDD<Integer,Tuple2<String,String>> count_po = po_count.mapToPair(f-> f.swap());
			/*
			 * Change {?p,?o} for integer 1;
			 */
			JavaPairRDD<Integer,Integer> count_value = count_po.mapValues(f->1);
			/*
			 * Count number of po that have same number of subjects. 
			 */
			JavaPairRDD<Integer,Integer> count_sum = count_value.reduceByKey((a,b)->a+b);
			
			JavaPairRDD<Integer,Integer> ffinal = count_sum.sortByKey();
			
			ffinal.saveAsTextFile(this.directorio+"/frec_po");
			context.close();
	}
}
