package vecinos;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


import scala.Tuple2;
import scala.Tuple3;

public class Count_contents {
	protected String directorio;
	protected String vecinos;
	protected long lenA;
	int co=0 ;
	/**
	 * Clase que cuenta los sujetos que tiene contenido en su conjuento de vecinos
	 *  un subconjunto que represnta la intersepción de otros dos sujetos.
	 * @param vec: Archivo donde se guardaron los conjutnos de cada vecino.
	 * @param direct : dirección donde se va guardar la respuesta.
	 */
	public Count_contents(String vec,String direct){
		this.directorio=direct;
		this.vecinos= vec;
		
	}
	/**
	 * Contar
	 * @param inter: Archivo donde estan las intercepciones. 
	 */
	public void count(String inter){
		String master = "local[*]";
		 
		 SparkConf conf = new SparkConf()
					.setAppName(CountNeightbor.class.getName())
					.setMaster(master);
			
	     	 JavaSparkContext context = new JavaSparkContext(conf);
			
			
			
			/*
			 * Cargamos data de las intecepciones.
			 */
			JavaRDD<String> inputRDD =  context.textFile(inter);
			
			/*
			 * Creamos tuplas(s1##s2,po##po##po,3)
			 */
			JavaRDD<Tuple3<String,String,Integer>> intercep = inputRDD.map(
					line->{
						String line1= line.replaceAll("[()]","");
						
						String[] data = line1.split(",");
						Tuple3<String,String,Integer> resp= new Tuple3<String,String,Integer>(data[0],data[1],Integer.valueOf(data[2]));
						return resp;
						
					});
			/*
			 * Creamos tupla(po,s1##s2,1)
			 */
			JavaPairRDD<String,Tuple2<String,Integer>> info1 = intercep.flatMapToPair(
					tuple->{
						ArrayList<Tuple2<String,Tuple2<String,Integer>>> list = new ArrayList<Tuple2<String,Tuple2<String,Integer>>>();
						String[] pos = tuple._2().split("##");
						for(String po:pos){
							Tuple2<String,Integer> s1= new Tuple2<String,Integer>(tuple._1(),1);
							Tuple2<String,Tuple2<String,Integer>> resp = new Tuple2<String,Tuple2<String,Integer>>(po,s1);
							list.add(resp);
						}
						return list.iterator();
					}
					);
			/*
			 * Cargamos data de los vecinos
			 */
			JavaRDD<String> inputRDD1 =  context.textFile(this.vecinos);
			/*
			 * Creamos tupla (po,id)
			 */
			JavaPairRDD<String,String> info2 = inputRDD1.flatMapToPair(
					line->{
						ArrayList<Tuple2<String,String>> list = new ArrayList<Tuple2<String,String>>();
						String line1 = line.replaceAll("[()]","");
						line1 = line1.replace(")","");
						String[] data = line1.split(",");
						String[] pos = data[1].split("##");
						for(String po:pos){
							Tuple2<String,String> resp = new Tuple2<String,String>(po,data[0]);
							list.add(resp);
						}
						return list.iterator();
					}); 
			
			/*
			 * Creamos join (po,(s1##s2,1),Id)
			 */
			JavaPairRDD<String, Tuple2<Tuple2<String, Integer>, String>> join =info1.join(info2);
			
			/*
			 * dejamos ((Id,s1##s2),1)
			 */
			
			JavaPairRDD<Tuple2<String, String>, Integer> corte =  join.mapToPair(
					tuple ->{
						/*
						 * Tuple (Id,s1##s2)
						 */
						Tuple2<String,String> key = new Tuple2<String,String>(tuple._2._2(),tuple._2._1._1);
						Tuple2<Tuple2<String,String>,Integer> resp = new Tuple2<Tuple2<String,String>,Integer>(key,tuple._2._1._2);
						return resp;

					});
			
			
			/*
			 * Cuento los vecinos.
			 */
			
			JavaPairRDD<Tuple2<String, String>, Integer> conteo = corte.reduceByKey(
					(a,b)-> a+b
					);
			/*
			 * Cambio de llave (s1##s2,(id,conteo))
			 */
			JavaPairRDD<String,Tuple2<String,Integer>> cambio = conteo.mapToPair(
					tuple->{
						/*
						 * Value (id,conteo)
						 */
						Tuple2<String,Integer> value = new Tuple2<String,Integer>(tuple._1._1,tuple._2);
						Tuple2<String,Tuple2<String,Integer>> resp = new Tuple2<String,Tuple2<String,Integer>>(tuple._1._2,value);
						return resp;
								
						
					}); 
			/*
			 * pasamos de (s1##s2,po##po##po,3) a (s1##s2,3)
			 */
			JavaPairRDD<String,Integer>data2 =  intercep.mapToPair(
					tuple->{
						Tuple2<String,Integer> resp= new Tuple2<String,Integer>(tuple._1(),tuple._3());
						return resp;
						
					});
			/*
			 * join (s1##s2,(id,conteo),3)
			 */
			
			JavaPairRDD<String, Tuple2<Tuple2<String, Integer>, Integer>> join1 = cambio.join(data2); 
			
			/*
			 * Filtramos por conteo igual
			 */
			
			JavaPairRDD<String, Tuple2<Tuple2<String, Integer>, Integer>> filter = join1.filter(
					tuple->tuple._2._1._2==tuple._2._2());
			
			/*
			 * Conteo final
			 */
			
			JavaPairRDD<String,Integer> finals = filter.aggregateByKey(0,
					(a,b)->1, 
					(a,b)->a+b);
			/*
			 * Cambio para orndar
			 */
			JavaPairRDD<Integer,String> swap1= finals.mapToPair(f->f.swap());
			
			/*
			 * Ordenar
			 */
			
			//JavaPairRDD<Integer,String> sort = swap1.sortByKey(true);
			
			swap1.saveAsTextFile(this.directorio+"/incluidos");
			
			context.close();
			
			
			
	}
}
