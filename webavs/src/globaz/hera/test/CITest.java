/*
 * Créé le 26 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.test;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.pavo.api.ICICompteIndividuel;

/**
 * @author ado
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CITest {

    public static void main(String[] args) {
        try {
            new CITest().doAVS();
            System.out.println("FIN !");
        } catch (Exception e) {

            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
	 * 
	 */
    private void addTiers() throws FWSecurityLoginException, Exception {
        BSession session = new BSession("HERA");
        session.connect("globazf", "ssiiadm");
        SFTiersHelper.addTiers(session, "100.00.206.372", "Dostes", "Arnaud", "5990001", "12.11.1977", "", "212",
                "505025", "", "");
    }

    public void doAVS() {
        for (int i = 100; i < 999; i++) {
            for (int j = 0; j < 99; j++) {
                for (int k = 0; k < 999999; k++) {
                    String ai = i + "";
                    while (ai.length() < 3) {
                        ai = "0" + ai;
                    }
                    String aj = "" + j;
                    while (aj.length() < 2) {
                        aj = "0" + aj;
                    }
                    String ak = "" + k;
                    while (ak.length() < 6) {
                        ak = "0" + ak;
                    }
                    String avs = ai + aj + ak;
                    try {
                        JAUtil.checkAvs(avs);
                        System.out.println(JAUtil.formatAvs(avs));
                    } catch (JAException e) {

                    }
                }
            }
        }

    }

    /**
	 * 
	 */
    private void findCI() throws Exception {
        JAUtil.checkAvs("00000000000");
        BSession localSession = new BSession("HERA");
        localSession.connect("globazf", "ssiiadm");

        ICICompteIndividuel remoteCI = (ICICompteIndividuel) localSession.getAPIFor(ICICompteIndividuel.class);
        BISession pavoSession = (BISession) localSession.getAttribute("PAVOSESSION");

        pavoSession = GlobazSystem.getApplication("PAVO").newSession(localSession);
        localSession.setAttribute("PAVOSESSION", pavoSession);

        if (!pavoSession.isConnected()) {
            localSession.connectSession(pavoSession);
        }
        // vide le buffer d'erreur
        pavoSession.getErrors();
        BTransaction pavoTransaction = (BTransaction) ((BSession) pavoSession).newTransaction();
        pavoTransaction.openTransaction();
        remoteCI.setISession(pavoSession);
        remoteCI.load("11111111111", pavoTransaction);
        System.out.println(remoteCI.isNew());
    }
}
