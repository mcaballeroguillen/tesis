package vecinos;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;


import scala.Tuple2;
import scala.Tuple3;


public class Kandalls_coef {
 protected String  Directorio;

public Kandalls_coef(String Directorio){
	this.Directorio = Directorio;
}

public void compare_rank(String rank1, String rank2){
	String master = "local[2]";
	 
	 SparkConf conf = new SparkConf()
				.setAppName(Kandalls_coef.class.getName())
				.setMaster(master);
		
    	 JavaSparkContext context = new JavaSparkContext(conf);
    	 
    	 /*
    	  * Cargamos los rankings
    	  */
    	 
    	 JavaRDD<String> inputrank1 =  context.textFile(rank1);
    	 JavaRDD<String> inputrank2 =  context.textFile(rank2);
    	 
    	 /*
    	  * Creamos la tupla s1##s2, pos
    	  * 
    	  * 
    	  */
    	 JavaPairRDD<String,Integer> rank1_par = inputrank1.mapToPair(
    			 line ->{
    				
    				String[] s = line.split("$");
    				if(s.length ==2){
    					 String ss = line.split("$")[0];
        				Integer pos = Integer.valueOf(line.split("$")[1]);
        				return new Tuple2<String,Integer>(ss,pos);
    				} 
    				return new Tuple2<String,Integer>("Nada",0);
    				
    			 });
    	 
    	 JavaPairRDD<String,Integer> rank2_par = inputrank2.mapToPair(
    			 line ->{
    				 String[] s = line.split("$");
     				if(s.length ==2){
     					 String ss = line.split("$")[0];
         				Integer pos = Integer.valueOf(line.split("$")[1]);
         				return new Tuple2<String,Integer>(ss,pos);
     				} 
     				return new Tuple2<String,Integer>("Nada",0);
    			 });
    	 /*
    	  * Creamos join
    	  */
    	 JavaPairRDD<String,Tuple2<Integer,Integer>> join = rank1_par.join(rank2_par);
    	 
    	 JavaPairRDD<String,Tuple2<Integer,Integer>>  filter = join.filter(
    			 f->{
    				 boolean b = f._1().equals("Nada");
    				 b = b != false;
    				 return b;
    			 });
    	 
    	
    	
    	 filter.saveAsTextFile(this.Directorio+"/rankings");
    	 context.close();
    	 
}
}
