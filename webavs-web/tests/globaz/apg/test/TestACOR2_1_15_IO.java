package globaz.apg.test;

import globaz.apg.acor.parser.APACORPrestationsParser;
import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APEnfantAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.api.IPRSituationProfessionnelle;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class TestACOR2_1_15_IO {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String DOSSIER_ACOR = "c:\\program files\\acor";

    // setup
    // ---------------------------------------------------------------------

    private static BSession SESSION = null;

    // des affilies valides, le no d'affilie sur 12 positions et son nom sur 32
    // positions
    private static final String[][] SETUP_AFFILIES = {
            { "     000.646", "11", "3811", "Kaufmann-Bucher                 " },
            { "     000.596", "9", "3810", "Wernly-Raths                    " } };

    // statique
    // ------------------------------------------------------------------

    // une demande pour une FEMME !!!!!
    private static final String SETUP_DEMANDE = "8";
    private static String SORTIE_ACOR_APG_COMPLEXE;
    private static String SORTIE_ACOR_APG_SIMPLE;
    private static String SORTIE_ACOR_MAT_COMPLEXE;

    private static String SORTIE_ACOR_MAT_SIMPLE;

    static {
        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);

        // maternité simple

        writer.println("$c57500500354 980107200506102005  7840.00   0.00    0.00  0    0.001");
        writer.print("$e");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("5   0000000000000  7840.00 .0.0000     0.0010.0505  395.900.0100   78.401");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("0.0200    0.00");
        writer.println("$a57500500354    0.00     0.00 .0.0000     0.0020.0505    0.000.0000    0.001");
        writer.println("$p         .090107200531072005 311  80.00  0.00 0   80.00  2480.00    0.00     0.00 100.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("  2480.00");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("$p         .090108200531082005 311  80.00  0.00 0   80.00  2480.00    0.00     0.00 100.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("  2480.00");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("$p         .090109200530092005 301  80.00  0.00 0   80.00  2400.00    0.00     0.00 100.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("  2400.00");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("$p         .090110200506102005  61  80.00  0.00 0   80.00   480.00    0.00     0.00 100.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("   480.00");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("$l1111111111311011911                                ");
        writer.println("$k1111111112101072005truc,machin                     ");
        writer.println("$m0107200506102005   100.00    80.00     0.00    80.00 36000.00");
        writer.print("$n");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("1.0000000  3000.005     0.00     0.001     0.0001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("36000.00");
        writer.print("$v01072005");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("  2480.000.0000000     0.000.0000000     0.00 00.0000000     0.00  2480.00  2480.00");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("0.0000000     0.00");
        writer.println("$g0107200531072005  31    80.001.0000000  2480.00");
        writer.print("$v01082005");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("  2480.000.0000000     0.000.0000000     0.00 00.0000000     0.00  2480.00  2480.00");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("0.0000000     0.00");
        writer.println("$g0108200531082005  31    80.001.0000000  2480.00");
        writer.print("$v01092005");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("  2400.000.0000000     0.000.0000000     0.00 00.0000000     0.00  2400.00  2400.00");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("0.0000000     0.00");
        writer.println("$g0109200530092005  30    80.001.0000000  2400.00");
        writer.print("$v01102005");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("   480.000.0000000     0.000.0000000     0.00 00.0000000     0.00   480.00   480.00");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println(" 0.0000000     0.00");
        writer.println("$g0110200506102005   6    80.001.0000000   480.00");

        TestACOR2_1_15_IO.SORTIE_ACOR_MAT_SIMPLE = buffer.getBuffer().toString();

        // maternité complexe

        buffer.getBuffer().setLength(0);

        writer.println("$c10664749314 980107200506102005 13092.80   0.00    0.00  0    0.001");
        writer.print("$e");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("5   0000000000000    29.40 .0.0000     0.0010.0505    1.500.0100    0.301");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("0.0200    0.00");
        writer.println("$a11111111113    0.00 13063.40 .0.0000     0.0020.0505  659.700.0100  130.651");
        writer.println("$p         .090107200531072005 311 133.60  0.00 0  133.60  4141.60    0.00  4132.30 167.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("     9.30");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("$p         .090108200531082005 311 133.60  0.00 0  133.60  4141.60    0.00  4132.30 167.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("     9.30");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("$p         .090109200530092005 301 133.60  0.00 0  133.60  4008.00    0.00  3999.00 167.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("     9.00");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("$p         .090110200506102005  61 133.60  0.00 0  133.60   801.60    0.00   799.80 167.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("     1.80");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("$l1111111111311011911                                ");
        writer.println("$k1000000000601072005truc,machin              ");
        writer.println("$m0107200506102005   167.00   133.60     0.00   133.60 60000.00");
        writer.print("$n");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("0.6000000  3000.005     0.00     0.001     0.0001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("36000.00");
        writer.print("$n");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("0.4000000  2000.005     0.00     0.001     0.0001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("24000.00");
        writer.println("$v01072005 11111111113  4132.300.0505000   208.700.0100000    41.30 00.0000000     0.00  3882.30  3882.30                                0.0000000     0.00");
        writer.println("$g0107200531072005  31   133.300.9977545  4132.30");
        writer.println("$v01082005 11111111113  4132.300.0505000   208.700.0100000    41.30 00.0000000     0.00  3882.30  3882.30                                0.0000000     0.00");
        writer.println("$g0108200531082005  31   133.300.9977545  4132.30");
        writer.println("$v01092005 11111111113  3999.000.0505000   201.950.0100000    40.00 00.0000000     0.00  3757.05  3757.05                                0.0000000     0.00");
        writer.println("$g0109200530092005  30   133.300.9977545  3999.00");
        writer.print("$v01102005");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("    29.400.0000000     0.000.0000000     0.00 00.0000000     0.00    29.40    29.40");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("0.0000000     0.00");
        writer.println("$g0107200531072005  31     0.300.0022455     9.30");
        writer.println("$g0108200531082005  31     0.300.0022455     9.30");
        writer.println("$g0109200530092005  30     0.300.0022455     9.00");
        writer.println("$g0110200506102005   6     0.300.0022455     1.80");
        writer.println("$v01102005 11111111113   799.800.0505000    40.400.0100000     8.00 00.0000000     0.00   751.40   751.40                                0.0000000     0.00");
        writer.println("$g0110200506102005   6   133.300.9977545   799.80");

        TestACOR2_1_15_IO.SORTIE_ACOR_MAT_COMPLEXE = buffer.getBuffer().toString();

        // apg simple

        buffer.getBuffer().setLength(0);

        writer.println("$c92131234114 310107200531072005  4340.00  59.00  300.00  0    0.001");
        writer.print("$e");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("50101200531122005  3100.00 .0.0000     0.0010.0505    0.000.0100    0.001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("0.0200    0.00");
        writer.println("$a92131234114  300.00  1540.00 .0.0000     0.0020.0505   62.600.0100   12.402");
        writer.println("$p         .020107200531072005 311  80.00 18.00 1  140.00  4340.00    0.00  1240.00 100.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("  3100.00");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("$m0107200531072005   100.00    80.00     0.00   140.00 36000.00");
        writer.print("$n");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("1.0000000  3000.005     0.00     0.001     0.0001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("36000.00");

        TestACOR2_1_15_IO.SORTIE_ACOR_APG_SIMPLE = buffer.getBuffer().toString();

        // apg complexe

        buffer.getBuffer().setLength(0);

        writer.println("$c92131234114 300107200531072005  4871.40   0.00    0.00  0    0.001");
        writer.print("$e");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("50101200519072005  1549.65 .0.0000     0.0010.0505    0.000.0100    0.001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("0.0200    0.00");
        writer.print("$e");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("50101200531122005  2922.75 .0.0000     0.0010.0505    0.000.0100    0.001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("0.0200    0.00");
        writer.println("$a92131234114    0.00   399.00 .0.0000     0.0020.0505   20.150.0100    4.002");
        writer.println("$p         .022607200531072005  61 133.60 36.00 2  167.00  1002.00    0.00     0.00 167.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("   601.20");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("   400.80");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("$p         .022007200525072005  61 133.60 36.00 2  167.00  1002.00    0.00   399.00 167.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("   601.20");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("     1.80");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("$p         .021007200519072005  91 133.60 36.00 2  167.00  1503.00    0.00     0.00 167.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("   901.80");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("   601.20");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("$p         .020107200509072005  91 133.60 18.00 1  151.60  1364.40    0.00     0.00 167.00");
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("   818.55");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.print("$f");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("   545.85");
        writer.println(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("$m0107200509072005   167.00   133.60     0.00   151.60 60000.00");
        writer.print("$n");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("0.6000000  3000.005     0.00     0.001     0.0001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("36000.00");
        writer.print("$n");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("0.4000000  2000.005     0.00     0.000     0.0001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("24000.00");
        writer.println("$m1007200531072005   167.00   133.60     0.00   167.00 60000.00");
        writer.print("$n");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][0]);
        writer.print("0.6000000  3000.005     0.00     0.001     0.0001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[0][3]);
        writer.println("36000.00");
        writer.print("$n");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][0]);
        writer.print("0.4000000  2000.005     0.00     0.000     0.0001");
        writer.print(TestACOR2_1_15_IO.SETUP_AFFILIES[1][3]);
        writer.println("24000.00");

        TestACOR2_1_15_IO.SORTIE_ACOR_APG_COMPLEXE = buffer.getBuffer().toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for globaz.apg.test");

        // suite.addTestSuite(TestACOR2_1_15_IO.class);

        return suite;
    }

    private APDroitLAPG creerDroitAPGComplexe(BTransaction transaction) throws Exception {
        // droit
        APDroitAPG droit = new APDroitAPG();

        droit.setSession(TestACOR2_1_15_IO.SESSION);
        droit.setDateDebutDroit("01.07.2005");
        droit.setDateDepot("01.07.2005");
        droit.setDateReception("01.07.2005");
        droit.setDateFinDroit("31.07.2005");
        droit.setDuplicata(Boolean.FALSE);
        droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droit.setGenreService(IAPDroitLAPG.CS_SERVICE_AVANCEMENT);
        droit.setNbrJourSoldes("31");
        droit.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
        droit.setNpa("1008");
        droit.setPays("100");
        droit.setIdDemande(TestACOR2_1_15_IO.SETUP_DEMANDE);
        droit.wantCallValidate(false);
        droit.add(transaction);
        droit.wantCallValidate(true);

        // situation familliale
        APSituationFamilialeAPG sitFam = new APSituationFamilialeAPG();

        sitFam.setSession(TestACOR2_1_15_IO.SESSION);
        sitFam.setIdSitFamAPG(droit.getIdSituationFam());
        sitFam.retrieve(transaction);
        sitFam.setFraisGarde("300");
        sitFam.setNbrEnfantsDebutDroit("1");
        sitFam.update(transaction);

        // enfant
        APEnfantAPG enfant = new APEnfantAPG();

        enfant.setSession(TestACOR2_1_15_IO.SESSION);
        enfant.setIdSituationFamiliale(sitFam.getIdSitFamAPG());
        enfant.setDateDebutDroit("10.07.2005");
        enfant.add(transaction);

        // periode
        APPeriodeAPG periodeAPG = new APPeriodeAPG();

        periodeAPG.setSession(TestACOR2_1_15_IO.SESSION);
        periodeAPG.setDateDebutPeriode("01.07.2005");
        periodeAPG.setDateFinPeriode("31.07.2005");
        periodeAPG.setIdDroit(droit.getIdDroit());
        periodeAPG.add(transaction);
        droit.update(transaction);

        // situation pro 1
        APEmployeur employeur = new APEmployeur();

        employeur.setSession(TestACOR2_1_15_IO.SESSION);
        employeur.setIdAffilie(TestACOR2_1_15_IO.SETUP_AFFILIES[0][1]);
        employeur.setIdTiers(TestACOR2_1_15_IO.SETUP_AFFILIES[0][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(TestACOR2_1_15_IO.SESSION);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("3000.00");
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        // situation pro 2
        employeur = new APEmployeur();

        employeur.setSession(TestACOR2_1_15_IO.SESSION);
        employeur.setIdAffilie(TestACOR2_1_15_IO.SETUP_AFFILIES[1][1]);
        employeur.setIdTiers(TestACOR2_1_15_IO.SETUP_AFFILIES[1][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(TestACOR2_1_15_IO.SESSION);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("2000.00");
        situationProfessionnelle.setMontantVerse("10.00");
        situationProfessionnelle.setDateDebut("20.07.2005");
        situationProfessionnelle.setDateFin("25.07.2005");
        situationProfessionnelle.setPeriodiciteMontantVerse(IPRSituationProfessionnelle.CS_PERIODICITE_MOIS);
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        return droit;
    }

    private APDroitLAPG creerDroitAPGSimple(BTransaction transaction) throws Exception {
        // droit
        APDroitAPG droit = new APDroitAPG();

        droit.setSession(TestACOR2_1_15_IO.SESSION);
        droit.setDateDebutDroit("01.07.2005");
        droit.setDateDepot("01.07.2005");
        droit.setDateReception("01.07.2005");
        droit.setDateFinDroit("31.07.2005");
        droit.setDuplicata(Boolean.FALSE);
        droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droit.setGenreService(IAPDroitLAPG.CS_SERVICE_AVANCEMENT);
        droit.setNbrJourSoldes("31");
        droit.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
        droit.setNpa("1008");
        droit.setPays("100");
        droit.setIdDemande(TestACOR2_1_15_IO.SETUP_DEMANDE);
        droit.wantCallValidate(false);
        droit.add(transaction);
        droit.wantCallValidate(true);

        // situation familliale
        APSituationFamilialeAPG sitFam = new APSituationFamilialeAPG();

        sitFam.setSession(TestACOR2_1_15_IO.SESSION);
        sitFam.setIdSitFamAPG(droit.getIdSituationFam());
        sitFam.retrieve(transaction);
        sitFam.setFraisGarde("300");
        sitFam.setNbrEnfantsDebutDroit("1");
        sitFam.update(transaction);

        // periode
        APPeriodeAPG periodeAPG = new APPeriodeAPG();

        periodeAPG.setSession(TestACOR2_1_15_IO.SESSION);
        periodeAPG.setDateDebutPeriode("01.07.2005");
        periodeAPG.setDateFinPeriode("31.07.2005");
        periodeAPG.setIdDroit(droit.getIdDroit());
        periodeAPG.add(transaction);
        droit.update(transaction);

        // situation pro
        APEmployeur employeur = new APEmployeur();

        employeur.setSession(TestACOR2_1_15_IO.SESSION);
        employeur.setIdAffilie(TestACOR2_1_15_IO.SETUP_AFFILIES[0][1]);
        employeur.setIdTiers(TestACOR2_1_15_IO.SETUP_AFFILIES[0][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(TestACOR2_1_15_IO.SESSION);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("3000.00");
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        return droit;
    }

    private APDroitLAPG creerDroitMaterniteComplexe(BTransaction transaction) throws Exception {
        // droit
        APDroitMaternite droit = new APDroitMaternite();

        droit.setSession(TestACOR2_1_15_IO.SESSION);
        droit.setDateDebutDroit("01.07.2005");
        droit.setDateDepot("01.07.2005");
        droit.setDateReception("01.07.2005");
        droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
        droit.setNpa("1008");
        droit.setPays("100");
        droit.setIdDemande(TestACOR2_1_15_IO.SETUP_DEMANDE);
        droit.add(transaction);

        // situation familliale
        APSituationFamilialeMat situationFamilialeMat = new APSituationFamilialeMat();

        situationFamilialeMat.setSession(TestACOR2_1_15_IO.SESSION);
        situationFamilialeMat.setIdDroitMaternite(droit.getIdDroit());
        situationFamilialeMat.setNom("truc");
        situationFamilialeMat.setPrenom("machin");
        situationFamilialeMat.setNoAVS("111.11.111.121");
        situationFamilialeMat.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
        situationFamilialeMat.add(transaction);

        // situation pro 1
        APEmployeur employeur = new APEmployeur();

        employeur.setSession(TestACOR2_1_15_IO.SESSION);
        employeur.setIdAffilie(TestACOR2_1_15_IO.SETUP_AFFILIES[0][1]);
        employeur.setIdTiers(TestACOR2_1_15_IO.SETUP_AFFILIES[0][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(TestACOR2_1_15_IO.SESSION);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("3000.00");
        situationProfessionnelle.setIsVersementEmployeur(Boolean.FALSE);
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        // situation pro 2
        employeur = new APEmployeur();

        employeur.setSession(TestACOR2_1_15_IO.SESSION);
        employeur.setIdAffilie(TestACOR2_1_15_IO.SETUP_AFFILIES[1][1]);
        employeur.setIdTiers(TestACOR2_1_15_IO.SETUP_AFFILIES[1][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(TestACOR2_1_15_IO.SESSION);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("2000.00");
        situationProfessionnelle.setMontantVerse("10.00");
        situationProfessionnelle.setPeriodiciteMontantVerse(IPRSituationProfessionnelle.CS_PERIODICITE_MOIS);
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        return droit;
    }

    private APDroitLAPG creerDroitMaterniteComplexeAvecDatesMontantVerse(BTransaction transaction) throws Exception {
        // droit
        APDroitMaternite droit = new APDroitMaternite();

        droit.setSession(TestACOR2_1_15_IO.SESSION);
        droit.setDateDebutDroit("01.07.2005");
        droit.setDateDepot("01.07.2005");
        droit.setDateReception("01.07.2005");
        droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
        droit.setNpa("1008");
        droit.setPays("100");
        droit.setIdDemande(TestACOR2_1_15_IO.SETUP_DEMANDE);
        droit.add(transaction);

        // situation familliale
        APSituationFamilialeMat situationFamilialeMat = new APSituationFamilialeMat();

        situationFamilialeMat.setSession(TestACOR2_1_15_IO.SESSION);
        situationFamilialeMat.setIdDroitMaternite(droit.getIdDroit());
        situationFamilialeMat.setNom("truc");
        situationFamilialeMat.setPrenom("machin");
        situationFamilialeMat.setNoAVS("111.11.111.121");
        situationFamilialeMat.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
        situationFamilialeMat.add(transaction);

        // situation pro 1
        APEmployeur employeur = new APEmployeur();

        employeur.setSession(TestACOR2_1_15_IO.SESSION);
        employeur.setIdAffilie(TestACOR2_1_15_IO.SETUP_AFFILIES[0][1]);
        employeur.setIdTiers(TestACOR2_1_15_IO.SETUP_AFFILIES[0][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(TestACOR2_1_15_IO.SESSION);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("3000.00");
        situationProfessionnelle.setIsVersementEmployeur(Boolean.FALSE);
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        // situation pro 2
        employeur = new APEmployeur();

        employeur.setSession(TestACOR2_1_15_IO.SESSION);
        employeur.setIdAffilie(TestACOR2_1_15_IO.SETUP_AFFILIES[1][1]);
        employeur.setIdTiers(TestACOR2_1_15_IO.SETUP_AFFILIES[1][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(TestACOR2_1_15_IO.SESSION);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("2000.00");
        situationProfessionnelle.setMontantVerse("10.00");
        situationProfessionnelle.setPeriodiciteMontantVerse(IPRSituationProfessionnelle.CS_PERIODICITE_MOIS);
        situationProfessionnelle.setDateDebut("10.08.2005");
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        return droit;
    }

    private APDroitLAPG creerDroitMaterniteSimple(BTransaction transaction) throws Exception {
        // droit
        APDroitMaternite droit = new APDroitMaternite();

        droit.setSession(TestACOR2_1_15_IO.SESSION);
        droit.setDateDebutDroit("01.07.2005");
        droit.setDateDepot("01.07.2005");
        droit.setDateReception("01.07.2005");
        droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
        droit.setNpa("1008");
        droit.setPays("100");
        droit.setIdDemande(TestACOR2_1_15_IO.SETUP_DEMANDE);
        droit.add(transaction);

        // situation familliale
        APSituationFamilialeMat situationFamilialeMat = new APSituationFamilialeMat();

        situationFamilialeMat.setSession(TestACOR2_1_15_IO.SESSION);
        situationFamilialeMat.setIdDroitMaternite(droit.getIdDroit());
        situationFamilialeMat.setNom("truc");
        situationFamilialeMat.setPrenom("machin");
        situationFamilialeMat.setNoAVS("111.11.111.121");
        situationFamilialeMat.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
        situationFamilialeMat.add(transaction);

        // situation pro
        APEmployeur employeur = new APEmployeur();

        employeur.setSession(TestACOR2_1_15_IO.SESSION);
        employeur.setIdAffilie(TestACOR2_1_15_IO.SETUP_AFFILIES[0][1]);
        employeur.setIdTiers(TestACOR2_1_15_IO.SETUP_AFFILIES[0][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(TestACOR2_1_15_IO.SESSION);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("3000.00");
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        return droit;
    }

    /**
     * setter pour l'attribut up
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    // @Override
    // protected void setUp() throws Exception {
    // if (TestACOR2_1_15_IO.SESSION == null) {
    // TestACOR2_1_15_IO.SESSION = TestAll.createSession();
    // }
    // }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testExportAPGComplexe() throws Exception {
        BTransaction transaction = (BTransaction) TestACOR2_1_15_IO.SESSION.newTransaction();
        APDroitLAPG droit;

        try {
            transaction.openTransaction();
            droit = creerDroitAPGComplexe(transaction);

            // exportation vers ACOR
            StringWriter out = new StringWriter();
            PrintWriter writer = new PrintWriter(out);

            // APACORBatchFilePrinter.getInstance().printBatchFile(writer, SESSION, droit, DOSSIER_ACOR);

            // assertions
            StringBuffer resultat = out.getBuffer();

            Assert.assertTrue(resultat.length() != 0);

            // TODO: aller fouiller dans le resultat
        } finally {
            try {
                transaction.rollback();
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testExportAPGSimple() throws Exception {
        BTransaction transaction = (BTransaction) TestACOR2_1_15_IO.SESSION.newTransaction();
        APDroitLAPG droit;

        try {
            transaction.openTransaction();
            droit = creerDroitAPGSimple(transaction);

            // exportation vers ACOR
            StringWriter out = new StringWriter();
            PrintWriter writer = new PrintWriter(out);

            // APACORBatchFilePrinter.getInstance().printBatchFile(writer, SESSION, droit, DOSSIER_ACOR);

            // assertions
            StringBuffer resultat = out.getBuffer();

            Assert.assertTrue(resultat.length() != 0);

            // TODO: aller fouiller dans le resultat
        } finally {
            try {
                transaction.rollback();
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testExportMaterniteComplexe() throws Exception {
        BTransaction transaction = (BTransaction) TestACOR2_1_15_IO.SESSION.newTransaction();
        APDroitLAPG droit;

        try {
            transaction.openTransaction();
            droit = creerDroitMaterniteComplexe(transaction);

            // exportation vers ACOR
            StringWriter out = new StringWriter();
            PrintWriter writer = new PrintWriter(out);

            // APACORBatchFilePrinter.getInstance().printBatchFile(writer, SESSION, droit, DOSSIER_ACOR);

            // assertions
            StringBuffer resultat = out.getBuffer();

            Assert.assertTrue(resultat.length() != 0);

            // TODO: aller fouiller dans le resultat
        } finally {
            try {
                transaction.rollback();
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testExportMaterniteComplexeAvecDatesMontantVerse() throws Exception {
        Assert.fail("ACOR n'accepte pas des dates de montant verse pour des droits maternité !!!");
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testExportMaterniteSimple() throws Exception {
        BTransaction transaction = (BTransaction) TestACOR2_1_15_IO.SESSION.newTransaction();
        APDroitLAPG droit;

        try {
            transaction.openTransaction();
            droit = creerDroitMaterniteSimple(transaction);

            // exportation vers ACOR
            StringWriter out = new StringWriter();
            PrintWriter writer = new PrintWriter(out);

            // APACORBatchFilePrinter.getInstance().printBatchFile(writer, SESSION, droit, DOSSIER_ACOR);

            // assertions
            StringBuffer resultat = out.getBuffer();

            Assert.assertTrue(resultat.length() != 0);

            // TODO: aller fouiller dans le resultat
        } finally {
            try {
                transaction.rollback();
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testImportAPGComplexe() throws Exception {
        BTransaction transaction = (BTransaction) TestACOR2_1_15_IO.SESSION.newTransaction();
        APDroitLAPG droit;

        try {
            transaction.openTransaction();
            droit = creerDroitAPGComplexe(transaction);

            // génération
            List bc = new APBasesCalculBuilder(TestACOR2_1_15_IO.SESSION, droit).createBasesCalcul();
            Collection pw = APACORPrestationsParser.parse(droit, bc, TestACOR2_1_15_IO.SESSION, new StringReader(
                    TestACOR2_1_15_IO.SORTIE_ACOR_APG_COMPLEXE));

            new APCalculateurPrestationStandardLamatAcmAlpha().reprendreDepuisACOR(TestACOR2_1_15_IO.SESSION, pw,
                    droit, new FWCurrency(300));

            // test
            APPrestationManager mgr = new APPrestationManager();

            mgr.setSession(TestACOR2_1_15_IO.SESSION);
            mgr.setForIdDroit(droit.getIdDroit());
            mgr.setOrderBy(APPrestation.FIELDNAME_DATEDEBUT);
            mgr.find(transaction);

            Assert.assertEquals(4, mgr.size());

            // première prestation
            // ------------------------------------------------------------------------------------
            APPrestation prestation = (APPrestation) mgr.get(0);

            Assert.assertEquals("1364.40", prestation.getMontantBrut());
            Assert.assertEquals("01.07.2005", prestation.getDateDebut());
            Assert.assertEquals("9", prestation.getNombreJoursSoldes());

            // répartition de paiement
            APRepartitionPaiementsManager rpMgr = new APRepartitionPaiementsManager();

            rpMgr.setSession(TestACOR2_1_15_IO.SESSION);
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals(3, rpMgr.size()); // 2 employeurs + frais garde

            // valeurs des répartitions
            HashSet reps = new HashSet();

            reps.add("818.55");
            reps.add("545.85");
            reps.add("90.00");

            for (int idRep = 0; idRep < rpMgr.size(); ++idRep) {
                APRepartitionPaiements rep = (APRepartitionPaiements) rpMgr.get(idRep);

                if (reps.contains(rep.getMontantBrut())) {
                    reps.remove(rep.getMontantBrut());
                } else {
                    Assert.fail("il manque une des répartitions pour la première prestation");
                }
            }

            if (!reps.isEmpty()) {
                Assert.fail("il manque une des répartitions pour la première prestation");
            }

            // deuxième prestation
            // ------------------------------------------------------------------------------------
            prestation = (APPrestation) mgr.get(1);

            Assert.assertEquals("1503.00", prestation.getMontantBrut());
            Assert.assertEquals("10.07.2005", prestation.getDateDebut());
            Assert.assertEquals("9", prestation.getNombreJoursSoldes());

            // répartition de paiement
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals(3, rpMgr.size()); // 2 employeurs + frais garde

            // valeurs des répartitions
            reps.clear();

            reps.add("901.80");
            reps.add("601.20");
            reps.add("90.00");

            for (int idRep = 0; idRep < rpMgr.size(); ++idRep) {
                APRepartitionPaiements rep = (APRepartitionPaiements) rpMgr.get(idRep);

                if (reps.contains(rep.getMontantBrut())) {
                    reps.remove(rep.getMontantBrut());
                } else {
                    Assert.fail("il manque une des répartitions pour la deuxième prestation");
                }
            }

            if (!reps.isEmpty()) {
                Assert.fail("il manque une des répartitions pour la deuxième prestation");
            }

            // troisième prestation
            // ------------------------------------------------------------------------------------
            prestation = (APPrestation) mgr.get(2);

            Assert.assertEquals("1002.00", prestation.getMontantBrut());
            Assert.assertEquals("20.07.2005", prestation.getDateDebut());
            Assert.assertEquals("6", prestation.getNombreJoursSoldes());

            // répartition de paiement
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals(4, rpMgr.size()); // 2 employeur + assuré + frais garde

            // valeurs des répartitions
            reps.clear();

            reps.add("601.20");
            reps.add("1.80");
            reps.add("399.00");
            reps.add("60.00");

            for (int idRep = 0; idRep < rpMgr.size(); ++idRep) {
                APRepartitionPaiements rep = (APRepartitionPaiements) rpMgr.get(idRep);

                if (reps.contains(rep.getMontantBrut())) {
                    reps.remove(rep.getMontantBrut());
                } else {
                    Assert.fail("il manque une des répartitions pour la troisième prestation");
                }
            }

            if (!reps.isEmpty()) {
                Assert.fail("il manque une des répartitions pour la troisième prestation");
            }

            // quatrième prestation
            // ------------------------------------------------------------------------------------
            prestation = (APPrestation) mgr.get(3);

            Assert.assertEquals("1002.00", prestation.getMontantBrut());
            Assert.assertEquals("26.07.2005", prestation.getDateDebut());
            Assert.assertEquals("6", prestation.getNombreJoursSoldes());

            // répartition de paiement
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals(3, rpMgr.size()); // employeur + assuré

            // valeurs des répartitions
            reps.clear();

            reps.add("601.20");
            reps.add("400.80");
            reps.add("60.00");

            for (int idRep = 0; idRep < rpMgr.size(); ++idRep) {
                APRepartitionPaiements rep = (APRepartitionPaiements) rpMgr.get(idRep);

                if (reps.contains(rep.getMontantBrut())) {
                    reps.remove(rep.getMontantBrut());
                } else {
                    Assert.fail("il manque une des répartitions pour la quatrième prestation");
                }
            }

            if (!reps.isEmpty()) {
                Assert.fail("il manque une des répartitions pour la quatrième prestation");
            }
        } finally {
            try {
                transaction.rollback();
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testImportAPGSimple() throws Exception {
        BTransaction transaction = (BTransaction) TestACOR2_1_15_IO.SESSION.newTransaction();
        APDroitLAPG droit;

        try {
            transaction.openTransaction();
            droit = creerDroitAPGSimple(transaction);

            // génération
            List bc = new APBasesCalculBuilder(TestACOR2_1_15_IO.SESSION, droit).createBasesCalcul();
            Collection pw = APACORPrestationsParser.parse(droit, bc, TestACOR2_1_15_IO.SESSION, new StringReader(
                    TestACOR2_1_15_IO.SORTIE_ACOR_APG_SIMPLE));

            new APCalculateurPrestationStandardLamatAcmAlpha().reprendreDepuisACOR(TestACOR2_1_15_IO.SESSION, pw,
                    droit, new FWCurrency(300));

            // test
            APPrestationManager mgr = new APPrestationManager();

            mgr.setSession(TestACOR2_1_15_IO.SESSION);
            mgr.setForIdDroit(droit.getIdDroit());
            mgr.find(transaction);

            Assert.assertEquals(1, mgr.size());

            // première et seule prestation
            APPrestation prestation = (APPrestation) mgr.get(0);

            Assert.assertEquals("4340.00", prestation.getMontantBrut());
            Assert.assertEquals("01.07.2005", prestation.getDateDebut());
            Assert.assertEquals("31", prestation.getNombreJoursSoldes());

            // répartition de paiement
            APRepartitionPaiementsManager rpMgr = new APRepartitionPaiementsManager();

            rpMgr.setSession(TestACOR2_1_15_IO.SESSION);
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals(3, rpMgr.size()); // employeur + assuré

            // première répartition, les frais de garde
            APRepartitionPaiements rp = (APRepartitionPaiements) rpMgr.get(0);

            Assert.assertEquals("300.00", rp.getMontantBrut());

            // première répartition, l'employeur
            rp = (APRepartitionPaiements) rpMgr.get(1);

            Assert.assertEquals("3100.00", rp.getMontantBrut());

            // 2eme répartition, l'assuré
            rp = (APRepartitionPaiements) rpMgr.get(2);

            Assert.assertEquals("1240.00", rp.getMontantBrut());
        } finally {
            try {
                transaction.rollback();
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testImportMaterniteComplexe() throws Exception {
        BTransaction transaction = (BTransaction) TestACOR2_1_15_IO.SESSION.newTransaction();
        APDroitLAPG droit;

        try {
            transaction.openTransaction();
            droit = creerDroitMaterniteComplexe(transaction);

            // génération
            List bc = new APBasesCalculBuilder(TestACOR2_1_15_IO.SESSION, droit).createBasesCalcul();
            Collection pw = APACORPrestationsParser.parse(droit, bc, TestACOR2_1_15_IO.SESSION, new StringReader(
                    TestACOR2_1_15_IO.SORTIE_ACOR_MAT_COMPLEXE));

            new APCalculateurPrestationStandardLamatAcmAlpha().reprendreDepuisACOR(TestACOR2_1_15_IO.SESSION, pw,
                    droit, new FWCurrency(300));

            // test
            APPrestationManager mgr = new APPrestationManager();
            APRepartitionPaiementsManager rpMgr = new APRepartitionPaiementsManager();

            rpMgr.setSession(TestACOR2_1_15_IO.SESSION);
            mgr.setSession(TestACOR2_1_15_IO.SESSION);
            mgr.setForIdDroit(droit.getIdDroit());
            mgr.setOrderBy(APPrestation.FIELDNAME_DATEDEBUT);
            mgr.find(transaction);

            Assert.assertEquals("nombre de prestations différent d'attendu", 4, mgr.size()); // 4
            // prestations

            // première prestation
            // ------------------------------------------------------------------------------------
            APPrestation prestation = (APPrestation) mgr.get(0);

            Assert.assertEquals("montant première prestation différent d'attendu", "4141.60",
                    prestation.getMontantBrut());
            Assert.assertEquals("date début première prestation différent d'attendu", "01.07.2005",
                    prestation.getDateDebut());
            Assert.assertEquals("nb jours soldés première prestation différent d'attendu", "31",
                    prestation.getNombreJoursSoldes());

            // répartitions de paiements pour la première prestation
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals("nombre de répartitions de paiements pour première prestation différent de attendu", 3,
                    rpMgr.size());

            // valeurs de repartitions
            HashSet reps = new HashSet();

            reps.add("94.90");
            reps.add("9.30");
            reps.add("4132.30");

            for (int idRep = 0; idRep < rpMgr.size(); ++idRep) {
                APRepartitionPaiements rep = (APRepartitionPaiements) rpMgr.get(idRep);

                if (reps.contains(rep.getMontantBrut())) {
                    reps.remove(rep.getMontantBrut());
                } else {
                    Assert.fail("il manque une des répartitions pour la première prestation");
                }
            }

            if (!reps.isEmpty()) {
                Assert.fail("il manque une des répartitions pour la première prestation");
            }

            // deuxième prestation
            // ------------------------------------------------------------------------------------
            prestation = (APPrestation) mgr.get(1);

            Assert.assertEquals("montant deuxième prestation différent d'attendu", "4141.60",
                    prestation.getMontantBrut());
            Assert.assertEquals("date début deuxième prestation différent d'attendu", "01.08.2005",
                    prestation.getDateDebut());
            Assert.assertEquals("nb jours soldés deuxième prestation différent d'attendu", "31",
                    prestation.getNombreJoursSoldes());

            // répartitions de paiements pour la deuxième prestation
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals("nombre de répartitions de paiements pour deuxième prestation différent de attendu", 3,
                    rpMgr.size());

            // valeurs de repartitions
            reps.clear();

            reps.add("94.90");
            reps.add("9.30");
            reps.add("4132.30");

            for (int idRep = 0; idRep < rpMgr.size(); ++idRep) {
                APRepartitionPaiements rep = (APRepartitionPaiements) rpMgr.get(idRep);

                if (reps.contains(rep.getMontantBrut())) {
                    reps.remove(rep.getMontantBrut());
                } else {
                    Assert.fail("il manque une des répartitions pour la deuxième prestation");
                }
            }

            if (!reps.isEmpty()) {
                Assert.fail("il manque une des répartitions pour la deuxième prestation");
            }

            // troisième prestation
            // -----------------------------------------------------------------------------------
            prestation = (APPrestation) mgr.get(2);

            Assert.assertEquals("montant troisième prestation différent d'attendu", "4008.00",
                    prestation.getMontantBrut());
            Assert.assertEquals("date début troisième prestation différent d'attendu", "01.09.2005",
                    prestation.getDateDebut());
            Assert.assertEquals("nb jours soldés troisième prestation différent d'attendu", "30",
                    prestation.getNombreJoursSoldes());

            // répartitions de paiements pour la troisième prestation
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals("nombre de répartitions de paiements pour troisième prestation différent de attendu",
                    3, rpMgr.size());

            // valeurs de repartitions
            reps.clear();

            reps.add("91.85");
            reps.add("9.00");
            reps.add("3999.00");

            for (int idRep = 0; idRep < rpMgr.size(); ++idRep) {
                APRepartitionPaiements rep = (APRepartitionPaiements) rpMgr.get(idRep);

                if (reps.contains(rep.getMontantBrut())) {
                    reps.remove(rep.getMontantBrut());
                } else {
                    Assert.fail("il manque une des répartitions pour la troisième prestation");
                }
            }

            if (!reps.isEmpty()) {
                Assert.fail("il manque une des répartitions pour la troisième prestation");
            }

            // quatrième prestation
            // -----------------------------------------------------------------------------------
            prestation = (APPrestation) mgr.get(3);

            Assert.assertEquals("montant quatrième prestation différent d'attendu", "801.60",
                    prestation.getMontantBrut());
            Assert.assertEquals("date début quatrième prestation différent d'attendu", "01.10.2005",
                    prestation.getDateDebut());
            Assert.assertEquals("nb jours soldés quatrième prestation différent d'attendu", "6",
                    prestation.getNombreJoursSoldes());

            // répartitions de paiements pour la deuxième prestation
            rpMgr.setForIdPrestation(prestation.getIdPrestationApg());
            rpMgr.find(transaction);

            Assert.assertEquals("nombre de répartitions de paiements pour quatrième prestation différent de attendu",
                    3, rpMgr.size());

            // valeurs de repartitions
            reps.clear();

            reps.add("18.35");
            reps.add("1.80");
            reps.add("799.80");

            for (int idRep = 0; idRep < rpMgr.size(); ++idRep) {
                APRepartitionPaiements rep = (APRepartitionPaiements) rpMgr.get(idRep);

                if (reps.contains(rep.getMontantBrut())) {
                    reps.remove(rep.getMontantBrut());
                } else {
                    Assert.fail("il manque une des répartitions pour la quatrième prestation");
                }
            }

            if (!reps.isEmpty()) {
                Assert.fail("il manque une des répartitions pour la quatrième prestation");
            }
        } finally {
            try {
                transaction.rollback();
            } finally {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testImportMaterniteComplexeAvecDatesMontantVerse() throws Exception {
        Assert.fail("ACOR n'accepte pas des dates de montant verse pour des droits maternité !!!");
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testImportMaterniteSimple() throws Exception {
        BTransaction transaction = (BTransaction) TestACOR2_1_15_IO.SESSION.newTransaction();
        APDroitLAPG droit;

        try {
            transaction.openTransaction();
            droit = creerDroitMaterniteComplexe(transaction);

            // génération
            List bc = new APBasesCalculBuilder(TestACOR2_1_15_IO.SESSION, droit).createBasesCalcul();
            Collection pw = APACORPrestationsParser.parse(droit, bc, TestACOR2_1_15_IO.SESSION, new StringReader(
                    TestACOR2_1_15_IO.SORTIE_ACOR_MAT_SIMPLE));

            new APCalculateurPrestationStandardLamatAcmAlpha().reprendreDepuisACOR(TestACOR2_1_15_IO.SESSION, pw,
                    droit, new FWCurrency(300));

            // test
            APPrestationManager mgr = new APPrestationManager();

            mgr.setSession(TestACOR2_1_15_IO.SESSION);
            mgr.setForIdDroit(droit.getIdDroit());
            mgr.setOrderBy(APPrestation.FIELDNAME_DATEDEBUT);
            mgr.find(transaction);

            Assert.assertEquals("nombre de prestations différent d'attendu", 4, mgr.size()); // 4
            // prestations

            // première prestation
            APPrestation prestation = (APPrestation) mgr.get(0);

            Assert.assertEquals("montant première prestation différent d'attendu", "2480.00",
                    prestation.getMontantBrut());
            Assert.assertEquals("date début première prestation différent d'attendu", "01.07.2005",
                    prestation.getDateDebut());
            Assert.assertEquals("nb jours soldés première prestation différent d'attendu", "31",
                    prestation.getNombreJoursSoldes());

            // deuxième prestation
            prestation = (APPrestation) mgr.get(1);

            Assert.assertEquals("montant deuxième prestation différent d'attendu", "2480.00",
                    prestation.getMontantBrut());
            Assert.assertEquals("date début deuxième prestation différent d'attendu", "01.08.2005",
                    prestation.getDateDebut());
            Assert.assertEquals("nb jours soldés deuxième prestation différent d'attendu", "31",
                    prestation.getNombreJoursSoldes());

            // troisième prestation
            prestation = (APPrestation) mgr.get(2);

            Assert.assertEquals("montant troisième prestation différent d'attendu", "2400.00",
                    prestation.getMontantBrut());
            Assert.assertEquals("date début troisième prestation différent d'attendu", "01.09.2005",
                    prestation.getDateDebut());
            Assert.assertEquals("nb jours soldés troisième prestation différent d'attendu", "30",
                    prestation.getNombreJoursSoldes());

            // quatrième prestation
            prestation = (APPrestation) mgr.get(3);

            Assert.assertEquals("montant quatrième prestation différent d'attendu", "480.00",
                    prestation.getMontantBrut());
            Assert.assertEquals("date début quatrième prestation différent d'attendu", "01.10.2005",
                    prestation.getDateDebut());
            Assert.assertEquals("nb jours soldés quatrième prestation différent d'attendu", "6",
                    prestation.getNombreJoursSoldes());
        } finally {
            try {
                transaction.rollback();
            } finally {
                transaction.closeTransaction();
            }
        }
    }
}
