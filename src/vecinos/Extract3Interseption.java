package vecinos;

import java.util.ArrayList;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import scala.Tuple3;

public class Extract3Interseption {
	protected String directorio;
	
	public Extract3Interseption(String direct){
		this.directorio=direct;
	}
	
	public void extract(){
		String master = "local[*]";
		 
		 SparkConf conf = new SparkConf()
					.setAppName(CountNeightbor.class.getName())
					.setMaster(master)
					.set("spark.executor.memory","12g")
		 			.set("spark.driver.memory", "12g")
		 			.set("spark.network.timeout", "600s");
		    
		   
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
							if(co>500){break;}
						}
						if(co<500){
							setva.add(tuple);
						}
						return setva.iterator();
					}
					);
			/*
			 * Creamos los pares (s1##s2##s3,po)
			 */
			JavaPairRDD<String,String> parsubject = count.flatMapToPair(
					tuple->{
						ArrayList<Tuple2<String,String>> list = new ArrayList<Tuple2<String,String>>();
						for(String s1: tuple._2()){
							for(String s2: tuple._2()){
								for(String s3: tuple._2()){
									if(s1.equals(s2)){continue;}
									if(s1.equals(s3)){continue;}
									//if(s2.equals(s3)){continue;}
									String par = "";
									String s1_id = s1.split("/")[4];
									String s2_id = s2.split("/")[4];
									String s3_id = s3.split("/")[4];
									if(s1_id.compareTo(s2_id)>0){
										par= s1_id.replace(",","" )+"##"+s2_id.replace(",","" );
									}else{
										par=s2_id.replace(",","" )+"##"+s1_id.replace(",","" );
									}
									par=par+"##"+s3_id;
									String po= (tuple._1()._1()+tuple._1()._2()).replace(",","");
									Tuple2<String,String> resp= new Tuple2<String,String>(par,po);
									list.add(resp);
								}
								}
						}
						return list.iterator();
					}
					
					);
			/*
			 * Agrupamos por par de sujetos
			 */
			JavaPairRDD<String,Iterable<String>> agrup= parsubject.groupByKey();
			/*
			 * Creamos pares (s1##s2,po##po##po,3)
			 */
			JavaPairRDD<String,Integer> ffinal = agrup.mapToPair(
					tuple->{
							Integer co=0;
							for(String po: tuple._2){
								co=co+1;
							}
							return  new Tuple2<String,Integer>(tuple._1,co);
					});
			ffinal.saveAsTextFile(this.directorio+"/interseption3_500");
			context.close();
	}
}
