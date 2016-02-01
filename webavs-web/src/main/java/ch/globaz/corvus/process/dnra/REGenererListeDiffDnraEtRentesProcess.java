package ch.globaz.corvus.process.dnra;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import ch.globaz.corvus.process.REAbstractJadeJob;

/**
 * Compare les données du NRA transmises via le fichier journalier (delta-quotidien) avec les données des rentes et
 * génère une liste recensant ces différences.
 * 
 * @author bjo
 * 
 */
public class REGenererListeDiffDnraEtRentesProcess extends REAbstractJadeJob {

    private static final long serialVersionUID = 7164797280525378834L;

    @Override
    public String getDescription() {
        return "Compare les données du NRA transmises via le fichier journalier avec les données des rentes et génère une liste recensant ces différences.";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    protected void process() {
        System.out.println("début du traitement");
        try {
            String nraUpiServerUri = getSessionPavo(getSession()).getApplication().getProperty("nraUpiServer");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // téléchargement des fichiers en local

        System.out.println("fin du traitement");
    }

    private static BSession getSessionPavo(BSession session) throws Exception {
        return (BSession) GlobazSystem.getApplication("PAVO").newSession(session);
    }

}
