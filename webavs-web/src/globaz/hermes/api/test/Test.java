package globaz.hermes.api.test;

/**
 * Insérez la description du type ici. Date de création : (19.11.2002 16:44:52)
 * 
 * @author: ado
 */
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;

//
public class Test {
    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        // Insérez ici le code de démarrage de l'application
        try {
            new Test().businessService();
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
        System.exit(-1);
    }

    /**
     * Commentaire relatif au constructeur Test.
     */
    public Test() {
        super();
    }

    /**
     * logique
     */
    public void businessService() throws Exception {
        // tout ce qu'il faut pour le remote
        BIApplication remoteApplication = GlobazSystem.getApplication("HERMES");
        BISession remoteSession = remoteApplication.newSession("userfr", "userfr");
        IHEInputAnnonce annonce = (IHEInputAnnonce) remoteSession.getAPIFor(IHEInputAnnonce.class);
        // ICICompteIndividuel remoteCI = (ICICompteIndividuel)
        // remoteSession.getAPIFor(ICICompteIndividuel.class);
        BITransaction transaction = ((BSession) remoteSession).newTransaction();
        try {
            annonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "38");
            annonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "1");
            annonce.put(IHEAnnoncesViewBean.CODE_1_OU_2, "1");
            annonce.add(transaction);
            annonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "2");
            annonce.add(transaction);
            annonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, "39");
            annonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "1");
            annonce.add(transaction);
            // remoteCI.load("39874269119", transaction);
            // System.out.println("39874269119 " + (remoteCI.isCiOuvert() ?
            // "est ouvert" : "n'est pas ouvert"));
            // remoteCI.load("70369407110", transaction);
            try {
                // remoteCI.isCiOuvert();
            } catch (Exception e) {
                System.out.println("70369407110 n'est pas ouvert");
            }
        } catch (Exception e) {
            transaction.closeTransaction();
            // System.out.println("CI non trouvé");
            e.printStackTrace();
        }
        if (remoteSession.hasErrors()) {
            System.err.println("Non-fatal errors : " + remoteSession.getErrors().toString());
        }
    }
}
