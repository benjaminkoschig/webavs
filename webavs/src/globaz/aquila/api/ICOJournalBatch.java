package globaz.aquila.api;

/**
 * <H1>Description</H1> Constantes pour le journal du batch contentieux
 * 
 * @author vre
 */
public interface ICOJournalBatch {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** journal est en traitement */
    String CS_EN_TRAITEMENT = "5150002";

    /** la famille de cs pour l'état du journal */
    String CS_FAMILLE_ETAT_JOURNAL = "COETATJRN";

    /** journal est ouvert */
    String CS_OUVERT = "5150001";

    /** journal est partiellement valide */
    String CS_PARTIEL = "5150003";

    /** journal est entièrement valide */
    String CS_VALIDE = "5150004";

}
