package globaz.corvus.vb.decisions;

/**
 * ViewBean pour l'affichage de l'écran preparerDecisionsAvecAjournement_de.jsp
 */
public class REPreparerDecisionAvecAjournementViewBean extends REPreparerDecisionSpecifiqueViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateSurLeDocument;
    private boolean documentAvecConfigurationPourLaGed;
    private boolean editionDuDocument;
    private boolean miseEnGed;

    public REPreparerDecisionAvecAjournementViewBean() {
        this(null, null);
    }

    public REPreparerDecisionAvecAjournementViewBean(Long idDemandeRente, String detailRequerant) {
        super(idDemandeRente, REPreparerDecisionViewBean.PAGE_PREPARATION_DECISION_AVEC_AJOURNEMENT, detailRequerant);

        dateSurLeDocument = null;
        documentAvecConfigurationPourLaGed = false;
        editionDuDocument = false;
        miseEnGed = false;
    }

    public String getDateSurLeDocument() {
        return dateSurLeDocument;
    }

    public boolean isDocumentAvecConfigurationPourLaGed() {
        return documentAvecConfigurationPourLaGed;
    }

    public boolean isEditionDuDocument() {
        return editionDuDocument;
    }

    public boolean isMiseEnGed() {
        return miseEnGed;
    }

    public void setDateSurLeDocument(String dateSurLeDocument) {
        this.dateSurLeDocument = dateSurLeDocument;
    }

    public void setDocumentAvecConfigurationPourLaGed(boolean documentAvecConfigurationPourLaGed) {
        this.documentAvecConfigurationPourLaGed = documentAvecConfigurationPourLaGed;
    }

    public void setEditionDuDocument(boolean editionDuDocument) {
        this.editionDuDocument = editionDuDocument;
    }

    public void setEditionDuDocument(String editionDuDocument) {
        this.editionDuDocument = Boolean.parseBoolean(editionDuDocument);
    }

    public void setMiseEnGed(boolean miseEnGed) {
        this.miseEnGed = miseEnGed;
    }
}
