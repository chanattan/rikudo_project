import java.lang.Math;   

import java.util.ArrayList;

public class TestGraph {
	
	public static Graph test() {
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node n = new Node(1, false);
        Node n2 = new Node(2, false);
        Node n3 = new Node(3, false);
        Node n4 = new Node(4, false);
        Node n5 = new Node(5, false);
        nodes.add(n);
        nodes.add(n2);
//        nodes.add(n3);
//        nodes.add(n4);
//        nodes.add(n5);
        Node.linkNodes(n, n2, Node.DIR.NE);
//        Node.linkNodes(n3, n2, Node.DIR.NE);
//        Node.linkNodes(n3, n4, Node.DIR.W);
//        Node.linkNodes(n4, n5, Node.DIR.NW);
        return new Graph(nodes);
    }
	
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

	// Generate a hexagonal graph with random start & end point
	// n is the size of the middle line
	// We must have n>4
	public static Graph testHexa(int n){
		if(n<4){
			System.out.println("Erreur dans le passage de l'argument de testHexa");
			return null;
		}
		ArrayList<Node> nodes = new ArrayList<Node>();
		int len = n;
		for(int i=4;i<n;i++){
			len+=2*i;
		}
		for(int i = 0; i < len; i++){
			nodes.add(new Node());
		}
		// First line (size 4)
		nodes.get(0).setNeighbor(nodes.get(1),Node.DIR.EAST);
		nodes.get(0).setNeighbor(nodes.get(4),Node.DIR.SOUTH_WEST);
		nodes.get(0).setNeighbor(nodes.get(5),Node.DIR.SOUTH_EAST);


		nodes.get(1).setNeighbor(nodes.get(2),Node.DIR.EAST);
		nodes.get(1).setNeighbor(nodes.get(0),Node.DIR.WEST);
		nodes.get(1).setNeighbor(nodes.get(5),Node.DIR.SOUTH_WEST);
		nodes.get(1).setNeighbor(nodes.get(6),Node.DIR.SOUTH_EAST);


		nodes.get(2).setNeighbor(nodes.get(3),Node.DIR.EAST);
		nodes.get(2).setNeighbor(nodes.get(1),Node.DIR.WEST);
		nodes.get(2).setNeighbor(nodes.get(6),Node.DIR.SOUTH_WEST);
		nodes.get(2).setNeighbor(nodes.get(7),Node.DIR.SOUTH_EAST);

		nodes.get(3).setNeighbor(nodes.get(2),Node.DIR.WEST);
		nodes.get(3).setNeighbor(nodes.get(7),Node.DIR.SOUTH_WEST);
		nodes.get(3).setNeighbor(nodes.get(8),Node.DIR.SOUTH_EAST);

		//Northern lines
		int fst_pos=0;
		for(int num=5;num<n;num++){
			fst_pos += num-1;// nb of the first node of the line
			//First node
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos-num+1),Node.DIR.NORTH_EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+1),Node.DIR.EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+num+1),Node.DIR.SOUTH_EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+num),Node.DIR.SOUTH_WEST);
			//Middle nodes
			for(int j=1;j<num-1;j++){
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-num+1),Node.DIR.NORTH_EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+1),Node.DIR.EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+num+1),Node.DIR.SOUTH_EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+num),Node.DIR.SOUTH_WEST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-1),Node.DIR.WEST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-num),Node.DIR.NORTH_WEST);
			}
			//Last node
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)+num+1),Node.DIR.SOUTH_EAST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)+num),Node.DIR.SOUTH_WEST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-1),Node.DIR.WEST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-num),Node.DIR.NORTH_WEST);
		}

		//Middle line (size n)
		fst_pos += n-1;
		//First node
		nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos-n+1),Node.DIR.NORTH_EAST);
		nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+1),Node.DIR.EAST);
		nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+n),Node.DIR.SOUTH_EAST);
		//Middle nodes
		for(int j=1;j<n-1;j++){
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-n+1),Node.DIR.NORTH_EAST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+1),Node.DIR.EAST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+n),Node.DIR.SOUTH_EAST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+n-1),Node.DIR.SOUTH_WEST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-1),Node.DIR.WEST);
			nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-n),Node.DIR.NORTH_WEST);
		}
		//Last node
		nodes.get(fst_pos+n-1).setNeighbor(nodes.get((fst_pos+n-1)+n-1),Node.DIR.SOUTH_WEST);
		nodes.get(fst_pos+n-1).setNeighbor(nodes.get((fst_pos+n-1)-1),Node.DIR.WEST);
		nodes.get(fst_pos+n-1).setNeighbor(nodes.get((fst_pos+n-1)-n),Node.DIR.NORTH_WEST);

		//Southern lines
		for(int num=n-1;num>4;num--){
			fst_pos += num+1;// nb of the first node of the line
			//First node
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos-num-1),Node.DIR.NORTH_WEST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos-num),Node.DIR.NORTH_EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+1),Node.DIR.EAST);
			nodes.get(fst_pos).setNeighbor(nodes.get(fst_pos+num),Node.DIR.SOUTH_EAST);
			//Middle nodes
			for(int j=1;j<num-1;j++){
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-num-1),Node.DIR.NORTH_WEST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-num),Node.DIR.NORTH_EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+1),Node.DIR.EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+num),Node.DIR.SOUTH_EAST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)+num-1),Node.DIR.SOUTH_WEST);
				nodes.get(fst_pos+j).setNeighbor(nodes.get((fst_pos+j)-1),Node.DIR.WEST);
			}
			//Last node
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-num-1),Node.DIR.NORTH_WEST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-num),Node.DIR.NORTH_EAST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)-1),Node.DIR.WEST);
			nodes.get(fst_pos+num-1).setNeighbor(nodes.get((fst_pos+num-1)+num-1),Node.DIR.SOUTH_WEST);

		}
		//Last line (size 4)
		nodes.get(len-4).setNeighbor(nodes.get(len-9),Node.DIR.NORTH_WEST);
		nodes.get(len-4).setNeighbor(nodes.get(len-8),Node.DIR.NORTH_EAST);
		nodes.get(len-4).setNeighbor(nodes.get(len-3),Node.DIR.EAST);

		nodes.get(len-3).setNeighbor(nodes.get(len-8),Node.DIR.NORTH_WEST);
		nodes.get(len-3).setNeighbor(nodes.get(len-7),Node.DIR.NORTH_EAST);
		nodes.get(len-3).setNeighbor(nodes.get(len-2),Node.DIR.EAST);
		nodes.get(len-3).setNeighbor(nodes.get(len-4),Node.DIR.WEST);

		nodes.get(len-2).setNeighbor(nodes.get(len-7),Node.DIR.NORTH_WEST);
		nodes.get(len-2).setNeighbor(nodes.get(len-6),Node.DIR.NORTH_EAST);
		nodes.get(len-2).setNeighbor(nodes.get(len-1),Node.DIR.EAST);
		nodes.get(len-2).setNeighbor(nodes.get(len-3),Node.DIR.WEST);

		nodes.get(len-1).setNeighbor(nodes.get(len-2),Node.DIR.WEST);
		nodes.get(len-1).setNeighbor(nodes.get(len-6),Node.DIR.NORTH_WEST);
		nodes.get(len-1).setNeighbor(nodes.get(len-5),Node.DIR.NORTH_EAST);

		// To change later
		int s = (int)(Math.random()*len);
		int t = (int)(Math.random()*len);
		while(t==s){
			t = (int)(Math.random()*len);
		}
		nodes.get(s).setLabel(1);
		nodes.get(s).setIsFixed(true);
		nodes.get(t).setLabel(len);
		nodes.get(t).setIsFixed(true);

		Graph g = new Graph(nodes,nodes.get(s),nodes.get(t));
		return g;
	}
}