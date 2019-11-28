package vecinos;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.lang.Math;
import java.util.ArrayList;
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
				.set("spark.executor.heartbeatInterval","35s")
				.set("spark.executor.memory","15g")
	 			.set("spark.driver.memory", "15g")
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
			 * Agrupamos por usario
			 */
			JavaPairRDD<Integer,Iterable<Tuple2<Integer,Double>>> grup = Par.groupByKey();
			
			/*
			 * Filtramos los usarios con menos de k calificaciones.
			 */
			JavaPairRDD<Integer,Iterable<Tuple2<Integer,Double>>>  filter_k  = grup.flatMapToPair(
					tuple->{
						ArrayList<Tuple2<Integer,Iterable<Tuple2<Integer,Double>>>> datos = new  ArrayList<Tuple2<Integer,Iterable<Tuple2<Integer,Double>>>>();
						Integer cont = 0;
						for(Tuple2<Integer,Double> par : tuple._2){
							cont=cont+1;
							if(cont>800){
								 System.out.println("Eliminado");
								break;}
						}
						if(cont<=800){
							datos.add(tuple);
						}
						return datos.iterator();
					});
			/*
			 * Volmemos a mapear
			 */
			JavaPairRDD<Integer,Tuple2<Integer,Double>> new_Par = filter_k.flatMapToPair(
					tuple->{
						ArrayList<Tuple2<Integer,Tuple2<Integer,Double>>> datos = new ArrayList<Tuple2<Integer,Tuple2<Integer,Double>>>();
						for(Tuple2<Integer,Double>  par: tuple._2){
							Tuple2<Integer,Tuple2<Integer,Double>> resp = new Tuple2<Integer,Tuple2<Integer,Double>>(tuple._1,par);
							datos.add(resp);
						}
						return datos.iterator();
					});
			/*
			 * Join devuelve (user,((peli1,cal1),peli2,cal2))
			 */
			JavaPairRDD<Integer,Tuple2<Tuple2<Integer,Double>,Tuple2<Integer,Double>>> join = new_Par.join(new_Par);
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
			 * Contar cuantos usuarios hay por par de pelis.
			 */
			JavaPairRDD<String,Integer> conteo = info.aggregateByKey(1, 
					(a,b)->a, (a,b)->a+b);
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
			 * Agregar Conteo (peli1##peli2,(frac,conteo))
			 */
			
			JavaPairRDD<String,Tuple2<Double,Integer>> triple =  frac.join(conteo);
			/*
			 * Filtramos pares de peliculas con menos de 4 calificaciones.
			 */
			
			JavaPairRDD<String,Tuple2<Double,Integer>> filter_c = triple.filter(f-> f._2._2>100);
			/*
			 * Invertimos para ordenar por simcoseno
			 */
			JavaPairRDD<Double,Tuple2<String,Integer>> inver = filter_c.mapToPair(
					tuple->{
						Tuple2<String,Integer> value = new Tuple2<String,Integer>(tuple._1(),tuple._2._2());
						Tuple2<Double,Tuple2<String,Integer>> resp = new Tuple2<Double,Tuple2<String,Integer>>(tuple._2._1(),value);
						return resp;
					});
			/*
			 * Ordenamos
			 */
			JavaPairRDD<Double,Tuple2<String,Integer>> sort = inver.sortByKey(false);
			
			sort.saveAsTextFile(this.directory+"/simcoseno");
			context.close();
	}
}
