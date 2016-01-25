package globaz.hermes.test;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.utils.DateUtils;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

/**
 * @author ald
 * @version 1.0
 * @param filename
 *            delim uid pwd
 */
public class HEFileToDB {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println(DateUtils.getTimeStamp()
                    + "java globaz.hermes.test.HEFileToDB <filename> <delim> <uid> <pwd>");
            System.exit(-1);
        }
        BTransaction transaction = null;
        try {
            // création de la session
            BSession session = new BSession("HERMES");
            session.connect(args[2], args[3]);
            //
            transaction = (BTransaction) session.newTransaction();
            // comptabiliser les arcs ajoutées
            int compteurOK = 0;
            // comptabiliser les erreurs
            int compteurErreur = 0;
            // ouvrir le fichier
            RandomAccessFile file = new RandomAccessFile(args[0], "r");
            // String crtLine = file.readLine();
            String crtLine = "";
            // while (crtLine != null) {
            String numAVS = "";
            String motif = "";
            while ((crtLine = file.readLine()) != null && !transaction.hasErrors()) {
                StringTokenizer strToken = new StringTokenizer(crtLine, args[1]);
                if (strToken.countTokens() < 2) {
                    System.out.println("La ligne " + crtLine + " n'a été traitée");
                    System.out.println("La ligne doit être au format n° avs<delim>motif");
                    compteurErreur++;
                } else {
                    numAVS = strToken.nextToken();
                    motif = strToken.nextToken();
                    System.out.println("num avs : " + JAUtil.formatAvs(numAVS) + " motif : " + motif);
                    // crée une nouvelle ARC et la pousser dans la base
                    HEInputAnnonceViewBean newARC = new HEInputAnnonceViewBean(session);

                    newARC.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
                    newARC.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                    newARC.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, motif);
                    newARC.put(IHEAnnoncesViewBean.NUMERO_ASSURE, numAVS);
                    newARC.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE, "AUTO/" + JAUtil.formatAvs(numAVS));

                    newARC.add(transaction);
                    compteurOK++;
                }
            }
            System.out.println("Traitement terminé");
            System.out.println(compteurOK + " ligne(s) traitée(s)");
            System.out.println(compteurErreur + " ligne(s) en erreur(s)");
            file.close();
            //
        } catch (Exception err) {
            System.out.println("Erreur de traitement : " + err.getMessage());
        } finally {
            try {
                if (!transaction.hasErrors()) {
                    System.out.println("Traitement OK");
                    transaction.commit();
                } else {
                    System.out.println("Traitement en erreur");
                    transaction.rollback();
                }
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
