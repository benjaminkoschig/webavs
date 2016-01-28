/*
 * Créé le 12 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJGrandeIJ;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGrandeIJPViewBean extends IJAbstractPrononceProxyViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    IJGrandeIJ prononce;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGrandeIJPViewBean.
     */
    public IJGrandeIJPViewBean() {
        super(new IJGrandeIJ());
        prononce = (IJGrandeIJ) getPrononce();

        // valeurs par défaut
        prononce.setCsEtat(IIJPrononce.CS_ATTENTE);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut indemnite exploitation
     * 
     * @return la valeur courante de l'attribut indemnite exploitation
     */
    public Boolean getIndemniteExploitation() {
        return prononce.getIndemniteExploitation();
    }

    /**
     * getter pour l'attribut montant indemnite assistance
     * 
     * @return la valeur courante de l'attribut montant indemnite assistance
     */
    public String getMontantIndemniteAssistance() {
        return prononce.getMontantIndemniteAssistance();
    }

    /**
     * getter pour l'attribut pourcent degre incapacite travail
     * 
     * @return la valeur courante de l'attribut pourcent degre incapacite travail
     */
    public String getPourcentDegreIncapaciteTravail() {
        return prononce.getPourcentDegreIncapaciteTravail();
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return prononce.hasSpy();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean is4emeRevision() throws Exception {
        return prononce.is4emeRevision();
    }

    /**
     * setter pour l'attribut indemnite exploitation
     * 
     * @param indemniteExploitation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIndemniteExploitation(Boolean indemniteExploitation) {
        prononce.setIndemniteExploitation(indemniteExploitation);
    }

    /**
     * setter pour l'attribut montant indemnite assistance
     * 
     * @param montantIndemniteAssistance
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteAssistance(String montantIndemniteAssistance) {
        prononce.setMontantIndemniteAssistance(montantIndemniteAssistance);
    }

    /**
     * setter pour l'attribut pourcent degre incapacite travail
     * 
     * @param pourcentDegreIncapaciteTravail
     *            une nouvelle valeur pour cet attribut
     */
    public void setPourcentDegreIncapaciteTravail(String pourcentDegreIncapaciteTravail) {
        prononce.setPourcentDegreIncapaciteTravail(pourcentDegreIncapaciteTravail);
    }
}
