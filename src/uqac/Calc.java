package uqac;

/**
 *
 * @author Orann
 */
public class Calc {

    /**
     * Constructeur obligatoire à laisser vide pour faire fonctionner les
     * différentes tâches du serveur.
     */
    public Calc() {
    }

    /**
     * Fonction qui ajoute deux String entre eux.
     * @param a
     * @param b
     * @return l'addition des deux String en int.
     */
    public int add(String a, String b) {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }
}
