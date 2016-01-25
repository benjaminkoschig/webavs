package globaz.test.helios.solde;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Ignore;
import org.junit.Test;

public class CGCheckSoldeAndMouvements {

    private static final String ZERO = "0.00";

    private static final String KEY_PROPERTY_USER = "user";
    private static final String KEY_PROPERTY_PASSWORD = "password";

    private static final String DEFAULT_USER = "ssii";
    private static final String DEFAULT_PASSWORD = "ssiiadm";

    private String user = DEFAULT_USER;
    private String password = DEFAULT_PASSWORD;

    /**
     * Si les propriétés user et password sont donnés à la JVM les utilier pour
     * creer la session dans les tests.
     */

    protected void setUp() throws Exception {
        if (System.getProperty(KEY_PROPERTY_USER) != null) {
            user = System.getProperty(KEY_PROPERTY_USER);
        }

        if (System.getProperty(KEY_PROPERTY_PASSWORD) != null) {
            password = System.getProperty(KEY_PROPERTY_PASSWORD);
        }
    }

    /**
     * Dans la talbe des soldes, doit + avoir = solde et doitprovisoire +
     * avoirprovisoire = soldeprovisoire <br/>
     * select * from DB2COTP.CGSOLDP where coalesce(doitprovisoire,0) +
     * avoirprovisoire <> soldeprovisoire
     * 
     * @throws Exception
     */
    @Test
    @Ignore
    public void testTableSolde() throws Exception {
        BTransaction transaction = null;

        try {
            BSession session = (BSession) GlobazSystem.getApplication(CGApplication.DEFAULT_APPLICATION_HELIOS)
                    .newSession(user, password);

            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            CGSoldeManager manager = new CGSoldeManager();
            manager.setSession(session);
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                CGSolde solde = (CGSolde) manager.get(i);

                FWCurrency doitProvisoire = new FWCurrency(solde.getDoitProvisoire());
                FWCurrency avoirProvisoire = new FWCurrency(solde.getAvoirProvisoire());
                FWCurrency soldeProvisoire = new FWCurrency(solde.getSoldeProvisoire());

                FWCurrency doit = new FWCurrency(solde.getDoit());
                FWCurrency avoir = new FWCurrency(solde.getAvoir());
                FWCurrency soldeTotal = new FWCurrency(solde.getSolde());

                doitProvisoire.add(avoirProvisoire);
                doit.add(avoir);

                if (doitProvisoire.compareTo(soldeProvisoire) != 0) {
                    throw new Exception("Solde provisoire erreur [idCompte " + solde.getIdCompte() + " - idPeriode "
                            + solde.getIdPeriodeComptable() + "]");
                }

                if (doit.compareTo(soldeTotal) != 0) {
                    throw new Exception("Solde erreur [idCompte " + solde.getIdCompte() + " - idPeriode "
                            + solde.getIdPeriodeComptable() + "]");
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.closeTransaction();
            }

            throw e;
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }

        }
    }

    /**
     * Tests les soldes et mouvements de la base de donnée.
     * 
     * @throws Exception
     */
    @Test
    @Ignore
    public void testSoldeAndMouvements() throws Exception {
        BTransaction transaction = null;

        try {
            BSession session = (BSession) GlobazSystem.getApplication(CGApplication.DEFAULT_APPLICATION_HELIOS)
                    .newSession(user, password);

            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            CGExerciceComptableManager exManager = new CGExerciceComptableManager();
            exManager.setSession(session);

            exManager.find(transaction);

            for (int j = 0; j < exManager.size(); j++) {
                CGExerciceComptable exercice = (CGExerciceComptable) exManager.get(j);

                if (!exercice.getMandat().isMandatConsolidation()) {
                    CGPlanComptableManager manager = new CGPlanComptableManager();
                    manager.setSession(session);

                    manager.setForIdExerciceComptable(exercice.getIdExerciceComptable());

                    manager.find(transaction, BManager.SIZE_NOLIMIT);

                    for (int i = 0; i < manager.size(); i++) {
                        CGPlanComptableViewBean compte = (CGPlanComptableViewBean) manager.get(i);

                        executeAndCheck(transaction, exercice.getIdExerciceComptable(), compte.getIdCompte(),
                                compte.getIdExterne());
                    }

                    executeAndCheck(transaction, exercice.getIdExerciceComptable(), null, null);
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.closeTransaction();
            }

            throw e;
        }
    }

    /**
     * Execute les sql et compare les soldes aux mouvements en provisoire et
     * définitif.
     * 
     * @param transaction
     * @param idExerciceComptable
     * @param idCompte
     * @param idExterneCompte
     * @throws Exception
     * @throws SQLException
     */

    public static void executeAndCheck(BTransaction transaction, String idExerciceComptable, String idCompte,
            String idExterneCompte) throws Exception, SQLException {
        BStatement s = new BStatement(transaction);
        s.createStatement();

        String query = new String();

        query = "select sum(soldeprovisoire), sum(solde) from " + new CGSoldeManager().getCollection()
                + "cgsoldp where idexercomptable = " + idExerciceComptable + " and idperiodecomptable <> 0";
        if (idCompte != null) {
            query += " and idcompte = " + idCompte;
        }

        ResultSet test = s.executeQuery(query);
        test.next();

        String cgSoldpSumProvisoire = test.getString(1);
        String cgSoldpSumSolde = test.getString(2);

        query = "select sum(montant) as montant from " + new CGSoldeManager().getCollection()
                + "cgecrip where idexercomptable = " + idExerciceComptable
                + " and estactive = '1' and estprovisoire = '2'";
        if (idCompte != null) {
            query += " and idcompte = " + idCompte;
        }

        test = s.executeQuery(query);
        test.next();

        String cgEcripSumMvtComptabilise = test.getString(1);

        query = "select sum(montant) as montant from " + new CGSoldeManager().getCollection()
                + "cgecrip where idexercomptable = " + idExerciceComptable + " and estactive = '1'";
        if (idCompte != null) {
            query += " and idcompte = " + idCompte;
        }

        test = s.executeQuery(query);
        test.next();

        String cgEcripSumMvtProvisoire = test.getString(1);

        query = "select sum(soldeprovisoire), sum(solde) from " + new CGSoldeManager().getCollection()
                + "cgsoldp where idexercomptable = " + idExerciceComptable + " and idperiodecomptable = 0";
        if (idCompte != null) {
            query += " and idcompte = " + idCompte;
        }

        test = s.executeQuery(query);
        test.next();

        String cgSoldpSumSoldeProvisoirePeriode0 = test.getString(1);
        String cgSoldpSumSoldePeriode0 = test.getString(2);

        if (cgSoldpSumProvisoire == null) {
            cgSoldpSumProvisoire = ZERO;
        } else {
            cgSoldpSumProvisoire = cgSoldpSumProvisoire.replace(',', '.');
        }

        if (cgSoldpSumSolde == null) {
            cgSoldpSumSolde = ZERO;
        } else {
            cgSoldpSumSolde = cgSoldpSumSolde.replace(',', '.');
        }

        if (cgEcripSumMvtProvisoire == null) {
            cgEcripSumMvtProvisoire = ZERO;
        } else {
            cgEcripSumMvtProvisoire = cgEcripSumMvtProvisoire.replace(',', '.');
        }

        if (cgEcripSumMvtComptabilise == null) {
            cgEcripSumMvtComptabilise = ZERO;
        } else {
            cgEcripSumMvtComptabilise = cgEcripSumMvtComptabilise.replace(',', '.');
        }

        if (cgSoldpSumSoldePeriode0 == null) {
            cgSoldpSumSoldePeriode0 = ZERO;
        } else {
            cgSoldpSumSoldePeriode0 = cgSoldpSumSoldePeriode0.replace(',', '.');
        }

        if (cgSoldpSumSoldeProvisoirePeriode0 == null) {
            cgSoldpSumSoldeProvisoirePeriode0 = ZERO;
        } else {
            cgSoldpSumSoldeProvisoirePeriode0 = cgSoldpSumSoldeProvisoirePeriode0.replace(',', '.');
        }

        if (!cgSoldpSumProvisoire.trim().equals(cgEcripSumMvtProvisoire.trim())) {
            throw new Exception("[compte=" + idExterneCompte + ", idEx=" + idExerciceComptable + "] Mvt provisoire "
                    + cgEcripSumMvtProvisoire + " <> Solde provisoire période (!= 0) " + cgSoldpSumProvisoire + "");
        }

        if (cgSoldpSumSolde != null && !cgSoldpSumSolde.trim().equals(cgEcripSumMvtComptabilise.trim())) {
            throw new Exception("[compte=" + idExterneCompte + ", idEx=" + idExerciceComptable + "] Mvt "
                    + cgEcripSumMvtComptabilise + " <> Solde période (!= 0) " + cgSoldpSumSolde + "");
        }

        if (!cgSoldpSumSoldeProvisoirePeriode0.trim().equals(cgEcripSumMvtProvisoire.trim())) {
            throw new Exception("[compte=" + idExterneCompte + ", idEx=" + idExerciceComptable + "] Mvt provisoire "
                    + cgEcripSumMvtProvisoire + " <> Solde provisoire période (0) " + cgSoldpSumSoldeProvisoirePeriode0
                    + "");
        }

        if (!cgSoldpSumSoldePeriode0.trim().equals(cgEcripSumMvtComptabilise.trim())) {
            throw new Exception("[compte=" + idExterneCompte + ", idEx=" + idExerciceComptable + "] Mvt "
                    + cgEcripSumMvtComptabilise + " <> Solde période (0) " + cgSoldpSumSoldePeriode0 + "");
        }

        s.closeStatement();
    }
}
