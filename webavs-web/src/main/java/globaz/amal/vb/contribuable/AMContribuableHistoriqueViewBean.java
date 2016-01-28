/**
 * 
 */
package globaz.amal.vb.contribuable;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListe;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMContribuableHistoriqueViewBean extends BJadePersistentObjectViewBean {
    private ContribuableHistoriqueRCListe contribuableHistoriqueRCListe = null;
    private SimpleContribuableInfos simpleContribuableInfos = null;

    public AMContribuableHistoriqueViewBean() {
        super();
        contribuableHistoriqueRCListe = new ContribuableHistoriqueRCListe();
        simpleContribuableInfos = new SimpleContribuableInfos();
    }

    public AMContribuableHistoriqueViewBean(ContribuableHistoriqueRCListe contribuableHistoriqueRCListe) {
        super();
        this.contribuableHistoriqueRCListe = contribuableHistoriqueRCListe;
        simpleContribuableInfos = new SimpleContribuableInfos();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
        AmalServiceLocator.getContribuableService().deleteInfo(simpleContribuableInfos);
    }

    public ContribuableHistoriqueRCListe getContribuableHistoriqueRCListe() {
        return contribuableHistoriqueRCListe;
    }

    @Override
    public String getId() {
        return getSimpleContribuableInfos().getIdContribuableInfo();
    }

    public SimpleContribuableInfos getSimpleContribuableInfos() {
        return simpleContribuableInfos;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        contribuableHistoriqueRCListe.setContribuableInfos(AmalServiceLocator.getContribuableService().readInfos(
                getId()));

    }

    public void setContribuableHistoriqueRCListe(ContribuableHistoriqueRCListe contribuableHistoriqueRCListe) {
        this.contribuableHistoriqueRCListe = contribuableHistoriqueRCListe;
    }

    @Override
    public void setId(String newId) {
        simpleContribuableInfos.setIdContribuableInfo(newId);

    }

    public void setSimpleContribuableInfos(SimpleContribuableInfos simpleContribuableInfos) {
        this.simpleContribuableInfos = simpleContribuableInfos;
    }

    @Override
    public void update() throws Exception {
    }

}
