Again, this project is unfinished. 

Currently, it can ingest and deserialize a .ngs graph and its perturbed counterpart, construct 
graphx graph objects from those files, conduct and aggregate the results of multiple random walks in
parallel on the perturbed graph, storing nodes visited, valuable data boolean, and the cost array at
each node, and a reduce operation is mostly finished to accumulate cost arrays and the likelihood that 
the array will store valuable data. 

What remains to be done is to calculate simranks for the nodes visited, attack decision functionality,
performance assessment, and to upload to AWS.

To run the program, generate graphs from NetGameSim with: 
java -Xms2G -Xmx30G -jar -DNGSimulator.NetModel.statesTotal=4000  target/scala-3.2.2/netmodelsim.jar
and rename the original graph file "original.ngs", the perturbed graph file "perturbed.ngs".

Clone this repo, and move those files into the "input" folder of the mitm project, and run the 
program from the Main file.
