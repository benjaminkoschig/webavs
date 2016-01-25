package globaz.corvus.anakin;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.prestation.tools.PRDateFormater;
import java.rmi.RemoteException;
import org.apache.log4j.Logger;
import ch.admin.ofit.anakin.commum.Session;
import ch.admin.ofit.anakin.donnee.Annonce10eme;
import ch.admin.ofit.anakin.donnee.AnnonceAbstraite;
import ch.admin.ofit.arena.augmentation.controleur.ControleurAugmentationRente;

public class TestAnakinAdaptation {

    public static void main(String[] args) throws RemoteException, Exception {

        BISession session;
        BITransaction transaction = null;

        session = GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS)
                .newSession("ciciglo", "glob4az");
        transaction = ((BSession) session).newTransaction();
        transaction.openTransaction();

        // REcherche des annonces concernées

        REAnnoncesAugmentationModification10Eme arc4401 = new REAnnoncesAugmentationModification10Eme();
        arc4401.setSession((BSession) session);
        arc4401.setIdAnnonce("420176");
        arc4401.retrieve();

        arc4401.setAnneeCotClasseAge("43");

        REAnnoncesAugmentationModification10Eme arc4402 = new REAnnoncesAugmentationModification10Eme();
        arc4402.setSession((BSession) session);
        arc4402.setIdAnnonce(arc4401.getIdLienAnnonce());
        arc4402.retrieve();

        arc4402.setAnneeCotClasseAge("43");

        REArcConverter converter = new REArcConverter();
        AnnonceAbstraite aa = new Annonce10eme();

        aa = converter.convertToAnakinArc((BSession) session, arc4401, arc4402, "11.2010");

        // Passage dans le module de revalorisation

        ControleurAugmentationRente revalorisation = new ControleurAugmentationRente(
                Logger.getLogger(ControleurAugmentationRente.class.getName()));

        Session.getInstance().setParametre(ControleurAugmentationRente.class.getName(), "dateActuelle",
                PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM("12.2010")));

        AnnonceAbstraite aar = revalorisation.controle(aa);

        System.out.println("STOP");

        System.exit(-1);

    }

}
