import processing.core.PApplet;

/**
 * Main class to execute sketch
 * @author Preston Wong and Jacky Ho
 *
 */
class Main {
	  	public static void main(String[] args) {

    	String[] processingArgs = {"CastleRush"};
		CastleRush mySketch = new CastleRush();
	    PApplet.runSketch(processingArgs, mySketch);
		PApplet.main("CastleRush");
	}
}
  
