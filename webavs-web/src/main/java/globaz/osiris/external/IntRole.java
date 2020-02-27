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
    String PERIODICITE_ANNUELLE = "802004";
    String PERIODICITE_MENSUELLE = "802001";
    String PERIODICITE_SEMESTRIELLE = "802003";
    String PERIODICITE_TRIMESTRIELLE = "802002";
    String ROLE_ADMINISTRATEUR = "517041";
    String ROLE_ADMINISTRATION = "517003";
    String ROLE_AF = "517036";
    String ROLE_AFFILIE = "517002";
    String ROLE_AFFILIE_PARITAIRE = "517039";
    String ROLE_AFFILIE_PERSONNEL = "517040";
    String ROLE_AMC = "19170089";
    String ROLE_APG = "517033";
    String ROLE_ASSURE = "517001";
    String ROLE_BANQUE = "517004";
    String ROLE_CONTRIBUABLE = "517005";
    String ROLE_DEBITEUR = "517090";
    String ROLE_FCF = "19170088";
    String ROLE_IJAI = "517034";
    String ROLE_PCF = "517045";
    String ROLE_RENTIER = "517038";
    String ROLE_BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES = "68902001";
    String ROLE_ASSOCIATION_PROFESSIONNELLE = "68902003";

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
    void checkIdExterneRoleFormat(String idRole, String idExterne) throws java.lang.Exception;

    /**
     * @return
     * @param idRole
     *            idRole
     * @param idExterneRole
     *            idExterneRole
     */
    String formatIdExterneRole(String idRole, String idExterneRole);

    IAFAffiliation getAffiliation();

    /**
     * Renvoie l'affilié correspondant au numéro en paramètre.
     *
     * @param idExterneRole Un numéro d'affilié
     * @return Un objet affilié
     */
    IAFAffiliation getAffiliation(String idExterneRole);

    /**
     * Retourne la date de début d'affiliation au format jj.mm.aaaa En cas de problème retourne ""
     *
     * @return la valeur de la propriété dateDebut
     */
    String getDateDebut();

    /**
     * Retourne la date de début d'affiliation au format jj.mm.aaaa pour un idExterneRole donné En cas de problème
     * retourne ""
     *
     * @param idExterneRole
     * @return la valeur de la propriété dateDebut
     */
    String getDateDebut(String idExterneRole);

    /**
     * Retourne la date de début et date de fin d'affiliation au format jj.mm.aaaa En cas de problème retourne "-"
     *
     * @return la valeur de la propriété dateDebutDateFin
     */
    String getDateDebutDateFin();

    /**
     * Retourne la date de début et date de fin d'affiliation au format jj.mm.aaaa pour un idExterneRole donné En cas de
     * problème retourne "-"
     *
     * @param idExterneRole
     * @return la valeur de la propriété dateDebutDateFin
     */
    String getDateDebutDateFin(String idExterneRole);

    /**
     * Retourne la date de fin d'affiliation au format jj.mm.aaaa En cas de problème retourne ""
     *
     * @return la valeur de la propriété dateFin
     */
    String getDateFin();

    /**
     * Retourne la date de fin d'affiliation au format jj.mm.aaaa pour un idExterneRole donné En cas de problème
     * retourne ""
     *
     * @param idExterne
     * @return la valeur de la propriété dateFin
     */
    String getDateFin(String idExterne);

    /**
     * Renvoie la valeur de la propriété description
     *
     * @return la valeur de la propriété description
     */
    String getDescription();

    /**
     * Renvoie la valeur de la propriété description
     *
     * @return la valeur de la propriété description
     * @param codeISOLangue
     *            codeISOLangue
     */
    String getDescription(String codeISOLangue);

    /**
     * Renvoie la valeur de la propriété idCategorie
     *
     * @return la valeur de la propriété idCategorie
     */
    String getIdCategorie();

    /**
     * Renvoie la valeur de la propriété idCategorie en fonction de lidExterneRole
     *
     * @param idExterneRole
     * @return la valeur de la propriété idCategorie en fonction de lidExterneRole
     */
    String getIdCategorie(String idExterneRole);

    /**
     * Renvoie la valeur de la propriété idExterne
     *
     * @return la valeur de la propriété idExterne
     */
    String getIdExterne();

    /**
     * Renvoie la valeur de la propriété idRole
     *
     * @return la valeur de la propriété idRole
     */
    String getIdRole();

    /**
     * Renvoie la valeur de la propriété idTiers
     *
     * @return la valeur de la propriété idTiers
     */
    String getIdTiers();

    String getMotifFin();

    String getPeriodicite();

    /**
     * Renvoie la valeur de la propriété tiers
     *
     * @return la valeur de la propriété tiers
     */

    IntTiers getTiers();

    /**
     * @return
     */
    boolean isOnError();

    /**
     * @param transaction
     * @param idRole
     * @param idExterne
     * @throws java.lang.Exception
     */
    void retrieve(BITransaction transaction, String idRole, String idExterne) throws java.lang.Exception;

    /**
     * @param idRole
     * @param idExterne
     * @throws java.lang.Exception
     */
    void retrieve(String idRole, String idExterne) throws java.lang.Exception;

    /**
     * @param idTiers
     * @param idRole
     * @param transaction
     * @throws java.lang.Exception
     */
    void retrieve(String idTiers, String idRole, BITransaction transaction) throws java.lang.Exception;

    IAFAffiliation getAffiliationByNumAffAndIDRoleAndIDTiers(String numAff, String idRole, String idTiers);
}
