package ch.globaz.vulpecula.externalservices;

import ch.globaz.mercato.mutations.myprodis.InfoType;
import ch.globaz.mercato.notification.Notification;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;

public class AdresseTracker extends BAbstractEntityExternalService {
    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        if (entity instanceof TIAvoirAdresse) {
            TIAvoirAdresse avoirAdressse = (TIAvoirAdresse) entity;
            Notification notification = new Notification(InfoType.AJOUT_AVOIR_ADRESSE, entity.getId(),
                    avoirAdressse.getIdTiers());
            requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
        } else if (entity instanceof TIAvoirPaiement) {
            TIAvoirPaiement avoirPaiement = (TIAvoirPaiement) entity;
            Notification notification = new Notification(InfoType.AJOUT_AVOIR_PAIEMENT, entity.getId(), avoirPaiement.getIdTiers());
            requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
        }
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
    	String idTiers = null;
        if (entity instanceof TIAvoirAdresse) {
            TIAvoirAdresse avoirAdressse = (TIAvoirAdresse) entity;
            idTiers = avoirAdressse.getIdTiers();
            if (JadeStringUtil.isBlankOrZero(((TIAvoirAdresse) entity).getDateFinRelation())) {
                Notification notification = new Notification(InfoType.MODIFICATION_AVOIR_ADRESSE, entity.getId(),
                		idTiers);
                requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
            }
        } else if (entity instanceof TIAvoirPaiement) {
            TIAvoirPaiement avoirPaiement = (TIAvoirPaiement) entity;
            idTiers = avoirPaiement.getIdTiers();
            if (JadeStringUtil.isBlankOrZero(((TIAvoirPaiement) entity).getDateFinRelation())) {
                Notification notification = new Notification(InfoType.MODIFICATION_AVOIR_PAIEMENT, entity.getId(),
                		idTiers);
                requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
            }
        }
        
        // Notification pour eBusiness
        if (idTiers != null && idTiers.length() != 0) {
        	requestFactory.persistFromAnciennePersistanceEbu(idTiers, entity.getSession());
        }
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}
