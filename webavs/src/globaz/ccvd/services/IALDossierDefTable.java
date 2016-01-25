package globaz.ccvd.services;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Tue Mar 06 14:28:07 CET 2007
 */
public interface IALDossierDefTable {
    /** NumeroAdressePostale - no adresse postale (EADRP) */
    public final String _NUMERO_ADRESSE_POSTALE = "EADRP";

    /** copie1 - copie 1 d�cision (ECOP1) */
    public final String COPIE1 = "ECOP1";

    /** copie2 - copie 2 d�cision (ECOP2) */
    public final String COPIE2 = "ECOP2";

    /** copie3 - copie 3 d�cision (ECOP3) */
    public final String COPIE3 = "ECOP3";

    /** copie4 - copie 4 d�cision (ECOP4) */
    public final String COPIE4 = "ECOP4";

    /** copie5 - copie 5 d�cision (ECOP5) */
    public final String COPIE5 = "ECOP5";

    /** debutActivite - d�but activit� (EDACT) */
    public final String DEBUT_ACTIVITE = "EDACT";

    /** debutValidite - d�but validit� (EDVAL) */
    public final String DEBUT_VALIDITE = "EDVAL";

    /** etatDossier - �tat dossier (EETAT) */
    public final String ETAT_DOSSIER = "EETAT";

    /** finActivite - fin activit� (EFACT) */
    public final String FIN_ACTIVITE = "EFACT";

    /** finValidite - fin validit� (EFVAL) */
    public final String FIN_VALIDITE = "EFVAL";

    /** idAllocataire - id allocataire (EIDP) */
    public final String ID_ALLOCATAIRE = "EIDP";

    /** idCategorie - id cat�gorie (EIDC) */
    public final String ID_CATEGORIE = "EIDC";

    /** idDossier - id dossier (cl� primaire) (EID) */
    public final String ID_DOSSIER = "EID";

    /** impressionDecision - impression d�cision (EIMDEC) */
    public final String IMPRESSION_DECISION = "EIMDEC";

    /** motifReduction - motif r�duction (EMOTRD) */
    public final String MOTIF_REDUCTION = "EMOTRD";

    /** nbrJourDebut - nombre de jour d�but (ENBJD) */
    public final String NBR_JOUR_DEBUT = "ENBJD";

    /** nbrJourFin - nombre de jour de fin (ENBJF) */
    public final String NBR_JOUR_FIN = "ENBJF";

    /** numeroAffilie - no affili� (cl� �trang�re mappaffi) (EIDAF) */
    public final String NUMERO_AFFILIE = "EIDAF";

    /** retenueImpot - retenue impot (EIMPOT) */
    public final String RETENUE_IMPOT = "EIMPOT";

    /** Table : JAFPDOS */
    public final String TABLE_NAME = "JAFPDOS";

    /** tauxOccupation - taux d'occupation (ETOCC) */
    public final String TAUX_OCCUPATION = "ETOCC";

    /** tauxReduction - taux de r�duction (EREDUC) */
    public final String TAUX_REDUCTION = "EREDUC";

    /** typeAlllocataireAF - type allocataire AF (ETYPAL) */
    public final String TYPE_ALLLOCATAIRE_AF = "ETYPAL";

    /** typeBonification - type de bonification du dossier (EBONIF) */
    public final String TYPE_BONIFICATION = "EBONIF";

    /** uniteCalcul - unit� de calcul (EUNICA) */
    public final String UNITE_CALCUL = "EUNICA";

    /** utilisateurDossier - utilisateur dossier (EUTIL) */
    public final String UTILISATEUR_DOSSIER = "EUTIL";
}
