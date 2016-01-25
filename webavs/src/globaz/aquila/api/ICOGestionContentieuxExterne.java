package globaz.aquila.api;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APISection;

public interface ICOGestionContentieuxExterne {

    /**
     * Cr�ation d'une contentieux pour une section pass�e en param�tre. <br/>
     * Utiliser pour le d�lai au paiement dans le cadre de l'impression des bulletins de soldes.
     * 
     * @param session
     * @param transaction
     * @param section
     * @return
     * @throws Exception
     */
    public void creerContentieux(BSession session, BTransaction transaction, APISection section) throws Exception;

    /**
     * D�cale l'�ch�ance de la transition courante du nombre de jour pass� en param�tre.
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
