package globaz.aquila.service.controleemployeur;

import globaz.aquila.db.access.poursuite.COHistoriqueAffilieManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * @author SCO
 * @since SCO 11 juin 2010
 */
public class COControleEmployeurService {

    /**
     * Permet de r�cup�rer la liste du contentieux (Commandement de payer, Sommation, Rappel, ....) pour un affili� et
     * une p�riode donn�e
     * 
     * @param session
     * @param numAffilie
     *            Un num�ro d'affili�
     * @param dateDebut
     *            Une date de d�but
     * @param dateFin
     * @return
     * @throws Exception
     */
    public static COHistoriqueAffilieManager searchHistoriqueAffilie(BSession session, BTransaction transaction,
            String numAffilie, String dateDebut, String dateFin) throws Exception {

        COHistoriqueAffilieManager manager = new COHistoriqueAffilieManager();
        manager.setSession(session);
        manager.setForNumAffilie(numAffilie);
        manager.setForDateDebut(dateDebut);
        manager.setForDateFin(dateFin);

        try {
            manager.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw e;
        }

        if (!manager.hasErrors()) {
            return manager;
        }

        return null;
    }

    /**
     * Constructeur de COControleEmployeurService
     */
    protected COControleEmployeurService() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
