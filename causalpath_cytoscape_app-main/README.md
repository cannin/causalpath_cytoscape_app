![Build](https://github.com/cannin/causalpath_cytoscape_app/actions/workflows/maven_build.yml/badge.svg)

# CausalPath Cytoscape App

This is a Cytoscape app for the CausalPath method. CausalPath (causalpath.org) evaluates proteomic measurements against prior knowledge of biological pathways and infers causality between changes in measured features, such as global protein and phospho-protein levels. 

* CausalPath Publication: https://pubmed.ncbi.nlm.nih.gov/34179843/
* CausalPath Code: https://github.com/PathwayAndDataAnalysis/causalpath

## Instructions to Run the CausalPath Cytoscape App
### Pre- Requisites
```
Java
Maven
Cytoscape
```
### Step 1 
Clone this repository into the local desktop.
```
Navigate to the directory "<your path to this directory>\causalpath_cytoscape_app\CausalPath Cytoscape App\" using cmd 
or open Git Bash in the mentioned directory.
```
### Run the following commands
```
command 1.mvn compile
command 2.mvn clean install
```
A Build Success will be shown in the cmd and a jar file named <b>causalpath_cytoscape_app-1.0</b> will be created in <b>/target</b> folder of the current directory.
### Step 2
```
1.Copy the Jar file from the target folder into "C:\Users\<your PC_NAME>\CytoscapeConfiguration\3\apps\installed".
2.Open the Cytoscape App and click on Apps from the menu bar and then click on the CausalPath App. 
```


