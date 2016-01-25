package globaz.perseus.vb.situationfamille;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PFSituationfamilialeViewBean extends BJadePersistentObjectViewBean {

    private PFConjointAjaxViewBean ajaxConjointViewBean = null;
    private Demande demande = null;
    private String idDemande = null;
    private String idTiersConjoint = null;
    private List listeEnfantFamille = new ArrayList();

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

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    @Override
    public String getId() {
        return situationFamiliale.getId();
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
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

    /**
     * Controle si la demande liée contient les conditions pour être octroyée. (Si le requérant est dans le canton
     * depuis plus de 3 ans ; qu'on ne force pas un refus ; pas d'enfant de moins de 16 ans)
     * 
     * @return
     */
    public boolean isDemandeAcceptable() {
        if (demande.getSimpleDemande().getRefusForce()) {
            return false;
        }
        if (JadeDateUtil.getNbYearsBetween(demande.getSimpleDemande().getDateArrivee(),
                JadeDateUtil.getGlobazFormattedDate(new Date())) < 3) {
            return false;
        }
        if (!demande.getSimpleDemande().getCalculable()) {
            return false;
        }
        if (demande.getSimpleDemande().getNonEntreeEnMatiere()) {
            return false;
        }
        return true;
    }

    @Override
    public void retrieve() throws Exception {
        situationFamiliale = PerseusServiceLocator.getSituationFamilialeService().read(situationFamiliale.getId());
        demande = PerseusServiceLocator.getDemandeService().read(idDemande);
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

        getISession()
                .setAttribute(
                        "likeNss",
                        demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                                .getNumAvsActuel());
    }

    public void setAjaxConjointViewBean(PFConjointAjaxViewBean ajaxConjointViewBean) {
        this.ajaxConjointViewBean = ajaxConjointViewBean;
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String newId) {
        situationFamiliale.setId(newId);
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
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
     * @param situationFamiliale
     *            the situationFamiliale to set
     */
    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    @Override
    public void update() throws Exception {
        // Si le conjoint a changé
        String ancienIdTiersConjoint = situationFamiliale.getConjoint().getMembreFamille().getSimpleMembreFamille()
                .getIdTiers();
        if (!idTiersConjoint.equals(ancienIdTiersConjoint) && !JadeStringUtil.isEmpty(ancienIdTiersConjoint)) {
            // On change de conjoint
            PerseusServiceLocator.getSituationFamilialeService().changeConjoint(situationFamiliale, idTiersConjoint,
                    idDemande);
        } else if (JadeStringUtil.isEmpty(ancienIdTiersConjoint) && !JadeStringUtil.isEmpty(idTiersConjoint)) {
            // On ajoute le conjoint
            PerseusServiceLocator.getSituationFamilialeService().addConjoint(situationFamiliale, idTiersConjoint);
        } else {
            // On modifie, (Pour l'instant rien d'autre ne peut être modifié mais)
            PerseusServiceLocator.getSituationFamilialeService().update(situationFamiliale);
        }

    }
}
