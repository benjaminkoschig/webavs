/**
 * 
 */
package ch.globaz.perseus.businessimpl.checkers.rentepont;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.constantes.CSTypeCreance;
import ch.globaz.perseus.business.models.rentepont.SimpleCreancierRentePont;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author MBO
 * 
 */
public class SimpleCreancierRentePontChecker extends PerseusAbstractChecker {

    public static void checkForCreate(SimpleCreancierRentePont simpleCreancierRentePont) {
        SimpleCreancierRentePontChecker.checkMandatory(simpleCreancierRentePont);
    }

    public static void checkForDelete(SimpleCreancierRentePont simpleCreancierRentePont) {

    }

    public static void checkForUpdate(SimpleCreancierRentePont simpleCreancierRentePont) {
        SimpleCreancierRentePontChecker.checkMandatory(simpleCreancierRentePont);
    }

    private static void checkMandatory(SimpleCreancierRentePont simpleCreancierRentePont) {
        if (!CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem()
                .equals(simpleCreancierRentePont.getCsTypeCreance())
                && JadeStringUtil.isEmpty(simpleCreancierRentePont.getIdTiers())) {
            JadeThread.logError(SimpleCreancierRentePontChecker.class.getName(),
                    "perseus.creancier.creancier.tiers.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleCreancierRentePont.getMontantRevendique())) {
            JadeThread.logError(SimpleCreancierRentePontChecker.class.getName(),
                    "perseus.creancier.creancier.montant.mandatory");
        }
        if (JadeStringUtil.isEmpty(simpleCreancierRentePont.getCsTypeCreance())) {
            JadeThread.logError(SimpleCreancierRentePontChecker.class.getName(),
                    "perseus.creancier.creancier.type.mandatory");
        }
        Float montantAccorde = new Float(0);
        if (!JadeStringUtil.isEmpty(simpleCreancierRentePont.getMontantAccorde())) {
            montantAccorde = Float.parseFloat(simpleCreancierRentePont.getMontantAccorde().replace("'", ""));
        }
        Float montantRevendique = new Float(0);
        if (!JadeStringUtil.isEmpty(simpleCreancierRentePont.getMontantRevendique())) {
            montantRevendique = Float.parseFloat(simpleCreancierRentePont.getMontantRevendique().replace("'", ""));
        }
        if (montantAccorde > montantRevendique) {
            JadeThread.logError(SimpleCreancierRentePontChecker.class.getName(),
                    "perseus.creancier.creanceAccordee.montant.integrity");
        }

    }
}
