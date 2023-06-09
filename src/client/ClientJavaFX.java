package client;



import javafx.application.Application;
import javafx.geometry.HPos;
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
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.ArrayList;
//test

public class ClientJavaFX extends Application {

    private Controleur controleur = new Controleur();


    /**
     *
     * Création de la fenetre principale
     *
     * @param primaryStage Fenetre principale
     * @throws Exception Exceptions qui peuvent etre rencontrées
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        fenetrePrincipale(primaryStage);
    }


    /**
     *
     * Assemblage des élements dans l'interface graphique de la fenetre
     *
     * @param primaryStage Fenetre principale
     */
    void fenetrePrincipale(Stage primaryStage) {

        try{
            Stage stage = new Stage();
            stage.setMaxWidth(600);
            stage.setMaxHeight(380);

            BorderPane pane = new BorderPane();
            pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            Scene scene = new Scene(pane,600,380);


            //// vBox1 : Section d'affichage des cours ////

            VBox vBox1 = new VBox();
            vBox1.setPrefSize(scene.getWidth()/2, scene.getHeight()*0.9);
            vBox1.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            vBox1.setMargin(vBox1, new Insets(3,3,1,3));
            vBox1.setPadding(new Insets(8,8,8,8));

            // Label
            Label titre1 = new Label ("Liste des cours");
            titre1.setMaxWidth(Double.MAX_VALUE);
            titre1.setAlignment(Pos.TOP_CENTER);
            titre1.setFont(Font.font("Amble CN",18));
            vBox1.getChildren().add(titre1);

            // Table View

            VBox table = new VBox();
            TableView<Course> tableView = new TableView<>();

            TableColumn<Course,String> code = new TableColumn("Code");
            code.setCellValueFactory(new PropertyValueFactory<>("code"));
            code.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));

            TableColumn<Course,String> cours = new TableColumn("Cours");
            cours.setCellValueFactory(new PropertyValueFactory<>("name"));
            cours.prefWidthProperty().bind(tableView.widthProperty().multiply(0.6));

            tableView.getColumns().addAll(code,cours);
            table.getChildren().add(tableView);
            vBox1.getChildren().add(table);



            //// vBox2 : Section du choix de session ////


            VBox vBox2 = new VBox();
            vBox2.setPrefSize(scene.getWidth()/2, scene.getHeight()*0.1);
            vBox2.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            vBox2.setMargin(vBox2, new Insets(1,3,3,3));
            vBox2.setPadding(new Insets(10,10,10,10));

            HBox buttonGroup = new HBox(50);

            // Menu Déroulant

            MenuButton menuButton = new MenuButton();
            menuButton.setPrefWidth(100);

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


            // Boutton charger

            HBox boutton1 = new HBox();
            Button bouttonCharger = new Button("charger");
            boutton1.getChildren().add(bouttonCharger);
            buttonGroup.getChildren().add(bouttonCharger);

            bouttonCharger.setOnAction(actionEvent -> {
                System.out.println(menuButton.getText());
                ArrayList<Course> listeCours = controleur.getListeCours(menuButton.getText());
                System.out.println(listeCours);
                tableView.getItems().clear();

                for (int i = 0; i < listeCours.size(); i ++){
                    tableView.getItems().add(listeCours.get(i));
                }

            });


            // Positionnement du buttonGroup

            buttonGroup.setMaxWidth(Double.MAX_VALUE);
            buttonGroup.setMaxHeight(Double.MAX_VALUE);
            buttonGroup.setAlignment(Pos.CENTER);
            vBox2.getChildren().add(buttonGroup);



            //// vBox 3 : Section d'informations personnelles ////

            VBox vBox3 = new VBox();
            vBox3.setPrefSize(scene.getWidth()/2, scene.getHeight());
            vBox3.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            vBox3.setMargin(vBox3, new Insets(3,3,3,3));
            vBox3.setPadding(new Insets(8,8,8,8));


            // Label

            Label titre2 = new Label ("Formulaire d'inscription");
            titre2.setMaxWidth(Double.MAX_VALUE);
            titre2.setAlignment(Pos.CENTER);
            titre2.setFont(Font.font("Amble CN",18));
            vBox3.getChildren().add(titre2);

            // GridPane

            GridPane gridPane = new GridPane();
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setPadding(new Insets(40,20,40,20));
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            ColumnConstraints colone1 = new ColumnConstraints(50,50,Double.MAX_VALUE);
            colone1.setHalignment(HPos.RIGHT);

            ColumnConstraints colone2 = new ColumnConstraints(125,125,Double.MAX_VALUE);
            colone2.setHalignment(HPos.LEFT);

            gridPane.getColumnConstraints().addAll(colone1,colone2);


            Label prenom = new Label("Prénom");
            GridPane.setHalignment(prenom, HPos.LEFT);
            gridPane.add(prenom,0,1);
            TextField prenomText = new TextField();
            prenomText.setPrefHeight(15);
            gridPane.add(prenomText, 1, 1);

            Label nom = new Label("Nom");
            GridPane.setHalignment(nom, HPos.LEFT);
            gridPane.add(nom,0,2);
            TextField nomText = new TextField();
            nomText.setPrefHeight(15);
            gridPane.add(nomText, 1, 2);

            Label email = new Label("Email");
            GridPane.setHalignment(email, HPos.LEFT);
            gridPane.add(email,0,3);
            TextField emailText = new TextField();
            emailText.setPrefHeight(15);
            gridPane.add(emailText, 1, 3);

            Label matricule = new Label("Matricule");
            GridPane.setHalignment(matricule, HPos.LEFT);
            gridPane.add(matricule,0,4);
            TextField matriculeText = new TextField();
            matriculeText.setPrefHeight(15);
            gridPane.add(matriculeText, 1, 4);


            // Boutton envoyer

            Button boutonEnvoyer = new Button("envoyer");

            GridPane.setHalignment(boutonEnvoyer, HPos.CENTER);
            gridPane.add(boutonEnvoyer,1,5);

            boutonEnvoyer.setOnAction(actionEvent -> {
                String stringPrenom = prenomText.getText();
                String stringNom = nomText.getText();
                String stringEmail = emailText.getText();
                String stringMatricule = matriculeText.getText();

                Course clic =tableView.getSelectionModel().getSelectedItem();
                if (clic == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur cours invalide");
                    alert.setContentText("Formulaire invalide. \n" + "Veuillez selectionner un cours.");
                    alert.showAndWait();

                }
                String coursTableView = (String) clic.getName();
                String codeTableView = (String) clic.getCode();
                //
                System.out.println(coursTableView);
                System.out.println(codeTableView);

                try {
                    RegistrationForm inscription = controleur.inscription(stringPrenom, stringNom, stringEmail, stringMatricule, codeTableView);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });


            vBox3.getChildren().add(gridPane);


            //// Assemblage des vBoxs,

            VBox gauche = new VBox();
            gauche.getChildren().addAll(vBox1,vBox2);
            pane.setLeft(gauche);

            VBox droite = new VBox();
            droite.getChildren().addAll(vBox3);
            pane.setRight(droite);

            primaryStage.setTitle("Inscription UdeM");
            primaryStage.setScene(scene);
            primaryStage.show();


        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
