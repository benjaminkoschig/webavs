package ch.globaz.perseus.businessimpl.checkers.lot;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.constantes.CSEtatPrestation;
import ch.globaz.perseus.business.models.lot.SimplePrestation;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public class SimplePrestationChecker extends PerseusAbstractChecker {

    /**
     * @param simplePrestation
     */
    public static void checkForCreate(SimplePrestation simplePrestation) {
        SimplePrestationChecker.checkMandatory(simplePrestation);
    }

    /**
     * @param simplePrestation
     */
    public static void checkForDelete(SimplePrestation simplePrestation) {
        // Si la prestation n'est pas comptabilisé alors on peut la supprimer
        // On ne permet pas de supprimer une prestation comptabilise à moins qu'elle soit à 0
        if (CSEtatPrestation.COMPTABILISE.getCodeSystem().equals(simplePrestation.getEtatPrestation())
                & !JadeNumericUtil.isEmptyOrZero(simplePrestation.getMontantTotal())) {

            System.out.println("Id Prestation = " + simplePrestation.getIdPrestation() + "Id Lot = "
                    + simplePrestation.getIdLot() + "Id DécisionPCF = " + simplePrestation.getIdDecisionPcf()
                    + "Id Facture = " + simplePrestation.getIdFacture() + "Date Debut = "
                    + simplePrestation.getDateDebut() + "Date Fin = " + simplePrestation.getDateFin()
                    + "Date Prestation = " + simplePrestation.getDatePrestation() + "Etat prestation = "
                    + simplePrestation.getEtatPrestation() + "Montant de la mesure = "
                    + simplePrestation.getMontantMesureCoaching() + "Montant total = "
                    + simplePrestation.getMontantTotal() + "Spy = " + simplePrestation.getSpy() + "CSpy = "
                    + simplePrestation.getCreationSpy());

            JadeThread.logError("SimplePrestationChecker", "perseus.lot.prestation.comptabilise.delete");
        }
    }

    /**
     * @param simplePrestation
     */
    public static void checkForUpdate(SimplePrestation simplePrestation) {
        SimplePrestationChecker.checkMandatory(simplePrestation);
    }

    /**
     * @param simplePrestation
     */
    private static void checkMandatory(SimplePrestation simplePrestation) {
        if (JadeStringUtil.isEmpty(simplePrestation.getDatePrestation())) {
            JadeThread.logError(SimplePrestationChecker.class.getName(), "perseus.lot.prestation.date.mandatory");
        }
        if (JadeStringUtil.isEmpty(simplePrestation.getEtatPrestation())) {
            JadeThread.logError(SimplePrestationChecker.class.getName(), "perseus.lot.prestation.etat.mandatory");
        }
        if (JadeStringUtil.isEmpty(simplePrestation.getMontantTotal())) {
            JadeThread.logError(SimplePrestationChecker.class.getName(), "perseus.lot.prestation.montant.mandatory");
        }
    }

}
