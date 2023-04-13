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

    /**
     * Commande à utiliser pour appeler la fonction handleRegistration()
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";

    /**
     * Commande à utiliser pour appeler la fonction handleLoadCourses
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur de la classe serveur
     * Ouvre le ServerSocket sur le port passé en paramétre et initialise les handlers comme arraylist de EventHandler (interface fonctionnelle)
     * et ajoute les deux commandes prédéfinies dans la liste de handlers (charger les cours disponible et s'inscrire à un cours)
     * @param port numéro de port sur lequel le serveur se connecte
     * @throws IOException erreur lors de la lecture ou écriture de fichiers
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajouter un EventHandler à la liste handlers de la classe
     * @param h EventHandler à ajouter
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Appeler les handles avec la commande et l'argument mis en paramètre
     * @param cmd commande à envoyer aux handlers (String)
     * @param arg argument à passer en paramètre au handler (String)
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Fonction qui démarre le serveur et attend qu'un client se connecte. Lorsque le client est connecté, la fonction crée
     * un buffered writer et reader pour recevoir et envoyer des information au client
     * Lorsque le serveur reçoit une commande du client, le serveur la traite avec la fonction listen puis déconnecte se déconnecte.
     * Cette boucle se répète tant que le serveur et le client sont connectés
     */
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

    /**
     * La fonction lit l'information envoyée par le client dans le InputStream si celui-ci est non-nul. La fonction appelle
     * la fonction alerte les handlers pour déterminer si la commande envoyée correspond à un handler qui existe et
     * si c'est le cas, la focntion correspondante est appelée
     * @throws IOException erreur à la lecture ou l'écriture du fichier pour la lecture des cours disponibles ou pour l'écriture dans le fichier d'inscriptions
     * @throws ClassNotFoundException erreur dans la lecture d'un objet dont la classe n'est pas connue
     */
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

    /**
     * La fonction prend en paramtre une ligne et la sépare de sorte à créer une paire de strings dont le premier terme correspond à la commande
     * et le deuxième correspond à l'argument (qui peut être composé de plusieurs mots).
     * La commnde correspond au premier mot dans la ligne de commande passée en paramètre et l'argument correspond au reste de la ligne.
     * @param line Strign qui correspond à la ligne de commande qui doit être traitée.
     * @return une paire de string dont le premier terme correspond à la commande et le deuxième terme correspond à l'argument
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Fermeture des stream et deconnection du client
     * @throws IOException erreur à la lecture ou l'écriture du fichier pour la lecture des cours disponibles ou pour l'écriture dans le fichier d'inscriptions
     */
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

    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        try{

            RegistrationForm ins = (RegistrationForm) objectInputStream.readObject();

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/server/data/inscription.txt", true));

            String tab = "\t";


            String inscription = ins.getCourse().getSession() + tab + ins.getCourse().getCode() + tab +
                    ins.getMatricule() + tab + ins.getPrenom() + tab + ins.getNom() + tab + ins.getEmail() + "\n";

            writer.write(inscription);
            writer.close();

            System.out.println("Nouvelle inscription : \n" + inscription);

        }
        catch(IOException e){
            System.out.println("Erreur à la lecture du fichier");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur, la classe n'existe pas.");
            throw new RuntimeException(e);
        }

    }
}