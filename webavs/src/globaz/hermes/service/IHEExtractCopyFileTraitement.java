package globaz.hermes.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.io.BufferedWriter;

/**
 * Interfave pour le traitement des fichiers de rente à envoyer
 * 
 * @author David Van Hooste
 */
public interface IHEExtractCopyFileTraitement {
    /**
     * @param ligne
     * @return la ligne traitée
     */
    public void genererFichier(BufferedWriter file, boolean hasCarriageReturns, BSession session,
            BTransaction transaction) throws Exception;

    public void setForListMotif(String[] motifs) throws Exception;
}
