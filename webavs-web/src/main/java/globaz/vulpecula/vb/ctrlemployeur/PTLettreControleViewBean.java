package globaz.vulpecula.vb.ctrlemployeur;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTLettreControleViewBean extends BJadeSearchObjectELViewBean {

    private String reviseur = "Edwin Locher/dd";
    private String email;
    private String date;
    private String dateReference;
    private String heure = "08:00";
    private String anneeDebut;
    private String anneeFin;
    private String idEmployeur;

    private boolean launched = false;

    public String getEmployeurService() {
        return EmployeurServiceCRUD.class.getName();
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

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public List<String> getTypes() {
        return TypeDecompte.getList().subList(0, TypeDecompte.getList().size() - 1);
    }

    public String getMessageEmployeurRequis() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.EMPLOYEUR_MANQUANT);
    }

    public String getMessageProcessusLance() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LISTE_PROCESSUS_LANCE");
    }

    public String getIdEmployeur() {
        return idEmployeur;
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

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getReviseur() {
        return reviseur;
    }

    public void setReviseur(String reviseur) {
        this.reviseur = reviseur;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String paramString) {

    }

    @Override
    public BSpy getSpy() {
        return null;
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

}
