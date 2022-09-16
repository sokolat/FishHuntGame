package Game.GameModel;
import java.util.Random;

import Game.GameVue;
import Game.ImageHelpers;
import javafx.scene.image.Image;


public abstract class Fish {
    public final static int WIDTH = 130, HEIGHT = 130;
    public final static double NORMALTIMELAPSE = 3 * 1e9, SPECIALTIMELAPSE = 5 * 1e9;
    public double accy, vx, vy, x, y;
    protected Random rand = new Random();
    protected Image fishImage;


    /**
     * Le constructeur de Fish, crée un poisson
     * @param level Le niveau du jeu au moment de l'appel du constructeur
     */
    public Fish(int level) {
        accy = 0;
        vy = -rand.nextDouble(100, 201);
        vx = 100 * Math.pow(level,1/3.0) + 200;
        x = -WIDTH;
        y = rand.nextDouble(GameVue.HEIGHT / 5, 4 * GameVue.HEIGHT / 5);
    }

    /**
     * Décide si le poisson commence à gauche ou à droite
     * @return true si le poisson commence à droite, false sinon
     */
    public boolean isInversed() {
        return rand.nextBoolean();
    }

    /**
     * Cette méthode renverse horizontalement l'image du Fish
     */
    public void flopImage() {
        this.fishImage = ImageHelpers.flop(this.fishImage);
    }

    /**
     * Cette méthode met à jour la vitesse en x du poisson
     * @param dt Le temps depuis le dernier appel de la méthode
     */
    public void updateVx(double dt) {
    }

    public double getVx() {
        return vx;
    }

    public Image getImage() {
        return this.fishImage;
    }

    /**
     * Cette méthode met à jour la vitesse en y du poisson
     * @param dt Le temps depuis le dernier appel de la méthode
     */
    public void updateVy(double dt) {
        this.vy += this.accy * dt * 1e-9;
    }

    /**
     * Cette méthode met à jour la position en y du poisson
     * @param dt Le temps depuis le dernier appel de la méthode
     */
    public void updateY(double dt) {
        this.y += this.vy * dt * 1e-9;
    }

    /**
     * Cette méthode met à jour la position en x du poisson
     * @param dt Le temps depuis le dernier appel de la méthode
     */
    public void updateX(double dt) {
        this.x += this.vx * dt * 1e-9;
    };

    /**
     * Cette méthode permet de tester la collision entre une balle et le poisson
     * @param x La coordonnée en x de la balle
     * @param y La coordonnée en y de la balle
     * @return true si la balle touche le poisson, false sinon
     */
    public boolean isCatched(double x, double y) {
        return x >= this.x && x <= this.x + WIDTH && y >= this.y && y <= this.y + HEIGHT;
    }

    /**
     * Cette méthode indique si le poisson atteint un mur
     * @return true si le poisson est arrivé au bout de l'écran false sinon
     */
    public boolean reachWall() {
        return this.x > GameVue.WIDTH || this.x < -WIDTH;
    }
}
