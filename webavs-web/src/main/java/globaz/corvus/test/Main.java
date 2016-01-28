package globaz.corvus.test;

import globaz.corvus.api.arc.downloader.REDownloaderInscriptionsCI;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.annonces.REAnnonce53;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.module.calcul.api.REMontantPrestationAPIParPeriode;
import globaz.corvus.module.calcul.api.REPeriodeAPIComparator;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.rmi.RemoteException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Descpription
 * 
 * @author bsc Date de création 29 dec. 06
 */
public class Main {

    public static void insertAnnonce() {

        BISession session;
        BITransaction transaction = null;

        try {

            session = GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).newSession("hpe", "hpe");
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            REAnnonce53 annonce = new REAnnonce53();

            // Annonce header
            annonce.setCodeApplication("12345678");
            annonce.setCodeEnregistrement01("12345678");
            annonce.setNumeroCaisse("12345678");
            annonce.setNumeroAgence("12345678");

            // Annonce Abstract level 1A
            annonce.setIdTiers("112132");
            annonce.setNoAssAyantDroit("707.83.211.112");
            annonce.setMoisRapport("072007");
            annonce.setFinDroit("20071231");
            annonce.setPremierNoAssComplementaire("707.83.211.112");
            annonce.setSecondNoAssComplementaire("707.83.211.112");
            annonce.setEtat("12345678");
            annonce.setReferenceCaisseInterne("123456789".toUpperCase());
            // annonce.setIsRefugie(Boolean.TRUE);
            annonce.setCantonEtatDomicile("12345678");
            annonce.setGenrePrestation("12345678");
            annonce.setDebutDroit("20070101");
            annonce.setMensualitePrestationsFrancs("100.10");
            annonce.setCodeMutation("12345678");

            // Annonce abstract level 2A
            annonce.setAnneeNiveau("2007");
            annonce.setEchelleRente("12345678");
            annonce.setDureeCotManquante48_72("12345678");
            annonce.setAnneeCotClasseAge("12345678");
            annonce.setOfficeAICompetent("12345678");
            annonce.setDegreInvalidite("12345678");
            annonce.setCodeInfirmite("12345678");
            annonce.setSurvenanceEvenAssure("12345678");
            annonce.setAgeDebutInvalidite("12345678");
            annonce.setGenreDroitAPI("12345678");
            annonce.setReduction("12345678");
            annonce.setCasSpecial1("12345678");
            annonce.setCasSpecial2("12345678");
            annonce.setCasSpecial3("12346578");
            annonce.setCasSpecial4("12345678");
            annonce.setCasSpecial5("12346578");
            annonce.setDureeCotManquante73_78("12346578");
            annonce.setEtatNominatif("12345678");
            annonce.setEtatOrigine("12345678");
            annonce.setDureeCoEchelleRenteAv73("12346678");
            // annonce.setFractionRente("132");
            annonce.setDureeCoEchelleRenteDes73("12345678");
            annonce.setNombreAnneeBTE("12");
            annonce.setRamDeterminant("132");
            annonce.setDureeCotPourDetRAM("123");
            annonce.setDureeAjournement("132465");
            annonce.setSupplementAjournement("132");
            annonce.setDateRevocationAjournement("20071212");
            annonce.setNumeroAnnonce("132132");
            //
            // // Annonce Abstract Level 3A
            annonce.setCodeRevenuSplitte("132");
            annonce.setNbreAnneeBTA("132");
            annonce.setNbreAnneeBonifTrans("132456");
            annonce.setNbreAnneeAnticipation("132456");
            annonce.setReductionAnticipation("13254");
            annonce.setDateDebutAnticipation("20071231");
            // annonce.setIsSurvivant(Boolean.TRUE);

            // Annonce 53

            // transaction + session
            annonce.setSession((BSession) session);
            annonce.add(transaction);

            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            System.exit(0);
        }

    }

    public static void insertRenteAccordee() {

        BISession session;
        BITransaction transaction = null;

        try {

            session = GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).newSession("hpe", "hpe");
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RERenteAccordee renteAcc = new RERenteAccordee();

            renteAcc.add(transaction);

            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws RemoteException, Exception {

        BSession session = (BSession) GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).newSession(
                "globazf", "globazf");

        BTransaction transaction = (BTransaction) session.newTransaction();
        transaction.openTransaction();

        System.out.println("START ************************************************");

        REDownloaderInscriptionsCI rci = new REDownloaderInscriptionsCI(transaction, session);

        rci.download();

        if (transaction.hasErrors() || session.hasErrors()) {
            System.out.println("TRANSACTION ERROR ************************************************");
            System.out.println(transaction.getErrors().toString());

            System.out.println("SESSION ERROR ************************************************");
            System.out.println(session.getErrors().toString());
        }

        // transfert des logs
        System.out.println("LOG **************************************************");
        rci.getLog().printToConsole();

        System.out.println("LOG END **************************************************");

        System.out.println("STOP ************************************************");

        System.exit(-1);

    }

    /**
	 *
	 */
    public Main() {
        super();
    }

    public void testAcorParser() {

    }

    public void testBC() {

        BISession session;
        BITransaction transaction = null;

        try {

            session = GlobazSystem.getApplication("CORVUS").newSession("scr", "scr");
            BIApplication app = GlobazSystem.getApplication("CORVUS");
            session = app.newSession(session);
            transaction = ((BSession) session).newTransaction();

            transaction.openTransaction();

            REBasesCalculDixiemeRevision bc = new REBasesCalculDixiemeRevision();
            bc.setRevenuPrisEnCompte("150");
            bc.add(transaction);
            System.out.println("BC ID = " + bc.getIdBasesCalcul());
            transaction.commit();

        } catch (Exception e) {
            try {
                e.printStackTrace();
                transaction.rollback();
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        } finally {
            System.exit(0);
        }

    }

    public void testComparator() {

        SortedSet<REMontantPrestationAPIParPeriode> result = new TreeSet<REMontantPrestationAPIParPeriode>(
                new REPeriodeAPIComparator());
        REMontantPrestationAPIParPeriode elm = new REMontantPrestationAPIParPeriode();

        elm.setDateDebut("01.01.1999");
        elm.setTypePrestation("90");
        result.add(elm);

        elm = new REMontantPrestationAPIParPeriode();
        elm.setDateDebut("01.01.2000");
        elm.setTypePrestation("90");
        result.add(elm);

        elm = new REMontantPrestationAPIParPeriode();
        elm.setDateDebut("01.06.1999");
        elm.setTypePrestation("90");
        result.add(elm);

        elm = new REMontantPrestationAPIParPeriode();
        elm.setDateDebut("01.01.1999");
        elm.setTypePrestation("80");
        result.add(elm);

        elm = new REMontantPrestationAPIParPeriode();
        elm.setDateDebut("01.01.2000");
        elm.setTypePrestation("80");
        result.add(elm);

        elm = new REMontantPrestationAPIParPeriode();
        elm.setDateDebut("01.06.1999");
        elm.setTypePrestation("80");
        result.add(elm);

        elm = new REMontantPrestationAPIParPeriode();
        elm.setDateDebut("01.01.1999");
        elm.setTypePrestation("95");
        result.add(elm);

        elm = new REMontantPrestationAPIParPeriode();
        elm.setDateDebut("01.01.2000");
        elm.setTypePrestation("95");
        result.add(elm);

        elm = new REMontantPrestationAPIParPeriode();
        elm.setDateDebut("01.06.1999");
        elm.setTypePrestation("95");
        result.add(elm);

        REMontantPrestationAPIParPeriode[] array = result.toArray(new REMontantPrestationAPIParPeriode[result.size()]);

        for (int i = 0; i < array.length; i++) {
            REMontantPrestationAPIParPeriode pp = array[i];
            System.out.println(pp.getTypePrestation() + " - " + pp.getDateDebut());
        }

    }

}
