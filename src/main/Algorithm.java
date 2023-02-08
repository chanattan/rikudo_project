package src.main;
import java.util.ArrayList;

public class Algorithm {
    
    //General backtrack algorithm
    public static void backtrack(Graph g, boolean boolDiam){ //The first node and the last node have to be labeled 
        ArrayList<Node> nodes = g.getNodes();
        Node start = g.getSource();
        Node terminal = g.getDestination();
        if(boolDiam){
            backtrackingDiamondsRec(g, start, terminal);
        }
        else{
            backtrackingRec(g, start, terminal);
        }
    }

    // Recursive backtracking solver without the diamonds (Depth-first search) on a labeled graph starting on the start node and ending on the terminal node 
	private static Graph backtrackingRec(Graph g, Node start, Node terminal){
        Node[] neigh;                   // Neighbors of the node start
        Graph solution = null;          // The graph with all nodes labeled. Is null if there is no solution
        System.out.print("*** start ");
        System.out.print(start.getLabel());
        System.out.println(" ***");
        Graph.pp(g);
        System.out.println("***************");
        if(start.getLabel() == terminal.getLabel()){          // if the path crosses all cells ->  terminal.getLabel()=g.size()
            return g;
        }
        else{                           // if start.getLabel() != g.size()
            neigh = start.getNeighbors();
            for(int i=0;i<6 && solution == null;i++){   // 6 because it has always 6 neighbors
                System.out.println(start.getLabel());
                if(neigh[i]!=null){
                    if((neigh[i].isFixed() && neigh[i].getLabel()==start.getLabel()+1) || (neigh[i].getLabel() == -1)){
                        neigh[i].setLabel(start.getLabel()+1);
                        solution = backtrackingRec(g,neigh[i],terminal);
                    }
                }
            }
            return solution;
        }
    }

    // Recursive backtracking solver with the diamonds (Depth-first search)
    private static Graph backtrackingDiamondsRec(Graph g, Node start, Node terminal){
        Node[] neigh;                     // Neighbors of the node start
        Graph solution = null;            // The graph with all nodes labeled. Is null if there is no solution

        if(start == terminal){
            return g;
        }
        else{
            Node.DIR[] dia=start.getDiamonds();
            Node[] prio_nei = new Node[] {start.getNeighbor(dia[0]),start.getNeighbor(dia[1])};
            //No diamond
            if(prio_nei[0]==null && prio_nei[1]==null){
                neigh = start.getNeighbors();
                for(int i=0;i<6 && solution == null;i++){   // 6 because it has always 6 neighbors
                    if(neigh[i]!=null){
                        if((neigh[i].isFixed() && neigh[i].getLabel()==start.getLabel()+1) || (neigh[i].getLabel() == -1)){
                            neigh[i].setLabel(start.getLabel()+1);
                            solution = backtrackingDiamondsRec(g,neigh[i],terminal);
                        }
                    }
                }
            }
            //One diamond and it is available
            else if((prio_nei[1]==null) && ((prio_nei[0].isFixed() && prio_nei[0].getLabel()==start.getLabel()+1) || (prio_nei[0].getLabel() == -1))){
                prio_nei[0].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[0],terminal);
            }
            //Symetrical case
            else if((prio_nei[0]==null) && ((prio_nei[1].isFixed() && prio_nei[1].getLabel()==start.getLabel()+1) || (prio_nei[1].getLabel() == -1))){
                prio_nei[1].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[1],terminal);
            }
            //Two diamonds but one is not available anymore
            else if(prio_nei[1]!=null && (prio_nei[0].getLabel() != -1)){
                prio_nei[1].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[1],terminal);
            }
            //Symetrical case
            else if(prio_nei[0]!=null && (prio_nei[1].getLabel() != -1)){
                prio_nei[0].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[0],terminal);
            }
            //In all the other cases the rules are broken, so it is not a valid graph

            return solution;
     
        }
    }
}