package vecinos;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;

public class ReduceNeightbor {
	protected String directorio;
	
	public ReduceNeightbor(String direc){
		this.directorio=direc;
	}
	
	
	public void run(){
		String master = "local[*]";
		 
		 SparkConf conf = new SparkConf()
					.setAppName(CountNeightbor.class.getName())
					.setMaster(master);
		JavaSparkContext context = new JavaSparkContext(conf);
		JavaRDD<String> inputRDD1 = context.textFile("/home/marco/WIKIDATA/tupless.txt");
		
		 JavaRDD<Tuple3<String,String,String>> tuples = inputRDD1.map(
					 line -> new Tuple3<String,String,String>(
							 line.split("\t")[0],line.split("\t")[1],line.split("\t")[2]
							 )
					 
					 );
			
			
		JavaPairRDD<Tuple2<String,String>,Integer> tuples2 = tuples.mapToPair(
					tuple -> new Tuple2<Tuple2<String,String>,Integer>(
							 new Tuple2<String,String>(tuple._1(),tuple._2()), Integer.valueOf(tuple._3())
							)
					);
		
		
		
		
		
		JavaPairRDD<Tuple2<String,String>,Integer> trip22 = tuples2.reduceByKey(
				(a,b)-> a+b
				
				);
		
		JavaPairRDD<Tuple2<String,String>,Integer> trip3 = trip22.filter(f->f._1._1.equals(f._1._2)== false);
		
		JavaPairRDD<Integer,Tuple2<String,String>> trip4 = trip3.mapToPair(f->f.swap());
		
		JavaPairRDD<Integer,Tuple2<String,String>> trip5 = trip4.sortByKey(false);
		trip5.saveAsTextFile(this.directorio+"/result");
		context.close();
	}

}
