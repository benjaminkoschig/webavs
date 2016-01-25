package ch.globaz.vulpecula.externalservices;

import globaz.globall.db.BIJadeSimpleModelExternalService;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.mercato.mutations.myprodis.InfoType;
import ch.globaz.mercato.notification.Notification;
import ch.globaz.vulpecula.business.models.postetravail.AdhesionCotisationPosteTravailSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;

public class AdhesionCotisationPosteTravailTracker implements BIJadeSimpleModelExternalService {

    private RequestFactory requestFactory = new RequestFactory();

    @Override
    public void afterAdd(JadeSimpleModel simpleModel) throws Throwable {
        AdhesionCotisationPosteTravail adhesionCotisationPosteTravail = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository().findById(simpleModel.getId());
        if (TypeAssurance.COTISATION_LPP.equals(adhesionCotisationPosteTravail.getTypeAssurance())) {
            Notification notification = new Notification(InfoType.AJOUT_COTISATION_POSTE_LPP, simpleModel.getId());
            requestFactory.persistFromNouvellePersistance(notification);

            if (!adhesionCotisationPosteTravail.getPeriode().sansFin()) {
                Notification radiation = new Notification(InfoType.RADIATION_COTISATION_POSTE_LPP, simpleModel.getId());
                requestFactory.persistFromNouvellePersistance(radiation);
            }
        }
    }

    @Override
    public void afterDelete(JadeSimpleModel simpleModel) throws Throwable {
    }

    @Override
    public void afterUpdate(JadeSimpleModel simpleModel) throws Throwable {
    }

    @Override
    public void beforeAdd(JadeSimpleModel simpleModel) throws Throwable {
    }

    @Override
    public void beforeDelete(JadeSimpleModel simpleModel) throws Throwable {
        AdhesionCotisationPosteTravailSimpleModel adhesionCotisationPosteTravailSimpleModel = (AdhesionCotisationPosteTravailSimpleModel) simpleModel;
        AdhesionCotisationPosteTravail adhesionCotisationPosteTravail = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository().findById(simpleModel.getId());
        if (TypeAssurance.COTISATION_LPP.equals(adhesionCotisationPosteTravail.getTypeAssurance())) {
            // Lors de la suppression, on transmet l'id du poste de travail car l'adhésion ne sera plus présente en base
            // de données.
            Notification notification = new Notification(InfoType.SUPPRESSION_COTISATION_POSTE_LPP,
                    adhesionCotisationPosteTravailSimpleModel.getIdPosteTravail(), adhesionCotisationPosteTravail
                            .getAssurance().getLibelleCourtFr().trim()); // on fait usage du champ 'extra', pour
                                                                         // communiquer quel plan a été supprimé - car
                                                                         // l'info ne sera plus présente en DB.
            requestFactory.persistFromNouvellePersistance(notification);
        }
    }

    @Override
    public void beforeUpdate(JadeSimpleModel simpleModel) throws Throwable {
        AdhesionCotisationPosteTravail adhesionCotisationOld = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository().findById(simpleModel.getId());

        if (TypeAssurance.COTISATION_LPP.equals(adhesionCotisationOld.getTypeAssurance())) {
            AdhesionCotisationPosteTravailSimpleModel adhesionCotisationUpdated = (AdhesionCotisationPosteTravailSimpleModel) simpleModel;

            // la date de début a changé
            if (!adhesionCotisationOld.getPeriode().getDateDebut().getSwissValue()
                    .equals(adhesionCotisationUpdated.getDateDebut())) {
                requestFactory.persistFromNouvellePersistance(new Notification(
                        InfoType.MODIFICATION_DATE_DEBUT_COTISATION_POSTE_LPP, adhesionCotisationOld.getId()));
            }

            // s'il y a une date de fin sur le modèle updaté ...
            if (!JadeNumericUtil.isEmptyOrZero(adhesionCotisationUpdated.getDateFin())) {
                // et qu'il y avait déjà une date de fin en db différente de celle du modèle updaté, alors c'est un
                // changement de date de fin
                if (adhesionCotisationOld.getPeriode().getDateFin() != null) {
                    if (!adhesionCotisationOld.getPeriode().getDateFin().getSwissValue()
                            .equals(adhesionCotisationUpdated.getDateFin())) {
                        requestFactory.persistFromNouvellePersistance(new Notification(
                                InfoType.MODIFICATION_DATE_FIN_COTISATION_POSTE_LPP, adhesionCotisationOld.getId()));
                    }
                }
                // sinon, c'est une radiation
                else {
                    requestFactory.persistFromNouvellePersistance(new Notification(
                            InfoType.RADIATION_COTISATION_POSTE_LPP, adhesionCotisationOld.getId()));
                }
            }

            // il n'y pas de date de fin sur le modèle updaté
            else {
                // y en avait-il une auparavant ?
                // si oui : on fait quoi ?
            }
        }
    }
}
