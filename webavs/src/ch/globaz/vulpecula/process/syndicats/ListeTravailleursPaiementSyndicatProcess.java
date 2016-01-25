package ch.globaz.vulpecula.process.syndicats;

import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.Map;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class ListeTravailleursPaiementSyndicatProcess extends ListeSyndicatsProcess {
    private static final long serialVersionUID = 7592021780339067297L;

    private Map<Pair<Administration, Administration>, ParametreSyndicat> parametresSyndicats;

    @Override
    protected void retrieve() {
        super.retrieve();
        parametresSyndicats = VulpeculaServiceLocator.getParametreSyndicatService().findParametresSyndicats(annee);
    }

    @Override
    protected void print() throws Exception {
        ListeTravailleursPaiementSyndicatExcel listeTravailleursSalaireSyndicatExcel = new ListeTravailleursPaiementSyndicatExcel(
                getSession(), DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_PAIEMENT_SYNDICAT_DOC_NAME,
                DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_PAIEMENT_SYNDICAT_NAME);
        listeTravailleursSalaireSyndicatExcel.setAffiliationsSyndicats(affiliationsGroupBySyndicat);
        listeTravailleursSalaireSyndicatExcel.setParametresSyndicats(parametresSyndicats);
        listeTravailleursSalaireSyndicatExcel.setAnnee(annee);
        listeTravailleursSalaireSyndicatExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listeTravailleursSalaireSyndicatExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_PAIEMENT_SYNDICAT_NAME;
    }

}
