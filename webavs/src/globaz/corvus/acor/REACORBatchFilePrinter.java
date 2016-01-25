package globaz.corvus.acor;

import globaz.corvus.acor.adapter.REAdapterFactory;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAbstractACORBatchFilePrinter;
import globaz.prestation.acor.PRAcorFileContent;
import java.util.Map;

/**
 * @author scr
 */
public class REACORBatchFilePrinter extends PRAbstractACORBatchFilePrinter {

    private static REACORBatchFilePrinter INSTANCE;

    /**
     * getter pour l'attribut instance.
     * 
     * @return la valeur courante de l'attribut instance
     */
    public static final synchronized REACORBatchFilePrinter getInstance() {
        if (REACORBatchFilePrinter.INSTANCE == null) {
            REACORBatchFilePrinter.INSTANCE = new REACORBatchFilePrinter();
        }

        return REACORBatchFilePrinter.INSTANCE;
    }

    /**
     * Crée une nouvelle instance de la classe IJACORBatchFilePrinter.
     */
    private REACORBatchFilePrinter() {
        // ne peut etre instancie
    }

    public void printBatchFileRentes(Map<String, PRAcorFileContent> fileContent, BSession session,
            REDemandeRente demande, String dossierRacineAcor) throws PRACORException {
        try {
            printBatchFile(fileContent, session, REAdapterFactory.getInstance(session).createAdapter(session, demande),
                    dossierRacineAcor);
        } catch (PRACORException e1) {
            throw e1;
        } catch (Exception e) {
            throw new PRACORException("impossible de charger l'adapteur pour le calcul de cette demande de rente", e);
        }
    }
}
