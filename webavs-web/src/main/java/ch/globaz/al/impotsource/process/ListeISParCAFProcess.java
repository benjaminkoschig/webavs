package ch.globaz.al.impotsource.process;

import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeISParCAFProcess extends BProcessWithContext {
    private static final long serialVersionUID = 931442789627393396L;

    private static final String LISTES_AF_RETENUES_PAR_CAF_NAME = "Impots retenus par CAF";
    private static final String LISTES_AF_RETENUES_PAR_CAF_DOC_NAME = "Impots_retenus_par_CAF";

    private Annee annee;
    private String canton;

    private Map<String, PrestationGroupee> prestationsAImprimer;
    private Map<String, BigDecimal> listeComptaAux;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        try {
            retrieve();
            print();
        } catch (Exception ex) {
            getMemoryLog().logMessage(ex.toString(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }
        return true;
    }

    private void retrieve() throws Exception {
        prestationsAImprimer = ALServiceLocator.getImpotSourceService().getPrestationsForAllocISGroupByCaisseAF(
                annee, canton);
        List<String> caisses = new ArrayList<>();
        for(PrestationGroupee prestation: prestationsAImprimer.values()) {
            caisses.add(prestation.getCodeCaisseAF());
        }
        listeComptaAux = ALServiceLocator.getImpotSourceService().getMontantISCaisseAFComptaAux(caisses, annee);
    }

    private void print() throws Exception {
        ListISParCAFExcel listISParCAFExcel = new ListISParCAFExcel(getSession(),LISTES_AF_RETENUES_PAR_CAF_DOC_NAME,LISTES_AF_RETENUES_PAR_CAF_NAME);
        listISParCAFExcel.setPrestationsAImprimer(prestationsAImprimer);
        listISParCAFExcel.setListeComptaAux(listeComptaAux);
        listISParCAFExcel.setAnnee(annee);
        listISParCAFExcel.setCanton(canton);
        listISParCAFExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listISParCAFExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("IS_CAF_OBJECT");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public Annee getAnnee() {
        return annee;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

}
