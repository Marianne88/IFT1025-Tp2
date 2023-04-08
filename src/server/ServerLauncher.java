package server;

public class ServerLauncher {

    /**
     * Numéro du port sur lequel le serveur se connecte.
     */
    public final static int PORT = 1337;

    /**
     * Connection au serveur en appelant la fonction run d'une instance de la classe serveur
     * @param args non utilisé
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}