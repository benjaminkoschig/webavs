package globaz.corvus.test;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REPaiementRentes;
import globaz.corvus.db.rentesaccordees.REPaiementRentesManager;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ExportRentesGrandPmt {

    /**
     * @param args
     */
    public static void main(String[] args) {

        BISession session;
        BITransaction transaction = null;

        BStatement statement = null;
        PrintWriter file = null;
        try {
            // session =
            // GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).newSession("screlier",
            // "8cr3113r");
            session = GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).newSession("ciciglo",
                    "glob4az");
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            file = new PrintWriter(new FileWriter("listesRentesFPV.csv"));

            REPaiementRentesManager mgr = new REPaiementRentesManager();
            mgr.setSession((BSession) session);
            mgr.setForDatePaiement("12.2010");
            mgr.setForIsEnErreur(Boolean.FALSE);
            REPaiementRentes rente = null;
            statement = mgr.cursorOpen((BTransaction) transaction);

            file.println("NSS; Nom; Genre rente; montant; Blocage; Retenue");
            file.flush();
            String SEPARATOR = ";";
            FWCurrency montantTot = new FWCurrency();
            int nombreRente = 0;
            FWCurrency montantTotBlocage = new FWCurrency();
            while ((rente = (REPaiementRentes) mgr.cursorReadNext(statement)) != null) {

                nombreRente++;
                montantTot.add(rente.getMontant());
                if (rente.getIsPrestationBloquee() != null && rente.getIsPrestationBloquee().booleanValue()) {
                    montantTotBlocage.add(rente.getMontant());
                }

                if (nombreRente % 50 == 0) {
                    System.out.println("");
                } else {
                    System.out.print(".");
                }

                StringBuffer sb = new StringBuffer();
                sb.append(rente.getNssTBE()).append(SEPARATOR);
                sb.append(rente.getNomTBE()).append(SEPARATOR);
                sb.append(rente.getCodePrestation()).append(SEPARATOR);
                sb.append(rente.getMontant()).append(SEPARATOR);
                sb.append(rente.getIsPrestationBloquee()).append(SEPARATOR);
                sb.append(rente.getIsRetenue());

                file.println(sb.toString());
                file.flush();
            }

            System.out.println("Nombre de rentes = " + nombreRente);
            System.out.println("montant total (inclut blocage + retenue) = " + montantTot.toStringFormat());
            System.out.println("montant total des blocages = " + montantTotBlocage.toStringFormat());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            statement.closeStatement();
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (Exception e2) {
                ;
            }
            System.exit(0);
        }

    }

}
