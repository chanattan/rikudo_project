package src.main;
import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> nodes;

	public Graph(ArrayList<Node> nodes){
		this.nodes = nodes;
	}


	public ArrayList<Node> getNodes(){
		return this.nodes;
	}
}