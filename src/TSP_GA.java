import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * @author M.Taghizadeh
 */
public class TSP_GA {
    static int ScreenSize = 600;
    static JFrame app = new JFrame("TSP Genetic Algorithm");
    static JFrame tsp_info_frame = new JFrame("Insert Information of TSP");
    static TextField txt_city_number = new TextField();
    static TextField txt_generation_number = new TextField();
    static TextField txt_pop_size = new TextField();
    static TextField txt_mute_rate = new TextField();
    static JLabel lbl_city_number = new JLabel("Number Of City");
    static JLabel lbl_generation_number = new JLabel("Max Generation Number");
    static JLabel lbl_pop_size = new JLabel("Population Size");
    static JLabel lbl_mute_rate = new JLabel("Mutation Rate");
    public static int Generations_Size;
    
    public static void main(String[] args) {
        
        //tsp info frame :
        tsp_info_frame.setSize(450,550);
        tsp_info_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tsp_info_frame.setResizable(true);
        tsp_info_frame.setLayout(null);
        tsp_info_frame.getContentPane().setBackground(new Color(153, 217, 234));
        
        //txts :
        txt_city_number.setBounds(250, 50, 100, 30);
        lbl_city_number.setBounds(50, 50, 170, 30);
        lbl_city_number.setForeground(new Color(64, 0, 0));
        lbl_city_number.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        txt_generation_number.setBounds(250, 120, 100, 30);
        lbl_generation_number.setBounds(50, 120, 190, 30);
        lbl_generation_number.setForeground(new Color(64, 0, 0));
        lbl_generation_number.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        txt_mute_rate.setBounds(250, 190, 100, 30);
        lbl_mute_rate.setBounds(50, 190, 170, 30);
        lbl_mute_rate.setForeground(new Color(64, 0, 0));
        lbl_mute_rate.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        txt_pop_size.setBounds(250, 260, 100, 30);
        lbl_pop_size.setBounds(50, 260, 170, 30);
        lbl_pop_size.setForeground(new Color(64, 0, 0));
        lbl_pop_size.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        //add to Frame:
        tsp_info_frame.add(txt_city_number);
        tsp_info_frame.add(txt_generation_number);
        tsp_info_frame.add(txt_mute_rate);
        tsp_info_frame.add(txt_pop_size);
        tsp_info_frame.add(lbl_city_number);
        tsp_info_frame.add(lbl_generation_number);
        tsp_info_frame.add(lbl_mute_rate);
        tsp_info_frame.add(lbl_pop_size);
        tsp_info_frame.setVisible(true);
        
        
        //run algorithm btn
        JButton btn_runTsp = new JButton("Run Genetic Algorithm");
        btn_runTsp.setBounds(15, 450, 400, 50);
        btn_runTsp.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn_runTsp.setForeground(Color.red);
        tsp_info_frame.add(btn_runTsp);
        
        btn_runTsp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                int numCities = Integer.valueOf(String.valueOf(txt_city_number.getText()));
                int Generation_NUumber = Integer.valueOf(String.valueOf(txt_generation_number.getText()));
                int popSize = Integer.valueOf(String.valueOf(txt_pop_size.getText()));
                Double muterate = Double.valueOf(String.valueOf(txt_mute_rate.getText()));
                tsp_info_frame.setVisible(false);
                run_GA(numCities, Generation_NUumber, popSize, muterate);
            }
        });
        
    }
   
    public static void run_GA(int nCity, int GenerationNumber, int PopSize, Double Muterate){
        
        //Create rnd cities:
        int numCities = nCity;
        Generations_Size = GenerationNumber;
        City cities[] = new City[numCities];
        int[][] tmp_cityPos = new int[numCities][2];//this matrix use for showing cities in JFrame;

        //create random cities:
        for(int cityIndex = 0; cityIndex < numCities; cityIndex++) {
            //Generate x,y position
            int xPos = (int) (480 * Math.random());
            int yPos = (int) (480 * Math.random());

            //Add city
            cities[cityIndex] = new City(xPos, yPos);
            System.out.println("City "+cityIndex+" ---> Position : ["+xPos +", "+yPos+"]");
            tmp_cityPos[cityIndex][0] = xPos;
            tmp_cityPos[cityIndex][1] = yPos;
        }
        
        //show rnd cost matrix
        System.out.print("\n[Cost Matrix]\n-----------------------------------\n");
        for(int i = 0; i<numCities; i++){
            for(int j = 0; j<numCities; j++){
                System.out.print((int)cities[i].distanceFrom(cities[j])+"\t");
            }
            System.out.println();
        }
        System.out.print("\n-----------------------------------\n");
        
        //Initial GA
        GeneticAlgorithm ga = new GeneticAlgorithm(PopSize, Muterate, 1, 0, 4);

        //Initialize population
        Population population = ga.initPopulation(cities.length);

        //Evaluate population
        ga.evalPopulation(population, cities);
        
        int generation = 1;
        //Start evolution loop
        while(generation<Generations_Size) {
            //Print fittest individual from population
            System.out.println("-----------------------------------");
            Route route = new Route(population.getFittest(0), cities);
            System.out.println("\n-----------------------------------");
            System.out.print("\nG"+generation+" Best ");
            route.getDistance();
            System.out.println("\n\n\n\n");
            System.out.println("__________________________________________________________________________________________________________");
            
            //Crossover
            population = ga.crossoverPopulation(population);

            //Mutation
            population = ga.mutatePopulation(population);

            //Evaluate population
            ga.evalPopulation(population, cities);

            //Next Generation
            generation++;
        }

        System.out.println("Stopped after " + Generations_Size+ " generations.");
        Route route = new Route(population.getFittest(0), cities);

        System.out.println("\nEND RESULT BY GA :");
        for(int i = 0; i<numCities; i++){
            System.out.print(route.endResult[i]+"\t");
        }route.getDistance();
        
        showJFrame(tmp_cityPos, route.endResult);
        
    }

    public static void showJFrame(int[][] city, int best_tour[] ){
        app.setSize(ScreenSize, ScreenSize+40);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setResizable(false);
        app.setLayout(new BorderLayout());
        app.setBackground(new Color(229, 228, 178));
        app.setVisible(true);
        
        JButton btn = new JButton("Best Tour of this Grid Network");
        btn.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn.setBackground(new Color(1, 254, 7));
        btn.setForeground(Color.BLUE);
        btn.setBounds(0, ScreenSize-50, ScreenSize, 60);
        app.add(btn);
        app.setResizable(false);
        app.setLayout(new BorderLayout());
        CityPanel city_panel = new CityPanel(city, best_tour, best_tour.length);
        app.add(city_panel);
        
        btn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                TourPanel tour_panel = new TourPanel(city, best_tour, best_tour.length);
                app.add(tour_panel);
                SwingUtilities.updateComponentTreeUI(app);
            }
        });
    }
}