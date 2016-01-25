/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.api.decisions;

/**
 * @author fha
 */
public interface IRFDecisions {

    public static final String ANNULE = "66000903";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String CS_GROUPE_ETAT_DECISION = "RFETADEC";

    // CONSTANTES POUR LE TYPE DE DECISION
    public static final String DECISION = "66001701";
    public static final String DECISION_OPPOSITION = "66001702";

    public static final String DECISION_RECOURS_TC = "66001703";
    public static final String DECISION_RECOURS_TF = "66001704";
    public static final String NON_VALIDE = "66000902";

    public static final String TYPE_VALIDATION_DECISION_NORMAL = "typeValidationDecisionNormal";
    public static final String TYPE_VALIDATION_DECISION_SIMULATION = "typeValidationDecisionSimulation";

    // CONSTANTES POUR L'ETAT DE LA DECISION
    public static final String VALIDE = "66000901";

}
