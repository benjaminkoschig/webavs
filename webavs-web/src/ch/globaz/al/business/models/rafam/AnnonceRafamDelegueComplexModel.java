package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeComplexModel;

public class AnnonceRafamDelegueComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AnnonceRafamModel annonceRafamModel = null;
    private ComplementDelegueModel complementDelegueModel = null;

    /**
     * Constructeur du modèle
     */
    public AnnonceRafamDelegueComplexModel() {
        super();
        annonceRafamModel = new AnnonceRafamModel();
        complementDelegueModel = new ComplementDelegueModel();

    }

    public AnnonceRafamModel getAnnonceRafamModel() {
        return annonceRafamModel;
    }

    public ComplementDelegueModel getComplementDelegueModel() {
        return complementDelegueModel;
    }

    @Override
    public String getId() {
        return getAnnonceRafamModel().getIdAnnonce();
    }

    @Override
    public String getSpy() {
        return annonceRafamModel.getSpy();
    }

    public void setAnnonceRafamModel(AnnonceRafamModel annonceRafamModel) {
        this.annonceRafamModel = annonceRafamModel;
    }

    public void setComplementDelegueModel(ComplementDelegueModel complementDelegueModel) {
        this.complementDelegueModel = complementDelegueModel;
    }

    @Override
    public void setId(String id) {
        annonceRafamModel.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        annonceRafamModel.setSpy(spy);

    }

}
