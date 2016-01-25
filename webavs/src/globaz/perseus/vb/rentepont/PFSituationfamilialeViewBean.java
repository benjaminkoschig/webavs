package globaz.perseus.vb.rentepont;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PFSituationfamilialeViewBean extends BJadePersistentObjectViewBean {

    private PFConjointAjaxViewBean ajaxConjointViewBean = null;
    private String idRentePont = null;
    private String idTiersConjoint = null;
    private List listeEnfantFamille = new ArrayList();
    private RentePont rentePont = null;
    private SituationFamiliale situationFamiliale = null;

    public PFSituationfamilialeViewBean() {
        super();
        situationFamiliale = new SituationFamiliale();
        setAjaxConjointViewBean(new PFConjointAjaxViewBean());
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public PFConjointAjaxViewBean getAjaxConjointViewBean() {
        return ajaxConjointViewBean;
    }

    @Override
    public String getId() {
        return situationFamiliale.getId();
    }

    /**
     * @return the idRentePont
     */
    public String getIdRentePont() {
        return idRentePont;
    }

    /**
     * @return the idTiersConjoint
     */
    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    /**
     * @return the listeEnfantFamille
     */
    public List getListeEnfantFamille() {
        return listeEnfantFamille;
    }

    /**
     * @return the rentePont
     */
    public RentePont getRentePont() {
        return rentePont;
    }

    /**
     * @return the situationFamiliale
     */
    public SituationFamiliale getSituationFamiliale() {
        return situationFamiliale;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(situationFamiliale.getSpy());
    }

    /**
     * Controle si le conjoint a déjà un dossier
     * 
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DossierException
     */
    public boolean hasConjointDossier() throws DossierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (JadeStringUtil.isBlank(idTiersConjoint)) {
            return false;
        }
        DossierSearchModel searchModel = new DossierSearchModel();
        searchModel.setForIdTiers(idTiersConjoint);
        searchModel = PerseusServiceLocator.getDossierService().search(searchModel);
        if (searchModel.getSize() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void retrieve() throws Exception {
        situationFamiliale = PerseusServiceLocator.getSituationFamilialeService().read(situationFamiliale.getId());
        rentePont = PerseusServiceLocator.getRentePontService().read(idRentePont);
        idTiersConjoint = situationFamiliale.getConjoint().getMembreFamille().getSimpleMembreFamille().getIdTiers();

        // Recherche des enfants
        EnfantFamilleSearchModel searchModel = new EnfantFamilleSearchModel();
        searchModel.setForIdSituationFamiliale(situationFamiliale.getId());
        searchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(searchModel);

        // Reparcourir la liste pour ajouter les données financières
        for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
            EnfantFamille ef = (EnfantFamille) abstractModel;
            listeEnfantFamille.add(ef);
        }
    }

    public void setAjaxConjointViewBean(PFConjointAjaxViewBean ajaxConjointViewBean) {
        this.ajaxConjointViewBean = ajaxConjointViewBean;
    }

    @Override
    public void setId(String newId) {
        situationFamiliale.setId(newId);
    }

    /**
     * @param idRentePont
     *            the idRentePont to set
     */
    public void setIdRentePont(String idRentePont) {
        this.idRentePont = idRentePont;
    }

    /**
     * @param idTiersConjoint
     *            the idTiersConjoint to set
     */
    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    /**
     * @param listeEnfantFamille
     *            the listeEnfantFamille to set
     */
    public void setListeEnfantFamille(List listeEnfantFamille) {
        this.listeEnfantFamille = listeEnfantFamille;
    }

    /**
     * @param rentePont
     *            the rentePont to set
     */
    public void setRentePont(RentePont rentePont) {
        this.rentePont = rentePont;
    }

    /**
     * @param situationFamiliale
     *            the situationFamiliale to set
     */
    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    @Override
    public void update() throws Exception {

    }
}
