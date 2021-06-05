package com.TETOSOFT.tilegame;

import com.TETOSOFT.tilegame.GameEngine;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameMenu extends Application {

    private GameMenuClass gameMenuClass;

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("ENTERED TO START");

        Pane root = new Pane();
        root.setPrefSize(800,600);

        //InputStream is = Files.newInputStream(Paths.get("images/menu_background.jpg"));
        InputStream is = Files.newInputStream(Paths.get("images/background.jpg"));
        Image menuBg = new Image(is);
        is.close();

        ImageView menuBgImgView = new ImageView(menuBg);
        menuBgImgView.setFitWidth(800);
        menuBgImgView.setFitHeight(600);

        gameMenuClass = new GameMenuClass();

        root.getChildren().addAll(menuBgImgView, gameMenuClass);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private class GameMenuClass extends Parent {

        public GameMenuClass(){

            VBox mainMenu = new VBox(10);
            VBox optionsMenu = new VBox(10);

            mainMenu.setTranslateX(100);
            mainMenu.setTranslateY(200);
            optionsMenu.setTranslateX(100);
            optionsMenu.setTranslateY(250);

            final int offset = 400;

            optionsMenu.setTranslateX(offset);

            MenuButton btnPlay = new MenuButton("PLAY");
            btnPlay.setOnMouseClicked(event -> {

                new GameEngine().run();
            });

            MenuButton btnOptions = new MenuButton("OPTIONS");
            btnOptions.setOnMouseClicked(event -> {

                getChildren().add(optionsMenu);

                //Translate Main Menu
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), mainMenu);
                tt.setToX(mainMenu.getTranslateX() - offset);

                //Bring the Options Menu to the main menu place
                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), optionsMenu);
                tt1.setToX(mainMenu.getTranslateX());

                tt.play();
                tt1.play();

                tt.setOnFinished(evt -> {
                    getChildren().remove(mainMenu);
                });
            });

            MenuButton btnExit = new MenuButton("EXIT");
            btnExit.setOnMouseClicked(event -> {

                System.exit(0);
            });

            mainMenu.getChildren().addAll(btnPlay, btnOptions, btnExit);

            MenuButton btnBack = new MenuButton("BACK");
            btnBack.setOnMouseClicked(event -> {

                getChildren().add(mainMenu);

                //Move Options Menu back to its place
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), optionsMenu);
                tt.setToX(optionsMenu.getTranslateX() + offset);

                //Move Main Menu back to its place
                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), mainMenu);
                tt1.setToX(optionsMenu.getTranslateX());

                tt.play();
                tt1.play();

                tt.setOnFinished(evt -> {
                    getChildren().remove(optionsMenu);
                });

            });

            MenuButton btnSound = new MenuButton("SOUND");
            btnSound.setOnMouseClicked(event -> {

                //Change Volume

            });


            optionsMenu.getChildren().addAll(btnBack, btnSound);

            Rectangle bg = new Rectangle(800,600);
            bg.setFill(Color.GRAY);
            bg.setOpacity(0.4);

            getChildren().addAll(bg, mainMenu);

        }
    }

    public static class MenuButton extends StackPane {

        private Text text;

        public MenuButton(String name){

            text = new Text(name);
            text.setFont(text.getFont().font(20));
            text.setFill(Color.WHITE);

            Rectangle bg = new Rectangle(250,30);
            bg.setOpacity(0.6);
            bg.setFill(Color.BLACK);
            //bg.setFill(Color.WHITE);
            GaussianBlur blur = new GaussianBlur(3.5);
            bg.setEffect(blur);

            setAlignment(Pos.CENTER_LEFT);
            setRotate(-0.5);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setTranslateX(10);
                text.setTranslateX(10);
                bg.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });

            setOnMouseExited(event -> {
                bg.setTranslateX(0);
                text.setTranslateX(0);
                bg.setFill(Color.BLACK);
                text.setFill(Color.WHITE);
            });

            DropShadow drop = new DropShadow(50, Color.WHITE);
            drop.setInput(new Glow());

            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> setEffect(null));
        }
    }
}
