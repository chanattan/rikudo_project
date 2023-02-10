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

