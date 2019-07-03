import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
/**
 * @author M.Taghizadeh
 */
public class Path extends JPanel{
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    
    Path(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.setColor(Color.blue);
        g.drawLine(this.x1, this.y1, this.x2, this.y2);
    }
}
