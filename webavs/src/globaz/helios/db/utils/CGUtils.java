/*
 * Créé le 7 sept. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.helios.db.utils;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.helios.db.classifications.CGClassification;
import globaz.helios.db.classifications.CGDefinitionListe;
import globaz.helios.db.classifications.CGDefinitionListeManager;
import globaz.helios.db.comptes.CGEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.translation.CodeSystem;
import java.util.StringTokenizer;

/**
 * @author sel Créé le 7 sept. 06
 */
public class CGUtils {

    /**
     * Permet de créer une partie informations pour globaz afin de mieux debugger le process en cas de probleme
     * 
     * @param message
     */
    public static void addMailInformationsError(FWMemoryLog log, String _message, String source, String titreMessage) {

        String message = titreMessage + "\n";
        message += _message;

        // Parcourir des informations
        StringTokenizer str = new StringTokenizer(message, "\n\r");
        while (str.hasMoreElements()) {
            log.logMessage(str.nextToken(), FWMessage.INFORMATION, source);
        }
    }

    /**
     * Retourne l'id par défaut pour la définition des listes d'un exrcice comptable.
     * 
     * @param session
     * @param exercice
     * @return
     */
    public static String getDefautIdDefinitionListe(BSession session, CGExerciceComptable exercice) {
        try {
            CGClassification classification = new CGClassification();
            classification.setSession(session);
            classification.setIdClassification(exercice.getMandat().getIdClassificationPrincipale());
            classification.retrieve();

            CGDefinitionListeManager mgr = new CGDefinitionListeManager();
            mgr.setSession(session);
            mgr.setForIdClassification(classification.getIdClassification());
            mgr.find(2);
            if (!mgr.isEmpty()) {
                return ((CGDefinitionListe) mgr.getFirstEntity()).getIdDefinitionListe();
            } else {
                return "";
            }

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Créé le : 8 sept. 06
     * 
     * @param session
     * @param idJournal
     * @param modeCredit
     * @return
     */
    private static String getTotal(BSession session, String idJournal, boolean modeCredit) {
        CGEcritureListViewBean manager = new CGEcritureListViewBean();
        FWCurrency total = new FWCurrency("0.00");

        try {
            manager.setForIdJournal(idJournal);
            manager.setSession(session);

            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                CGEcritureViewBean ecriture = (CGEcritureViewBean) manager.get(i);

                if (modeCredit) {
                    if (CodeSystem.CS_CREDIT.equals(ecriture.getCodeDebitCredit())
                            || CodeSystem.CS_EXTOURNE_CREDIT.equals(ecriture.getCodeDebitCredit())) {
                        total.add(ecriture.getMontantBase());
                    }
                } else {
                    if (CodeSystem.CS_DEBIT.equals(ecriture.getCodeDebitCredit())
                            || CodeSystem.CS_EXTOURNE_DEBIT.equals(ecriture.getCodeDebitCredit())) {
                        total.add(ecriture.getMontantBase());
                    }
                }
            }

            total.abs();

        } catch (Exception e) {
            return "";
        }

        return total.toString();
    }

    /**
     * Créé le : 8 sept. 06
     * 
     * @param session
     * @param idJournal
     * @return le total des crédits
     */
    static public String getTotalCredit(BSession session, String idJournal) {
        return CGUtils.getTotal(session, idJournal, true);
    }

    /**
     * Créé le : 8 sept. 06
     * 
     * @param session
     * @param idJournal
     * @return le total des débits
     */
    static public String getTotalDebit(BSession session, String idJournal) {
        return CGUtils.getTotal(session, idJournal, false);
    }
}
