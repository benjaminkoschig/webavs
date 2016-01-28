package globaz.perseus.vb.rentepont;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PFConjointAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Demande demande = null;
    private String idDemande = null;
    private String idTiersConjoint = null;
    private List listeEnfantFamille = new ArrayList();
    private SituationFamiliale situationFamiliale = null;

    public PFConjointAjaxViewBean() {
        super();
        situationFamiliale = new SituationFamiliale();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

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

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
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

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        situationFamiliale = PerseusServiceLocator.getSituationFamilialeService().read(situationFamiliale.getId());
        // this.demande = PerseusServiceLocator.getDemandeService().read(this.idDemande);
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

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setGetListe(boolean arg0) {

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

    @Override
    public void setListViewBean(FWViewBeanInterface arg0) {
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
