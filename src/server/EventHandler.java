package server;

@FunctionalInterface
public interface EventHandler {
    /**
     * Interface fonctionnelle qui n'a qu'une fonction qui doit être définie dans les classes anonymes qui l'implémentent
     * Handle gère la commande envoyée en paramètre en fonction du cas et selon l'argument passé en paramètre.
     * @param cmd String commande à gérer
     * @param arg String argument à traiter avec la commande
     */
    void handle(String cmd, String arg);
}