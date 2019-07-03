import java.util.ArrayList;
import java.util.Random;
/**
 * @author M.Taghizadeh
 */
public class Individual {
    /**
     * In this case, the chromosome is an array of integers rather than a string. 
     */
    private int[] chromosome;
    private double fitness = -1;

    public Individual(int[] chromosome) {
        //Create individualchromosome
        this.chromosome = chromosome;
    }

    public Individual(int chromosomeLength) {
        //Create random individual
        int[] individual;
        individual = new int[chromosomeLength];

        ArrayList<Integer> tmp_individual =new ArrayList();
        for(int gene = 0; gene < chromosomeLength; gene++) {
            tmp_individual.add(gene);
        }
        
        Random rnd = new Random();
        for(int gene = 0; gene < chromosomeLength; gene++) {
            int tmp_index = rnd.nextInt(tmp_individual.size());
            individual[gene] = tmp_individual.get(tmp_index);
            tmp_individual.remove(tmp_individual.get(tmp_index));
        }

        this.chromosome = individual;
    }

    public int[] getChromosome() {
        return this.chromosome;
    }

    public int getChromosomeLength() {
        return this.chromosome.length;
    }

    public void setGene(int offset, int gene) {
        this.chromosome[offset] = gene;
    }

    public int getGene(int offset) {
        return this.chromosome[offset];
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
            return this.fitness;
    }

    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene] + ",";
        }
        return output;
    }

    /**
     * Search for a specific integer gene in this individual;
     */
    public boolean containsGene(int gene) {
        for (int i = 0; i < this.chromosome.length; i++) {
            if (this.chromosome[i] == gene) {
                    return true;
            }
        }
        return false;
    }
}
