package ch.globaz.al.businessimpl.checker.model.adi;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.adi.AdiSaisieModel;
import ch.globaz.al.business.models.adi.AdiSaisieSearchModel;
import ch.globaz.al.business.models.droit.EnfantSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.allocataire.AllocataireModelChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de validation du mod�le AdiSaisieModel
 * 
 * @author GMO
 * 
 */
public class AdiSaisieModelChecker extends ALAbstractChecker {
    /**
     * v�rification des r�gles m�tier
     * 
     * @param adiSaisieModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(AdiSaisieModel adiSaisieModel) throws JadePersistenceException,
            JadeApplicationException {

        // int�grit� de la date du d�but (ant�rieur) et de la date de
        // fin(post�rieur)

        if (JadeDateUtil.isDateMonthYearAfter(adiSaisieModel.getPeriodeDe(), adiSaisieModel.getPeriodeA())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.dateDebutDateFin.businessIntegrity");
        }

        // v�rification de l'enfant li� � la saisie
        if (!JadeNumericUtil.isEmptyOrZero(adiSaisieModel.getIdEnfant())) {
            EnfantSearchModel searchModel = new EnfantSearchModel();
            searchModel.setForIdEnfant(adiSaisieModel.getIdEnfant());
            if (ALImplServiceLocator.getEnfantModelService().count(searchModel) == 0) {
                JadeThread.logError(AdiSaisieModelChecker.class.getName(),
                        "al.adi.adiSaisieModel.idEnfant.businessIntegrity.existingId");
            }
        }
        // V�rification si saisie d�j� existante
        AdiSaisieSearchModel searchSaisie = new AdiSaisieSearchModel();
        searchSaisie.setForIdDecompteAdi(adiSaisieModel.getIdDecompteAdi());
        searchSaisie.setForIdEnfant(adiSaisieModel.getIdEnfant());
        searchSaisie.setWhereKey("saisieExistante");
        searchSaisie.setForPeriodeMin(adiSaisieModel.getPeriodeDe());
        searchSaisie.setForPeriodeMax(adiSaisieModel.getPeriodeA());

        searchSaisie = ALServiceLocator.getAdiSaisieModelService().search(searchSaisie);
        if (searchSaisie.getSize() > 0) {
            JadeThread.logError(AdiSaisieModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.warning.businessIntegrity.saisieExistante");
        }
    }

    /**
     * v�rification de l'int�grit� des donn�es relatives � la base
     * 
     * @param adiSaisieModel
     *            Mod�le � valider
     */
    private static void checkDatabaseIntegrity(AdiSaisieModel adiSaisieModel) {

        // validit� p�riode d�but
        if (!JadeDateUtil.isGlobazDateMonthYear(adiSaisieModel.getPeriodeDe())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.periodeDebut.databaseIntegrity");
        }

        // validit� p�riode fin
        if (!JadeDateUtil.isGlobazDateMonthYear(adiSaisieModel.getPeriodeA())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.AnneeDecompte.databaseIntegrity");
        }

        // id du d�compte adi
        if (!JadeNumericUtil.isIntegerPositif(adiSaisieModel.getIdDecompteAdi())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.idDecompteAdi.databaseIntegrity");
        }

        // id du d�compte adi
        if (!JadeNumericUtil.isEmptyOrZero(adiSaisieModel.getIdEnfant())
                && !JadeNumericUtil.isIntegerPositif(adiSaisieModel.getIdEnfant())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.idEnfant.databaseIntegrity");
        }

        // montant saisi
        if (!JadeNumericUtil.isNumeric(adiSaisieModel.getMontantSaisi())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.montantSaisie.databaseIntegrity");
        }

    }

    /**
     * v�rification des param�tres requis
     * 
     * @param adiSaisieModel
     *            Mod�le � valider
     */
    private static void checkMandatory(AdiSaisieModel adiSaisieModel) {

        // p�riode du d�but du d�compte obligatoire
        if (JadeStringUtil.isEmpty(adiSaisieModel.getPeriodeDe())) {
            JadeThread
                    .logError(DecompteAdiModelChecker.class.getName(), "al.adi.adiSaisieModel.PeriodeDebut.mandatory");
        }
        // p�riode de fin du d�compte obligatoire
        if (JadeStringUtil.isEmpty(adiSaisieModel.getPeriodeA())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(), "al.adi.adiSaisieModel.PeriodeFin.mandatory");
        }

        if (JadeStringUtil.isEmpty(adiSaisieModel.getIdDecompteAdi())) {
            JadeThread.logError(DecompteAdiModelChecker.class.getName(),
                    "al.adi.adiSaisieModel.idDecompteAdi.mandatory");
        }

    }

    /**
     * validation de l'int�grit� des donn�es et des r�gles m�tier, de l'obligation des donn�es
     * 
     * @param saisieModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(AdiSaisieModel saisieModel) throws JadePersistenceException, JadeApplicationException {
        AdiSaisieModelChecker.checkMandatory(saisieModel);
        AdiSaisieModelChecker.checkDatabaseIntegrity(saisieModel);
        AdiSaisieModelChecker.checkBusinessIntegrity(saisieModel);
    }

}
