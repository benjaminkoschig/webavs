/*
 * Créé le 6 août 07
 */
package globaz.corvus.vb.annonces;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author HPE
 * 
 */
public class REAnnoncesAugmentationModification10EmeViewBean extends REAnnoncesAugmentationModification10Eme implements
        FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeEnr02CodeEnregistrement01 = "";
    REAnnoncesAugmentationModification10Eme codeEnregistrement02 = new REAnnoncesAugmentationModification10Eme();

    REAnnoncesAugmentationModification10Eme codeEnregistrement03 = new REAnnoncesAugmentationModification10Eme();
    String idLienAnnonceCodeEnregistrement02 = "";

    String idLienAnnonceCodeEnregistrement03 = "";

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    public boolean isAnnonceDiminution() {
        if (this.getDebutDroit().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String getCodeEnr02CodeEnregistrement01() {
        return codeEnr02CodeEnregistrement01;
    }

    public REAnnoncesAugmentationModification10Eme getCodeEnregistrement02() throws Exception {

        if (codeEnregistrement02.isNew()) {
            if (JadeStringUtil.isBlankOrZero(getIdLienAnnonce())) {
                return new REAnnoncesAugmentationModification10Eme();
            } else {
                codeEnregistrement02 = new REAnnoncesAugmentationModification10Eme();
                codeEnregistrement02.setSession(getSession());
                codeEnregistrement02.setIdAnnonce(getIdLienAnnonce());
                codeEnregistrement02.retrieve();

                setIdLienAnnonceCodeEnregistrement02(codeEnregistrement02.getIdLienAnnonce());

                return codeEnregistrement02;
            }
        } else {
            return codeEnregistrement02;
        }
    }

    public REAnnoncesAugmentationModification10Eme getCodeEnregistrement03() throws Exception {

        if (codeEnregistrement03.isNew()) {
            if (JadeStringUtil.isBlankOrZero(getIdLienAnnonce())) {
                return new REAnnoncesAugmentationModification10Eme();
            } else {
                codeEnregistrement03 = new REAnnoncesAugmentationModification10Eme();
                codeEnregistrement03.setSession(getSession());
                codeEnregistrement03.setIdAnnonce(getIdLienAnnonce());
                codeEnregistrement03.retrieve();

                setIdLienAnnonceCodeEnregistrement03(codeEnregistrement03.getIdLienAnnonce());

                return codeEnregistrement03;
            }
        } else {
            return codeEnregistrement03;
        }
    }

    public String getIdLienAnnonceCodeEnregistrement02() {
        return idLienAnnonceCodeEnregistrement02;
    }

    public String getIdLienAnnonceCodeEnregistrement03() {
        return idLienAnnonceCodeEnregistrement03;
    }

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return IREAnnonces.CS_ETAT_OUVERT.equals(getEtat());
    }

    /**
     * getter pour l'attribut supprimer permis.
     * 
     * @return la valeur courante de l'attribut supprimer permis
     */
    public boolean isSupprimerPermis() {
        return IREAnnonces.CS_ETAT_OUVERT.equals(getEtat());
    }

    public void setCodeEnr02CodeEnregistrement01(String codeEnr02CodeEnregistrement01) {
        this.codeEnr02CodeEnregistrement01 = codeEnr02CodeEnregistrement01;
    }

    public void setCodeEnregistrement02(REAnnoncesAugmentationModification10Eme codeEnregistrement02) {
        this.codeEnregistrement02 = codeEnregistrement02;
    }

    public void setCodeEnregistrement03(REAnnoncesAugmentationModification10Eme codeEnregistrement03) {
        this.codeEnregistrement03 = codeEnregistrement03;
    }

    public void setIdLienAnnonceCodeEnregistrement02(String idLienAnnonceCodeEnregistrement02) {
        this.idLienAnnonceCodeEnregistrement02 = idLienAnnonceCodeEnregistrement02;
    }

    public void setIdLienAnnonceCodeEnregistrement03(String idLienAnnonceCodeEnregistrement03) {
        this.idLienAnnonceCodeEnregistrement03 = idLienAnnonceCodeEnregistrement03;
    }

}
