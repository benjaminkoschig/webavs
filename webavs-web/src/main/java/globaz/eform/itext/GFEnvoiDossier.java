package globaz.eform.itext;

public class GFEnvoiDossier extends GFDocumentDossier {

    private static final long serialVersionUID = 1L;
    private final static String LABEL_TITRE = "DOCUMENT_ENVOI_TITRE";
    private final static String LABEL_PARAGRAPHE = "DOCUMENT_ENVOI_PARAGRAPHE";
    private final static String LABEL_SALUTATION = "DOCUMENT_ENVOI_SALUTATION";

    public static String FILENAME = "lead_transfert.pdf";


    @Override
    public void createDataSource() throws Exception {
        // set du template
        setTemplateFile(GFEnvoiDossier.MODEL_NAME);

        // envoi des paramètres dans le template
        this.setParametres(PARAM_TITRE, getSession().getLabel(LABEL_TITRE));
        this.setParametres(PARAM_PARAGRAPHE, getSession().getLabel(LABEL_PARAGRAPHE));
        this.setParametres(PARAM_SALUTATION, getSession().getLabel(LABEL_SALUTATION));

        super.createDataSource();
    }
}
