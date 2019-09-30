package vecinos;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.lang.Math;
import java.util.Map;

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
				.setMaster(master)
				.set("spark.executor.heartbeatInterval","20s")
				.set("spark.executor.memory","12g")
	 			.set("spark.driver.memory", "12g")
	 			.set("spark.network.timeout", "600s");
			
	     	 JavaSparkContext context = new JavaSparkContext(conf);
			
			
			JavaRDD<String> inputRDD = context.textFile(this.file);
			/*
			 * Leer las tuplas
			 */
						
			JavaRDD<Tuple3<Integer,Integer,Double>> data = inputRDD.map(
					line->{
							String[] dat = line.split(",");
							Integer Userid = Integer.valueOf(dat[0]);
							Integer Movieid = Integer.valueOf(dat[1]);
							Double Ranking = Double.valueOf(dat[2]);
							Tuple3<Integer,Integer,Double> resp = new Tuple3<Integer,Integer,Double>(Userid,Movieid,Ranking);
							return resp;
						
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
			 * Conteo de tuplas
			 */
			JavaPairRDD<Integer,Integer> count = filter.aggregateByKey(1, 
					(a,b)->a,
					(a,b)->a+b);
			
			/*
			 * Join con el conteo
			 */
			JavaPairRDD<Integer,Tuple2<Integer,Tuple2<Tuple2<Integer,Double>,Tuple2<Integer,Double>>>> join1= count.join(filter);
			
			/*
			 * Filtrar por menor a k.
			 */
			
			JavaPairRDD<Integer,Tuple2<Integer,Tuple2<Tuple2<Integer,Double>,Tuple2<Integer,Double>>>> filter1 = join1.filter(f->f._2()._1()<300);
			/*
			 * Creamos para(peli1#peli2,rank1,rank2)
			 */
			JavaPairRDD<String,Tuple2<Double,Double>> info = filter1.mapToPair(
					data1->{
						Integer peli1 = data1._2()._2()._1()._1();
						Double rank1 = data1._2()._2()._1()._2();
						Integer peli2 = data1._2()._2()._2()._1();
						Double rank2 = data1._2()._2()._2()._2();
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
			/*
			 * Suma de los rank1 cuadrados
			 */
			
			JavaPairRDD<String,Double> sum_rank1_2 = rank1_2.reduceByKey((a,b)->a+b);
			/*
			 * Suma de los rank2 cuadradis
			 */
			JavaPairRDD<String,Double> sum_rank2_2 = rank2_2.reduceByKey((a,b)->a+b);
			/*
			 * Raíz de la suma de los cuadrados de rank1
			 */
			JavaPairRDD<String,Double> sqrt_sum1 = sum_rank1_2.mapValues(f-> Math.sqrt(f));
			/*
			 *  Raíz de la suma de los cuadrados de rank2
			 */
			JavaPairRDD<String,Double> sqrt_sum2 = sum_rank2_2.mapValues(f-> Math.sqrt(f));
			/*
			 * Juntar dos resultados
			 */
			JavaPairRDD<String,Tuple2<Double,Double>> denominador_2 = sqrt_sum1.join(sqrt_sum2);
			/*
			 * Multiplicar las raíces de las suma de los cuadrados
			 */
			JavaPairRDD<String,Double> denominador = denominador_2.mapValues(f-> f._1()*f._2());
			/*
			 * Suma de la multiplicación de cada coordenada
			 */
			JavaPairRDD<String,Double> numerador = producto.reduceByKey((a,b)->a+b);
			/*
			 * Juntamos numerador y denomidaor
			 */
			JavaPairRDD<String,Tuple2<Double,Double>> frac_1 = numerador.join(denominador);
			/*
			 * Dividimos
			 */
			JavaPairRDD<String,Double> frac = frac_1.mapValues(f->f._1()/f._2());
			/*
			 * Invertimos par para poder ordenar por similitud  coseno
			 */
			JavaPairRDD<Double,String> frac_swap = frac.mapToPair(f->f.swap());
			/*
			 * Ordenamos 
			 */
			JavaPairRDD<Double,String> frac_sort = frac_swap.sortByKey(false);
			
			frac_sort.saveAsTextFile(this.directory+"/simcoseno");
			context.close();
	}
}
