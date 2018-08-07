package globaz.al.vb.rafam;

import java.util.Date;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

/**
 * ViewBean représentant une annonce RAFAM
 *
 * @author jts
 *
 */
public class ALAnnonceRafamViewBean extends BJadePersistentObjectViewBean {

    /**
     * Modèle d'une annonce
     */
    private AnnonceRafamComplexModel annonce = null;

    /** Indique si l'annonce peut être supprimée */
    private Boolean canDelete = null;
    /** Indique si l'annonce peut être validée */
    private Boolean canValidate = null;

    /** Liste des erreurs liées à l'annonce */
    public AnnonceRafamErrorComplexSearchModel errors = null;

    /**
     * Constructeur du viewBean
     */
    public ALAnnonceRafamViewBean() {
        super();
        annonce = new AnnonceRafamComplexModel();
    }

    @Override
    public void add() throws Exception {
        annonce.setAnnonceRafamModel(
                ALServiceLocator.getAnnonceRafamModelService().create(annonce.getAnnonceRafamModel()));
    }

    public Boolean canDelete() {
        return canDelete;
    }

    public Boolean canValidate() {
        return canValidate;
    }

    @Override
    public void delete() throws Exception {
        ALServiceLocator.getAnnonceRafamModelService().delete(annonce.getAnnonceRafamModel());
    }

    /**
     * Retourne le modèle d'annonce Rafam
     *
     * @return the annonce
     */
    public AnnonceRafamComplexModel getAnnonce() {
        return annonce;
    }

    public String getEditionModeFields() {
        if (canValidate == false) {
            try {
                ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                        ALConstParametres.APPNAME, ALConstParametres.RAFAM_ADMIN_EDITION,
                        JadeDateUtil.getGlobazFormattedDate(new Date()));
                canValidate = "1".equals(param.getValeurAlphaParametre()) ? true : false;

                // si le paramètre edition admin est à true ou si l'annonce est nouvelle (on la créé manuellement) ou si
                // elle
                // est en cours
                if (canValidate || annonce.isNew()
                        || RafamEtatAnnonce.ENREGISTRE.equals(
                                RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getAnnonceRafamModel().getEtat()))
                        || RafamEtatAnnonce.A_TRANSMETTRE.equals(
                                RafamEtatAnnonce.getRafamEtatAnnonceCS(annonce.getAnnonceRafamModel().getEtat()))) {
                    return "";
                } else {

                    return "disabled=\"disabled\" class=\"readOnly\" readonly=\"readonly\"";
                }
            } catch (Exception e) {
                canValidate = false;
            }
        }

        return "disabled=\"disabled\" class=\"readOnly\" readonly=\"readonly\"";

    }

    /**
     *
     * @return Erreurs liées à l'annonce
     * @throws Exception
     *             Exception levée si l'id de l'annonce n'est pas défini ou si les erreurs n'ont pas pu être chargées
     */
    public AnnonceRafamErrorComplexSearchModel getErrors() throws Exception {
        if (errors == null) {
            errors = ALServiceLocator.getAnnoncesRafamErrorBusinessService().getErrorsForAnnonce(getId());
        }

        return errors;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return annonce.getId();
    }

    /**
     * @return the session
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
        return (annonce != null) && !annonce.isNew() ? new BSpy(annonce.getSpy()) : new BSpy(getSession());
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
        // ALConstParametres.APPNAME, ALConstParametres.RAFAM_ADMIN_EDITION,
        // JadeDateUtil.getGlobazFormattedDate(new Date()));
        // this.editionAdmin = "1".equals(param.getValeurAlphaParametre()) ? true : false;
        annonce = ALServiceLocator.getAnnonceRafamComplexModelService().read(getId());
        // FIXME : remplacer ALImplServiceLocator par ALServiceLocator
        canValidate = ALImplServiceLocator.getAnnonceRafamBusinessService().canValidate(annonce.getAnnonceRafamModel());
        canDelete = ALImplServiceLocator.getAnnonceRafamBusinessService().canDelete(annonce.getAnnonceRafamModel());
    }

    /**
     * @param annonce
     *            le modèle représentant l'annonce
     */
    public void setAnnonce(AnnonceRafamComplexModel annonce) {
        this.annonce = annonce;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        annonce.setId(newId);

    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        annonce.setAnnonceRafamModel(
                ALServiceLocator.getAnnonceRafamModelService().update(annonce.getAnnonceRafamModel()));

    }

    public String getPays() {
        try {
            String codeCentrale = getAnnonce().getAnnonceRafamModel().getCodeCentralePaysEnfant();
            if (!JadeStringUtil.isBlankOrZero(codeCentrale)) {
                PaysSearchSimpleModel searchModel = new PaysSearchSimpleModel();
                searchModel.setForIdPays(codeCentrale);
                searchModel = (PaysSearchSimpleModel) JadePersistenceManager.search(searchModel);
                if (searchModel.getSearchResults().length > 0) {
                    return ((PaysSimpleModel) searchModel.getSearchResults()[0]).getLibelleFr();
                }
            }
            return "";
        } catch (JadePersistenceException e) {
            return "";
        }
    }
}
