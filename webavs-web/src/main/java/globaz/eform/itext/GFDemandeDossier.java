package globaz.eform.itext;

public class GFDemandeDossier extends GFDocumentDossier {

    private static final long serialVersionUID = 1L;
    private final static String LABEL_TITRE = "DOCUMENT_DEMANDE_TITRE";
    private final static String LABEL_PARAGRAPHE = "DOCUMENT_DEMANDE_PARAGRAPHE";
    private final static String LABEL_SALUTATION = "DOCUMENT_DEMANDE_SALUTATION";


    @Override
    public void createDataSource() throws Exception {

        // envoi des paramètres dans le template
        this.setParametres(PARAM_TITRE, getSession().getLabel(LABEL_TITRE));
        this.setParametres(PARAM_PARAGRAPHE, getSession().getLabel(LABEL_PARAGRAPHE));
        this.setParametres(PARAM_SALUTATION, getSession().getLabel(LABEL_SALUTATION));

        super.createDataSource();
    }
}
