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
     * V�rification de l'int�grit� business des donn�es
     * 
     * @param lienDossierModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * v�rifie l'int�grit� des donn�es relatives aux codes syst�mes , si non respect�e lance un message sur l'int�grit�
     * de(s) donn�e(s)
     * 
     * @param lienDossierModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * v�rifie l'int�grit� des donn�es relatives � la base de donn�es , si non respect�e lance un message sur
     * l'int�grit� de(s) donn�e(s)
     * 
     * @param lienDossierModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(LienDossierModel lienDossierModel) throws JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id du dossier p�re
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
     * V�rification de l'int�grit� des donn�es pour la suppression d'un lien
     * 
     * @param lienDossierModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkDeleteIntegrity(LienDossierModel lienDossierModel) throws JadePersistenceException,
            JadeApplicationException {

    }

    /**
     * v�rifie l'obligation des donn�es, si non respect�e lance un message sur l'obligation de(s) donn�e(s)
     * 
     * @param lienDossierModel
     *            Mod�le � valider
     */
    private static void checkMandatory(LienDossierModel lienDossierModel) {

        // id du dossier p�re
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
     * validation de l'int�grit� des donn�es et des r�gles m�tier, de l'obligation des donn�es et de l'int�grit� des
     * codes syst�mes
     * 
     * @param lienDossierModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(LienDossierModel lienDossierModel) throws JadePersistenceException,
            JadeApplicationException {
        LienDossierModelChecker.checkMandatory(lienDossierModel);
        LienDossierModelChecker.checkDatabaseIntegrity(lienDossierModel);
        LienDossierModelChecker.checkCodesystemIntegrity(lienDossierModel);
        LienDossierModelChecker.checkBusinessIntegrity(lienDossierModel);
    }

    /**
     * Valide l'int�grit� des donn�es avant suppression
     * 
     * @param lienDossierModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validateForDelete(LienDossierModel lienDossierModel) throws JadePersistenceException,
            JadeApplicationException {
        LienDossierModelChecker.checkDeleteIntegrity(lienDossierModel);
    }
}
