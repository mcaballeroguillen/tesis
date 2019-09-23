package vecinos;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.lang.Math; 

import scala.Tuple3;
import scala.Tuple2;
/**
 * Clase para calcular la similitud coseno de las peliculas. 
 * @author marco
 *
 */
public class SimCoseno {
	protected String file;
	protected String directory;
	/**
	 * 
	 * @param file : Archivo de peliculas
	 * @param directory: Directorio donde guardar el resultado
	 */
	public SimCoseno(String file, String directory){
		this.file = file;
		this.directory = directory;
	}
	
	public void Calculate(){
		String master = "local[*]";
		 
		 SparkConf conf = new SparkConf()
					.setAppName(CountNeightbor.class.getName())
					.setMaster(master);
			
	     	 JavaSparkContext context = new JavaSparkContext(conf);
			
			
			JavaRDD<String> inputRDD = context.textFile(this.file);
			/*
			 * Leer las tuplas
			 */
						
			JavaRDD<Tuple3<Integer,Integer,Double>> data = inputRDD.map(
					line->{
						try{
							String[] dat = line.split(",");
							Integer Userid = Integer.valueOf(dat[0]);
							Integer Movieid = Integer.valueOf(dat[1]);
							Double Ranking = Double.valueOf(dat[2]);
							Tuple3<Integer,Integer,Double> resp = new Tuple3<Integer,Integer,Double>(Userid,Movieid,Ranking);
							
						}catch(Exception e){
							System.out.print("Tupla ignorada");
						}
						return null;
					});
			/*
			 * Crear par (usuraio,(peli,Calificacion))
			 */
			JavaPairRDD<Integer,Tuple2<Integer,Double>> Par = data.mapToPair(
					tripleta -> new Tuple2<Integer,Tuple2<Integer,Double>>(tripleta._1(),new Tuple2<Integer,Double>(tripleta._2(),tripleta._3())));
			
			/*
			 * Join devuelve (user,((peli1,cal1),peli2,cal2))
			 */
			JavaPairRDD<Integer,Tuple2<Tuple2<Integer,Double>,Tuple2<Integer,Double>>> join = Par.join(Par);
			/*
			 * Filtrando donde peli1> peli2
			 */
			JavaPairRDD<Integer,Tuple2<Tuple2<Integer,Double>,Tuple2<Integer,Double>>> filter = join.filter(
					f -> f._2()._1()._1()> f._2()._2()._1());
			
			/*
			 * Creamos para(peli1#peli2,rank1,rank2)
			 */
			JavaPairRDD<String,Tuple2<Double,Double>> info = filter.mapToPair(
					data1->{
						Integer peli1 = data1._2()._1()._1();
						Double rank1 = data1._2()._1()._2();
						Integer peli2 = data1._2()._2()._1();
						Double rank2 = data1._2()._2()._2();
						String key = peli1.toString() + "##" + peli2.toString();
						Tuple2<Double,Double> value = new Tuple2<Double,Double>(rank1,rank2);
						Tuple2<String,Tuple2<Double,Double>> resp = new Tuple2<String,Tuple2<Double,Double>>(key,value);
						return resp;
					});
			
			/*
			 *  Multiplicamos coordenda por coordenada (peli1##peli2,rank1*rank2)
			 */
			JavaPairRDD<String,Double> producto =  info.mapValues(f->f._1()*f._2());
			/*
			 * Elevamos al cuadrado rank1 (peli1##peli2,rank1²)
			 */
			JavaPairRDD<String,Double> rank1_2 = info.mapValues(f-> f._1()*f._1());
			/*
			 * Elevamos al cuadrado rank1 (peli1##peli2,rank2²)
			 */
			JavaPairRDD<String,Double> rank2_2 = info.mapValues(f-> f._2()*f._2());
			
			
			JavaPairRDD<String,Double> sum_rank1_2 = rank1_2.reduceByKey((a,b)->a+b);
			
			JavaPairRDD<String,Double> sum_rank2_2 = rank2_2.reduceByKey((a,b)->a+b);
			
			JavaPairRDD<String,Double> sqrt_sum1 = sum_rank1_2.mapValues(f-> Math.sqrt(f));
			
			JavaPairRDD<String,Double> sqrt_sum2 = sum_rank2_2.mapValues(f-> Math.sqrt(f));
			
			JavaPairRDD<String,Tuple2<Double,Double>> denominador_2 = sqrt_sum1.join(sqrt_sum2);
			
			JavaPairRDD<String,Double> denominador = denominador_2.mapValues(f-> f._1()*f._2());
			
			JavaPairRDD<String,Double> numerador = producto.reduceByKey((a,b)->a+b);
			
			JavaPairRDD<String,Tuple2<Double,Double>> frac_1 = numerador.join(denominador);
			
			JavaPairRDD<String,Double> frac = frac_1.mapValues(f->f._1()/f._2());
			
			JavaPairRDD<Double,String> frac_swap = frac.mapToPair(f->f.swap());
			
			JavaPairRDD<Double,String> frac_sort = frac_swap.sortByKey(false);
			
			frac_sort.saveAsTextFile(this.directory+"/simcoseno");
			context.close();
	}
}
