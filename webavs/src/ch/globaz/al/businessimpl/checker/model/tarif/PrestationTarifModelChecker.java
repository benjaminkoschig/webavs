package ch.globaz.al.businessimpl.checker.model.tarif;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALPrestationTarifModelException;
import ch.globaz.al.business.models.tarif.PrestationTarifModel;
import ch.globaz.al.business.models.tarif.PrestationTarifSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validit� des donn�es de PrestationTarifModel
 * 
 * @author PTA
 * 
 */
public class PrestationTarifModelChecker extends ALAbstractChecker {
    /**
     * v�rification des r�gles m�tier
     * 
     * @param model
     *            Mod�le � valider
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(PrestationTarifModel model) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification de la chronologie des dates de d�but et de fin de
        // validit�
        if (!JadeStringUtil.isEmpty(model.getFinValidite())
                && JadeDateUtil.isDateBefore(model.getFinValidite(), model.getDebutValidite())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.dateDebutFin.businessIntegrity.dateChronology");
        }

        // Deux tarifs identiques ne peuvent exister
        PrestationTarifSearchModel pts = new PrestationTarifSearchModel();
        pts.setForCapableExercer(model.getCapableExercer());
        pts.setForCategorieResident(model.getCategorieResident());
        pts.setForDebutValidite(model.getDebutValidite());
        pts.setForFinValidite(model.getFinValidite());
        pts.setForIdCategorieTarif(model.getIdCategorieTarif());
        pts.setForIdCritereAge(model.getIdCritereAge());
        pts.setForIdCritereNombre(model.getIdCritereNombre());
        pts.setForIdCritereRang(model.getIdCritereRang());
        pts.setForIdCritereRevenuIndependant(model.getIdCritereRevenuIndependant());
        pts.setForIdCritereRevenuNonActif(model.getIdCritereRevenuNonActif());
        pts.setForTypePrestation(model.getTypePrestation());
        pts.setForMoisSeparation(model.getMoisSeparation() == null ? "0" : model.getMoisSeparation());

        if (ALImplServiceLocator.getPrestationTarifModelService().count(pts) > 0) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.prestation.businessIntegrity.alreadyExisting");

        }
    }

    /**
     * v�rification des codes syst�me
     * 
     * @param prestationTarifModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodeSystemIntegrity(PrestationTarifModel prestationTarifModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        try {
            // r�sident
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_RESIDENT,
                    prestationTarifModel.getCategorieResident())) {
                JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                        "al.tarif.prestationTarifModel.categorieResident.codeSystemIntegrity");

            }
            // type de prestation
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDroit.GROUP_TYPE,
                    prestationTarifModel.getTypePrestation())) {

                JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                        "al.tarif.prestationTarifModel.typePrestation.codeSystemIntegrity");
            }

        } catch (Exception e) {
            throw new ALPrestationTarifModelException(
                    "PrestationTarifModel problem during checking codes system integrity", e);
        }
    }

    /**
     * v�rification de l'int�grit� des types de donn�es
     * 
     * @param prestationTarifModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(PrestationTarifModel prestationTarifModel)
            throws JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // type de donn�e identifiant du crit�re �ge
        if (!JadeNumericUtil.isIntegerPositif(prestationTarifModel.getIdCritereAge())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereAge.databaseIntegrity.type");
        }
        // type de donn�e identifiant du crit�re nombre
        if (!JadeNumericUtil.isIntegerPositif(prestationTarifModel.getIdCritereNombre())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereNombre.databaseIntegrity.type");
        }
        // type de donn�e identifiant du crit�re rang
        if (!JadeNumericUtil.isIntegerPositif(prestationTarifModel.getIdCritereRang())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereRang.databaseIntegrity.type");
        }
        // type de donn�e identifiant du crit�re de revenu pour un ind�pendant
        if (!JadeNumericUtil.isIntegerPositif(prestationTarifModel.getIdCritereRevenuIndependant())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereRevenuIndependant.databaseIntegrity.type");
        }
        // type de donn�e identifiant du crit�re de revenu pour un ind�pendant
        if (!JadeNumericUtil.isIntegerPositif(prestationTarifModel.getIdCritereRevenuNonActif())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereRevenuNonActif.databaseIntegrity.type");
        }
        // type de donn�e de l'identifiant de la cat�gorie
        if (!JadeNumericUtil.isIntegerPositif(prestationTarifModel.getIdCategorieTarif())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCategorieTarif.databaseIntegrity.type");
        }
        // type de donn�e du montant
        if (!JadeNumericUtil.isNumeric(prestationTarifModel.getMontant())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.montant.databaseIntegrity.type");
        }
        // type de donn�e cat�gorie de r�sident
        if (!JadeNumericUtil.isIntegerPositif(prestationTarifModel.getCategorieResident())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.categorieResident.databaseIntegrity.type");
        }
        // type de donn�e du d�but de la validit�
        if (!JadeDateUtil.isGlobazDate(prestationTarifModel.getDebutValidite())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.debutValidite.databaseIntegrity.dateFormat");
        }
        // type de donn�e de fin de validit�
        if (!JadeStringUtil.isEmpty(prestationTarifModel.getFinValidite())
                && !JadeDateUtil.isGlobazDate(prestationTarifModel.getFinValidite())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.finValidite.databaseIntegrity.dateFormat");

        }

        // type de donn�e mois s�paration
        if (!JadeStringUtil.isEmpty(prestationTarifModel.getMoisSeparation())
                && !JadeNumericUtil.isIntegerPositif(prestationTarifModel.getMoisSeparation())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.moisSeparation.databaseIntegrity.type");

        }

    }

    /**
     * v�rification des donn�es obligatoires
     * 
     * @param prestationTarifModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(PrestationTarifModel prestationTarifModel) throws JadePersistenceException,
            JadeApplicationException {

        // obligation de l'identifiant du crit�re age
        if (JadeStringUtil.isEmpty(prestationTarifModel.getIdCritereAge())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereAge.mandatory");
        }
        // obligation de l'identifiant du crit�re nombre
        if (JadeStringUtil.isEmpty(prestationTarifModel.getIdCritereNombre())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereNombre.mandatory");
        }
        // obligation de l'identifiant du crit�re de revenu pour un ind�pendant
        if (JadeStringUtil.isEmpty(prestationTarifModel.getIdCritereRevenuIndependant())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereRevenuIndependant.mandatory");
        }
        // obligation de l'identifiant du crit�re de revenu pour un non-actif
        if (JadeStringUtil.isEmpty(prestationTarifModel.getIdCritereRevenuNonActif())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereRevenuNonActif.mandatory");
        }
        // obligation de l'identifiant du crit�re rang
        if (JadeStringUtil.isEmpty(prestationTarifModel.getIdCritereRang())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCritereRang.mandatory");
        }
        // obligation de l'identifiant de la cat�gorie
        if (JadeStringUtil.isEmpty(prestationTarifModel.getIdCategorieTarif())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.idCategorie.mandatory");
        }
        // obligation de capable d'exercer
        if (null == prestationTarifModel.getCapableExercer()) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.capableExercer.mandatory");
        }
        // obligation du montant
        if (JadeStringUtil.isEmpty(prestationTarifModel.getMontant())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.montant.mandatory");
        }
        // obligation de la cat�gorie de r�sident
        if (JadeStringUtil.isEmpty(prestationTarifModel.getCategorieResident())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.categorieResident.mandatory");
        }
        // obligation du d�but de la validit�
        if (JadeStringUtil.isEmpty(prestationTarifModel.getDebutValidite())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.debutValidite.mandatory");
        }
        // obligation
        if (JadeStringUtil.isEmpty(prestationTarifModel.getTypePrestation())) {
            JadeThread.logError(PrestationTarifModelChecker.class.getName(),
                    "al.tarif.prestationTarifModel.typePrestation.mandatory");
        }
    }

    /**
     * validation des donn�es
     * 
     * @param prestationTarifModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(PrestationTarifModel prestationTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        PrestationTarifModelChecker.checkMandatory(prestationTarifModel);
        PrestationTarifModelChecker.checkDatabaseIntegrity(prestationTarifModel);
        PrestationTarifModelChecker.checkCodeSystemIntegrity(prestationTarifModel);
        PrestationTarifModelChecker.checkBusinessIntegrity(prestationTarifModel);
    }

}
