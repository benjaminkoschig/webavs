package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

public interface APIRubrique extends BIEntity {
    public final static int AK_IDEXTERNE = 1;
    public final static String AMENDE = "200012";
    public final static String AMORTISSEMENT = "200015";
    public final static String COMPTE_COMPENSATION = "200007";
    public final static String COMPTE_COURANT_CREANCIER = "200006";
    public final static String COMPTE_COURANT_DEBITEUR = "200005";
    // Les 4 codes syst�mes suivant ne doivent pas chang�s => Influence sur le
    // calcul des int�r�ts moratoires tardifs.
    public final static String COMPTE_FINANCIER = "200004";
    public final static String COTISATION_AVEC_MASSE = "200002";
    public final static String COTISATION_SANS_MASSE = "200003";
    public final static String FRAIS_POURSUITES = "200014";
    // Constantesdes rubriques
    public static final String ID_EXTERNE_BEGIN_WITH_2110_3300 = "2110.3300";
    public static final String ID_EXTERNE_BEGIN_WITH_2110_4000 = "2110.4000";

    public static final String ID_EXTERNE_BEGIN_WITH_2110_4010 = "2110.4010";
    public static final String ID_EXTERNE_BEGIN_WITH_2112_2112 = "2112.2112";
    public static final String ID_EXTERNE_BEGIN_WITH_2112_9996 = "2112.9996";

    public static final String ID_EXTERNE_BEGIN_WITH_2112_9997 = "2112.9997";
    public static final String ID_EXTERNE_BEGIN_WITH_2160_4030 = "2160.4030";
    public static final String ID_EXTERNE_BEGIN_WITH_6010_4030 = "6010.4030";
    public static final String ID_EXTERNE_BEGIN_WITH_7750_4030 = "7750.4030";

    public static final String ID_EXTERNE_BEGIN_WITH_9100_6000 = "9100.6000";
    public final static String INTERETS = "200008";
    public final static String INTERETS_MORATOIRES = "200009";
    public final static String INTERETS_REMUNERATOIRES = "200010";
    public final static String RECOUVREMENT = "200016";
    public final static String STANDARD = "200001";
    public final static String TAXATION_OFFICE = "200013";

    public final static String TAXE_SOMMATION = "200011";

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception java.lang.Exception
     *                si la suppression a �chou�e
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2002 18:01:41)
     * 
     * @return String
     */
    public String getAlias();

    /**
     * Getter
     */
    public String getAnneeCotisation();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 14:00:41)
     * 
     * @return globaz.osiris.db.comptes.CACompteCourant
     */
    public APICompteCourant getCompteCourant();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.12.2001 15:16:55)
     * 
     * @return String
     */
    public String getDescription();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.12.2001 15:16:55)
     * 
     * @return String
     */
    public String getDescription(String codeISOLangue);

    public java.lang.Boolean getEstVentilee();

    public String getIdContrepartie();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.12.2001 11:02:39)
     * 
     * @return String
     */
    public String getIdentificationSource();

    public String getIdExterne();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 13:58:09)
     * 
     * @return String
     */
    public String getIdExterneCompteCourantEcran();

    public String getIdRubrique();

    public String getIdSecteur();

    public String getIdTraduction();

    public String getNatureRubrique();

    public String getNumCompteCG();

    /**
     * R�cup�re le num�ro de compte pour la mise en compte en comptabilit� g�n�rale Date de cr�ation : (28.10.2002
     * 10:48:53)
     * 
     * @return String le num�ro de compte en comptabilit� g�n�rale
     */
    public String getNumeroComptePourCG();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.01.2002 10:36:57)
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    public APITauxRubriques getTauxRubriques();

    public java.lang.Boolean getTenirCompteur();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a �chou�
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.03.2002 18:01:41)
     * 
     * @param newAlias
     *            String
     */
    public void setAlias(String newAlias);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (24.02.2003 10:01:41)
     * 
     * @param alternateKey
     *            String
     */
    void setAlternateKey(int alternateKey);

    /**
     * Setter
     */
    public void setAnneeCotisation(String newAnneeCotisation);

    /**
     * Description dans la langue de l'utilisateur Date de cr�ation : (19.12.2001 10:55:21)
     * 
     * @param newDescription
     *            String
     */
    public void setDescription(String newDescription);

    /**
     * Description dans la langue fournie Date de cr�ation : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            String
     * @param codeISOLangue
     *            String
     */
    public void setDescriptionDe(String newDescription);

    /**
     * Description dans la langue fournie Date de cr�ation : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            String
     * @param codeISOLangue
     *            String
     */
    public void setDescriptionFr(String newDescription);

    /**
     * Description dans la langue fournie Date de cr�ation : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            String
     * @param codeISOLangue
     *            String
     */
    public void setDescriptionIt(String newDescription);

    public void setEstVentilee(java.lang.Boolean newEstVentilee);

    public void setIdContrepartie(String newIdContrepartie);

    public void setIdExterne(String newIdExterne);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 13:58:09)
     * 
     * @param newIdExterneCompteCourantEcran
     *            String
     */
    public void setIdExterneCompteCourantEcran(String newIdExterneCompteCourantEcran);

    public void setIdRubrique(String newIdRubrique);

    public void setIdSecteur(String newIdSecteur);

    public void setIdTraduction(String newIdTraduction);

    public void setNatureRubrique(String newNatureRubrique);

    public void setNumCompteCG(String newNumCompteCG);

    public void setTenirCompteur(java.lang.Boolean newTenirCompteur);

    /**
     * Met � jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise � jour a �chou�e
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
