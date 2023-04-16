package client;


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

 //   public void run(){

        //      try{

 ///       connect();

        //     }
        //     catch (ConnectException x){
        //         System.out.println("Connexion impossible aui port 1337: pas de serveur.");
        //     }
        //    catch(ClassNotFoundException e){
        //        System.out.println("La classe n'existe pas.");
        //        e.printStackTrace();
        //  }

 //   }


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

    public RegistrationForm inscription(String prenom, String nom, String email, String matricule, String code) throws IOException {

        connect();

        //Trouver  quoi faire si matricule, courriel ou code cours pas bon
        if (email.length() < 14 || ! email.substring(email.length()-13).equals("@umontreal.ca")) {
            return null;
            //System.out.print("Courriel invalide, veuillez entrer une adresse courrielle valide:");
        }

        // Vérification du matricule
        if (matricule.length() != 8 && matricule.matches("^[0-9]+$")){
            return null;
            //System.out.print("Matricule incorrect, veuillez réessayer, veuillez saisir un matricule valide:");
        }


        //Course coursInscrire = new Course()
        Course coursInscrire = trouverCours(listeCours, code);
        if (coursInscrire == null){
            return null;
            //System.out.print("Cours entré invalide, veuillez entrer un cours qui est disponible pour la session sélectionnée:");
            //coursInscrire = trouverCours(listeCours, code);
        }

        RegistrationForm insc = new RegistrationForm(prenom, nom, email, matricule, coursInscrire);

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