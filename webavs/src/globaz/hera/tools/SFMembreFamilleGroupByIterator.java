/*
 * Cr�� le 11 mai. 07
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.tools;

import globaz.hera.db.famille.SFMembreFamille;
import java.util.Iterator;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
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
     * Vrai si les deux objets sont �gaux
     * 
     * @return true si les deux objets sont �gaux
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