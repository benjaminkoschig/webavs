package globaz.musca.itext.impfactbvrutil;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.musca.application.FAApplication;

/**
 * Object qui permet à partir des propriétés applicatives d'initialiser les propriétés du processus de
 * génération/impression des BVR
 * 
 * @author VYJ
 */
public abstract class FAImpFactPropertiesProvider {

    /**
     * Permet d'activer le mode debug pour les logs
     */
    public static boolean IS_DEBUG_MODE = false;
    /**
     * Permet de savoir si l'objet a déjà été initialisé ou pas
     */
    public static boolean IS_INITIALIZED = false;
    /**
     * Permet de définir si l'on fait du multi threading (chargement asynchrone des données) ou pas...
     */
    public static boolean IS_MODE_ASYNCHRONE = false;
    /**
     * Taille maximale de la pile, si la pile est au delà de cette taille, le loader se met en attente afin que le
     * consommateur ait la possiblité de consommer les données. Ce mequanisme est en place pour éviter les problèmes de
     * mémoire.
     */
    public static int MAX_STACK_SIZE = 20;

    /**
     * Permet de définir le temps d'attente dans le cas où la pile n'a pas encore de données à traiter et que le
     * consommateurs souhaite en obtenir - par défaut 100mls
     */
    public static long NEXT_DATASOURCE_WAITING_TIME = 100;

    /**
     * Temps d'attente pour le loader lorsque la taille limite de la pile a été atteinte.
     */
    public static long STACK_FULL_WAITING_TIME = 500;

    /**
     * Permet d'initialiser en fonction des propriétés le paramétrage du système de chargement asynchrone des données
     * 
     * @param application
     */
    protected static void init(FAApplication application) {
        if (!FAImpFactPropertiesProvider.IS_INITIALIZED) {
            // Initialise le temps d'attente lorsque la pile est vide et est en
            // attente d'être chargée pour pouvoir consommer
            String value = application.getProperty("musca.bvr.impression.pile.vide.temps.attente");
            if ((value != null) && JadeNumericUtil.isInteger(value)) {
                FAImpFactPropertiesProvider.NEXT_DATASOURCE_WAITING_TIME = Long.parseLong(value);
            }

            // Initialise la taille maximale de la pile
            value = application.getProperty("musca.bvr.impression.pile.taille.max");
            if ((value != null) && JadeNumericUtil.isInteger(value)) {
                FAImpFactPropertiesProvider.MAX_STACK_SIZE = Integer.parseInt(value);
            }

            // Initialise le temps d'attente du loader lorsque la pile est
            // pleine
            value = application.getProperty("musca.bvr.impression.pile.pleine.temps.attente");
            if ((value != null) && JadeNumericUtil.isInteger(value)) {
                FAImpFactPropertiesProvider.STACK_FULL_WAITING_TIME = Long.parseLong(value);
            }

            // Permet d'activer le mode débug pour les logs
            value = application.getProperty("musca.bvr.impression.debug.mode");
            if (value != null) {
                FAImpFactPropertiesProvider.IS_DEBUG_MODE = Boolean.valueOf(value).booleanValue();
            }

            // Permet de définir si l'on est en mode asynchrone ou pas
            value = application.getProperty("musca.bvr.impression.asynchrone.mode");
            if (value != null) {
                FAImpFactPropertiesProvider.IS_MODE_ASYNCHRONE = Boolean.valueOf(value).booleanValue();
            }

            // Défini comme quoi l'objet est maintenant initialisé avec les
            // valeurs des propriétés
            FAImpFactPropertiesProvider.IS_INITIALIZED = true;
        }
    }
}
