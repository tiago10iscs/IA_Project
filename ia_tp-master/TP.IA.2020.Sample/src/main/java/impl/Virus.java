package impl;

import engine.Engine;
import engine.VirusConfiguration;
import engine.enums.NodeType;
import engine.interfaces.IEdge;
import engine.interfaces.INode;
import java.util.*;

/**
 * Classe que representa um Virus (representado na forma de um grafo (composto por nos e arestas))
 */
public class Virus {
    private ArrayList<INode> nodes;
    private ArrayList<IEdge> edges;
    private int nEgdes;
    private int nNodes;
    private double fitness;
    private double nodeFitness;
    private double edgeFitness;
    private static Random rnd = new Random();

//Construtores da classe
    public Virus(int originX, int originY, int yMax, int xMax, int nNodes, int nEdges) {
        this.nodes = new ArrayList<>(nNodes);
        this.edges = new ArrayList<>(nEdges);
        this.nEgdes = nEdges;
        this.nNodes = nNodes;


        //Gerar Nodes
        for (int i = 0; i < nNodes; i++) {
            boolean repeat = false;
            GraphNode newNode = new GraphNode(randomType(), new Random().nextInt(), randomBounded(originX, originX + xMax), randomBounded(originY, originY + yMax));

            for (INode node : nodes) {
                if (node.getX() == newNode.getX() && node.getY() == newNode.getY()) {
                    repeat = true;
                    i--;
                }
            }

            if (repeat == false) {
                nodes.add(newNode);
            }
        }

        //Gerar Arestas
        for(int i = 0; i < nEdges; i++) {
            int n1 = randomBounded(0, nNodes - 1);
            int n2;

            do {
                n2 = randomBounded(0, nNodes - 1);
            } while (n2 == n1);

            IEdge e1 = new GraphEdge(this.nodes.get(n1), this.nodes.get(n2), randomBounded(1,3), randomLabel());
            this.edges.add(e1);
        }
    }

    public Virus(Virus virus) {
        this.edges = new ArrayList(virus.getnEgdes());
        this.nodes = new ArrayList(virus.getnNodes());

        for(int i = 0; i  < virus.getnNodes(); i++) {
            GraphNode newNode = new GraphNode(virus.getNodes().get(i));
            this.nodes.add(newNode);
        }

        for(int j = 0; j  < virus.getnEgdes(); j++) {
            GraphEdge newEdge = new GraphEdge(virus.getEdges().get(j));
            this.edges.add(newEdge);
        }

        this.nNodes = virus.getnNodes();
        this.nEgdes = virus.getnEgdes();
        this.fitness = virus.getFitness();
    }

    public Virus(int nEdges, int nNodes) {
        this.nEgdes = nEdges;
        this.nNodes = nNodes;

        this.nodes = new ArrayList<>(nNodes);
        this.edges = new ArrayList<>(nEdges);

        for (int i = 0; i < nNodes; i++) {
            INode newNode = new GraphNode(randomType(), new Random().nextInt(), randomBounded(0,10), randomBounded(11,20));
            this.nodes.add(newNode);
        }

        for (int i = 0; i < nEdges; i++) {
            IEdge newEdge = new GraphEdge(this.nodes.get(0), this.nodes.get(1), 5, randomLabel());
            this.edges.add(newEdge);
        }

    }

//Metodos Get e Set


    public double getNodeFitness() {
        return nodeFitness;
    }

    public void setNodeFitness(double nodeFitness) {
        this.nodeFitness = nodeFitness;
    }

    public double getEdgeFitness() {
        return edgeFitness;
    }

    public void setEdgeFitness(double edgeFitness) {
        this.edgeFitness = edgeFitness;
    }

    public int getnEgdes() {
        return nEgdes;
    }

    public int getnNodes() {
        return nNodes;
    }

    public ArrayList<INode> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<INode> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<IEdge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<IEdge> edges) {
        this.edges = edges;
    }

    public void setNode(int index, INode node) {
        this.nodes.set(index, node);
    }

    public void setEdge(int index, IEdge edge) {
        this.edges.set(index, edge);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

//Metodo que permite gerar um tipo de Node aleatoriamente, com base nos tipos presentes na enumeração NodeType
    public static NodeType randomType(){
        int pick = new Random().nextInt(NodeType.values().length);
        return NodeType.values()[pick];
    }

//Metodo que permite gerar uma localização aleatoria mas delimitada por uma area á escolha (min, max)
    private static int randomBounded(int min, int max){
        return rnd.nextInt(max - min + 1) + min;
    }

//Metodo que permite gerar uma label aleatoria
    private static String randomLabel() {
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 7;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphabet.length());

            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        return sb.toString();
    }

//Metodo que gera um nó do grafo aleatoriamente
    public GraphNode randomNode(Engine eng) {
        VirusConfiguration conf = eng.getVirusConfiguration();
        boolean repeat = false;
        GraphNode newNode;

        do {
            newNode = new GraphNode(randomType(), rnd.nextInt(), randomBounded(conf.getX_origin(), conf.getX_origin() + conf.getWidth()), randomBounded(conf.getY_origin(), conf.getY_origin() + conf.getHeigth()));
            for (INode node : nodes) {
                if (node.getX() == newNode.getX() && node.getY() == newNode.getY()) {
                    repeat = true;
                } else {
                    repeat = false;
                }
            }
        } while (repeat);

        return newNode;
    }

    public GraphEdge randomEdge() {
        GraphEdge newEdge;

        int n1 = randomBounded(0, nNodes - 1);
        int n2;

        do {
            n2 = randomBounded(0, nNodes - 1);
        } while (n2 == n1);

        newEdge = new GraphEdge(this.nodes.get(n1), this.nodes.get(n2), randomBounded(1,3), randomLabel());

        return newEdge;
    }

}


