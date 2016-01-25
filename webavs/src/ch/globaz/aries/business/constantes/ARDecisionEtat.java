package ch.globaz.aries.business.constantes;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.aries.business.exceptions.AriesTechnicalException;

public enum ARDecisionEtat {

    COMPTABILISEE("962003"),
    RECTIFIEE("962005"),
    REPRISE("962004"),
    SUPPRIMEE("962002"),
    VALIDEE("962001");

    public static ARDecisionEtat getEnumFromCodeSystem(String codeSystem) {

        for (ARDecisionEtat etat : ARDecisionEtat.values()) {
            if (etat.getCodeSystem().equals(codeSystem)) {
                return etat;
            }
        }

        throw new AriesTechnicalException(ARDecisionEtat.class.getName() + " : no enum founded for codeSystem("
                + codeSystem + ")");

    }

    public static List<String> getListEtatActif() {

        List<String> listEtatActif = new ArrayList<String>();

        listEtatActif.add(ARDecisionEtat.REPRISE.getCodeSystem());
        listEtatActif.add(ARDecisionEtat.VALIDEE.getCodeSystem());
        listEtatActif.add(ARDecisionEtat.COMPTABILISEE.getCodeSystem());

        return listEtatActif;
    }

    public static List<String> getListEtatRenouvelable() {

        List<String> listEtatRenouvelable = new ArrayList<String>();

        listEtatRenouvelable.add(ARDecisionEtat.REPRISE.getCodeSystem());
        listEtatRenouvelable.add(ARDecisionEtat.VALIDEE.getCodeSystem());
        listEtatRenouvelable.add(ARDecisionEtat.COMPTABILISEE.getCodeSystem());

        return listEtatRenouvelable;
    }

    public static boolean isEtatModifiable(ARDecisionEtat etat) {
        return ARDecisionEtat.isInList(etat, ARDecisionEtat.VALIDEE);
    }

    public static boolean isEtatModifiable(String etat) {
        return ARDecisionEtat.isEtatModifiable(ARDecisionEtat.getEnumFromCodeSystem(etat));
    }

    private static boolean isInList(ARDecisionEtat etat, ARDecisionEtat... etats) {

        for (ARDecisionEtat tmp : etats) {
            if (tmp.equals(etat)) {
                return true;
            }
        }

        return false;
    }

    private String codeSystem;

    private ARDecisionEtat(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public boolean equals(String codeSystem) {
        return this.equals(ARDecisionEtat.getEnumFromCodeSystem(codeSystem));
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
