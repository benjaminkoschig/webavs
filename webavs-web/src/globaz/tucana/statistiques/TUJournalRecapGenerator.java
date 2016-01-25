package globaz.tucana.statistiques;

import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.db.journal.access.TUDetailGroupeCategorieRubriqueManager;
import globaz.tucana.exception.fw.TUFWFindException;
import globaz.tucana.exception.fw.TUFWRetrieveException;
import globaz.tucana.exception.process.TUProcessJournalGeneratorException;

/**
 * Generation de la liste F001_1
 * 
 * @author fgo date de création : 13 juil. 06
 * @version : version 1.0
 * 
 */
public class TUJournalRecapGenerator {

    /**
     * Recherche du bouclement
     * 
     * @param transaction
     * @param annee
     * @param mois
     * @return
     */
    private static TUBouclement findBouclement(BTransaction transaction, String annee, String mois)
            throws TUFWRetrieveException {
        TUBouclement bouclement = null;
        try {
            bouclement = new TUBouclement();
            bouclement.setAlternateKey(1);
            bouclement.setSession(transaction.getSession());
            bouclement.setAnneeComptable(annee);
            bouclement.setMoisComptable(mois);
            bouclement.retrieve();
        } catch (Exception e) {
            throw new TUFWRetrieveException("TUJournalRecapGenerator.findBouclement()", e);
        }
        return bouclement;
    }

    public static void generate(BTransaction transaction, String annee, String mois, String agence) throws Exception {
        // Recherche du détail de bouclement pour le mois et l'année en
        // paramètre
        TUBouclement bouclement = null;
        TUSupportJournalRecap journal = null;
        try {
            bouclement = findBouclement(transaction, annee, mois);
            if (!bouclement.hasErrors() && !bouclement.isNew()) {

                journal = new TUSupportJournalRecap(annee, mois, agence);

            } else {
                throw new TUProcessJournalGeneratorException("Erreur lors de la recherche du bouclement", bouclement
                        .getErrors().toString());
            }

        } catch (TUFWRetrieveException e) {
            throw new Exception(e.toString());
        }
        // recherche les rubriques
        if (journal != null) {
            TUDetailGroupeCategorieRubriqueManager detailMgr = new TUDetailGroupeCategorieRubriqueManager();
            detailMgr.setSession(transaction.getSession());
            detailMgr.setForIdBouclement(bouclement.getIdBouclement());
            // detailMgr.setForCsType(ITUCSConstantes.CS_TY_CATEGORIE_RUBRIQUE_F001_1);
            try {
                detailMgr.find(BManager.SIZE_NOLIMIT);
            } catch (Exception e) {
                throw new TUFWFindException(
                        "TUJournalRecapGenerator.generate() : Problème lors de la lecture des détails",
                        detailMgr.getCurrentSqlQuery(), e);
            }
            if (detailMgr.size() > 0) {
                journal.addLines(transaction, detailMgr);
            }

        }
    }

}
