package globaz.pavo.print.itext;

import globaz.naos.itext.AFDocumentItextService;

/**
 * @author MMO
 * @since 14.09.2011
 */
public class CISplittingAccuseReception_Doc extends CISplittingDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOCUMENT_TITLE = "Accusé de réception";
    public static final String ID_DOCUMENT = "126000000001";
    public static final String MODEL_NAME = "PAVO_SPLITTING_ACCUSE_RECEPTION";
    public static final String NUMERO_INFOROM = "0276CCI";

    @Override
    protected String getEMailObject() {

        if (!isAborted() && !isOnError() && !getSession().hasErrors()) {
            return getSession().getLabel("ACCUSE_RECEPTION_EMAIL_SUBJECT_SUCCES");
        }

        return getSession().getLabel("ACCUSE_RECEPTION_EMAIL_SUBJECT_ERROR");
    }

    @Override
    public void initDoc() throws Exception {
        setSendCompletionMail(true);
        setDocumentTitle(CISplittingAccuseReception_Doc.DOCUMENT_TITLE);
        setTemplateFile(CISplittingAccuseReception_Doc.MODEL_NAME);
        setCatalogue(AFDocumentItextService.retrieveCatalogue(getSession(), getLangueISO(),
                CISplittingDocumentManager.DOMAINE_DOCUMENT, CISplittingDocumentManager.TYPE_DOCUMENT,
                CISplittingAccuseReception_Doc.ID_DOCUMENT, CISplittingAccuseReception_Doc.ID_DOCUMENT));
        fillDocInfo(CISplittingAccuseReception_Doc.NUMERO_INFOROM);
    }

}
