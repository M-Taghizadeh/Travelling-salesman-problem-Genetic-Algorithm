import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
/**
 * @author M.Taghizadeh
 */
public class TourPanel extends JPanel{
    public int[][] tmp_cityPos;
    public int[] endResult;
    public int nCity;
    static Timer t;
    
    TourPanel(int[][] tmp_cityPos, int[] endresult, int numCity){
        this.endResult = new int[numCity];
        this.endResult = endresult;
        this.tmp_cityPos = new int[numCity][2];
        this.tmp_cityPos = tmp_cityPos;
    }
    
    @Override 
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        
        g.setColor(Color.BLUE);
        drawTour(g);
    }
     
    public void drawTour(Graphics g){
        int count = 0;
        while(count<=endResult.length-2){
            int i = endResult[count];
            int j = endResult[count+1];
            int step = count;
            ActionListener al = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    Path line = new Path(tmp_cityPos[i][0], tmp_cityPos[i][1], tmp_cityPos[j][0], tmp_cityPos[j][1]);
                    TSP_GA.app.add(line);
                    SwingUtilities.updateComponentTreeUI(TSP_GA.app);
                    
                    if(step==endResult.length-2){
                        ActionListener end_al = new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Path end_line = new Path(tmp_cityPos[endResult[endResult.length-1]][0], tmp_cityPos[endResult[endResult.length-1]][1], tmp_cityPos[endResult[0]][0], tmp_cityPos[endResult[0]][1]);
                                TSP_GA.app.add(end_line);
                                SwingUtilities.updateComponentTreeUI(TSP_GA.app);
                            }
                        };
                        Timer end_t = new Timer(300, end_al);
                        end_t.start();
                        end_t.setRepeats(false);
                        t.stop();
                    }
                    
                }
            };
            
            t = new Timer(count*300, al);
            t.start();
            t.setRepeats(false);
            count++;
        }
    }
}