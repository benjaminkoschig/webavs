package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * Classe du modèle complexe de copie
 * 
 * @author PTA
 * 
 */
public class CopieComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * CopieModel
     */
    private CopieModel copieModel = null;// entité maître

    /**
     * PersonneEtendueComplexModel
     */
    private TiersSimpleModel tiersSimpleModel = null;

    /**
     * Constructeur du modèle
     */
    public CopieComplexModel() {
        super();
        copieModel = new CopieModel();

        tiersSimpleModel = new TiersSimpleModel();
    }

    /**
     * 
     * @return copieModel
     */

    public CopieModel getCopieModel() {
        return copieModel;
    }

    @Override
    public String getId() {
        return copieModel.getId();
    }

    @Override
    public String getSpy() {
        return copieModel.getSpy();
    }

    /**
     * 
     * @return tiersModel
     */
    public TiersSimpleModel getTiersSimpleModel() {
        return tiersSimpleModel;
    }

    /**
     * 
     * @param copieModel
     *            : the copieModel to set
     */
    public void setCopieModel(CopieModel copieModel) {
        this.copieModel = copieModel;
    }

    @Override
    public void setId(String id) {
        copieModel.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        copieModel.setSpy(spy);

    }

    /**
     * 
     * @param tiersSimpleModel
     *            : the tiersModel to set
     */
    public void setTiersSimpleModel(TiersSimpleModel tiersSimpleModel) {
        this.tiersSimpleModel = tiersSimpleModel;
    }
}
