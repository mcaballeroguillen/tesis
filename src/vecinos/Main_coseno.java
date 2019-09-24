package vecinos;

public class Main_coseno {

  public static void main(String[] args){
	  if(args.length!=2){System.out.println("Debe ingresar archivo de rankings y dirctorio donde guardar el resultado"); System.exit(-1);}
	  SimCoseno calculador  = new SimCoseno(args[0],args[1]);
	  calculador.Calculate();
  }
}
