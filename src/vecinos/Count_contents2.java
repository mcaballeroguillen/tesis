package vecinos;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


import scala.Tuple2;
import scala.Tuple3;

public class Count_contents2 {
	protected String directorio;
	protected String dobleA;
	protected long lenA;
	int co=0 ;
	/**
	 * Clase que cuenta los sujetos que tiene contenido en su conjuento de vecinos
	 *  un subconjunto que represnta la intersepción de otros dos sujetos.
	 * @param vec: La doble intercepsion.
	 * @param direct : dirección donde se va guardar la respuesta.
	 */
	public Count_contents2(String vec,String direct){
		this.directorio=direct;
		this.dobleA= vec;
		
	}
	/**
	 * Contar
	 * @param tripleA: Archivo donde estan las triples intercepciones. 
	 */
	public void count(String tripleA){
		String master = "local[*]";
		 
		 SparkConf conf = new SparkConf()
					.setAppName(CountNeightbor.class.getName())
					.setMaster(master);
			
	     	 JavaSparkContext context = new JavaSparkContext(conf);
			
			
			
			/*
			 * Cargamos data de las intecepciones.
			 */
			JavaRDD<String> inputRDD =  context.textFile(tripleA);
			
			/*
			 * Creamos tuplas(s1##s2,s3,|A3|)
			 */
			JavaRDD<Tuple3<String,String,Integer>> intercep = inputRDD.map(
					line->{
						String line1= line.replaceAll("[()]","");
						
						String[] data = line1.split(",");
						String s1 = data[0].split("##")[0];
						String s2 = data[0].split("##")[1];
						String s3 = data[0].split("##")[2];
						String llave = s1+"##"+s2;
						Integer a3 = Integer.valueOf(data[1]);
						Tuple3<String,String,Integer> resp = new Tuple3<String,String,Integer>(llave,s3,a3);
						return resp;
						
					});
			
			/*
			 * Creamos para (s1##s2,(s3,|A3|))
			 */
			
			JavaPairRDD<String,Tuple2<String,Integer>> data1 = intercep.mapToPair(
					triple->{
						Tuple2<String,Integer> value = new Tuple2<String,Integer>(triple._2(),triple._3());
						return new Tuple2<String,Tuple2<String,Integer>>(triple._1(),value);
						
					});
			/*
			 * Cargamos los datos de la doble intercesion.
			 */
			
			JavaRDD<String> inputRDD1 =  context.textFile(this.dobleA);
			
			JavaPairRDD<String,Integer> data2 = inputRDD1.mapToPair(
					line->{
						String line1= line.replaceAll("[()]","");
						
						String[] data = line1.split(",");
						
						return new Tuple2<String,Integer>(data[0],Integer.valueOf(data[1]));
					});
			
			/*
			 * join (s1##s2,(|A2|,s3,|A3|)
			 * )
			 */
			JavaPairRDD<String, Tuple2<Integer, Tuple2<String, Integer>>> join = data2.join(data1);
			
			/*
			 * Filtrar tal que |A2| == |A3|
			 */
			
			JavaPairRDD<String, Tuple2<Integer, Tuple2<String, Integer>>> filter = join.filter(
					tuple->{
						Integer a2 = tuple._2()._1();
						Integer a3 = tuple._2()._2()._2();
						return a2.equals(a3);
					});
			
			
			JavaPairRDD<String,Integer>  result = filter.mapValues(
					f->{return 1;});
			
			JavaPairRDD<String,Integer> count  = result.reduceByKey(
					(a,b)->a+b);
			
			JavaPairRDD<Integer,String> swap = count.mapToPair(f->f.swap());
			
			JavaPairRDD<Integer,String> sort = swap.sortByKey(true);
			
			sort.saveAsTextFile(this.directorio+"/incluidos");
			
			context.close();
			
			
			
	}
}
