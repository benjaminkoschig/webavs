package globaz.pavo.print.itext;

import globaz.naos.itext.AFDocumentItextService;

/**
 * @author MMO
 * @since 14.09.2011
 */
public class CISplittingLettreAccompagnement_Doc extends CISplittingDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOCUMENT_TITLE = "Lettre d'accompagnement des données du splitting";
    public static final String ID_DOCUMENT = "126000000003";
    public static final String MODEL_NAME = "PAVO_SPLITTING_LETTRE_ACCOMPAGNEMENT";
    public static final String NUMERO_INFOROM = "0278CCI";

    @Override
    public void initDoc() throws Exception {
        // Ce document est piloté par un processus parent (CISplittingApercuAndLettreAccompagnementMergeProcess) qui se
        // charge de l'envoi du mail
        setSendCompletionMail(false);
        setDocumentTitle(CISplittingLettreAccompagnement_Doc.DOCUMENT_TITLE);
        setTemplateFile(CISplittingLettreAccompagnement_Doc.MODEL_NAME);
        setCatalogue(AFDocumentItextService.retrieveCatalogue(getSession(), getLangueISO(),
                CISplittingDocumentManager.DOMAINE_DOCUMENT, CISplittingDocumentManager.TYPE_DOCUMENT,
                CISplittingLettreAccompagnement_Doc.ID_DOCUMENT, CISplittingLettreAccompagnement_Doc.ID_DOCUMENT));
        fillDocInfo(CISplittingLettreAccompagnement_Doc.NUMERO_INFOROM);

        String annexe = "";
        try {
            annexe = AFDocumentItextService.getTexte(getCatalogue(), 2, null);
        } catch (Exception e) {
            annexe = "";
        }
        this.setParametres("P_ANNEXE", annexe);
    }

}
