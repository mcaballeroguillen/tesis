package vecinos;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;

public class Count_probalistic {
	protected String directorio;
	protected long Numberofsubjects;
	/**
	 * 
	 * @param File: File to search
	 */
	public Count_probalistic(String File){
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
			 * Count the number of Subjects 
			 */
			Numberofsubjects = pardepelis.groupByKey().count();
			List<Double> as = new ArrayList<Double>();
			as.add((double)Numberofsubjects);
					
			JavaDoubleRDD NumObjes = context.parallelizeDoubles(as);
			
			
			/*
			 * swap
			 */
			JavaPairRDD<Tuple2<String, String>, String> pardepelisinver= pardepelis.mapToPair(f->f.swap());
			/*
			 * Agrupate by predicate and object
			 */
			JavaPairRDD<Tuple2<String, String>, Iterable<String>> pars =  pardepelisinver.groupByKey();
			/*
			 * Agregate number of subjects
			 */
			
			JavaPairRDD<Tuple2<String, String>, Double> sas = pars.aggregateByKey(NumObjes.first(), 
					(a,b)->a, 
					(a,b)->a);
			/*
			 * Count negibors and eliminate very common neighbors.
			 * 
			 */
			
			
			JavaPairRDD<Tuple2<String, String>, Tuple2<Iterable<String>, Double>> join = pars.join(sas);
			
			JavaPairRDD<Iterable<String>,Tuple2<Integer,Double>> count = join.flatMapToPair(
					tuple ->{
						ArrayList<Tuple2<Iterable<String>,Tuple2<Integer,Double>>> setva = new ArrayList<Tuple2<Iterable<String>,Tuple2<Integer,Double>>>();
						Integer co=0;
						for(String v1:tuple._2()._1()){
							co=co+1;
							if(co>1000){break;}
						}
						if(co<1000){
							Tuple2<Integer,Double> s1= new Tuple2<Integer,Double>(co,tuple._2()._2()); 
							Tuple2<Iterable<String>,Tuple2<Integer,Double>> s= new Tuple2<Iterable<String>,Tuple2<Integer,Double>>(tuple._2()._1(),s1);
							setva.add(s);
						}
						return setva.iterator();
					}
					);
			
					
			
			
			JavaPairRDD<Tuple2<String,String>,Double> trip1 = count.flatMapToPair(
					parss ->{
						ArrayList<Tuple2<Tuple2<String,String>,Double>> s= new ArrayList<Tuple2<Tuple2<String,String>,Double>> ();  
						
						for(String v1: parss._1){
							for(String v2: parss._1){
							if(v1.equals(v2)){continue;}
							try{
							Tuple2<String,String>s1 = new Tuple2<String,String>(v1,v2);
							Double a= ((double)parss._2()._1()-1.0)/((parss._2()._2()));
							Tuple2<Tuple2<String,String>,Double> resp = new Tuple2<Tuple2<String,String>,Double>(s1,a);
							s.add(resp);}catch(Exception e){
								return s.iterator();
							}
							}
							
						}
						return s.iterator();
					}
					
					
					);	
			
			
			
			JavaPairRDD<Tuple2<String,String>,Double> trip22 = trip1.reduceByKey(
					(a,b)-> a*b
					
					);
	
			JavaPairRDD<Tuple2<String,String>,Double> trip23 = trip22.flatMapToPair(
					parss->{
						ArrayList<Tuple2<Tuple2<String,String>,Double>> s= new ArrayList<Tuple2<Tuple2<String,String>,Double>> ();
						Tuple2<Tuple2<String,String>,Double> t = new Tuple2<Tuple2<String,String>,Double>(parss._1,1.0-parss._2());
						s.add(t);
						return s.iterator();
						
					});
			
			
			JavaPairRDD<Tuple2<String,String>,Double> trip3 = trip23.filter(f->f._1._1.equals(f._1._2)== false);
			/*
			 * Cambiar para ordenar
			 */
			JavaPairRDD<Double,Tuple2<String,String>> trip4 = trip3.mapToPair(f->f.swap());
			/*
			 * Ordenar
			 */
			JavaPairRDD<Double,Tuple2<String,String>> trip5 = trip4.sortByKey(false);
			/*
			 * Contar por llave
			 */
			
			
			
			
			
			trip5.saveAsTextFile(this.directorio+"/result");
			
					
			
			context.close();
			
			
			
					
	}
}
