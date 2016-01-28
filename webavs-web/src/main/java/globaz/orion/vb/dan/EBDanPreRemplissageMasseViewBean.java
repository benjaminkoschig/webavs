package globaz.orion.vb.dan;

import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.vb.EBAbstractViewBean;

/**
 * View bean qui match l'écran de lancement de pré-remplissage de la DAN
 * 
 * @author SCO
 * @since 05 avr. 2011
 */
public class EBDanPreRemplissageMasseViewBean extends EBAbstractViewBean {

    private String annee = null;
    private String email = null;
    private String typeDeclaration = null;

    public String getAnnee() {
        return annee;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    public void validate() {

        if (JadeStringUtil.isEmpty(getAnnee())) {
            getSession().addError(getSession().getLabel("VAL_ANNEE"));
        }

        if (JadeStringUtil.isEmpty(getTypeDeclaration())) {
            getSession().addError(getSession().getLabel("VAL_TYPE_DECLARATION"));
        }
    }
}
