package globaz.aquila.api;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APISection;

public interface ICOGestionContentieuxExterne {

    /**
     * Création d'une contentieux pour une section passée en paramètre. <br/>
     * Utiliser pour le délai au paiement dans le cadre de l'impression des bulletins de soldes.
     * 
     * @param session
     * @param transaction
     * @param section
     * @return
     * @throws Exception
     */
    public void creerContentieux(BSession session, BTransaction transaction, APISection section) throws Exception;

    /**
     * Décale l'échéance de la transition courante du nombre de jour passé en paramètre.
     * 
     * @param session
     * @param transaction
     * @param section
     * @param nombreJours
     * @throws Exception
     */
    public void decalerEcheance(BSession session, BTransaction transaction, APISection section, int nombreJours)
            throws Exception;
}
