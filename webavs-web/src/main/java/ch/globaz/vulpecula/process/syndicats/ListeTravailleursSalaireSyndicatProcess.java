package ch.globaz.vulpecula.process.syndicats;

import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import ch.globaz.vulpecula.documents.DocumentConstants;

public class ListeTravailleursSalaireSyndicatProcess extends ListeSyndicatsProcess {
    private static final long serialVersionUID = 7592021780339067297L;

    @Override
    protected void print() throws Exception {
        ListeTravailleursSalaireSyndicatExcel listeTravailleursSalaireSyndicatExcel = new ListeTravailleursSalaireSyndicatExcel(
                getSession(), DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SALAIRE_SYNDICAT_DOC_NAME,
                DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SALAIRE_SYNDICAT_NAME);
        listeTravailleursSalaireSyndicatExcel.setAffiliationsSyndicats(affiliationsGroupBySyndicat);
        listeTravailleursSalaireSyndicatExcel.setAnnee(annee);
        listeTravailleursSalaireSyndicatExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listeTravailleursSalaireSyndicatExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SALAIRE_SYNDICAT_NAME;
    }

}
