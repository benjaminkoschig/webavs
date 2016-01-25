package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALCommentaireModelException;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;
import ch.globaz.al.business.services.models.dossier.CommentaireModelService;
import ch.globaz.al.businessimpl.checker.model.dossier.CommentaireModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistance des commentaires de dossier
 * 
 * @author jts
 */
public class CommentaireModelServiceImpl extends ALAbstractBusinessServiceImpl implements CommentaireModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.CommentaireModelService
     * #clone(ch.globaz.al.business.models.dossier.CommentaireModel, java.lang.String)
     */
    @Override
    public CommentaireModel clone(CommentaireModel commentaireModel, String idDossier) throws JadeApplicationException,
            JadePersistenceException {
        CommentaireModel newCommentaireModel = new CommentaireModel();
        newCommentaireModel = commentaireModel;
        newCommentaireModel.setId("");
        newCommentaireModel.setCreationSpy("");
        newCommentaireModel.setSpy("");
        newCommentaireModel.setIdDossier(idDossier);

        return newCommentaireModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.CommentaireModelService
     * #count(ch.globaz.al.business.models.dossier.CommentaireSearch)
     */
    @Override
    public int count(CommentaireSearchModel commentaireSearch) throws JadePersistenceException {
        return JadePersistenceManager.count(commentaireSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CommentaireModelService#create(
     * ch.globaz.al.business.model.dossier.CommentaireModel)
     */
    @Override
    public CommentaireModel create(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException {

        if (commentaireModel == null) {
            throw new ALCommentaireModelException("Unable to add model (commentaireModel) - the model passed is null!");
        }
        // Valide le modèle
        CommentaireModelChecker.validate(commentaireModel);

        // L'ajoute en persistence
        return (CommentaireModel) JadePersistenceManager.add(commentaireModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CommentaireModelService#delete(
     * ch.globaz.al.business.model.dossier.CommentaireModel)
     */
    @Override
    public CommentaireModel delete(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException {
        if (commentaireModel == null) {
            throw new ALCommentaireModelException(
                    "Unable to remove model (commentaireModel) - the model passed is null!");
        }

        if (commentaireModel.isNew()) {
            throw new ALCommentaireModelException(
                    "Unable to remove model (commentaireModel) - the model passed is new!");
        }

        // Le supprime en DB
        return (CommentaireModel) JadePersistenceManager.delete(commentaireModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CommentaireModelService#read(java .lang.String)
     */
    @Override
    public CommentaireModel read(String idCommentaire) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idCommentaire)) {
            throw new ALCommentaireModelException(
                    "Unable to read model (commentaireModel) - the id passed is not defined!");
        }

        CommentaireModel commentaireModel = new CommentaireModel();
        commentaireModel.setId(idCommentaire);

        return (CommentaireModel) JadePersistenceManager.read(commentaireModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.CommentaireModelService#
     * search(ch.globaz.al.business.models.dossier.CommentaireSearch)
     */
    @Override
    public CommentaireSearchModel search(CommentaireSearchModel commentaireSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (commentaireSearchModel == null) {
            throw new ALCommentaireModelException("unable to search commentaireModel - the model passed is null");
        }
        return (CommentaireSearchModel) JadePersistenceManager.search(commentaireSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CommentaireModelService#update(
     * ch.globaz.al.business.model.dossier.CommentaireModel)
     */
    @Override
    public CommentaireModel update(CommentaireModel commentaireModel) throws JadeApplicationException,
            JadePersistenceException {
        if (commentaireModel == null) {
            throw new ALCommentaireModelException(
                    "Unable to update model (commentaireModel) - the model passed is null!");
        }
        if (commentaireModel.isNew()) {
            throw new ALCommentaireModelException(
                    "Unable to update model (commentaireModel) - the model passed is new!");
        }
        // Valide l'integrity
        CommentaireModelChecker.validate(commentaireModel);
        // Le modifie en DB
        return (CommentaireModel) JadePersistenceManager.update(commentaireModel);
    }

}
