package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import model.data_structures.Comparendo;
import model.data_structures.Queue;
import model.data_structures.Stack;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo 
{
	// Solucion de carga de datos publicada al curso Estructuras de Datos 2020-10
	private Stack<Comparendo> pila;
	private Queue<Comparendo> cola;
	private int iniciado;
	

	public static String PATH = "./data/comparendos_dei_2018.geojson";

	public Modelo()
	{
		pila = new Stack<Comparendo>();
		cola = new Queue<Comparendo>();
		iniciado = 0;
	}
	
	public Queue<Comparendo> darCola()
	{
		return cola;
	}
	
	public int darIniciacion()
	{
		return iniciado;
	}

	public int darTamPila()
	{
		return pila.getSize();
	}
	
	public int darTamCola()
	{
		return cola.getSize();
	}
	
	public Comparendo darComparendoStack()
	{
		return pila.getElement();
	}
	
	public Comparendo darComparendoQueue()
	{
		return cola.getElement();
	}
	
	public Queue<Comparendo> opcion2()
	{
		Queue<Comparendo> nuevo = new Queue<Comparendo>();
		Queue<Comparendo> mayor = new Queue<Comparendo>();
		Comparendo actual = null;
		
		while (cola.isEmpty() == false)
		{
			actual = cola.dequeue();
			
			if(nuevo.getElement() == null)
			{
				nuevo.enqueue(actual);
			}
			else if(nuevo.getElement().darCodInfeaccion().equals(actual.darCodInfeaccion()))
			{
				nuevo.enqueue(actual);
			}
			else
			{
				if(nuevo.getSize()>mayor.getSize())
				{
					mayor = nuevo;
				}
				nuevo = new Queue<Comparendo>();
				nuevo.enqueue(actual);
			}
		}
		
		return mayor;
	}
	
	public Stack<Comparendo> opcion3(int PnumeroComparendos, String pcodigo )
	{
		Stack<Comparendo> nuevo = new Stack<Comparendo>();
		boolean termine = false;
		Comparendo actual = null;
		int tamano = pila.getSize();
		
		for(int i = 0; i < tamano && termine == false; i ++)
		{
			actual = pila.pop();
			
			if(actual.darCodInfeaccion().equals(pcodigo))
			{
				nuevo.push(actual);
			}
			
			if(nuevo.getSize() == PnumeroComparendos)
			{
				termine = true;
			}
		}
		
		
		return nuevo;
	}
	
	
//	465191
	public void cargarDatos() 
	{

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(PATH));
			JsonElement elem = JsonParser.parseReader(reader); 
			JsonArray e2 = elem.getAsJsonObject().get("features").getAsJsonArray();


			SimpleDateFormat parser=new SimpleDateFormat("yyyy/MM/dd");

			for(JsonElement e: e2) {
				int OBJECTID = e.getAsJsonObject().get("properties").getAsJsonObject().get("OBJECTID").getAsInt();

				String s = e.getAsJsonObject().get("properties").getAsJsonObject().get("FECHA_HORA").getAsString();	
				Date FECHA_HORA = parser.parse(s); 

				String MEDIO_DETE = e.getAsJsonObject().get("properties").getAsJsonObject().get("MEDIO_DETE").getAsString();
				String CLASE_VEHI = e.getAsJsonObject().get("properties").getAsJsonObject().get("CLASE_VEHI").getAsString();
				String TIPO_SERVI = e.getAsJsonObject().get("properties").getAsJsonObject().get("TIPO_SERVI").getAsString();
				String INFRACCION = e.getAsJsonObject().get("properties").getAsJsonObject().get("INFRACCION").getAsString();
				String DES_INFRAC = e.getAsJsonObject().get("properties").getAsJsonObject().get("DES_INFRAC").getAsString();	
				String LOCALIDAD = e.getAsJsonObject().get("properties").getAsJsonObject().get("LOCALIDAD").getAsString();

				double longitud = e.getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray()
						.get(0).getAsDouble();

				double latitud = e.getAsJsonObject().get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray()
						.get(1).getAsDouble();

				Comparendo c = new Comparendo(OBJECTID, FECHA_HORA, DES_INFRAC, MEDIO_DETE, CLASE_VEHI, TIPO_SERVI, INFRACCION, LOCALIDAD, longitud, latitud);
				pila.push(c);
				cola.enqueue(c);
				iniciado = 1;
			}

		} catch (FileNotFoundException | ParseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
}
