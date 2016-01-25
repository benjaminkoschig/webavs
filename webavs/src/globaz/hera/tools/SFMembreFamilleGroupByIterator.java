/*
 * Créé le 11 mai. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.tools;

import globaz.hera.db.famille.SFMembreFamille;
import java.util.Iterator;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFMembreFamilleGroupByIterator extends SFAbstractGroupByIterator {

    /**
     * @param iterator
     * @throws NullPointerException
     */
    public SFMembreFamilleGroupByIterator(Iterator iterator) throws NullPointerException {
        super(iterator);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Vrai si les deux objets sont égaux
     * 
     * @return true si les deux objets sont égaux
     */
    @Override
    public boolean areEntitiesEquals(Object e1, Object e2) {

        if (e1 != null && e2 != null) {
            return ((SFMembreFamille) e1).getIdMembreFamille().equals(((SFMembreFamille) e2).getIdMembreFamille());
        } else {
            return false;
        }
    }
}