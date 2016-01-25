package globaz.vulpecula.vb.registre;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTParametresyndicatAjaxViewBean extends JadeAbstractAjaxFindForDomain<ParametreSyndicat> {
    private static final long serialVersionUID = -6347369944000337411L;

    private String idSyndicat;
    private String idCaisseMetier;

    public List<ParametreSyndicat> getParametresSyndicats() {
        return getList();
    }

    @Override
    public ParametreSyndicat getEntity() {
        return new ParametreSyndicat();
    }

    @Override
    public Repository<ParametreSyndicat> getRepository() {
        return VulpeculaRepositoryLocator.getParametreSyndicatRepository();
    }

    @Override
    public List<ParametreSyndicat> findByRepository() {
        return VulpeculaRepositoryLocator.getParametreSyndicatRepository().findByIdSyndicat(idSyndicat, idCaisseMetier);
    }

    public void setIdSyndicat(String idSyndicat) {
        this.idSyndicat = idSyndicat;
    }

    public void setIdCaisseMetier(String idCaisseMetier) {
        this.idCaisseMetier = idCaisseMetier;
    }
}
