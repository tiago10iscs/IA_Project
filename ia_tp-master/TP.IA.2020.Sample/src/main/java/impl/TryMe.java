package impl;

import engine.Engine;
import engine.VirusConfiguration;
import engine.exceptions.ElementNotFoundException;
import engine.exceptions.VirusDoesNotExistException;
import engine.interfaces.ISolution;
import engine.results.Result;


/**
 * Classe main onde o programa será executado
 */
public class TryMe {

    final static int POPULATION_MAX = 100;
    final static double MUTATION_RATE = 0.01;
    final static double CROSSOVER_RATE = 0.95;
    final static int ELITISM = 4;

    public static void main(String args[]) throws VirusDoesNotExistException, ElementNotFoundException {


        int virus_id = 1;

        //1- Initialize the Engine for Virus number 1
        final Engine eng = new Engine(virus_id);

        //2- Let's add a new window so that we can see our solution later on
        eng.addVisualization("Best Solution");


        //3- Get the configuration of the Virus and print it to know what we are up against...
        VirusConfiguration conf = eng.getVirusConfiguration();


        GeneticAlgorithm ga = new GeneticAlgorithm(POPULATION_MAX, MUTATION_RATE, CROSSOVER_RATE, ELITISM, eng);

        Population population = ga.initPopulation();
        ga.evalPopulation(population);

        int generation = 1;

        while(!ga.isTerminationConditionMet(population)) {

            ISolution mySolution = new Solution(population.getVirus(0).getNodes(), population.getVirus(0).getEdges(), conf);
            Result res2 = eng.testSolution(mySolution);

            /*
            System.out.println("Found a solution after " + generation + " generations.");
            System.out.println(mySolution.toString());
            System.out.println("Best solution: \n" + res2.toString());
            */System.out.println("Fitness: \n" + population.getVirus(0).getFitness());

            population = ga.crossoverPopulation(population);
            population = ga.mutatePopulation(population);
            ga.evalPopulation(population);

            generation++;
            System.out.println("-------------------------------------------------------------------------------------------------");

        }


        ISolution myBestSolution = new Solution(population.getVirus(0).getNodes(), population.getVirus(0).getEdges(), conf);
        Result bestOne = eng.testSolution(myBestSolution);
        //eng.updateVisualization("Best Solution", myBestSolution);

        System.out.println("Found a solution after " + generation + " generations.");
        System.out.println(myBestSolution.toString());
        System.out.println("Best solution: \n" + bestOne.toString());
        System.out.println("Fitness: \n" + population.getVirus(0).getFitness());
        //eng.submit(myBestSolution, "The Fellowship Of The Virus");

    }

}
