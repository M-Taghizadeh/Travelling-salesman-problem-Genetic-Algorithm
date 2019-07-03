import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
/**
 * @author M.Taghizadeh
 */
public class CityPanel extends JPanel{
    public int[][] tmp_cityPos;
    public int[] endResult;
    
    CityPanel(int[][] tmp_cityPos, int[] endresult, int numCity){
        this.endResult = new int[numCity];
        this.endResult = endresult;
        this.tmp_cityPos = new int[numCity][2];
        this.tmp_cityPos = tmp_cityPos;
    }
    
    @Override 
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        for(int i = 0; i<500; i=i+30){
            g.drawLine(i, 0, i, 500);
            g.drawLine(0, i, 500, i);
            g.drawString(String.valueOf(i), 520, i);
            g.drawString(String.valueOf(i), i, 520);
        }
        
        for(int index = 0; index<endResult.length; index++){
            g.setColor(Color.BLACK);
            g.fillOval(tmp_cityPos[index][0], tmp_cityPos[index][1], 10, 10);
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString(String.valueOf(index), tmp_cityPos[index][0]+10, tmp_cityPos[index][1]+10);
        }
    }
}