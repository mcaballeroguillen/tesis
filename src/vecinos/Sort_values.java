package vecinos;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class Sort_values {
	protected String directorio;
	
	public Sort_values(String direc){
		this.directorio=direc;
	}
	
	public void sort(String file){
		String master = "local[*]";
		 
		 SparkConf conf = new SparkConf()
					.setAppName(CountNeightbor.class.getName())
					.setMaster(master);
		 			
	     JavaSparkContext context = new JavaSparkContext(conf);
			
			
		JavaRDD<String> inputRDD = context.textFile(file);
		
		JavaPairRDD<Double,String> pars = inputRDD.mapToPair(
				linea->{
					String[] data = linea.split(",");
					String par = data[0];
					Double value = Double.valueOf(data[1]);
					Tuple2<Double,String> resp = new Tuple2<Double,String>(value,par);
					return resp;
				});
		JavaPairRDD<Double,String> sort = pars.sortByKey(false);
		
		sort.saveAsTextFile(this.directorio+"/simrank_sort");
		
		context.close();
	}
}
