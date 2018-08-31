package ch.globaz.vulpecula.process.syndicats;

import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;

public class ListeTravailleursCaisseMetierProcess extends ListeSyndicatsProcess {
    private static final long serialVersionUID = 7592021780339067297L;
    protected Map<Administration, List<AffiliationSyndicat>> affiliationsGroupByCaisseMetier;

    @Override
    protected void print() throws Exception {
        ListeTravailleursCaisseMetierExcel listeTravailleursSyndicatExcel = new ListeTravailleursCaisseMetierExcel(
                getSession(), DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT_DOC_NAME,
                DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT_NAME);
        listeTravailleursSyndicatExcel.setAffiliationsSyndicats(affiliationsGroupByCaisseMetier);
        listeTravailleursSyndicatExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listeTravailleursSyndicatExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT_NAME;
    }

    @Override
    protected void retrieve() {
        affiliationsGroupByCaisseMetier = affiliationSyndicatService.findByAnneeWithCumulSalairesGroupByCaisseMetier(
                idSyndicat, idCaisseMetier, getAnnee(), listeErreur, idTravailleur);
    }

}
