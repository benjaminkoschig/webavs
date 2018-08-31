package globaz.vulpecula.vb.ctrlemployeur;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;

public class PTRecapEmployeurViewBean extends BJadeSearchObjectELViewBean {

    private String email;
    private String idEmployeur;
    private String dateReference = Date.now().getSwissValue();

    public String getEmployeurService() {
        return EmployeurServiceCRUD.class.getName();
    }

    /**
     * @return the email
     */
    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    /**
     * @return the idEmployeur
     */
    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param idEmployeur the idEmployeur to set
     */
    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * @return the dateReference
     */
    public String getDateReference() {
        return dateReference;
    }

    /**
     * @param dateReference the dateReference to set
     */
    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }
}
