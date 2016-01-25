package ch.globaz.perseus.businessimpl.checkers.rentepont;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.perseus.business.models.rentepont.SimpleFactureRentePont;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author jsi
 * 
 */
public class SimpleFactureRentePontChecker extends PerseusAbstractChecker {

    public static void checkForCreate(SimpleFactureRentePont factureRentePont) {
        if (!PerseusAbstractChecker.threadOnError()) {
            SimpleFactureRentePontChecker.checkIntegrity(factureRentePont);
            SimpleFactureRentePontChecker.checkMandatory(factureRentePont);
        }
    }

    private static void checkIntegrity(SimpleFactureRentePont factureRentePont) {
    }

    private static void checkMandatory(SimpleFactureRentePont factureRentePont) {
        if (JadeStringUtil.isEmpty(factureRentePont.getDateReception())) {
            JadeThread.logError(SimpleFactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.dateReception.mandatory");
        }
        if (JadeStringUtil.isEmpty(factureRentePont.getDateFacture())) {
            JadeThread.logError(SimpleFactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.dateFacture.mandatory");
        }
        if (JadeStringUtil.isEmpty(factureRentePont.getCsTypeSoinRentePont())) {
            JadeThread.logError(SimpleFactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.typeSoin.mandatory");
        }
        if (JadeStringUtil.isEmpty(factureRentePont.getCsSousTypeSoinRentePont())) {
            JadeThread.logError(SimpleFactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.typeSoin.mandatory");
        }
        if (JadeStringUtil.isEmpty(factureRentePont.getMontantRembourse())) {
            JadeThread.logError(SimpleFactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.montantRembourse.mandatory");
        }
        if (JadeStringUtil.isEmpty(factureRentePont.getMontant())) {
            JadeThread.logError(SimpleFactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.montant.mandatory");
        }
        if (JadeStringUtil.isEmpty(factureRentePont.getIdTiersAdressePaiement())) {
            JadeThread.logError(SimpleFactureRentePontChecker.class.getName(),
                    "perseus.rentePont.factureRentePont.idTiersAdressePaiement.mandatory");
        }
    }

}
