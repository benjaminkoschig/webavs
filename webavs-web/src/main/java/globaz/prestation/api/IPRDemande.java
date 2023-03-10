/*
 * Cr?? le 10 mai 05
 * 
 * Pour changer le mod?le de ce fichier g?n?r?, allez ? : Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.api;

/**
 * Constantes pour le projet prestation (notamment les codes syst?mes)
 * 
 * @author vre
 */
public interface IPRDemande {

    /** etat demande: CLOTURE */
    String CS_ETAT_CLOTURE = "52200002";

    /** etat demande: OUVERT */
    String CS_ETAT_OUVERT = "52200001";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    /** nom du groupe des codes syst?mes pour l'?tat d'une demande */
    String CS_GROUPE_ETAT_DEMANDE = "PRETADEMAN";

    /** nom du groupe des codes syst?mes pour le type d'une demande */
    String CS_GROUPE_TYPE_DEMANDE = "PRTYPDEMAN";

    String CS_TYPE_APG = "52201001";
    String CS_TYPE_IJ = "52201003";
    String CS_TYPE_MATERNITE = "52201002";
    String CS_TYPE_PC = "52201005";
    String CS_TYPE_PC_FAMILLES = "52201007";
    String CS_TYPE_RENTE = "52201004";
    String CS_TYPE_RFM = "52201006";
    String CS_TYPE_PANDEMIE = "52201008";
    String CS_TYPE_PATERNITE = "52201009";
    String CS_TYPE_PROCHE_AIDANT = "52201010";

}
