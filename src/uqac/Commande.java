package uqac;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * C'est cette objet Commande qui va être échangé par le client le serveur via son flux i/o.
 * @author Orann
 */
public class Commande implements Serializable {

    // Notre commande de base sera décomposée en plusieurs String (pour la classe, la fonciton et les paramètres). 
    private ArrayList<String> commandes = new ArrayList<>();

    // On stock le résultat de la commande.
    private String resultat;

    /**
     * Instancie une nouvelle Commande.
     *
     * @param commande
     */
    public Commande(String commande) {
        // On split notre commande de base pour récupérer les différents String nécessaires.
        for (String str : commande.split("&")) {
            commandes.add(str);
        }
        // Pour l'instant, le résultat n'est pas traiter mais le sera par la suite grâce au serveur.
        resultat = "";
    }

    /**
     * Get la commande de base.
     *
     * @return commandes
     */
    public ArrayList<String> getCommandes() {
        return commandes;
    }

    /**
     * Get la classe de la commande.
     *
     * @return String
     */
    public String getClassCommande() {
        return commandes.get(0);
    }

    /**
     * Get la fonction de la commande.
     *
     * @return String
     */
    public String getFunctionCommande() {
        return commandes.get(1);
    }

    /**
     * Get le.s paramètres de la commande de base.
     *
     * @return String
     */
    public String getParametersCommande() {
        return commandes.get(2);
    }

    /**
     * Set le resultat de la commande de base.
     *
     * @param resultat de la commande.
     */
    public void setResultatCommande(String resultat) {
        this.resultat = resultat;
    }

    /**
     * Get le resultat de la commande de base.
     *
     * @return String du résultat de la commande.
     */
    public String getResultatCommande() {
        return this.resultat;
    }
}
