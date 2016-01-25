/**
 * CPValidationJournalRetourViewBean Créé le 26.02.07
 * 
 * @author : jpa
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPValidationJournalRetourViewBean extends CPCommunicationFiscaleRetourValidation implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String eMailAdress = "";

    private String forAnnee = "";

    private String forGrpExtraction = "";

    private String forGrpTaxation = "";

    private String forJournal = "";

    private String forNumAff = "";

    private String forNumAffilie = "";

    private Boolean forValide = new Boolean(false);

    private Boolean isDevalidation = new Boolean(false);

    private String likeNumAffilie = "";

    public String getEMailAdress() {
        return eMailAdress;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForGrpExtraction() {
        return forGrpExtraction;
    }

    public String getForGrpTaxation() {
        return forGrpTaxation;
    }

    public String getForJournal() {
        return forJournal;
    }

    public String getForNumAff() {
        return forNumAff;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public Boolean getForValide() {
        return forValide;
    }

    public Boolean getIsDevalidation() {
        return isDevalidation;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public String getNombreJournauxAValider() {
        CPCommunicationFiscaleRetourValidationManager manager = new CPCommunicationFiscaleRetourValidationManager();
        manager.setWhitAffiliation(true);
        manager.setSession(getSession());
        if (getForJournal().length() > 0) {
            manager.setForJournal(getForJournal());
        }
        if (getLikeNumAffilie().length() > 0) {
            manager.setLikeNumAffilie(getLikeNumAffilie());
        }
        if (getForAnnee().length() > 0) {
            manager.setForAnnee(getForAnnee());
        }
        if (getForGrpExtraction().length() > 0) {
            manager.setForGrpExtraction(getForGrpExtraction());
        }
        if (getForGrpTaxation().length() > 0) {
            manager.setForGrpTaxation(getForGrpTaxation());
        }
        try {
            return String.valueOf(manager.getCount());
        } catch (Exception e) {
            return "0";
        }
    }

    public void setEMailAdress(String mailAdress) {
        eMailAdress = mailAdress;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForGrpExtraction(String forGrpExtraction) {
        this.forGrpExtraction = forGrpExtraction;
    }

    public void setForGrpTaxation(String forGrpTaxation) {
        this.forGrpTaxation = forGrpTaxation;
    }

    public void setForJournal(String forJournal) {
        this.forJournal = forJournal;
    }

    public void setForNumAff(String forNumAff) {
        this.forNumAff = forNumAff;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setForValide(Boolean forValide) {
        this.forValide = forValide;
    }

    public void setIsDevalidation(Boolean isDevalidation) {
        this.isDevalidation = isDevalidation;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }
}
