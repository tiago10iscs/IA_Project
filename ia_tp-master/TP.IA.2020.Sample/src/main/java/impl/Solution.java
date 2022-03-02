package impl;

import engine.*;
import engine.enums.NodeType;
import engine.exceptions.ElementNotFoundException;
import engine.interfaces.IEdge;
import engine.interfaces.INode;
import engine.interfaces.ISolution;
import engine.results.Result;
import org.miv.pherd.geom.Point2;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Classe que representa uma Solução
 */
public class Solution implements ISolution
{
    private VirusConfiguration virusConfiguration;
    private ArrayList<INode> nodes;
    private ArrayList<IEdge> edges;

//Construtores da classe
    public Solution(ArrayList<INode> nodes, ArrayList<IEdge> edges, VirusConfiguration virusConfiguration) {
        this.nodes = nodes;
        this.edges = edges;
        this.virusConfiguration = virusConfiguration;
    }

//Metodos Get e Set
    public ArrayList<INode> getNodes() {
        return this.nodes;
    }

    public void setNodes(ArrayList<INode> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<IEdge> getEdges() {
        return this.edges;
    }

    public void setEdges(ArrayList<IEdge> edges) {
        this.edges = edges;
    }

//Metodo toString()
    @Override
    public String toString() {
        return "Solution{" +
                "configuration=" + virusConfiguration+
                ", \nnodes=" + nodes.stream().map(x -> x.toString()).reduce((x,y) -> x + ", \n\t"+y) +
                ", \nedges=" + edges.stream().map(x -> x.toString()).reduce((x,y) -> x + ", \n\t"+y) +
                '}';
    }

//Metodo que permite obter a configuração de um virus, ou seja, a sua posição de origem, altura, largura, etc...
    public VirusConfiguration getVirusConfiguration() {
        return virusConfiguration;
    }

//Metodo que permite atribuir valores á configuração do virus, ou seja, atribuir uma determinada coordenada, p.ex
    public void setVirusConfiguration(VirusConfiguration virusConfiguration) {
        this.virusConfiguration = virusConfiguration;
    }
}
