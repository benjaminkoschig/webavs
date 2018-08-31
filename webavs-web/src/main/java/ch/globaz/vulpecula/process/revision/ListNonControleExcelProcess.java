package ch.globaz.vulpecula.process.revision;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.exception.JadePersistenceException;
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

public class ListNonControleExcelProcess extends BProcessWithContext {
    private static final long serialVersionUID = -5703110255156351983L;

    private Date dateDebut;
    private Date dateFin;
    private String nombreAnnee;
    private String uniquementAVS = "";

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        process();
        return true;
    }

    private void process() throws IOException, JadePersistenceException {
        ListNonControleExcel excel = new ListNonControleExcel(getSession(),
                DocumentConstants.LISTES_NOUVELLES_AFFILIATIONS_NAME,
                DocumentConstants.LISTES_NOUVELLES_AFFILIATIONS_DOC_NAME, this);
        excel.setEmployeurs(retrieve());
        setProgressScaleValue(excel.getEmployeurs().size());
        excel.setDateDebut(dateDebut);
        excel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excel.getOutputFile());
    }

    private List<Employeur> retrieve() throws JadePersistenceException {
        int nbreAnnee = Integer.parseInt(getNombreAnnee());
        Date dateControleAu = dateDebut.addDays(-1).addYear(-nbreAnnee);
        List<Employeur> employeurs = new ArrayList<Employeur>();
        if (nbreAnnee != 0) {
            employeurs = VulpeculaRepositoryLocator.getControleEmployeurRepository()
                    .findEmployeurAControlerParRapportADateControleAu(dateControleAu);
        } else {
            employeurs = VulpeculaRepositoryLocator.getControleEmployeurRepository().findEmployeurAControlerTous(
                    dateControleAu);
        }

        if (getUniquementAVS().equals("true")) {
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

        } else {
            return employeurs;
        }
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_NON_CONTROLE_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public String getUniquementAVS() {
        return uniquementAVS;
    }

    public void setUniquementAVS(String uniquementAVS) {
        this.uniquementAVS = uniquementAVS;
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

    public String getNombreAnnee() {
        return nombreAnnee;
    }

    public void setNombreAnnee(String nombreAnnee) {
        this.nombreAnnee = nombreAnnee;
    }
}
