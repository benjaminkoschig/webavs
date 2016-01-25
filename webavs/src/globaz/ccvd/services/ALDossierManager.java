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

    /** Pour etatDossier - état dossier est = à ... (EETAT) */
    private String forEtatDossier = new String();

    /** Pour idAllocataire - id allocataire est = à ... (EIDP) */
    private String forIdAllocataire = new String();

    /** Pour idCategorie - id catégorie est = à ... (EIDC) */
    private String forIdCategorie = new String();

    /** Pour idDossier - id dossier (clé primaire) est = à ... (EID) */
    private String forIdDossier = new String();

    /** Pour motifReduction - motif réduction est = à ... (EMOTRD) */
    private String forMotifReduction = new String();

    /** Pour NumeroAdressePostale - no adresse postale est = à ... (EADRP) */
    private String forNumeroAdressePostale = new String();

    /**
     * Pour numeroAffilie - no affilié (clé étrangère mappaffi) est = à ... (EIDAF)
     */
    private String forNumeroAffilie = new String();

    /** Pour tauxOccupation - taux d'occupation est = à ... (ETOCC) */
    private String forTauxOccupation = new String();

    /** Pour tauxReduction - taux de réduction est = à ... (EREDUC) */
    private String forTauxReduction = new String();

    /** Pour typeAlllocataireAF - type allocataire AF est = à ... (ETYPAL) */
    private String forTypeAlllocataireAF = new String();

    /**
     * Pour typeBonification - type de bonification du dossier est = à ... (EBONIF)
     */
    private String forTypeBonification = new String();

    /** Pour debutActivite - début activité est >= à ... (EDACT) */
    private String fromDebutActivite = new String();

    /** Pour debutValidite - début validité est >= à ... (EDVAL) */
    private String fromDebutValidite = new String();

    /** Pour finActivite - fin activité est >= à ... (EFACT) */
    private String fromFinActivite = new String();

    /** Pour finValidite - fin validité est >= à ... (EFVAL) */
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
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALDossier();
    }

    /**
     * Renvoie forEtatDossier;
     * 
     * @return String etatDossier - état dossier;
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
     * @return String idCategorie - id catégorie;
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * Renvoie forIdDossier;
     * 
     * @return String idDossier - id dossier (clé primaire);
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * Renvoie forMotifReduction;
     * 
     * @return String motifReduction - motif réduction;
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
     * @return String numeroAffilie - no affilié (clé étrangère mappaffi);
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
     * @return String tauxReduction - taux de réduction;
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
     * Sélection par fromDebutActivite;
     * 
     * @return String debutActivite - début activité;
     */
    public String getFromDebutActivite() {
        return fromDebutActivite;
    }

    /**
     * Sélection par fromDebutValidite;
     * 
     * @return String debutValidite - début validité;
     */
    public String getFromDebutValidite() {
        return fromDebutValidite;
    }

    /**
     * Sélection par fromFinActivite;
     * 
     * @return String finActivite - fin activité;
     */
    public String getFromFinActivite() {
        return fromFinActivite;
    }

    /**
     * Sélection par fromFinValidite;
     * 
     * @return String finValidite - fin validité;
     */
    public String getFromFinValidite() {
        return fromFinValidite;
    }

    /**
     * Sélection par forEtatDossier
     * 
     * @param newForEtatDossier
     *            String - état dossier
     */
    public void setForEtatDossier(String newForEtatDossier) {
        forEtatDossier = newForEtatDossier;
    }

    /**
     * Sélection par forIdAllocataire
     * 
     * @param newForIdAllocataire
     *            String - id allocataire
     */
    public void setForIdAllocataire(String newForIdAllocataire) {
        forIdAllocataire = newForIdAllocataire;
    }

    /**
     * Sélection par forIdCategorie
     * 
     * @param newForIdCategorie
     *            String - id catégorie
     */
    public void setForIdCategorie(String newForIdCategorie) {
        forIdCategorie = newForIdCategorie;
    }

    /**
     * Sélection par forIdDossier
     * 
     * @param newForIdDossier
     *            String - id dossier (clé primaire)
     */
    public void setForIdDossier(String newForIdDossier) {
        forIdDossier = newForIdDossier;
    }

    /**
     * Sélection par forMotifReduction
     * 
     * @param newForMotifReduction
     *            String - motif réduction
     */
    public void setForMotifReduction(String newForMotifReduction) {
        forMotifReduction = newForMotifReduction;
    }

    /**
     * Sélection par forNumeroAdressePostale
     * 
     * @param newForNumeroAdressePostale
     *            String - no adresse postale
     */
    public void setForNumeroAdressePostale(String newForNumeroAdressePostale) {
        forNumeroAdressePostale = newForNumeroAdressePostale;
    }

    /**
     * Sélection par forNumeroAffilie
     * 
     * @param newForNumeroAffilie
     *            String - no affilié (clé étrangère mappaffi)
     */
    public void setForNumeroAffilie(String newForNumeroAffilie) {
        forNumeroAffilie = newForNumeroAffilie;
    }

    /**
     * Sélection par forTauxOccupation
     * 
     * @param newForTauxOccupation
     *            String - taux d'occupation
     */
    public void setForTauxOccupation(String newForTauxOccupation) {
        forTauxOccupation = newForTauxOccupation;
    }

    /**
     * Sélection par forTauxReduction
     * 
     * @param newForTauxReduction
     *            String - taux de réduction
     */
    public void setForTauxReduction(String newForTauxReduction) {
        forTauxReduction = newForTauxReduction;
    }

    /**
     * Sélection par forTypeAlllocataireAF
     * 
     * @param newForTypeAlllocataireAF
     *            String - type allocataire AF
     */
    public void setForTypeAlllocataireAF(String newForTypeAlllocataireAF) {
        forTypeAlllocataireAF = newForTypeAlllocataireAF;
    }

    /**
     * Sélection par forTypeBonification
     * 
     * @param newForTypeBonification
     *            String - type de bonification du dossier
     */
    public void setForTypeBonification(String newForTypeBonification) {
        forTypeBonification = newForTypeBonification;
    }

    /**
     * Sélection par fromDebutActivite
     * 
     * @param newFromDebutActivite
     *            String - début activité
     */
    public void setFromDebutActivite(String newFromDebutActivite) {
        fromDebutActivite = newFromDebutActivite;
    }

    /**
     * Sélection par fromDebutValidite
     * 
     * @param newFromDebutValidite
     *            String - début validité
     */
    public void setFromDebutValidite(String newFromDebutValidite) {
        fromDebutValidite = newFromDebutValidite;
    }

    /**
     * Sélection par fromFinActivite
     * 
     * @param newFromFinActivite
     *            String - fin activité
     */
    public void setFromFinActivite(String newFromFinActivite) {
        fromFinActivite = newFromFinActivite;
    }

    /**
     * Sélection par fromFinValidite
     * 
     * @param newFromFinValidite
     *            String - fin validité
     */
    public void setFromFinValidite(String newFromFinValidite) {
        fromFinValidite = newFromFinValidite;
    }

}
