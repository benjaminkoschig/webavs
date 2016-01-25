/*
 * Créé le 4 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.test;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationListViewBean;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.cotisation.AFCotisation;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFJournalisationTest extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            BSession session = new BSession(AFApplication.DEFAULT_APPLICATION_NAOS);
            session = (BSession) GlobazServer.getCurrentSystem().getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .newSession(args[0], args[1]);
            AFJournalisationTest t = new AFJournalisationTest();
            t.setSession(session);
            t.executeProcess();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(200);
        }
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        AFAffiliationListViewBean listAff = new AFAffiliationListViewBean();
        BStatement st = null;
        try {
            listAff.setSession(getSession());
            listAff.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
            listAff.setForDateFin("0");
            st = listAff.cursorOpen(getTransaction());
            AFAffiliation crtAf;
            while ((crtAf = (AFAffiliation) listAff.cursorReadNext(st)) != null) {
                try {
                    AFCotisation crt = new AFCotisation();
                    crt.setSession(getSession());
                    System.out.println("Employeur " + crtAf.getAffilieNumero() + " cotisation AF :"
                            + crt.getLibelleCourtCotisationAF(crtAf.getAffiliationId(), JACalendar.todayJJsMMsAAAA()));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(crtAf.getAffilieNumero());
                }
            }
            listAff.cursorClose(st);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(200);
        } finally {
            listAff.cursorClose(st);
        }
        return true;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_LONG;
    }
}
