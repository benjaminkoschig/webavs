package ch.globaz.al.businessimpl.checker.model.dossier;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.dossier.DossierSearchModel;
import ch.globaz.al.business.models.dossier.LienDossierModel;
import ch.globaz.al.business.models.dossier.LienDossierSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.allocataire.AllocataireModelChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

public class LienDossierModelChecker extends ALAbstractChecker {

    /**
     * Vérification de l'intégrité business des données
     * 
     * @param lienDossierModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(LienDossierModel lienDossierModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        DossierSearchModel searchDossier = new DossierSearchModel();
        searchDossier.setForIdDossier(lienDossierModel.getIdDossierPere());
        searchDossier = ALServiceLocator.getDossierModelService().search(searchDossier);

        if (0 == searchDossier.getSize()) {
            JadeThread.logError(LienDossierModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.idDossierPere.businessIntegrity.existingId");
        }

        searchDossier.setForIdDossier(lienDossierModel.getIdDossierFils());
        searchDossier = ALServiceLocator.getDossierModelService().search(searchDossier);

        if (0 == searchDossier.getSize()) {
            JadeThread.logError(LienDossierModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.idDossierFils.businessIntegrity.existingId");
        }

        LienDossierSearchModel searchLiens = new LienDossierSearchModel();
        searchLiens.setForIdDossierPere(lienDossierModel.getIdDossierPere());
        searchLiens.setForIdDossierFils(lienDossierModel.getIdDossierFils());
        searchLiens = ALImplServiceLocator.getLienDossierModelService().search(searchLiens);

        if ((searchLiens.getSize() > 0) && lienDossierModel.isNew()) {
            JadeThread.logError(LienDossierModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.businessIntegrity.lienAlreadyExists");
        }
    }

    /**
     * vérifie l'intégrité des données relatives aux codes systèmes , si non respectée lance un message sur l'intégrité
     * de(s) donnée(s)
     * 
     * @param lienDossierModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodesystemIntegrity(LienDossierModel lienDossierModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {

            // type de lien
            if (!JadeNumericUtil.isEmptyOrZero(lienDossierModel.getTypeLien())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_LIEN_DOSSIER,
                            lienDossierModel.getTypeLien())) {
                JadeThread.logError(LienDossierModelChecker.class.getName(),
                        "al.dossier.lienDossierModel.typeLien.codesystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALDossierModelException(
                    "LienDossierModelChecker#checkCodesystemIntegrity : unable to check codes system", e);
        }

    }

    /**
     * vérifie l'intégrité des données relatives à la base de données , si non respectée lance un message sur
     * l'intégrité de(s) donnée(s)
     * 
     * @param lienDossierModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(LienDossierModel lienDossierModel) throws JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id du dossier père
        if (!JadeNumericUtil.isIntegerPositif(lienDossierModel.getIdDossierPere())) {
            JadeThread.logError(LienDossierModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.idDossierPere.databaseIntegrity.type");
        }

        // id du dossier fils
        if (!JadeNumericUtil.isIntegerPositif(lienDossierModel.getIdDossierFils())) {
            JadeThread.logError(LienDossierModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.idDossierFils.databaseIntegrity.type");
        }

        // type de lien
        if (!JadeNumericUtil.isEmptyOrZero(lienDossierModel.getTypeLien())
                && !JadeNumericUtil.isIntegerPositif(lienDossierModel.getTypeLien())) {
            JadeThread.logError(LienDossierModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.typeLien.databaseIntegrity.type");
        }
    }

    /**
     * Vérification de l'intégrité des données pour la suppression d'un lien
     * 
     * @param lienDossierModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkDeleteIntegrity(LienDossierModel lienDossierModel) throws JadePersistenceException,
            JadeApplicationException {

    }

    /**
     * vérifie l'obligation des données, si non respectée lance un message sur l'obligation de(s) donnée(s)
     * 
     * @param lienDossierModel
     *            Modèle à valider
     */
    private static void checkMandatory(LienDossierModel lienDossierModel) {

        // id du dossier père
        if (JadeStringUtil.isEmpty(lienDossierModel.getIdDossierPere())) {
            JadeThread.logError(LienDossierModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.idDossierPere.mandatory");
        }

        // id du dossier fils
        if (JadeStringUtil.isEmpty(lienDossierModel.getIdDossierFils())) {
            JadeThread.logError(LienDossierModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.idDossierFils.mandatory");
        }

        // type de lien
        if (JadeStringUtil.isEmpty(lienDossierModel.getTypeLien())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.dossier.lienDossierModel.typeLien.mandatory");
        }
    }

    /**
     * validation de l'intégrité des données et des règles métier, de l'obligation des données et de l'intégrité des
     * codes systèmes
     * 
     * @param lienDossierModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(LienDossierModel lienDossierModel) throws JadePersistenceException,
            JadeApplicationException {
        LienDossierModelChecker.checkMandatory(lienDossierModel);
        LienDossierModelChecker.checkDatabaseIntegrity(lienDossierModel);
        LienDossierModelChecker.checkCodesystemIntegrity(lienDossierModel);
        LienDossierModelChecker.checkBusinessIntegrity(lienDossierModel);
    }

    /**
     * Valide l'intégrité des données avant suppression
     * 
     * @param lienDossierModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validateForDelete(LienDossierModel lienDossierModel) throws JadePersistenceException,
            JadeApplicationException {
        LienDossierModelChecker.checkDeleteIntegrity(lienDossierModel);
    }
}
