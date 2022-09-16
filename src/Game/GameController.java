package Game;
import java.util.ArrayList;
import java.util.Random;

import Game.GameModel.Balle;
import Game.GameModel.Bubble;
import Game.GameModel.Crabe;
import Game.GameModel.*;

public class GameController {
    private ArrayList<Fish> fishes;
    private ArrayList<Balle> balles;
    private ArrayList<Bubble> bubbles;
    private int level = 1, score = 0, lives = 3, nextLevelScore = 5, currentLevel = 0;
    private final static double NORMALTIMELAPSE = 3 * 1e9, SPECIALTIMELAPSE = 5 * 1e9;
    private boolean inAnnounce, gameOver, finish;
    private String announce;
    private Random random= new Random();
    private long lastTime, announceStart, prevNormalFishTime, prevSpecFishTime, prevBubbleTime;


    /**
     * Le constructeur de gameModel
     */
    public GameController() {
        fishes = new ArrayList<>();
        balles = new ArrayList<>();
        bubbles = new ArrayList<>();
        announce="";
        lastTime=0;
        announceStart=0;
        prevNormalFishTime=0;
        prevSpecFishTime=0;
        prevBubbleTime=0;
        inAnnounce=false;
        gameOver=false;
        finish=false;
    }

    
    
    public String getAnnounce() {
        return announce;
    }

    public boolean isFinished() {
        return finish;
    }

    /**
     * Cette méthode s'occupe de mettre à jour tous les éléments de gameModel et de
     * s'assurer d'ajouter des poissons et des bulles lorsqu'il le faut
     * 
     * @param now Le moment où on apelle cette méthode
     */
    public void updateGameModel( long now) {
        if (lastTime == 0) {
            prevNormalFishTime = now;
            prevSpecFishTime = now;
            prevBubbleTime = now;
            lastTime = now;
            return;
        }
        long dt=now-lastTime;
        if(score>=nextLevelScore){
            nextLevelScore+=5;
            level++;
        }

        if(currentLevel!=level && lives!=0){
            currentLevel=level;
            announceStart = now;
            announce="Level "+level;
            inAnnounce=true;
        }

        if (lives == 0 && !gameOver) {
            announceStart = now;
            inAnnounce=true;
            gameOver=true;
            announce = "Game Over";
        }

        if (now - announceStart >= NORMALTIMELAPSE && gameOver) {
            finish=true;
        }

        if (now - announceStart >= NORMALTIMELAPSE && inAnnounce) {
            announce="";
            inAnnounce=false;
        }

        if(inAnnounce){
            prevNormalFishTime=now;
            prevSpecFishTime=now;

            int prevScore=score;
            @SuppressWarnings("unchecked")
            ArrayList<Fish> prevFishes= (ArrayList<Fish>) fishes.clone();
            updateBalles(dt);
            score=prevScore;
            fishes=prevFishes;

            int prevLives=lives;
            updateFishes(dt);
            lives=prevLives;

            updateBubbles(dt);
        }
        else{
            updateBalles(dt);
            updateFishes(dt);
            updateBubbles(dt);
        }
        if (now - prevNormalFishTime >= NORMALTIMELAPSE) {
            spawnNormalFish(now);
        }
        if(now - prevSpecFishTime >= SPECIALTIMELAPSE && level>=2){
            spawnSpecialFish(now);
        }
        if (now - prevBubbleTime >= NORMALTIMELAPSE) {
            addBubbles(now);
        }
        
        lastTime=now;
    }

    /**
     * Elle update la vitesse et les coordonnées de tous les poissons de l'ArrayList
     * fishes. Si un poisson sort de l'écran il est enlevé.
     * @param dt Le temps depuis le dernier appel de cette méthode
     */
    private void updateFishes(double dt) {
        ArrayList<Fish> deadFish = new ArrayList<>();
        for (Fish fish : fishes) {
            if (!fish.reachWall()) {
                fish.updateVx(dt);
                fish.updateVy(dt);
                fish.updateX(dt);
                fish.updateY(dt);
            } else {
                deadFish.add(fish);
                lives--;
            }
        }
        fishes.removeAll(deadFish);
    }
    /**
     * Cette méthode met à jour toutes les coordonnées de toutes les balles dans
     * l'ArrayList balles. Elle vérifie aussi si un poisson est touché par une
     * balle pour l'enlever. Si une balle disparait de l'écran, elle est enlevé
     * @param dt Le temps depuis le dernier appel de cette méthode
     */
    private void updateBalles(double dt) {
        ArrayList<Balle> removedBalles = new ArrayList<>();
        for (Balle balle : balles) {
            balle.updateRayon(dt);
            if (balle.getRayon() == 0) {
                ArrayList<Fish> deadFish = new ArrayList<>();
                for (Fish fish : fishes) {
                    if (fish.isCatched(balle.getX(), balle.getY())) {
                        score++;
                        deadFish.add(fish);
                    }
                }
                fishes.removeAll(deadFish);
                removedBalles.add(balle);
            }
        }
        balles.removeAll(removedBalles);
    }

    /**
     * Cette méthode met à jour les coordonnées de toutes les bulles dans l'ArrayList
     * bubbles et enlève les bulles si elles sortent de l'écran
     * @param dt Le temps depuis le dernier appel de cette méthode
     */
    private void updateBubbles(double dt) {
        ArrayList<Bubble> removedBubbles = new ArrayList<>();
        for(Bubble bubble : bubbles){
            bubble.updateY(dt);
            if(bubble.getY() < - 2*bubble.getRayon()){
                removedBubbles.add(bubble);
            }
        }
        bubbles.removeAll(removedBubbles);
    }

    public ArrayList<Balle> getBalles() {
        return balles;
    }

    /**
     * Ajoute une balle dans l'ArrayList balles
     * @param x Le centre en x de la nouvelle balle
     * @param y Le centre en y de la nouvelle balle
     */
    public void addBalle( double x, double y){
        balles.add(new Balle(x, y));
    }


    public ArrayList<Fish> getFishes() {
        return fishes;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getLevel() {
        return level;
    }

    /**
     * Augmente le level de 1
     */
    public void addLevel() {
        level++;
    }

    public int getLives() {
        return lives;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getNextLevelScore() {
        return nextLevelScore;
    }

    public void setNextLevelScore(int nextLevelScore) {
        this.nextLevelScore = nextLevelScore;
    }

    public int getScore() {
        return score;
    }

    /**
     * Augmente le score du gameModel de 1
     */
    public void addScore() {
        score++;
    }

    public boolean getInAnnounce() {
        return inAnnounce;
    }

    public void setInAnnounce(boolean inAnnounce) {
        this.inAnnounce = inAnnounce;
    }


    /**
     * La méthode est private puisqu'on ne veut pas pouvoir l'appeler toute seul,
     * on veut l'appeler dans updateGameModel.
     * Fait apparaître un normalFish dans l'ArrayList fishes.
     * 
     * @param now Le moment où on apelle cette fonction
     */
    private void spawnNormalFish(long now) {
        Fish fish = new NormalFish(level);
        if (fish.isInversed()) {
            fish.x = GameVue.WIDTH;
            fish.vx = -fish.vx;
            fish.flopImage();
        }
        fishes.add(fish);
        prevNormalFishTime=now;
    }

    /**
     * La méthode est private puisqu'on ne veut pas pouvoir l'appeler toute seul,
     * on veut l'appeler dans updateGameModel.
     * Fait apparaître un specialFish dans l'ArrayList fishes.
     * 
     * @param now Le moment où on apelle cette fonction
     */
    private void spawnSpecialFish(long now){
        Fish fish;
        if( random.nextBoolean()){
            fish= new StarFish(level, now);
        }
        else{
            fish= new Crabe(level);
        }
        if(fish.isInversed()){
            fish.x = GameVue.WIDTH;
            fish.vx = -fish.vx;
            fish.flopImage();
        }
        fishes.add(fish);
        prevSpecFishTime = now;
    }

    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    /**
     * La méthode est privé puisqu'on ne veut pas pouvoir l'appeler toute seul,
     * on veut l'appeler dans updateGameModel.
     * Ajout de bulles dans le ArrayList bubbles
     * 
     * @param now Le moment où on apelle cette fonction
     */
    private void addBubbles(long now){
        for(int i=0; i<3; i++){
            double baseX = random.nextDouble(GameVue.WIDTH);
            for(int j=0; j<5; j++){
                bubbles.add(new Bubble(random.nextDouble(baseX-20, baseX+20)));
            }
        }
        prevBubbleTime = now;
    }
}
