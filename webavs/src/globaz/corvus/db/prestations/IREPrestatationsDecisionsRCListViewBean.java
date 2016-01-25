package globaz.corvus.db.prestations;

import globaz.corvus.vb.decisions.REDecisionJointDemandeRenteViewBean;
import globaz.corvus.vb.prestations.REPrestationsJointTiersViewBean;

/**
 * Interface commune entre {@link REDecisionJointDemandeRenteViewBean} et {@link REPrestationsJointTiersViewBean}<br/>
 * Permet de centralis� le traitement de la recherche de date de fin dans {@link REPrestationsDecisionsRCListFormatter}
 * 
 * @author PBA
 */
public interface IREPrestatationsDecisionsRCListViewBean {

    public String getCsTypeDecision();

    /**
     * Retourne la date de d�but � afficher, s�lectionn�e par le traitement dans
     * {@link REPrestationsDecisionsRCListFormatter}
     * 
     * @return La date de d�but � afficher
     */
    public String getDateDebutAffichage();

    public String getDateDebutDroit();

    /**
     * Retourne la date de fin � afficher, s�lectionn�e par le traitement dans
     * {@link REPrestationsDecisionsRCListFormatter}
     * 
     * @return La date de fin � afficher
     */
    public String getDateFinAffichage();

    public String getDateFinDroit();

    public String getDateFinRetro();

    public String getGenrePrestation();

    public String getGenrePrestationAffichage();

    public String getIdDecision();

    public String getIdPrestation();

    public int getNumDateDebutDroit();

    public void setDateDebutAffichage(String dateDebutAffichage);

    public void setDateFinAffichage(String dateFinAffichage);

    public void setGenrePrestationAffichage(String genrePrestationAffichage);
}
