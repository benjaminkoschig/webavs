package globaz.corvus.process;

import globaz.corvus.TestConfig;
import globaz.corvus.db.rentesaccordees.REPaiementRentesManager;
import globaz.corvus.utils.AbstractTestCaseWithContext;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.util.Date;
import org.junit.Ignore;
import org.junit.Test;

public class REExecuterPaiementMensuelProcessTest extends AbstractTestCaseWithContext {

    public void executeREPaiementRenteManagerRequest() throws Exception {
        System.out.println("--------------------------------------------");
        System.out.println("REPaiementRentesManager : avec domaine tiers");

        Long start = new Date().getTime();
        REPaiementRentesManager mgr = new REPaiementRentesManager();
        mgr.setSession(getSession());
        BITransaction transaction = getSession().newTransaction();
        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }
        mgr.setForDatePaiement("02.2013");
        mgr.setForIsEnErreur(Boolean.FALSE);
        BStatement statement = mgr.cursorOpen((BTransaction) transaction);
        Long stop = new Date().getTime();
        System.out.println("Time to execute request : " + ((stop - start) / 1000));
        System.out.println();
    }

    @Override
    protected String getApplicationName() {
        return TestConfig.getDefaultConfig().getApplicationName();
    }

    @Override
    protected String getUserName() {
        return TestConfig.getDefaultConfig().getUserName();
    }

    @Override
    protected String getUserPassword() {
        return TestConfig.getDefaultConfig().getUserPassword();
    }

    @Ignore
    @Test
    public void mainTest() throws Exception {
        executeREPaiementRenteManagerRequest();
    }

}
