/*
 * Cr�� le 9 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author mmu
 * 
 *         Manager faisant le lien entre la table des enfants (SFENFANT) et les membresFamille(SFMBRFAM) pour afficher
 *         les infos perso de l'enfant (nom,prenom,...)
 */
public class SFApercuEnfantManager extends SFEnfantManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFrom(BStatement statement) {
        return SFApercuEnfant.createFromClause(_getCollection());
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFApercuEnfant();
    }

}
