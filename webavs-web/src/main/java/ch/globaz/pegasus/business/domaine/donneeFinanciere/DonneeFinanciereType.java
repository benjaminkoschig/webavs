package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum DonneeFinanciereType implements CodeSystemEnum<DonneeFinanciereType> {

    RENTE_AVS_AI("64007001"),
    IJAI("64007002"),
    API_AVS_AI("64007003"),
    AUTRE_RENTE("64007004"),
    INDEMNITE_JOURNLIERE_APG("64007005"),
    AUTRE_API("64007006"),
    LOYER("64007007"),
    TAXE_JOURNALIERE_HOME("64007008"),
    COMPTE_BANCAIRE_POSTAL("64007009"),
    // BIEN_IMMOBILIER("64007010"),
    TITRE("64007011"),
    ASSURANCE_VIE("64007012"),
    CAPITAL_LPP("64007013"),
    AUTRE_DETTE_PROUVEE("64007014"),
    PRET_ENVERS_TIERS("64007015"),
    ASSURANCE_RENTE_VIAGERE("64007016"),
    NUMERAIRE("64007017"),
    MARCHANDISE_STOCK("64007018"),
    VEHICULE("64007019"),
    BETAIL("64007020"),
    AUTRE_FORTUNE_MOBILIERE("64007021"),
    REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE("64007022"),
    REVENU_HYPOTHETIQUE("64007023"),
    ALLOCATION_FAMILIALLE("64007024"),
    CONTRAT_ENTRETIEN_VIAGER("64007025"),
    AUTRE_REVENU("64007026"),
    COTISATION_PSAL("64007027"),
    PENSION_ALIMENTAIRE("64007028"),
    DESSAISISSEMENT_FORTUNE("64007029"),
    DESSAISISSEMENT_REVENU("64007030"),
    REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE("64007031"),
    BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE("64007032"),
    BIEN_IMMOBILIER_NON_HABITABLE("64007033"),
    BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE("64007034"),
    FRAIS_GARDE("64007035"),
    PRIME_ASSURANCE_MALADIE("64007036"),
    SUBSIDE_ASSURANCE_MALADIE("64007037"),
    SEJOUR_MOIS_PARTIEL_HOME("64007038"),
    UNDIFINED("0");

    private String value;

    DonneeFinanciereType(String value) {
        this.value = value;
    }

    public static DonneeFinanciereType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, DonneeFinanciereType.class);
    }

    public boolean isRenteAvsAi() {
        return equals(RENTE_AVS_AI);
    }

    public boolean isIjAi() {
        return equals(IJAI);
    }

    public boolean isApiAvsAi() {
        return equals(API_AVS_AI);
    }

    public boolean isAutreRente() {
        return equals(AUTRE_RENTE);
    }

    public boolean isIndeminteJournaliereApg() {
        return equals(INDEMNITE_JOURNLIERE_APG);
    }

    public boolean isAutreApi() {
        return equals(AUTRE_API);
    }

    public boolean isLoyer() {
        return equals(LOYER);
    }

    public boolean isTaxeJournalierHome() {
        return equals(TAXE_JOURNALIERE_HOME);
    }

    public boolean isCompteBancairePostal() {
        return equals(COMPTE_BANCAIRE_POSTAL);
    }

    // public boolean isBienImmobilier() {
    // return equals(BIEN_IMMOBILIER);
    // }

    public boolean isTitre() {
        return equals(TITRE);
    }

    public boolean isAssuranceVie() {
        return equals(ASSURANCE_VIE);
    }

    public boolean isCapitalLpp() {
        return equals(CAPITAL_LPP);
    }

    public boolean isAutreDetteProuvee() {
        return equals(AUTRE_DETTE_PROUVEE);
    }

    public boolean isPretEnversTiers() {
        return equals(PRET_ENVERS_TIERS);
    }

    public boolean isAssuranceRenteViagere() {
        return equals(ASSURANCE_RENTE_VIAGERE);
    }

    public boolean isNumeraire() {
        return equals(NUMERAIRE);
    }

    public boolean isMarchandiseStock() {
        return equals(MARCHANDISE_STOCK);
    }

    public boolean isVehicule() {
        return equals(VEHICULE);
    }

    public boolean isBetail() {
        return equals(BETAIL);
    }

    public boolean isAutreFortuneMobiliere() {
        return equals(AUTRE_FORTUNE_MOBILIERE);
    }

    public boolean isRevenuActiviteLucrativeIndependante() {
        return equals(REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE);
    }

    public boolean isRevenueHypothtique() {
        return equals(REVENU_HYPOTHETIQUE);
    }

    public boolean isAllocationFamilliale() {
        return equals(ALLOCATION_FAMILIALLE);
    }

    public boolean isContratEntretienViager() {
        return equals(CONTRAT_ENTRETIEN_VIAGER);
    }

    public boolean isAutreRevenue() {
        return equals(AUTRE_REVENU);
    }

    public boolean isCotisationPsal() {
        return equals(COTISATION_PSAL);
    }

    public boolean isPensionAlimentaire() {
        return equals(PENSION_ALIMENTAIRE);
    }

    public boolean isDessaississementFortune() {
        return equals(DESSAISISSEMENT_FORTUNE);
    }

    public boolean isDessaisissementRevenu() {
        return equals(DESSAISISSEMENT_REVENU);
    }

    public boolean isRevenueActiviteLucrativeDependante() {
        return equals(REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE);
    }

    public boolean isBienImmobilierServantHbitationPrincipale() {
        return equals(BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE);
    }

    public boolean isBienImmobilierNonHabitable() {
        return equals(BIEN_IMMOBILIER_NON_HABITABLE);
    }

    public boolean isBienImmobilierNonPrincipale() {
        return equals(BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE);
    }

    public boolean isPrimeAssuranceMaladie() {
        return equals(PRIME_ASSURANCE_MALADIE);
    }

    public boolean isSubsideAssuranceMaladie() {
        return equals(SUBSIDE_ASSURANCE_MALADIE);
    }

    public boolean isFraisDeGarde() {
        return equals(FRAIS_GARDE);
    }

    public boolean isSejourMoisPartiel()  {
        return equals(SEJOUR_MOIS_PARTIEL_HOME);
    }

    @Override
    public String getValue() {
        return value;
    }

}
