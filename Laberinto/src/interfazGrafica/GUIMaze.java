package interfazGrafica;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

import Laberinto.Maze;
import Laberinto.RalentizarTiempo;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.JPanel;
import javax.swing.*; 

public class GUIMaze extends JFrame{

	//Instanciacion de la clase Maze
	static Maze maze = new Maze();
	
	//Instanciacion de la clase RalentizarTiempo 
	RalentizarTiempo ralentizar = new RalentizarTiempo();
	
	//Este arrayList aloja los movimiento generados que se guardaron en el algoritmo de backtracking
	static ArrayList<int[][]> toDraw = maze.toDrawMatrices; 
	
	//El tamaño del laberinto afectara al tamaño del JFrame
	static int tamanio=maze.tamanio;

	//Frame que se le mostrara al usuario para visualizar el laberinto
	private JFrame frame;
	
	/**
	 * Lanzamiento del programa
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		public void run() 
		{
			GUIMaze window = new GUIMaze();
			window.setVisible(true);
		}
		});
	}

	/**
	 * Creamos el programa
	 */
	public GUIMaze() {
		initialize();
	}

	/**
	 * Inicializacion de los contenidos del frame
	 */
	private void initialize() {
		setTitle("Juego laberinto - Adrian Hoyos - NRC: 20511");
		
		setSize(tamanio*40,tamanio*42);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		//Comprobamos si el laberinto generado tiene solucion
		maze.compruebaSolucion();
		
		//Llamamos al clon de findPathForm, puesto que el original fue usado para generar el laberinto con solucion
		maze.ClonfindPathFrom(1, 1);
	}
	
	//Creamos las variables que pintaran el laberinto
	ImageIcon obstaculo = new ImageIcon("src/imagenesLab/obstaculo40.png");//Imagen del obstaculo
	ImageIcon suelo = new ImageIcon("src/imagenesLab/suelo40.png");//Imagen del espacio abierto
	ImageIcon personaje = new ImageIcon("src/imagenesLab/personaje40.png");//Imagen del personaje
	ImageIcon meta = new ImageIcon("src/imagenesLab/meta.png");//Imagen de la meta
	
	//Pintamos el laberinto con las variables anteriores
	public void paint(Graphics g)
	{
		super.paint(g);
		
		//Trassladamos el laberinto un poco hacia abajo para poderlo visualizar correctamente
		g.translate(5,25);
		
		//Este for recorre las matrices enviadas por el algoritmo backtracking de la clase
		//Maze
		for(int i=0;i<toDraw.size();i++)
		{
			
			//Estos dos for recorren la posicion de cada una de las matrices
			for (int h = 0; h < maze.maze.length; h++) {
				for (int j = 0; j < maze.maze[h].length; j++) {
					//Segun el valor que s encuentre en ese momento de la matriz, sera pintado
					//con la imagen, teniendo en cuenta que ->
					// 0 -> Obstaculo; 1 -> Espacio abierto; 2-> Personaje; 3 -> Meta
					switch(toDraw.get(i)[h][j])
					{
						case 0: g.drawImage(obstaculo.getImage(), 40*j, 40*h, null); break;
						case 1: g.drawImage(suelo.getImage(), 40*j, 40*h, null); break;
						case 2: g.drawImage(personaje.getImage(), 40*j, 40*h, null); break;
						case 3: g.drawImage(meta.getImage(), 40*j, 40*h, null);break;
						
					}
					
				}
			}
			
			
			//Este metodo ejecuta una accion y la "duerme" ciertos milisegundos
			ralentizar.ralentizar();
		}
		
		//Mensaje de que se ha encontrado la solucion
		JOptionPane.showMessageDialog(null,"Solucion encontrada!");
	}
	
	

}
