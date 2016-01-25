package globaz.hermes.test;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.parametrage.HEChampannonceListViewBean;
import globaz.hermes.db.parametrage.HEChampannonceViewBean;
import globaz.hermes.db.parametrage.HEChampobligatoireViewBean;
import globaz.hermes.db.parametrage.HECriteremotifViewBean;
import globaz.hermes.db.parametrage.HEMotifcodeapplication;
import globaz.hermes.db.parametrage.HEMotifsListViewBean;
import globaz.hermes.db.parametrage.HEMotifsViewBean;
import java.util.StringTokenizer;

/**
 * Insérez la description du type ici. Date de création : (10.02.2003 11:19:17)
 * 
 * @author: Administrator
 */
public class HESaisie {
    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 11:22:44)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        HECriteremotifViewBean criteres = new HECriteremotifViewBean();
        // ////////////////////////////////
        // ////////////////////////////////
        String escapeString = "0";
        // ////////////////////////////////
        String inputCritere = "";
        String inputMotif = "";
        String inputEnregistrement = "";
        String inputChamp = "";
        // ///////////////////////////////
        BTransaction transaction = null;
        try {
            BSession session;
            session = new BSession("HERMES");
            session.connect("userfr", "userfr");
            transaction = new BTransaction(session);
            transaction.openTransaction();
            criteres.setSession(session);
            // ///////////////////////////////////
            HESaisie saisie = new HESaisie();
            HEMotifsListViewBean motifs = new HEMotifsListViewBean();
            motifs.setSession(session);
            HEMotifcodeapplication motifCA = new HEMotifcodeapplication();
            motifCA.setSession(session);
            HEChampannonceListViewBean champs = new HEChampannonceListViewBean();
            champs.setSession(session);
            do {
                System.out.println("Entrer le critere");
                inputCritere = saisie.readLine();
                if (inputCritere.equalsIgnoreCase(escapeString)) {
                    break;
                }
                criteres.setIdcriteremotif(inputCritere);
                criteres.retrieve(transaction);
                if (criteres.isNew()) { // pas trouvé
                    System.out.println("Critere " + inputCritere + " introuvable");
                } else {
                    System.out.println(((HEApplication) session.getApplication()).getCsCritereListe(session)
                            .getCodeSysteme(criteres.getCri_idcriteremotif()).getCurrentCodeUtilisateur().getLibelle());
                    // motif
                    do {
                        // do {
                        System.out.println("Entrer le motif");
                        inputMotif = saisie.readLine();
                        if (inputMotif.equalsIgnoreCase(escapeString)) {
                            break;
                        }
                        motifs.setForCodeUtilisateur(inputMotif);
                        motifs.find(transaction);
                        HEMotifsViewBean motif = (HEMotifsViewBean) motifs.getEntity(0);
                        System.out.println("Motif " + motif.getId());
                        motifCA.setIdCritereMotif(inputCritere);
                        motifCA.setIdMotif(motif.getId());
                        motifCA.setIdCodeApplication("111001");
                        try {
                            System.out.print("Insertion ");
                            motifCA.add(transaction);
                            System.out.println("réussie");
                        } catch (Exception e) {
                            System.out.println("ratée");
                        }
                        // l'enregistrement
                        do {
                            System.out.println("Entrer l'enregistrement");
                            inputEnregistrement = saisie.readLine();
                            if (inputEnregistrement.equalsIgnoreCase(escapeString)) {
                                break;
                            }
                            champs.setForIdParametrageAnnonce(inputEnregistrement);
                            champs.find(transaction);
                            HEChampobligatoireViewBean champOb = new HEChampobligatoireViewBean();
                            champOb.setSession(session);
                            champOb.setIdCritereMotif(motifCA.getIdMotifCodeApplication());
                            do {
                                System.out.println("Entrer le champ");
                                inputChamp = saisie.readLine();
                                if (inputChamp.equalsIgnoreCase(escapeString)) {
                                    break;
                                }
                                HEChampannonceViewBean champ = (HEChampannonceViewBean) champs.getEntity((Integer
                                        .parseInt(inputChamp) - 1));
                                champOb.setIdChampAnnonce(champ.getIdChampAnnonce());
                                try {
                                    System.out.print("Insertion ");
                                    champOb.add(transaction);
                                    System.out.println("réussie");
                                } catch (Exception e) {
                                    System.out.println("ratée");
                                }
                            } while (true);
                        } while (true);
                    } while (true);
                }
            } while (true);
            try {
                if (transaction != null && transaction.isOpened() && transaction.hasErrors()) {
                    transaction.rollback();
                    transaction.closeTransaction();
                } else {
                    transaction.commit();
                    transaction.closeTransaction();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception ioe) {
            System.out.println(ioe);
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Bye");
        System.exit(-1);
    }

    String input;

    StringTokenizer st;

    /**
     * Commentaire relatif au constructeur HESaisie.
     */
    public HESaisie() {
        super();
        String motif = "85";
        input = new String("44," + motif + ",1,7,12,0,4,3,6,8,10,11,12,13,14,15,16,17,18,0,0,0," + "45," + motif
                + ",1,7,12,0,4,3,4,5,6,8,10,11,12,13,14,15,16,17,18,0,0,0," + "68," + motif
                + ",1,7,8,10,11,12,0,2,3,5,6,0,4,3,6,8,10,11,12,13,14,15,16,17,18,0,0,0," + "69," + motif
                + ",1,7,8,10,11,12,0,2,3,5,6,0,4,3,4,5,6,8,10,11,12,13,14,15,16,17,18,0,0,0,0");
        st = new StringTokenizer(input, ",");
    }

    public String readLine() throws java.io.IOException {
        /*
         * String retour = ""; int c; while ((c = System.in.read()) != 13) { retour += (char) c; }
         * System.out.println(":" + retour); return retour.trim();
         */
        String retour = st.nextToken();
        System.out.println(":" + retour);
        return retour;

    }
}
