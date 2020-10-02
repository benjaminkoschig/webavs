package ch.globaz.pegasus.business.domaine.decision;

public enum MotifDecision {
    JUSTIFICATIFS_PAS_FOURNIS(false),
    JUSTIFICATIFS_DEMANDES(false),
    PARTIS_SANS_ADRESSE(false),
    INTERETS_ETRANGERS(false),
    SEJOUR_ETRANGERS(false),
    DEPART_DEFINITIF(false),
    RENCONTRE_IMPOSSIBLE(false),
    SUPPRESSION_RENTE(false),
    NOTIFIE_ENFANT(false),
    PARENT_ETRANGER(false),
    SEPARATION(false),
    DEMANDE_BENEFICIAIRE(false),
    INCARCERATION(false),
    TRANSFERT_DOSSIER_AUTRE_ORGANE(false),
    DECES(false),
    VEUVAGE(false),
    MARIAGE(false),
    DIVORCE(false),
    AUTRE(false),
    INDEFINIT(false),
    SEUIL_FORTUNE_DEPASSE_SUPRESSION(false),

    JUSTIFICATIFS_DEMANDES_INITIAL(true),
    PARTIS_SANS_ADRESSE_INITIAL(true),
    RENCONTRE_IMPOSSIBLE_INITIAL(true),
    DOMICILLE_PAR_RECONNU(true),
    AUCUN_DROIT_DOMICILLE(true),
    AUCUN_DROIT_EMS(true),
    AUCUN_DROIT_EMS2(true),
    DROIT_ENTRETIEN(true),
    DROIT_INDEMNITE_AI(true),
    ENFANT_PARENT_RENTIER(true),
    DELAI_CARENCE(true),
    RENONCIATION(true),
    SEUIL_FORTUNE_DEPASSE(true);

    private boolean isRefus;

    private MotifDecision(boolean isRefus) {
        this.isRefus = isRefus;
    }

    public boolean isRefus() {
        return isRefus;
    }
}
