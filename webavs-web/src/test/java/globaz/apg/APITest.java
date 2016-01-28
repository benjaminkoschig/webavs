package globaz.apg;

import globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant;
import globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependantLoader;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import java.rmi.RemoteException;

/**
 * Descpription
 * 
 * @author scr Date de création 28 sept. 05
 */
public class APITest {

    public static void main(String[] args) {

        BISession session = null;
        try {
            session = GlobazSystem.getApplication("APG").newSession("globaz", "glob4az");

            IAPPeriodesRevenuIndependantLoader periodeRevInd = (IAPPeriodesRevenuIndependantLoader) session
                    .getAPIFor(IAPPeriodesRevenuIndependantLoader.class);
            IAPPeriodesRevenuIndependant[] result = periodeRevInd.load("123456", "01.01.2006", "31.01.2006");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
	 * 
	 */
    public APITest() {
        super();
    }
}
