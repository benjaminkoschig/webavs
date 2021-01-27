package ch.globaz.pegasus.businessimpl.checkers.habitat;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCTaxeJournaliereHomeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaxeJournaliereHomeChecker extends PegasusAbstractChecker {

    private static final Logger LOG = LoggerFactory.getLogger(TaxeJournaliereHomeChecker.class);

    public static void checkForCreate(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException {
        if (!PegasusAbstractChecker.threadOnError()) {
            TaxeJournaliereHomeChecker.checkIntegrity(taxeJournaliereHome);

            try {
                String prix = PCTaxeJournaliereHomeHandler.getPrix(taxeJournaliereHome, BSessionUtil.getSessionFromThreadContext(), false);
                if(taxeJournaliereHome.getSimpleTaxeJournaliereHome().getPrixJournalier() != null
                        && taxeJournaliereHome.getSimpleTaxeJournaliereHome().getPrixJournalier().equals(prix)) {
                    taxeJournaliereHome.getSimpleTaxeJournaliereHome().setPrixJournalier(null);
                }
            } catch (JadeApplicationServiceNotAvailableException e) {
                LOG.error("erreur lors de la création de la taxe journalière", e);
            }

        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleLoyer
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException {
    }

}
