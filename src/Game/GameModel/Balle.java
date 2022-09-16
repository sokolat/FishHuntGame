package Game.GameModel;
public class Balle {
    private double rayon, x, y;

    /**
     * Construteur de Balle, crée une balle
     * @param x Le centre de la balle en x
     * @param y Le centre de la balle en y
     */
    public Balle(double x, double y) {
        this.rayon=50;
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x-rayon;
    }

    public double getY() {
        return y-rayon;
    }
    public double getRayon() {
        return rayon;
    }

    /**
     * Cette méthode met à jour le rayon de la balle
     * @param dt Le temps depuis le dernier appel de cette méthode
     */
    public void updateRayon(double dt) {
        this.rayon-=300*dt*1e-9;
        if(rayon<0){
            rayon=0;
        }
        
    }
    
}
