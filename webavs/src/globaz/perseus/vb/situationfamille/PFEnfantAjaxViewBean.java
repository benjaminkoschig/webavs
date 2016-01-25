package globaz.perseus.vb.situationfamille;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.Iterator;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PFEnfantAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private EnfantFamille enfantFamille = null;
    private boolean getListe = false;
    private String idDemande = null;
    private String idSituationFamiliale = null;
    private transient PFEnfantAjaxListViewBean listeEnfantFamille;
    private PFSituationfamilialeViewBean parentViewBean = null;

    public PFEnfantAjaxViewBean() {
        super();
        enfantFamille = new EnfantFamille();
    }

    public PFEnfantAjaxViewBean(EnfantFamille enfantFamille) {
        super();
        this.enfantFamille = enfantFamille;
    }

    @Override
    public void add() throws Exception {
        enfantFamille = PerseusImplServiceLocator.getEnfantFamilleService().createAjax(enfantFamille);

        updateListe();
    }

    @Override
    public void delete() throws Exception {
        enfantFamille = PerseusImplServiceLocator.getEnfantFamilleService().delete(enfantFamille, idDemande);
        updateListe();
    }

    /**
     * @return the enfantFamille
     */
    public EnfantFamille getEnfantFamille() {
        return enfantFamille;
    }

    @Override
    public String getId() {
        return enfantFamille.getId();
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idSituationFamiliale
     */
    public String getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    /**
     * @return the listeEnfantFamille
     */
    public PFEnfantAjaxListViewBean getListeEnfantFamille() {
        return listeEnfantFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeEnfantFamille;
    }

    /**
     * @return the parentViewBean
     */
    public PFSituationfamilialeViewBean getParentViewBean() {
        return parentViewBean;
    }

    @Override
    public BSpy getSpy() {
        return (enfantFamille != null) && !enfantFamille.isNew() ? new BSpy(enfantFamille.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    @Override
    public boolean hasList() {
        return true;
    }

    /**
     * @return the getListe
     */
    public boolean isGetListe() {
        return getListe;
    }

    @Override
    public Iterator iterator() {

        return listeEnfantFamille.getListeEnfantFamille().iterator();
    }

    @Override
    public void retrieve() throws Exception {
        enfantFamille = PerseusImplServiceLocator.getEnfantFamilleService().read(enfantFamille.getId());
    }

    /**
     * @param enfantFamille
     *            the enfantFamille to set
     */
    public void setEnfantFamille(EnfantFamille enfantFamille) {
        this.enfantFamille = enfantFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    /**
     * @param getListe
     *            the getListe to set
     */
    @Override
    public void setGetListe(boolean getListe) {
        this.getListe = getListe;
    }

    @Override
    public void setId(String newId) {
        enfantFamille.setId(newId);
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idSituationFamiliale
     *            the idSituationFamiliale to set
     */
    public void setIdSituationFamiliale(String idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }

    /**
     * @param listeEnfantFamille
     *            the listeEnfantFamille to set
     */
    public void setListeEnfantFamille(PFEnfantAjaxListViewBean listeEnfantFamille) {
        this.listeEnfantFamille = listeEnfantFamille;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface listViewBean) {
        listeEnfantFamille = (PFEnfantAjaxListViewBean) listViewBean;

    }

    /**
     * @param parentViewBean
     *            the parentViewBean to set
     */
    public void setParentViewBean(PFSituationfamilialeViewBean parentViewBean) {
        this.parentViewBean = parentViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        /* On bypass l'update normale en passant par un appel Ajax (situationfamiliale_MembrePart.js) */
        // enfantFamille = PerseusImplServiceLocator.getEnfantFamilleService().update(enfantFamille);
        updateListe();
    }

    private void updateListe() throws Exception {
        if (getListe) {
            listeEnfantFamille = new PFEnfantAjaxListViewBean();
            listeEnfantFamille.getEnfantFamilleSearch().setForIdSituationFamiliale(idSituationFamiliale);
            listeEnfantFamille.find();
        }
    }

}
