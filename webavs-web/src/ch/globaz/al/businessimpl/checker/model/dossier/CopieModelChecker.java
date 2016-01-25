package ch.globaz.al.businessimpl.checker.model.dossier;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.dossier.CopieModel;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSearchSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe de vérification des données de <code>CopieModel</code>
 * 
 * @author jts
 * 
 */
public abstract class CopieModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégrité métier des données d'une copie
     * 
     * @param copieModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(CopieModel copieModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérifie que l'id dossier existe
        DossierFkSearchModel sd = new DossierFkSearchModel();
        sd.setForIdDossier(copieModel.getIdDossier());
        if (0 == ALImplServiceLocator.getDossierFkModelService().count(sd)) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.dossier.copieModel.idDossier.businessIntegrity.existingId");
        }

        // vérification de l'existance du tiers
        TiersSearchSimpleModel ts = new TiersSearchSimpleModel();
        ts.setForIdTiers(copieModel.getIdTiersDestinataire());
        if (0 == JadePersistenceManager.count(ts)) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.dossier.copieModel.idTiersdestinataire.businessIntegrity.existingId");
        }

        // vérifie que l'adresse du tiers destinataire existe pour tous les
        // copies dont l'ordre est supérieur à 2
        if (Integer.parseInt(copieModel.getOrdreCopie()) > 2) {

            AdresseTiersDetail adresseTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                    copieModel.getIdTiersDestinataire(), true, JadeDateUtil.getGlobazFormattedDate(new Date()),
                    ALCSTiers.DOMAINE_AF, AdresseService.CS_TYPE_COURRIER, "");

            String adresse = adresseTiers.getAdresseFormate();

            if (JadeStringUtil.isEmpty(adresse)) {
                JadeThread.logError(CopieModelChecker.class.getName(),
                        "al.dossier.copieModel.idTiersDestinataire.adresse.businessIntegrity.existingAdresse");
            }

        }

    }

    /**
     * Vérifie l'intégrité des codes Systèmes
     * 
     * @param copieModel
     *            modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodesystemIntegrity(CopieModel copieModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }
        // type de copie
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSCopie.GROUP_COPIE_TYPE, copieModel.getTypeCopie())) {
                JadeThread.logError(CopieModelChecker.class.getName(),
                        "al.dossier.copieModel.typeCopie.codesystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALDossierModelException("CopieModelChecker problem during checking codes system integrity", e);
        }

    }

    /**
     * Vérifie que toutes les données aient le format attendu par la base de données
     * 
     * @param copieModel
     *            Modèle à valider Modèle à valider
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkDatabaseIntegrity(CopieModel copieModel) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id du dossier
        if (!JadeNumericUtil.isIntegerPositif(copieModel.getIdDossier())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.dossier.copieModel.idDossier.databaseIntegrity.type");
        }

        // type de copie
        if (!JadeNumericUtil.isIntegerPositif(copieModel.getTypeCopie())) {
            JadeThread.logError(CopieModelChecker.class.getName(), "al.dossier.copieModel.typeCopie.databaseIntegrity");
        }

        // ordre
        if (!JadeNumericUtil.isIntegerPositif(copieModel.getOrdreCopie())) {
            JadeThread
                    .logError(CopieModelChecker.class.getName(), "al.dossier.copieModel.ordreCopie.databaseIntegrity");
        }

        // id tiers
        if (!JadeNumericUtil.isIntegerPositif(copieModel.getIdTiersDestinataire())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.dossier.copieModel.idTiersDestinataire.databaseIntegrity");
        }
    }

    /**
     * Vérifie si tous les paramètres requis ont été indiqués
     * 
     * @param copieModel
     *            Modèle à valider Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException {

        // id du dossier
        if (JadeStringUtil.isEmpty(copieModel.getIdDossier())) {
            JadeThread.logError(CopieModelChecker.class.getName(), "al.dossier.copieModel.idDossier.mandatory");
        }

        // type de copie obligatoire
        if (JadeStringUtil.isEmpty(copieModel.getTypeCopie())) {
            JadeThread.logError(CopieModelChecker.class.getName(), "al.dossier.copieModel.typeCopie.mandatory");
        }

        // ordre
        if (JadeStringUtil.isEmpty(copieModel.getOrdreCopie())) {
            JadeThread.logError(CopieModelChecker.class.getName(), "al.dossier.copieModel.ordreCopie.mandatory");
        }

        // id tiers
        if (JadeStringUtil.isEmpty(copieModel.getIdTiersDestinataire())) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.dossier.copieModel.idTiersDestinataire.mandatory");
        }
    }

    /**
     * Exécute les vérifications nécessaire avant l'enregistrement du modèle
     * 
     * @param copieModel
     *            Modèle à valider Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException {

        CopieModelChecker.checkMandatory(copieModel);
        CopieModelChecker.checkDatabaseIntegrity(copieModel);
        CopieModelChecker.checkCodesystemIntegrity(copieModel);
        CopieModelChecker.checkBusinessIntegrity(copieModel);
    }
}
