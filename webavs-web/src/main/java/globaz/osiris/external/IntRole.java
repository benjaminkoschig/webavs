package globaz.osiris.external;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import globaz.naos.api.IAFAffiliation;

/**
 * Interface représentant un rôle au format définit par la comptabilité auxiliaire
 * 
 * @author auteur
 */
public interface IntRole extends BIEntity {
    public String PERIODICITE_ANNUELLE = "802004";
    public String PERIODICITE_MENSUELLE = "802001";
    public String PERIODICITE_SEMESTRIELLE = "802003";
    public String PERIODICITE_TRIMESTRIELLE = "802002";
    public String ROLE_ADMINISTRATEUR = "517041";
    public String ROLE_ADMINISTRATION = "517003";
    public String ROLE_AF = "517036";
    public String ROLE_AFFILIE = "517002";
    public String ROLE_AFFILIE_PARITAIRE = "517039";
    public String ROLE_AFFILIE_PERSONNEL = "517040";
    public String ROLE_AMC = "19170089";
    public String ROLE_APG = "517033";
    public String ROLE_ASSURE = "517001";
    public String ROLE_BANQUE = "517004";
    public String ROLE_CONTRIBUABLE = "517005";
    public String ROLE_DEBITEUR = "517090";
    public String ROLE_FCF = "19170088";
    public String ROLE_IJAI = "517034";
    public String ROLE_PCF = "517045";
    public String ROLE_RENTIER = "517038";
    public String ROLE_BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES = "68902001";

    /**
     * 
     * 
     * @param idRole
     *            idRole
     * @param idExterne
     *            idExterne
     * @exception java.lang.Exception
     *                si ???
     */
    public void checkIdExterneRoleFormat(String idRole, String idExterne) throws java.lang.Exception;

    /**
     * @return
     * @param idRole
     *            idRole
     * @param idExterneRole
     *            idExterneRole
     */
    public String formatIdExterneRole(String idRole, String idExterneRole);

    public IAFAffiliation getAffiliation();

    /**
     * Renvoie l'affilié correspondant au numéro en paramètre.
     * 
     * @param idExterneRole Un numéro d'affilié
     * @return Un objet affilié
     */
    public IAFAffiliation getAffiliation(String idExterneRole);

    /**
     * Retourne la date de début d'affiliation au format jj.mm.aaaa En cas de problème retourne ""
     * 
     * @return la valeur de la propriété dateDebut
     */
    public String getDateDebut();

    /**
     * Retourne la date de début d'affiliation au format jj.mm.aaaa pour un idExterneRole donné En cas de problème
     * retourne ""
     * 
     * @param idExterneRole
     * @return la valeur de la propriété dateDebut
     */
    public String getDateDebut(String idExterneRole);

    /**
     * Retourne la date de début et date de fin d'affiliation au format jj.mm.aaaa En cas de problème retourne "-"
     * 
     * @return la valeur de la propriété dateDebutDateFin
     */
    public String getDateDebutDateFin();

    /**
     * Retourne la date de début et date de fin d'affiliation au format jj.mm.aaaa pour un idExterneRole donné En cas de
     * problème retourne "-"
     * 
     * @param idExterneRole
     * @return la valeur de la propriété dateDebutDateFin
     */
    public String getDateDebutDateFin(String idExterneRole);

    /**
     * Retourne la date de fin d'affiliation au format jj.mm.aaaa En cas de problème retourne ""
     * 
     * @return la valeur de la propriété dateFin
     */
    public String getDateFin();

    /**
     * Retourne la date de fin d'affiliation au format jj.mm.aaaa pour un idExterneRole donné En cas de problème
     * retourne ""
     * 
     * @param idExterneRole
     * @return la valeur de la propriété dateFin
     */
    public String getDateFin(String idExterne);

    /**
     * Renvoie la valeur de la propriété description
     * 
     * @return la valeur de la propriété description
     */
    public String getDescription();

    /**
     * Renvoie la valeur de la propriété description
     * 
     * @return la valeur de la propriété description
     * @param codeISOLangue
     *            codeISOLangue
     */
    public String getDescription(String codeISOLangue);

    /**
     * Renvoie la valeur de la propriété idCategorie
     * 
     * @return la valeur de la propriété idCategorie
     */
    public String getIdCategorie();

    /**
     * Renvoie la valeur de la propriété idCategorie en fonction de lidExterneRole
     * 
     * @param idExterneRole
     * @return la valeur de la propriété idCategorie en fonction de lidExterneRole
     */
    public String getIdCategorie(String idExterneRole);

    /**
     * Renvoie la valeur de la propriété idExterne
     * 
     * @return la valeur de la propriété idExterne
     */
    public String getIdExterne();

    /**
     * Renvoie la valeur de la propriété idRole
     * 
     * @return la valeur de la propriété idRole
     */
    public String getIdRole();

    /**
     * Renvoie la valeur de la propriété idTiers
     * 
     * @return la valeur de la propriété idTiers
     */
    public String getIdTiers();

    public String getMotifFin();

    public String getPeriodicite();

    /**
     * Renvoie la valeur de la propriété tiers
     * 
     * @return la valeur de la propriété tiers
     */

    public IntTiers getTiers();

    /**
     * @return
     */
    public boolean isOnError();

    /**
     * @param transaction
     * @param idRole
     * @param idExterne
     * @throws java.lang.Exception
     */
    public void retrieve(BITransaction transaction, String idRole, String idExterne) throws java.lang.Exception;

    /**
     * @param idRole
     * @param idExterne
     * @throws java.lang.Exception
     */
    public void retrieve(String idRole, String idExterne) throws java.lang.Exception;

    /**
     * @param idTiers
     * @param idRole
     * @param transaction
     * @throws java.lang.Exception
     */
    public void retrieve(String idTiers, String idRole, BITransaction transaction) throws java.lang.Exception;
}
