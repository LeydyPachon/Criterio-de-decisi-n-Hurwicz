import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.plot.Plot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

import java.io.*;



public class Criterio {
	//listas de los ejes 
	public static List <Float> ejeX = new ArrayList<Float>();
	public static List <Float> ejeY = new ArrayList<Float>();
	static float lambda = (Float) 0.0f;
	static String tipo = "G";
	static JavaPlot p = new JavaPlot();
	
	public static void main(String args[]) throws IOException{
		System.out.println("CRITERIO DE HURWICZ");
		
		//lambda = (float) 0.25;
		//tipo = 'C';
		
		tipo = JOptionPane.showInputDialog(null, "Introduce El tipo \n C para Costos \n G para Ganancia");
		
		
		int resp=JOptionPane.showConfirmDialog(null,"¿Conoce el grado de pesimismo relativo?");
	    if (JOptionPane.OK_OPTION == resp){
	    	lambda = Float.parseFloat(JOptionPane.showInputDialog(null, "Introduce el Grado de pesimismo relativo."));
		}
	    else{
	    	lambda = (Float) 0.0f;
	    }
		
		 muestraContenido("matrizHurwicz.txt");
		 System.out.println("\n--------------------");
		 
		// generar ´puntos en el plano 2D
		 for(int i=0;i<ejeX.size();i = i+2){
			 generarPuntos(ejeX.get(i),ejeY.get(i), ejeX.get(i+1), ejeY.get(i+1));
		 }
		 
		 if(lambda==0.0f){
			 System.out.println("posiciones en el Plano");
			 System.out.println(ejeX.toString());
			 System.out.println(ejeY.toString());
			 p.plot();
		 }
		 else{
			 System.out.println("Resultado de Actividad");
			 System.out.println(ejeY.toString());
		 }		         
		 
	}
	
	public static void generarPuntos(float x1,float y1, float x2, float y2){
		float data[][] = new float[2][2];
		 data[0][0] = x1;
		 data[0][1] = y1;
		 data[1][0] = x2;
		 data[1][1] = y2;
		 
	   PlotStyle myPlotStyle = new PlotStyle();
       myPlotStyle.setStyle(Style.LINES);
       DataSetPlot s = new DataSetPlot(data);
       myPlotStyle.setLineWidth(1);
       s.setPlotStyle(myPlotStyle);
       p.addPlot(s);
       //p.newGraph();
	}
	
	//leer el archivo matriz de criterio
	 public static void muestraContenido(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        int cont = 0;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
        	System.out.println("\n"+"Toma de la actividad No."+(cont+1));
            System.out.println("\n"+cadena);
            //reconocer cual es el mejor y peor evento por actividad
            String[] peorMejor = ordenar(cadena).split(";");
            //System.out.println(peorMejor[0]+"=="+peorMejor[1]);
            //verificación de lambda Si se conoce el grado de pesimismo relativo
            if(lambda!=0.0f){
            	if(lambda > 1 || lambda < 0){
            		System.err.println("Lambda debe pertenecer al intervalo [0,1] ");
            	}
            	else{
            		criterio(Float.parseFloat(peorMejor[0]), Float.parseFloat(peorMejor[1]), lambda);
            	}
            }
            else{
            	criterio(Float.parseFloat(peorMejor[0]), Float.parseFloat(peorMejor[1]), 0);
            	System.out.println("\n");
                criterio(Float.parseFloat(peorMejor[0]), Float.parseFloat(peorMejor[1]), 1);
            }
            cont++;
        }
        b.close();
    }
	 
	public static String ordenar(String cadena){
		//CON EL FIN DE SABER CUAL ES LA MEJOR (Mi) Y LA PEOR OPCIÓN (mi)
		String event[] = cadena.split(" ");
		float mejor = 0;
		float peor = 0;
		//pasar event string a entero
		List <Float> eventos = new ArrayList<Float>();
		for(int t=0;t<event.length;t++){
			eventos.add(Float.parseFloat(event[t]));
		}
		eventos.sort(null);
		//verificación de orden
		for(int i=0;i<eventos.size();i++){
			System.out.print(eventos.get(i)+" ");
		}
		//identifiación del mejor y el peor 
		if(tipo=="G"){
			//TIPO GANANCIA
			peor = eventos.get(0);
			mejor = eventos.get(eventos.size()-1);
		}
		else{
			//TIPO COSTO
			mejor = eventos.get(0);
			peor = eventos.get(eventos.size()-1);
		}
		
		//System.out.println(Integer.toString(peor)+";"+Integer.toString(mejor));
		return Float.toString(peor)+";"+Float.toString(mejor);
	}
	
	public static void criterio(float m, float M,float lam){
		//HACER LA ECUACIÓN DEL CRITERIO 
		//LAM(mi)+(1-LAM)Mi
		float Py = (lam*m) + ((1-lam)*M);
		ejeX.add(lam);
		ejeY.add(Py);
	}
	
	public static void lambda(){
		//HALLAR LAMBDA	
	}
}
