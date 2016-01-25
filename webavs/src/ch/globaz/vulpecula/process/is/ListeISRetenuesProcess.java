package ch.globaz.vulpecula.process.is;

import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.is.TauxImpositionNotFoundException;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.util.ExceptionsUtil;

public class ListeISRetenuesProcess extends BProcessWithContext {
    private static final long serialVersionUID = 2139647038684081597L;

    private Annee annee;
    private String canton;
    private String caisseAF;
    private String idProcessusAF;

    private Map<String, Collection<PrestationGroupee>> prestationsAImprimer;
    private String libelleCaisseAF;

    private boolean wantRetrieve = true;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        try {
            if (wantRetrieve) {
                retrieve();
            }

            if (isOnError()) {
                return false;
            }

            if (prestationsAImprimer.size() == 0) {
                getTransaction().addErrors(getSession().getLabel("EMAIL_PAS_ELEMENTS"));
                return false;
            }
            print();
        } catch (Exception ex) {
            getMemoryLog().logMessage(ex.toString(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }
        return true;
    }

    private void retrieve() {
        try {
            prestationsAImprimer = VulpeculaServiceLocator.getImpotSourceService().getPrestationsForAllocIS(canton,
                    caisseAF, annee);
        } catch (TauxImpositionNotFoundException e) {
            getTransaction().addErrors(ExceptionsUtil.translateException(e));
        }
        if (!JadeStringUtil.isEmpty(caisseAF)) {
            libelleCaisseAF = VulpeculaRepositoryLocator.getAdministrationRepository().findById(caisseAF)
                    .getDesignation1();
        }
    }

    private void print() throws IOException {
        ListISRetenuesExcel excel = new ListISRetenuesExcel(getSession(),
                DocumentConstants.LISTES_AF_RETENUES_DOC_NAME, DocumentConstants.LISTES_AF_RETENUES_COMMISSION_NAME);
        excel.setPrestationsAImprimer(prestationsAImprimer);
        excel.setAnnee(annee);
        excel.setCanton(canton);
        excel.setCaisseAF(libelleCaisseAF);
        excel.setIdProcessusAF(idProcessusAF);
        excel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("IS_RETENUE_OBJECT");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getCaisseAF() {
        return caisseAF;
    }

    public void setCaisseAF(String caisseAF) {
        this.caisseAF = caisseAF;
    }

    public void setPrestationsAImprimer(Map<String, Collection<PrestationGroupee>> prestationsAImprimer) {
        this.prestationsAImprimer = prestationsAImprimer;
    }

    public Map<String, Collection<PrestationGroupee>> getPrestationsAImprimer() {
        return prestationsAImprimer;
    }

    public boolean getWantRetrieve() {
        return wantRetrieve;
    }

    public void setWantRetrieve(boolean wantRetrieve) {
        this.wantRetrieve = wantRetrieve;
    }

    public String getIdProcessusAF() {
        return idProcessusAF;
    }

    public void setIdProcessusAF(String idProcessusAF) {
        this.idProcessusAF = idProcessusAF;
    }
}
