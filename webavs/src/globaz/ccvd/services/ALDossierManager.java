package globaz.ccvd.services;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Tue Mar 06 14:28:07 CET 2007
 */
public class ALDossierManager extends BManager {

    private static final long serialVersionUID = 7422350166897286211L;

    /** Table : JAFPDOS */

    /** Pour etatDossier - �tat dossier est = � ... (EETAT) */
    private String forEtatDossier = new String();

    /** Pour idAllocataire - id allocataire est = � ... (EIDP) */
    private String forIdAllocataire = new String();

    /** Pour idCategorie - id cat�gorie est = � ... (EIDC) */
    private String forIdCategorie = new String();

    /** Pour idDossier - id dossier (cl� primaire) est = � ... (EID) */
    private String forIdDossier = new String();

    /** Pour motifReduction - motif r�duction est = � ... (EMOTRD) */
    private String forMotifReduction = new String();

    /** Pour NumeroAdressePostale - no adresse postale est = � ... (EADRP) */
    private String forNumeroAdressePostale = new String();

    /**
     * Pour numeroAffilie - no affili� (cl� �trang�re mappaffi) est = � ... (EIDAF)
     */
    private String forNumeroAffilie = new String();

    /** Pour tauxOccupation - taux d'occupation est = � ... (ETOCC) */
    private String forTauxOccupation = new String();

    /** Pour tauxReduction - taux de r�duction est = � ... (EREDUC) */
    private String forTauxReduction = new String();

    /** Pour typeAlllocataireAF - type allocataire AF est = � ... (ETYPAL) */
    private String forTypeAlllocataireAF = new String();

    /**
     * Pour typeBonification - type de bonification du dossier est = � ... (EBONIF)
     */
    private String forTypeBonification = new String();

    /** Pour debutActivite - d�but activit� est >= � ... (EDACT) */
    private String fromDebutActivite = new String();

    /** Pour debutValidite - d�but validit� est >= � ... (EDVAL) */
    private String fromDebutValidite = new String();

    /** Pour finActivite - fin activit� est >= � ... (EFACT) */
    private String fromFinActivite = new String();

    /** Pour finValidite - fin validit� est >= � ... (EFVAL) */
    private String fromFinValidite = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "JAFPDOS" (Model : ALDossier)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(IALDossierDefTable.TABLE_NAME).toString();
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param statement
     *            de type BStatement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Retourne la clause WHERE de la requete SQL
     * 
     * @param statement
     *            BStatement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        /* composant de la requete initialises avec les options par defaut */
        StringBuffer sqlWhere = new StringBuffer();
        // traitement du positionnement
        if (getForNumeroAdressePostale().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable._NUMERO_ADRESSE_POSTALE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNumeroAdressePostale()));
        }
        // traitement du positionnement
        if (getForTypeBonification().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.TYPE_BONIFICATION).append("=")
                    .append(_dbWriteString(statement.getTransaction(), getForTypeBonification()));
        }
        // traitement du positionnement
        if (getForEtatDossier().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.ETAT_DOSSIER).append("=")
                    .append(_dbWriteString(statement.getTransaction(), getForEtatDossier()));
        }
        // traitement du positionnement
        if (getForIdDossier().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.ID_DOSSIER).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdDossier()));
        }
        // traitement du positionnement
        if (getForNumeroAffilie().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.NUMERO_AFFILIE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForNumeroAffilie()));
        }
        // traitement du positionnement
        if (getForIdCategorie().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.ID_CATEGORIE).append("=")
                    .append(_dbWriteString(statement.getTransaction(), getForIdCategorie()));
        }
        // traitement du positionnement
        if (getForIdAllocataire().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.ID_ALLOCATAIRE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdAllocataire()));
        }
        // traitement du positionnement
        if (getForMotifReduction().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.MOTIF_REDUCTION).append("=")
                    .append(_dbWriteString(statement.getTransaction(), getForMotifReduction()));
        }
        // traitement du positionnement
        if (getForTauxReduction().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.TAUX_REDUCTION).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForTauxReduction()));
        }
        // traitement du positionnement
        if (getForTauxOccupation().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.TAUX_OCCUPATION).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForTauxOccupation()));
        }
        // traitement du positionnement
        if (getForTypeAlllocataireAF().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.TYPE_ALLLOCATAIRE_AF).append("=")
                    .append(_dbWriteString(statement.getTransaction(), getForTypeAlllocataireAF()));
        }

        // traitement du positionnement
        if (getFromDebutActivite().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.DEBUT_ACTIVITE).append(">=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getFromDebutActivite()));
        }

        // traitement du positionnement
        if (getFromDebutValidite().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.DEBUT_VALIDITE).append(">=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getFromDebutValidite()));
        }

        // traitement du positionnement
        if (getFromFinActivite().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.FIN_ACTIVITE).append(">=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getFromFinActivite()));
        }

        // traitement du positionnement
        if (getFromFinValidite().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IALDossierDefTable.FIN_VALIDITE).append(">=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getFromFinValidite()));
        }

        return sqlWhere.toString();
    }

    /**
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALDossier();
    }

    /**
     * Renvoie forEtatDossier;
     * 
     * @return String etatDossier - �tat dossier;
     */
    public String getForEtatDossier() {
        return forEtatDossier;
    }

    /**
     * Renvoie forIdAllocataire;
     * 
     * @return String idAllocataire - id allocataire;
     */
    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    /**
     * Renvoie forIdCategorie;
     * 
     * @return String idCategorie - id cat�gorie;
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * Renvoie forIdDossier;
     * 
     * @return String idDossier - id dossier (cl� primaire);
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * Renvoie forMotifReduction;
     * 
     * @return String motifReduction - motif r�duction;
     */
    public String getForMotifReduction() {
        return forMotifReduction;
    }

    /**
     * Renvoie forNumeroAdressePostale;
     * 
     * @return String NumeroAdressePostale - no adresse postale;
     */
    public String getForNumeroAdressePostale() {
        return forNumeroAdressePostale;
    }

    /**
     * Renvoie forNumeroAffilie;
     * 
     * @return String numeroAffilie - no affili� (cl� �trang�re mappaffi);
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * Renvoie forTauxOccupation;
     * 
     * @return String tauxOccupation - taux d'occupation;
     */
    public String getForTauxOccupation() {
        return forTauxOccupation;
    }

    /**
     * Renvoie forTauxReduction;
     * 
     * @return String tauxReduction - taux de r�duction;
     */
    public String getForTauxReduction() {
        return forTauxReduction;
    }

    /**
     * Renvoie forTypeAlllocataireAF;
     * 
     * @return String typeAlllocataireAF - type allocataire AF;
     */
    public String getForTypeAlllocataireAF() {
        return forTypeAlllocataireAF;
    }

    /**
     * Renvoie forTypeBonification;
     * 
     * @return String typeBonification - type de bonification du dossier;
     */
    public String getForTypeBonification() {
        return forTypeBonification;
    }

    /**
     * S�lection par fromDebutActivite;
     * 
     * @return String debutActivite - d�but activit�;
     */
    public String getFromDebutActivite() {
        return fromDebutActivite;
    }

    /**
     * S�lection par fromDebutValidite;
     * 
     * @return String debutValidite - d�but validit�;
     */
    public String getFromDebutValidite() {
        return fromDebutValidite;
    }

    /**
     * S�lection par fromFinActivite;
     * 
     * @return String finActivite - fin activit�;
     */
    public String getFromFinActivite() {
        return fromFinActivite;
    }

    /**
     * S�lection par fromFinValidite;
     * 
     * @return String finValidite - fin validit�;
     */
    public String getFromFinValidite() {
        return fromFinValidite;
    }

    /**
     * S�lection par forEtatDossier
     * 
     * @param newForEtatDossier
     *            String - �tat dossier
     */
    public void setForEtatDossier(String newForEtatDossier) {
        forEtatDossier = newForEtatDossier;
    }

    /**
     * S�lection par forIdAllocataire
     * 
     * @param newForIdAllocataire
     *            String - id allocataire
     */
    public void setForIdAllocataire(String newForIdAllocataire) {
        forIdAllocataire = newForIdAllocataire;
    }

    /**
     * S�lection par forIdCategorie
     * 
     * @param newForIdCategorie
     *            String - id cat�gorie
     */
    public void setForIdCategorie(String newForIdCategorie) {
        forIdCategorie = newForIdCategorie;
    }

    /**
     * S�lection par forIdDossier
     * 
     * @param newForIdDossier
     *            String - id dossier (cl� primaire)
     */
    public void setForIdDossier(String newForIdDossier) {
        forIdDossier = newForIdDossier;
    }

    /**
     * S�lection par forMotifReduction
     * 
     * @param newForMotifReduction
     *            String - motif r�duction
     */
    public void setForMotifReduction(String newForMotifReduction) {
        forMotifReduction = newForMotifReduction;
    }

    /**
     * S�lection par forNumeroAdressePostale
     * 
     * @param newForNumeroAdressePostale
     *            String - no adresse postale
     */
    public void setForNumeroAdressePostale(String newForNumeroAdressePostale) {
        forNumeroAdressePostale = newForNumeroAdressePostale;
    }

    /**
     * S�lection par forNumeroAffilie
     * 
     * @param newForNumeroAffilie
     *            String - no affili� (cl� �trang�re mappaffi)
     */
    public void setForNumeroAffilie(String newForNumeroAffilie) {
        forNumeroAffilie = newForNumeroAffilie;
    }

    /**
     * S�lection par forTauxOccupation
     * 
     * @param newForTauxOccupation
     *            String - taux d'occupation
     */
    public void setForTauxOccupation(String newForTauxOccupation) {
        forTauxOccupation = newForTauxOccupation;
    }

    /**
     * S�lection par forTauxReduction
     * 
     * @param newForTauxReduction
     *            String - taux de r�duction
     */
    public void setForTauxReduction(String newForTauxReduction) {
        forTauxReduction = newForTauxReduction;
    }

    /**
     * S�lection par forTypeAlllocataireAF
     * 
     * @param newForTypeAlllocataireAF
     *            String - type allocataire AF
     */
    public void setForTypeAlllocataireAF(String newForTypeAlllocataireAF) {
        forTypeAlllocataireAF = newForTypeAlllocataireAF;
    }

    /**
     * S�lection par forTypeBonification
     * 
     * @param newForTypeBonification
     *            String - type de bonification du dossier
     */
    public void setForTypeBonification(String newForTypeBonification) {
        forTypeBonification = newForTypeBonification;
    }

    /**
     * S�lection par fromDebutActivite
     * 
     * @param newFromDebutActivite
     *            String - d�but activit�
     */
    public void setFromDebutActivite(String newFromDebutActivite) {
        fromDebutActivite = newFromDebutActivite;
    }

    /**
     * S�lection par fromDebutValidite
     * 
     * @param newFromDebutValidite
     *            String - d�but validit�
     */
    public void setFromDebutValidite(String newFromDebutValidite) {
        fromDebutValidite = newFromDebutValidite;
    }

    /**
     * S�lection par fromFinActivite
     * 
     * @param newFromFinActivite
     *            String - fin activit�
     */
    public void setFromFinActivite(String newFromFinActivite) {
        fromFinActivite = newFromFinActivite;
    }

    /**
     * S�lection par fromFinValidite
     * 
     * @param newFromFinValidite
     *            String - fin validit�
     */
    public void setFromFinValidite(String newFromFinValidite) {
        fromFinValidite = newFromFinValidite;
    }

}
