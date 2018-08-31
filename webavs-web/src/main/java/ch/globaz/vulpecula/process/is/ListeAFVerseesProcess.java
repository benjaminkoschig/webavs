package ch.globaz.vulpecula.process.is;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.vulpecula.business.exception.VulpeculaException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.is.ImpotSourceService;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.is.DetailPrestationAF;
import ch.globaz.vulpecula.external.BProcessWithContext;

/**
 * 
 * @author jwe
 * 
 *         Processus permettant de lancer la consolidation de la liste des AF versées.
 * 
 */
public class ListeAFVerseesProcess extends BProcessWithContext {

    private static final long serialVersionUID = 1L;
    private String dateDebut;
    private String dateFin;
    private Map<String, Collection<DetailPrestationAF>> prestationsAImprimer = new HashMap<String, Collection<DetailPrestationAF>>();

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("JSP_LISTE_AF_VERSEES");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        retrieve();
        print();
        return true;
    }

    private void retrieve() {
        // 1. Récupérer tous les cas impôts à la source (paiement direct)
        ImpotSourceService impotSourceService = VulpeculaServiceLocator.getImpotSourceService();
        try {
            prestationsAImprimer = impotSourceService.getPrestationsForListAFVersees(getDateDebut(), getDateFin());
        } catch (VulpeculaException e) {
            JadeLogger.error(this, e.toString());
            // Envoi du message d'erreur par Mail en fin de processus
            String errorMsg = getSession()
                    .getLabel("An error happened while trying to load the list of prestations : ") + e.toString();
            getTransaction().addErrors(errorMsg);
        }

    }

    private void print() throws Exception {
        ListAFVerseesExcel listAFVerseesExcel = new ListAFVerseesExcel(getSession(),
                DocumentConstants.LISTES_AF_VERSEES_NAME_DOC_NAME, DocumentConstants.LISTES_AF_VERSEES_TYPE_NUMBER);
        listAFVerseesExcel.setPrestationsAImprimer(prestationsAImprimer);
        listAFVerseesExcel.setDateDebut(new Date(getDateDebut()));
        listAFVerseesExcel.setDateFin(new Date(getDateFin()));
        listAFVerseesExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listAFVerseesExcel.getOutputFile());
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

}
