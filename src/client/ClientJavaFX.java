package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
//test

public class ClientJavaFX extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        fenetrePrincipale(primaryStage);
    }

    void fenetrePrincipale(Stage primaryStage) {

        try{

            BorderPane pane = new BorderPane();
            pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            //Scene scene = new Scene(pane,600,380);

            VBox gauche = new VBox (getVBox1(), getVBox2());
            pane.setLeft(gauche);
            pane.setRight(getVBox3());

            Scene scene = new Scene(pane,600,380);


            primaryStage.setTitle("Inscription UdeM");
            primaryStage.setScene(scene);
            primaryStage.show();


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private VBox getVBox1() {


        VBox vBox1 = new VBox();

        vBox1.setPrefSize(300,350);
        vBox1.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox1.setMargin(vBox1, new Insets(3,3,1,3));

        vBox1.setPadding(new Insets(8,8,8,8));

        //label
        Label titre1 = new Label ("Liste des cours");
        titre1.setMaxWidth(Double.MAX_VALUE);
        titre1.setAlignment(Pos.TOP_CENTER);
        titre1.setFont(Font.font("Amble CN",18));
        vBox1.getChildren().add(titre1);

        return vBox1;

    }
    private VBox getVBox2() {

        // Box Style

        VBox vBox2 = new VBox();

        vBox2.setPrefSize(300,40);
        vBox2.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox2.setMargin(vBox2, new Insets(1,3,3,3));

        vBox2.setPadding(new Insets(10,10,10,10));

        HBox buttonGroup = new HBox(50);

        // Menu Déroulant

        MenuButton menuButton = new MenuButton();
        menuButton.setPrefWidth(100);
        //Font fontMenuButton = new Font(16);
        //menuButton.setFont(fontMenuButton);

        MenuItem menuChoix1 = new MenuItem("Hiver");
        MenuItem menuChoix2 = new MenuItem("Été");
        MenuItem menuChoix3 = new MenuItem("Automne");

        menuButton.getItems().addAll(menuChoix1, menuChoix2, menuChoix3);
        menuButton.setText(menuChoix1.getText());

        menuChoix1.setOnAction(actionEvent -> {
            menuButton.setText(menuChoix1.getText());
        });

        menuChoix2.setOnAction(actionEvent -> {
            menuButton.setText(menuChoix2.getText());
        });

        menuChoix3.setOnAction(actionEvent -> {
            menuButton.setText(menuChoix3.getText());
        });

        buttonGroup.getChildren().add(menuButton);

        //vBox2.getChildren().add(menu);


        // Boutton
        HBox button = new HBox();
        Button charger = new Button("charger");
        button.getChildren().add(charger);
        buttonGroup.getChildren().add(button);
        //button.setAlignment(Pos.CENTER_RIGHT);
        //vBox2.getChildren().add(button);

        //Positionnement
        buttonGroup.setMaxWidth(Double.MAX_VALUE);
        buttonGroup.setMaxHeight(Double.MAX_VALUE);
        buttonGroup.setAlignment(Pos.CENTER);
        vBox2.getChildren().add(buttonGroup);

        return vBox2;

    }

    private VBox getVBox3() {

        // box style

        VBox vBox3 = new VBox();
        //vBox3.setPrefWidth(Double.MAX_VALUE/2);
        //vBox3.setPrefHeight(Double.MAX_VALUE);


        vBox3.setPrefSize(300,380);
        vBox3.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox3.setMargin(vBox3, new Insets(1,3,3,3));

        vBox3.setPadding(new Insets(8,8,8,8));


        //label
        Label titre2 = new Label ("Formulaire d'inscription");
        titre2.setMaxWidth(Double.MAX_VALUE);
        titre2.setAlignment(Pos.CENTER);
        titre2.setFont(Font.font("Amble CN",18));
        vBox3.getChildren().add(titre2);


        return vBox3;

    }


    public static void main(String[] args) {
        launch(args);
    }
}
