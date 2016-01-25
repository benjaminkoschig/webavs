/*
 * Créé le 8 févr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package test;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcrituresSomme;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * @author jmc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class Test {
    public static void main(String[] args) {
        try {
            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession("globazf", "globazf");

            BufferedReader br = null;
            FileInputStream fi = null;
            // fichier d'import
            fi = new FileInputStream("Z:/administratif/CCVD/CICP.txt");
            br = new BufferedReader(new InputStreamReader(fi));
            // fichier d'export
            File outputFile = new File("Z:/administratif/CCVD/new.txt");
            FileWriter out = new FileWriter(outputFile);
            int i = 0;
            String nss = br.readLine();
            while (nss != null) {
                i++;
                if (i % 10 == 0) {
                    System.out.println(i);

                }

                nss = NSUtil.returnNNSS((BSession) session, nss);
                nss = NSUtil.unFormatAVS(nss);
                CICompteIndividuelManager mgr = new CICompteIndividuelManager();
                mgr.setSession((BSession) session);
                mgr.setForNumeroAvs(nss);
                mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
                CICompteIndividuel ci = (CICompteIndividuel) mgr.getFirstEntity();
                for (int j = 2004; j < 2009; j++) {
                    CIEcrituresSomme somme = new CIEcrituresSomme();
                    somme.setSession((BSession) session);
                    somme.setForCompteIndividuelId(ci.getCompteIndividuelId());
                    somme.setForGenreCotPers("true");
                    somme.setEtatEcritures("active");
                    if (somme.getSum("KBMMON").compareTo(new BigDecimal("0")) != 0) {
                        out.write(nss + ";" + j);
                    }
                }
                nss = br.readLine();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);

    }

    BufferedReader br = null;

    FileInputStream fi = null;
}
