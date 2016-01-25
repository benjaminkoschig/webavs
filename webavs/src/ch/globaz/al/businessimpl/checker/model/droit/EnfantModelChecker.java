package ch.globaz.al.businessimpl.checker.model.droit;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.exceptions.model.droit.ALEnfantModelException;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.prestation.DetailPrestationModelChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALImportUtils;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe de vérification des données de la classe <code>EnfantModel</code>
 * 
 * @author PTA
 * @see ch.globaz.al.business.models.droit.EnfantModel
 */
public abstract class EnfantModelChecker extends ALAbstractChecker {

    /**
     * Vérification de l'integrité métier des données
     * 
     * @param enfantModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(EnfantModel enfantModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérification de la validité du pays
        PaysSearchSimpleModel pssm = new PaysSearchSimpleModel();
        pssm.setForIdPays(enfantModel.getIdPaysResidence());
        TIBusinessServiceLocator.getAdresseService().findPays(pssm);
        if (pssm.getSize() == 0) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.idPaysResidence.businessIntegrity.existingId");
        }

        // Vérification de l'existence de l'identifiant du tiers
        PersonneEtendueSearchComplexModel ts = new PersonneEtendueSearchComplexModel();
        ts.setForIdTiers(enfantModel.getIdTiersEnfant());
        if (0 == TIBusinessServiceLocator.getPersonneEtendueService().count(ts)) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.idTiers.businessIntegrity.existingId");

        }

        // si le pays de résidence est la Suisse, le canton de résidence est
        // obligatoire (vide autorisé si importation de données ALFA-Gest)

        if (!ALImportUtils.importFromAlfaGest) {
            if (ALCSPays.PAYS_SUISSE.equals(enfantModel.getIdPaysResidence())
                    && JadeStringUtil.isEmpty(enfantModel.getCantonResidence())) {
                JadeThread.logError(EnfantModelChecker.class.getName(),
                        "al.droit.enfantModel.cantonResidence.businessIntegrity.mandatoryIfSuisse");

            }
        }

        // Si l'enfant (déjà existant) a déjà eu une prestation naissance
        // versée, on écrase pas
        // les données naissance en cas de mise à jour
        // if (!enfantModel.isNew()) {
        // EnfantModel enfantDB = new EnfantModel();
        // enfantDB = ALImplServiceLocator.getEnfantModelService().read(
        // enfantModel.getId());
        //
        // if (enfantDB.getAllocationNaissanceVersee().booleanValue()
        // && (!enfantDB.getMontantAllocationNaissanceFixe().equals(
        // enfantModel.getMontantAllocationNaissanceFixe())
        // || !enfantDB.getTypeAllocationNaissance().equals(
        // enfantModel.getTypeAllocationNaissance()) || !enfantModel
        // .getAllocationNaissanceVersee().booleanValue())) {
        // JadeThread
        // .logError(EnfantModelChecker.class.getName(),
        // "al.droit.enfantModel.allocationNaissanceVersee.businessIntegrity.readOnly");
        //
        // }
        // }

    }

    /**
     * Vérification de l'integrité des codes système
     * 
     * @param enfantModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(EnfantModel enfantModel) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }
        try {
            // type d'allocation de naissance
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDroit.GROUP_NAISSANCE_TYPE,
                    enfantModel.getTypeAllocationNaissance())) {
                JadeThread.logError(EnfantModelChecker.class.getName(),
                        "al.droit.enfantModel.typeAllocationNaissance.codesystemIntegrity");
            }

            // canton de résidence
            if (!JadeNumericUtil.isEmptyOrZero(enfantModel.getCantonResidence())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSCantons.GROUP_CANTONS,
                            enfantModel.getCantonResidence())) {
                JadeThread.logError(EnfantModelChecker.class.getName(),
                        "al.droit.enfantModel.cantonResidence.codesystemIntegrity");
            }

        } catch (Exception e) {

            throw new ALEnfantModelException("DroitModelChecker problem during checking codes system integrity", e);
        }

    }

    /**
     * Vérification de l'intégrité des données
     * 
     * @param enfantModel
     *            Modèle à valider
     */
    private static void checkDatatbaseIntegrity(EnfantModel enfantModel) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id tiers
        if (!JadeNumericUtil.isIntegerPositif(enfantModel.getIdTiersEnfant())) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.idTiers.databaseIntegrity.type");
        }

        // pays de résidence
        if (!JadeNumericUtil.isIntegerPositif(enfantModel.getIdPaysResidence())) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.idPaysResidence.databaseIntegrity.type");
        }

        // canton de résidence
        if (!JadeStringUtil.isEmpty(enfantModel.getCantonResidence())
                && !JadeNumericUtil.isInteger(enfantModel.getCantonResidence())) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.cantonResidence.databaseIntegrity.type");
        }

        // type de l'allocation de naissance
        if (!JadeNumericUtil.isIntegerPositif(enfantModel.getTypeAllocationNaissance())) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.typeAllocationNaissance.databaseIntegrity.type");
        }

        // montant forcé de l'allocation de naissance
        if (!JadeStringUtil.isEmpty(enfantModel.getMontantAllocationNaissanceFixe())
                && !JadeNumericUtil.isNumeric(enfantModel.getMontantAllocationNaissanceFixe())) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.montantNaissanceFixe.databaseIntegrity.type");
        }

    }

    /**
     * Vérification de l'integrité métier des données avant suppression
     * 
     * @param enfantModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkDeleteIntegrity(EnfantModel enfantModel) throws JadePersistenceException,
            JadeApplicationException {

        DroitSearchModel ds = new DroitSearchModel();
        ds.setForIdEnfant(enfantModel.getIdEnfant());
        // L'enfant peut-être supprimé que si il n'est pas utilisé dans d'autres
        // droits
        if (1 < ALImplServiceLocator.getDroitModelService().count(ds)) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.droit.enfantModel.idEnfant.businessIntegrity.hasDroit");
        }
    }

    /**
     * Validation des données obligatoire pour un enfant
     * 
     * @param enfantModel
     *            Modèle à valider
     */
    private static void checkMandatory(EnfantModel enfantModel) {

        // id du tiers
        if (JadeStringUtil.isEmpty(enfantModel.getIdTiersEnfant())) {
            JadeThread.logError(EnfantModelChecker.class.getName(), "al.droit.enfantModel.idTiers.mandatory");
        }

        // pays de résidence
        if (JadeStringUtil.isEmpty(enfantModel.getIdPaysResidence())) {
            JadeThread.logError(EnfantModelChecker.class.getName(), "al.droit.enfantModel.idPaysResidence.mandatory");
        }

        // capacité d'exercer
        if (null == enfantModel.getCapableExercer()) {
            JadeThread.logError(EnfantModelChecker.class.getName(), "al.droit.enfantModel.capableExercer.mandatory");
        }

        // type de l'allocation de naissance
        if (JadeStringUtil.isEmpty(enfantModel.getTypeAllocationNaissance())) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.typeAllocationNaissance.mandatory");
        }

        // allocation de naissance
        if (null == enfantModel.getAllocationNaissanceVersee()) {
            JadeThread.logError(EnfantModelChecker.class.getName(),
                    "al.droit.enfantModel.allocationNaissanceVersee.mandatory");
        }
    }

    /**
     * Validation des données
     * 
     * @param enfantModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException {
        EnfantModelChecker.checkMandatory(enfantModel);
        EnfantModelChecker.checkDatatbaseIntegrity(enfantModel);
        EnfantModelChecker.checkCodesystemIntegrity(enfantModel);
        EnfantModelChecker.checkBusinessIntegrity(enfantModel);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param enfantModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validateForDelete(EnfantModel enfantModel) throws JadePersistenceException,
            JadeApplicationException {
        EnfantModelChecker.checkDeleteIntegrity(enfantModel);
    }
}
