package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle de recherche pour les saisie adi complexe
 * 
 * @author GMO
 * 
 */
public class AdiSaisieComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère id decompte adi
     */
    private String forIdDecompteAdi = null;

    /**
     * @return forIdDecompteAdi
     */
    public String getForIdDecompteAdi() {
        return forIdDecompteAdi;
    }

    /**
     * @param forIdDecompteAdi
     *            critère id decompte adi
     */
    public void setForIdDecompteAdi(String forIdDecompteAdi) {
        this.forIdDecompteAdi = forIdDecompteAdi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return AdiSaisieComplexModel.class;
    }

}
