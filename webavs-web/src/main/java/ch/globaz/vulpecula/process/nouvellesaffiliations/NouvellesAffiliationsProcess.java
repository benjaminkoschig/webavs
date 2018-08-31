package ch.globaz.vulpecula.process.nouvellesaffiliations;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class NouvellesAffiliationsProcess extends BProcessWithContext {
    private static final long serialVersionUID = -5703110255156351983L;

    private Date dateDebut;
    private Date dateFin;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        process();
        return true;
    }

    private void process() throws IOException {
        NouvellesAffiliationsExcel excel = new NouvellesAffiliationsExcel(getSession(),
                DocumentConstants.LISTES_NOUVELLES_AFFILIATIONS_NAME,
                DocumentConstants.LISTES_NOUVELLES_AFFILIATIONS_DOC_NAME);
        excel.setEmployeurs(retrieve());
        excel.setDateDebut(dateDebut);
        excel.setDateFin(dateFin);
        excel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excel.getOutputFile());
    }

    private List<Employeur> retrieve() {
        List<Employeur> employeurs = VulpeculaRepositoryLocator.getEmployeurRepository().findByPeriode(dateDebut,
                dateFin);

        List<Employeur> employeursAVS = new ArrayList<Employeur>();
        for (Employeur employeur : employeurs) {
            List<Cotisation> cotisations = VulpeculaServiceLocator.getCotisationService().findByIdAffilie(
                    employeur.getId());
            employeur.setCotisations(cotisations);

            if (employeur.isSoumisAVS()) {
                employeursAVS.add(employeur);
            }
        }
        return employeursAVS;
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_NOUVELLES_AFFILIATIONS_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
}
