package globaz.helios.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Cette classe représente un plan comptable. Un plan comptable est lié à un <b>Exercice Comptable</b>
 * {@link ICGExerciceComptable}<br>
 * Pour récupérer un plan comptable, il faut fournir un ID et exécuter un appel à la méthode <b>retrieve</b>.<br>
 * <br>
 */

public interface ICGPlanComptable extends BIEntity {
    public final static int AK_EXTERNE = 1;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:04)
     * 
     * @return int
     */
    public String getAvoir();

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:18)
     * 
     * @return int
     */
    public String getAvoirProvisoire();

    public java.lang.String getCodeISOMonnaie();

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:39)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDoit();

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDoitProvisoire();

    public java.lang.String getDomaineLibelle();

    public java.lang.String getGenreLibelle();

    /**
     * Getter
     */
    public java.lang.String getIdCompte();

    public java.lang.String getIdCompteTVA();

    public java.lang.String getIdDomaine();

    /**
     * Renvoie l'ID de l'exercice comptable.
     * 
     * @return l'ID de l'exercice comptable
     */
    public java.lang.String getIdExerciceComptable();

    public java.lang.String getIdExterne();

    public java.lang.String getIdGenre();

    public java.lang.String getIdMandat();

    public java.lang.String getIdNature();

    public java.lang.String getIdParametreBouclement();

    public java.lang.String getIdRemarque();

    public java.lang.String getIdSecteurAVS();

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 18:21:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdSolde();

    public java.lang.String getLibelle();

    public java.lang.String getLibelleDe();

    public java.lang.String getLibelleFr();

    public java.lang.String getLibelleIt();

    public java.lang.String getNumeroCompteAVS();

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    public java.lang.String getRemarque();

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSolde();

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSoldeProvisoire();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.05.2003 15:10:17)
     * 
     * @param newAlternateKey
     *            int
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    void setAlternateKey(int newAlternateKey) throws java.lang.Exception;

    public void setAReouvrir(java.lang.Boolean newAReouvrir);

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:04)
     * 
     * @param newAvoir
     *            int
     */
    public void setAvoir(String newAvoir);

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:54:18)
     * 
     * @param newAvoirProvisoire
     *            int
     */
    public void setAvoirProvisoire(String newAvoirProvisoire);

    public void setCodeISOMonnaie(java.lang.String newCodeISOMonnaie);

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:39)
     * 
     * @param newDoit
     *            java.lang.String
     */
    public void setDoit(java.lang.String newDoit);

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 12:53:56)
     * 
     * @param newDoitProvisoire
     *            java.lang.String
     */
    public void setDoitProvisoire(java.lang.String newDoitProvisoire);

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 13:25:11)
     * 
     * @param newEstCompteAvs
     *            boolean
     */
    public void setEstCompteAvs(Boolean newEstCompteAvs);

    public void setEstConfidentiel(java.lang.Boolean newEstConfidentiel);

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 17:24:49)
     * 
     * @param newEstPeriode
     *            java.lang.Boolean
     */
    public void setEstPeriode(java.lang.Boolean newEstPeriode);

    public void setEstVerrouille(java.lang.Boolean newEstVerrouille);

    /**
     * Setter
     */
    public void setIdCompte(java.lang.String newIdCompte);

    public void setIdCompteTVA(java.lang.String newIdCompteTVA);

    public void setIdDomaine(java.lang.String newIdDomaine);

    /**
     * Définit l'ID de l'exercice comptable.
     * 
     * @param newIdExerciceComptable
     *            newIdExerciceComptable
     */
    public void setIdExerciceComptable(java.lang.String newIdExerciceComptable);

    public void setIdExterne(java.lang.String newIdExterne);

    public void setIdGenre(java.lang.String newIdGenre);

    public void setIdMandat(java.lang.String newIdMandat);

    public void setIdNature(java.lang.String newIdNature);

    public void setIdParametreBouclement(java.lang.String newIdParametreBouclement);

    public void setIdRemarque(java.lang.String newIdRemarque);

    public void setIdSecteurAVS(java.lang.String newIdSecteurAVS);

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 18:21:21)
     * 
     * @param newIdSolde
     *            java.lang.String
     */
    public void setIdSolde(java.lang.String newIdSolde);

    public void setLibelleDe(java.lang.String newLibelleDe);

    public void setLibelleFr(java.lang.String newLibelleFr);

    public void setLibelleIt(java.lang.String newLibelleIt);

    public void setNumeroCompteAVS(java.lang.String newNumeroCompteAVS);

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    public void setRemarque(java.lang.String rem);

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:02)
     * 
     * @param newSolde
     *            java.lang.String
     */
    public void setSolde(java.lang.String newSolde);

    /**
     * Insérez la description de la méthode ici. Date de création : (15.11.2002 15:51:47)
     * 
     * @param newSoldeProvisoir
     *            java.lang.String
     */
    public void setSoldeProvisoire(java.lang.String newSoldeProvisoire);
}
