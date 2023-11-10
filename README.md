Again, this project is unfinished. 

Currently, it can ingest and deserialize a .ngs graph and its perturbed counterpart, construct 
graphx graph objects from those files, conduct and aggregate the results of multiple random walks in
parallel on the perturbed graph, storing nodes visited, valuable data boolean, and the weight array of edges at
each node. A map/reduce operation aggregates node arrays and the probability that a node with a given weight array will have valuable data. For each node visited in the random walks, a simrank/jaccard probability is calculated from every node in the original graph, and if the graph exceeds a valuable data probability and similarity thresholds, the trigger to attack is executed.

What remains to be done is attack decision functionality, performance assessment, and to upload to AWS.

To run the program, generate graphs from NetGameSim with: 
java -Xms2G -Xmx30G -jar -DNGSimulator.NetModel.statesTotal=4000  target/scala-3.2.2/netmodelsim.jar
and rename the original graph file "original.ngs", the perturbed graph file "perturbed.ngs".

Clone this repo, and move those files into the "input" folder of the mitm project, and run the 
program from the Main file.
