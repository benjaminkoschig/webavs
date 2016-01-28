package globaz.perseus.vb.dossier;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFGestionnaireHelper;
import globaz.perseus.utils.PFUserHelper;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFDossierViewBean extends BJadePersistentObjectViewBean {
    private Dossier dossier = null;

    public PFDossierViewBean() {
        super();
        dossier = new Dossier();
    }

    public PFDossierViewBean(Dossier dossier) {
        super();
        this.dossier = dossier;
    }

    @Override
    public void add() throws Exception {
        PerseusServiceLocator.getDossierService().create(dossier);

        getISession().setAttribute("likeNss",
                dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getDossierService().delete(dossier);
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailAssure() throws Exception {

        return PFUserHelper.getDetailAssure(getSession(), dossier.getDemandePrestation().getPersonneEtendue());

    }

    public String getDetailGestionnaire() throws FWSecurityLoginException, Exception {

        return PFGestionnaireHelper.getDetailGestionnaire(getSession(), dossier.getDossier().getGestionnaire());
    }

    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return dossier.getId();
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(dossier.getSpy());
    }

    public boolean hasDemande() throws DossierException, DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (!JadeStringUtil.isEmpty(dossier.getId())) {
            DemandeSearchModel search = new DemandeSearchModel();
            search.setForIdDossier(dossier.getId());
            return PerseusServiceLocator.getDemandeService().count(search) > 0;

        }
        return false;

    }

    @Override
    public void retrieve() throws Exception {
        dossier = PerseusServiceLocator.getDossierService().read(dossier.getId());

        getISession().setAttribute("likeNss",
                dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
    }

    @Override
    public void setId(String newId) {
        dossier.setId(newId);
    }

    @Override
    public void update() throws Exception {
        PerseusServiceLocator.getDossierService().update(dossier);
    }

}
