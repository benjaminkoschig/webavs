/*
 * Créé le 1 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process.generation;

import globaz.globall.db.BSession;
import java.lang.reflect.Constructor;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class LEGeneratorFactory {
    public static ILEGeneration newInstance(String className, BSession session) throws Exception {
        Constructor constructeur = Class.forName(className).getConstructor(new Class[] { BSession.class });
        Object instance = constructeur.newInstance(new Object[] { session });
        return ((ILEGeneration) instance);
    }
}
