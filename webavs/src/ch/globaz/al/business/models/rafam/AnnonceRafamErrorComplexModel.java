package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle complexe d'une annonce RAFAM.
 * 
 * @author jts
 * 
 */
public class AnnonceRafamErrorComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Modèle simple de l'annonce
     */
    AnnonceRafamModel annonceRafamModel = null;
    /**
     * Erreur liée à l'annonce
     */
    ErreurAnnonceRafamModel erreurAnnonceRafamModel = null;

    ErrorPeriodModel errorPeriodModel = null;

    OverlapInformationModel overlapInformationModel = null;

    /**
     * Constructeur
     */
    public AnnonceRafamErrorComplexModel() {
        erreurAnnonceRafamModel = new ErreurAnnonceRafamModel();
        annonceRafamModel = new AnnonceRafamModel();
        errorPeriodModel = new ErrorPeriodModel();
        overlapInformationModel = new OverlapInformationModel();
    }

    public AnnonceRafamModel getAnnonceRafamModel() {
        return annonceRafamModel;
    }

    public ErreurAnnonceRafamModel getErreurAnnonceRafamModel() {
        return erreurAnnonceRafamModel;
    }

    public ErrorPeriodModel getErrorPeriodModel() {
        return errorPeriodModel;
    }

    @Override
    public String getId() {
        return annonceRafamModel.getIdAnnonce();
    }

    public OverlapInformationModel getOverlapInformationModel() {
        return overlapInformationModel;
    }

    @Override
    public String getSpy() {
        return annonceRafamModel.getSpy();
    }

    public void setAnnonceRafamModel(AnnonceRafamModel annonceRafamModel) {
        this.annonceRafamModel = annonceRafamModel;
    }

    public void setErreurAnnonceRafamModel(ErreurAnnonceRafamModel erreurAnnonceRafamModel) {
        this.erreurAnnonceRafamModel = erreurAnnonceRafamModel;
    }

    public void setErrorPeriodModel(ErrorPeriodModel errorPeriodModel) {
        this.errorPeriodModel = errorPeriodModel;
    }

    @Override
    public void setId(String id) {
        annonceRafamModel.setId(id);
    }

    public void setOverlapInformationModel(OverlapInformationModel overlapInformationModel) {
        this.overlapInformationModel = overlapInformationModel;
    }

    @Override
    public void setSpy(String spy) {
        annonceRafamModel.setSpy(spy);
    }

}
