package globaz.corvus.vb.decisions;

/**
 * Base pour les viewBeans de pr�paration sp�cifique de d�cision. Contient les donn�es pour l'�cran
 * communsAuxDeuxPreparationsDeDecisionsSpecfiques.jsp
 */
public abstract class REPreparerDecisionSpecifiqueViewBean extends REPreparerDecisionViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseEmailGestionnaire;

    public REPreparerDecisionSpecifiqueViewBean() {
        this(null, null, null);
    }

    public REPreparerDecisionSpecifiqueViewBean(Long idDemandeRente, String destination, String detailRequerant) {
        super(idDemandeRente, destination, detailRequerant);

        adresseEmailGestionnaire = null;
    }

    public String getAdresseEmailGestionnaire() {
        return adresseEmailGestionnaire;
    }

    public void setAdresseEmailGestionnaire(String adresseEmailGestionnaire) {
        this.adresseEmailGestionnaire = adresseEmailGestionnaire;
    }
}
