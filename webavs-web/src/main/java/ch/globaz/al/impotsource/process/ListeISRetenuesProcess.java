package ch.globaz.al.impotsource.process;

import ch.globaz.al.business.services.ALRepositoryLocator;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.exception.TauxImpositionNotFoundException;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.util.ExceptionsUtil;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ListeISRetenuesProcess extends BProcessWithContext {
    private static final long serialVersionUID = 2139647038684081597L;

    private String dateDebut;
    private String dateFin;
    private String canton;
    private String caisseAF;
    private String idProcessusAF;

    private Map<String, List<PrestationGroupee>> prestationsAImprimer;
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
            prestationsAImprimer = ALServiceLocator.getImpotSourceService().getPrestationsForAllocIS(dateDebut, dateFin);
        } catch (TauxImpositionNotFoundException e) {
            getTransaction().addErrors(ExceptionsUtil.translateException(e));
        } catch (PropertiesException e) {
            getTransaction().addErrors(e.getMessage());
        }

        if (!JadeStringUtil.isEmpty(caisseAF)) {
            libelleCaisseAF = ALRepositoryLocator.getAdministrationRepository().findById(caisseAF)
                    .getDesignation1();
        }
    }

    private void print() throws IOException {
        if (StringUtils.isNotEmpty(canton)) {
            List<PrestationGroupee> prestationsGroupees = prestationsAImprimer.get(canton);
            createExcel(prestationsGroupees, canton);
        } else {
            for (Map.Entry<String, List<PrestationGroupee>> eachEntry : prestationsAImprimer.entrySet()) {
                createExcel(eachEntry.getValue(), eachEntry.getKey());
            }
        }

    }

    private void createExcel(List<PrestationGroupee> prestationsGroupees, String canton) throws IOException {
        ListISRetenuesExcel excel = new ListISRetenuesExcel(getSession(),
                DocumentConstants.LISTES_AF_RETENUES_DOC_NAME, DocumentConstants.LISTES_AF_RETENUES_COMMISSION_NAME);
        excel.setPrestationsAImprimer(prestationsGroupees);
        excel.setCanton(canton);
        excel.setDateDebut(dateDebut);
        excel.setDateFin(dateFin);
        excel.setIdProcessusAF(idProcessusAF);
        excel.setLangueUser(getSession().getIdLangueISO());
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

    public void setPrestationsAImprimer(Map<String, List<PrestationGroupee>> prestationsAImprimer) {
        this.prestationsAImprimer = prestationsAImprimer;
    }

    public Map<String, List<PrestationGroupee>> getPrestationsAImprimer() {
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
