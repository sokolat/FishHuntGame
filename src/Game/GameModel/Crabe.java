package Game.GameModel;
import javafx.scene.image.Image;
import Game.Images.*;

public final class Crabe extends Fish {
    private long timeLastOscillation;
    private boolean isAdvancing;

    /**
     * Le constructeur de Crab, crée un nouveau crabe
     * @param level
     */
    public Crabe(int level){ 
        super(level);
        vx = 1.3*(100*Math.pow(level, 1/3.0) + 200);
        fishImage = new Image("/crabe.png");
        vy=0;
        timeLastOscillation=0;
        isAdvancing=true;
    }

    @Override
    public void updateVx(double dt) {
        timeLastOscillation+=dt;
        if(isAdvancing){
            if(timeLastOscillation>=0.5*1e9){
                vx=-vx;
                timeLastOscillation=0;
                isAdvancing=false;
            }
        }
        else{
            if(timeLastOscillation>=0.25*1e9){
                vx=-vx;
                timeLastOscillation=0;
                isAdvancing=true;
            }
        }
    }
}