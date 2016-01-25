package globaz.tucana.statistiques;

import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.db.bouclement.access.TUBouclementManager;
import globaz.tucana.db.journal.access.TUDetailGroupeCategorieRubriqueManager;
import globaz.tucana.exception.fw.TUFWException;
import globaz.tucana.exception.fw.TUFWFindException;
import globaz.tucana.exception.process.TUProcessJournalException;
import globaz.tucana.exception.process.TUProcessJournalGeneratorException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe permettant de générer le journal
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 */
public class TUJournalGenerator {
    private static final String GRP_BGRCID = "grp.bgrcid";

    /**
     * Chargement du journal
     * 
     * @param transaction
     * @param csAgence
     * @param mois
     * @param annee
     * @param bouclement
     * @return
     */
    private static TUJournal createJournal(BTransaction transaction, String annee, String mois, String csAgence,
            TUBouclement bouclement) {
        // return new TUJournal(transaction, bouclement.getCsAgence(),
        // chargeLibelleAgence(transaction, bouclement),
        // bouclement.getAnneeComptable(), bouclement.getMoisComptable(),
        // JACalendarGregorian.getMonthName(Integer.parseInt(bouclement.getMoisComptable())));
        String vCsAgence = JadeStringUtil.isBlankOrZero(csAgence) ? bouclement.getCsAgence() : csAgence;
        String vLibellemois = JadeStringUtil.isBlankOrZero(mois) ? transaction.getSession().getLabel("LIST_MOIS_TOUS")
                : JACalendar.getMonthName(Integer.parseInt(mois));
        return new TUJournal(transaction, vCsAgence, transaction.getSession().getCodeLibelle(vCsAgence), annee, mois,
                vLibellemois);
    }

    /**
     * Recherche du bouclement
     * 
     * @param transaction
     * @param annee
     * @param mois
     * @param csAgence
     * @param csRubriqueList
     * @return
     * @throws TUFWFindException
     */
    private static TUBouclementManager findBouclement(BTransaction transaction, String annee, String mois,
            String csAgence) throws TUFWFindException {
        TUBouclementManager bouclementMrg = null;
        try {
            bouclementMrg = new TUBouclementManager();
            bouclementMrg.setSession(transaction.getSession());
            bouclementMrg.setForAnneeComptable(annee);
            if (!JadeStringUtil.isBlankOrZero(mois)) {
                bouclementMrg.setForMoisComptable(mois);
            }
            if (!JadeStringUtil.isBlankOrZero(csAgence)) {
                bouclementMrg.setForCsAgence(csAgence);
            }
            bouclementMrg.find();
        } catch (Exception e) {
            throw new TUFWFindException("TUJournalGenerator.findBouclement()", bouclementMrg.getCurrentSqlQuery(), e);
        }
        return bouclementMrg;
    }

    /**
     * @param bouclementManager
     * @return une arrayList des idBouclement
     */
    private static ArrayList findListIdBouclement(TUBouclementManager bouclementManager) {
        ArrayList myArrayList = new ArrayList();
        Iterator iter = bouclementManager.iterator();
        while (iter.hasNext()) {
            myArrayList.add(((TUBouclement) iter.next()).getIdBouclement());
        }
        return myArrayList;
    }

    /**
     * Permet de générer un journal statistique
     * 
     * @param transaction
     * @param annee
     * @param mois
     * @param csAgence
     * @param csRubriqueList
     * @param typeCategorieRubrique
     * @param typeChargement
     * @return
     * @throws Exception
     */
    public static TUJournal generate(BTransaction transaction, String annee, String mois, String csAgence,
            ArrayList csRubriqueList, String typeCategorieRubrique, String typeChargement) throws Exception {
        TUJournal journal = null;
        try {
            // Recherche du détail de bouclement pour le mois et l'année en
            // paramètre
            TUBouclementManager bouclementMgr = findBouclement(transaction, annee, mois, csAgence);
            if (!bouclementMgr.hasErrors()) {
                // Création du journal
                journal = createJournal(transaction, annee, mois, csAgence,
                        (TUBouclement) bouclementMgr.getFirstEntity());
                // Lecture des lignes de détail pour le bouclement en question
                TUDetailGroupeCategorieRubriqueManager detailMgr = new TUDetailGroupeCategorieRubriqueManager();
                detailMgr.setSession(transaction.getSession());
                detailMgr.setOrder(TUJournalGenerator.GRP_BGRCID);
                // detailMgr.setForIdBouclement(bouclement.getIdBouclement());
                detailMgr.setInIdBouclement(findListIdBouclement(bouclementMgr));
                detailMgr.setForCsType(typeCategorieRubrique);
                detailMgr.setForCsRubriqueList(csRubriqueList);
                try {
                    detailMgr.find(BManager.SIZE_NOLIMIT);
                } catch (Exception e) {
                    throw new TUFWFindException(
                            "TUJournalGenerator.generate() : Problème lors de la lecture des détails",
                            detailMgr.getCurrentSqlQuery(), e);
                }
                if (detailMgr.size() > 0) {
                    journal.chargeJournal(transaction, detailMgr, typeChargement, typeCategorieRubrique);
                    // addGroupeCategories(transaction, detailMgr,
                    // Integer.toString(typeCategorieRubrique), typeChargement);
                }
            } else {
                throw new TUProcessJournalGeneratorException("Erreur lors de la recherche du bouclement", bouclementMgr
                        .getErrors().toString());
            }
            // Traitement des exceptions soulevées
        } catch (TUFWException e) {
            throw new Exception(e.toString());
        } catch (TUProcessJournalGeneratorException e) {
            throw new Exception(e.toString());
        } catch (TUProcessJournalException e) {
            throw new Exception(e.toString());
        }
        return journal;
    }

    /**
     * Permet de générer un journal statistique
     * 
     * @param transaction
     * @param annee
     * @param mois
     * @param csAgence
     * @param typeCategorieRubrique
     * @param typeChargement
     * @return
     * @throws Exception
     */
    public static TUJournal generate(BTransaction transaction, String annee, String mois, String csAgence,
            String typeCategorieRubrique, String typeChargement) throws Exception {
        return generate(transaction, annee, mois, csAgence, new ArrayList(), typeCategorieRubrique, typeChargement);
    }

    /**
     * Constructeur de la classe
     */
    public TUJournalGenerator() {
        super();
    }
}
