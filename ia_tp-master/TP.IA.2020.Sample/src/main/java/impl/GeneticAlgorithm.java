package impl;

import engine.Engine;
import engine.VirusConfiguration;
import engine.exceptions.ElementNotFoundException;
import engine.exceptions.VirusDoesNotExistException;
import engine.interfaces.IEdge;
import engine.interfaces.INode;
import engine.results.EdgeResult;
import engine.results.NodeResult;
import engine.results.Result;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe que representa o Algoritmo Genetico
 */
public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    Engine eng;

//Construtores da classe
    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, Engine eng)  {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.eng = eng;
    }

//Inicializar uma população
    public Population initPopulation() {
        Population population = new Population(this.populationSize, this.eng);
        return population;
    }

//Metodo para calcular o fitness (quanto mais alto for o fitness melhor, neste caso significa que maior é a semelhança com o virus ja criado)
    public double calcFitness(Virus virus) throws ElementNotFoundException, VirusDoesNotExistException {
        VirusConfiguration conf = eng.getVirusConfiguration();
        Solution solution = new Solution(virus.getNodes(), virus.getEdges(), conf);
        Result res = eng.testSolution(solution);

        Iterator itr = res.getNodeResults().iterator();
        double correctNodes = 0;
        double correctEdges = 0;

        while (itr.hasNext()) {
            NodeResult nRes = (NodeResult) itr.next();

            if (nRes.isIs_x_ok()) {
                correctNodes += 0.33;
            }
            if (nRes.isIs_y_ok()) {
                correctNodes += 0.33;
            }
            if(nRes.isIs_type_ok()) {
                correctNodes += 0.34;
            }
        }

        itr = res.getEdgeResults().iterator();

        while (itr.hasNext()) {
            EdgeResult nRes = (EdgeResult) itr.next();

            if (nRes.isOne_node_ok()) {
                    correctEdges += 0.25;
            }
            if (nRes.isIs_weight_ok()) {
                    correctEdges += 0.50;
            }
            if (nRes.isBoth_nodes_ok()) {
                    correctEdges += 0.25;
            }
        }

        double fitness = ((correctNodes / virus.getnNodes()) + (correctEdges / virus.getnEgdes())) / 2;
        virus.setFitness(fitness);
        virus.setNodeFitness((correctNodes / virus.getnNodes()));
        virus.setEdgeFitness((correctEdges / virus.getnEgdes()));

        return fitness;
    }

//Metodo para avaliar uma população
    public void evalPopulation(Population population) {

        //double populationFitness = 0;

        for (Virus virus : population.getVirusArr()) {
            try {
                calcFitness(virus);
            } catch (ElementNotFoundException | VirusDoesNotExistException e) {
                e.printStackTrace();
            }
        }

        population.setPopulationFitness(population.getFittest(0).getFitness());
    }

//Metodo que verifica se a condição de paragem foi verificada
    public boolean isTerminationConditionMet(Population population) {
        for (Virus virus : population.getVirusArr()) {
            if((virus.getFitness()) == 1.0) {
                return true;
            }
        }
        return false;
    }

//Metodo que seleciona o par para um determinado Virus
    public Virus selectParent(Population population) {
        ArrayList<Virus> virus = new ArrayList(population.getPopulationSize());

        for(int i = 0; i < population.getVirusArr().size(); i++) {
            Virus newVirus = new Virus(population.getVirusArr().get(i));
            virus.add(newVirus);
        }

        double populationFitness = population.getPopulationFitness();
        double rouletteWheelPosition = Math.random() * populationFitness;

        double spinWheel = 0;
        for(Virus individual : virus) {
            spinWheel += individual.getFitness();
            if(spinWheel >= rouletteWheelPosition) {
                return individual;
            }
        }
        return virus.get(population.getPopulationSize() - 1);
    }

//Metodo que verifica e faz o crossover na população de Virus
    public Population crossoverPopulation(Population population) {
        Population newPopulation = new Population(population.getPopulationSize(), this.eng);
        VirusConfiguration conf = this.eng.getVirusConfiguration();

        for(int populationIndex = 0; populationIndex < population.getPopulationSize(); populationIndex++) {
            Virus virusA = new Virus(population.getVirus(populationIndex));

            if(this.crossoverRate > Math.random() && populationIndex > this.elitismCount) {
                Virus offspring = new Virus(conf.getN_edges(), conf.getN_nodes());
                Virus virusB = new Virus(selectParent(population));


                    for (int nodeIndex = 0; nodeIndex < virusA.getNodes().size(); nodeIndex++) {
                        if (0.5 > Math.random()) {
                            if (repeatedNode(offspring, virusA.getNodes().get(nodeIndex), nodeIndex)) {
                                offspring.setNode(nodeIndex, new GraphNode(virusB.getNodes().get(nodeIndex)));
                            } else {
                                offspring.setNode(nodeIndex, new GraphNode(virusA.getNodes().get(nodeIndex)));
                            }
                        } else {
                            if (repeatedNode(offspring, virusB.getNodes().get(nodeIndex), nodeIndex)) {
                                offspring.setNode(nodeIndex, new GraphNode(virusA.getNodes().get(nodeIndex)));
                            } else {
                                offspring.setNode(nodeIndex, new GraphNode(virusB.getNodes().get(nodeIndex)));
                            }
                        }
                    }

                    for(int edgeIndex = 0; edgeIndex < virusA.getEdges().size(); edgeIndex++) {
                    if(0.5 > Math.random()) {
                        if(repeatedEdge(offspring, virusA.getEdges().get(edgeIndex), edgeIndex)) {
                            offspring.setEdge(edgeIndex, new GraphEdge(virusB.getEdges().get(edgeIndex)));
                        } else {
                            offspring.setEdge(edgeIndex, new GraphEdge(virusA.getEdges().get(edgeIndex)));
                        }
                    } else {
                        if(repeatedEdge(offspring, virusB.getEdges().get(edgeIndex), edgeIndex)) {
                            offspring.setEdge(edgeIndex, new GraphEdge(virusA.getEdges().get(edgeIndex)));
                        } else {
                            offspring.setEdge(edgeIndex, new GraphEdge(virusB.getEdges().get(edgeIndex)));
                        }
                    }
                }

                newPopulation.setVirus(populationIndex, offspring);
            } else {
                newPopulation.setVirus(populationIndex, population.getVirus(populationIndex));
            }
        }
        return newPopulation;
    }

//Metodo que verifica e cria uma mutação na população de Virus
    public Population mutatePopulation(Population population) {
        Population newPopulation = new Population(this.populationSize, this.eng);

        for(int populationIndex = 0; populationIndex < population.getPopulationSize(); populationIndex++) {
            Virus virus = new Virus(population.getVirus(populationIndex));

            for(int nodeIndex = 0; nodeIndex < virus.getnNodes(); nodeIndex++) {
                if(populationIndex >= this.elitismCount) {
                    if(this.mutationRate > Math.random()) {
                        virus.setNode(nodeIndex, new GraphNode(virus.randomNode(this.eng)));

                    }
                }
            }

            for(int edgeIndex = 0; edgeIndex < virus.getnEgdes(); edgeIndex++) {
                if(populationIndex >= this.elitismCount) {
                    if(this.mutationRate > Math.random()) {
                        virus.setEdge(edgeIndex, new GraphEdge(virus.randomEdge()));
                    }
                }
            }

            newPopulation.setVirus(populationIndex, virus);
        }

        return newPopulation;
    }

    public static boolean repeatedNode(Virus virus, INode node, int index) {
        int i = 0;
        for(INode _node : virus.getNodes()) {
            if(i == index) {
                return false;
            }

            if(_node.getX()  == node.getX() && _node.getY() == node.getY()) {
                return true;
            }
            i++;
        }

        return false;
    }

    public static boolean repeatedEdge(Virus virus, IEdge edge, int index) {
        int i = 0;
        for(IEdge _edge : virus.getEdges()) {
            if(i == index) {
                return false;
            }

            if(_edge.getLabel().equals(edge.getLabel())) {
                return true;
            }
            i++;
        }

        return false;
    }
}

