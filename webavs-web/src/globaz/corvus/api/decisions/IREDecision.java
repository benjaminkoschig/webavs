package globaz.corvus.api.decisions;

public interface IREDecision {

    public final static String CS_ETAT_ATTENTE = "52837001";
    public final static String CS_ETAT_PREVALIDE = "52837002";
    public final static String CS_ETAT_VALIDE = "52837003";

    public final static String CS_GENRE_DECISION_COMMUNICATION = "52850001";
    public final static String CS_GENRE_DECISION_DECISION = "52850002";
    public final static String CS_GENRE_DECISION_DECISION_SUR_OPPOSITION = "52850003";

    public final static String CS_GROUPE_ETAT_DECISION = "REETADEC";
    public final static String CS_GRP_GENRE_DECISION = "REGENDEC";
    public final static String CS_GRP_TYPE_DECISION = IREPreparationDecision.CS_GROUPE_TYPE_PREPARATION_DECISION;

    public final static String CS_TYPE_DECISION_COURANT = IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT;
    public final static String CS_TYPE_DECISION_RETRO = IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO;
    public final static String CS_TYPE_DECISION_STANDARD = IREPreparationDecision.CS_TYP_PREP_DECISION_STANDARD;
}
