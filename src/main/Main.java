package src.main;

public class Main {
	public static void main(String arg[]){
		Graph g = TestGraph.test4(7,10,0);
		if(!Algorithm.backtrack(g,true)){
			System.out.println("**************");
			System.out.println("SOS");
		}
		else{
			System.out.println("***solution***");
			Graph.pp(g);
		}
		System.out.println("**************");
	}
}
