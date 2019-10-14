package Laberinto;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import interfazGrafica.GUIMaze;

public class Maze{
	
	RalentizarTiempo ralentizar = new RalentizarTiempo();
	
	//Variable que modifica el tamaño del laberinto y a su vez le pide un tamaño por pantalla al usuario
	public static int tamanio = Integer.parseInt( JOptionPane.showInputDialog(
	        null,"Introduzca el tamaño del laberinto",
	        "Generador de laberinto",
	        JOptionPane.QUESTION_MESSAGE) );
	
	//Esta variable guardara un laberinto con solucion
	public static int[][] maze = generateMaze();
	
	public static ArrayList<int[][]> toDrawMatrices = new ArrayList<int [][]>();
	
	// Simbolos para que la visualizacion sea mas facil
	// 0 - obstaculo - '#'
	// 1 - espacio abierto - '.'
	// 2 - camino tomado - '+'
	// 3 - meta - '*'
	
	private static final char[] MAZE_SYMBOLS = {'#', '.', '+', '*' };

	//Tratar de encontrar la ruta desde la posición inicial del laberinto se resuelve imprimiendo la solución

	public static void main(String[] args) {
		compruebaSolucion();
		
		System.out.println("Si lo regreso");
		
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {
				System.out.print(maze[i][j]+" ");
			}
			System.out.println("");
		}
	}

	//Algoritmo backtracking, sera usado para generar el laberinto con solucion
	public static boolean findPathFrom(int row, int col) {

		// cuando alcanzamos el objetivo, hemos resuelto el problema.
		if (maze[row][col] == 3) {
			return true;
		}

		// agrega la posición a nuestro camino cambiando su valor a '2'
		maze[row][col] = 2;

		// prueba todos los vecinos disponibles
		// Norte (fila-1, col), sur (fila + 1, col), este (fila, col + 1) y oeste (fila, col-1)
		// si alguno de estos devuelve verdadero, entonces hemos resuelto el problema
		
		//Norte
		if (isAvailablePosition(row - 1, col) && findPathFrom(row - 1, col)) {
			return true;
		}
		
		//Sur
		if (isAvailablePosition(row + 1, col) && findPathFrom(row + 1, col)) {
			return true;
		}
		
		//Este
		if (isAvailablePosition(row, col - 1) && findPathFrom(row, col - 1)) {
			return true;
		}
		
		//Oeste
		if (isAvailablePosition(row, col + 1) && findPathFrom(row, col + 1)) {
			return true;
		}

		// Si ninguna de las posiciones anteriores es válida o coincide con el objetivo, es necesario revertir el
		//estado temporal. Esta reversión o retroceso es lo que le da nombre al algoritmo: Backtracking
		maze[row][col] = 1;

		return false;
	}

	// Una posición está disponible si: (1) está dentro de los límites del laberinto
	// (2) si es un espacio abierto o (3) es el objetivo
	private static boolean isAvailablePosition(int row, int col) {
		boolean result =  row >= 0 && row < maze.length
				&& col >= 0 && col < maze[row].length
				&& (maze[row][col] == 1 || maze[row][col] == 3);
				return result;
	}

	// imprime el resultado usando MAZE_SYMBOLS
	private static void print(){
		
		//Variable que guardara los pasos que esta ejecutando el Backtracking
		int aux[][]=new int[maze.length][maze.length];
		
		for(int row = 0; row < maze.length; ++row) {
			for(int col = 0; col < maze[row].length; ++col) {
				//System.out.print(MAZE_SYMBOLS[maze[row][col]]);
				//Guardamos en la variable aux el movimiento que esta generando en el momento
				//el backtracking, hay que tener en cuenta que no se puede guardar la variable
				//maze directamente, puesto que guardaria el mismo movimiento
				aux[row][col]=maze[row][col];
			}
			//System.out.println();
		}
		
		//añadimos la matriz, en el arrayList 
		toDrawMatrices.add(aux);
	}
	
	
	/**
	 * Metodo que genera laberintos aleatorios
	 * @return retorna el laberinto generado
	 */
	public static int[][] generateMaze()
	{
		Random rnd = new Random();
		
		int maze [][] = new int [tamanio][tamanio];
		
		//Llenamos la matriz con obstaculos 
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {
				
				//Llenamos los bordes con obstaculos
				if(i==0||j==0||j==maze.length-1||i==maze.length-1)
				{
					maze[i][j]=0;
				}
				
				//Genera obstaculos en las posiciones de j e i impares
				else if(j%2!=0&&i%2!=0)
				{
					maze[i][j]=0;
				}
				
				//En caso de ser par, dicha posicion sera llenada con un espacio abierto
				else {
					maze[i][j]=1;
				}
			}
		}
		
		//Unimos los obstaculos generados
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {
				//Nos situamos en la posicion de los obstacules diferente de los bordes
				//para poder dificultar el laberinto generando mas obstaculos
				if(j%2!=0&&i%2!=0&&i!=maze.length-1&&j!=maze.length-1)
				{
					//Esta variable escogera entre 4 posibilidades para poner el obstaculo
					int pon = rnd.nextInt(4)+1;
					switch (pon) {
					
					//obstaculo hacia arriba
					case 1:
						maze[i-1][j]=0;
						break;
						
					//Obstaculo hacia abajo
					case 2:
						maze[i+1][j]=0;
						break;
					case 3:
					//Obstaculo hacia la izquierda
						maze[i][j-1]=0;
						break;
					//Obstaculo hacia la derecha
					case 4:
						maze[i][j+1]=0;
						break;
					}
				}			
			}
		}
		
		//Definimos la meta
		maze[maze.length-2][maze.length-2]=3;
		
		//Definimos la posicion inicial
		maze[1][1]=2;
		
		
		
		return maze;
	}
	
	/**
	 * Metodo que regresa un laberinto con su solucion
	 */
	public static void compruebaSolucion()
	{
		//Si retorna true, es porque el laberinto tiene solucion
		if (findPathFrom(1, 1)) {
			borrarPisadas();
			return;
		} 
		//En caso de que retorne false, generamos otro laberinto aleatorio y de nuevo llamamos
		//a la funcion compruebaSolucion (Recursividad).
		else {
			maze = generateMaze();
			compruebaSolucion();
		}
	}
	
	/**
	 * Metodo que borra las pisadas del laberinto solucion
	 * @param maze
	 * @return
	 */
	public static int[][] borrarPisadas()
	{
		for (int i = 0; i < maze.length; i++){
			for (int j = 0; j < maze.length; j++){
				if(maze[i][j]==2){//Si hay una pisada
					maze[i][j]=1;//La convertimos en espsacio abierto
				}
			}
		}
		
		//Como la posicion incial fue borrada, la reestablecemos
		maze[1][1]=2;
		
		//retornamos el laberinto con solucion
		return maze;
	}
	
	
	//clonamos el algoritmo de backtracking para generar sus movimientos*******************
	public static boolean ClonfindPathFrom(int row, int col) {

		// cuando alcanzamos el objetivo, hemos resuelto el problema.
		if (maze[row][col] == 3) {
			return true;
		}

		// agrega la posición a nuestro camino cambiando su valor a '2'
		maze[row][col] = 2;
		
		print();
		System.out.println("\n");
		
		
		// prueba todos los vecinos disponibles
		// Norte (fila-1, col), sur (fila + 1, col), este (fila, col + 1) y oeste (fila, col-1)
		// si alguno de estos devuelve verdadero, entonces hemos resuelto el problema
		
		//Norte
		if (isAvailablePosition(row - 1, col) && ClonfindPathFrom(row - 1, col)) {
			return true;
		}
		
		//Sur
		if (isAvailablePosition(row + 1, col) && ClonfindPathFrom(row + 1, col)) {
			return true;
		}
		
		//Este
		if (isAvailablePosition(row, col - 1) && ClonfindPathFrom(row, col - 1)) {
			return true;
		}
		
		//Oeste
		if (isAvailablePosition(row, col + 1) && ClonfindPathFrom(row, col + 1)) {
			return true;
		}

		// Si ninguna de las posiciones anteriores es válida o coincide con el objetivo, es necesario revertir el
		//estado temporal. Esta reversión o retroceso es lo que le da nombre al algoritmo: Backtracking
		maze[row][col] = 1;

		return false;
	}
	
}
