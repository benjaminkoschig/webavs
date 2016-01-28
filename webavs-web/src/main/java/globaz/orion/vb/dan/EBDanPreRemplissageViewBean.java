package globaz.orion.vb.dan;

import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.vb.EBAbstractViewBean;

/**
 * View bean qui match l'écran de lancement de pré-remplissage de la DAN
 * 
 * @author SCO
 * @since 05 avr. 2011
 */
public class EBDanPreRemplissageViewBean extends EBAbstractViewBean {

    private String annee = null;
    private String email = null;
    private String nomAffilie = "";
    private String numAffilie = "";

    public String getAnnee() {
        return annee;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void validate() {

        if (JadeStringUtil.isEmpty(getAnnee())) {
            getSession().addError(getSession().getLabel("VAL_ANNEE"));
        }

        if (JadeStringUtil.isEmpty(getNumAffilie())) {
            getSession().addError(getSession().getLabel("VAL_NUM_AFFILIE"));
        }
    }
}
