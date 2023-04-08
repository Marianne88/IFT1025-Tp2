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
            //Socket cS = new Socket("127.0.0.1", 1337);
            connect();

            //ObjectOutputStream objOutStream = new ObjectOutputStream(cS.getOutputStream());

            //ObjectInputStream objInStream = new ObjectInputStream(cS.getInputStream());

            //OutputStreamWriter os = new OutputStreamWriter(objOutStream);


            //this.inscription(null);
            this.choixCours();

            disconnect();
            //cS.connect("127.0.0.1", 1337);

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
//                    connect();
                    this.choixCours();
                }
                else if (choixIntermediaire.equals("2")){
//                    connect();
                    Object commandeIns = new String("INSCRIRE");
                    objOutStream.writeObject(this.inscription(listeCours));
                    objOutStream.flush();
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

        //Faire vérification de courriel
        System.out.print("Veuillez saisir votre email: ");
        String email = sc.nextLine();


        //Verifier que juste chiffre
        System.out.print("Veuillez saisir votre matricule: ");
        String matricule = sc.nextLine();

        while (matricule.length() != 8){
            System.out.println("Matricule incorrect, veuillez réessayer");
            System.out.print("Veuillez saisir votre matricule: ");
            matricule = sc.nextLine();
        }

        System.out.print("Veuillez saisir le code du cours: ");
        String code = sc.nextLine();


        Course coursInscrire = trouverCours(listeCours, code);
        if (coursInscrire == null){
            System.out.println("Cours entré invalide");
            return null;
        }

        //connect();

        System.out.println("Félicitation! Inscription réussie de " + prenom + " au cours " + code);

        return new RegistrationForm(prenom, nom, email, matricule, coursInscrire);
        //return new RegistrationForm(prenom, nom, email, matricule, new Course("Prog1", "IFT1015", "Automne" ));

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
