package globaz.vulpecula.vb.postetravail;

import globaz.vulpecula.vb.PTAjaxDisplayViewBean;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.prestations.Prestation;

public class PTPrestationsdetailAjaxViewBean extends PTAjaxDisplayViewBean {
    private static final long serialVersionUID = -7922582733158531866L;

    private final List<Prestation> prestations = new ArrayList<Prestation>();

    private String idPassage;
    private String idEmployeur;

    @Override
    public void retrieve() throws Exception {
        prestations.addAll(VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().findByIdPassage(idPassage,
                idEmployeur));
        prestations.addAll(VulpeculaRepositoryLocator.getCongePayeRepository().findByIdPassage(idPassage, idEmployeur));
        prestations.addAll(VulpeculaRepositoryLocator.getServiceMilitaireRepository().findByIdPassage(idPassage,
                idEmployeur));
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public List<Prestation> getPrestations() {
        return prestations;
    }
}
