package globaz.apg.timestamp;

import globaz.jade.client.util.JadeDateUtil;
import org.junit.Test;

/**
 * Class pour g�n�rer des timestamp lors de la cr�ation d'annonces en base de donn�es via script
 * 
 * @author lga
 * 
 */
public class APTimestampGenerator {

    @Test
    public void createTimestamp() throws InterruptedException {
        int nombre = 20;
        for (int ctr = 0; ctr < nombre; ctr++) {
            System.out.println(JadeDateUtil.getCurrentTime().toString());
            Thread.sleep(100);
        }
    }
}
