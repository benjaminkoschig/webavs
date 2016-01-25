package ch.globaz.sar;

import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.globall.db.BTransaction;
import org.junit.Test;

/**
 * Test diver sde l'utilisation des BTrasnsaction
 * 
 * @author sce
 * 
 */
public class BTransactions {

    static {
        try {
            JadeManager.runJade("ccjuglo", "glob4az", "FRAMEWORK");
        } catch (Exception e) {
            System.out.println("Problem during loading Jade:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void erroneousInstanciation() {

        try {
            BTransaction transaction = new BTransaction(null);

            transaction.openTransaction();

            APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
            situationFamilialeAPG.setSession(null);
            situationFamilialeAPG.add(transaction);

            transaction.commit();

            transaction.closeTransaction();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }

    }
}
