package ch.globaz.auriga.business.constantes;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.auriga.business.exceptions.AurigaTechnicalException;

public enum AUDecisionEtat {
    COMPTABILISEE("951003"),
    RECTIFIEE("951005"),
    REPRISE("951004"),
    SUPPRIMEE("951002"),
    VALIDEE("951001");

    public static AUDecisionEtat getEnumFromCodeSystem(String codeSystem) {

        for (AUDecisionEtat etat : AUDecisionEtat.values()) {
            if (etat.getCodeSystem().equals(codeSystem)) {
                return etat;
            }
        }

        throw new AurigaTechnicalException(AUDecisionEtat.class.getName() + " : no enum founded for codeSystem("
                + codeSystem + ")");

    }

    public static List<String> getListEtatActif() {

        List<String> listEtatActif = new ArrayList<String>();

        listEtatActif.add(AUDecisionEtat.REPRISE.getCodeSystem());
        listEtatActif.add(AUDecisionEtat.VALIDEE.getCodeSystem());
        listEtatActif.add(AUDecisionEtat.COMPTABILISEE.getCodeSystem());

        return listEtatActif;
    }

    public static List<String> getListEtatRenouvelable() {

        List<String> listEtatRenouvelable = new ArrayList<String>();

        listEtatRenouvelable.add(AUDecisionEtat.REPRISE.getCodeSystem());
        listEtatRenouvelable.add(AUDecisionEtat.VALIDEE.getCodeSystem());
        listEtatRenouvelable.add(AUDecisionEtat.COMPTABILISEE.getCodeSystem());

        return listEtatRenouvelable;
    }

    public static boolean isEtatModifiable(AUDecisionEtat etat) {
        return AUDecisionEtat.isInList(etat, AUDecisionEtat.VALIDEE);
    }

    public static boolean isEtatModifiable(String etat) {
        return AUDecisionEtat.isEtatModifiable(AUDecisionEtat.getEnumFromCodeSystem(etat));
    }

    private static boolean isInList(AUDecisionEtat etat, AUDecisionEtat... etats) {

        for (AUDecisionEtat tmp : etats) {
            if (tmp.equals(etat)) {
                return true;
            }
        }

        return false;
    }

    private String codeSystem;

    private AUDecisionEtat(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public boolean equals(String codeSystem) {
        return this.equals(AUDecisionEtat.getEnumFromCodeSystem(codeSystem));
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
