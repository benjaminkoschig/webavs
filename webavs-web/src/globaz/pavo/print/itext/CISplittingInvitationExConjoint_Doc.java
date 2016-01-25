package globaz.pavo.print.itext;

import globaz.naos.itext.AFDocumentItextService;

/**
 * @author MMO
 * @since 14.09.2011
 */
public class CISplittingInvitationExConjoint_Doc extends CISplittingDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOCUMENT_TITLE = "Invitation à participer au splitting";
    public static final String ID_DOCUMENT = "126000000002";
    public static final String MODEL_NAME = "PAVO_SPLITTING_INVITATION";
    public static final String NUMERO_INFOROM = "0277CCI";
    private String infoDemandeur = "";

    @Override
    protected String getEMailObject() {

        if (!isAborted() && !isOnError() && !getSession().hasErrors()) {
            return getSession().getLabel("INVITATION_EMAIL_SUBJECT_SUCCES");
        }

        return getSession().getLabel("INVITATION_EMAIL_SUBJECT_ERROR");

    }

    public String getInfoDemandeur() {
        return infoDemandeur;
    }

    @Override
    public String[] getParametersCorps2() {
        return new String[] { getFormulePolitesse(), getInfoDemandeur() };
    }

    @Override
    public void initDoc() throws Exception {

        setSendCompletionMail(true);
        setDocumentTitle(CISplittingInvitationExConjoint_Doc.DOCUMENT_TITLE);
        setTemplateFile(CISplittingInvitationExConjoint_Doc.MODEL_NAME);
        setCatalogue(AFDocumentItextService.retrieveCatalogue(getSession(), getLangueISO(),
                CISplittingDocumentManager.DOMAINE_DOCUMENT, CISplittingDocumentManager.TYPE_DOCUMENT,
                CISplittingInvitationExConjoint_Doc.ID_DOCUMENT, CISplittingInvitationExConjoint_Doc.ID_DOCUMENT));
        fillDocInfo(CISplittingInvitationExConjoint_Doc.NUMERO_INFOROM);

        String annexe = "";
        try {
            annexe = AFDocumentItextService.getTexte(getCatalogue(), 2, null);
        } catch (Exception e) {
            annexe = "";
        }
        this.setParametres("P_ANNEXE", annexe);

    }

    public void setInfoDemandeur(String infoDemandeur) {
        this.infoDemandeur = infoDemandeur;
    }

}
