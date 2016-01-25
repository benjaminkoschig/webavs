package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import ch.globaz.mercato.mutations.myprodis.InfoType;
import ch.globaz.mercato.notification.Notification;

public class AdresseTracker extends BAbstractEntityExternalService {
    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        if (entity instanceof TIAvoirAdresse) {
            TIAvoirAdresse avoirAdressse = (TIAvoirAdresse) entity;
            // On annonce que pour les adresses de type "Domicile"
            if (IConstantes.CS_AVOIR_ADRESSE_DOMICILE.equals(avoirAdressse.getTypeAdresse())) {
                Notification notification = new Notification(InfoType.AJOUT_AVOIR_ADRESSE, entity.getId());
                requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
            }
        } else if (entity instanceof TIAvoirPaiement) {
            Notification notification = new Notification(InfoType.AJOUT_AVOIR_PAIEMENT, entity.getId());
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
        if (entity instanceof TIAvoirAdresse) {
            TIAvoirAdresse avoirAdressse = (TIAvoirAdresse) entity;
            // On annonce que pour les adresses de type "Domicile"
            if (IConstantes.CS_AVOIR_ADRESSE_DOMICILE.equals(avoirAdressse.getTypeAdresse())) {
                if (JadeStringUtil.isBlankOrZero(((TIAvoirAdresse) entity).getDateFinRelation())) {
                    Notification notification = new Notification(InfoType.MODIFICATION_AVOIR_ADRESSE, entity.getId());
                    requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
                }
            }
        } else if (entity instanceof TIAvoirPaiement) {
            if (JadeStringUtil.isBlankOrZero(((TIAvoirPaiement) entity).getDateFinRelation())) {
                Notification notification = new Notification(InfoType.MODIFICATION_AVOIR_PAIEMENT, entity.getId());
                requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
            }
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
