import java.util.ArrayList;

public class TestGraph {

	public static Graph test1(){
		ArrayList<Node> nodes = new ArrayList<Node>();

		// Création des nodes (sans les liées)
		Node node1 = new Node(1, true);
		nodes.add(node1);

		for(int i = 0; i < 3; i++){
			nodes.add(new Node());
		}
		Node node5 = new Node(5, true);
		nodes.add(node5);

		// Liaison des nodes
		// 1
		Node.linkNodes(node1, nodes.get(1), Node.DIR.NE);
		Node.linkNodes(node1, node5, Node.DIR.NW);

		// 2
		Node.linkNodes(nodes.get(1), nodes.get(2), Node.DIR.NE);
		Node.linkNodes(nodes.get(1), nodes.get(3), Node.DIR.NW);

		// 4
		Node.linkNodes(nodes.get(3), nodes.get(2), Node.DIR.E);
		Node.linkNodes(nodes.get(3), node5, Node.DIR.SW);

		return new Graph(nodes);
	}
	
	public static Graph test2() {
		Node node = new Node(1, true);
		Node node2 = new Node(2, true);
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(node);
		nodes.add(node2);
		Node.linkNodes(node, node2, Node.DIR.NW);
		return new Graph(nodes);
	}
	
}