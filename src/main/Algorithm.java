package src.main;
import java.util.ArrayList;

public class Algorithm {
    // Recursive backtracking solver without the diamonds (Depth-first search) on a labeled graph starting on the start node and ending on the terminal node 
	public Graph backtrackingRec(Graph g, Node start, Node terminal){
        Node[] nei;                     // Neighbors of the node start
        Graph solution = null;          // The graph with all nodes labeled. Is null if there is no solution

        if(start == terminal){          // if the path crosses all cells ->  terminal.getLabel()=g.size()
            return g;
        }
        else{                           // if start.getLabel() != g.size()
            nei = start.getNeighbors();
            for(int i=0;i<6 && solution == null;i++){
                if(nei[i]!=null){
                    if((nei[i].getIsFixed() && nei[i].getLabel()==start.getLabel()+1) || (nei[i].getLabel() == -1)){
                        nei[i].setLabel(start.getLabel()+1);
                        solution = backtrackingRec(g,nei[i],terminal);
                    }
                }
            }
            return solution;
        }
    }

    // Recursive backtracking solver with the diamonds (Depth-first search)
    public Graph backtrackingDiamondsRec(Graph g, Node start, Node terminal){
        Node[] nei;                     // Neighbors of the node start
    Graph solution = null;              // The graph with all nodes labeled. Is null if there is no solution

        if(start == terminal){
            return g;
        }
        else{
            Node.DIR[] dia=start.getDiamonds();
            Node[] prio_nei = new Node[] {start.getNeighbor(dia[0]),start.getNeighbor(dia[1])};
            //No diamond
            if(prio_nei[0]==null && prio_nei[1]==null){
                nei = start.getNeighbors();
                for(int i=0;i<6 && solution == null;i++){
                    if(nei[i]!=null){
                        if((nei[i].getIsFixed() && nei[i].getLabel()==start.getLabel()+1) || (nei[i].getLabel() == -1)){
                            nei[i].setLabel(start.getLabel()+1);
                            solution = backtrackingDiamondsRec(g,nei[i],terminal);
                        }
                    }
                }
            }
            //One diamond and it is available
            else if((prio_nei[1]==null) && ((prio_nei[0].getIsFixed() && prio_nei[0].getLabel()==start.getLabel()+1) || (prio_nei[0].getLabel() == -1))){
                prio_nei[0].setLabel(start.getLabel()+1);
                solution = backtrackingDiamondsRec(g,prio_nei[0],terminal);
            }
            //Symetrical case
            else if((prio_nei[0]==null) && ((prio_nei[1].getIsFixed() && prio_nei[1].getLabel()==start.getLabel()+1) || (prio_nei[1].getLabel() == -1))){
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