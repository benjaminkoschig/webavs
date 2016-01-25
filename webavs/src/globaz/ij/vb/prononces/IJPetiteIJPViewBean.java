/*
 * Créé le 12 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJPetiteIJ;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPetiteIJPViewBean extends IJAbstractPrononceProxyViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private IJPetiteIJ prononce = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPetiteIJPViewBean.
     */
    public IJPetiteIJPViewBean() {
        super(new IJPetiteIJ());
        prononce = (IJPetiteIJ) getPrononce();

        prononce.setCsEtat(IIJPrononce.CS_ATTENTE);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut cs situation assure
     * 
     * @return la valeur courante de l'attribut cs situation assure
     */
    public String getCsSituationAssure() {
        return prononce.getCsSituationAssure();
    }

    /**
     * getter pour l'attribut dernier revenu ou manque AGagner
     * 
     * @return la valeur courante de l'attribut dernier revenu ou manque AGagner
     */
    public String getDernierRevenuOuManqueAGagner() {
        return prononce.getIdDernierRevenuOuManqueAGagner();
    }

    /**
     * getter pour l'attribut soumis cotisation AC
     * 
     * @return la valeur courante de l'attribut soumis cotisation AC
     */
    public Boolean getSoumisCotisationAC() {
        return prononce.getSoumisCotisationAC();
    }

    /**
     * getter pour l'attribut soumis cotisation AVSAIAPG
     * 
     * @return la valeur courante de l'attribut soumis cotisation AVSAIAPG
     */
    public Boolean getSoumisCotisationAVSAIAPG() {
        return prononce.getSoumisCotisationAVSAIAPG();
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return prononce.hasSpy();
    }

    /**
     * setter pour l'attribut cs situation assure
     * 
     * @param csSituationAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsSituationAssure(String csSituationAssure) {
        prononce.setCsSituationAssure(csSituationAssure);
    }

    /**
     * setter pour l'attribut dernier revenu ou manque AGagner
     * 
     * @param dernierRevenuOuManqueAGagner
     *            une nouvelle valeur pour cet attribut
     */
    public void setDernierRevenuOuManqueAGagner(String dernierRevenuOuManqueAGagner) {
        prononce.setIdDernierRevenuOuManqueAGagner(dernierRevenuOuManqueAGagner);
    }

    /**
     * setter pour l'attribut soumis cotisation AC
     * 
     * @param soumisCotisationAC
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisCotisationAC(Boolean soumisCotisationAC) {
        prononce.setSoumisCotisationAC(soumisCotisationAC);
    }

    /**
     * setter pour l'attribut soumis cotisation AVSAIAPG
     * 
     * @param soumisCotisationAVSAIAPG
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisCotisationAVSAIAPG(Boolean soumisCotisationAVSAIAPG) {
        prononce.setSoumisCotisationAVSAIAPG(soumisCotisationAVSAIAPG);
    }
}
