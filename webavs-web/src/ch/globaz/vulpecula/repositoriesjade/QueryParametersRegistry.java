package ch.globaz.vulpecula.repositoriesjade;

/**
 * Registre des paramètres pouvant être transmis en AJAX aux repositories Jade.
 */
public interface QueryParametersRegistry {
    public final String EMPTY = "";

    public final String PT_NOM_PRENOM = "nomPrenom";
    public final String PT_ID_POSTE_TRAVAIL = "idPosteTravail";
    public final String PT_ID_TRAVAILLEUR = "idTravailleur";
    public final String PT_ID_EMPLOYEUR = "idEmployeur";
    public final String PT_DATE_NAIS = "dateNais";
    public final String PT_NSS = "nss";
    public final String PT_QUALIFICATION = "qualification";
    public final String PT_GENRE = "genre";
    public final String PT_RAISON_SOCIALE_EMPLOYEUR = "raisonSociale";
    public final String PT_CONVENTION = "convention";
}
