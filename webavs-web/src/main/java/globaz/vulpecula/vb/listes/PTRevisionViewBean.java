package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTRevisionViewBean extends BJadeSearchObjectELViewBean {
    private String email;
    private String idEmployeur;
    private String dateReference;
    private String anneeDebut;
    private String anneeFin;
    private String designationEmployeur;
    private String date;
    private String heure = "08:00";
    private String reviseur = "Edwin Locher/dd";
    private boolean printLettre;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    /**
     * Méthode utilisée par le framework pour setter l'email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de l'employeur.
     * 
     * @param idEmployeur String représentant un id de l'employeur
     */
    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de la convention.
     * 
     * @return email auquel le mail sera envoyé
     */

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    public String getEmployeurViewService() {
        return EmployeurServiceCRUD.class.getName();
    }

    public String getDesignationEmployeur() {
        return designationEmployeur;
    }

    public void setDesignationEmployeur(String designationEmployeur) {
        this.designationEmployeur = designationEmployeur;
    }

    public String getMessageEmployeurManquant() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.EMPLOYEUR_MANQUANT);
    }

    public String getMessageAnneeObligatoire() {
        return SpecificationMessage
                .getMessage(I18NUtil.getUserLocale(), SpecificationMessage.MESSAGE_ANNEE_OBLIGATOIRE);
    }

    public String getMessageProcessusLance() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LISTE_PROCESSUS_LANCE");
    }

    public String getDate() {
        if (date == null) {
            return Date.now().getSwissValue();
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getReviseur() {
        return reviseur;
    }

    public void setReviseur(String reviseur) {
        this.reviseur = reviseur;
    }

    /**
     * @return the dateReference
     */
    public String getDateReference() {
        if (dateReference == null) {
            return Date.now().getSwissValue();
        }
        return dateReference;
    }

    /**
     * @param dateReference the dateReference to set
     */
    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

    public boolean isPrintLettre() {
        return printLettre;
    }

    public void setPrintLettre(boolean printLettre) {
        this.printLettre = printLettre;
    }
}
