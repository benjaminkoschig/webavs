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
 * classe de validation du mod�le de d�compte ADI
 * 
 * @author PTA
 * 
 */
public class DecompteAdiModelChecker extends ALAbstractChecker {

    /** Longueur maximal du commentaire d'un d�compte */
    private static final int COMMENT_MAX_LENGTH = 900;

    /**
     * v�rification des r�gles m�tier
     * 
     * @param decompteAdiModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(DecompteAdiModel decompteAdiModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // int�grit� de la date du d�but (mois ant�rieur) et de la date de
        // fin(mois post�rieur)
        if (JadeDateUtil.isDateMonthYearBefore(decompteAdiModel.getPeriodeFin(), decompteAdiModel.getPeriodeDebut())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.dateDebutDateFin.businessIntegrity");
        }

        String anneeDebut = decompteAdiModel.getPeriodeDebut().substring(3);
        String anneeFin = decompteAdiModel.getPeriodeFin().substring(3);
        // v�rification que la p�riode de d�but est dans la m�me ann�e que le d�compte
        if (!anneeDebut.equals(decompteAdiModel.getAnneeDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeDebutEtAnnee.businessIntegrity",
                    new String[] { String.valueOf(decompteAdiModel.getAnneeDecompte()) });
        }
        // v�rification que la p�riode de fin est dans la m�me ann�e que le d�compte
        if (!anneeFin.equals(decompteAdiModel.getAnneeDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeFinEtAnnee.businessIntegrity",
                    new String[] { String.valueOf(decompteAdiModel.getAnneeDecompte()) });
        }

        // v�rification de l'existence du d�compte remplac�
        DecompteAdiSearchModel decompteSearch = new DecompteAdiSearchModel();
        decompteSearch.setForIdDecompteAdi(decompteAdiModel.getIdDecompteRemplace());

        if (!JadeNumericUtil.isEmptyOrZero(decompteAdiModel.getIdDecompteRemplace())
                && (0 == ALServiceLocator.getDecompteAdiModelService().count(decompteSearch))) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idDecompteRemplace.businessIntegrity.ExistingId");
        }

        // v�rification de l'existence de l'en-t�te de prestation
        if (JadeNumericUtil.isIntegerPositif(decompteAdiModel.getIdPrestationAdi())) {

            EntetePrestationSearchModel ea = new EntetePrestationSearchModel();
            ea.setForIdEntete(decompteAdiModel.getIdPrestationAdi());
            if ((0 == ALImplServiceLocator.getEntetePrestationModelService().count(ea))) {

                // Dans le cas de l'importation depuis ALFA-Gest il se peut
                // qu'il
                // reste des liens vers des prestations temporaires. Si c'est le
                // cas, on supprime ce lien � la condition qu'un d�compte de
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
     * V�rifie que les codes syst�me appartiennent � la famille de code attendue
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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

            // �tat du d�compte
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
     * v�rification de l'int�grit� des donn�es relatives � la base
     * 
     * @param model
     *            Mod�le � valider
     */
    private static void checkDatabaseIntegrity(DecompteAdiModel model) {

        // id du dossier
        if (!JadeNumericUtil.isIntegerPositif(model.getIdDossier())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idDossier.databaseIntegrity.type");
        }

        // id de l'organisme �tranger
        if (!JadeNumericUtil.isIntegerPositif(model.getIdTiersOrganismeEtranger())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idTiersOrganismeEtranger.databaseIntegrity.type");
        }

        // code de la monnaie
        if (!JadeNumericUtil.isIntegerPositif(model.getIdTiersOrganismeEtranger())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idTiersOrganismeEtranger.databaseIntegrity.type");
        }

        // ann�e du d�compte
        if (!JadeDateUtil.isGlobazDateYear(model.getAnneeDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.AnneeDecompte.databaseIntegrity.type");
        }

        // date de r�ception
        if (!JadeStringUtil.isEmpty(model.getDateReception()) && !JadeDateUtil.isGlobazDate(model.getDateReception())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.dateReception.databaseIntegrity.type");

        }

        // date d'�tat
        if (!JadeStringUtil.isEmpty(model.getDateEtat()) && !JadeDateUtil.isGlobazDate(model.getDateEtat())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.dateEtat.databaseIntegrity.type");

        }

        // �tat du d�compte
        if (!JadeNumericUtil.isIntegerPositif(model.getEtatDecompte())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.etatDecompte.databaseIntegrity.type");
        }

        // p�riode de d�but
        if (!JadeDateUtil.isGlobazDateMonthYear(model.getPeriodeDebut())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeDebut.databaseIntegrity.type");
        }

        // p�riode de fin
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

        // id du d�compte de remplacement
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
     * v�rification pour la suppression
     * 
     * @param decompteAdiModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * v�rification des param�tres requis
     * 
     * @param model
     *            Mod�le � valider
     */
    private static void checkMandatory(DecompteAdiModel model) {

        // id du dossier
        if (JadeStringUtil.isEmpty(model.getIdDossier())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(), "al.adi.decompteAdiModel.idDossier.mandatory");
        }

        // id de l'organisme �tranger
        if (JadeStringUtil.isEmpty(model.getIdTiersOrganismeEtranger())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.idTiersOrganismeEtranger.mandatory");
        }

        // code de la monnaie
        if (JadeStringUtil.isEmpty(model.getCodeMonnaie())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.codeMonnaie.mandatory");
        }

        // ann�e du d�compte
        if (JadeStringUtil.isEmpty(model.getAnneeDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.AnneeDecompte.mandatory");
        }

        // �tat du d�compte
        if (JadeStringUtil.isEmpty(model.getEtatDecompte())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.etatDecompte.mandatory");
        }

        // p�riode de d�but
        if (JadeStringUtil.isEmpty(model.getPeriodeDebut())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.decompteAdiModel.periodeDebut.mandatory");
        }

        // p�riode de fin
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
     * Validation de l'obligation des donn�es, de l'int�grit� des donn�es et des r�gles m�tier d'un mod�le de d�compte
     * ADI
     * 
     * @param decompteAdiModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(DecompteAdiModel decompteAdiModel) throws JadePersistenceException,
            JadeApplicationException {
        DecompteAdiModelChecker.checkMandatory(decompteAdiModel);
        DecompteAdiModelChecker.checkDatabaseIntegrity(decompteAdiModel);
        DecompteAdiModelChecker.checkCodesystemIntegrity(decompteAdiModel);
        DecompteAdiModelChecker.checkBusinessIntegrity(decompteAdiModel);
    }

    /**
     * Validation de la suppression des donn�es d'un mod�le de d�compte ADI
     * 
     * @param decompteAdiModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException {
        DecompteAdiModelChecker.checkDeleteIntegrity(decompteAdiModel);
    }
}
