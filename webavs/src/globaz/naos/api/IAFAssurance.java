package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;
import java.util.List;

/**
 * Interface sur l'assurance Date de cr�ation : (28.05.2002 09:11:43)
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
    /** genre de param�tre d'assurance */
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
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'assurances.<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche dans la liste suivante : <br>
     * <br>
     * <li><br> <li><br>
     * <br>
     * 
     * @return IAFAssurance[] tableau d'assurances trouv�es, vide si aucune occurence n'est trouv�e
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public IAFAssurance[] findAssurance(Hashtable params) throws Exception;

    /**
     * Retourne le code syst�me du canton li� � l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du canton li� � l'assurance
     */
    public java.lang.String getAssuranceCanton();

    /**
     * Retourne le code syst�me du genre de l'assurance (AVS, AF, etc) Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du genre de l'assurance (AVS, AF, etc)
     */
    public java.lang.String getAssuranceGenre();

    /**
     * Retourne l'id de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return l'id de l'assurance
     */
    public java.lang.String getAssuranceId();

    /**
     * Retourne le libell� allemand de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le libell� allemand de l'assurance
     */
    public java.lang.String getAssuranceLibelleAl();

    /**
     * Retourne le libell� allemand court de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le libell� allemand court de l'assurance
     */
    public java.lang.String getAssuranceLibelleCourtAl();

    /**
     * Retourne le libell� fran�ais court de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le libell� fran�ais court de l'assurance
     */
    public java.lang.String getAssuranceLibelleCourtFr();

    /**
     * Retourne le libell� italien court de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le libell� italien court de l'assurance
     */
    public java.lang.String getAssuranceLibelleCourtIt();

    /**
     * Retourne le libell� fran�ais de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le libell� fran�ais de l'assurance
     */
    public java.lang.String getAssuranceLibelleFr();

    /**
     * Retourne le libell� italien de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le libell� italien de l'assurance
     */
    public java.lang.String getAssuranceLibelleIt();

    /**
     * Retourne la valeur du param�tre de type "typeParam" de l'assurance idAssurance Date de cr�ation : (28.11.2007
     * 07:21:44)
     * 
     * @param la
     *            valeur du param�tre ou une cha�ne vide
     */
    public String getParametreAssuranceValeur(java.lang.String idAssurance, java.lang.String genre,
            java.lang.String date, java.lang.String sexe) throws Exception;

    /**
     * Retourne l'id de la rubrique comptable Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return l'id de la rubrique comptable
     */
    public java.lang.String getRubriqueId();

    /**
     * Retourne une liste de taux par rapport � un idAssurance,sexe,une date de d�but et de fin Date de cr�ation :
     * (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du genre de l'assurance (AVS, AF, etc)
     */
    public List getTauxList(String idAssurance, String sexe, String dateDebut, String dateFin) throws Exception;

    /**
     * Retourne le code syst�me du type de l'assurance (paritaire, personnel) Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du type de l'assurance (paritaire, personnel)
     */
    public java.lang.String getTypeAssurance();

    /**
     * Recuperer l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la recuperation a �chou�
     */

    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * D�finit le code syst�me du canton li� � l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceCanton
     *            le code syst�me du canton li� � l'assurance
     */
    public void setAssuranceCanton(java.lang.String newAssuranceCanton);

    /**
     * D�finit le code syst�me du genre de l'assurance (AVS, AF, etc) Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceGenre
     *            le code syst�me du genre de l'assurance (AVS, AF, etc)
     */
    public void setAssuranceGenre(java.lang.String newAssuranceGenre);

    /**
     * D�finit l'id de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceId
     *            l'id de l'assurance
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * D�finit le libell� allemand de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleAl
     *            le libell� allemand de l'assurance
     */
    public void setAssuranceLibelleAl(java.lang.String newAssuranceLibelleAl);

    /**
     * D�finit le libell� allemand court de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleCourtAl
     *            le libell� allemand court de l'assurance
     */
    public void setAssuranceLibelleCourtAl(java.lang.String newAssuranceLibelleCourtAl);

    /**
     * D�finit le libell� fran�ais court de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleCourtFr
     *            le libell� fran�ais court de l'assurance
     */
    public void setAssuranceLibelleCourtFr(java.lang.String newAssuranceLibelleCourtFr);

    /**
     * D�finit le libell� italien court de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleCourtIt
     *            le libell� italien court de l'assurance
     */
    public void setAssuranceLibelleCourtIt(java.lang.String newAssuranceLibelleCourtIt);

    /**
     * D�finit le libell� fran�ais de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleFr
     *            le libell� fran�ais de l'assurance
     */
    public void setAssuranceLibelleFr(java.lang.String newAssuranceLibelleFr);

    /**
     * D�finit le libell� italien de l'assurance Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceLibelleIt
     *            le libell� italien de l'assurance
     */
    public void setAssuranceLibelleIt(java.lang.String newAssuranceLibelleIt);

    /**
     * D�finit l'id de la rubrique comptable Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newRubriqueId
     *            l'id de la rubrique comptable
     */
    public void setRubriqueId(java.lang.String newRubriqueId);

    /**
     * D�finit le code syst�me du type de l'assurance (paritaire, personnel) Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newTypeAssurance
     *            le code syst�me du type de l'assurance (paritaire, personnel)
     */
    public void setTypeAssurance(java.lang.String newTypeAssurance);

    /**
     * Mise � jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la mise � jour a �chou�
     */
    public void update(BITransaction transaction) throws Exception;

}
