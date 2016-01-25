package ch.globaz.vulpecula.process.syndicats;

import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import ch.globaz.vulpecula.documents.DocumentConstants;

public class ListeTravailleursSyndicatProcess extends ListeSyndicatsProcess {
    private static final long serialVersionUID = 7592021780339067297L;

    @Override
    protected void print() throws Exception {
        ListeTravailleursSyndicatExcel listeTravailleursSyndicatExcel = new ListeTravailleursSyndicatExcel(
                getSession(), DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT_DOC_NAME,
                DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT_NAME);
        listeTravailleursSyndicatExcel.setAffiliationsSyndicats(affiliationsGroupBySyndicat);
        listeTravailleursSyndicatExcel.setAnnee(annee);
        listeTravailleursSyndicatExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listeTravailleursSyndicatExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT_NAME;
    }

}
