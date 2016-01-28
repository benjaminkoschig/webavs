package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;
import java.util.List;

/**
 * Interface sur l'assurance Date de création : (28.05.2002 09:11:43)
 * 
 * @author David Girardin
 */
public interface IAFAssurance extends BIEntity {

    public static final String GEN_PARAM_ASS_AGE_MAX = "826004";
    public static final String GEN_PARAM_ASS_AGE_MIN = "826003";
    public static final String GEN_PARAM_ASS_CATEGORIE_AF = "826011";
    public static final String GEN_PARAM_ASS_CODE_CALC_AGE_MAX = "826006";
    public static final String GEN_PARAM_ASS_CODE_CALC_AGE_MIN = "826005";
    public static final String GEN_PARAM_ASS_EXCLUSION_CAT = "826010";

    public static final String GEN_PARAM_ASS_FACT_EMPLOYE = "826007";
    /** genre de paramètre d'assurance */
    /** Genre Param Assurance */
    public static final String GEN_PARAM_ASS_FRANCHISE = "826001";

    public static final String GEN_PARAM_ASS_PLAFOND = "826002";
    public static final String GEN_PARAM_ASS_REDUCTION = "826008";
    public static final String GEN_PARAM_ASS_REMISE = "826009";
    /** Assurance paritaire */
    public final static String PARITAIRE = "801001";
    /** Assurance personnelle */
    public final static String PERSONNEL = "801002";
    /** Assurance autre */
    public final static String TYPE_ASS_AUTRES = "812004";
    /** Assurance AF */
    public final static String TYPE_ASS_COTISATION_AF = "812002";
    /** Assurance AVS/AI */
    public final static String TYPE_ASS_COTISATION_AVS_AI = "812001";
    /** Assurance FFPP */
    public final static String TYPE_ASS_FFPP = "812009";
    /** Assurance Frais administratifs */
    public final static String TYPE_ASS_FRAIS_ADMIN = "812003";
    /** Assurance LAMAT */
    public final static String TYPE_ASS_MATERNITE = "812005";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @exception Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'assurances.<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche dans la liste suivante : <br>
     * <br>
     * <li><br> <li><br>
     * <br>
     * 
     * @return IAFAssurance[] tableau d'assurances trouvées, vide si aucune occurence n'est trouvée
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public IAFAssurance[] findAssurance(Hashtable params) throws Exception;

    /**
     * Retourne le code système du canton lié à l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système du canton lié à l'assurance
     */
    public java.lang.String getAssuranceCanton();

    /**
     * Retourne le code système du genre de l'assurance (AVS, AF, etc) Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système du genre de l'assurance (AVS, AF, etc)
     */
    public java.lang.String getAssuranceGenre();

    /**
     * Retourne l'id de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return l'id de l'assurance
     */
    public java.lang.String getAssuranceId();

    /**
     * Retourne le libellé allemand de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return le libellé allemand de l'assurance
     */
    public java.lang.String getAssuranceLibelleAl();

    /**
     * Retourne le libellé allemand court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return le libellé allemand court de l'assurance
     */
    public java.lang.String getAssuranceLibelleCourtAl();

    /**
     * Retourne le libellé français court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return le libellé français court de l'assurance
     */
    public java.lang.String getAssuranceLibelleCourtFr();

    /**
     * Retourne le libellé italien court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return le libellé italien court de l'assurance
     */
    public java.lang.String getAssuranceLibelleCourtIt();

    /**
     * Retourne le libellé français de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return le libellé français de l'assurance
     */
    public java.lang.String getAssuranceLibelleFr();

    /**
     * Retourne le libellé italien de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return le libellé italien de l'assurance
     */
    public java.lang.String getAssuranceLibelleIt();

    /**
     * Retourne la valeur du paramètre de type "typeParam" de l'assurance idAssurance Date de création : (28.11.2007
     * 07:21:44)
     * 
     * @param la
     *            valeur du paramètre ou une chaîne vide
     */
    public String getParametreAssuranceValeur(java.lang.String idAssurance, java.lang.String genre,
            java.lang.String date, java.lang.String sexe) throws Exception;

    /**
     * Retourne l'id de la rubrique comptable Date de création : (26.02.2003 09:54:44)
     * 
     * @return l'id de la rubrique comptable
     */
    public java.lang.String getRubriqueId();

    /**
     * Retourne une liste de taux par rapport à un idAssurance,sexe,une date de début et de fin Date de création :
     * (26.02.2003 09:54:44)
     * 
     * @return le code système du genre de l'assurance (AVS, AF, etc)
     */
    public List getTauxList(String idAssurance, String sexe, String dateDebut, String dateFin) throws Exception;

    /**
     * Retourne le code système du type de l'assurance (paritaire, personnel) Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système du type de l'assurance (paritaire, personnel)
     */
    public java.lang.String getTypeAssurance();

    /**
     * Recuperer l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la recuperation a échoué
     */

    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * Définit le code système du canton lié à l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceCanton
     *            le code système du canton lié à l'assurance
     */
    public void setAssuranceCanton(java.lang.String newAssuranceCanton);

    /**
     * Définit le code système du genre de l'assurance (AVS, AF, etc) Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceGenre
     *            le code système du genre de l'assurance (AVS, AF, etc)
     */
    public void setAssuranceGenre(java.lang.String newAssuranceGenre);

    /**
     * Définit l'id de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceId
     *            l'id de l'assurance
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * Définit le libellé allemand de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleAl
     *            le libellé allemand de l'assurance
     */
    public void setAssuranceLibelleAl(java.lang.String newAssuranceLibelleAl);

    /**
     * Définit le libellé allemand court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleCourtAl
     *            le libellé allemand court de l'assurance
     */
    public void setAssuranceLibelleCourtAl(java.lang.String newAssuranceLibelleCourtAl);

    /**
     * Définit le libellé français court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleCourtFr
     *            le libellé français court de l'assurance
     */
    public void setAssuranceLibelleCourtFr(java.lang.String newAssuranceLibelleCourtFr);

    /**
     * Définit le libellé italien court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleCourtIt
     *            le libellé italien court de l'assurance
     */
    public void setAssuranceLibelleCourtIt(java.lang.String newAssuranceLibelleCourtIt);

    /**
     * Définit le libellé français de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleFr
     *            le libellé français de l'assurance
     */
    public void setAssuranceLibelleFr(java.lang.String newAssuranceLibelleFr);

    /**
     * Définit le libellé italien de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleIt
     *            le libellé italien de l'assurance
     */
    public void setAssuranceLibelleIt(java.lang.String newAssuranceLibelleIt);

    /**
     * Définit l'id de la rubrique comptable Date de création : (26.02.2003 09:54:44)
     * 
     * @param newRubriqueId
     *            l'id de la rubrique comptable
     */
    public void setRubriqueId(java.lang.String newRubriqueId);

    /**
     * Définit le code système du type de l'assurance (paritaire, personnel) Date de création : (26.02.2003 09:54:44)
     * 
     * @param newTypeAssurance
     *            le code système du type de l'assurance (paritaire, personnel)
     */
    public void setTypeAssurance(java.lang.String newTypeAssurance);

    /**
     * Mise à jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la mise é jour a échoué
     */
    public void update(BITransaction transaction) throws Exception;

}
