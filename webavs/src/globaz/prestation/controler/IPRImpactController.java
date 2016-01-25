package globaz.prestation.controler;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author SCR
 * 
 *         Interface g�n�rique pour tous les 'ImpactControler'
 */
public interface IPRImpactController {

    public final static int DOMAINE_RENTE_CONTROLER = 1000;

    /**
     * 
     * Control des impacts des applications concern�es, selon les donn�es contenues dans le dataControler.
     * 
     * @param session
     * @param transaction
     * @param dataControler
     * @return StringBuffer
     * @throws Exception
     */
    public StringBuffer control(BSession session, BTransaction transaction, IPRDataController dataControler)
            throws Exception;
}
