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

public class ClientJavaFX extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        fenetrePrincipale(primaryStage);
    }

    void fenetrePrincipale(Stage primaryStage) {

        try{

            BorderPane pane = new BorderPane();
            pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

            VBox gauche = new VBox (getVBox1(), getVBox2());
            pane.setLeft(gauche);
            pane.setRight(getVBox3());

            Scene scene = new Scene(pane);


            primaryStage.setTitle("Inscription UdeM");
            primaryStage.setScene(scene);
            primaryStage.show();


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private VBox getVBox1() {

        VBox vBox1 = new VBox(15);

        vBox1.setPrefSize(350,425);
        vBox1.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox1.setMargin(vBox1, new Insets(3,3,1,3));

        vBox1.setPadding(new Insets(15,20,20,20));

        //label
        Label titre1 = new Label ("Liste des cours");
        titre1.setMaxWidth(Double.MAX_VALUE);
        titre1.setAlignment(Pos.CENTER);
        titre1.setFont(Font.font("Amble CN",18));
        vBox1.getChildren().add(titre1);

        return vBox1;

    }
    private VBox getVBox2() {

        VBox vBox2 = new VBox(15);

        vBox2.setPrefSize(350,75);
        vBox2.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox2.setMargin(vBox2, new Insets(1,3,3,3));

        vBox2.setPadding(new Insets(15,5,5,5));

        return vBox2;

    }

    private VBox getVBox3() {

        VBox vBox3 = new VBox(15);

        vBox3.setPrefSize(300,500);
        vBox3.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox3.setMargin(vBox3, new Insets(1,3,3,3));

        vBox3.setPadding(new Insets(15,5,5,5));


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
