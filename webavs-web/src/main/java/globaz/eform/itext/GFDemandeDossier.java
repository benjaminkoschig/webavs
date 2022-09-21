package globaz.eform.itext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;

public class GFDemandeDossier extends GFDocumentDossier {

    private static final long serialVersionUID = 1L;
    private final static String LABEL_TITRE = "DOCUMENT_DEMANDE_TITRE";
    private final static String LABEL_PARAGRAPHE = "DOCUMENT_DEMANDE_PARAGRAPHE";
    private final static String LABEL_SALUTATION = "DOCUMENT_DEMANDE_SALUTATION";

    public GFDemandeDossier() {
    }

    public GFDemandeDossier(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    public GFDemandeDossier(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    @Override
    public void createDataSource() throws Exception {

        // envoi des paramètres dans le template
        this.setParametres(PARAM_TITRE, getSession().getLabel(LABEL_TITRE));
        this.setParametres(PARAM_PARAGRAPHE, getSession().getLabel(LABEL_PARAGRAPHE));
        this.setParametres(PARAM_SALUTATION, getSession().getLabel(LABEL_SALUTATION));

        super.createDataSource();
    }
}
