package globaz.lynx.db.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

public class LXChoixCanevasViewBean extends LXJournal implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idFournisseur = null;
    private String idOperationCanevas = null;
    private String idSectionCanevas = null;
    private LXJournal journal = null;
    private String libelle = null;

    private String montant = null;

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdOperationCanevas() {
        return idOperationCanevas;
    }

    public String getIdSectionCanevas() {
        return idSectionCanevas;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Permet de retrouner le journal
     * 
     * @return LXJournal
     */
    public LXJournal getJournal() {
        if (journal == null && !JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            retrieveJournal();
        }
        return journal;
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    /**
     * Permet de savoir si le journal est dans un etat d'édition
     * 
     * @return boolean
     */
    public boolean isJournalEditable() {
        try {
            return (getJournal() != null && !getJournal().isNew() && getJournal().isOuvert());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Récupère le journal
     * 
     * @return LXJournal
     */
    private LXJournal retrieveJournal() {
        if (journal == null && !JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            journal = new LXJournal();
            journal.setSession(getSession());

            journal.setIdJournal(getIdJournal());

            try {
                journal.retrieve();
            } catch (Exception e) {
                // Do nothing;
            }
        }

        return journal;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdOperationCanevas(String idOperationCanevas) {
        this.idOperationCanevas = idOperationCanevas;
    }

    public void setIdSectionCanevas(String idSectionCanevas) {
        this.idSectionCanevas = idSectionCanevas;
    }

    @Override
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
