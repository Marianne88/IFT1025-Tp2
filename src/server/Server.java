package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        Object obj = this.objectInputStream.readObject();
        line = obj.toString();
        if (line != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Gérer les commandes reçue en paramètre, si la commande reçue est la commande d'inscription ou de chargement,
     * la fonction relait le travail à la méthode spécifique qui gère la commande dépendemment du cas.
     * Si la commande ne correspond à aucune des deux prédéfinies, la fonction ne fait rien
     * @param cmd String du nom de la commande à traiter
     * @param arg String argument
     */

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transorfmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        try{

            //DataInputStream dIS = new DataInputStream("data/cours.txt");
            //ObjectInputStream is = new ObjectInputStream(new FileInputStream("data/cours.txt"));

            BufferedReader bReader = new BufferedReader(new FileReader("src/server/data/cours.txt"));
            ArrayList<Course> listeCoursDispo = new ArrayList<>();

            String s;
            while((s = bReader.readLine()) != null){
                String[] parts = s.split("\t");
                if (parts[2].equals(arg)){
                    listeCoursDispo.add(new Course(parts[1], parts[0], parts[2] ));
                }

            }

            System.out.println("Les cours disponibles pour la session " + arg + " sont:");
            for(Course cours: listeCoursDispo){
                System.out.println(cours.toString());
            }

            objectOutputStream.writeObject(listeCoursDispo);
            objectOutputStream.flush();
            bReader.close();
            //run();

        }
        catch(IOException e){
            System.out.println("Erreur à la lecture du fichier");
            e.printStackTrace();
        }
//        catch(ClassNotFoundException e){
//            System.out.println("La classe lue n'existe pas dans le programme");
//        }

    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try{

            RegistrationForm ins = (RegistrationForm) objectInputStream.readObject();

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/server/data/inscription.txt"));

            String tab = "\t";

            String inscription = ins.getCourse().getSession() + tab + ins.getCourse().getCode() + tab +
                    ins.getMatricule() + tab + ins.getPrenom() + tab + ins.getNom() + tab + ins.getEmail();

            writer.write(inscription);
            writer.close();

            System.out.println("Nouvelle inscription : \n" + inscription);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}