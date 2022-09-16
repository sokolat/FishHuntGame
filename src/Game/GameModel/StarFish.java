package Game.GameModel;
import javafx.scene.image.Image;

public final class StarFish extends Fish {
    private long timeSinceSpawn;
    private double yInitial;

    /**
     * Le constructeur de Starfish, crée une étoile de mer
     * @param level Le niveau du jeu au moment de l'appel de la méthode
     * @param now Le moment où on apelle la méthode
     */
    public StarFish(int level, long now){
        super(level);
        fishImage = new Image("/star.png");
        yInitial=y;
        accy=0;
        timeSinceSpawn=now;
    }

    
    @Override
    public void updateY(double dt) {
        timeSinceSpawn+=dt;
        y=50*Math.sin(timeSinceSpawn*1e-9*2*Math.PI)+yInitial;
        
    }
}
