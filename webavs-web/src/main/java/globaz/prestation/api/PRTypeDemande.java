package globaz.prestation.api;

import ch.globaz.common.util.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PRTypeDemande {

    APG(IPRDemande.CS_TYPE_APG, "APG"),
    MATERNITE(IPRDemande.CS_TYPE_MATERNITE, "MATERNITE"),
    PANDEMIE(IPRDemande.CS_TYPE_PANDEMIE, "PANDEMIE"),
    PATERNITE(IPRDemande.CS_TYPE_PATERNITE, "PATERNITE"),
    PROCHE_AIDANT(IPRDemande.CS_TYPE_PROCHE_AIDANT, "PROCHE_AIDANT");

    private final String csType;
    /**
     * Utilisé pour récupérer les valeurs des références pour le calcul de la prestation (calculAPGReferenceData.xml)
     */
    private final String calculreferenceData;

    /**
     * Permet de savoir si l'enum est de type proche aidant.
     *
     * @return si l'enum est de type proche aidant.
     */
    public boolean isProcheAidant() {
        return this == PROCHE_AIDANT;
    }

    /**
     * Permet de savoir si l'enum est de type pandémie.
     *
     * @return si l'enum est de type pandémie.
     */
    public boolean isPandemie() {
        return this == PANDEMIE;
    }

    /**
     * Permet de savoir si l'enum est de type maternité.
     *
     * @return si l'enum est de type maternité.
     */
    public boolean isMaternite() {
        return this == MATERNITE;
    }

    /**
     * Permet de savoir si l'enum est de type APG.
     *
     * @return si l'enum est de type APG.
     */
    public boolean isApg() {
        return this == APG;
    }

    /**
     * Permet de savoir si l'enum est de type paternité.
     *
     * @return si l'enum est de type /**.
     */
    public boolean isPaternite() {
        return this == PATERNITE;
    }

    /**
     * Converti le code système en enum.
     *
     * @param codeSystem Le code système à convertir.
     *
     * @return L'enum correspondante aux code système.
     */
    public static PRTypeDemande toEnumByCs(String codeSystem) {
        return Enums.toEnum(codeSystem, PRTypeDemande.class, PRTypeDemande::getCsType);
    }

}
