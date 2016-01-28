package ch.globaz.vulpecula.process.prestations;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.BProcessWithContext;

public abstract class AbstractPrestationExcelProcess extends BProcessWithContext {
    private static final long serialVersionUID = 991928528663185784L;

    protected String idPassageFacturation;
    protected String idEmployeur;
    protected String idTravailleur;
    protected String idConvention;
    protected String periodeDebut;
    protected String periodeFin;

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public Employeur getEmployeur() {
        if (JadeStringUtil.isEmpty(idEmployeur)) {
            return null;
        }
        return VulpeculaRepositoryLocator.getEmployeurRepository().findById(idEmployeur);
    }

    public Travailleur getTravailleur() {
        if (JadeStringUtil.isEmpty(idTravailleur)) {
            return null;
        }
        return VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur);
    }

    public Convention getConvention() {
        if (JadeStringUtil.isEmpty(idConvention)) {
            return null;
        }
        return VulpeculaRepositoryLocator.getConventionRepository().findById(idConvention);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    protected void _executeCleanUp() {
        super._executeCleanUp();
    }
}
