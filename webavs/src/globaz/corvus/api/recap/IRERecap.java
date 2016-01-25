/*
 * Créé le 17 juil. 07
 */
package globaz.corvus.api.recap;

/**
 * @author SCR
 */
public interface IRERecap {

    public final static int CODE_AUGMENTATION_BISANNUELLE_AI_API = 513003;
    public final static int CODE_AUGMENTATION_BISANNUELLE_AI_REO = 511003;
    public final static int CODE_AUGMENTATION_BISANNUELLE_AI_RO = 510003;
    public final static int CODE_AUGMENTATION_BISANNUELLE_AVS_API = 503003;
    public final static int CODE_AUGMENTATION_BISANNUELLE_AVS_REO = 501003;
    public final static int CODE_AUGMENTATION_BISANNUELLE_AVS_RO = 500003;

    public final static int CODE_AUGMENTATION_MOIS_RAPPORT_AI_API = 513002;
    public final static int CODE_AUGMENTATION_MOIS_RAPPORT_AI_REO = 511002;
    public final static int CODE_AUGMENTATION_MOIS_RAPPORT_AI_RO = 510002;
    public final static int CODE_AUGMENTATION_MOIS_RAPPORT_AVS_API = 503002;
    public final static int CODE_AUGMENTATION_MOIS_RAPPORT_AVS_REO = 501002;
    public final static int CODE_AUGMENTATION_MOIS_RAPPORT_AVS_RO = 500002;

    public final static int CODE_DIMINUTION_MOIS_PRECEDENT_AI_API = 513004;
    public final static int CODE_DIMINUTION_MOIS_PRECEDENT_AI_REO = 511004;
    public final static int CODE_DIMINUTION_MOIS_PRECEDENT_AI_RO = 510004;
    public final static int CODE_DIMINUTION_MOIS_PRECEDENT_AVS_API = 503004;
    public final static int CODE_DIMINUTION_MOIS_PRECEDENT_AVS_REO = 501004;
    public final static int CODE_DIMINUTION_MOIS_PRECEDENT_AVS_RO = 500004;

    public final static int CODE_EXTOURNE_2002115_AI_API = 513007;
    public final static int CODE_EXTOURNE_2002115_AI_REO = 511007;
    public final static int CODE_EXTOURNE_2002115_AI_RO = 510007;
    public final static int CODE_EXTOURNE_2002115_AVS_API = 503007;
    public final static int CODE_EXTOURNE_2002115_AVS_REO = 501007;
    public final static int CODE_EXTOURNE_2002115_AVS_RO = 500007;

    public final static int CODE_PAIEMENT_ALLOC_VEUVE_AVS_REO = 501006;
    public final static int CODE_PAIEMENT_ALLOC_VEUVE_AVS_RO = 500006;
    public final static int CODE_PAIEMENT_RETRO_AI_API = 513005;
    public final static int CODE_PAIEMENT_RETRO_AI_REO = 511005;
    public final static int CODE_PAIEMENT_RETRO_AI_RO = 510005;
    public final static int CODE_PAIEMENT_RETRO_AVS_API = 503005;

    public final static int CODE_PAIEMENT_RETRO_AVS_REO = 501005;
    public final static int CODE_PAIEMENT_RETRO_AVS_RO = 500005;

    public final static int CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AI_API = 513001;
    public final static int CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AI_REO = 511001;
    public final static int CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AI_RO = 510001;
    public final static int CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AVS_API = 503001;
    public final static int CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AVS_REO = 501001;
    // CODE RECAP
    public final static int CODE_RENTE_EN_COURS_MOIS_PRECEDENT_AVS_RO = 500001;

    public static final int GENRE_RECAP_ADAPTATION_BISANNUEL = 3000;
    public static final int GENRE_RECAP_ALLOC_UNIQUE_VEUVE = 6000;
    public static final int GENRE_RECAP_AUGMENTATION = 2000;
    public static final int GENRE_RECAP_DIMINUTION = 4000;
    public static final int GENRE_RECAP_EXTOURNE_200_2115 = 7000;
    public static final int GENRE_RECAP_PMT_RETROACTIF = 5000;
    public static final int GENRE_RECAP_REC_FIN_MOIS_PRECEDENT = 1000;

    public final static String RECAP_PC_MONTANT_AI = "421001";
    public final static String RECAP_PC_MONTANT_AVS = "411001";
    public final static String RECAP_PC_NOMBRE_AI = "421000";
    public final static String RECAP_PC_NOMBRE_AVS = "411000";

}
