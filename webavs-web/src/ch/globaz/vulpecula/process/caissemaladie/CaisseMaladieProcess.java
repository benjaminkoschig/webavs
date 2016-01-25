package ch.globaz.vulpecula.process.caissemaladie;

import globaz.globall.db.GlobazJobQueue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.repositories.caissemaladie.AffiliationCaisseMaladieRepository;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public abstract class CaisseMaladieProcess extends BProcessWithContext {
    private static final long serialVersionUID = 3980771093093508731L;

    protected String idCaisseMaladie;
    protected Date dateAnnonce;
    protected List<AffiliationCaisseMaladie> affiliationsCaissesMaladies;
    protected Map<Administration, Collection<AffiliationCaisseMaladie>> affiliationsGroupByCaisseMaladie;
    protected Administration caisseMaladie;

    protected AffiliationCaisseMaladieRepository affiliationCaisseMaladieRepository = VulpeculaRepositoryLocator
            .getAffiliationCaisseMaladieRepository();

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        retrieve();
        print();
        return true;
    }

    protected abstract void retrieve();

    protected abstract void print() throws Exception;

    public String getIdCaisseMaladie() {
        return idCaisseMaladie;
    }

    public void setIdCaisseMaladie(String idCaisseMaladie) {
        this.idCaisseMaladie = idCaisseMaladie;
    }

    public Date getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(Date dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }
}
