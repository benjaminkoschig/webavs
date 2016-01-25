package ch.globaz.al.businessimpl.checker.model.adi;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.model.adi.ALDecompteAdiModelException;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.adi.DecompteAdiSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.droit.DroitModelChecker;
import ch.globaz.al.businessimpl.checker.model.prestation.EntetePrestationModelChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALImportUtils;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * classe de validation du modèle de décompte ADI
 * 
 * @author PTA
 * 
 */
public class DecompteAdiModelChecker extends ALAbstractChecker {

    /** Longueur maximal du commentaire d'un décompte */
    private static final int COMMENT_MAX_LENGTH = 900;

    /**
     * vérification des règles métier
     * 
     * @param decompteAdiModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(DecompteAdiModel decompteAdiModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // intégrité de la date du début (mois antérieur) et de la date de
        // fin(mois postérieur)
        if (JadeDateUtil.isDateMonthYearBefore(decompteAdiModel.getPeriodeFin(), decompteAdiModel.getPeriodeDebut())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.dateDebutDateFin.businessIntegrity");
        }

        String anneeDebut = decompteAdiModel.getPeriodeDebut().substring(3);
        String anneeFin = decompteAdiModel.getPeriodeFin().substring(3);
        // vérification que la période de début est dans la même année que le décompte
        if (!anneeDebut.equals(decompteAdiModel.getAnneeDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeDebutEtAnnee.businessIntegrity",
                    new String[] { String.valueOf(decompteAdiModel.getAnneeDecompte()) });
        }
        // vérification que la période de fin est dans la même année que le décompte
        if (!anneeFin.equals(decompteAdiModel.getAnneeDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeFinEtAnnee.businessIntegrity",
                    new String[] { String.valueOf(decompteAdiModel.getAnneeDecompte()) });
        }

        // vérification de l'existence du décompte remplacé
        DecompteAdiSearchModel decompteSearch = new DecompteAdiSearchModel();
        decompteSearch.setForIdDecompteAdi(decompteAdiModel.getIdDecompteRemplace());

        if (!JadeNumericUtil.isEmptyOrZero(decompteAdiModel.getIdDecompteRemplace())
                && (0 == ALServiceLocator.getDecompteAdiModelService().count(decompteSearch))) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idDecompteRemplace.businessIntegrity.ExistingId");
        }

        // vérification de l'existence de l'en-tête de prestation
        if (JadeNumericUtil.isIntegerPositif(decompteAdiModel.getIdPrestationAdi())) {

            EntetePrestationSearchModel ea = new EntetePrestationSearchModel();
            ea.setForIdEntete(decompteAdiModel.getIdPrestationAdi());
            if ((0 == ALImplServiceLocator.getEntetePrestationModelService().count(ea))) {

                // Dans le cas de l'importation depuis ALFA-Gest il se peut
                // qu'il
                // reste des liens vers des prestations temporaires. Si c'est le
                // cas, on supprime ce lien à la condition qu'un décompte de
                // remplacement existe
                if (ALImportUtils.importFromAlfaGest) {
                    if (JadeNumericUtil.isEmptyOrZero(decompteAdiModel.getIdDecompteRemplace())) {
                        JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                                "al.adi.decompteAdiModel.idPrestationAdi.businessIntegrity.ExistingIdImport");
                    } else {
                        decompteAdiModel.setIdPrestationAdi(null);
                    }
                } else {
                    JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                            "al.adi.decompteAdiModel.idPrestationAdi.businessIntegrity.ExistingId");
                }
            }
        }

        AdministrationSearchComplexModel ascm = new AdministrationSearchComplexModel();
        ascm.setForIdTiersAdministration(decompteAdiModel.getIdTiersOrganismeEtranger());

        if (TIBusinessServiceLocator.getAdministrationService().count(ascm) == 0) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idTiersOrganismeEtranger.businessIntegrity.ExistingId");

        }
    }

    /**
     * Vérifie que les codes système appartiennent à la famille de code attendue
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(DecompteAdiModel model) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            // code monnaie
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTiers.GROUP_MONNAIE, model.getCodeMonnaie())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.adi.decompteAdiModel.etatDecompte.codesystemIntegrity");
            }

            // état du décompte
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, model.getEtatDecompte())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.adi.decompteAdiModel.etatDecompte.codesystemIntegrity");
            }

        } catch (Exception e) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiModelChecker#checkCodesystemIntegrity : unable to check code system integrity", e);
        }
    }

    /**
     * vérification de l'intégrité des données relatives à la base
     * 
     * @param model
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(DecompteAdiModel model) {

        // id du dossier
        if (!JadeNumericUtil.isIntegerPositif(model.getIdDossier())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idDossier.databaseIntegrity.type");
        }

        // id de l'organisme étranger
        if (!JadeNumericUtil.isIntegerPositif(model.getIdTiersOrganismeEtranger())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idTiersOrganismeEtranger.databaseIntegrity.type");
        }

        // code de la monnaie
        if (!JadeNumericUtil.isIntegerPositif(model.getIdTiersOrganismeEtranger())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idTiersOrganismeEtranger.databaseIntegrity.type");
        }

        // année du décompte
        if (!JadeDateUtil.isGlobazDateYear(model.getAnneeDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.AnneeDecompte.databaseIntegrity.type");
        }

        // date de réception
        if (!JadeStringUtil.isEmpty(model.getDateReception()) && !JadeDateUtil.isGlobazDate(model.getDateReception())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.dateReception.databaseIntegrity.type");

        }

        // date d'état
        if (!JadeStringUtil.isEmpty(model.getDateEtat()) && !JadeDateUtil.isGlobazDate(model.getDateEtat())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.dateEtat.databaseIntegrity.type");

        }

        // état du décompte
        if (!JadeNumericUtil.isIntegerPositif(model.getEtatDecompte())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.etatDecompte.databaseIntegrity.type");
        }

        // période de début
        if (!JadeDateUtil.isGlobazDateMonthYear(model.getPeriodeDebut())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeDebut.databaseIntegrity.type");
        }

        // période de fin
        if (!JadeDateUtil.isGlobazDateMonthYear(model.getPeriodeFin())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeFin.databaseIntegrity.type");
        }

        // texte libre
        if (!JadeStringUtil.isEmpty(model.getTexteLibre())
                && (model.getTexteLibre().length() > DecompteAdiModelChecker.COMMENT_MAX_LENGTH)) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.texteLibre.databaseIntegrity.length",
                    new String[] { String.valueOf(DecompteAdiModelChecker.COMMENT_MAX_LENGTH) });
        }

        // id du décompte de remplacement
        if (!JadeNumericUtil.isEmptyOrZero(model.getIdDecompteRemplace())
                && !JadeNumericUtil.isIntegerPositif(model.getIdDecompteRemplace())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idDecompteRemplace.databaseIntegrity.type");
        }

        // id de la prestation
        if (!JadeNumericUtil.isEmptyOrZero(model.getIdPrestationAdi())
                && !JadeNumericUtil.isIntegerPositif(model.getIdPrestationAdi())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idPrestationAdi.databaseIntegrity.type");
        }
    }

    /**
     * vérification pour la suppression
     * 
     * @param decompteAdiModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        if (!ALCSPrestation.ETAT_PR.equals(decompteAdiModel.getEtatDecompte())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idDecompteAdi.deleteIntegrity.etatNonPr");

        }

    }

    /**
     * vérification des paramètres requis
     * 
     * @param model
     *            Modèle à valider
     */
    private static void checkMandatory(DecompteAdiModel model) {

        // id du dossier
        if (JadeStringUtil.isEmpty(model.getIdDossier())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(), "al.adi.decompteAdiModel.idDossier.mandatory");
        }

        // id de l'organisme étranger
        if (JadeStringUtil.isEmpty(model.getIdTiersOrganismeEtranger())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idTiersOrganismeEtranger.mandatory");
        }

        // code de la monnaie
        if (JadeStringUtil.isEmpty(model.getCodeMonnaie())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.codeMonnaie.mandatory");
        }

        // année du décompte
        if (JadeStringUtil.isEmpty(model.getAnneeDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.AnneeDecompte.mandatory");
        }

        // état du décompte
        if (JadeStringUtil.isEmpty(model.getEtatDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.etatDecompte.mandatory");
        }

        // période de début
        if (JadeStringUtil.isEmpty(model.getPeriodeDebut())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeDebut.mandatory");
        }

        // période de fin
        if (JadeStringUtil.isEmpty(model.getPeriodeFin())) {
            JadeThread
                    .logError(DecompteAdiModelChecker.class.getName(), "al.adi.decompteAdiModel.periodeFin.mandatory");
        }

        // id du dossier
        if (JadeStringUtil.isEmpty(model.getIdDossier())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(), "al.adi.decompteAdiModel.idDossier.mandatory");
        }
    }

    /**
     * Validation de l'obligation des données, de l'intégrité des données et des règles métier d'un modèle de décompte
     * ADI
     * 
     * @param decompteAdiModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(DecompteAdiModel decompteAdiModel) throws JadePersistenceException,
            JadeApplicationException {
        DecompteAdiModelChecker.checkMandatory(decompteAdiModel);
        DecompteAdiModelChecker.checkDatabaseIntegrity(decompteAdiModel);
        DecompteAdiModelChecker.checkCodesystemIntegrity(decompteAdiModel);
        DecompteAdiModelChecker.checkBusinessIntegrity(decompteAdiModel);
    }

    /**
     * Validation de la suppression des données d'un modèle de décompte ADI
     * 
     * @param decompteAdiModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException {
        DecompteAdiModelChecker.checkDeleteIntegrity(decompteAdiModel);
    }
}
