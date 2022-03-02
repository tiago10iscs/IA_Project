package impl;

import engine.Engine;
import engine.VirusConfiguration;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Classe relativa a uma população de Virus
 */
public class Population {
    private ArrayList<Virus> virus;
    private double populationFitness;

//Construtores da classe
    public Population(int populationSize, Engine eng) {
        this.virus = new ArrayList(populationSize);
        VirusConfiguration conf = eng.getVirusConfiguration();

        for(int i = 0; i < populationSize; i++){
            Virus _virus = new Virus(conf.getX_origin(), conf.getY_origin(), conf.getHeigth(), conf.getWidth(), conf.getN_nodes(), conf.getN_edges());
            this.virus.add(_virus);
        }
    }

//Metodos Get e Set
    public ArrayList<Virus> getVirusArr() {
        return virus;
    }

    public Virus getVirus(int index) {
        return this.virus.get(index);
    }

    public void setVirus(int index, Virus virus) {
        this.virus.set(index, virus);
    }

    public double getPopulationFitness() {
        return populationFitness;
    }

    public void setPopulationFitness(double populationFitness) {
        this.populationFitness = populationFitness;
    }

//Metodo para obter o fittest
    public Virus getFittest(int offset){

        this.virus.sort(new Comparator<Virus>() {
            @Override
            public int compare(Virus o1, Virus o2) {
                if(o1.getFitness() > o2.getFitness()) {
                    return -1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return this.virus.get(offset);
    }

//Metodo que obtem o tamanho da população
    public int getPopulationSize(){
        return virus.size();
    }
}
