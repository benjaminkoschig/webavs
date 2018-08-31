package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.naos.db.cotisation.AFCotisation;
import ch.globaz.mercato.mutations.myprodis.InfoType;
import ch.globaz.mercato.notification.Notification;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;

public class CotisationTracker extends BAbstractEntityExternalService {
    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        AFCotisation cotisation = (AFCotisation) entity;
        if(isAssuranceLPP(cotisation)) {
            Notification notification = new Notification(InfoType.AJOUT_COTISATION_LPP, entity.getId());
            requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
    
            if (!JadeNumericUtil.isEmptyOrZero(cotisation.getDateFin())) {
                Notification radiationNotification = new Notification(InfoType.RADIATION_COTISATION_LPP, entity.getId());
                requestFactory.persistFromAnciennePersistance(radiationNotification, entity.getSession());
            }
        }
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        AFCotisation cotisation = (AFCotisation) entity;
        if(isAssuranceLPP(cotisation)) {
            Notification notification = new Notification(InfoType.SUPPRESSION_COTISATION_LPP, cotisation.getAdhesion().getAffiliationId(), cotisation.getAssurance().getAssuranceLibelleCourtFr().trim());
            requestFactory.persistFromAnciennePersistance(notification, entity.getSession());
        }
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        // A chaque modification de cotisation, si la date de fin est saisie, on annonce la radiation de la cotisation
        // LPP
        AFCotisation cotisation = (AFCotisation) entity;
        if(isAssuranceLPP(cotisation)) {
            Notification notification = new Notification(InfoType.RADIATION_COTISATION_LPP, entity.getId());
            if (!JadeNumericUtil.isEmptyOrZero(cotisation.getDateFin())) {
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
    
    private boolean isAssuranceLPP(AFCotisation cotisation) {
        return TypeAssurance.COTISATION_LPP.getValue().equals(cotisation.getAssurance().getTypeAssurance());
    }
}
