package globaz.hermes.utils;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.pavo.api.ICIException;

/**
 * <code>AffiliationAPITest</code> est une classe de test des API d'Affiliation
 * 
 * @author David Girardin
 */
public class HermesAPITest {
    /**
     * Exécute le test
     * 
     * @param args
     *            [] arguments: idTiers
     */
    public static void main(String[] args) {
        try {
            HermesAPITest test = new HermesAPITest();
            // API GLOBAZ:
            // - recherche d'une application
            // (sa localisation est définie dans le fichier GlobazSystem.xml)
            //
            // API GLOBAZ:
            // - ouverture d'une session utilisateur sur l'application obtenue
            //
            try {
                test.session = GlobazServer.getCurrentSystem().getApplication("HERMES")
                        .newSession("ciciglo", "glob4az");
            } catch (Exception e) {
            }
            //
            test.addArc();
            // test.addException();

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    /** Session */
    public BISession session;

    /** Mode de renvoi des informations sur System.out */
    private boolean verbose = true;

    private void addArc() throws Exception {
        IHEInputAnnonce d = (IHEInputAnnonce) session.getAPIFor(IHEInputAnnonce.class);
        BITransaction transaction = ((BSession) session).newTransaction();
        d.setISession(session);
        d.setNumeroAffilie("107.0450");
        d.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
        d.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
        d.put(IHEAnnoncesViewBean.NUMERO_ASSURE, "756.0459.4948.34");
        d.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "61");
        d.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE, "TEST/ALD");
        d.setLangueCorrespondance("503002");
        d.setDateEngagement("01.01.2012");
        d.add(transaction);
    }

    private void addException() throws Exception {
        ICIException d = (ICIException) session.getAPIFor(ICIException.class);
        BITransaction transaction = ((BSession) session).newTransaction();
        d.setISession(session);
        d.setIsJsp("true");
        d.setAffilie("107.0450");
        d.setNumeroAvs("756.5540.1017.45");
        d.setDateEngagement("26.07.2012");
        d.setLangueCorrespondance("503002");
        d.add(transaction);
        System.out.println(transaction.getErrors());

        transaction.commit();

    }

    /**
     * Imprime une ligne d'information
     * 
     * @param text
     *            le texte à imprimer
     */
    public void println(String text) {
        if (verbose) {
            System.out.println(text);
        }
    }
}
