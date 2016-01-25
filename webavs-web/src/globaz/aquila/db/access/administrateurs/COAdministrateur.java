/*
 * Créé le 22 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.access.administrateurs;

import globaz.aquila.db.access.plaintes.COPlaintePenaleManager;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteurManager;

/**
 * @author dvh
 */
public class COAdministrateur extends CACompteAnnexe {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        CACompteurManager compteurManager = new CACompteurManager();
        compteurManager.setSession(getSession());
        compteurManager.setForIdCompteAnnexe(getIdCompteAnnexe());
        if (!compteurManager.isEmpty()) {
            _addError(transaction, getSession().getLabel("AQUILA_COMPTEURS_EXISTANTS"));
        }
        COPlaintePenaleManager plaintePenaleManager = new COPlaintePenaleManager();
        plaintePenaleManager.setSession(getSession());
        plaintePenaleManager.setForIdCompteAuxiliaire(getIdCompteAnnexe());
        if (plaintePenaleManager.getCount(transaction) != 0) {
            _addError(transaction, getSession().getLabel("AQUILA_PLAINTES_EXISTANTES"));
        }
        super._beforeDelete(transaction);
    }

}
