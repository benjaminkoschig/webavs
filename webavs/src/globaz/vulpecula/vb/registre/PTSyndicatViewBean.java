package globaz.vulpecula.vb.registre;

import globaz.globall.db.BSpy;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class PTSyndicatViewBean extends BJadeSearchObjectELViewBean {

    private String idSyndicat;
    private List<Administration> caissesMetiers;

    public PTSyndicatViewBean() {
        super();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        caissesMetiers = VulpeculaRepositoryLocator.getAdministrationRepository().findAllCaissesMetiers();
    }

    @Override
    public void setId(String newId) {
    }

    public String getIdSyndicat() {
        return idSyndicat;
    }

    public void setIdSyndicat(String idSyndicat) {
        this.idSyndicat = idSyndicat;
    }

    public List<Administration> getCaissesMetiers() {
        return caissesMetiers;
    }
}
