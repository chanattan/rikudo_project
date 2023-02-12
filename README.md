# rikudo_project

Hello,

it is the Algorithms 1 class project wihch is about the Rikudo game made by
Regaud Gaëtan, Sok Chanattan and Timmerman Jules

# What is Rikudo ?
http://www.rikudo.fr/

## Goal

The goal is to put all the numbers from 1 to the number of cells of the instance in order to create a path of following numbers.

## Rules

Every number has to appear one and only one time in the grid (at the end, all the cells have to be filled with a number)
The first cell(with label : 1) and the last cell(with label : number of cells of the instance) are fixed.
Others cells can have a fixed number(label) that the player can not modifie.
There is a relation named diamond between cells. If a cell have a diamond on one of its edges, the cell on the other side of the edge is forced to be a consecutive number.

# Available Algorithm

## SAT

We are using a SAT-Solver (Sat4j) to solve the Rikudo problem. We are converting an instance of the problem to a logical formula. The exact working of the transformation and other remarks about the use of the library can be found in `SAT.pdf`

You can use the SAT algorithm using the function : `satSolve(Graph)`. It will return a boolean signifying whether there is a solution and modify the graph. 

## Backtrack

The implemantation of the backtracking is a naive implentation. It is a breath-first search.

We give to the backtracking algorithm the graph that corresponds to the Rikudo problem. It is modified by side effect.

The two backtracking algorithms are recursive and take the graph, the start node and the destination node.
Only `backtrackingDiamondsRec` takes the diamonds in consideration.

We return a boolean if we found a solution. We don't explore other possibilities when we have already found a solution.

If we don't find a solution, it explores all possibilies and return false. In this case, the graph is not modified.

If the graph given to the function doesn't follow the rules (for example if a node has three diamonds) the function still explore all the graph. We can kill the process in this case because it won't give a solution.


# Graph tests

We can generate three little graphs with `test1()`, `test2()` and `test3()`.

We can generate a hexagonal graph with `testHaxa(n, diam, fix)` where `n` is the number of nodes on the middle line (the weigth of the graph), `diam` is the maximum nuber of diamonds the graph will have, `fix` is the maximum number of fixed label nodes in the graph (without taking the start and the end node in consideration). The graph generated does not always respect the Rikudo rules.

In order to be sure of the existence of the solution, we can use `generateHex(n,diam,fix)` with the same arguments. There is safeguard for the arguments (no more diamonds and cells fixed than the number of cells), but the programm can run during a lot of time if the values are too big. In this case, it might mean that there is no valid graph that can verify the arguments and the Rikudo rules.

# Proof NP-complete
https://arxiv.org/pdf/2101.09332.pdf

## NP-Hard
To check if a instance is a solution, we only have to travel along the graph and check if there is a successor in the neighbors and if it always verifies the diamond relations.

It is polynomial (linear) on the size of the graph.

## NP-difficult
We reduce the problem to the problem of Hamiltonian circuits in hexagonalgrid graph.

### All the cells have 2 neighbors and there is no diamond
If we consider each cell as a vertex and each edge of two cells as an edge of the graph, this is exactly the situation of the Hamiltonian circuits in hexagonalgrid graph.

### All the cells have 2 or 3 neighbors and there are diamonds ()
The idea of the proof is :

We consider an instance of the Hamiltonian circuits in hexagonal grid graph problem. We put it on a grid with smaller hexagons such as :

- each node of the instance is on a corner of a small hexagon
- each center of the egde of the instance is at the center of a hexagon

Then we divide the grid with small hexagons on two types of cells :
- cells in the triangle centered on a node and cells on the middle of the edges
- the others cells (the holes of the graph)

The idea is that we can solve the problem by dividing it on these two parts :
- We can make an Hamiltonian path on each hole that begins and ends on the same triangle by puting diamonds in a particular way.
- We can find an Hamiltonian circuits on the triangles and the hexagons on the middle of the edges such as for each hole, there is a triangle that contains a couple of two neighboring cells in this circuit.

Thus, we can plug each circuit of the holes on the bigger circuit.
We created an Hamiltonian circuits.

# Interface

 We developed a GUI interface with Java using Swing and AWT to draw the hexagon maps and interact with the algorithms.
 You can run it using the command `java -jar RikudoCreatorJGC.jar` in the command line.
 The software first opens in *PLAY MODE* which is where you can play around with one graph.
 In order to switch between the modes you can use the key **M**.
 
 ## Play Mode
  In this mode, you have several buttons :
 
 - Solve SAT : considering the current graph on the play mode it tries to solve the graph using SAT problem, if the graph has a solution the label is solvable must print the value true and the label of the nodes of the graph should be changed to the solution. If there is no solution then is solvable prints false and the graph should not be changed or partially.
 
 - Solve BACKTRACK : this does the same thing as the Solve SAT but tries to solve the graph using backtracking, the solution might be slow to output on the graph.
 
 - Check Solution : considering that the current graph has been played and thus the nodes have been labelled with the hope of being a solution, then the proposed solution is checked using a verification algorithm, the answer is output in the label : is solution.
 
 Note that the algorithms may take time to run and the software may freeze in the meantime.
 
 ## Creator Mode
 In this mode, you can create your own puzzle (map) to try them on the play mode.
 You have several buttons :
 
 - Load : it loads the created graph into the play mode.
 
 - Generate : considering the parameters given into the dialog that pops up, it generates an hexagonal graph with the given parameters that has at least one solution. (it might show some problem at the moment, it needs to be fixed)
 
 The commands for creation are shown on the panel at the bottom right of the screen,
 you can drag your mouse around to drag the graph.
 
 ## Extensions/Remaining work
 This section is about the considered extensions and the remaining work that could have been implemented if given more time.
 
 - Fixing the padding in the graph that are drawn to the screen.
 - Adding more parameters for more complex puzzles in the generator.
 - Fixing labelling in the creator mode.
 - Making the software more interactive and easier to use for the user.
 - Implementing loading bars for the running algorithms that could take time.
 - Making the play mode to be as similar as the rikudo website's but better.
 - Going further in the implementation of the algorithms for them to be resilient to all graphs.
 
