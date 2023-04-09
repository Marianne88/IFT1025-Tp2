package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private Socket cS;
    ObjectOutputStream objOutStream;

    ObjectInputStream objInStream;


    public void run(){

        try{

            connect();

            this.choixCours();

            //disconnect();

        }
        catch (ConnectException x){
            System.out.println("Connexion impossible aui port 1337: pas de serveur.");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    private void connect() {
        try {
            cS = new Socket("127.0.0.1", 1337);

            objOutStream = new ObjectOutputStream(cS.getOutputStream());

            objInStream = new ObjectInputStream(cS.getInputStream());


        } catch (ConnectException x) {
            System.out.println("Connexion impossible aui port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        }
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


        private void choixCours() throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);

        String intro = "\n*** Bienvenue au portail d'inscription de cours de l'UdeM ***\n\n";

        String choixSession = "Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours:\n\n1. Automne\n2. Hiver\n3. Ete\nchoix:";

        System.out.print(intro + choixSession);

        String sessionChoisie = "";


            String choixSessionUser = sc.nextLine();
            switch (choixSessionUser){
                case "1" -> sessionChoisie = "Automne";
                case "2" -> sessionChoisie = "Hiver";
                case "3" -> sessionChoisie = "Ete";

            }

            if (sessionChoisie != "") {
                System.out.println("Les cours offerts pour la session " + sessionChoisie + " sont:\n");

                Object commande = new String("CHARGER " + sessionChoisie);
                objOutStream.writeObject(commande);
                objOutStream.flush();

                ArrayList<Course> listeCours = (ArrayList<Course>) objInStream.readObject();

                for (int i = 0; i < listeCours.size(); i++) {
                    System.out.println(i + 1 + ". " + listeCours.get(i).getCode() + "\t" + listeCours.get(i).getName());
                }


                System.out.print("Choix\n1. Consulter les cours pour une autre session\n2. Inscription a un cours\nChoix:");

                disconnect();
                connect();

                String choixIntermediaire = sc.nextLine();


                if (choixIntermediaire.equals("1")){
                    this.choixCours();
                }
                else if (choixIntermediaire.equals("2")){
                    Object commandeIns = new String("INSCRIRE");
                    objOutStream.writeObject(commandeIns);
                    objOutStream.writeObject(this.inscription(listeCours));
                    objOutStream.flush();
                }
                else{
                    System.out.println("choix invlaide, veuillez sélectionner 1 ou 2");
                    choixIntermediaire = sc.nextLine();
                }

        }

        disconnect();

    }

    public RegistrationForm inscription(ArrayList<Course> listeCours){

        Scanner sc = new Scanner(System.in);
        System.out.print("Veuillez saisir votre prénom: ");
        String prenom = sc.nextLine();

        System.out.print("Veuillez saisir votre nom: ");
        String nom = sc.nextLine();


        System.out.print("Veuillez saisir votre email: ");
        String email = sc.nextLine();

        // Vérification du courriel
        while (email.length() < 14 || ! email.substring(email.length()-13).equals("@umontreal.ca")){
            System.out.print("Courriel invalide, veuillez entrer une adresse courrielle valide:");
            email = sc.nextLine();
        }



        System.out.print("Veuillez saisir votre matricule: ");
        String matricule = sc.nextLine();

        // Vérification du matricule
        while (matricule.length() != 8 && matricule.matches("^[0-9]+$")){
            System.out.print("Matricule incorrect, veuillez réessayer, veuillez saisir un matricule valide:");
            matricule = sc.nextLine();
        }

        System.out.print("Veuillez saisir le code du cours: ");
        String code = sc.nextLine();


        Course coursInscrire = trouverCours(listeCours, code);
        while (coursInscrire == null){
            System.out.print("Cours entré invalide, veuillez entrer un cours qui est disponible pour la session sélectionnée:");
            code = sc.nextLine();
            coursInscrire = trouverCours(listeCours, code);
        }


        System.out.println("Félicitation! Inscription réussie de " + prenom + " au cours " + code);

        return new RegistrationForm(prenom, nom, email, matricule, coursInscrire);

    }

    private Course trouverCours(ArrayList<Course> listeCours, String codeATrouver){
        for (Course cours: listeCours){
            if (cours.getCode().equals(codeATrouver)){
                return cours;
            }
        }
        return null;
    }

}
