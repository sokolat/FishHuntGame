package Game;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import Game.GameModel.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameVue extends Application {
	public final static double WIDTH = 640, HEIGHT = 480;
	private final Button menu = setButton("Menu", 285, 430, 50);
	private final Button bestScores = setButton("Meilleurs Scores", 275, 355, 100);
	private final Button newPart = setButton("Nouvelle Partie!", 275, 320, 100);
	private GameController gameModel;
	private ArrayList<String> scores = new ArrayList<>();
	private ListView<String> list;
	private Stage stage;

	public static void main(String[] args) {
		GameVue.launch(args);
	}

	/**
	 * Cette méthode crée un nouveau bouton avec les attributs donnés en paramètre 
	 * @param texte le texte dans le bouton
	 * @param x la coordonnée en x du bouton
	 * @param y la coordonnée en y du bouton
	 * @param width la largueur du bouton
	 * @return Le bouton créé
	 */
	private static Button setButton(String texte, double x, double y, double width) {
		Button button = new Button(texte);
		button.setLayoutX(x);
		button.setLayoutY(y);
		button.setMinWidth(width);
		return button;
	}

	/**
	 * Cette méthode crée une nouvelle instance de ListView et la place à l'endroit 
	 * de la listView dans la scène des meilleurs scores
	 * @return la listView créée
	 */
	private static ListView<String> setList() {
		ListView<String> listView = new ListView<>();
		listView.setLayoutX(40);
		listView.setLayoutY(75);
		listView.setPrefHeight(300);
		listView.setPrefWidth(560);
		return listView;
	}

	/**
	 * Cette méthode crée un ImageView avec les attributs donnés en paramètre
	 * @param imgName Le nom de l'image dans le dossier
	 * @param width La largeur de l'ImageView
	 * @param height La hauteur de l'ImageView
	 * @param x La coordonnée en x de l'ImageView
	 * @param y La coordonnée en y de l'ImageView
	 * @return L'ImageView créé
	 */
	private static ImageView setImage(String imgName, double width, double height, double x, double y) {
		ImageView imageView = new ImageView(new Image("/" + imgName));
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		imageView.setX(x);
		imageView.setY(y);
		return imageView;
	}

	/**
	 * Cette méthode crée un Text avec les attributs donnés en paramètre
	 * @param texte La chaine de caractère dans le Text
	 * @param color La couleur du texte
	 * @param size La taille de la police
	 * @param x La coordonnée en x du Text
	 * @param y La coordonnée en y du Text
	 * @return Le Text créé 
	 */
	private static Text setText(String texte, Paint color, int size, double x, double y) {
		Text scoreText = new Text(texte);
		scoreText.setFill(color);
		scoreText.setFont( Font.font(size) );
		scoreText.setX(x);
		scoreText.setY(y);
		return scoreText;
	}

	/**
	 * Crée la scène du jeu
	 * 
	 * @return La scène du jeu
	 */
	private Scene game() {
		gameModel = new GameController();

		Pane gameRoot = new Pane();
		Pane stat = new Pane();
		stat.getChildren().add(setText("" + gameModel.getScore(), Color.web("#FFFFFF"),30, 300, 50));

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		GraphicsContext context = canvas.getGraphicsContext2D();

		ImageView targetView = setImage("cible.png", 50, 50, 120, 60);
		gameRoot.setOnMouseMoved((event) -> {
			setTargetCursor(targetView, event.getX(), event.getY());
		});

		gameRoot.setOnMouseDragged((event) -> {
			setTargetCursor(targetView, event.getX(), event.getY());
		});

		gameRoot.setOnMouseClicked((event) -> {
			gameModel.addBalle(event.getX(), event.getY());
		});

		gameRoot.setBackground(new Background( new BackgroundFill(Color.web("#00008b"), CornerRadii.EMPTY, Insets.EMPTY)));
		gameRoot.getChildren().addAll(canvas, targetView, stat);

		AnimationTimer timer = new AnimationTimer() {
			private Text announce;

			@Override
			public void handle(long now) {

				gameModel.updateGameModel(now);
				draw(context, gameModel);

				
				Text scoreText = setText("" + gameModel.getScore(), Color.web("#FFFFFF"), 30, 300, 50);

				ArrayList<ImageView> lifeImages = new ArrayList<>();
				for (int i = 0; i < gameModel.getLives(); i++) {
					lifeImages.add(setImage("00.png", 30, 30, 250 + i * 40, 80));
				}

				if (gameModel.getAnnounce() == "Game Over") {
					announce = setText(gameModel.getAnnounce(), Color.web("#FF0000"), 100, 80, 280);
				} 
				else {
					announce = setText(gameModel.getAnnounce(), Color.WHITE, 100, 150, 280);
				}

				stat.getChildren().clear();
				stat.getChildren().addAll(lifeImages);
				stat.getChildren().addAll(scoreText, announce);

				if (gameModel.isFinished()) {
					stage.setScene(bestScores(gameModel.getScore()));
					stop();
				}
			}
		};
		timer.start();
		return new Scene(gameRoot, WIDTH, HEIGHT);
	}

	/**
	 * Cette méthode fait la scène de l'écran d'accueil
	 * @return Retourne la scène créée
	 */
	private Scene homePage() {
		Pane homePageRoot = new Pane();
		homePageRoot.setBackground(new Background(
				new BackgroundFill(Color.web("#00008b"), CornerRadii.EMPTY, Insets.EMPTY)));
		ImageView logoView = setImage("logo.png", 400, 240, 120, 60);
		homePageRoot.getChildren().addAll(logoView, newPart, bestScores);
		return new Scene(homePageRoot, WIDTH, HEIGHT);
	}

	/**
	 * Cette méthode fait la scène pour voir les meilleurs scores
	 * @return Retourne la scène créée
	 */
	private Scene bestScores() {
		Text title = new Text(240, 50, "Meilleurs Scores");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFont(Font.font(25));
		scores.clear();
		try {
			FileReader fr = new FileReader("score.txt");
			BufferedReader reader = new BufferedReader(fr);
			String line;
			while ((line = reader.readLine()) != null) {
				scores.add(line);
			}
			fr.close();
			reader.close();
		} catch (Exception e) {
		}

		list = setList();
		list.getItems().clear();
		list.getItems().addAll(scores);
		Pane bestScoresRoot = new Pane(); 
		bestScoresRoot.getChildren().addAll(title, list, menu);
		return new Scene(bestScoresRoot, WIDTH, HEIGHT);
	}


	/**
	 * Cette méthode fait la scène de meilleurs scores, mais avec la possibilité
	 * d'ajouter son propre score. 
	 * @param score Le score de la partie avant d'appeler cette méthode
	 * @return La scène de meilleurs scores
	 */
	private Scene bestScores(int score) {
		Scene bestScoreScene = bestScores();
		Pane bestScoreRoot = (Pane) bestScoreScene.getRoot();
		HBox hBox = new HBox();
		Text name = new Text("Votre nom :");
		TextField input = new TextField();
		Text scoreText = new Text("a fait " + score + " points!");

		Button add = new Button("Ajouter!");
		add.setOnAction((event) -> {
			addScore(input.getText(), score);
			stage.setScene(homePage());
		});
		hBox.getChildren().addAll(name, input, scoreText, add);
		hBox.setLayoutX(150);
		hBox.setLayoutY(390);
		hBox.setSpacing(5);
		hBox.setAlignment(Pos.CENTER);
		bestScoreRoot.getChildren().add(hBox);

		return bestScoreScene;
	}

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		stage.setResizable(false);
		stage.setTitle("Fish Hunt");
		stage.setScene(homePage());
		bestScores.setOnAction((event) -> {
			stage.setScene(bestScores());
		});
		menu.setOnAction((event) -> {
			stage.setScene(homePage());
		});
		newPart.setOnAction((event) -> {
			Scene gamescene = game();
			gamescene.setOnKeyPressed((value) -> {
				if (value.getCode() == KeyCode.H) {
					gameModel.addLevel();
				} else if (value.getCode() == KeyCode.J && gameModel.getLives() != 0) {
					gameModel.addScore();
				} else if (value.getCode() == KeyCode.K && gameModel.getLives() < 3 && gameModel.getLives() != 0) {
					gameModel.setLives(gameModel.getLives() + 1);
				} else if (value.getCode() == KeyCode.L) {
					gameModel.setLives(0);
				}
			});
			stage.setScene(gamescene);
		});
		stage.show();
	}
	
	/**
	 * Place le targetView aux coordonnées données
	 * @param targetView L'image à placer
	 * @param xCursor La coordonnée en x
	 * @param yCursor La coordonnée en y
	 */
	private void setTargetCursor(ImageView targetView ,double xCursor, double yCursor) {
		double width = targetView.getBoundsInLocal().getWidth();
		double height = targetView.getBoundsInLocal().getHeight();
		targetView.setX(xCursor - width / 2);
		targetView.setY(yCursor - height / 2);
		
	}

	/**
	 * Dessine sur le contexte graphique donné en paramètre le contenu du GameModel
	 * @param context La où on veut dessiner les éléments
	 * @param gameModel Le gameModel d'où les éléments qu'on veut dessiner viennent
	 */
	private void draw(GraphicsContext context, GameController gameModel) {
		context.clearRect(0, 0, WIDTH, HEIGHT);
		context.setFill(Bubble.COLOR);
		for (Bubble bubble : gameModel.getBubbles()) {
			context.fillOval(bubble.getX(), bubble.getY(), 2 * bubble.getRayon(), 2 * bubble.getRayon());
		}
		for (Fish fish : gameModel.getFishes()) {
			context.drawImage(fish.getImage(), fish.x, fish.y, Fish.WIDTH, Fish.HEIGHT);
		}
		context.setFill(Color.BLACK);
		for (Balle balle : gameModel.getBalles()) {
			context.fillOval(balle.getX(), balle.getY(), 2 * balle.getRayon(), 2 * balle.getRayon());
		}

	}

	/**
	 * Ajoute le nom et le score dans le fichier score.txt
	 * @param name Nom du joueur
	 * @param score Score du joueur
	 */
	private void addScore(String name, int score){
		String yourScore = "#0-" + name + "-" + score;

			scores.add(yourScore);
			ArrayList<String> scoresWithoutPlace = new ArrayList<>();
			for (String scoreString : scores) {
				String[] strings = scoreString.split("-");
				scoreString = "";
				for (int i = strings.length - 1; i > 0; i--) {
					scoreString += strings[strings.length - i];
					if (i != 1) {
						scoreString += "-"; 
					}
				}
				scoresWithoutPlace.add(scoreString);
			}
			scores = scoresWithoutPlace;
			scores.sort(new Comparator<String>() {
				@Override
				public int compare(String score1, String score2) {
					String[] score1TextParts = score1.split("-");
					String[] score2TextParts = score2.split("-");
					int valeur1 = Integer.parseInt(score1TextParts[score1TextParts.length - 1]);
					int valeur2 = Integer.parseInt(score2TextParts[score2TextParts.length - 1]);
					if (valeur1 < valeur2) {
						return 1;
					} else if (valeur1 > valeur2) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			ArrayList<String> removedScores = new ArrayList<>();
			for (int i = 0; i < scores.size(); i++) {
				String scoreString = "#" + (i + 1) + "-" + scores.get(i);
				scores.set(i, scoreString);
				if (i >= 10) {
					removedScores.add(scores.get(i));
				}
			}
			scores.removeAll(removedScores);

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter("score.txt"));
				for (String scoreString : scores) {
					writer.append(scoreString + "\n");
				}
				writer.close();
			} catch (IOException e) {
			}
	}
}
