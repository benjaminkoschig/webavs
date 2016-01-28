package globaz.tucana.statistiques;

import globaz.globall.db.BTransaction;
import globaz.tucana.db.journal.access.TUDetailGroupeCategorieRubriqueManager;
import java.util.ArrayList;

/**
 * Représentation des données d'un journal récap
 * 
 * @author fgo date de création : 13 juil. 06
 * @version : version 1.0
 * 
 */
public class TUSupportJournalRecap {
    private String agence = null;

    private String annee = null;

    private ArrayList lignes = null;

    private String mois = null;

    /**
     * Constructeur
     * 
     * @param _annee
     * @param _mois
     * @param _agence
     */
    public TUSupportJournalRecap(String _annee, String _mois, String _agence) {
        annee = _annee;
        mois = _mois;
        agence = _agence;
        lignes = new ArrayList();

    }

    /**
     * Ajout des lignes de récap
     * 
     * @param transaction
     * @param detailMgr
     */
    public void addLines(BTransaction transaction, TUDetailGroupeCategorieRubriqueManager detailMgr) {
        // TUDetailGroupeCategorieRubrique detail = null;
        // for (int i = 0; i < detailMgr.size(); i++){
        // detail = (TUDetailGroupeCategorieRubrique) detailMgr.getEntity(i);
        // //lignes.add(new
        // TUSupportJournalRecapLigne(transaction.getSession().getCodeLibelle(detail.getCsGroupeCategorie()));
        // }

    }

    public String getAgence() {
        return agence;
    }

    public String getAnnee() {
        return annee;
    }

    public ArrayList getLignes() {
        return lignes;
    }

    public String getMois() {
        return mois;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setLignes(ArrayList lignes) {
        this.lignes = lignes;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

}
