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
 * classe contenant les sp�cificit�s li�es aux calculs des �ch�ances
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

        // si type droit m�nage
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
            // enfant existe le d�but de validit� commence le d�but du mois
            // suivant la fin du droit enfant
            DroitComplexSearchModel se = new DroitComplexSearchModel();
            se.setWhereKey("echeancesDroit");
            se.setOrderKey("echeancesDroit");
            se.setForIdDossier(droitComplexModel.getDroitModel().getIdDossier());
            se.setForIdEnfant(droitComplexModel.getEnfantComplexModel().getEnfantModel().getIdEnfant());
            se.setForTypeDroit(ALCSDroit.TYPE_ENF);
            se = ALServiceLocator.getDroitComplexModelService().search(se);

            // si aucun r�sultat on initialise le d�but de validit� � la date du
            // d�but du dossier ou � la date du d�but du droit de formation selon la lafam si cette date est post�rieure
            // � la date du d�but d'activit�

            if ((se.getSize() == 0)
                    || JadeNumericUtil.isEmptyOrZero(droitComplexModel.getEnfantComplexModel().getEnfantModel()
                            .getIdEnfant())) {
                // ajouter l'�ge de d�but de formation � la date de naissance et mois suivant et retourner la date
                String datenaissance = droitComplexModel.getEnfantComplexModel()
                        .getPersonneEtendueComplexModel().getPersonne().getDateNaissance();
                String dateDebutValiditeDroitFormation = ALFomationUtils.calculDateDebutFormation(datenaissance);
                // si la date du d�but du dossier est ant�rieure � la date du d�but de la formation on retourne la date
                // du d�but du droit sinon la date du d�but du dossier
                if (JadeDateUtil.isDateAfter(dateDebutValiditeDroitFormation, dateDebutDossier)) {
                    dateDebutValiditeDroit = dateDebutValiditeDroitFormation;
                } else {
                    dateDebutValiditeDroit = dateDebutDossier;
                }

            }

            // ici on r�cup�re le droit le plus r�cent
            else if (se.getSize() != 0) {

                DroitComplexModel newFormationDroit = new DroitComplexModel();
                // r�cup�re le droit le plus r�cent
                newFormationDroit = (DroitComplexModel) se.getSearchResults()[0];
                // d�but de validit� d�but du mois suivant la fin de valitdit�
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

        // fin d'un droit enfant calcul�, il faut rajouter l'�ge de d�but de formation � la date de
        // naissance pour la fin d'un droit type enfant et capable exercer, 20
        // ans � la date d naissance pour un enfant incapable exercer et 25 ans
        // pour un droit
        // type formation

        // seulement ex�cuter si type droit est enfant ou formation
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

                // ajout de l'�ge de formation pour un type de droit Enfant pour un capable
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
