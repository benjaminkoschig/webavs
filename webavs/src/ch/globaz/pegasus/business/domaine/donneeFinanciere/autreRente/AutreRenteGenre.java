package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum AutreRenteGenre implements CodeSystemEnum<AutreRenteGenre> {

    LAA("64018001"),
    LPP("64018002"),
    RENTE_ETRANGERE("64018003"),
    ASSURANCE_PRIVEE("64018004"),
    TROISIEME_PILIER("64018005"),
    LAM("64018006"),
    AUTRES("64018007");

    private String value;

    AutreRenteGenre(String value) {
        this.value = value;
    }

    public static AutreRenteGenre fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, AutreRenteGenre.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isLaa() {
        return equals(LAA);
    }

    public boolean isLpp() {
        return equals(LPP);
    }

    public boolean isRenteEtrangere() {
        return equals(RENTE_ETRANGERE);
    }

    public boolean isAssurancePrivee() {
        return equals(ASSURANCE_PRIVEE);
    }

    public boolean isTroisiemePilier() {
        return equals(TROISIEME_PILIER);
    }

    public boolean isLam() {
        return equals(LAM);
    }

    public boolean isAutres() {
        return equals(AUTRES);
    }

}
