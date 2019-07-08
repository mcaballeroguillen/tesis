package vecinos;

import java.util.ArrayList;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;

public class CountNeightbor_1 {
	protected String directorio;
	/**
	 * 
	 * @param File: File to search
	 */
	public CountNeightbor_1(String File){
		this.directorio=File;
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
			
			
			JavaRDD<String> inputRDD = context.textFile(this.directorio+"/result.txt");
			/*
			 * Read tuples
			 */
			
			JavaRDD<Tuple3<String,String,String>> moviesnt = inputRDD.map(
					 line -> {
						 String[] values = line.split("\t");
						 String sujetcs_id = values[0].split("/")[4];
						 return new Tuple3<String,String,String>(sujetcs_id,values[1],values[2]);
					 	}
					 
					 );
			/*
			 * Create pair (?s,{?p,?o})
			 */
			JavaPairRDD<String,Tuple2<String,String>> pardepelis= moviesnt.mapToPair(
					 tripleta -> new Tuple2<String,Tuple2<String,String>>(tripleta._1(),new Tuple2<String,String>(tripleta._2(),tripleta._3()))
					 );
			
			JavaPairRDD<Tuple2<String, String>, String> pardepelisinver= pardepelis.mapToPair(f->f.swap());
			/*
			 * Agrupate by neighbor1
			 */
			JavaPairRDD<Tuple2<String, String>, Iterable<String>> pars =  pardepelisinver.groupByKey();
			
			/*
			 * Count negibors and eliminate very common neighbors.
			 * 
			 */
			
			JavaPairRDD<Tuple2<String, String>, Iterable<String>> count = pars.flatMapToPair(
					tuple ->{
						ArrayList<Tuple2<Tuple2<String, String>, Iterable<String>>> setva = new ArrayList<Tuple2<Tuple2<String, String>, Iterable<String>>>();
						Integer co=0;
						for(String v1:tuple._2){
							co=co+1;
							if(co>300){break;}
						}
						if(co<300){
							setva.add(tuple);
						}
						return setva.iterator();
					}
					);
			
					
			
			JavaPairRDD<Tuple2<String,String>,Integer> trip1 = count.flatMapToPair(
					parss ->{
						ArrayList<Tuple2<Tuple2<String,String>,Integer>> s= new ArrayList<Tuple2<Tuple2<String,String>,Integer>> ();  
						
						for(String v1: parss._2){
							for(String v2: parss._2){
							if(v1.equals(v2)){continue;}
							try{
							Tuple2<String,String>s1 = new Tuple2<String,String>(v1,v2);
							Tuple2<Tuple2<String,String>,Integer> resp = new Tuple2<Tuple2<String,String>,Integer>(s1,1);
							s.add(resp);}catch(Exception e){
								return s.iterator();
							}
							}
							
						}
						return s.iterator();
					}
					
					
					);	
			

			JavaPairRDD<Tuple2<String,String>,Integer> trip22 = trip1.reduceByKey(
					(a,b)-> a+b
					
					);
			/**
			 * Eliminar pares (a,a)
			 */
			JavaPairRDD<Tuple2<String,String>,Integer> trip3 = trip22.filter(f->f._1._1.equals(f._1._2)== false);
			/*
			 * Cambiar para ordenar
			 */
			JavaPairRDD<Integer,Tuple2<String,String>> trip4 = trip3.mapToPair(f->f.swap());
			/*
			 * Ordenar
			 */
			JavaPairRDD<Integer,Tuple2<String,String>> trip5 = trip4.sortByKey(false);
			/*
			 * Contar por llave
			 */
			
			JavaPairRDD<Integer, Integer> trip7 = trip4.aggregateByKey(0,
					 (a,b) ->{return a+1;}
					, (a,b)->{return a+b;});
			
			
			
			trip5.saveAsTextFile(this.directorio+"/result_norm");
			trip7.saveAsTextFile(this.directorio+"/dis_frec");
					
			
			context.close();
			
			
			
					
	}
}
