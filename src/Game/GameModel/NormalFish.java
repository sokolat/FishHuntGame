package Game.GameModel;
import Game.ImageHelpers;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import Game.Images.*;

public final class NormalFish extends Fish {
    
    
    /**
     * Le constructeur de normalFish, crée un poisson normal
     * @param level Le niveau du jeu lors de l'appel de la fonction
     */
    public NormalFish(int level){
        super(level);
        accy = 100;
        fishImage = setImage(new Image("/0" + rand.nextInt(0, 8) + ".png"));
    }


    /**
     * Cette méthode colorie l'image d'un poisson normal avec une couleur aléatoire
     * @param fishImage L'image à colorié
     * @return L'image colorié
     */
    private Image setImage(Image fishImage) {
        return ImageHelpers.colorize(fishImage,
                Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
    }


}
