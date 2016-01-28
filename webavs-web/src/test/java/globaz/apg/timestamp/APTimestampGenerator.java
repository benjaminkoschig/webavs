package globaz.apg.timestamp;

import globaz.jade.client.util.JadeDateUtil;
import org.junit.Test;

/**
 * Class pour générer des timestamp lors de la création d'annonces en base de données via script
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
