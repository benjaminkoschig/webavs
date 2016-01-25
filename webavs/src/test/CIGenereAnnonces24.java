/*
 * Créé le 4 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package test;

import globaz.commons.nss.NSUtil;
import globaz.framework.process.FWProcess;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.pavo.application.CIApplication;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author jmc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIGenereAnnonces24 extends FWProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        String uid = "globazd";

        String email = "jmc@globaz.ch";

        CIGenereAnnonces24 process = null;
        try {
            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession("ccjuglo", "glob4az");
            process = new CIGenereAnnonces24();
            process.setSession((BSession) session);
            process.setEMailAddress(email);
            process.executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(1);

    }

    private final String codeApplicationEnregCaisse = "2401026001";

    /**
	 * 
	 */
    public CIGenereAnnonces24() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // TODO Raccord de méthode auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader("d://ccju//nss.txt"));
            String line = null;
            while ((line = fileIn.readLine()) != null) {
                try {
                    String nss = NSUtil.returnNNSS(getSession(), line.trim());
                    if (nss == null) {
                        System.out.println(line);
                    }
                } catch (Exception f) {
                    System.out.println(line);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return !isAborted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return "La génération du fichier 24 a échouée";
        } else {
            return "La génération du fichier 24 a réussi";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Raccord de méthode auto-généré
        return GlobazJobQueue.READ_SHORT;
    }

}
