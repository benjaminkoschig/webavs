package ch.globaz.orion.ws.allocationfamiliale;

import ch.globaz.al.business.constantes.ALCSDossier;

public enum ALDossierEtat {
    ACTIF(ALCSDossier.ETAT_ACTIF),
    COMPLET(ALCSDossier.ETAT_COMPLET),
    EN_CONSTITUTION(ALCSDossier.ETAT_EN_CONSTITUTION),
    RADIE(ALCSDossier.ETAT_RADIE),
    REFUSE(ALCSDossier.ETAT_REFUSE),
    SUSPENDU(ALCSDossier.ETAT_SUSPENDU);

    private String codeSystem;

    private ALDossierEtat(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public static ALDossierEtat getEnumFromCodeSystem(final String codeSystem) {
        for (ALDossierEtat alDossierEtatEnum : ALDossierEtat.values()) {
            if (alDossierEtatEnum.getCodeSystem().equals(codeSystem)) {
                return alDossierEtatEnum;
            }
        }
        throw new IllegalArgumentException("this code system is not allowed" + codeSystem);
    }
}
