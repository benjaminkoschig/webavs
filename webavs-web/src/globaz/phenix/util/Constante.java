package globaz.phenix.util;

/**
 * Classe de constantes Date de création : (27.05.2003 11:24:22)
 * 
 * @author: ado
 */
public interface Constante {
    public final static String FORMAT_ADRESSE_COURRIER = "formatAdresseCourrier";
    public final static String FORMAT_ADRESSE_LISTE = "formatAdresseListe";
    public final static String FORMAT_ADRESSE_LISTE_RUELOCALITE = "formatAdresseListeRueLocalite";
    // permet d'éviter les références sur bambou
    public final static java.lang.String FWPROCESS_MGS_220 = "Phase envoi des documents";
    // options possibles
    public final static String OPTION_CALCULER = "calculer";
    public final static String OPTION_DEVALIDER = "devalider";
    public final static String OPTION_DUPLICATA = "duplicata";
    public final static String OPTION_IMPRIMER = "imprimer";
    public final static String OPTION_MODIFIER = "modifier";

    public final static String OPTION_SUPPRIMER = "supprimer";
    public final static String OPTION_SUPPRIMERDIRECT = "supprimerDirect";

    // les options à monitorer et éventuellement interdire
    public final static String[] OPTIONS_CHECK = { OPTION_CALCULER, OPTION_IMPRIMER, OPTION_DEVALIDER,
            OPTION_SUPPRIMER, OPTION_MODIFIER, OPTION_DUPLICATA, OPTION_SUPPRIMERDIRECT };
    public final static String USERDETAIL_EMAIL = "EMail";
    // Constante pour le user
    public final static String USERDETAIL_PHONE = "Phone";

}
