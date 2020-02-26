import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Graph extends JPanel{
		int[] values; 
		int[] values2;
	    public Graph() {
	       setSize(1000, 800);
	       values = new int[this.getWidth()];
	       values2 = new int[this.getWidth()];
	    }
	    
	    public void setValues(int[] v){
	    	for(int i = 0; i < values.length; i++){
	    		values[i] = v[i];
	    	}
	    }
	    public void setValues2(int[] v){
	    	for(int i = 0; i < values2.length; i++){
	    		values2[i] = v[i];
	    	}
	    }
	    @Override
	    public void paintComponent(Graphics g) {
	    	float total = 0;
	    	for(int i = 0;i<values.length;i++){
	    		total+=values[i];
	    	}
	    	//System.out.println(total);
	    	Color d = new Color(Color.HSBtoRGB((float)(-(total-8000)/30000+0.62), (float)1,(float) 0.6));
	    		g.setColor(d);
	    		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	    		g.setColor(Color.ORANGE);
	    		for(int i = 0; i < values.length - 10; i+=10){
	    			Color c = new Color(Color.HSBtoRGB(1-((float)i)/values.length/2+(float)0.8, 1, (float)1));
	    			g.setColor(c);
	    			int[] x = {i,i,i+10,i+10};
	    			int[] y = {0,values[i],values[i+10],0};
	    			g.fillPolygon(x, y, 4);
	    			c = new Color(Color.HSBtoRGB((1-(float)i)/values.length/2+(float)0.6, (float)0.9, (float).9));
	    			g.setColor(c);
	    			g.drawLine(i, values[i], i+10, values[i+10]);
	    			c = new Color(Color.HSBtoRGB((1-(float)i)/values.length/2+(float)0.6, (float)0.8, (float).9));
	    			g.setColor(c);
	    			g.drawLine(i, values[i]+1, i+10, values[i+10]+1);
	    			c = new Color(Color.HSBtoRGB((1-(float)i)/values.length/2+(float)0.6, (float)0.7, (float).9));
	    			g.setColor(c);
	    			g.drawLine(i, values[i]+2, i+10, values[i+10]+2);

	    		}
	    		for(int i = 0; i < values2.length - 10; i+=10){
	    			Color c = new Color(Color.HSBtoRGB(1-((float)i)/values2.length/2+(float)0.8, 1, (float)1));
	    			g.setColor(c);
	    			int[] x = {i,i,i+10,i+10};
	    			int[] y = {743,743-values2[i],743-values2[i+10],743};
	    			g.fillPolygon(x, y, 4);
	    			c = new Color(Color.HSBtoRGB((1-(float)i)/values2.length/2+(float)0.6, (float)0.9, (float).9));
	    			g.setColor(c);
	    			g.drawLine(i, 743-values2[i], i+10, 743- values2[i+10]);
	    			c = new Color(Color.HSBtoRGB((1-(float)i)/values2.length/2+(float)0.6, (float)0.8, (float).9));
	    			g.setColor(c);
	    			g.drawLine(i, 743-values2[i]+1, i+10, 743-values2[i+10]+1);
	    			c = new Color(Color.HSBtoRGB((1-(float)i)/values2.length/2+(float)0.6, (float)0.7, (float).9));
	    			g.setColor(c);
	    			g.drawLine(i, 743-values2[i]+2, i+10, 743-values2[i+10]+2);

	    		}
	    }

}
