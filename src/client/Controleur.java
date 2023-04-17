package client;


import javafx.scene.control.Alert;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class Controleur {

    private Socket cS;
    private ObjectOutputStream objOutStream;

    private ObjectInputStream objInStream;

    private ArrayList<Course> listeCours;


    private void connect(){
        try {
            cS = new Socket("127.0.0.1", 1337);

            objOutStream = new ObjectOutputStream(cS.getOutputStream());
            objInStream = new ObjectInputStream(cS.getInputStream());


        } catch (ConnectException x) {
            //System.out.println("Connexion impossible au port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public Controleur() {
       //connect();
    }


    /**
     *
     * Fermeture des streams et deconnection du client
     *
     */
    public void disconnect(){
        try{
            cS.close();
            objOutStream.close();
            objInStream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


    /**
     *
     *
     *
     * @param session
     * @return
     */

    public ArrayList<Course> getListeCours(String session) {
        try{
            connect();
            if (session.equals("Été")){session = "Ete";}

            Object commande = new String("CHARGER " + session);
            objOutStream.writeObject(commande);
            objOutStream.flush();
            listeCours = (ArrayList<Course>) objInStream.readObject();

            System.out.println("Liste cours est " + listeCours);

            return listeCours;

        }
        catch(IOException e){
            System.out.println("IOException");
            return null;
        }
        catch (ClassNotFoundException c){
            System.out.println("ClassnotFound exception");
            return null;
        }
    }


    /**
     *
     * Recoit les informations de l'etudiant et les verifie pour que l'inscription soit validée.
     *
     * @param prenom Prénom de l'etudiant (String)
     * @param nom Nom de l'étudiant (String)
     * @param email Email de l'étudiant (String)
     * @param matricule Matricule de l'étudiant (String)
     * @param code Code de l'étudiant (string)
     * @return
     * @throws IOException Erreur lors de l'éxecution ou l'écriture du fichier
     */
    public RegistrationForm inscription(String prenom, String nom, String email, String matricule, String code) throws IOException {

        connect();

        //Trouver  quoi faire si matricule, courriel ou code cours pas bon
        if (email.length() < 14 || ! email.substring(email.length()-13).equals("@umontreal.ca")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur courriel");
            alert.setContentText("Courriel invalide. \n" + " Veuillez entrer une adresse courrielle valide");
            alert.showAndWait();
            return null;

        }

        // Vérification du matricule
        if (matricule.length() != 8 && matricule.matches("^[0-9]+$")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur matricule");
            alert.setContentText("Matricule incorrect. \n" +" Veuillez saisir un matricule valide");
            alert.showAndWait();
            return null;

        }



        Course coursInscrire = trouverCours(listeCours, code);
        if (coursInscrire == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur cours invalide");
            alert.setContentText("Formulaire invalide. \n" + " Veuillez selectionner un cours.");
            alert.showAndWait();
            return null;

        }

        RegistrationForm insc = new RegistrationForm(prenom, nom, email, matricule, coursInscrire);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation inscription");
        alert.setContentText("Felicitation! " + insc.getPrenom() + " " + insc.getNom() + " est inscrit(e) \n" + "avec succès pour le cours " + coursInscrire.getCode());
        alert.showAndWait();

        Object commandeIns = new String("INSCRIRE");
        objOutStream.writeObject(commandeIns);
        objOutStream.writeObject(insc);
        objOutStream.flush();

        return insc;
    }

    private Course trouverCours(ArrayList<Course> listeCours, String codeATrouver){
        for (Course cours: listeCours){
            if (cours.getCode().equals(codeATrouver)){
                System.out.println(cours);
                return cours;
            }
        }
        return null;
    }

}