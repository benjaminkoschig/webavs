package ch.globaz.al.business.constantes;

/**
 * Codes système liés aux tiers
 * 
 * @author GMO
 * 
 */
public interface ALCSTiers {
    public static final String DOMAINE_AF = "61410001";
    /**
     * CS : groupe des CS monnaie
     */
    public static final String GROUP_MONNAIE = "10500010";
    // FIXME il faudra encore ajouter les monnaies: zloty Pologne, couronne
    // estonienn Estonie, lats Lettonie, litas Lituanie, forint HONGRIE, lau
    // Roumanie, lev Bulgarie
    /**
     * Groupe 'sexe'
     * 
     * @see ALCSTiers#SEXE_FEMME
     * @see ALCSTiers#SEXE_HOMME
     */
    public static final String GROUP_SEXE = "10500016";
    /**
     * CS: langue allemand
     */
    public static final String LANGUE_ALLEMAND = "503002";
    /**
     * CS : langue francais
     */
    public static final String LANGUE_FRANCAIS = "503001";

    /**
     * CS: langue italien
     */
    public static final String LANGUE_ITALIEN = "503004";

    /**
     * CS: monnaie du décompte : couronne danoise
     */
    public static final String MONNAIE_DANOISE = "510044";

    /**
     * CS : monnaie du décompte : euro
     */
    public static final String MONNAIE_EURO = "510003";

    /**
     * CS : monnaie du décompte: livre sterling;
     */
    public static final String MONNAIE_GB = "510057";
    /**
     * CS : monnaie du décompte: couronne norvégienne
     */
    public static final String MONNAIE_NORVE = "510119";
    /**
     * CS: monnaie du décompte: couronne suédoise
     */
    public static final String MONNAIE_SUEDE = "510139";
    /**
     * CS: monnaie du décompte: couronne tchèque
     */
    public static final String MONNAIE_TCHEQUE = "510041";
    /**
     * CS : Rôle du tiers : allocations familiales
     */
    public static final String ROLE_AF = "517036";
    /**
     * CS : sexe du tiers : Femme
     */
    public static final String SEXE_FEMME = "516002";
    /**
     * CS : sexe du tiers : Homme
     */
    public static final String SEXE_HOMME = "516001";
    /**
     * CS: tiers liaison agence communale
     */
    public static final String TYPE_LIAISON_AG_COMMUNALE = "507007";

}
