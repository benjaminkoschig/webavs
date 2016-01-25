package globaz.al.vb.prestation;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant l'ent�te d'un prestation
 * 
 * @author GMO
 * 
 */
public class ALEntetePrestationViewBean extends BJadePersistentObjectViewBean {

    /**
     * Mod�le de recherche sur les d�tails de prestations, pour avoir la liste
     */
    private DetailPrestationComplexSearchModel detailPrestationComplexSearchModel = null;

    /**
     * Mod�le du dossier auquel la prestation appartient, utile pour infos alloc
     */
    private DossierComplexModel dossierComplexModel = null;
    /**
     * Mod�le contenant l'ent�te prestation
     */
    private EntetePrestationModel entetePrestationModel = null;

    /**
     * indique si la prestation est dans une r�cap verouill�e ou non
     */
    private boolean isRecapVerrouillee = false;

    /**
     * Constructeur de la classe
     */
    public ALEntetePrestationViewBean() {
        super();
        setDetailPrestationComplexSearchModel(new DetailPrestationComplexSearchModel());
        dossierComplexModel = new DossierComplexModel();
        entetePrestationModel = new EntetePrestationModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        entetePrestationModel = ALServiceLocator.getEntetePrestationModelService().delete(entetePrestationModel);

    }

    /**
     * Retourne le mod�le de recherche (contiendra les d�tails li�s � l'ent�te apr�s le retrieve)
     * 
     * @return detailPrestationComplexSearchModel
     */
    public DetailPrestationComplexSearchModel getDetailPrestationComplexSearchModel() {
        return detailPrestationComplexSearchModel;
    }

    /**
     * Retourne le dossier auquel la prestation appartient
     * 
     * @return dossierComplexModel
     */
    public DossierComplexModel getDossierComplexModel() {
        return dossierComplexModel;
    }

    /**
     * Retourne le mod�le de l'ent�te de la prestation
     * 
     * @return entetePrestationModel
     */
    public EntetePrestationModel getEntetePrestationModel() {
        return entetePrestationModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return entetePrestationModel.getId();
    }

    /**
     * @return session courante
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (entetePrestationModel != null) && !entetePrestationModel.isNew() ? new BSpy(
                entetePrestationModel.getSpy()) : new BSpy(getSession());
    }

    public boolean isRecapVerrouillee() {
        return isRecapVerrouillee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        entetePrestationModel = ALServiceLocator.getEntetePrestationModelService().read(getId());

        if (!JadeStringUtil.isBlankOrZero(entetePrestationModel.getIdRecap())) {
            isRecapVerrouillee = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().isRecapVerouillee(
                    ALServiceLocator.getRecapitulatifEntrepriseModelService().read(entetePrestationModel.getIdRecap()));
        } else {
            isRecapVerrouillee = false;
        }
        dossierComplexModel = ALServiceLocator.getDossierComplexModelService().read(
                entetePrestationModel.getIdDossier());
        detailPrestationComplexSearchModel.setForIdEntete(getId());
        detailPrestationComplexSearchModel = ALServiceLocator.getDetailPrestationComplexModelService().search(
                detailPrestationComplexSearchModel);
    }

    /**
     * D�finit le mod�le de recherche (les crit�res) pour les d�tails de prestations � afficher selon l'ent�te Ne
     * devrait pas �tre utilis� ailleurs que dans le retrieve dans ce viewBean
     * 
     * @param detailPrestationComplexSearchModel
     *            Le mod�le complexe de recherche d'un d�tail de prestation
     */
    public void setDetailPrestationComplexSearchModel(
            DetailPrestationComplexSearchModel detailPrestationComplexSearchModel) {
        this.detailPrestationComplexSearchModel = detailPrestationComplexSearchModel;
    }

    /**
     * D�finit le mod�le du dossier auquel le droit appartient
     * 
     * @param dossierComplexModel
     *            Le mod�le du dossier
     */
    public void setDossierComplexModel(DossierComplexModel dossierComplexModel) {
        this.dossierComplexModel = dossierComplexModel;
    }

    /**
     * D�finit le mod�le de l'entete prestation qui va �tre g�rer par le viewBean
     * 
     * @param entetePrestationModel
     *            : l'ent�te de prestation
     */
    public void setEntetePrestationModel(EntetePrestationModel entetePrestationModel) {
        this.entetePrestationModel = entetePrestationModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        entetePrestationModel.setId(newId);

    }

    public void setRecapVerrouillee(boolean isRecapVerrouillee) {
        this.isRecapVerrouillee = isRecapVerrouillee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");

    }

}
