package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BIJadeSimpleModelExternalService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSearchSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSimpleModel;

public class PosteTravailTracker implements BIJadeSimpleModelExternalService {

    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(JadeSimpleModel model) throws Throwable {
        // Notification notification = new Notification(InfoType.NOUVEAU_POSTE, model.getId());
        // requestFactory.persistFromNouvellePersistance(notification);
    }

    @Override
    public void afterDelete(JadeSimpleModel model) throws Throwable {
    }

    @Override
    public void afterUpdate(JadeSimpleModel model) throws Throwable {
    }

    @Override
    public void beforeAdd(JadeSimpleModel model) throws Throwable {
    }

    @Override
    public void beforeDelete(JadeSimpleModel model) throws Throwable {
    }

    @Override
    public void beforeUpdate(JadeSimpleModel model) throws Throwable {
        // PosteTravailSimpleModel posteTravail = (PosteTravailSimpleModel) model;
        // PosteTravailSimpleModel oldPosteTravail = retrieveOldPosteTravail(model.getId());
        //
        // if (isDateDebutActiviteChanged(posteTravail, oldPosteTravail)) {
        // Notification notificationDateDebut = new Notification(InfoType.MODIFICATION_DATE_DEBUT_POSTE, model.getId());
        // requestFactory.persistFromNouvellePersistance(notificationDateDebut);
        // }
        //
        // if (isDateFinActiviteChanged(posteTravail, oldPosteTravail)) {
        // Notification notificationDateDebut = new Notification(InfoType.MODIFICATION_DATE_FIN_POSTE, model.getId());
        // requestFactory.persistFromNouvellePersistance(notificationDateDebut);
        // }
    }

    private PosteTravailSimpleModel retrieveOldPosteTravail(String idPosteTravail) {
        try {
            PosteTravailSearchSimpleModel searchModel = new PosteTravailSearchSimpleModel();
            searchModel.setForIdPosteTravail(idPosteTravail);
            JadePersistenceManager.search(searchModel);
            if (searchModel.getSize() == 1) {
                return (PosteTravailSimpleModel) searchModel.getSearchResults()[0];
            } else {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_INCONNUE);
            }
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
    }

    private boolean isDateDebutActiviteChanged(PosteTravailSimpleModel posteTravail,
            PosteTravailSimpleModel oldPosteTravail) {
        return !JadeStringUtil.equals(posteTravail.getDebutActivite(), oldPosteTravail.getDebutActivite(), true);
    }

    private boolean isDateFinActiviteChanged(PosteTravailSimpleModel posteTravail,
            PosteTravailSimpleModel oldPosteTravail) {
        return !JadeStringUtil.equals(posteTravail.getFinActivite(), oldPosteTravail.getFinActivite(), true);
    }
}
