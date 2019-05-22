package serveur;

import uqac.Commande;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Orann
 */
public class ApplicationServeur {

    /**
     * Créer un serveur basé sur le socket.
     * Dès qu'un client se connecte, il traite sa commande et lui renvoie un resultat.
     */
    public ApplicationServeur() {
        try {
            ServerSocket socket_server = new ServerSocket(8080);
            System.out.println("Le serveur est prêt ! Port utilisé : " + 8080);
            while (true) {

                // Un client se connecte, on l'accepte :
                Socket socket = socket_server.accept();
                System.out.println("Le serveur a accepté la connexion d'un client");

                //On créé les flux i/o
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                System.out.println("Le serveur a créé les flux d'entrées/sorties");

                //On récupère l'objet Commande envoyé par le client, puis on traite la commande :
                Commande commande = (Commande) in.readObject();
                String resultat = traiteCommande(commande);
                System.out.println("Le serveur a traité la commande et envoie au client le résultat");

                //On envoie le résultat de la commande traité au client :
                commande.setResultatCommande(resultat + "\n");
                out.writeObject(commande.getResultatCommande());
                out.flush();

                //On ferme les flux i/o et on ferme également la socket :
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prend une Commande dument formattée et envoyée par un client et la traite.
     */
    public String traiteCommande(Commande commande) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String resultat = null;
        if (commande instanceof Commande && commande.getClass() != null) {
            //On traite la compilation de Calc dans un premier temps :
            traiterCompilation("./src/uqac/Calc.java");
            
            //Ensuite on charge notre classe uqac.Calc :
            traiterChargement("uqac." + commande.getClassCommande());
            
            //Puis on traite la création/instanciation de l'objet Calc pour pouvoir taiter la commande :
            Object instance = traiterCreation("uqac." + commande.getClassCommande());            
            
            //Enfin on traite l'appel à la fonction définie par le client de notre Objet calc instancié précédemment :
            resultat = traiterAppel("uqac." + commande.getClassCommande(), commande.getFunctionCommande(), commande.getParametersCommande(), instance);
        }
        return resultat;
    }

    /**
     * Traite le chargement d’une classe.
     *
     * @param classe
     */
    public void traiterChargement(String classe) {
        try {
            Class currentClass = Class.forName(classe);
            System.out.println("Classe chargée : " + currentClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Traite la compilation d’un fichier source java.
     */
    public void traiterCompilation(String chemin) {
        // on récupère le  compilateur Java du système
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // on compile
        int result = compiler.run(null, null, null, chemin);

        if (result == 0) {
            // on déplace le fichier compilé dans le répertoire cible
            int index = chemin.lastIndexOf(".");
            String fileWithoutExtension = chemin.substring(chemin.lastIndexOf("/"), index);
            String filename = fileWithoutExtension.replace("/", "");

            try {
                Files.move(Paths.get(chemin.substring(0, index) + ".class"), Paths.get("./src/serveur/classes/" + filename + ".class"), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Classe compilée : " + filename + ".class ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Traite la création/l'instanciation d'un objet.
     *
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object traiterCreation(String classeDeLobjet) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            Class c = Class.forName(classeDeLobjet);
            Constructor cons = c.getConstructor();
            System.out.println("Nouvelle instance Calc créée.");
            return cons.newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Traite l’appel d’une méthode, en prenant comme argument
     * le nom de la classe sur lequel on effectue l’appel, le nom de la fonction à appeler,
     * un tableau de paramètres/d’arguments pour la fonction et l'instance de l'objet pour éxecuter sa méthode.
     */
    public String traiterAppel(Object classe, String nomFonction, String parametres,
            Object instance) {
        
        //On split nos paramètres qui ston contenus dans un seuls String de base
        //dans un tableau maintenant appelé arguments (synonyme pour ne pas se perdre) :
        String[] arguments = parametres.split(",");
        
        //On construit une tableau de types d'objets :
        Class[] typeObjects = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
                typeObjects[i] = String.class;
        }

        try {
            Method method = instance.getClass().getMethod(nomFonction, typeObjects);
            Object value = method.invoke(instance, arguments);
            System.out.println("Le résultat est : " + value.toString());
            return value.toString();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Le main de note application serveur.
     */
    public static void main(String[] args) {
        ApplicationServeur serveur = new ApplicationServeur();
    }
}
