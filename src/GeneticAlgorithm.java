import java.util.Arrays;
/**
 * @author M.Taghizadeh
 */
public class GeneticAlgorithm {
	
    private int popSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    protected int tournamentSize;

    //constructor:
    public GeneticAlgorithm(int popSize, double mutationRate, double crossoverRate, int elitismCount,int tournamentSize) {
        this.popSize = popSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }


    public Population initPopulation(int chromosomeLength){
        //Initialize population
        Population population = new Population(this.popSize, chromosomeLength);
        return population;
    }
    
    /**
     * Calculate individual's fitness value
     * 
     */
    public double calcFitness(Individual individual, City cities[]){
        //Get fitness
        Route route = new Route(individual, cities);
        double fitness = 1 / route.getDistance();
                
        //Store fitness
        individual.setFitness(fitness);
        System.out.format("Fitness : %5f\n", fitness);
        return fitness;
    }

    /**
     * Evaluate population --> run calcFitness on each individual.
     */
    public void evalPopulation(Population population, City cities[]){
        double populationFitness = 0;
        
        //Loop over population evaluating individuals and summing population fitness
        for(Individual individual : population.getIndividuals()) {
            populationFitness += this.calcFitness(individual, cities);
        }
        
        double avgFitness = populationFitness / population.size();
        population.setPopulationFitness(avgFitness);
    }
 
    /**
     * Selects parent for crossover using tournament selection
     */
    public Individual selectParent(Population population) {
        //Create tournament
        Population tournament = new Population(this.tournamentSize);

        //Add random individuals to the tournament
        population.shuffle();
        for (int i = 0; i < this.tournamentSize; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }

        //Return the best
        return tournament.getFittest(0);
    }
	
    /*
     * Ordered crossover mutation
     * 
     * Chromosomes in the TSP require that each city is visited exactly once.
     * Uniform crossover can break the chromosome by accidentally selecting a
     * city that has already been visited from a parent; this would lead to one
     * city being visited twice and another city being skipped altogether.
     * 
     * Additionally, uniform or random crossover doesn't really preserve the
     * most important aspect of the genetic information: the specific order of a
     * group of cities.
     * 
     * We need a more clever crossover algorithm here. What we can do is choose
     * two pivot points, add chromosomes from one parent for one of the ranges,
     * and then only add not-yet-represented cities to the second range. This
     * ensures that no cities are skipped or visited twice, while also
     * preserving ordered batches of cities.
     * 
     * @param population
     * @return The new population
     */
    public Population crossoverPopulation(Population population){
        //Create new population
        Population newPopulation = new Population(population.size());
        
        //Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            //Get parent1
            Individual parent1 = population.getFittest(populationIndex);
            System.out.println("parent 1 :" +parent1);
            
            //Apply crossover to this individual?
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                //Find parent2 with tournament selection
                Individual parent2 = this.selectParent(population);
                System.out.println("parent 2 :" + parent2);
                
                //Create blank offspring chromosome
                int offspringChromosome[] = new int[parent1.getChromosomeLength()];
                Arrays.fill(offspringChromosome, -1);
                Individual offspring = new Individual(offspringChromosome);

                //Get subset of parent chromosomes
                int substrPos1 = (int) (Math.random() * parent1.getChromosomeLength());
                int substrPos2 = (int) (Math.random() * parent1.getChromosomeLength());

                //make the smaller the start and the larger the end
                final int startSubstr = Math.min(substrPos1, substrPos2);
                final int endSubstr = Math.max(substrPos1, substrPos2);
                System.out.println("startSubstr : "+startSubstr);
                System.out.println("endSubstr : "+endSubstr);
                    
                //Loop and add the sub tour from parent1 to our child
                for (int i = startSubstr; i <= endSubstr; i++) {
                    offspring.setGene(i, parent1.getGene(i));
                }

                //Loop through parent2's city tour
                for (int i = 0; i < parent2.getChromosomeLength(); i++) {
                    int parent2Gene = i + endSubstr +1;
                    System.out.println("parent2Gene"+parent2Gene);
                    if (parent2Gene >= parent2.getChromosomeLength()) {
                        parent2Gene -= parent2.getChromosomeLength();
                        System.out.println("new parent2Gene"+parent2Gene);
                    }

                    //If offspring doesn't have the city add it
                    System.out.println("parent2.getGene(parent2Gene)"+parent2.getGene(parent2Gene));
                    if(offspring.containsGene(parent2.getGene(parent2Gene)) == false) {
                        //Loop to find a spare position in the child's tour
                        for (int ii = 0; ii < offspring.getChromosomeLength(); ii++) {
                            //Spare position found, add city
                            if (offspring.getGene(ii) == -1) {
                                System.out.println("parent2.getGene(parent2Gene)"+parent2.getGene(parent2Gene));
                                offspring.setGene(ii, parent2.getGene(parent2Gene));
                                break;
                            }
                        }
                    }
                }

                //Add child
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                //Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }
            System.out.println("new child : "+newPopulation.getIndividual(populationIndex));
            System.out.println("----------------------------------------------");
        }       
        return newPopulation;
    }

   /*
    * Apply mutation to population
    * 
    * Because the traveling salesman problem must visit each city only once,
    * this form of mutation will randomly swap two genes instead of
    * bit-flipping a gene like in earlier examples.
    * 
    * @param population
    *            The population to apply mutation to
    * @return The mutated population
    */
    public Population mutatePopulation(Population population){
        // Initialize new population
        Population newPopulation = new Population(this.popSize);
        
        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

            // Skip mutation if this is an elite individual
            if (populationIndex >= this.elitismCount) {   
            	// System.out.println("Mutating population member "+populationIndex);
                // Loop over individual's genes
                for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {   
                    // System.out.println("\tGene index "+geneIndex);
                    // Does this gene need mutation?
                    if (this.mutationRate > Math.random()) {
                        // Get new gene position
                        int newGenePos = (int) (Math.random() * individual.getChromosomeLength());
                        // Get genes to swap
                        int gene1 = individual.getGene(newGenePos);
                        int gene2 = individual.getGene(geneIndex);
                        // Swap genes
                        individual.setGene(geneIndex, gene1);
                        individual.setGene(newGenePos, gene2);
                    }
                }
            }
            // Add individual to population
            newPopulation.setIndividual(populationIndex, individual);
        }
        // Return mutated population
        return newPopulation;
    }
}
