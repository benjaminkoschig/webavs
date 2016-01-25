package ch.globaz.al.businessimpl.checker.model.adi;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.adi.DecompteAdiSearchModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.prestation.DetailPrestationModelChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validation du mod�le AdiEnfantMoisModel
 * 
 * @author PTA
 * 
 */
public class AdiEnfantMoisModelChecker extends ALAbstractChecker {

    /**
     * v�rification des r�gles m�tier
     * 
     * @param adiEnfantMoisModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    private static void checkBusinessIntegrity(AdiEnfantMoisModel adiEnfantMoisModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // idDecompteAdi

        // v�rification de l'existence du droit
        DroitSearchModel sd = new DroitSearchModel();
        sd.setForIdDroit(adiEnfantMoisModel.getIdDroit());
        if (0 == ALImplServiceLocator.getDroitModelService().count(sd)) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.idDroit.businessIntegrity.ExistingId");
        }

        DecompteAdiSearchModel dasm = new DecompteAdiSearchModel();
        dasm.setForIdDecompteAdi(adiEnfantMoisModel.getIdDecompteAdi());
        if (0 == ALServiceLocator.getDecompteAdiModelService().count(dasm)) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.idDecompteAdi.businessIntegrity.ExistingId");
        }
    }

    /**
     * v�rification de l'int�grit� des donn�es relative � la base de donn�es
     * 
     * @param model
     *            Mod�le � valider
     */
    private static void checkDatabaseIntegrity(AdiEnfantMoisModel model) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // idDecompteAdi
        if (!JadeNumericUtil.isIntegerPositif(model.getIdDecompteAdi())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.idDecompteAdi.databaseIntegrity");
        }

        // idDroit
        if (!JadeNumericUtil.isIntegerPositif(model.getIdDroit())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.idDroit.databaseIntegrity");
        }

        // moisPeriode
        if (!JadeDateUtil.isGlobazDateMonthYear(model.getMoisPeriode())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.moisPeriode.databaseIntegrity");

        }

        // coursChangeMonnaie
        if (!JadeNumericUtil.isNumeric(model.getCoursChangeMonnaie())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.coursChangeMonnaie.mandatory");
        }

        // nbrEnfantFamille
        if (!JadeNumericUtil.isIntegerPositif(model.getNbrEnfantFamille())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.nbrEnfantFamille.databaseIntegrity");
        }

        // montantAllocCH
        if (!JadeNumericUtil.isNumeric(model.getMontantAllocCH())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantAllocCH.databaseIntegrity");
        }

        // montantAllocEtr
        if (!JadeNumericUtil.isNumeric(model.getMontantAllocEtr())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantAllocEtr.databaseIntegrity");
        }

        // montantRepartiEtrTotal
        if (!JadeNumericUtil.isNumeric(model.getMontantRepartiEtrTotal())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantRepartiEtrTotal.databaseIntegrity");
        }

        // montantRepartiEtr
        if (!JadeNumericUtil.isNumeric(model.getMontantRepartiEtr())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantRepartiEtr.databaseIntegrity");
        }

        // montantRepartiCHTotal
        if (!JadeNumericUtil.isNumeric(model.getMontantRepartiCHTotal())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantRepartiCHTotal.databaseIntegrity");
        }

        // montantRepartiCH
        if (!JadeNumericUtil.isNumeric(model.getMontantRepartiCH())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantRepartiCH.databaseIntegrity");
        }

        // montantCHTotal
        if (!JadeNumericUtil.isNumeric(model.getMontantCHTotal())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantCHTotal.databaseIntegrity");
        }

        // montantEtrTotal
        if (!JadeNumericUtil.isNumeric(model.getMontantEtrTotal())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantEtrTotal.databaseIntegrity");
        }

        // montantEtrTotalEnCh
        if (!JadeNumericUtil.isNumeric(model.getMontantEtrTotalEnCh())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantEtrTotalEnCh.databaseIntegrity");
        }

        // montantAdi
        if (!JadeNumericUtil.isNumeric(model.getMontantAdi())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantAdi.databaseIntegrity");
        }
    }

    /**
     * V�rification des param�tres requis
     * 
     * @param model
     *            Mod�le � valider
     */
    private static void checkMandatory(AdiEnfantMoisModel model) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // idDecompteAdi
        if (JadeStringUtil.isEmpty(model.getIdDecompteAdi())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.idDecompteAdi.mandatory");
        }

        // idDroit
        if (JadeStringUtil.isEmpty(model.getIdDroit())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.idDroit.mandatory");
        }

        // moisPeriode
        if (JadeStringUtil.isEmpty(model.getMoisPeriode())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.moisPeriode.mandatory");
        }

        // coursChangeMonnaie
        if (JadeStringUtil.isEmpty(model.getCoursChangeMonnaie())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.coursChangeMonnaie.mandatory");
        }

        // nbrEnfantFamille
        if (JadeStringUtil.isEmpty(model.getNbrEnfantFamille())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.nbrEnfantFamille.mandatory");
        }

        // montantAllocCH
        if (JadeStringUtil.isEmpty(model.getMontantAllocCH())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantAllocCH.mandatory");
        }

        // montantAllocEtr
        if (JadeStringUtil.isEmpty(model.getMontantAllocEtr())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantAllocEtr.mandatory");
        }

        // montantRepartiEtrTotal
        if (JadeStringUtil.isEmpty(model.getMontantRepartiEtrTotal())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantRepartiEtrTotal.mandatory");
        }

        // montantRepartiEtr
        if (JadeStringUtil.isEmpty(model.getMontantRepartiEtr())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantRepartiEtr.mandatory");
        }

        // montantRepartiCHTotal
        if (JadeStringUtil.isEmpty(model.getMontantRepartiCHTotal())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantRepartiCHTotal.mandatory");
        }

        // montantRepartiCH
        if (JadeStringUtil.isEmpty(model.getMontantRepartiCH())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantRepartiCH.mandatory");
        }

        // montantCHTotal
        if (JadeStringUtil.isEmpty(model.getMontantCHTotal())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantCHTotal.mandatory");
        }

        // montantEtrTotal
        if (JadeStringUtil.isEmpty(model.getMontantEtrTotal())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantEtrTotal.mandatory");
        }

        // montantEtrTotalEnCh
        if (JadeStringUtil.isEmpty(model.getMontantEtrTotalEnCh())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantEtrTotalEnCh.mandatory");
        }

        // montantAdi
        if (JadeStringUtil.isEmpty(model.getMontantAdi())) {
            JadeThread.logError(AdiEnfantMoisModelChecker.class.getName(),
                    "al.adi.adiEnfantMoisModel.montantAdi.mandatory");
        }
    }

    /**
     * Validation de l'obligation des donn�es, de l'int�grit� des donn�es et des r�gles m�tier du mod�le AdiEnfant
     * 
     * @param adiEnfantMoisModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(AdiEnfantMoisModel adiEnfantMoisModel) throws JadePersistenceException,
            JadeApplicationException {
        AdiEnfantMoisModelChecker.checkMandatory(adiEnfantMoisModel);
        AdiEnfantMoisModelChecker.checkDatabaseIntegrity(adiEnfantMoisModel);
        AdiEnfantMoisModelChecker.checkBusinessIntegrity(adiEnfantMoisModel);
    }
}
