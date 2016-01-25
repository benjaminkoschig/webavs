package globaz.ij.test;

import globaz.babel.api.doc.CTScalableDocumentFactory;
import globaz.babel.api.doc.ICTScalableDocument;
import globaz.babel.api.doc.ICTScalableDocumentAnnexe;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.ij.process.IJEcheancesPrononcesProcess;
import globaz.itucana.exception.TUModelInstanciationException;
import java.rmi.RemoteException;
import java.util.Random;

/**
 * Descpription
 * 
 * @author scr Date de création 28 sept. 05
 */
public class Main {

    public static void main(String[] args) throws TUModelInstanciationException {
        Main m = new Main();
        m.random1();
    }

    private static void showRandomInteger(long aStart, long aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        // get the range, casting to long to avoid overflow problems
        long range = (aEnd - aStart) + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        System.out.println("Generated : " + randomNumber);
    }

    /**
	 * 
	 */
    public Main() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    public void doTraitement() {

        BISession session;
        try {

            session = GlobazSystem.getApplication("IJ").newSession("hpe", "hpe");

            // Setter manuellement toutes les properties dans les ICTxxxxx

            CTScalableDocumentFactory factory = CTScalableDocumentFactory.getInstance();

            ICTScalableDocument document = new CTScalableDocumentAbstractViewBeanDefaultImpl();
            ICTScalableDocumentProperties documentProperties = factory.createNewScalableDocumentProperties();
            ICTScalableDocumentAnnexe documentAnnexe = null;
            ICTScalableDocumentCopie documentCopie = null;

            documentProperties.setParameter("idPrononce", "151");
            documentProperties.setIdTiersPrincipal("134944");

            documentCopie = factory.createNewScalableDocumentCopie();
            documentCopie.setIdTiers("134945");
            documentProperties.addCopie(documentCopie);
            documentCopie = factory.createNewScalableDocumentCopie();
            documentCopie.setIdTiers("112148");
            documentProperties.addCopie(documentCopie);
            documentCopie = factory.createNewScalableDocumentCopie();
            documentCopie.setIdTiers("107961");
            documentProperties.addCopie(documentCopie);

            documentAnnexe = factory.createNewScalableDocumentAnnexe();
            documentAnnexe.setLibelle("Tabelles IJAI Version 1.4.8");
            documentProperties.addAnnexe(documentAnnexe);
            documentAnnexe = factory.createNewScalableDocumentAnnexe();
            documentAnnexe.setLibelle("Liste des caisses de compensations suisses");
            documentProperties.addAnnexe(documentAnnexe);
            documentAnnexe = factory.createNewScalableDocumentAnnexe();
            documentAnnexe.setLibelle("Formulaire de retour pour remboursement");
            documentProperties.addAnnexe(documentAnnexe);

            document.setScalableDocumentProperties(documentProperties);
            document.setEMailAddress("hpe@globaz.ch");

            // Démarrer le process avec les properties de BABEL

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doTraitement2() {

        BISession session;
        try {

            session = GlobazSystem.getApplication("IJ").newSession("hpe", "hpe");

            IJEcheancesPrononcesProcess process = new IJEcheancesPrononcesProcess();

            process.setMoisTraitement("12.2008");
            process.setEmailAddress("hpe@globaz.ch");
            process.setSession((BSession) session);
            BProcessLauncher.start(process, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** Generate random integers in a certain range. */

    public void random1() {

        long START = 100000000;
        long END = 999999999;
        Random random = new Random();
        Main.showRandomInteger(START, END, random);
    }

}
