package ch.globaz.pegasus.business.domaine.membreFamille;

import java.util.List;
import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum RoleMembreFamille implements CodeSystemEnum<RoleMembreFamille> {
    REQUERANT("64004001"),
    CONJOINT("64004002"),
    ENFANT("64004003"),
    INDEFINIT("0");

    private String value;

    RoleMembreFamille(String value) {
        this.value = value;
    }

    public static RoleMembreFamille fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, RoleMembreFamille.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isRequerant() {
        return equals(REQUERANT);
    }

    public boolean isEnfant() {
        return equals(ENFANT);
    }

    public boolean isConjoint() {
        return equals(CONJOINT);
    }

    public boolean isIn(List<RoleMembreFamille> rolesMembresFamilles) {
        for (RoleMembreFamille role : rolesMembresFamilles) {
            if (equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean isIn(RoleMembreFamille... rolesMembresFamilles) {
        for (int i = 0; i < rolesMembresFamilles.length; i++) {
            if (equals(rolesMembresFamilles[i])) {
                return true;
            }
        }
        return false;
    }
}
