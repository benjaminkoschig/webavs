/*
 * Créé le 9 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendarGregorianStandard;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.hera.tools.nss.SFUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.ITIPersonne;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * DOCUMENT ME!
 * 
 * @author mmu Jointure entre les conjoints, les membres de famille et les tiers dans le cas ou juste idTiers est
 *         sauvegardé
 */
public class SFApercuRelationConjoint extends SFRelationConjoint {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String A_CAN1 = "A_CAN1";
    public static final String A_CAN2 = "A_CAN2";
    public static final String A_CS_DOMAINE = "A_CS_DOMAINE";
    public static final String A_DEC1 = "A_DEC1";
    public static final String A_DEC2 = "A_DEC2";
    /** Alias des champs du select */
    public static final String A_IDTI1 = "A_IDTI1";
    public static final String A_IDTI2 = "A_IDTI2";
    public static final String A_NAI1 = "A_NAI1";

    public static final String A_NAI2 = "A_NAI2";
    public static final String A_NAT1 = "A_NAT1";
    public static final String A_NAT2 = "A_NAT2";
    public static final String A_NOM1 = "A_NOM1";
    public static final String A_NOM2 = "A_NOM2";
    public static final String A_PAY1 = "A_PAY1";
    public static final String A_PAY2 = "A_PAY2";
    public static final String A_PRENOM1 = "A_PRENOM1";
    public static final String A_PRENOM2 = "A_PRENOM2";
    // public static final String A_AVS1 = "A_AVS1";
    public static final String A_SEXE1 = "A_SEXE1";
    // public static final String A_AVS2 = "A_AVS2";
    public static final String A_SEXE2 = "A_SEXE2";
    public static final String A_TI_AVS1 = "A_TI_AVS1";
    public static final String A_TI_AVS2 = "A_TI_AVS2";
    public static final String A_TI_CAN1 = "A_TI_CAN1";
    public static final String A_TI_CAN2 = "A_TI_CAN2";
    public static final String A_TI_DEC1 = "A_TI_DEC1";
    public static final String A_TI_DEC2 = "A_TI_DEC2";
    public static final String A_TI_NAI1 = "A_TI_NAI1";
    public static final String A_TI_NAI2 = "A_TI_NAI2";
    public static final String A_TI_NOM1 = "A_TI_NOM1";
    public static final String A_TI_NOM2 = "A_TI_NOM2";
    public static final String A_TI_PAY1 = "A_TI_PAY1";
    public static final String A_TI_PAY2 = "A_TI_PAY2";
    public static final String A_TI_PRENOM1 = "A_TI_PRENOM1";
    public static final String A_TI_PRENOM2 = "A_TI_PRENOM2";
    public static final String A_TI_SEXE1 = "A_TI_SEXE1";
    public static final String A_TI_SEXE2 = "A_TI_SEXE2";
    private static final String TABLE_AVS1 = "TI_AVS_1";
    private static final String TABLE_AVS2 = "TI_AVS_2";
    private static final String TABLE_M1 = "MEMBRE_1";
    private static final String TABLE_M2 = "MEMBRE_2";
    private static final String TABLE_PERS1 = "TI_PERS_1";
    private static final String TABLE_PERS2 = "TI_PERS_2";
    private static final String TABLE_TI1 = "TI_TI_1";

    private static final String TABLE_TI2 = "TI_TI_2";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Effectue une jointure sur (relationConjoint, conjoint, membreFamille1, membreFamille2)
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {
        return schema + SFRelationConjoint.TABLE_NAME + " AS " + SFRelationConjoint.TABLE_NAME + " INNER JOIN "
                + schema + SFConjoint.TABLE_NAME + " AS " + SFConjoint.TABLE_NAME + " ON ("
                + SFRelationConjoint.TABLE_NAME + "." + SFRelationConjoint.FIELD_IDCONJOINTS + " = "
                + SFConjoint.TABLE_NAME + "." + SFConjoint.FIELD_IDCONJOINTS + ")" +

                " INNER JOIN " + schema + SFMembreFamille.TABLE_NAME + " AS " + TABLE_M1 + " ON (" + TABLE_M1 + "."
                + SFMembreFamille.FIELD_IDMEMBREFAMILLE + " = " + SFConjoint.TABLE_NAME + "."
                + SFConjoint.FIELD_IDCONJOINT1 + ")"
                + SFMembreFamille.createJoinClause(schema, TABLE_M1, TABLE_TI1, TABLE_PERS1, TABLE_AVS1) +

                " INNER JOIN " + schema + SFMembreFamille.TABLE_NAME + " AS " + TABLE_M2 + " ON (" + TABLE_M2 + "."
                + SFMembreFamille.FIELD_IDMEMBREFAMILLE + " = " + SFConjoint.TABLE_NAME + "."
                + SFConjoint.FIELD_IDCONJOINT2 + ")"
                + SFMembreFamille.createJoinClause(schema, TABLE_M2, TABLE_TI2, TABLE_PERS2, TABLE_AVS2);
    }

    public static String createSelectClause() {
        // Les champs faisant partie de la table des membre doivent être aliasé
        // pour faire la requête
        String as = " AS ";
        String comma = " , ";
        String fields = TABLE_NAME + "." + FIELD_IDRELATIONCONJOINT + comma + TABLE_NAME + "." + FIELD_IDCONJOINTS
                + comma + TABLE_NAME + "." + FIELD_DATEDEBUT + comma + TABLE_NAME + "." + FIELD_DATEFIN + comma
                + TABLE_NAME + "." + FIELD_TYPERELATION + comma + TABLE_NAME + "." + FIELD_DATEDEBUT + comma
                + TABLE_NAME + "." + FIELD_DATEFIN + comma + TABLE_NAME + "." + FIELD_TYPERELATION + comma
                + SFConjoint.TABLE_NAME + "." + SFConjoint.FIELD_IDCONJOINT1 + comma + SFConjoint.TABLE_NAME + "."
                + SFConjoint.FIELD_IDCONJOINT2 + comma + TABLE_M1 + "." + SFMembreFamille.FIELD_IDTIERS + as + A_IDTI1
                + comma + TABLE_M1 + "." + SFMembreFamille.FIELD_NOM + as + A_NOM1 + comma + TABLE_M1 + "."
                + SFMembreFamille.FIELD_PRENOM + as + A_PRENOM1 + comma + TABLE_M1 + "." + SFMembreFamille.FIELD_SEX
                + as + A_SEXE1 + comma + TABLE_M1 + "." + SFMembreFamille.FIELD_DATENAISSANCE + as + A_NAI1 + comma
                + TABLE_M1 + "." + SFMembreFamille.FIELD_DATEDECES + as + A_DEC1 + comma + TABLE_M1 + "."
                + SFMembreFamille.FIELD_CANTON + as + A_CAN1 + comma + TABLE_M1 + "." + SFMembreFamille.FIELD_PAYS_DE_DOMICILE + as
                + A_PAY1 + comma + TABLE_M1 + "." + SFMembreFamille.FIELD_NATIONALITE + as + A_NAT1 + comma + TABLE_M1
                + "." + SFMembreFamille.FIELD_DOMAINE_APPLICATION + as + A_CS_DOMAINE + comma + TABLE_TI1 + "."
                + SFMembreFamille.FIELD_TI_NOM + as + A_TI_NOM1 + comma + TABLE_TI1 + "."
                + SFMembreFamille.FIELD_TI_PRENOM + as + A_TI_PRENOM1 + comma + TABLE_AVS1 + "."
                + SFMembreFamille.FIELD_AVS_NOAVS + as + A_TI_AVS1 + comma + TABLE_PERS1 + "."
                + SFMembreFamille.FIELD_PERS_SEX + as + A_TI_SEXE1 + comma + TABLE_PERS1 + "."
                + SFMembreFamille.FIELD_PERS_DATENAI + as + A_TI_NAI1 + comma + TABLE_PERS1 + "."
                + SFMembreFamille.FIELD_PERS_DATEDEC + as + A_TI_DEC1 + comma + TABLE_PERS1 + "."
                + SFMembreFamille.FIELD_PERS_CANTON + as + A_TI_CAN1 + comma + TABLE_TI1 + "."
                + SFMembreFamille.FIELD_TI_PAYS + as + A_TI_PAY1

                + comma + TABLE_M2 + "." + SFMembreFamille.FIELD_IDTIERS + as + A_IDTI2 + comma + TABLE_M2 + "."
                + SFMembreFamille.FIELD_NOM + as + A_NOM2 + comma + TABLE_M2 + "." + SFMembreFamille.FIELD_PRENOM + as
                + A_PRENOM2 + comma + TABLE_M2 + "." + SFMembreFamille.FIELD_SEX + as + A_SEXE2 + comma + TABLE_M2
                + "." + SFMembreFamille.FIELD_DATENAISSANCE + as + A_NAI2 + comma + TABLE_M2 + "."
                + SFMembreFamille.FIELD_DATEDECES + as + A_DEC2 + comma + TABLE_M2 + "." + SFMembreFamille.FIELD_CANTON
                + as + A_CAN2 + comma + TABLE_M2 + "." + SFMembreFamille.FIELD_PAYS_DE_DOMICILE + as + A_PAY2 + comma + TABLE_M2
                + "." + SFMembreFamille.FIELD_NATIONALITE + as + A_NAT2 + comma + TABLE_TI2 + "."
                + SFMembreFamille.FIELD_TI_NOM + as + A_TI_NOM2 + comma + TABLE_TI2 + "."
                + SFMembreFamille.FIELD_TI_PRENOM + as + A_TI_PRENOM2 + comma + TABLE_AVS2 + "."
                + SFMembreFamille.FIELD_AVS_NOAVS + as + A_TI_AVS2 + comma + TABLE_PERS2 + "."
                + SFMembreFamille.FIELD_PERS_SEX + as + A_TI_SEXE2 + comma + TABLE_PERS2 + "."
                + SFMembreFamille.FIELD_PERS_DATENAI + as + A_TI_NAI2 + comma + TABLE_PERS2 + "."
                + SFMembreFamille.FIELD_PERS_DATEDEC + as + A_TI_DEC2 + comma + TABLE_PERS2 + "."
                + SFMembreFamille.FIELD_PERS_CANTON + as + A_TI_CAN2 + comma + TABLE_TI2 + "."
                + SFMembreFamille.FIELD_TI_PAYS + as + A_TI_PAY2;
        return fields;
    }

    private String canton1 = "";
    private String canton2 = "";
    // Un seul champ suffit, car les 2 membres de la famille seront toujours
    // dans le même domaine
    private String csDomaine = "";
    private String dateDeces1 = "";
    private String dateDeces2 = "";
    private String dateNaissance1 = "";

    private String dateNaissance2 = "";
    private SFMembreFamille femme = null;

    private SFMembreFamille homme = null;
    private String idAssure1 = "";
    private String idAssure2 = "";
    private String idConjoint1 = "";
    private String idConjoint2 = "";
    private String idTiers1 = "";
    private String idTiers2 = "";
    private String nationalite1 = "";

    private String nationalite2 = "";
    private String noAvs1 = "";
    private String noAvs2 = "";
    private String nom1 = "";
    private String nom2 = "";
    private String pays1 = "";
    private String pays2 = "";
    private String prenom1 = "";

    private String prenom2 = "";

    private String provenance1 = "";
    private String provenance2 = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String sexe1 = "";

    private String sexe2 = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return createSelectClause();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = createFromClause(_getCollection());

        return from;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // Recherche la relation
        super._readProperties(statement);

        // Recherche les conjoints
        idConjoint1 = statement.dbReadString(SFConjoint.FIELD_IDCONJOINT1);
        idConjoint2 = statement.dbReadString(SFConjoint.FIELD_IDCONJOINT2);
        csDomaine = statement.dbReadNumeric(A_CS_DOMAINE);
        // Recherche le premier membre
        idTiers1 = statement.dbReadNumeric(A_IDTI1);
        if (JadeStringUtil.isIntegerEmpty(idTiers1)) {
            // on recherche en local si notre membre existe
            nom1 = statement.dbReadString(A_NOM1);
            prenom1 = statement.dbReadString(A_PRENOM1);
            sexe1 = statement.dbReadNumeric(A_SEXE1);
            dateNaissance1 = statement.dbReadDateAMJ(A_NAI1);
            dateDeces1 = statement.dbReadDateAMJ(A_DEC1);
            canton1 = statement.dbReadNumeric(A_CAN1);
            pays1 = statement.dbReadNumeric(A_PAY1);
            nationalite1 = statement.dbReadNumeric(A_NAT1);
        } else { // on va rechercher dans les tiers
            // si le nom est null on prend la valeur qui nous vient des tiers
            nom1 = statement.dbReadString(A_TI_NOM1);
            prenom1 = statement.dbReadString(A_TI_PRENOM1);
            noAvs1 = statement.dbReadString(A_TI_AVS1);
            sexe1 = statement.dbReadNumeric(A_TI_SEXE1);
            dateNaissance1 = statement.dbReadDateAMJ(A_TI_NAI1);
            dateDeces1 = statement.dbReadDateAMJ(A_TI_DEC1);
            canton1 = statement.dbReadNumeric(A_TI_CAN1);
            if (JadeStringUtil.isIntegerEmpty(canton1)) {
                canton1 = statement.dbReadNumeric(A_CAN1);
            }
            pays1 = statement.dbReadNumeric(A_TI_PAY1);
        }
        // Recherche le deuxième membre
        idTiers2 = statement.dbReadNumeric(A_IDTI2);
        if (JadeStringUtil.isIntegerEmpty(idTiers2)) {
            nom2 = statement.dbReadString(A_NOM2);
            prenom2 = statement.dbReadString(A_PRENOM2);
            sexe2 = statement.dbReadNumeric(A_SEXE2);
            dateNaissance2 = statement.dbReadDateAMJ(A_NAI2);
            dateDeces2 = statement.dbReadDateAMJ(A_DEC2);
            canton2 = statement.dbReadNumeric(A_CAN2);
            pays2 = statement.dbReadNumeric(A_PAY2);
            nationalite2 = statement.dbReadNumeric(A_NAT2);
        } else { // on va rechercher dans les tiers
            nom2 = statement.dbReadString(A_TI_NOM2);
            prenom2 = statement.dbReadString(A_TI_PRENOM2);
            noAvs2 = statement.dbReadString(A_TI_AVS2);
            sexe2 = statement.dbReadNumeric(A_TI_SEXE2);
            dateNaissance2 = statement.dbReadDateAMJ(A_TI_NAI2);
            dateDeces2 = statement.dbReadDateAMJ(A_TI_DEC2);
            canton2 = statement.dbReadNumeric(A_TI_CAN2);
            if (JadeStringUtil.isIntegerEmpty(canton2)) {
                canton2 = statement.dbReadNumeric(A_CAN2);
            }
            pays2 = statement.dbReadNumeric(A_TI_PAY2);
        }
    }

    /**
     * @return
     */
    public String getCanton1() {
        return canton1;
    }

    /**
     * @return
     */
    public String getCanton2() {
        return canton2;
    }

    /**
     * Renvoie le noAvs du conjoint du membre
     */
    public String getCantonConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return canton2;
        } else {
            return canton1;
        }
    }

    /**
     * Renvoie le noAvs du membre
     */
    public String getCantonMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return canton2;
        } else {
            return canton1;
        }
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    /**
     * @return
     */
    public String getDateDeces1() {
        return dateDeces1;
    }

    /**
     * @return
     */
    public String getDateDeces2() {
        return dateDeces2;
    }

    /**
     * Renvoie le noAvs du conjoint du membre
     */
    public String getDateDecesConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return dateDeces2;
        } else {
            return dateDeces1;
        }
    }

    /**
     * Renvoie le noAvs du membre
     */
    public String getDateDecesMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return dateDeces2;
        } else {
            return dateDeces1;
        }
    }

    /**
     * Donne la date de fin d'une relation mariage/séparation lors qu'elle s'achève par un décès
     * 
     * @return null en cas d'exception
     */
    public String getDateFinRelation() {
        JACalendarGregorianStandard calendar = new JACalendarGregorianStandard();
        if (JAUtil.isDateEmpty(getDateDeces1()) && JAUtil.isDateEmpty(getDateDeces2())) {
            try {
                if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getDateDeces1(), getDateDeces2())) {
                    return getDateDeces1();
                } else {
                    return getDateDeces2();
                }
            } catch (Exception e) {
                return null;
            }
        } else if (JAUtil.isDateEmpty(getDateDeces1())) {
            return getDateDeces2();
        } else {
            return getDateDeces1();
        }

    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDateNaissance1() {
        return dateNaissance1;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDateNaissance2() {
        return dateNaissance2;
    }

    /**
     * Renvoie le noAvs du conjoint du membre
     */
    public String getDateNaissanceConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return dateNaissance2;
        } else {
            return dateNaissance1;
        }
    }

    /**
     * Renvoie le noAvs du membre
     */
    public String getDateNaissanceMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return dateNaissance2;
        } else {
            return dateNaissance1;
        }
    }

    /**
     * renvoie un itérateur sur les enfants de conjoints null est renvoyé en cas d'erreur
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @return les enfants pour un idConjoints ou null en cas d'erreur
     */
    public Iterator getEnfants(BITransaction transaction) {

        // On est obligé de passer par la table des conjoints
        SFApercuEnfantManager enfantsManager = new SFApercuEnfantManager();

        try {
            enfantsManager.setSession(getSession());
            enfantsManager.setForIdConjoint(getIdConjoints());
            enfantsManager.find(transaction);
        } catch (Exception e) {
            return null;
        }

        return enfantsManager.iterator();
    }

    /**
     * Renvoie la femme du couple, Si aucun conjoit n'a un sexe renseigné, la femme est désignée aléatoirement
     * 
     * @return
     */
    public SFMembreFamille getFemme() {
        // la méthode getHomme fixe les sexe
        // --> on l'appelle pour trouver la femme
        if (femme == null) {
            getHomme();
        }
        return femme;
    }

    /**
     * Renvoie l'homme du couple, Si aucun conjoit n'a un sexe renseigné, l'homme est désigné aléatoirement
     * 
     * @return
     */
    public SFMembreFamille getHomme() {
        if (homme == null) {
            // Si un des sexe est renseigné alors
            if (ITIPersonne.CS_HOMME.equals(sexe1) || ITIPersonne.CS_FEMME.equals(sexe2)) {
                homme = getMembreFamilleMembre(idConjoint1);
                femme = getMembreFamilleMembre(idConjoint2);
            } else if (ITIPersonne.CS_FEMME.equals(sexe1) || ITIPersonne.CS_HOMME.equals(sexe2)) {
                femme = getMembreFamilleMembre(idConjoint1);
                homme = getMembreFamilleMembre(idConjoint2);
            } else {
                // Aléatoirement
                femme = getMembreFamilleMembre(idConjoint1);
                homme = getMembreFamilleMembre(idConjoint2);
            }
        }
        return homme;
    }

    /**
     * Renvoie le l'id (idMembre) du conjoint du membre
     */
    public String getIdConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return idConjoint2;
        } else {
            return idConjoint1;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdConjoint1() {
        return idConjoint1;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdConjoint2() {
        return idConjoint2;
    }

    public String getIdMembreFamilleFemme() {
        return getFemme().getIdMembreFamille();
    }

    public String getIdMembreFamilleHomme() {
        return getHomme().getIdMembreFamille();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdTiers1() {
        return idTiers1;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdTiers2() {
        return idTiers2;
    }

    /**
     * Renvoie l'id tiers du conjoint du membre
     */
    public String getIdTiersConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return idTiers2;
        } else {
            return idTiers1;
        }
    }

    /**
     * Renvoie le noAvs du membre
     */
    public String getIdTiersMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return idTiers2;
        } else {
            return idTiers1;
        }
    }

    public String getLibellePays() {

        String pays = "";

        if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(getPays1())) {
            pays = getPays1();
        } else {
            pays = getPays2();
        }

        return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", pays));
    }

    /**
     * Renvoie le libelle Pays du conjoint du membre
     */
    public String getLibellePaysConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return getSession().getCodeLibelle(pays2);
        } else {
            return getSession().getCodeLibelle(pays1);
        }
    }

    /**
     * Renvoie le libelle Sexe du conjoint du membre
     */
    public String getLibelleSexeConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return getSession().getCodeLibelle(sexe2);
        } else {
            return getSession().getCodeLibelle(sexe1);
        }
    }

    /**
     * Renvoie le membre de la famille qui est conjoint de l'id donné
     * 
     * @param idConjoint
     * @return
     */
    public SFMembreFamille getMembreFamilleConjoint(String idConjoint) {
        if (idConjoint2.equals(idConjoint)) {
            return getMembreFamilleMembre(idConjoint1);
        } else {
            return getMembreFamilleMembre(idConjoint2);
        }
    }

    /**
     * Renvoie le membre de famille donnée par l'id
     * 
     * @param idMembreFamille
     * @return
     */
    public SFMembreFamille getMembreFamilleMembre(String idMembreFamille) {
        SFMembreFamille membre = new SFMembreFamille();
        membre.setSession(getSession());
        if (!JadeStringUtil.isBlankOrZero(idConjoint2) && idConjoint2.equals(idMembreFamille)) {
            membre.setIdMembreFamille(idConjoint2);
            membre.setNom(nom2);
            membre.setPrenom(prenom2);
            membre.setNss(noAvs2);
            membre.setIdTiers(idTiers2);
            membre.setCsCantonDomicile(canton2);
            membre.setCsNationalite(nationalite2);
            membre.setCsSexe(sexe2);
            membre.setDateNaissance(dateNaissance2);
            membre.setDateDeces(dateDeces2);
            if (!JadeStringUtil.isIntegerEmpty(idTiers2)) {
                membre.setProvenance(SFUtil.PROVENANCE_TIERS);
                membre.setIdAssure(idTiers2);
            }
        } else {
            membre.setIdMembreFamille(idConjoint1);
            membre.setNom(nom1);
            membre.setPrenom(prenom1);
            membre.setNss(noAvs1);
            membre.setIdTiers(idTiers1);
            membre.setCsCantonDomicile(canton1);
            membre.setCsNationalite(nationalite1);
            membre.setCsSexe(sexe1);
            membre.setDateNaissance(dateNaissance1);
            membre.setDateDeces(dateDeces1);
            if (!JadeStringUtil.isIntegerEmpty(idTiers1)) {
                membre.setProvenance(SFUtil.PROVENANCE_TIERS);
                membre.setIdAssure(idTiers1);
            }
        }
        return membre;
    }

    /**
     * @return
     */
    public String getNationalite1() {
        return nationalite1;
    }

    /**
     * @return
     */
    public String getNationalite2() {
        return nationalite2;
    }

    /**
     * Renvoie le noAvs du conjoint du membre
     */
    public String getNationaliteConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return nationalite2;
        } else {
            return nationalite1;
        }
    }

    /**
     * Renvoie le noAvs du membre
     */
    public String getNationaliteMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return nationalite2;
        } else {
            return nationalite1;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getNoAvs1() {
        return noAvs1;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getNoAvs2() {
        return noAvs2;
    }

    /**
     * Renvoie le noAvs du conjoint du membre
     */
    public String getNoAvsConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return JAStringFormatter.unFormatAVS(JadeStringUtil.isEmpty(noAvs2) ? "000.00.000.000" : noAvs2, "");
        } else {
            return JAStringFormatter.unFormatAVS(JadeStringUtil.isEmpty(noAvs1) ? "000.00.000.000" : noAvs1, "");
        }
    }

    public String getNoAvsFemme() {
        return getFemme().getNss();
    }

    public String getNoAvsHomme() {
        return getHomme().getNss();
    }

    /**
     * Renvoie le noAvs du membre
     */
    public String getNoAvsMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return noAvs2;
        } else {
            return noAvs1;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getNom1() {
        return nom1;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getNom2() {
        return nom2;
    }

    /**
     * Renvoie le nom du conjoint du membre
     */
    public String getNomConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return nom2;
        } else {
            return nom1;
        }
    }

    public String getNomFemme() {
        return getFemme().getNom();
    }

    public String getNomHomme() {
        return getHomme().getNom();
    }

    /**
     * Renvoie le nom du membre
     */
    public String getNomMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return nom2;
        } else {
            return nom1;
        }
    }

    /**
     * @return
     */
    public String getPays1() {
        return pays1;
    }

    /**
     * @return
     */
    public String getPays2() {
        return pays2;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getPrenom1() {
        return prenom1;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getPrenom2() {
        return prenom2;
    }

    /**
     * Renvoie le prénom du conjoint du membre
     */
    public String getPrenomConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return prenom2;
        } else {
            return prenom1;
        }
    }

    public String getPrenomFemme() {
        return getFemme().getPrenom();
    }

    public String getPrenomHomme() {
        return getHomme().getPrenom();
    }

    /**
     * Renvoie le prénom du membre
     */
    public String getPrenomMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return prenom2;
        } else {
            return prenom1;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getSexe1() {
        return sexe1;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getSexe2() {
        return sexe2;
    }

    /**
     * Renvoie le csSexe du conjoint du membre
     */
    public String getSexeConjoint(String idConjoint) {
        if (idConjoint1.equals(idConjoint)) {
            return sexe2;
        } else {
            return sexe1;
        }
    }

    /**
     * Renvoie le csSexe du membre
     */
    public String getSexeMembre(String idMembre) {
        if (idConjoint2.equals(idMembre)) {
            return sexe2;
        } else {
            return sexe1;
        }
    }

    /**
     * @param string
     */
    public void setCanton1(String string) {
        canton1 = string;
    }

    /**
     * @param string
     */
    public void setCanton2(String string) {
        canton2 = string;
    }

    /**
     * @param string
     */
    public void setDateDeces1(String string) {
        dateDeces1 = string;
    }

    /**
     * @param string
     */
    public void setDateDeces2(String string) {
        dateDeces2 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setDateNaissance1(String string) {
        dateNaissance1 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setDateNaissance2(String string) {
        dateNaissance2 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdConjoint1(String string) {
        idConjoint1 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdConjoint2(String string) {
        idConjoint2 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdTiers1(String string) {
        idTiers1 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdTiers2(String string) {
        idTiers2 = string;
    }

    public void setMembreFamilleConjoint(SFMembreFamille membre) {
        if (idConjoint2.equals(membre.getIdMembreFamille())) {
            setMembreFamilleMembre(membre);
        } else {
            setMembreFamilleMembre(membre);
        }
    }

    public void setMembreFamilleMembre(SFMembreFamille membre) {

        csDomaine = membre.getCsDomaineApplication();
        if (membre.getIdMembreFamille().equals(idConjoint2)) {
            idConjoint2 = membre.getIdMembreFamille();
            nom2 = membre.getNom();
            prenom2 = membre.getPrenom();
            noAvs2 = membre.getNss();
            idTiers2 = membre.getIdTiers();
            canton2 = membre.getCsCantonDomicile();
            nationalite2 = membre.getCsNationalite();
            sexe2 = membre.getCsSexe();
            dateNaissance2 = membre.getDateNaissance();
            dateDeces2 = membre.getDateDeces();
            provenance2 = membre.getProvenance();
            idAssure2 = membre.getIdAssure();

        } else {
            idConjoint1 = membre.getIdMembreFamille();
            nom1 = membre.getNom();
            prenom1 = membre.getPrenom();
            noAvs1 = membre.getNss();
            idTiers1 = membre.getIdTiers();
            canton1 = membre.getCsCantonDomicile();
            nationalite1 = membre.getCsNationalite();
            sexe1 = membre.getCsSexe();
            dateNaissance1 = membre.getDateNaissance();
            dateDeces1 = membre.getDateDeces();
            provenance1 = membre.getProvenance();
            idAssure1 = membre.getIdAssure();
        }
    }

    /**
     * @param string
     */
    public void setNationalite1(String string) {
        nationalite1 = string;
    }

    /**
     * @param string
     */
    public void setNationalite2(String string) {
        nationalite2 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setNoAvs1(String string) {
        noAvs1 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setNoAvs2(String string) {
        noAvs2 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setNom1(String string) {
        nom1 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setNom2(String string) {
        nom2 = string;
    }

    /**
     * @param string
     */
    public void setPays1(String string) {
        pays1 = string;
    }

    /**
     * @param string
     */
    public void setPays2(String string) {
        pays2 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setPrenom1(String string) {
        prenom1 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setPrenom2(String string) {
        prenom2 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setSexe1(String string) {
        sexe1 = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setSexe2(String string) {
        sexe2 = string;
    }

}
