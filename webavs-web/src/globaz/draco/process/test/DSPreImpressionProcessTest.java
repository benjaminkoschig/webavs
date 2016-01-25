package globaz.draco.process.test;

import globaz.draco.process.DSPreImpressionProcess;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.api.BISession;
import globaz.globall.db.GlobazServer;

/**
 * Cette classe permet de tester le lancement du process de préimpression de la déclaration de salaires
 * 
 * @author Sébastien Chappatte
 */
public class DSPreImpressionProcessTest {
    /**
     * Méthode principale de la classe permet le lancement de cette classe
     */
    public static void main(String[] args) {
        try {
            // Initialiser
            String sEMailAddress = "sch@globaz.ch";
            boolean bAffilieTous = true;
            String sAffilieDebut = "";
            String sAffilieFin = "";
            String sDateSurDocument = "15.01.2001";
            String sDateRetourEff = "31.01.2002";
            String sAnnee = "2001";
            boolean bImprimerLettre = false;
            boolean bImprimerDeclaration = false;
            // Récupérer les paramètres
            if (args.length > 0) {
                sEMailAddress = args[0];
            }
            if (args.length > 1) {
                bAffilieTous = Boolean.valueOf(args[1]).booleanValue();
            }
            if (args.length > 2) {
                sAffilieDebut = args[2];
            }
            if (args.length > 3) {
                sAffilieFin = args[3];
            }
            if (args.length > 4) {
                sDateSurDocument = args[4];
            }
            if (args.length > 5) {
                sDateRetourEff = args[5];
            }
            if (args.length > 6) {
                sAnnee = args[6];
            }
            if (args.length > 7) {
                bImprimerLettre = Boolean.valueOf(args[7]).booleanValue();
            }
            if (args.length > 8) {
                bImprimerDeclaration = Boolean.valueOf(args[8]).booleanValue();
            }
            // Instancier le process
            DSPreImpressionProcess proc = new DSPreImpressionProcess();
            // proc.setSession(new
            // globaz.globall.db.BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
            BISession session = GlobazServer.getCurrentSystem().getApplication("DRACO").newSession();
            FWDispatcher dispatcher = new FWDispatcher(session, session.getApplicationId(), "DS");
            dispatcher.connect("uti", "motpass");
            proc.setISession(session);
            proc.setEMailAddress(sEMailAddress);
            proc.setControleTransaction(true);
            proc.setAffilieTous(bAffilieTous);
            proc.setAffilieDebut(sAffilieDebut);
            proc.setAffilieFin(sAffilieFin);
            proc.setDateSurDocument(sDateSurDocument);
            proc.setDateRetourEff(sDateRetourEff);
            proc.setAnnee(sAnnee);
            proc.setImprimerLettre(bImprimerLettre);
            proc.setImprimerDeclaration(bImprimerDeclaration);
            // Exécution
            proc.executeProcess();
            try {
                // proc.join();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Fin
        System.exit(0);
    }

    /**
     * Constructor for DSPreImpressionProcessTest
     */
    public DSPreImpressionProcessTest() {
        super();
    }
}