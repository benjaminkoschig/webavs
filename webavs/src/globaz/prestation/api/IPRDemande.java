/*
 * Créé le 10 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.api;

/**
 * Constantes pour le projet prestation (notamment les codes systèmes)
 * 
 * @author vre
 */
public interface IPRDemande {

    /** etat demande: CLOTURE */
    public String CS_ETAT_CLOTURE = "52200002";

    /** etat demande: OUVERT */
    public String CS_ETAT_OUVERT = "52200001";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    /** nom du groupe des codes systèmes pour l'état d'une demande */
    public String CS_GROUPE_ETAT_DEMANDE = "PRETADEMAN";

    /** nom du groupe des codes systèmes pour le type d'une demande */
    public String CS_GROUPE_TYPE_DEMANDE = "PRTYPDEMAN";

    public String CS_TYPE_APG = "52201001";
    public String CS_TYPE_IJ = "52201003";
    public String CS_TYPE_MATERNITE = "52201002";
    public String CS_TYPE_PC = "52201005";
    public String CS_TYPE_PC_FAMILLES = "52201007";
    public String CS_TYPE_RENTE = "52201004";
    public String CS_TYPE_RFM = "52201006";
}
