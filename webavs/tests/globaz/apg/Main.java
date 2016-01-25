package globaz.apg;

import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APCotisationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.babel.api.helper.CTHtmlConverter;
import globaz.external.tucana.APProcessBouclementAlpha;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.itucana.exception.TUModelInstanciationException;
import globaz.itucana.model.ITUModelBouclement;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.rmi.RemoteException;

/**
 * Descpription
 * 
 * @author scr Date de création 28 sept. 05
 */
public class Main {

    public static void main(String[] args) throws TUModelInstanciationException {

        Main m = new Main();

        try {
            m.testNPA();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }

    }

    /**
	 * 
	 */
    public Main() {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    //
    // /* List l = new ArrayList();
    // l.add(PRUtil.PROVENANCE_TIERS);
    // Vector v = PRTiersHelper.getPays((BSession)session);
    //
    // for (Iterator iter = v.iterator(); iter.hasNext();) {
    // String[] element = (String[]) iter.next();
    // System.out.println(element[0] + " " + element[1]);
    //
    // }
    // */
    // }
    // catch (RemoteException e) {
    // e.printStackTrace();
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // finally {
    // System.exit(0);
    // }

    // }

    public void doTraitement() throws TUModelInstanciationException {

        ITUModelBouclement bouclement = null;

        APProcessBouclementAlpha bcl = new APProcessBouclementAlpha("2006", "11");
        BISession session;

        try {

            session = GlobazSystem.getApplication("APG").newSession("scr", "scr");

            // Class c =
            // Class.forName("globaz.apg.process.APGenererDecomptesProcess");
            // APGenererDecomptesProcess process =
            // (APGenererDecomptesProcess)c.newInstance();
            //
            // process.setSession((BSession)session);
            // process.setEMailAddress("scr@globaz.ch");
            // process.setIdLot("1");
            // process.setDateValeurComptable("10.10.2006");
            // process.setIsDefinitif(Boolean.FALSE);
            // BProcessLauncher.start(process);

            //
            // CPTaxationDefinitiveManager mgr = new
            // CPTaxationDefinitiveManager();
            // mgr.setSession((BSession)session);
            // mgr.setForNoPassage("1");
            // mgr.find();
            //
            // for (int i = 0; i < mgr.size(); i++) {
            // CPTaxationDefinitive entity =
            // (CPTaxationDefinitive)mgr.getEntity(i);
            // System.out.println(entity.getNss() + " " + entity.getDateDebut()
            // + " - " + entity.getDateFin() + entity.getRevenuDeterminant() +
            // " - " + entity.getRevenuIndependant() );
            // }
            //

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void majRepartitionPmt(String domaine, String userName, String pwd, String fromIdCotisation,
            String toIdCotisation) throws TUModelInstanciationException {

        BISession session;
        BITransaction transaction = null;
        try {

            session = GlobazSystem.getApplication(domaine).newSession(userName, pwd);
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            APCotisationManager mgr = new APCotisationManager();
            mgr.setSession((BSession) session);
            mgr.setFromIdCotisation(fromIdCotisation);
            mgr.setToIdCotisation(toIdCotisation);
            mgr.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < mgr.size(); i++) {
                APCotisation cotisation = (APCotisation) mgr.getEntity(i);

                System.out.println("Traitement de la cotisation id : " + cotisation.getIdCotisation());
                System.out.println("--- Repartition pmt id = : " + cotisation.getIdRepartitionBeneficiairePaiement());

                FWCurrency montantCotisation = new FWCurrency(cotisation.getMontant());
                // On récupère la répartition de cette cotisation
                APRepartitionPaiements rep = new APRepartitionPaiements();
                rep.setSession((BSession) session);
                rep.setIdRepartitionBeneficiairePaiement(cotisation.getIdRepartitionBeneficiairePaiement());
                rep.retrieve(transaction);
                if (rep == null || rep.isNew()) {
                    System.out.println("La répartion de la cotisation id : " + cotisation.getIdCotisation()
                            + " n'a pas été trouvée");
                } else {
                    FWCurrency montantNetRepartition = new FWCurrency(rep.getMontantNet());
                    System.out.println("*** Ancien montant : " + rep.getMontantNet());
                    System.out.println("*** Montant a ajouter : " + montantCotisation);
                    montantNetRepartition.add(montantCotisation);
                    System.out.println("*** Nouveau montant : " + montantNetRepartition);

                    rep.setMontantNet(montantNetRepartition.toString());

                    rep.wantCallMethodAfter(false);
                    rep.wantCallMethodBefore(false);
                    rep.wantCallValidate(false);

                    rep.update(transaction);
                    if (transaction.hasErrors()) {
                        System.out.println("Traitement annulé, Cause : " + transaction.getErrors().toString());
                        transaction.rollback();
                    } else {
                        transaction.commit();
                        System.out.println("Répartition id : " + rep.getIdRepartitionBeneficiairePaiement()
                                + " mise à jours");
                        System.out.println("Montant ajouté = " + montantNetRepartition);
                    }
                }

                System.out.println("");
                System.out.println("");
            }
        } catch (Exception e) {

        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            System.exit(0);
        }

    }

    public void testBabel() {
        String s = CTHtmlConverter.htmlToIText("<p><font>salut les petiots<br /></font></p>", "120");
        System.out.println(s);

    }

    public void testBouclementAlpha() throws Exception {

        BISession session = null;

        session = GlobazSystem.getApplication("APG").newSession("h517glo", "glob4az");

        APProcessBouclementAlpha bcl = new APProcessBouclementAlpha("2007", "02");
        bcl.setSession((BSession) session);
        // Pour tester, commenter toutes les méthodes utilisant le parametere de
        // la methode initBouclement.
        // Décommenter les System.out.println() et controlez le résultat.
        bcl.initBouclement(null);

    }

    public void testNPA() throws Exception {

        BISession session = null;

        session = GlobazSystem.getApplication("APG").newSession("h514glo", "glob4az");

        String canton = PRTiersHelper.getCanton(session, "254000");
        String csCanton = PRACORConst.csCantonToAcor(canton);
    }
}
