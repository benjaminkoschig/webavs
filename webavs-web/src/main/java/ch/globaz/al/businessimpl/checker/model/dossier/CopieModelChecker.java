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
 * Classe de v�rification des donn�es de <code>CopieModel</code>
 * 
 * @author jts
 * 
 */
public abstract class CopieModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit� m�tier des donn�es d'une copie
     * 
     * @param copieModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(CopieModel copieModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rifie que l'id dossier existe
        DossierFkSearchModel sd = new DossierFkSearchModel();
        sd.setForIdDossier(copieModel.getIdDossier());
        if (0 == ALImplServiceLocator.getDossierFkModelService().count(sd)) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.dossier.copieModel.idDossier.businessIntegrity.existingId");
        }

        // v�rification de l'existance du tiers
        TiersSearchSimpleModel ts = new TiersSearchSimpleModel();
        ts.setForIdTiers(copieModel.getIdTiersDestinataire());
        if (0 == JadePersistenceManager.count(ts)) {
            JadeThread.logError(CopieModelChecker.class.getName(),
                    "al.dossier.copieModel.idTiersdestinataire.businessIntegrity.existingId");
        }

        // v�rifie que l'adresse du tiers destinataire existe pour tous les
        // copies dont l'ordre est sup�rieur � 2
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
     * V�rifie l'int�grit� des codes Syst�mes
     * 
     * @param copieModel
     *            mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * V�rifie que toutes les donn�es aient le format attendu par la base de donn�es
     * 
     * @param copieModel
     *            Mod�le � valider Mod�le � valider
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * V�rifie si tous les param�tres requis ont �t� indiqu�s
     * 
     * @param copieModel
     *            Mod�le � valider Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * Ex�cute les v�rifications n�cessaire avant l'enregistrement du mod�le
     * 
     * @param copieModel
     *            Mod�le � valider Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException {

        CopieModelChecker.checkMandatory(copieModel);
        CopieModelChecker.checkDatabaseIntegrity(copieModel);
        CopieModelChecker.checkCodesystemIntegrity(copieModel);
        CopieModelChecker.checkBusinessIntegrity(copieModel);
    }
}
