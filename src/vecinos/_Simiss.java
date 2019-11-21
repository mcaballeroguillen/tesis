package vecinos;

import pt.tumba.links.WebGraph;
import pt.tumba.links.SimRank;
public class _Simiss {
private static WebGraph ss; 

public _Simiss(){
	ss = new WebGraph();
}

public static void prueba(){
	ss = new WebGraph();
	ss.addLink("A", "B", 1.0);
	ss.addLink("B", "C", 1.0);
	ss.addLink("A", "C", 1.0);
	ss.addLink("C", "D", 1.0);
	
	SimRank simi = new SimRank(ss);
	simi.computeSimRank();
	System.out.print(simi.simRank("A", "C"));
}

public static void main(String[] args){
	prueba();
}
}
