package ch.globaz.prestation.domaine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.corvus.domaine.constantes.DegreImpotenceAPI;
import ch.globaz.corvus.domaine.constantes.RevisionLoiAVS;
import ch.globaz.corvus.domaine.constantes.TypeBeneficiaireRenteAVS;
import ch.globaz.corvus.domaine.constantes.TypeRenteAPI;
import ch.globaz.corvus.domaine.constantes.TypeRenteAVS;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;
import ch.globaz.prestation.domaine.constantes.TypePrestationComplementaire;

/**
 * <p>
 * Repr�sentation d'un code prestation
 * </p>
 */
public enum CodePrestation {

    // @formatter:off
    CODE_10(10,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_11(11,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_12(12,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.CONJOINT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_13(13,
            DomaineCodePrestation.SURVIVANT,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_14(14,
            DomaineCodePrestation.SURVIVANT,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_15(15,
            DomaineCodePrestation.SURVIVANT,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_16(16,
            DomaineCodePrestation.SURVIVANT,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_20(20,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_21(21,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_22(22,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.CONJOINT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_23(23,
            DomaineCodePrestation.SURVIVANT,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_24(24,
            DomaineCodePrestation.SURVIVANT,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_25(25,
            DomaineCodePrestation.SURVIVANT,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_26(26,
            DomaineCodePrestation.SURVIVANT,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_33(33,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.CONJOINT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_34(34,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_35(35,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_36(36,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_43(43,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.CONJOINT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_44(44,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_45(45,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_46(46,
            DomaineCodePrestation.VIEILLESSE,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_50(50,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_51(51,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_52(52,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.CONJOINT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_53(53,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.CONJOINT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_54(54,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_55(55,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_56(56,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.ORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_70(70,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_71(71,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ASSURE,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_72(72,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.CONJOINT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_73(73,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.CONJOINT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_74(74,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_75(75,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_76(76,
            DomaineCodePrestation.AI,
            TypeBeneficiaireRenteAVS.ENFANT,
            TypeRenteAVS.EXTRAORDINAIRE,
            RevisionLoiAVS.NEUVIEME_REVISION),
    CODE_81(81,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AI,
            DegreImpotenceAPI.FAIBLE,
            false,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_82(82,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AI,
            DegreImpotenceAPI.MOYEN,
            false,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_83(83,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AI,
            DegreImpotenceAPI.GRAVE,
            false,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_84(84,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AI,
            DegreImpotenceAPI.FAIBLE,
            true,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_85(85,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AVS,
            DegreImpotenceAPI.FAIBLE,
            false,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_86(86,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AVS,
            DegreImpotenceAPI.MOYEN,
            false,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_87(87,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AVS,
            DegreImpotenceAPI.GRAVE,
            false,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_88(88,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AI,
            DegreImpotenceAPI.MOYEN,
            true,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_89(89,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AVS,
            DegreImpotenceAPI.FAIBLE,
            true,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_91(91,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AI,
            DegreImpotenceAPI.FAIBLE,
            false,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_92(92,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AI,
            DegreImpotenceAPI.MOYEN,
            false,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_93(93,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AI,
            DegreImpotenceAPI.GRAVE,
            false,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_94(94,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AVS,
            DegreImpotenceAPI.FAIBLE,
            false,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_95(95,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AVS,
            DegreImpotenceAPI.FAIBLE,
            false,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_96(96,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AVS,
            DegreImpotenceAPI.MOYEN,
            false,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_97(97,
            DomaineCodePrestation.API,
            TypeRenteAPI.API_AVS,
            DegreImpotenceAPI.GRAVE,
            false,
            RevisionLoiAVS.NEUVIEME_REVISION,
            RevisionLoiAVS.DIXIEME_REVISION),
    CODE_110(110,
             DomaineCodePrestation.VIEILLESSE,
             TypePrestationComplementaire.STANDARD),
    CODE_113(113,
            DomaineCodePrestation.SURVIVANT,
             TypePrestationComplementaire.STANDARD),
    CODE_118(118,
             DomaineCodePrestation.VIEILLESSE,
             TypePrestationComplementaire.ALLOCATION_DE_NOEL),
    CODE_119(119,
             DomaineCodePrestation.VIEILLESSE,
             TypePrestationComplementaire.ALLOCATION_DE_NOEL),
    CODE_150(150,
             DomaineCodePrestation.AI,
             TypePrestationComplementaire.STANDARD),
    CODE_158(158,
            DomaineCodePrestation.AI,
             TypePrestationComplementaire.ALLOCATION_DE_NOEL),
    CODE_159(159,
             DomaineCodePrestation.AI,
             TypePrestationComplementaire.ALLOCATION_DE_NOEL),
    CODE_210(210,
             DomaineCodePrestation.VIEILLESSE),
    CODE_213(213,
             DomaineCodePrestation.SURVIVANT),
    CODE_250(250,
             DomaineCodePrestation.AI),
    INCONNU();
    // @formatter:on

    private static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 
     * Retourne tous les codePrestation de type API
     * 
     * @return Iterator<CodePrestation>
     */
    public static Iterator<CodePrestation> getCodesPrestationApiIterator() {

        List<CodePrestation> list = new ArrayList<CodePrestation>();
        for (CodePrestation codePrestationCourant : CodePrestation.values()) {
            if (codePrestationCourant.isAPI()) {
                list.add(codePrestationCourant);
            }
        }

        return list.iterator();
    }

    /**
     * <p>
     * Recherche et retourne un code prestation correspondant au num�ro de code prestation pass� en param�tre.<br/>
     * Si le code est incorrect, lance une {@link IllegalArgumentException}
     * </p>
     * 
     * @param codePrestation
     *            un entier repr�sentant un code prestation
     * @return le code prestation avec toutes ses caract�ristiques
     */
    public static CodePrestation getCodePrestation(int codePrestation) {
        for (CodePrestation unCodePrestation : CodePrestation.values()) {
            if ((unCodePrestation.getCodePrestation() == codePrestation) && (INCONNU != unCodePrestation)) {
                return unCodePrestation;
            }
        }

        throw new IllegalArgumentException("[codePrestation:" + codePrestation + "] doesn't match any known value");
    }

    /**
     * <p>
     * Parse et retourne le code prestation correspondant � la cha�ne de caract�res pass�e en param�tre.
     * </p>
     * <p>
     * Si la cha�ne est null, lance une {@link NullPointerException}<br />
     * Si la cha�ne est vide, retourne {@link #INCONNU}<br />
     * Si la cha�ne n'est pas fait uniquement de nombre, lance une {@link IllegalArgumentException}<br />
     * Si la cha�ne de correspond pas � un code connu, lance une {@link IllegalArgumentException}
     * </p>
     * 
     * @param codePrestation une cha�ne de caract�res repr�sentant un code prestation
     * @return le code prestation correspondant � la cha�ne de caract�res pass�e en param�tre
     * @throws NullPointerException si la cha�ne de caract�res pass�e en param�tre est null
     * @throws IllegalArgumentException si la cha�ne de caract�res pass�e en param�tre n'est pas valide
     */
    public static CodePrestation parse(String codePrestation) {
        Checkers.checkNotNull(codePrestation, "codePrestation");

        if ("".equals(codePrestation)) {
            return INCONNU;
        }

        try {
            return getCodePrestation(Integer.parseInt(codePrestation));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("[codePrestation:" + codePrestation
                    + "] doesn't have the good format (only digits)");
        }
    }

    private int codePrestation;
    private DomaineCodePrestation domaineCodePrestation;

    /** Pour les rentes AVS */
    private Set<RevisionLoiAVS> revisions;
    /** Pour les rentes AVS */
    private TypeBeneficiaireRenteAVS typeBeneficiaireRenteAVS;
    /** Pour les rentes AVS */
    private TypeRenteAVS typeRenteAVS;

    /** Pour les rentes API */
    private boolean avecAccompagnement;
    /** Pour les rentes API */
    private DegreImpotenceAPI degreImpotenceAPI;
    /** Pour les rentes API */
    private TypeRenteAPI typeRenteAPI;

    /** Pour les PC */
    private TypePrestationComplementaire typePC;

    /** Pour les PC et RFM */
    private DomaineCodePrestation domaineComplementaire;

    /**
     * Constructeur "package friendly" afin de construire un code prestation non d�fini, invalide, mais qui n'aura aucun
     * attribut � <code>null</code>
     */
    CodePrestation() {
        codePrestation = 0;
        domaineCodePrestation = DomaineCodePrestation.NON_DEFINI;

        revisions = new HashSet<RevisionLoiAVS>();
        typeBeneficiaireRenteAVS = TypeBeneficiaireRenteAVS.NON_DEFINI;
        typeRenteAVS = TypeRenteAVS.NON_DEFINI;

        avecAccompagnement = false;
        degreImpotenceAPI = DegreImpotenceAPI.NON_DEFINI;
        typeRenteAPI = TypeRenteAPI.NON_DEFINI;

        typePC = TypePrestationComplementaire.NON_DEFINI;

        domaineComplementaire = DomaineCodePrestation.NON_DEFINI;
    }

    /**
     * Constructeur pour les rentes AVS
     * 
     * @param codePrestation le code prestation
     * @param domaineCodePrestation le domaine de cette rente
     * @param typeBeneficiaireRenteAVS qui re�oit cette rente
     * @param typeRenteAVS le type de rente AVS
     * @param revisions les r�visions dans lesquelles la loi est valide
     */
    CodePrestation(int codePrestation, DomaineCodePrestation domaineCodePrestation,
            TypeBeneficiaireRenteAVS typeBeneficiaireRenteAVS, TypeRenteAVS typeRenteAVS, RevisionLoiAVS... revisions) {
        this();

        this.codePrestation = codePrestation;
        this.domaineCodePrestation = domaineCodePrestation;
        this.revisions = new HashSet<RevisionLoiAVS>();
        for (RevisionLoiAVS uneRevision : revisions) {
            this.revisions.add(uneRevision);
        }
        this.typeBeneficiaireRenteAVS = typeBeneficiaireRenteAVS;
        this.typeRenteAVS = typeRenteAVS;
    }

    /**
     * Constructeur pour les rentes API
     * 
     * @param codePrestation le code prestation
     * @param domaineCodePrestation le domaine de cette rente
     * @param typeBeneficiaireRenteAVS qui re�oit cette rente
     * @param typeRenteAVS le type de rente AVS
     * @param revisions les r�visions dans lesquelles la loi est valide
     */
    CodePrestation(int codePrestation, DomaineCodePrestation typeRente, TypeRenteAPI typeRenteAPI,
            DegreImpotenceAPI degreImpotenceAPI, boolean avecAccompagnement, RevisionLoiAVS... revisions) {
        this();

        this.avecAccompagnement = avecAccompagnement;
        this.codePrestation = codePrestation;
        this.degreImpotenceAPI = degreImpotenceAPI;
        domaineCodePrestation = DomaineCodePrestation.API;
        this.revisions = new HashSet<RevisionLoiAVS>();
        for (RevisionLoiAVS uneRevision : revisions) {
            this.revisions.add(uneRevision);
        }
        typeBeneficiaireRenteAVS = TypeBeneficiaireRenteAVS.ASSURE;
        this.typeRenteAPI = typeRenteAPI;
    }

    /**
     * Constructeur pour les PC
     * 
     * @param codePrestation
     * @param typePC
     */
    CodePrestation(int codePrestation, DomaineCodePrestation domaineComplementaire, TypePrestationComplementaire typePC) {
        this();

        this.codePrestation = codePrestation;
        this.domaineComplementaire = domaineComplementaire;
        domaineCodePrestation = DomaineCodePrestation.PRESTATION_COMPLEMENTAIRE;
        this.typePC = typePC;
    }

    /**
     * Constructeur pour les RFM
     * 
     * @param codePrestation
     */
    CodePrestation(int codePrestation, DomaineCodePrestation domaineComplementaire) {
        this();

        this.codePrestation = codePrestation;
        this.domaineComplementaire = domaineComplementaire;
        domaineCodePrestation = DomaineCodePrestation.REMBOURSEMENT_FRAIS_MEDICAUX;
    }

    /**
     * @return le code prestation sous la forme d'un entier
     */
    public int getCodePrestation() {
        return codePrestation;
    }

    /**
     * @return le degr� d'impotence API. Retournera {@link DegreImpotenceAPI#NON_DEFINI} si c'est une rente AVS.
     */
    public DegreImpotenceAPI getDegreImpotenceAPI() {
        return degreImpotenceAPI;
    }

    /**
     * @return le type de cette rente (API, AI, Survivant ou vieillesse)
     */
    public DomaineCodePrestation getDomaineCodePrestation() {
        return domaineCodePrestation;
    }

    /**
     * @return le domaine compl�mentaire
     */
    public DomaineCodePrestation getDomaineComplementaire() {
        return domaineComplementaire;
    }

    /**
     * @return les r�visions dans lesquelles ce code prestation est valide
     */
    public Set<RevisionLoiAVS> getRevisions() {
        return Collections.unmodifiableSet(revisions);
    }

    /**
     * @return le situation du b�n�ficiaire vis � vis de la famille du requ�rant
     */
    public TypeBeneficiaireRenteAVS getTypeBeneficiaireRenteAVS() {
        return typeBeneficiaireRenteAVS;
    }

    /**
     * @return le type de prestation compl�mentaire que repr�sente ce code prestation
     */
    public TypePrestationComplementaire getTypePC() {
        return typePC;
    }

    /**
     * @return le type de rente API (sur quelle type de rente AVS est bas�e cette API). Retournera
     *         {@link TypeRenteAPI#NON_DEFINI} si c'est une rente AVS.
     */
    public TypeRenteAPI getTypeRenteAPI() {
        return typeRenteAPI;
    }

    /**
     * N'est valable que pour les rentes AVS (pas les API)
     * 
     * @return le type de rente AVS (ordinaire ou extraordinaire). Si rente API, retournera
     *         {@link TypeRenteAVS#NON_DEFINI}
     */
    public TypeRenteAVS getTypeRenteAVS() {
        return typeRenteAVS;
    }

    /**
     * @return vrai si ce code prestation correspond � une rente AI
     */
    public boolean isAI() {
        return DomaineCodePrestation.AI.equals(domaineCodePrestation);
    }

    /**
     * @return vrai si ce code prestation correspond � une rente API
     */
    public boolean isAPI() {
        return DomaineCodePrestation.API.equals(domaineCodePrestation);
    }

    /**
     * @return vrai si ce code prestation correspond � une API d�coulant d'un droit AI
     */
    public boolean isAPIAI() {
        return DomaineCodePrestation.API.equals(domaineCodePrestation) && TypeRenteAPI.API_AI.equals(typeRenteAPI);
    }

    /**
     * @return vrai si ce code prestation correspond � une API d�coulant d'un droit AVS
     */
    public boolean isAPIAVS() {
        return DomaineCodePrestation.API.equals(domaineCodePrestation) && TypeRenteAPI.API_AVS.equals(typeRenteAPI);
    }

    /**
     * Uniquement pour les API
     * 
     * @return vrai si cette API comprend de l'accompagnement
     */
    public boolean isAvecAccompagnement() {
        return avecAccompagnement;
    }

    /**
     * @param revision
     *            une r�vision dont on aimerait savoir si ce code prestation fait parti
     * @return vrai si ce code prestation fait parti de la r�vision pass�e en param�tre
     */
    public boolean isDansRevision(RevisionLoiAVS revision) {
        CodePrestation.checkNotNull(revision, "[revision] can't be null");
        return revisions.contains(revision);
    }

    /**
     * @return vrai si le code prestation correspond � une rente compl�mentaire pour enfant ou conjoint
     */
    public boolean isRenteComplementaire() {
        return isRenteComplementairePourConjoint() || isRenteComplementairePourEnfant();
    }

    /**
     * @return vrai si le code prestation correspond � une rente compl�mentaire pour conjoint
     */
    public boolean isRenteComplementairePourConjoint() {
        return TypeBeneficiaireRenteAVS.CONJOINT.equals(typeBeneficiaireRenteAVS);
    }

    /**
     * @return vrai si ce code prestation correspond � une rente compl�mentaire pour enfant
     */
    public boolean isRenteComplementairePourEnfant() {
        return TypeBeneficiaireRenteAVS.ENFANT.equals(typeBeneficiaireRenteAVS);
    }

    /**
     * @return vrai si ce code prestation correspond � une rente AVS extraordinaire
     */
    public boolean isRenteExtraordinaire() {
        return TypeRenteAVS.EXTRAORDINAIRE.equals(typeRenteAVS);
    }

    /**
     * @return vrai si ce code prestation correspond � une rente AVS ordinaire
     */
    public boolean isRenteOrdinaire() {
        return TypeRenteAVS.ORDINAIRE.equals(typeRenteAVS);
    }

    /**
     * @return vrai si ce code prestation correspond � une rente principale (et n'est pas une API)
     */
    public boolean isRentePrincipale() {
        return TypeBeneficiaireRenteAVS.ASSURE.equals(typeBeneficiaireRenteAVS) && !isAPI();
    }

    /**
     * @return vrai si le code prestation correspond � une rente compl�mentaire pour enfant, d�coulant d'une rente
     *         principale accord�e � la m�re
     */
    public boolean isRentesComplementairePourEnfantsLieesRenteDeLaMere() {
        switch (this) {
            case CODE_15:
            case CODE_16:
            case CODE_25:
            case CODE_26:
            case CODE_35:
            case CODE_36:
            case CODE_45:
            case CODE_46:
            case CODE_55:
            case CODE_56:
            case CODE_75:
            case CODE_76:
                return true;
            default:
                return false;
        }
    }

    /**
     * @return vrai si le code prestation correspond � une rente compl�mentaire pour enfant, d�coulant d'une rente
     *         principale accord�e au p�re
     */
    public boolean isRentesComplementairePourEnfantsLieesRenteDuPere() {
        switch (this) {
            case CODE_14:
            case CODE_16:
            case CODE_24:
            case CODE_26:
            case CODE_34:
            case CODE_36:
            case CODE_44:
            case CODE_46:
            case CODE_54:
            case CODE_56:
            case CODE_74:
            case CODE_76:
                return true;
            default:
                return false;
        }
    }

    /**
     * @return vrai si ce code prestation correspond � une rente de survivant
     */
    public boolean isSurvivant() {
        return DomaineCodePrestation.SURVIVANT.equals(domaineCodePrestation);
    }

    /**
     * @return vrai si ce code prestation correspond � une rente de vieillesse
     */
    public boolean isVieillesse() {
        return DomaineCodePrestation.VIEILLESSE.equals(domaineCodePrestation);
    }

    @Override
    public String toString() {
        return "" + codePrestation;
    }

    /**
     * @return vrai si ce code prestation correspond � une prestation compl�mentaire
     */
    public boolean isPC() {
        return DomaineCodePrestation.PRESTATION_COMPLEMENTAIRE.equals(domaineCodePrestation);
    }

    /**
     * @return vrai si ce code prestation correspond � une prestation compl�mentaire de type allocation de No�l
     *         (sp�cifique au canton de Vaud)
     */
    public boolean isPCAllocationNoel() {
        return TypePrestationComplementaire.ALLOCATION_DE_NOEL.equals(typePC);
    }

    /**
     * @return vrai si ce code prestation correspond � un remboursement de frais m�dicaux
     */
    public boolean isRFM() {
        return DomaineCodePrestation.REMBOURSEMENT_FRAIS_MEDICAUX.equals(domaineCodePrestation);
    }

    /**
     * @return vrai si ce code prestation repr�sente une prestation compl�mentaire � une rente AI
     */
    public boolean isPrestationComplementaireAUneRenteAI() {
        return DomaineCodePrestation.AI.equals(domaineComplementaire);
    }

    /**
     * @return vrai si ce code prestation repr�sente une prestation compl�mentaire � une rente AVS
     */
    public boolean isPrestationComplementaireAUneRenteAVS() {
        return DomaineCodePrestation.SURVIVANT.equals(domaineComplementaire)
                || DomaineCodePrestation.VIEILLESSE.equals(domaineComplementaire);
    }

    /**
     * <p>
     * Permet de d�finir si le code prestation pass� en param�tre fait parti du m�me groupe de prestation (AVS/AI, API,
     * PC ou RFM), et dans le cas d'une rente compl�mentaire pour enfant si le donneur de droit est le m�me, afin de
     * determiner si la rente doit �tre prise en comte pour diff�rent traitement m�tier (calcul de rente vers�e � tort,
     * d�finition des rentes � diminuer lors de la validation d'une demande de rente, etc...)
     * </p>
     * 
     * @param unAutreCodePrestation un autre code prestation
     * @return vrai si le code prestation pass� en param�tre est de la m�me famille
     * @throws NullPointerException si le code prestation pass� en param�tre est null
     */
    public boolean estDeLaMemeFamilleDePrestationQue(CodePrestation unAutreCodePrestation) {
        Checkers.checkNotNull(unAutreCodePrestation, "unAutreCodePrestation");
        boolean ceCodeEstAPI = isAPI();
        boolean unAutreCodeEstAPI = unAutreCodePrestation.isAPI();

        if (ceCodeEstAPI || unAutreCodeEstAPI) {
            return ceCodeEstAPI && unAutreCodeEstAPI;
        } else {

            boolean cetteRenteEstUneComplementairePourEnfant = isRenteComplementairePourEnfant();
            boolean uneAutreRenteEstUneComplementairePourEnfant = unAutreCodePrestation
                    .isRenteComplementairePourEnfant();

            if (cetteRenteEstUneComplementairePourEnfant && uneAutreRenteEstUneComplementairePourEnfant) {
                /*
                 * dans le cas de compl�mentaires pour enfants, seules les rentes issues du m�me droit (p�re ou
                 * m�re) sont � prendre en compte.
                 * On v�rifie donc que cette rente, et l'autre rente, sont soit li�es les deux � la rente du p�re,
                 * ou � les deux � la rente de la m�re. Sinon on ne les prends pas en compte pour �viter les calculs
                 * � double de rente vers�es � tort que les utilisateurs devront de tout fa�on supprimer � la main
                 * par la suite pour que la d�cision soit correcte.
                 */
                return (isRentesComplementairePourEnfantsLieesRenteDeLaMere() && unAutreCodePrestation
                        .isRentesComplementairePourEnfantsLieesRenteDeLaMere())
                        || (isRentesComplementairePourEnfantsLieesRenteDuPere() && unAutreCodePrestation
                                .isRentesComplementairePourEnfantsLieesRenteDuPere());
            } else {

                /*
                 * Il faut ici v�rifier que les deux codes sont du m�me domaine (PC, RFM ou AVS/AI)
                 */
                switch (domaineCodePrestation) {
                    case AI:
                    case VIEILLESSE:
                    case SURVIVANT:

                        /*
                         * cas particulier pour l'AVS/AI, si les codes font parti d'un des trois domaines de l'AVS/AI
                         * (pas n�cessairement le m�me) ce sont des codes de la m�me famille
                         */
                        return DomaineCodePrestation.AI.equals(unAutreCodePrestation.domaineCodePrestation)
                                || DomaineCodePrestation.VIEILLESSE.equals(unAutreCodePrestation.domaineCodePrestation)
                                || DomaineCodePrestation.SURVIVANT.equals(unAutreCodePrestation.domaineCodePrestation);

                    default:
                        return domaineCodePrestation.equals(unAutreCodePrestation.domaineCodePrestation);
                }
            }
        }
    }
}
