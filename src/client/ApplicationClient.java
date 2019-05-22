package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import uqac.Commande;

/**
 *
 * @author Orann
 */
public class ApplicationClient {    
    /**
     * Prend une uqac.Commande dûment formatée et la fait exécuter par le serveur. 
     *
     * @param uneCommande 
     */
    public ApplicationClient(Commande uneCommande) {
        Socket socket;
        try {
            //On créé un socket pour communiquer avec le serveur sur le port 8080 :
            socket = new Socket("localhost", 8080);

            //On créé les flux i/o :
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Le client a créé les flux d'entrées/sorties");

            //On envoie la commande pour que le serveur la traite :
            out.writeObject(uneCommande);
            out.flush();
            System.out.println("Le client a émit une commande");

            //On récupère le résultat du serveur de la commande précédemment envoyée :
            Object objetRecu = in.readObject();
            String resultat = (String) objetRecu;
            System.out.println("Le client a reçu le résultat de la commande : " + resultat);

            //On sauvegarde le résultat dans un fichier txt :
            Path orderPath = Paths.get("outputs/resultats.txt");
            byte[] strToBytes = resultat.getBytes();
            try {
                Files.write(orderPath, strToBytes, StandardOpenOption.APPEND);
            } catch (IOException ex) {
                Logger.getLogger(ApplicationClient.class.getName()).log(Level.SEVERE, null, ex);
            }

            //On ferme les flux i/o et on ferme également la socket :
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApplicationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Le main de note application client.
     */
    public static void main(String[] args) {
        //On récupère notre fichier de résultats et on écrit une String vide pour le réinitialiser :
        Path orderPath = Paths.get("outputs/resultats.txt");
        String reinitialise = "";
        byte[] strToBytes = reinitialise.getBytes();
        try {
            Files.write(orderPath, strToBytes, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(ApplicationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Fin de la réinitialisation de notre fichier de résultat.

        //On lance le client en le faisant traiter la commande "Calc&add&3,5" :
        ApplicationClient client = new ApplicationClient(new Commande("Calc&add&3,5"));
    }
}