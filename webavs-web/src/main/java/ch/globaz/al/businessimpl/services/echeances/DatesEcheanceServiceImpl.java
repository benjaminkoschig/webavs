package ch.globaz.al.businessimpl.services.echeances;

import ch.globaz.al.utils.ALFomationUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.exceptions.business.ALDroitBusinessException;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.DatesEcheanceService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * classe contenant les spécificités liées aux calculs des échéances
 * 
 * @author PTA
 * 
 */
public class DatesEcheanceServiceImpl extends ALAbstractBusinessServiceImpl implements DatesEcheanceService {

    @Override
    public String getDateDebutValiditeDroit(DroitComplexModel droitComplexModel, String dateDebutDossier)
            throws JadeApplicationException, JadePersistenceException {

        String dateDebutValiditeDroit = null;
        if (droitComplexModel == null) {
            throw new ALDroitBusinessException(
                    "DatesEcheanceServiceImpl#getDateDebutValiditeDroit : droitComplexModel is null");
        }

        // TODO mettre un message plus clair pour l'utilisateur
        if (!JadeDateUtil.isGlobazDate(dateDebutDossier)) {
            throw new ALDroitBusinessException("DatesEcheanceServiceImpl#getDateDebutValiditeDroit : "
                    + dateDebutDossier + " is not a valid date");
        }

        // si type droit ménage
        if (JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN, true)) {
            dateDebutValiditeDroit = dateDebutDossier;
        }
        // si type droit enfant
        else if (JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_ENF, true)) {

            dateDebutValiditeDroit = ALImplServiceLocator.getDatesEcheancePrivateService().getDateDebutValiditeDroit(
                    droitComplexModel.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                            .getDateNaissance(), dateDebutDossier);
        }
        // si type droit formation
        else if (JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_FORM, true)) {

            // recherche si existence droit enfant pour l'enfant, si ce droit
            // enfant existe le début de validité commence le début du mois
            // suivant la fin du droit enfant
            DroitComplexSearchModel se = new DroitComplexSearchModel();
            se.setWhereKey("echeancesDroit");
            se.setOrderKey("echeancesDroit");
            se.setForIdDossier(droitComplexModel.getDroitModel().getIdDossier());
            se.setForIdEnfant(droitComplexModel.getEnfantComplexModel().getEnfantModel().getIdEnfant());
            se.setForTypeDroit(ALCSDroit.TYPE_ENF);
            se = ALServiceLocator.getDroitComplexModelService().search(se);

            // si aucun résultat on initialise le début de validité à la date du
            // début du dossier ou à la date du début du droit de formation selon la lafam si cette date est postérieure
            // à la date du début d'activité

            if ((se.getSize() == 0)
                    || JadeNumericUtil.isEmptyOrZero(droitComplexModel.getEnfantComplexModel().getEnfantModel()
                            .getIdEnfant())) {
                // ajouter l'âge de début de formation à la date de naissance et mois suivant et retourner la date
                String datenaissance = droitComplexModel.getEnfantComplexModel()
                        .getPersonneEtendueComplexModel().getPersonne().getDateNaissance();
                String dateDebutValiditeDroitFormation = ALFomationUtils.calculDateDebutFormation(datenaissance);
                // si la date du début du dossier est antérieure à la date du début de la formation on retourne la date
                // du début du droit sinon la date du début du dossier
                if (JadeDateUtil.isDateAfter(dateDebutValiditeDroitFormation, dateDebutDossier)) {
                    dateDebutValiditeDroit = dateDebutValiditeDroitFormation;
                } else {
                    dateDebutValiditeDroit = dateDebutDossier;
                }

            }

            // ici on récupère le droit le plus récent
            else if (se.getSize() != 0) {

                DroitComplexModel newFormationDroit = new DroitComplexModel();
                // récupère le droit le plus récent
                newFormationDroit = (DroitComplexModel) se.getSearchResults()[0];
                // début de validité début du mois suivant la fin de valitdité
                dateDebutValiditeDroit = ALImplServiceLocator.getDatesEcheancePrivateService()
                        .getDateDebutValiditeDroit(newFormationDroit.getDroitModel().getFinDroitForcee());

            }

        }

        return dateDebutValiditeDroit;
    }

    @Override
    public String getDateFinValiditeDroitCalculee(DroitComplexModel droitComplexModel) throws JadeApplicationException, JadePersistenceException {

        if (droitComplexModel == null) {
            throw new ALEcheanceModelException(
                    "DatesEcheanceServiceImpl#getDateFinValiditeDroitCalculee : droitComplex is null");

        }

        String dateFinValiditeCalcule = null;

        // fin d'un droit enfant calculé, il faut rajouter l'âge de début de formation à la date de
        // naissance pour la fin d'un droit type enfant et capable exercer, 20
        // ans à la date d naissance pour un enfant incapable exercer et 25 ans
        // pour un droit
        // type formation

        // seulement exécuter si type droit est enfant ou formation
        if (JadeStringUtil.equals(ALCSDroit.TYPE_ENF, droitComplexModel.getDroitModel().getTypeDroit(), false)
                || JadeStringUtil.equals(ALCSDroit.TYPE_FORM, droitComplexModel.getDroitModel().getTypeDroit(), false)) {
            if (!JadeDateUtil.isGlobazDate(droitComplexModel.getEnfantComplexModel().getPersonneEtendueComplexModel()
                    .getPersonne().getDateNaissance())) {
                throw new ALEcheanceModelException("DatesEcheanceServiceImpl#getDateFinValiditeDroitCalcule : "
                        + droitComplexModel.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                                .getDateNaissance() + " is not a valid globaz  Date");
            }
            if (JadeStringUtil.isEmpty(droitComplexModel.getDroitModel().getTypeDroit())) {
                throw new ALEcheanceModelException("DatesEcheanceServiceImpl#typeDroit : " + toString()
                        + " is not a valid null");
            }
            if (JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_ENF, true)
                    || JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_FORM,
                            true)) {

                // ajout de l'âge de formation pour un type de droit Enfant pour un capable
                // exercer
                if (JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_ENF, true)
                        && (droitComplexModel.getEnfantComplexModel().getEnfantModel().getCapableExercer())) {
                    dateFinValiditeCalcule = ALFomationUtils.calculEcheanceFormation(droitComplexModel.getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getPersonne().getDateNaissance());

                }// ajout de 20 ans pour un enfant incapable d'exercer
                else if (JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_ENF,
                        true) && (!droitComplexModel.getEnfantComplexModel().getEnfantModel().getCapableExercer())) {
                    dateFinValiditeCalcule = ALDateUtils.getDateAjoutAnneesFinMois(droitComplexModel
                            .getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance(),
                            20);
                }// ajout de 25 ans pour un type de droit Formation
                else if (JadeStringUtil.equals(droitComplexModel.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_FORM,
                        true)) {
                    dateFinValiditeCalcule = ALDateUtils.getDateAjoutAnneesFinMois(droitComplexModel
                            .getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne().getDateNaissance(),
                            25);
                }

            }
        }
        return dateFinValiditeCalcule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.echeances.DatesEcheanceService#getDateFinValiditeDroitCalculeeFormAnticipe(ch.
     * globaz.al.business.models.droit.DroitComplexModel)
     */
    @Override
    public String getDateFinValiditeDroitCalculeeFormAnticipe(DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException {

        DroitComplexModel copie = ALServiceLocator.getDroitComplexModelService().copie(droit);
        copie.getDroitModel().setTypeDroit(ALCSDroit.TYPE_ENF);
        copie.getEnfantComplexModel().getEnfantModel().setCapableExercer(true);

        return getDateFinValiditeDroitCalculee(copie);
    }
}
