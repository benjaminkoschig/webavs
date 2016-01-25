package ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum IndemniteJournaliereApgGenre implements CodeSystemEnum<IndemniteJournaliereApgGenre> {
    IJ_CHOMAGE("64019001"),
    IJ_AA("64019002"),
    IJ_AM("64019003"),
    IJ_LAMAL("64019004"),
    APG("64019005"),
    AUTRE("64019006");

    private String value;

    IndemniteJournaliereApgGenre(String value) {
        this.value = value;
    }

    public static IndemniteJournaliereApgGenre fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, IndemniteJournaliereApgGenre.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isIjChomage() {
        return equals(IJ_CHOMAGE);
    }

    public boolean isIjAa() {
        return equals(IJ_AA);
    }

    public boolean isIjAm() {
        return equals(IJ_AM);
    }

    public boolean isIjLamal() {
        return equals(IJ_LAMAL);
    }

    public boolean isApg() {
        return equals(APG);
    }

    public boolean isAutre() {
        return equals(AUTRE);
    }
}
