package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * 
 * Mod�le complexe pour l'affichage des prestations dans l'�cran principal du dossier
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
     * Mod�le simple du d�tail de la prestation (mod�le root)
     */
    private DetailPrestationModel detailPrestationModel = null;
    /**
     * Mod�le simple de l'ent�te prestation li�e au d�tail prestation
     */
    private EntetePrestationModel entetePrestationModel = null;
    /**
     * mod�le simple de la r�cap li� � la prestation
     */
    private RecapitulatifEntrepriseModel recapModel = null;

    /**
     * Constructeur du mod�le du d�tail prestation
     */
    public EnteteAndDetailPrestationComplexModel() {
        super();
        detailPrestationModel = new DetailPrestationModel();
        entetePrestationModel = new EntetePrestationModel();
        recapModel = new RecapitulatifEntrepriseModel();
    }

    /**
     * Retourne le mod�le d�tail prestation (root)
     * 
     * @return detailPrestationModel
     */
    public DetailPrestationModel getDetailPrestationModel() {
        return detailPrestationModel;
    }

    /**
     * Retourne le mod�le de l'ent�te prestation li� au d�tail prestation
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
     * D�finit le d�tail de prestation
     * 
     * @param detailPrestationModel
     *            d�tail de la prestation
     */
    public void setDetailPrestationModel(DetailPrestationModel detailPrestationModel) {
        this.detailPrestationModel = detailPrestationModel;
    }

    /**
     * D�finit l'ent�te prestation li� au d�tail prestation
     * 
     * @param entetePrestationModel
     *            En-t�te de la prestation
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
     * Retourne le mod�le de la r�cap
     * 
     * @return recapModel
     */
    public RecapitulatifEntrepriseModel getRecapModel() {
        return recapModel;
    }

    /**
     * D�finit la r�cap
     * 
     * @param recapModel
     *            r�cap de la prestation
     */
    public void setRecapModel(RecapitulatifEntrepriseModel recapModel) {
        this.recapModel = recapModel;
    }
}
