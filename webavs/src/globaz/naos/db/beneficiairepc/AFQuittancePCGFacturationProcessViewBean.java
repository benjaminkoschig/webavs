/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db.beneficiairepc;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.process.AFQuittancePCGFacturationProcess;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFQuittancePCGFacturationProcessViewBean extends AFQuittancePCGFacturationProcess implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPassageFacturation = "";
    private String libelleJournal = "";
    private String libellePassageFacturation = "";
    private String typeMontant = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isBlankOrZero(getIdPassageFacturation())) {
            this._addError(getSession().getLabel("AUCUN_PASSAGE"));
        }

        if (getTypeMontant().equalsIgnoreCase("montantNet") && JadeStringUtil.isBlankOrZero(getTaux())) {
            this._addError(getSession().getLabel("TAUX_OBLIG"));
        }

        // init le job queue du process
        super._validate();
    }

    @Override
    protected String getEMailObject() {
        return "Facturation des quittances PCG";
    }

    @Override
    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getLibelleJournal() {
        return libelleJournal;
    }

    public String getLibellePassageFacturation() {
        return libellePassageFacturation;
    }

    public String getTypeMontant() {
        return typeMontant;
    }

    @Override
    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setLibelleJournal(String libelleJournal) {
        this.libelleJournal = libelleJournal;
    }

    public void setLibellePassageFacturation(String libellePassageFacturation) {
        this.libellePassageFacturation = libellePassageFacturation;
    }

    public void setTypeMontant(String typeMontant) {
        this.typeMontant = typeMontant;
    }
}
