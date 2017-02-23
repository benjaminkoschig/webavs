/*
 * Créé le Feb 17, 2005
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.ordres.format;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationOrdreManager;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.exception.AucuneAdressePaiementException;
import globaz.osiris.process.CAProcessOrdre;
import java.io.PrintWriter;

/**
 * @author dda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class CAOrdreFormateur {
    protected final static String CRLF = "\n";

    protected static final String GENRE_TRANSACTION = "genreTransaction";
    protected static final String MONTANT = "montant";

    protected StringBuffer base = null;

    private boolean echoToConsole = false;
    private boolean insertNewLine = false;
    private FWMemoryLog memoryLog = null;
    protected APIOrdreGroupe ordreGroupe = null;

    private PrintWriter printWriter = null;
    private BSession session;

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 11:33:24)
     */
    public void executeOrdreVersement(CAOrdreGroupe og, CAProcessOrdre context) throws Exception {
        // Initialiser
        long nbTransaction = 0;
        FWCurrency fTotalCalcule = new FWCurrency();

        // Sous contrôle d'exceptions
        try {

            // Instancier un manager
            CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
            mgr.setSession(context.getSession());
            mgr.setForIdOrdreGroupe(og.getIdOrdreGroupe());
            mgr.setOrderBy(CAOperationOrdreManager.ORDER_IDORDREGROUPE_NOMCACHE);

            // Récupérer les ordres
            mgr.find(context.getTransaction(), BManager.SIZE_NOLIMIT);

            // Calculer le nombre à exécuter
            context.setState(og.getSession().getLabel("6113"));
            context.setProgressScaleValue(mgr.size());

            // Boucler sur les entités
            for (int i = 0; i < mgr.size(); i++) {

                // Vérifier le contexte d'exécution
                if (context.isAborted()) {
                    return;
                }

                // Récupérer l'ordre de versement
                CAOperationOrdreVersement ov = (CAOperationOrdreVersement) mgr.getEntity(i);
                ov.setSession(og.getSession());

                // Numéroter la transaction
                nbTransaction++;
                ov.setNumTransaction(String.valueOf(nbTransaction));

                // Valider l'ordre avant le paiement
                ov._validerAvantVersement(context.getTransaction());

                // S'il y a des erreurs, on le signale
                if (ov.getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {

                    og.getMemoryLog().logMessage("5204", ov.getNumTransaction(), FWMessage.ERREUR,
                            og.getClass().getName());
                    og.getMemoryLog().logMessage(ov.getMemoryLog());
                } else {

                    // Déclencher le versement
                    try {
                        if (context.getComptabiliserOrdre()) {
                            if (context.getGenererFichierEchange()) {
                                ov.verser(this, og.getJournal(), context.getTransaction());
                            } else {
                                ov.verser(null, og.getJournal(), context.getTransaction());
                            }
                        } else if (context.getGenererFichierEchange()) {
                            ov.verser(this, null, context.getTransaction());
                        }
                    } catch (AucuneAdressePaiementException e) {
                        og.getMemoryLog().logStringBuffer(new StringBuffer(e.getMessage()), ov.getClass().getName());
                    }

                    // Vérifier les erreurs
                    if (context.getTransaction().hasErrors()) {

                        og.erreur(context.getTransaction(),
                                og.getSession().getLabel("5205") + " " + ov.getIdOperation());
                        return;
                    }
                }

                // Mise à jour de l'ordre de versement
                ov.update(context.getTransaction());
                if (context.getTransaction().hasErrors()) {
                    og.erreur(context.getTransaction(), og.getSession().getLabel("5205") + " " + ov.getIdOperation());
                    return;
                }

                // Totaliser
                fTotalCalcule.add(ov.getMontant());

                // Progression
                context.incProgressCounter();
            }

            // Erreur s'il n'y a pas de transaction
            if (nbTransaction == 0) {
                og.getMemoryLog().logMessage("5203", null, FWMessage.ERREUR, og.getClass().getName());
            } else {
                // Vérifier le nombre de transactions et le montant total
                int iNombreTransactions = 0;
                FWCurrency fTotal = new FWCurrency(og.getTotal());
                try {
                    iNombreTransactions = Integer.parseInt(og.getNombreTransactions());
                } catch (Exception e) {
                    // Do nothing
                }

                if (iNombreTransactions != nbTransaction) {
                    og.getMemoryLog().logMessage("5215", null, FWMessage.ERREUR, og.getClass().getName());
                }

                if (!fTotal.equals(fTotalCalcule)) {
                    og.getMemoryLog().logMessage("5216", null, FWMessage.ERREUR, og.getClass().getName());
                }
            }

        } catch (Exception e) {
            og.erreur(context.getTransaction(), e.getMessage());
        }

    }

    public abstract StringBuffer format(APICommonOdreVersement ov) throws Exception;

    public abstract StringBuffer format(CAOperationOrdreRecouvrement or) throws Exception;

    public abstract StringBuffer formatEOF(APIOrdreGroupe og) throws Exception;

    public abstract StringBuffer formatHeader(APIOrdreGroupe og) throws Exception;

    /**
     * Commentaire relatif à la méthode getEchoToConsole.
     */
    public boolean getEchoToConsole() {
        return echoToConsole;
    }

    /**
     * Commentaire relatif à la méthode getInsertNewLine.
     */
    public boolean getInsertNewLine() {
        return insertNewLine;
    }

    /**
     * Commentaire relatif à la méthode getMemoryLog.
     */
    public FWMemoryLog getMemoryLog() {
        if (memoryLog == null) {
            memoryLog = new FWMemoryLog();
            memoryLog.setSession(getSession());
        }
        return memoryLog;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.04.2002 11:16:00)
     * 
     * @return java.io.PrintWriter
     */
    public java.io.PrintWriter getPrintWriter() {
        return printWriter;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 10:41:30)
     * 
     * @return globaz.globall.db.BSession
     */
    public globaz.globall.db.BSession getSession() {
        return session;
    }

    /**
     * Commentaire relatif à la méthode setEchoToConsole.
     */
    public void setEchoToConsole(boolean isEchoToConsole) {
        echoToConsole = isEchoToConsole;
    }

    /**
     * Commentaire relatif à la méthode setInsertNewLine.
     */
    public void setInsertNewLine(boolean newLine) {
        insertNewLine = newLine;
    }

    /**
     * Commentaire relatif à la méthode setMemoryLog.
     */
    public void setMemoryLog(FWMemoryLog newLog) {
        memoryLog = newLog;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.04.2002 11:16:00)
     * 
     * @param newPrintWriter
     *            java.io.PrintWriter
     */
    public void setPrintWriter(java.io.PrintWriter newPrintWriter) {
        printWriter = newPrintWriter;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 10:41:30)
     * 
     * @param newSession
     *            globaz.globall.db.BSession
     */
    public void setSession(globaz.globall.db.BSession newSession) {
        session = newSession;
    }

}
