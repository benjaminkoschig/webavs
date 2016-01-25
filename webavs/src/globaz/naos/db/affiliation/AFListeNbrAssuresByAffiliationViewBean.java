package globaz.naos.db.affiliation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * ViewBean correspondant à l'écran : <BR>
 * CAF2020 - Liste du nombre d'assurés par affiliation paritaire
 */
public class AFListeNbrAssuresByAffiliationViewBean extends AFAbstractViewBean {

    private String forAnnee = "";
    private String mail = "";
    private String fromNumAffilie = "";
    private String toNumAffilie = "";
    private String idAssurance = "";
    private String libelleAssurance = "";

    /**
     * @return the annee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @param annee the annee to set
     */
    public void setForAnnee(String annee) {
        forAnnee = annee;
    }

    /**
     * @return the eMailAddress
     */
    public String getMail() {
        if (!JadeStringUtil.isEmpty(mail)) {
            return mail;
        } else {
            return getSession().getUserEMail();
        }
    }

    /**
     * @param eMailAddress the eMailAddress to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return the fromNumAffilie
     */
    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * @param fromNumAffilie the fromNumAffilie to set
     */
    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    /**
     * @return the toNumAffilie
     */
    public String getToNumAffilie() {
        return toNumAffilie;
    }

    /**
     * @param toNumAffilie the toNumAffilie to set
     */
    public void setToNumAffilie(String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }

    /**
     * @return the codeAssurance
     */
    public String getIdAssurance() {
        return idAssurance;
    }

    /**
     * @param codeAssurance the codeAssurance to set
     */
    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

    /**
     * @return the libelleAssurance
     */
    public String getLibelleAssurance() {
        return libelleAssurance;
    }

    /**
     * @param libelleAssurance the libelleAssurance to set
     */
    public void setLibelleAssurance(String libelleAssurance) {
        this.libelleAssurance = libelleAssurance;
    }
}
