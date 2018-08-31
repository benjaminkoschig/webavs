package ch.globaz.vulpecula.domain.models.postetravail;

/**
 * Enumération représentant la qualification d'un travailleur pour un poste de
 * travail.
 */
public enum Qualification {
    APPRENTI(68007001),
    CHEF_MONTEUR_ET_MONTEUR_A(68007002),
    ETUDIANT(68007003),
    FERBLANTIER(68007004),
    FERBLANTIER_INSTALLATEUR_SANITAIRE(68007005),
    FEMME_DE_MENAGE(68007006),
    INDEPENDANT(68007007),
    INSTALLATEUR_SANITAIRE(68007008),
    MANOEUVRE(68007009),
    PERSONNEL_ADMINISTRATIF(68007010),
    PERSONNEL_TECHNIQUE(68007011),
    TRAVAILLEUR_QUALIFIE(68007012),
    CHARPENTIER_QUALIFIE(68007013),
    DECORATEUR_INTERIEUR(68007014),
    MENUISIER_QUALIFIE(68007015),
    MACHINISTE_SPECIALISE(68007016),
    PARQUETEUR_QUALIFIE(68007017),
    PERSONNEL_VENTE(68007018),
    TRAVAILLEUR_SEMI_QUALIFIE(68007019),
    VITRIER_QUALIFIE(68007020),
    EBENISTE_QUALIFIE(68007021),
    PLATRIER(68007022),
    PLATRIER_PEINTRE(68007023),
    SOUDEUR(68007024),
    AIDE_MONTEUR_ELECTRIQUE(68007025),
    CONTROLEUR_BREVET(68007026),
    CHEF_CHANTIER(68007027),
    DESINATEUR_ELECTRICIEN(68007028),
    MONTEUR_ELECTRICIEN(68007029),
    CONTREMAITRE_MAITRISE(68007030),
    MONTEUR_LIGNE(68007031),
    SPECIALISTE_TELECOM(68007032),
    CONTREMAITRE(68007033),
    PAYSAGISTE(68007034),
    TRAVAILLEUR_DEBUTANT(68007035),
    TRAVAILLEUR_INTERIM_AUTRE(68007036),
    TRAVAILLEUR_INTERIM_METAL(68007037),
    TRAVAILLEUR_INTERIM_FERBLANTIER(68007038),
    TRAVAILLEUR_INTERIM_ELECTRICIEN(68007039),
    TRAVAILLEUR_INTERIM_MENUISIER(68007040),
    TRAVAILLEUR_INTERIM_NETTOYEUR(68007041),
    TRAVAILLEUR_INTERIM_PEINTRE(68007042),
    TRAVAILLEUR_INTERIM_PAYSAGISTE(68007043),
    TRAVAILLEUR_INTERIM_REMPLACANT(68007044),
    TRAVAILLEUR_INTERIM_INF_22HEURES(68007045),
    TRAVAILLEUR_SANS_QUALIF_4ANS(68007046),
    TRAVAILLEUR_SANS_QUALIFICATION(68007047),
    MAGASINIER(68007048),
    SCIEUR(68007049),
    STAGIAIRE(68007050),
    COURTEPOINTIERE(68007051),
    PEINTRE_QUALIFIE(68007052),
    COUVREUR(68007053),
    MONTEUR_CHAUFFAGE(68007054),
    SERRURIER(68007055),
    CHEF_EQUIPE(68007056),
    SELLIER(68007057),
    MONTEUR_EN_TABLEAU(68007058),
    OUVRIER_B(68007059),
    AGENT_PROPRETE(68007060),
    AGENT_EXPLOITATION(68007061),
    PRE_APPRENTI(68007062),
    TRAVAILLEUR_INF_18H(68007063),
    TRAVAILLEUR_SUP_18H(68007064),
    POSEUR_DE_SOL(68007065),
    INSTALLATEUR_CHAUFFAGE(68007066),
    TUYAUTEUR(68007067),
    AUTOMATICIEN(68007068),
    ELECTRICIEN_DE_MONTAGE(68007069),
    INSTALLATEUR_ELECTRICIEN(68007070),
    HORTICULTEUR(68007071),
    JARDINIER(68007072),
    PAYSAGISTE_MACHINISTE(68007073),
    PAYSAGISTE_MACON(68007074),
    CONSTRUCTEUR_METALLIQUE(68007075);

    private int value;

    private Qualification(final int value) {
        this.value = value;
    }

    /**
     * Construction de l'énumération à partir d'un code système
     * 
     * @param value
     *            Code système sous forme de String
     * @return Etat de l'énumération
     */
    public static Qualification fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de qualification");
        }

        for (Qualification q : Qualification.values()) {
            if (valueAsInt == q.value) {
                return q;
            }
        }
        throw new IllegalArgumentException("La valeur (" + value + ") ne correspond à aucune qualification connue");
    }

    /**
     * Retourne le code système représentant la qualification
     * 
     * @return String représentant un code système
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Retourne si le paramètre correspond bien à un code système de la famille
     * des qualifications
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
