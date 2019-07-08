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
    				try{
    				String[] s = line.split(",");
    				String par= s[0];
    				Integer rank = Integer.valueOf(s[1]);
    				return new Tuple2<String,Integer>(par,rank);
    				}catch(Exception e){
    					System.out.println("algo pasó");
    					e.printStackTrace();
    					System.exit(-1);
    					return null;
    				}
					
    				
    			 });
    	 
    	 JavaPairRDD<String,Integer> rank2_par = inputrank2.mapToPair(
    			 line ->{
    				 try{
    	    				String[] s = line.split(",");
    	    				String par= s[0];
    	    				Integer rank = Integer.valueOf(s[1]);
    	    				return new Tuple2<String,Integer>(par,rank);
    	    				}catch(Exception e){
    	    					System.out.println("algo pasó2");
    	    					e.printStackTrace();
    	    					System.exit(-1);
    	    					return null;
    	    				}
    			 });
    	 
    	 JavaPairRDD<String,Integer> filter_1 = rank1_par.filter(f->f!=null);
    	 JavaPairRDD<String,Integer> filter_2 = rank2_par.filter(f->f!=null);
    	 /*
    	  * Creamos join
    	  */
    	 JavaPairRDD<String,Tuple2<Integer,Integer>> join = filter_1.join(filter_2);
    	 
    	 
    	 
    	
    	
    	 join.saveAsTextFile(this.Directorio+"/rankings");
    	 
    	 context.close();
    	 
}
}
