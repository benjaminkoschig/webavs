package globaz.helios.db.classifications;

import globaz.globall.db.BConstants;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;

public class CGExtendedCompte extends CGNeedExerciceComptable implements java.io.Serializable, CGLibelleInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Nature d'un compte
    public final static String CS_CENTRE_CHARGE = "700004";
    public final static String CS_COMPTE_ADMINISTRATION = "702002";
    // Domaine d'un compte
    public final static String CS_COMPTE_BILAN = "702001";
    public final static String CS_COMPTE_COLLECTIF = "700002";
    public final static String CS_COMPTE_EXPLOITATION = "702003";
    public final static String CS_COMPTE_INVESTISSEMENT = "702004";
    public final static String CS_COMPTE_TOUS = "702005";
    // Genre d'un compte
    public final static String CS_GENRE_ACTIF = "701001";
    public final static String CS_GENRE_CHARGE = "701005";
    public final static String CS_GENRE_CLOTURE = "701004";
    public final static String CS_GENRE_OUVERTURE = "701003";
    public final static String CS_GENRE_PASSIF = "701002";
    public final static String CS_GENRE_PRODUIT = "701006";

    public final static String CS_GENRE_RESULTAT = "701007";
    public final static String CS_GENRE_TOUS = "701008";
    // Nature d'un compte
    public final static String CS_MONNAIE_ETRANGERE = "700003";
    public final static String CS_STANDARD = "700001"; // Nature d'un compte
    public final static String CS_TVA_CREANCIER = "700006";
    // Nature d'un compte
    // Nature d'un compte
    public final static String CS_TVA_DEBITEUR = "700005"; // Nature d'un compte
    private final static String labelPrefix = "EXTENDED_COMPTE_";

    public static boolean hasValidFormat(String compte, int length) {

        if (compte == null) {
            return false;
        }
        if (compte.length() != length) {
            return false;
        }
        if (compte.charAt(0) == '0') {
            return false;
        }

        try {
            Integer.parseInt(compte);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String codeISOMonnaie = new String();;
    private Boolean estConfidentiel = new Boolean(false);;
    private Boolean estVerrouille = new Boolean(false);

    private String idClasseCompte = new String();
    private String idClassification = new String();
    protected String idCompte = "";
    private String idCompteTVA = new String();
    private String idDomaine = new String();
    private String idExterne;

    private String idGenre = new String();
    private String idMandat = new String();
    private String idNature = CGExtendedCompte.CS_STANDARD;
    private String idParametreBouclement = new String();
    private String idRemarque = "0";
    private String idSecteurAVS = new String();
    private String idSuperClasse = new String();
    private Boolean imprimerResultat = new Boolean(false);

    private Boolean imprimerTitre = new Boolean(false);
    private Boolean imprimerTotal = new Boolean(false);
    private String libelleDe = new String();
    private String libelleDeClasseNiv1;
    private String libelleDeClasseNiv2;

    private String libelleDePlanComptable;

    private String libelleFr = new String();
    private String libelleFrClasseNiv1;
    private String libelleFrClasseNiv2;
    private String libelleFrPlanComptable;
    private String libelleIt = new String();
    private String libelleItClasseNiv1;
    private String libelleItClasseNiv2;
    private String libelleItPlanComptable;
    private String noClasse = new String();
    private String noClasseNiv1;
    // Classes de compte de niveau 1 et 2 associées
    private String noClasseNiv2;
    private String numeroCompteAVS = new String();

    // code systeme

    private String numeroOrdre = new String();

    /**
     * Commentaire relatif au constructeur CGCompte
     */
    public CGExtendedCompte() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGCOMTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        idClasseCompte = statement.dbReadString("IDCLASSECOMPTE");
        noClasseNiv2 = statement.dbReadString("NOCLASSENIV2");
        noClasseNiv1 = statement.dbReadString("NOCLASSENIV1");
        libelleFrClasseNiv2 = statement.dbReadString("LIBFRCLASSENIV2");
        libelleItClasseNiv2 = statement.dbReadString("LIBITCLASSENIV2");
        libelleDeClasseNiv2 = statement.dbReadString("LIBDECLASSENIV2");
        libelleFrClasseNiv1 = statement.dbReadString("LIBFRCLASSENIV1");
        libelleItClasseNiv1 = statement.dbReadString("LIBITCLASSENIV1");
        libelleDeClasseNiv1 = statement.dbReadString("LIBDECLASSENIV1");
        libelleFrPlanComptable = statement.dbReadString("LIBFRPLANCOMPTABLE");
        libelleItPlanComptable = statement.dbReadString("LIBITPLANCOMPTABLE");
        libelleDePlanComptable = statement.dbReadString("LIBDEPLANCOMPTABLE");
        idExterne = statement.dbReadString("IDEXTERNE");
        idGenre = statement.dbReadString("IDGENRE");
        idDomaine = statement.dbReadString("IDDOMAINE");
        idNature = statement.dbReadString("IDNATURE");
        idSecteurAVS = statement.dbReadString("IDSECTEURAVS");
        codeISOMonnaie = statement.dbReadString("CODEISOMONNAIE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        /* idNature - obligatoire */
        if (JadeStringUtil.isBlank(getIdNature())) {

            _addError(statement.getTransaction(), label("CHAMP_NATURE_OBLIGATOIRE"));
        }

        /* idDomaine - obligatoire */
        if (JadeStringUtil.isBlank(getIdDomaine())) {
            _addError(statement.getTransaction(), label("CHAMP_DOMAINE_COMPTE_OBLIGATOIRE"));
        }

        /* idGenre -obligatoire */
        if (JadeStringUtil.isBlank(getIdGenre())) {
            _addError(statement.getTransaction(), label("CHAMP_GENRE_COMPTE_OBLIGATOIRE"));
        }

        /*
         * idCompteTVA - si renseigné le compte doit etre de nature TVA_DEBITEUR ou TVA_CREANCIER
         */
        if (!JadeStringUtil.isIntegerEmpty(idCompteTVA)) {
            if ((!CGCompte.CS_TVA_CREANCIER.equals(idCompteTVA)) || (!CGCompte.CS_TVA_DEBITEUR.equals(idCompteTVA))) {
                _addError(statement.getTransaction(), label("CHAMP_ASS_CPTE_TVA_OBLIGATOIRE"));
            }
        }

        /* codeISOMonnaie -obligatoire */
        // A Faire
        /*
         * if (JadeStringUtil.isBlank(getCodeISOMonnaie())) { String champ = ""; champ = "'Monnaie'";
         * _addError(statement.getTransaction(), "le champ " + champ + "est obligatoire"); }
         */
        /* codeISOMaonnaie - si compte suisse seulement CHF */
        // A Faire

        /* idClassificationPrincipale - voir analyse */
        // A Faire

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDCOMPTE", this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {

        // statement.writeField("IDPARAMETREBOUCL",_dbWriteNumeric(statement.getTransaction(),
        // getIdParametreBouclement(),"idParametreBouclement"));

        statement.writeField("IDCOMPTE", this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField("IDREMARQUE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField("IDCOMPTETVA",
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteTVA(), "idCompteTVA"));
        statement.writeField("IDSECTEURAVS",
                this._dbWriteNumeric(statement.getTransaction(), getIdSecteurAVS(), "idSecteurAVS"));
        statement.writeField("IDMANDAT", this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField("NUMEROCOMPTEAVS",
                this._dbWriteNumeric(statement.getTransaction(), getNumeroCompteAVS(), "numeroCompteAVS"));
        statement.writeField("IDNATURE", this._dbWriteNumeric(statement.getTransaction(), getIdNature(), "idNature"));
        statement
                .writeField("IDDOMAINE", this._dbWriteNumeric(statement.getTransaction(), getIdDomaine(), "idDomaine"));
        statement.writeField("IDGENRE", this._dbWriteNumeric(statement.getTransaction(), getIdGenre(), "idGenre"));
        statement.writeField("CODEISOMONNAIE",
                this._dbWriteString(statement.getTransaction(), getCodeISOMonnaie(), "codeISOMonnaie"));
        statement.writeField("ESTCONFIDENTIEL", this._dbWriteBoolean(statement.getTransaction(), isEstConfidentiel(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estConfidentiel"));
        statement.writeField("ESTVERROUILLE", this._dbWriteBoolean(statement.getTransaction(), isEstVerrouille(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estVerrouille"));
    }

    public String getCodeISOMonnaie() {
        return codeISOMonnaie;
    }

    public String getDomaineLibelle() {
        try {
            return CodeSystem.getLibelle(getSession(), idDomaine);
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    public String getGenreLibelle() {
        try {
            return CodeSystem.getLibelle(getSession(), idGenre);
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    /**
     * Getter
     */
    public String getIdClasseCompte() {
        return idClasseCompte;
    }

    public String getIdClassification() {
        return idClassification;
    }

    /**
     * Getter
     */
    public String getIdCompte() {
        return idCompte;
    }

    public String getIdCompteTVA() {
        return idCompteTVA;
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:25:06)
     * 
     * @return String
     */
    public String getIdExterne() {
        return idExterne;
    }

    public String getIdGenre() {

        return idGenre;
    }

    public String getIdMandat() {
        return idMandat;
    }

    public String getIdNature() {
        return idNature;
    }

    public String getIdParametreBouclement() {
        return idParametreBouclement;
    }

    public String getIdRemarque() {
        return idRemarque;
    }

    public String getIdSecteurAVS() {
        return idSecteurAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 17:21:56)
     * 
     * @return String
     */
    public String getIdSuperClasse() {
        return idSuperClasse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:43:56)
     * 
     * @return Boolean
     */
    public Boolean getImprimerResultat() {
        return imprimerResultat;
    }

    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleApp(this);
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:40:17)
     * 
     * @return String
     */
    public String getLibelleClasseNiv1() {
        String langue = getSession().getIdLangueISO();
        if (langue != null) {
            if ("IT".equalsIgnoreCase(langue)) {
                return getLibelleItClasseNiv1();
            } else if ("DE".equalsIgnoreCase(langue)) {
                return getLibelleDeClasseNiv1();
            } else {
                return getLibelleFrClasseNiv1();
            }
        }
        return "";
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:39:58)
     * 
     * @return String
     */
    public String getLibelleClasseNiv2() {
        String langue = getSession().getIdLangueISO();
        if (langue != null) {
            if ("IT".equalsIgnoreCase(langue)) {
                return getLibelleItClasseNiv2();
            } else if ("DE".equalsIgnoreCase(langue)) {
                return getLibelleDeClasseNiv2();
            } else {
                return getLibelleFrClasseNiv2();
            }
        }
        return "";
    }

    @Override
    public String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:30:12)
     * 
     * @return String
     */
    public String getLibelleDeClasseNiv1() {
        return libelleDeClasseNiv1;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:27:10)
     * 
     * @return String
     */
    public String getLibelleDeClasseNiv2() {
        return libelleDeClasseNiv2;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:26:37)
     * 
     * @return String
     */
    public String getLibelleDePlanComptable() {
        return libelleDePlanComptable;
    }

    @Override
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:29:33)
     * 
     * @return String
     */
    public String getLibelleFrClasseNiv1() {
        return libelleFrClasseNiv1;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:26:24)
     * 
     * @return String
     */
    public String getLibelleFrClasseNiv2() {
        return libelleFrClasseNiv2;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:25:54)
     * 
     * @return String
     */
    public String getLibelleFrPlanComptable() {
        return libelleFrPlanComptable;
    }

    @Override
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:29:52)
     * 
     * @return String
     */
    public String getLibelleItClasseNiv1() {
        return libelleItClasseNiv1;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:26:42)
     * 
     * @return String
     */
    public String getLibelleItClasseNiv2() {
        return libelleItClasseNiv2;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:26:13)
     * 
     * @return String
     */
    public String getLibelleItPlanComptable() {
        return libelleItPlanComptable;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:30:54)
     * 
     * @return String
     */
    public String getLibellePlanComptable() {
        String langue = getSession().getIdLangueISO();
        if (langue != null) {
            if ("IT".equalsIgnoreCase(langue)) {
                return getLibelleItPlanComptable();
            } else if ("DE".equalsIgnoreCase(langue)) {
                return getLibelleDePlanComptable();
            } else {
                return getLibelleFrPlanComptable();
            }
        }
        return "";
    }

    public String getNatureLibelle() {
        try {
            return CodeSystem.getLibelle(getSession(), idNature);
        } catch (Exception e) {
            e.printStackTrace();
            return CGLibelle.LIBELLE_ERROR;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:45:51)
     * 
     * @return String
     */
    public String getNoClasse() {
        return noClasse;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 13:42:35)
     * 
     * @return String
     */
    public String getNoClasseNiv1() {
        return noClasseNiv1;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 13:42:06)
     * 
     * @return String
     */
    public String getNoClasseNiv2() {
        return noClasseNiv2;
    }

    public String getNumeroCompteAVS() {
        return numeroCompteAVS;
    }

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    public String getRemarque() {
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:03)
     * 
     * @return boolean
     */
    public boolean hasCGRemarque() {
        return false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:03)
     * 
     * @return boolean
     */
    public boolean hasRemarque() {
        return false;
    }

    public Boolean isEstConfidentiel() {
        return estConfidentiel;
    }

    public Boolean isEstVerrouille() {
        return estVerrouille;
    }

    public Boolean isImprimerTitre() {
        return imprimerTitre;
    }

    public Boolean isImprimerTotal() {
        return imprimerTotal;
    }

    private String label(String codeLabel) {
        return getSession().getLabel(CGExtendedCompte.labelPrefix + codeLabel);
    }

    public void setCodeISOMonnaie(String newCodeISOMonnaie) {
        codeISOMonnaie = newCodeISOMonnaie;
    }

    public void setEstConfidentiel(Boolean newEstConfidentiel) {
        estConfidentiel = newEstConfidentiel;
    }

    public void setEstVerrouille(Boolean newEstVerrouille) {
        estVerrouille = newEstVerrouille;
    }

    public void setExerciceComptable(CGExerciceComptable exerice) {
        exerciceComptable = exerice;

    }

    /**
     * Setter
     */
    public void setIdClasseCompte(String newIdClasseCompte) {
        idClasseCompte = newIdClasseCompte;
    }

    public void setIdClassification(String newIdClassification) {
        idClassification = newIdClassification;
    }

    /**
     * Setter
     */
    public void setIdCompte(String newIdCompte) {
        idCompte = newIdCompte;
    }

    public void setIdCompteTVA(String newIdCompteTVA) {
        idCompteTVA = newIdCompteTVA;
    }

    public void setIdDomaine(String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:25:06)
     * 
     * @param newIdExterne
     *            String
     */
    public void setIdExterne(String newIdExterne) {
        idExterne = newIdExterne;
    }

    public void setIdGenre(String newIdGenre) {
        idGenre = newIdGenre;
    }

    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    public void setIdNature(String newIdNature) {
        idNature = newIdNature;
    }

    public void setIdParametreBouclement(String newIdParametreBouclement) {
        idParametreBouclement = newIdParametreBouclement;
    }

    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setIdSecteurAVS(String newIdSecteurAVS) {
        idSecteurAVS = newIdSecteurAVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 17:21:56)
     * 
     * @param newIdSuperClasse
     *            String
     */
    public void setIdSuperClasse(String newIdSuperClasse) {
        idSuperClasse = newIdSuperClasse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:43:56)
     * 
     * @param newImprimerResultat
     *            Boolean
     */
    public void setImprimerResultat(Boolean newImprimerResultat) {
        imprimerResultat = newImprimerResultat;
    }

    public void setImprimerTitre(Boolean newImprimerTitre) {
        imprimerTitre = newImprimerTitre;
    }

    public void setImprimerTotal(Boolean newImprimerTotal) {
        imprimerTotal = newImprimerTotal;
    }

    public void setLibelleDe(String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:30:12)
     * 
     * @param newLibelleDeClasseNiv1
     *            String
     */
    public void setLibelleDeClasseNiv1(String newLibelleDeClasseNiv1) {
        libelleDeClasseNiv1 = newLibelleDeClasseNiv1;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:27:10)
     * 
     * @param newLibelleDeClasseNiv2
     *            String
     */
    public void setLibelleDeClasseNiv2(String newLibelleDeClasseNiv2) {
        libelleDeClasseNiv2 = newLibelleDeClasseNiv2;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:26:37)
     * 
     * @param newLibelleDePlanComptable
     *            String
     */
    public void setLibelleDePlanComptable(String newLibelleDePlanComptable) {
        libelleDePlanComptable = newLibelleDePlanComptable;
    }

    public void setLibelleFr(String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:29:33)
     * 
     * @param newLibelleFrClasseNiv1
     *            String
     */
    public void setLibelleFrClasseNiv1(String newLibelleFrClasseNiv1) {
        libelleFrClasseNiv1 = newLibelleFrClasseNiv1;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:26:24)
     * 
     * @param newLibelleFrClasseNiv2
     *            String
     */
    public void setLibelleFrClasseNiv2(String newLibelleFrClasseNiv2) {
        libelleFrClasseNiv2 = newLibelleFrClasseNiv2;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:25:54)
     * 
     * @param newLibelleFrPlanComptable
     *            String
     */
    public void setLibelleFrPlanComptable(String newLibelleFrPlanComptable) {
        libelleFrPlanComptable = newLibelleFrPlanComptable;
    }

    public void setLibelleIt(String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:29:52)
     * 
     * @param newLibelleItClasseNiv1
     *            String
     */
    public void setLibelleItClasseNiv1(String newLibelleItClasseNiv1) {
        libelleItClasseNiv1 = newLibelleItClasseNiv1;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 14:26:42)
     * 
     * @param newLibelleItClasseNiv2
     *            String
     */
    public void setLibelleItClasseNiv2(String newLibelleItClasseNiv2) {
        libelleItClasseNiv2 = newLibelleItClasseNiv2;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 15:26:13)
     * 
     * @param newLibelleItPlanComptable
     *            String
     */
    public void setLibelleItPlanComptable(String newLibelleItPlanComptable) {
        libelleItPlanComptable = newLibelleItPlanComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:45:51)
     * 
     * @param newNoClasse
     *            String
     */
    public void setNoClasse(String newNoClasse) {
        noClasse = newNoClasse;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 13:42:35)
     * 
     * @param newNoClasseNiv1
     *            String
     */
    public void setNoClasseNiv1(String newNoClasseNiv1) {
        noClasseNiv1 = newNoClasseNiv1;
    }

    /**
     * Insert the method's description here. Creation date: (01.07.2003 13:42:06)
     * 
     * @param newNoClasseNiv2
     *            String
     */
    public void setNoClasseNiv2(String newNoClasseNiv2) {
        noClasseNiv2 = newNoClasseNiv2;
    }

    public void setNumeroCompteAVS(String newNumeroCompteAVS) {
        numeroCompteAVS = newNumeroCompteAVS;
    }

    public void setNumeroOrdre(String newNumeroOrdre) {
        numeroOrdre = newNumeroOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 09:35:31)
     * 
     * @return globaz.helios.db.comptes.CGRemarque
     */
    public void updateRemarque(globaz.globall.db.BTransaction transaction) throws Exception {
    }
}
