package ch.globaz.orion.business.domaine.pucs;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum EtatPucsFile implements CodeSystemEnum<EtatPucsFile> {

    A_VALIDE("11020001"),
    REJETE("11020002"),
    A_TRAITER("11020003"),
    EN_TRAITEMENT("11020004"),
    UPLOADED(""), // Pour EBU
    TRAITER(""), // pour EBU
    COMPTABILISE("11020005"),
    EN_ERREUR("11020006"),
    UNDEFINDED("");

    private String value;

    public static final String CODE_FAMILLE = "GEBETPUC";

    EtatPucsFile(String csEtat) {
        value = csEtat;
    }

    public static EtatPucsFile fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, EtatPucsFile.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isRefuse() {
        return REJETE.equals(this);
    }

    public boolean isComptabilise() {
        return COMPTABILISE.equals(this);
    }

    public boolean isEnTraitement() {
        return EN_TRAITEMENT.equals(this);
    }

    public boolean isTraite() {
        return TRAITER.equals(this);
    }

    public boolean isATraiter() {
        return A_TRAITER.equals(this);
    }

    public boolean isEnErreur() {
        return EN_ERREUR.equals(this);
    }

    public boolean isAValider() {
        return A_VALIDE.equals(this);
    }

    public boolean isTraitable() {
        return isATraiter() || isEnErreur();
    }

    public boolean isEditable() {
        return !isComptabilise() && !isTraite() && !isEnTraitement();
    }

}