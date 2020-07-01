package globaz.aquila.process.elp;

public interface ElpStatut {

    // Type SC
    String CDP_SANS_OPPOSITION = "102";
    String CDP_NON_DELIVRE ="103";
    String CDP_AVEC_OPPOSITION = "104";
    String PAIEMENT_COMPLET="105";

    // Type SP
    String PV_NON_LIEU = "102";
    String PV_SAISIE_MOB = "202";
    String SOLDE_OP = "203";
    String PV_SAISIE_SAL = "204";
    String PV_SAISIE_SAL_MOB = "205";
    String PV_SAISIE_ADB = "206";
    String COMMINATION_FAILLITE = "207";


    // Type RC
    String REGLEMENT_COMPLET_DETTE= "302";
    String ADB = "303";
    String DEBUT_PROCEDURE_FAILLITE = "304";
    String SURSIS = "305";

}
