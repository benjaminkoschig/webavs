package globaz.ij.vb.prononces;

import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJFpi;
import globaz.ij.db.prononces.IJPetiteIJ;

/**
 * <H1>Description</H1>
 * 
 * @author ebko
 */
public class IJFpiViewBean extends IJAbstractPrononceProxyViewBean {

    private IJFpi prononce = null;
    private String dateNaissance = "";

    /**
     * Crée une nouvelle instance de la classe IJFpiViewBean.
     */
    public IJFpiViewBean() {
        super(new IJFpi());
        prononce = (IJFpi) getPrononce();
        prononce.setCsEtat(IIJPrononce.CS_ATTENTE);
    }

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
//    public Boolean getSoumisCotisationAC() {
//        return prononce.getSoumisCotisationAC();
//    }

    /**
     * getter pour l'attribut soumis cotisation AVSAIAPG
     * 
     * @return la valeur courante de l'attribut soumis cotisation AVSAIAPG
     */
//    public Boolean getSoumisCotisationAVSAIAPG() {
//        return prononce.getSoumisCotisationAVSAIAPG();
//    }

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

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * setter pour l'attribut soumis cotisation AC
     * 
     * @param soumisCotisationAC
     *            une nouvelle valeur pour cet attribut
     */
//    public void setSoumisCotisationAC(Boolean soumisCotisationAC) {
//        prononce.setSoumisCotisationAC(soumisCotisationAC);
//    }

    /**
     * setter pour l'attribut soumis cotisation AVSAIAPG
     * 
     * @param soumisCotisationAVSAIAPG
     *            une nouvelle valeur pour cet attribut
     */
//    public void setSoumisCotisationAVSAIAPG(Boolean soumisCotisationAVSAIAPG) {
//        prononce.setSoumisCotisationAVSAIAPG(soumisCotisationAVSAIAPG);
//    }
}
