package vecinos;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


import scala.Tuple2;

public class Count_contents {
	
	protected String vecinos;
	protected long lenA;
	int co=0 ;
	public Count_contents(String vec){
		
		this.vecinos= vec;
		
	}
	
	public long count(String A){
		String master = "local[*]";
		 
		 SparkConf conf = new SparkConf()
					.setAppName(CountNeightbor.class.getName())
					.setMaster(master);
			
	     	 JavaSparkContext context = new JavaSparkContext(conf);
			
			
			/*
			 * Paralelizar linea de A			
			 */
			List<String> Al = new ArrayList<String>();
			Al.add(A);
			
			JavaRDD<String> inputRDD1 = context.parallelize(Al);
			
			/*
			 * Crear pares (PO,1)
			 */
			JavaPairRDD<String,Integer> Inter = inputRDD1.flatMapToPair(
					tuple->{
						ArrayList<Tuple2<String,Integer>> list = new ArrayList<Tuple2<String,Integer>>();
						String[] pos= tuple.split("##");
						for(String po:pos){
							Tuple2<String,Integer> resp = new Tuple2<String,Integer>(po,1);
							list.add(resp);
						}
						return list.iterator();
						
					});
			/*
			 * Sacamos el largo de A.
			 */
			
			this.lenA = Inter.groupByKey().count();
			
			/*
			 * Cargar lineas de vecinos
			 */
			JavaRDD<String> inputRDD = context.textFile(this.vecinos);
			
			JavaPairRDD<Integer,String> par = inputRDD.mapToPair(
					line->{
						line = line.replace("(","");
						line = line.replace(")","");
						String[] s = line.split(",");
						Integer a = Integer.valueOf(s[0]);
						Tuple2<Integer,String> resp= new Tuple2<Integer,String>(a,s[1]);
						return resp;
						
					});
			/*
			 * Quitamos conjuentos de maneor tamaño.
			 */
			JavaPairRDD<Integer,String> filter = par.filter(f->f._1()>=this.lenA);
			
			JavaPairRDD<String,String> conju= filter.flatMapToPair(
					tuple->{
						ArrayList<Tuple2<String,String>> list = new ArrayList<Tuple2<String,String>>();
						String id = "ID"+ Integer.toString(co);
						co=co+1;
						String[] pos= tuple._2().split("##");
						for(String po: pos){
							Tuple2<String,String> resp= new Tuple2<String,String>(po,id);
							list.add(resp)
;						}
						return list.iterator();
					}
					);
			/*
			 * Creamos los pares (po,(id,1))
			 */
			JavaPairRDD<String, Tuple2<String, Integer>> join = conju.join(Inter);
			/*
			 * dejamos (id,1)
			 */
			JavaPairRDD<String,Integer> reduc = join.mapToPair(f->f._2());
			/*
			 * Contamos los 1.
			 */
			
			JavaPairRDD<String,Integer> cout= reduc.reduceByKey((a,b)->a+b);
			
			JavaPairRDD<String,Integer> fil1 = cout.filter(f->f._2==this.lenA);
			
			long result = fil1.groupByKey().count();
			context.close();
			return result;
			
			
	}
}
