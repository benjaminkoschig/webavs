package ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum LoyerType implements CodeSystemEnum<LoyerType> {

    NET_AVEC_CHARGE("64011001"),
    NET_AVEC_CHARGE_FORFAITAIRES("64011002"),
    NET_SANS_CHARGE("64011003"),
    BRUT_CHARGES_COMPRISES("64011004"),
    VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE("64011005"),
    PENSION_NON_RECONNUE("64011006");

    private String value;

    LoyerType(String value) {
        this.value = value;
    }

    public static LoyerType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, LoyerType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isNetAvecCharge() {
        return equals(NET_AVEC_CHARGE);
    }

    public boolean isNetAvecChargeForfaitaires() {
        return equals(NET_AVEC_CHARGE_FORFAITAIRES);
    }

    public boolean isNetSansCharge() {
        return equals(NET_SANS_CHARGE);
    }

    public boolean isBrutChargesComprises() {
        return equals(BRUT_CHARGES_COMPRISES);
    }

    public boolean isValeurLocativeChezProprietaire() {
        return equals(VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE);
    }

    public boolean isPensionsNonRecounnue() {
        return equals(PENSION_NON_RECONNUE);
    }
}
