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
 * classe de validation des donn�es de AllocataireModel
 * 
 * @author PTA
 * 
 */
public abstract class AllocataireModelChecker extends ALAbstractChecker {

    /**
     * V�rification de l'int�grit� business des donn�es
     * 
     * @param allocataireModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification du pays de r�sidence
        PaysSearchSimpleModel pssm = new PaysSearchSimpleModel();
        pssm.setForIdPays(allocataireModel.getIdPaysResidence());
        TIBusinessServiceLocator.getAdresseService().findPays(pssm);
        if (pssm.getSize() == 0) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idPaysResidence.businessIntegrity.existingId");
        }

        // V�rification de l'id du tiers allocataire
        PersonneEtendueSearchComplexModel ts = new PersonneEtendueSearchComplexModel();
        ts.setForIdTiers(allocataireModel.getIdTiersAllocataire());
        ts = TIBusinessServiceLocator.getPersonneEtendueService().find(ts);
        if (0 == ts.getSize()) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idTiersAllocataire.businessIntegrity.existingId");
            // v�rification de la pr�sence de permis si nationalit� n'est pas
            // Suisse
        } else if (!ALCSPays.PAYS_SUISSE.equals(((PersonneEtendueComplexModel) ts.getSearchResults()[0]).getTiers()
                .getIdPays()) && JadeStringUtil.isEmpty(allocataireModel.getPermis())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.permis.businessIntegrity.mandatoryIfNotSuisse");
        }

        // si le pays de r�sidence est la Suisse, le canton de r�sidence est
        // obligatoire (vide autoris� si importation de donn�es ALFA-Gest)
        if (!ALImportUtils.importFromAlfaGest) {
            if (ALCSPays.PAYS_SUISSE.equals(allocataireModel.getIdPaysResidence())
                    && JadeStringUtil.isEmpty(allocataireModel.getCantonResidence())) {
                JadeThread.logError(AllocataireModelChecker.class.getName(),
                        "al.allocataire.allocataireModel.cantonResidence.businessIntegrity.mandatoryIfSuisse");
            }
        }
        // si le pays de r�sidence est autre que la suisse, il ne doit pas avoir de canton de r�sidence
        if (!JadeStringUtil.equals(ALCSPays.PAYS_SUISSE, allocataireModel.getIdPaysResidence(), false)
                && !JadeStringUtil.isBlankOrZero(allocataireModel.getCantonResidence())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.cantonResidence.businessIntegrity.nullIfNoSuisse");
        }
    }

    /**
     * v�rifie l'int�grit� des donn�es relatives aux codes syst�mes , si non respect�e lance un message sur l'int�grit�
     * de(s) donn�e(s)
     * 
     * @param allocataireModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkCodesystemIntegrity(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            // canton de r�sidence
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
     * v�rifie l'int�grit� des donn�es relatives � la base de donn�es , si non respect�e lance un message sur
     * l'int�grit� de(s) donn�e(s)
     * 
     * @param allocataireModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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

        // pays de r�sidence
        if (!JadeNumericUtil.isIntegerPositif(allocataireModel.getIdPaysResidence())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idPaysResidence.databaseIntegrity.type");
        }

        // canton de r�sidence
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
     * V�rification de l'int�grit� des donn�es pour la suppression d'un allocataire
     * 
     * @param allocataireModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkDeleteIntegrity(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {

        // v�rifie que l'allocataire n'ait pas de donn�es agricoles
        AgricoleSearchModel as = new AgricoleSearchModel();
        as.setForIdAllocataire(allocataireModel.getIdAllocataire());
        if (ALImplServiceLocator.getAgricoleModelService().count(as) != 0) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idAllocataire.deleteIntegrity.hasAgricole");
        }

        // v�rifie que l'allocataire n'ait pas de donn�es de revenu
        RevenuSearchModel rs = new RevenuSearchModel();
        rs.setForIdAllocataire(allocataireModel.getIdAllocataire());
        if (ALImplServiceLocator.getRevenuModelService().count(rs) != 0) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idAllocataire.deleteIntegrity.hasRevenu");

        }

        // v�rifie que l'allocataire ne soit pas utilis� dans un autre dossier
        DossierFkSearchModel sd = new DossierFkSearchModel();
        sd.setForIdAllocataire(allocataireModel.getIdAllocataire());
        if (ALImplServiceLocator.getDossierFkModelService().count(sd) > 1) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idAllocataire.deleteIntegrity.hasDossier");
        }
    }

    /**
     * v�rifie l'obligation des donn�es, si non respect�e lance un message sur l'obligation de(s) donn�e(s)
     * 
     * @param allocataireModel
     *            Mod�le � valider
     */
    private static void checkMandatory(AllocataireModel allocataireModel) {

        // id du tiers allocataire
        if (JadeStringUtil.isEmpty(allocataireModel.getIdTiersAllocataire())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idTiersAllocataire.mandatory");
        }

        // pays de r�sidence
        if (JadeStringUtil.isEmpty(allocataireModel.getIdPaysResidence())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireModel.idPaysResidence.mandatory");
        }
    }

    /**
     * validation de l'int�grit� des donn�es et des r�gles m�tier, de l'obligation des donn�es et de l'int�grit� des
     * codes syst�mes
     * 
     * @param allocataireModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {
        AllocataireModelChecker.checkMandatory(allocataireModel);
        AllocataireModelChecker.checkDatabaseIntegrity(allocataireModel);
        AllocataireModelChecker.checkCodesystemIntegrity(allocataireModel);
        AllocataireModelChecker.checkBusinessIntegrity(allocataireModel);
    }

    /**
     * Valide l'int�grit� des donn�es avant suppression
     * 
     * @param allocataireModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validateForDelete(AllocataireModel allocataireModel) throws JadePersistenceException,
            JadeApplicationException {
        AllocataireModelChecker.checkDeleteIntegrity(allocataireModel);
    }
}