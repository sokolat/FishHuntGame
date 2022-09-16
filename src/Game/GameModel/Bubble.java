package Game.GameModel;
import java.util.Random;
import Game.Images.*;

import Game.GameVue;
import javafx.scene.paint.Color;

public class Bubble {
    private double rayon, x, y, vy;
    public static Color COLOR=Color.rgb(0, 0, 255, 0.4);;


    /**
     * Le constructeur de Bubble, crée une nouvelle bulle
     * @param x La coordonnée en x de la bulle
    */
    public Bubble(double x){
        rayon= new Random().nextDouble(10,40);
        y= GameVue.HEIGHT;
        vy= - new Random().nextDouble(350, 450);
        this.x= x; 
    }

    public double getX() {
        return x;
    }

    public double getRayon() {
        return rayon;
    }

    public double getY() {
        return y;
    }

    /**
     * Met à jour la position en y de la bulle
     * @param dt Temps depuis le dernier appel de cette méthode
     */
    public void updateY(double dt){
        y+=vy*dt*1e-9;
    }

}
