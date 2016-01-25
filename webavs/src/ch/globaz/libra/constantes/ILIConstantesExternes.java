package ch.globaz.libra.constantes;

/**
 * @author hpe
 */
public interface ILIConstantesExternes {

    // DOMAINES
    // -----------------------------------------------------------------------------------------------------------------

    public static final String CS_ACTION_FORMULE = "LIACTION";
    public static final String CS_ACTION_FORMULE_GRP = "62000005";
    public static final String CS_ACTION_MAJ_DOSSIER_PC = "62500002";
    public static final int CS_ACTION_MAJ_DOSSIER_PC_INT = 62500002;

    public static final String CS_ACTION_REC_STANDARD = "62500001";
    public static final int CS_ACTION_REC_STANDARD_INT = 62500001;
    public static final String CS_AFF_PRETS = "62100002";
    public static final String CS_AFF_SUSPENS = "62100001";
    public static final String CS_AFF_TOUS = "62100003";

    // DOSSIERS
    // -----------------------------------------------------------------------------------------------------------------

    public static final String CS_DOMAINE_AF = "62300004";
    public static final String CS_DOMAINE_AMAL = "62300005";
    public static final String CS_DOMAINE_PC = "62300001";
    public static final String CS_DOMAINE_RENTES = "62300003";
    public static final String CS_DOMAINE_RFM = "62300002";

    public static final String CS_ETAT_CLOS = "62200002";
    // ETAT DOSSIERS
    public static final String CS_ETAT_DOSSIERS = "LIETATDOS";
    public static final String CS_ETAT_OUVERT = "62200001";

    // FORMULES
    // -----------------------------------------------------------------------------------------------------------------

    // TYPE AFFICHAGE
    public static final String CS_TYPE_AFFICHAGE = "LITYPAFFI";
    public static final String CS_TYPE_FORM_COTISATIONS = "62400001";
    public static final String CS_TYPE_FORM_PRESTATIONS = "62400002";

    public static final String CS_TYPE_FORMULE = "LITYPES";
    public static final String DOMAINE_AF = "al";
    public static final String DOMAINE_AMAL = "amal";
    // Domaines existants dans LIBRA
    public static final String DOMAINE_PC = "pegasus";
    public static final String DOMAINE_RENTES = "corvus";

    public static final String DOMAINE_RFM = "cygnus";
    // CODES SYSTEMES POUR DOMAINE
    public static final String GROUPE_DOMAINES = "LIDOMAINES";

    // JOURNALISATIONS - ECHEANCES
    // ----------------------------------------------------------------------------------------------

    // TYPE JOURNALISATIONS
    // ==> VOIR JOCONSTANTES !!
    public static final String JOURNALISATIONS_TYPE = "JOFMTJOUR";

    public static final String REF_PRO_DOSSIER = "41000002";
    // CONSTANTES POUR TYPE DE REFERENCE PROVENANCE
    public static final String REF_PRO_TIERS = "41000001";

}
