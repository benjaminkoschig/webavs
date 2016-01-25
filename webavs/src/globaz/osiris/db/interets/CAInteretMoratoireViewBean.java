/*
 * Créé le 27 janv. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.interets;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAPassage;

/**
 * @author jts 27 janv. 05 11:27:14
 */
public class CAInteretMoratoireViewBean extends CAInteretMoratoire implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String enteteFactureDescriptionDecompte;

    private String enteteFactureTypeDescription = new String();

    private String libelleRubrique = new String();
    private String nomTiers = new String();
    private boolean nouvelleDecision = false;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        if (isSoumis() || isManuel()) {
            BSession muscaSession = (BSession) GlobazServer.getCurrentSystem()
                    .getApplication(globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA)
                    .newSession(getSession());

            FAAfact afact = new FAAfact();
            afact.setSession(muscaSession);
            afact.setAlternateKey(CAInteretMoratoire.AK_INTERETSMORATOIRES);
            afact.setReferenceExterne(getIdInteretMoratoire());

            afact.retrieve(transaction);

            if (afact.hasErrors()) {
                throw new Exception(afact.getErrors().toString());
            }

            if (!afact.isNew()) {
                if ((getPassage() != null) && !getPassage().getStatus().equals(FAPassage.CS_ETAT_COMPTABILISE)
                        && !getPassage().getStatus().equals(FAPassage.CS_ETAT_ANNULE)) {
                    afact.delete(transaction);
                } else {
                    throw new Exception(getSession().getLabel("DELETE_INTERET_ERROR"));
                }
            }

        }

    }

    /*
     * Si la décision d'intérêt moratoire est modifiée, il faut synchroniser les afacts
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdJournalFacturation())) {
            updateAfact(transaction);
            updateEntete(transaction);
        }
    }

    @Override
    protected void _validate(BStatement statement) {
        super._validate(statement);
        if (getMotifcalcul().equalsIgnoreCase(CAInteretMoratoire.CS_AUTOMATIQUE)) {
            _addError(statement.getTransaction(), getSession().getLabel("ERROR_IM_MODE_AUTMATIQUE_INTERDIT"));
        }
    }

    public String getEnteteFactureDescriptionDecompte() {
        if (JadeStringUtil.isBlank(enteteFactureDescriptionDecompte)) {
            enteteFactureDescriptionDecompte = getEnteteFacture().getDescriptionDecompte();
        }
        return enteteFactureDescriptionDecompte;

    }

    public String getEnteteFactureTypeDescription() {
        if (JadeStringUtil.isBlank(enteteFactureTypeDescription)) {
            enteteFactureTypeDescription = getEnteteFacture().getTypeDescription();
        }
        return enteteFactureTypeDescription;
    }

    /**
     * @return
     */
    public String getLibelleRubrique() {
        return libelleRubrique;
    }

    /**
     * @return
     */
    public String getNomTiers() {
        if (JadeStringUtil.isBlank(nomTiers)) {
            if (isDomaineCA()) {
                nomTiers = getCompteAnnexe().getTiers().getNom();
            } else {
                nomTiers = getEnteteFacture().getNomTiers();
            }
        }
        return nomTiers;
    }

    /**
     * @return
     */
    public boolean isNouvelleDecision() {
        return nouvelleDecision;
    }

    /**
     * @param string
     */
    public void setEnteteFactureDescriptionDecompte(String string) {
        enteteFactureDescriptionDecompte = string;
    }

    /**
     * @param string
     */
    public void setEnteteFactureTypeDescription(String string) {
        enteteFactureTypeDescription = string;
    }

    /**
     * @param string
     */
    public void setLibelleRubrique(String string) {
        libelleRubrique = string;
    }

    /**
     * @param string
     */
    public void setNomTiers(String string) {
        nomTiers = string;
    }

    /**
     * @param b
     */
    public void setNouvelleDecision(boolean b) {
        nouvelleDecision = b;
    }

}
