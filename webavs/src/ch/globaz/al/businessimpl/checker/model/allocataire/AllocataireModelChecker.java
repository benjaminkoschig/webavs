package ch.globaz.al.businessimpl.checker.model.allocataire;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSPays;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireModelException;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.allocataire.RevenuSearchModel;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALImportUtils;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * classe de validation des données de AllocataireModel
 * 
 * @author PTA
 * 
 */
public abstract class AllocataireModelChecker extends ALAbstractChecker {

    /**
     * Vérification de l'intégrité business des données
     * 
     * @param allocataireModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérification du pays de résidence
        PaysSearchSimpleModel pssm = new PaysSearchSimpleModel();
        pssm.setForIdPays(allocataireModel.getIdPaysResidence());
        TIBusinessServiceLocator.getAdresseService().findPays(pssm);
        if (pssm.getSize() == 0) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idPaysResidence.businessIntegrity.existingId");
        }

        // Vérification de l'id du tiers allocataire
        PersonneEtendueSearchComplexModel ts = new PersonneEtendueSearchComplexModel();
        ts.setForIdTiers(allocataireModel.getIdTiersAllocataire());
        ts = TIBusinessServiceLocator.getPersonneEtendueService().find(ts);
        if (0 == ts.getSize()) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idTiersAllocataire.businessIntegrity.existingId");
            // vérification de la présence de permis si nationalité n'est pas
            // Suisse
        } else if (!ALCSPays.PAYS_SUISSE.equals(((PersonneEtendueComplexModel) ts.getSearchResults()[0]).getTiers()
                .getIdPays()) && JadeStringUtil.isEmpty(allocataireModel.getPermis())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.permis.businessIntegrity.mandatoryIfNotSuisse");
        }

        // si le pays de résidence est la Suisse, le canton de résidence est
        // obligatoire (vide autorisé si importation de données ALFA-Gest)
        if (!ALImportUtils.importFromAlfaGest) {
            if (ALCSPays.PAYS_SUISSE.equals(allocataireModel.getIdPaysResidence())
                    && JadeStringUtil.isEmpty(allocataireModel.getCantonResidence())) {
                JadeThread.logError(AllocataireModelChecker.class.getName(),
                        "al.allocataire.allocataireModel.cantonResidence.businessIntegrity.mandatoryIfSuisse");
            }
        }
        // si le pays de résidence est autre que la suisse, il ne doit pas avoir de canton de résidence
        if (!JadeStringUtil.equals(ALCSPays.PAYS_SUISSE, allocataireModel.getIdPaysResidence(), false)
                && !JadeStringUtil.isBlankOrZero(allocataireModel.getCantonResidence())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.cantonResidence.businessIntegrity.nullIfNoSuisse");
        }
    }

    /**
     * vérifie l'intégrité des données relatives aux codes systèmes , si non respectée lance un message sur l'intégrité
     * de(s) donnée(s)
     * 
     * @param allocataireModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodesystemIntegrity(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            // canton de résidence
            if (!JadeNumericUtil.isEmptyOrZero(allocataireModel.getCantonResidence())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSCantons.GROUP_CANTONS,
                            allocataireModel.getCantonResidence())) {
                JadeThread.logError(AllocataireModelChecker.class.getName(),
                        "al.allocataire.allocataireModel.cantonResidence.codesystemIntegrity");
            }

            // permis
            if (!JadeNumericUtil.isEmptyOrZero(allocataireModel.getPermis())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSAllocataire.GROUP_ALLOCATAIRE_PERMIS,
                            allocataireModel.getPermis())) {
                JadeThread.logError(AllocataireModelChecker.class.getName(),
                        "al.allocataire.allocataireModel.permis.codesystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALAllocataireModelException(
                    "AllocataireModelChecker#checkCodesystemIntegrity : unable to check codes system", e);
        }

    }

    /**
     * vérifie l'intégrité des données relatives à la base de données , si non respectée lance un message sur
     * l'intégrité de(s) donnée(s)
     * 
     * @param allocataireModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(AllocataireModel allocataireModel) throws JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id du tiers allocataire
        if (!JadeNumericUtil.isIntegerPositif(allocataireModel.getIdTiersAllocataire())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idTiersAllocataire.databaseIntegrity.type");
        }

        // pays de résidence
        if (!JadeNumericUtil.isIntegerPositif(allocataireModel.getIdPaysResidence())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idPaysResidence.databaseIntegrity.type");
        }

        // canton de résidence
        if (!JadeNumericUtil.isEmptyOrZero(allocataireModel.getCantonResidence())
                && !JadeNumericUtil.isIntegerPositif(allocataireModel.getCantonResidence())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.cantonResidence.databaseIntegrity.type");
        }

        // permis
        if (!JadeNumericUtil.isEmptyOrZero(allocataireModel.getPermis())
                && !JadeNumericUtil.isIntegerPositif(allocataireModel.getPermis())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.permis.databaseIntegrity.type");
        }
    }

    /**
     * Vérification de l'intégrité des données pour la suppression d'un allocataire
     * 
     * @param allocataireModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkDeleteIntegrity(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {

        // vérifie que l'allocataire n'ait pas de données agricoles
        AgricoleSearchModel as = new AgricoleSearchModel();
        as.setForIdAllocataire(allocataireModel.getIdAllocataire());
        if (ALImplServiceLocator.getAgricoleModelService().count(as) != 0) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idAllocataire.deleteIntegrity.hasAgricole");
        }

        // vérifie que l'allocataire n'ait pas de données de revenu
        RevenuSearchModel rs = new RevenuSearchModel();
        rs.setForIdAllocataire(allocataireModel.getIdAllocataire());
        if (ALImplServiceLocator.getRevenuModelService().count(rs) != 0) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idAllocataire.deleteIntegrity.hasRevenu");

        }

        // vérifie que l'allocataire ne soit pas utilisé dans un autre dossier
        DossierFkSearchModel sd = new DossierFkSearchModel();
        sd.setForIdAllocataire(allocataireModel.getIdAllocataire());
        if (ALImplServiceLocator.getDossierFkModelService().count(sd) > 1) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idAllocataire.deleteIntegrity.hasDossier");
        }
    }

    /**
     * vérifie l'obligation des données, si non respectée lance un message sur l'obligation de(s) donnée(s)
     * 
     * @param allocataireModel
     *            Modèle à valider
     */
    private static void checkMandatory(AllocataireModel allocataireModel) {

        // id du tiers allocataire
        if (JadeStringUtil.isEmpty(allocataireModel.getIdTiersAllocataire())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idTiersAllocataire.mandatory");
        }

        // pays de résidence
        if (JadeStringUtil.isEmpty(allocataireModel.getIdPaysResidence())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idPaysResidence.mandatory");
        }
    }

    /**
     * validation de l'intégrité des données et des règles métier, de l'obligation des données et de l'intégrité des
     * codes systèmes
     * 
     * @param allocataireModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {
        AllocataireModelChecker.checkMandatory(allocataireModel);
        AllocataireModelChecker.checkDatabaseIntegrity(allocataireModel);
        AllocataireModelChecker.checkCodesystemIntegrity(allocataireModel);
        AllocataireModelChecker.checkBusinessIntegrity(allocataireModel);
    }

    /**
     * Valide l'intégrité des données avant suppression
     * 
     * @param allocataireModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validateForDelete(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {
        AllocataireModelChecker.checkDeleteIntegrity(allocataireModel);
    }
}