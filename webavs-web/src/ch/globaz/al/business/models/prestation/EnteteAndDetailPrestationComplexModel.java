package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * 
 * Modèle complexe pour l'affichage des prestations dans l'écran principal du dossier
 * 
 * @author AGE
 * 
 */
public class EnteteAndDetailPrestationComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Modèle simple du détail de la prestation (modèle root)
     */
    private DetailPrestationModel detailPrestationModel = null;
    /**
     * Modèle simple de l'entête prestation liée au détail prestation
     */
    private EntetePrestationModel entetePrestationModel = null;
    /**
     * modèle simple de la récap lié à la prestation
     */
    private RecapitulatifEntrepriseModel recapModel = null;

    /**
     * Constructeur du modèle du détail prestation
     */
    public EnteteAndDetailPrestationComplexModel() {
        super();
        detailPrestationModel = new DetailPrestationModel();
        entetePrestationModel = new EntetePrestationModel();
        recapModel = new RecapitulatifEntrepriseModel();
    }

    /**
     * Retourne le modèle détail prestation (root)
     * 
     * @return detailPrestationModel
     */
    public DetailPrestationModel getDetailPrestationModel() {
        return detailPrestationModel;
    }

    /**
     * Retourne le modèle de l'entête prestation lié au détail prestation
     * 
     * @return entetePrestationModel
     */
    public EntetePrestationModel getEntetePrestationModel() {
        return entetePrestationModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return detailPrestationModel.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return detailPrestationModel.getSpy();
    }

    /**
     * Définit le détail de prestation
     * 
     * @param detailPrestationModel
     *            détail de la prestation
     */
    public void setDetailPrestationModel(DetailPrestationModel detailPrestationModel) {
        this.detailPrestationModel = detailPrestationModel;
    }

    /**
     * Définit l'entête prestation lié au détail prestation
     * 
     * @param entetePrestationModel
     *            En-tête de la prestation
     */
    public void setEntetePrestationModel(EntetePrestationModel entetePrestationModel) {
        this.entetePrestationModel = entetePrestationModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        detailPrestationModel.setId(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        detailPrestationModel.setSpy(spy);

    }

    /**
     * Retourne le modèle de la récap
     * 
     * @return recapModel
     */
    public RecapitulatifEntrepriseModel getRecapModel() {
        return recapModel;
    }

    /**
     * Définit la récap
     * 
     * @param recapModel
     *            récap de la prestation
     */
    public void setRecapModel(RecapitulatifEntrepriseModel recapModel) {
        this.recapModel = recapModel;
    }
}
