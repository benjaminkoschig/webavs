package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.pyxis.util.CommonNSSFormater;
import java.math.BigInteger;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.allocataire.AllocataireSearchModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALRafamUtils;

/**
 * classe de validation des données de {@link ch.globaz.al.business.models.rafam.AnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class AnnonceRafamModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected static void checkBusinessIntegrity(AnnonceRafamModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérification de l'existence du droit
        if (!ALRafamUtils.modeAlfaGest && !model.getDelegated()) {
            DroitSearchModel rd = new DroitSearchModel();
            rd.setForIdDroit(model.getIdDroit());
            if (0 == ALImplServiceLocator.getDroitModelService().count(rd)) {
                JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                        "al.rafam.annonceRafamModel.idDroit.businessIntegrity.ExistingId");
            }

            // vérification de l'existence de l'allocataire

            AllocataireSearchModel allocR = new AllocataireSearchModel();
            allocR.setForIdAllocataire(model.getIdAllocataire());
            if (0 == ALImplServiceLocator.getAllocataireModelService().count(allocR)) {
                JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                        "al.rafam.annonceRafamModel.idAllocataire.businessIntegrity.ExistingId");
            }
        }

        // vérification de la chronologie entre les dates de début de droit et
        // l'échéance

        if (!JadeStringUtil.isEmpty(model.getDebutDroit())
                && (!JadeStringUtil.isEmpty(model.getEcheanceDroit()) && (JadeDateUtil.isDateAfter(
                        model.getDebutDroit(), model.getEcheanceDroit())))) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.debutDroit.businessIntegrity.dateChronology");
        }

        // NSS de l'enfant
        if (!ALConstRafam.UPI_UNKNOWN_NSS.equals(model.getNssEnfant()) && !isNssValid(model.getNssEnfant())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.nssEnfant.databaseIntegrity.type");
        }

        // Nouveau NSS de l'enfant
        if (!JadeStringUtil.isBlankOrZero(model.getNewNssEnfant())
                && !ALConstRafam.UPI_UNKNOWN_NSS.equals(model.getNewNssEnfant())
                && !isNssValid(model.getNewNssEnfant())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.newNssEnfant.databaseIntegrity.type");
        }

        // NSS de l'allocataire
        if (!JadeNumericUtil.isEmptyOrZero(model.getNssAllocataire())
                && !ALConstRafam.UPI_UNKNOWN_NSS.equals(model.getNssAllocataire())
                && !isNssValid(model.getNssAllocataire())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.nssEnfant.databaseIntegrity.type");
        }

        // Legal Office
        if (!JadeStringUtil.isBlank(model.getLegalOffice()) && !LegalOfficeChecker.isValid(model.getLegalOffice())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.legalOffice.format", new String[] { model.getLegalOffice() });
        }
    }

    private static boolean isNssValid(String nss) {

        CommonNSSFormater nssf = new CommonNSSFormater();

        try {
            nssf.checkNss(nssf.unformat(nss));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param model
     *            Modèle à valider
     * @param type
     *            Type d'annonce
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(AnnonceRafamModel model, RafamTypeAnnonce type)
            throws JadePersistenceException, JadeApplicationException {
        switch (type) {
            case _68A_CREATION:
                AnnonceRafam68aModelChecker.checkBusinessIntegrity(model);
                break;
            case _68B_MUTATION:
                AnnonceRafam68bModelChecker.checkBusinessIntegrity(model);
                break;
            case _68C_ANNULATION:
                AnnonceRafam68cModelChecker.checkBusinessIntegrity(model);
                break;
            case _69B_SYNCHRO_UPI:
                AnnonceRafam69bModelChecker.checkBusinessIntegrity(model);
                break;
            case _69C_REGISTER_STATUS:
                AnnonceRafam69cModelChecker.checkBusinessIntegrity(model);
                break;
            case _69D_NOTICE:
                AnnonceRafam69dModelChecker.checkBusinessIntegrity(model);
                break;
            default:
                throw new ALAnnonceRafamException(
                        "AnnonceRafamModelChecker#getSpecificChecker : this type is not supported");
        }
    }

    /**
     * Vérification des codesSystems
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkCodesystemIntegrity(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            // cs sexe de l'enfant
            if (!JadeNumericUtil.isEmptyOrZero(model.getSexeEnfant())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSAllocataire.GROUP_SEXE, model.getSexeEnfant())) {
                JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                        "al.rafam.annonceRafamModel.sexeEnfant.codesystemIntegrity");
            }

            // cs sexe de l'allocataire
            if (!JadeNumericUtil.isEmptyOrZero(model.getSexeAllocataire())
                    && !JadeCodesSystemsUtil
                            .checkCodeSystemType(ALCSAllocataire.GROUP_SEXE, model.getSexeAllocataire())) {
                JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                        "al.rafam.annonceRafamModel.sexeAllocataire.codesystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALAnnonceRafamException(
                    "annonceRafamModelChecker#checkCodesystemIntegrity : problem during checking codes system integrity",
                    e);
        }

    }

    /**
     * Vérification des codes système spécifique au type d'annonce
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(AnnonceRafamModel model, RafamTypeAnnonce type)
            throws JadeApplicationException, JadePersistenceException {
        switch (type) {
            case _68A_CREATION:
                AnnonceRafam68aModelChecker.checkCodesystemIntegrity(model);
                break;
            case _68B_MUTATION:
                AnnonceRafam68bModelChecker.checkCodesystemIntegrity(model);
                break;
            case _68C_ANNULATION:
                AnnonceRafam68cModelChecker.checkCodesystemIntegrity(model);
                break;
            case _69B_SYNCHRO_UPI:
                AnnonceRafam69bModelChecker.checkCodesystemIntegrity(model);
                break;
            case _69C_REGISTER_STATUS:
                AnnonceRafam69cModelChecker.checkCodesystemIntegrity(model);
                break;
            case _69D_NOTICE:
                AnnonceRafam69dModelChecker.checkCodesystemIntegrity(model);
                break;
            default:
                throw new ALAnnonceRafamException(
                        "AnnonceRafamModelChecker#getSpecificChecker : this type is not supported");
        }
    }

    /**
     * vérification de l'intégrité des données
     * 
     * @param model
     *            Modèle à valider
     */
    protected static void checkDatabaseIntegrity(AnnonceRafamModel model) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // idAnnonce
        if (!JadeNumericUtil.isEmptyOrZero(model.getIdAnnonce())
                && !JadeNumericUtil.isNumericPositif(model.getIdAnnonce())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.idAnnonce.databaseIntegrity.type");
        }

        // idDroit
        if (!model.getDelegated() && !JadeNumericUtil.isIntegerPositif(model.getIdDroit())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.idDroit.databaseIntegrity.type");
        }

        // idAllocataire
        if (!model.getDelegated() && !JadeNumericUtil.isIntegerPositif(model.getIdAllocataire())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.idAllocataire.databaseIntegrity.type");
        }

        // record number
        if (!JadeStringUtil.isBlank(model.getRecordNumber()) && (new BigInteger(model.getRecordNumber()).signum() <= 0)) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.recordNumber.databaseIntegrity.type");
        }

        // Sexe Enfant
        if (!JadeStringUtil.isEmpty(model.getSexeEnfant()) && !JadeNumericUtil.isInteger(model.getSexeEnfant())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.sexeEnfant.databaseIntegrity.type");
        }

        // date naissance enfant
        if (!JadeStringUtil.isEmpty(model.getDateNaissanceEnfant())
                && !JadeDateUtil.isGlobazDate(model.getDateNaissanceEnfant())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.dateNaissanceEnfant.databaseIntegrity.dateFormat");
        }

        // date mort enfant
        if (!JadeStringUtil.isEmpty(model.getDateMortEnfant()) && !JadeDateUtil.isGlobazDate(model.getDateMortEnfant())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.dateMortEnfant.databaseIntegrity.dateFormat");
        }

        // Sexe allocataire
        if (!JadeStringUtil.isEmpty(model.getSexeAllocataire())
                && !JadeNumericUtil.isInteger(model.getSexeAllocataire())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.sexeAllocataire.databaseIntegrity.type");
        }

        // date naissance allocataire
        if (!JadeStringUtil.isEmpty(model.getDateNaissanceAllocataire())
                && !JadeDateUtil.isGlobazDate(model.getDateNaissanceAllocataire())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.dateNaissanceAllocataire.databaseIntegrity.dateFormat");
        }

        // date mort allocataire
        if (!JadeStringUtil.isEmpty(model.getDateMortAllocataire())
                && !JadeDateUtil.isGlobazDate(model.getDateMortAllocataire())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.dateMortAllocataire.databaseIntegrity.dateFormat");
        }

        // date de début de droit
        if (!JadeStringUtil.isEmpty(model.getDebutDroit()) && !JadeDateUtil.isGlobazDate(model.getDebutDroit())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.debutDroit.databaseIntegrity.dateFormat");
        }

        // date d'échéance du droit
        if (!JadeStringUtil.isEmpty(model.getEcheanceDroit()) && !JadeDateUtil.isGlobazDate(model.getEcheanceDroit())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.echeanceDroit.databaseIntegrity.dateFormat");
        }

        // base légale
        if (!JadeStringUtil.isEmpty(model.getBaseLegale()) && !JadeNumericUtil.isInteger(model.getBaseLegale())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.baseLegale.databaseIntegrity.type");
        }

        // statut familial
        if (!JadeStringUtil.isEmpty(model.getCodeStatutFamilial())
                && !JadeNumericUtil.isInteger(model.getCodeStatutFamilial())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.statutFamilial.databaseIntegrity.type");
        }

        // genre prestation
        if (!JadeNumericUtil.isInteger(model.getGenrePrestation())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.genrePrestation.databaseIntegrity.type");
        }

        // type activite
        if (!JadeStringUtil.isEmpty(model.getCodeStatutFamilial())
                && !JadeNumericUtil.isInteger(model.getCodeTypeActivite())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.codeTypeActivite.databaseIntegrity.type");
        }

        // code remarque
        if (!JadeStringUtil.isEmpty(model.getCodeRemarque()) && !JadeNumericUtil.isInteger(model.getCodeRemarque())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.codeRemarque.databaseIntegrity.type");
        }

        // code retour
        if (!JadeNumericUtil.isInteger(model.getCodeRetour())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.codeRetour.databaseIntegrity.type");
        }

        // date de création
        if (!JadeStringUtil.isBlank(model.getDateCreation()) && !JadeDateUtil.isGlobazDate(model.getDateCreation())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.dateCreation.databaseIntegrity.dateFormat");
        }

        // date de mutation
        if (!JadeStringUtil.isBlank(model.getDateMutation()) && !JadeDateUtil.isGlobazDate(model.getDateMutation())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.dateCreation.databaseIntegrity.dateFormat");
        }

        // date réception
        if (!JadeStringUtil.isBlank(model.getDateReception()) && !JadeDateUtil.isGlobazDate(model.getDateReception())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.dateReception.databaseIntegrity.dateFormat");
        }

        // code etat
        if (!JadeNumericUtil.isInteger(model.getEtat())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.etat.databaseIntegrity.type");
        }
    }

    /**
     * vérification de l'intégrité des données spécifique au type d'annonce
     * 
     * @param model
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(AnnonceRafamModel model, RafamTypeAnnonce type)
            throws JadeApplicationException {
        switch (type) {
            case _68A_CREATION:
                AnnonceRafam68aModelChecker.checkDatabaseIntegrity(model);
                break;
            case _68B_MUTATION:
                AnnonceRafam68bModelChecker.checkDatabaseIntegrity(model);
                break;
            case _68C_ANNULATION:
                AnnonceRafam68cModelChecker.checkDatabaseIntegrity(model);
                break;
            case _69B_SYNCHRO_UPI:
                AnnonceRafam69bModelChecker.checkDatabaseIntegrity(model);
                break;
            case _69C_REGISTER_STATUS:
                AnnonceRafam69cModelChecker.checkDatabaseIntegrity(model);
                break;
            case _69D_NOTICE:
                AnnonceRafam69dModelChecker.checkDatabaseIntegrity(model);
                break;
            default:
                throw new ALAnnonceRafamException(
                        "AnnonceRafamModelChecker#getSpecificChecker : this type is not supported");
        }
    }

    /**
     * Vérification de l'intégrité métier avant suppression
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkDeleteIntegrity(AnnonceRafamModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }
    }

    /**
     * vérification des données requises
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkMandatory(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {

        // idDroit
        if (!model.getDelegated() && JadeStringUtil.isEmpty(model.getIdDroit())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.idDroit.mandatory");
        }

        // idAllocataire
        if (!model.getDelegated() && JadeStringUtil.isEmpty(model.getIdAllocataire())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.idAllocataire.mandatory");
        }

        // NssEnfant
        if (JadeStringUtil.isEmpty(model.getNssEnfant())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.nssEnfant.mandatory");
        }

        // Genre d'allocation
        if (JadeStringUtil.isEmpty(model.getGenrePrestation())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.annonceRafamModel.codePrestation.mandatory");
        }
    }

    /**
     * vérification des données requises spécifique au type d'annonce
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(AnnonceRafamModel model, RafamTypeAnnonce type) throws JadeApplicationException,
            JadePersistenceException {
        switch (type) {
            case _68A_CREATION:
                AnnonceRafam68aModelChecker.checkMandatory(model);
                break;
            case _68B_MUTATION:
                AnnonceRafam68bModelChecker.checkMandatory(model);
                break;
            case _68C_ANNULATION:
                AnnonceRafam68cModelChecker.checkMandatory(model);
                break;
            case _69B_SYNCHRO_UPI:
                AnnonceRafam69bModelChecker.checkMandatory(model);
                break;
            case _69C_REGISTER_STATUS:
                AnnonceRafam69cModelChecker.checkMandatory(model);
                break;
            case _69D_NOTICE:
                AnnonceRafam69dModelChecker.checkMandatory(model);
                break;
            default:
                throw new ALAnnonceRafamException(
                        "AnnonceRafamModelChecker#getSpecificChecker : this type is not supported");
        }
    }

    /**
     * validation des données de droitModel
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(AnnonceRafamModel model) throws JadeApplicationException, JadePersistenceException {

        RafamTypeAnnonce type = RafamTypeAnnonce.getRafamTypeAnnonce(model.getTypeAnnonce());

        AnnonceRafamModelChecker.checkMandatory(model);
        AnnonceRafamModelChecker.checkMandatory(model, type);

        AnnonceRafamModelChecker.checkDatabaseIntegrity(model);
        AnnonceRafamModelChecker.checkDatabaseIntegrity(model, type);

        AnnonceRafamModelChecker.checkCodesystemIntegrity(model);
        AnnonceRafamModelChecker.checkCodesystemIntegrity(model, type);

        AnnonceRafamModelChecker.checkBusinessIntegrity(model);
        AnnonceRafamModelChecker.checkBusinessIntegrity(model, type);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        AnnonceRafamModelChecker.checkDeleteIntegrity(model);
    }
}