package globaz.ccvd.services;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Tue Mar 06 14:28:07 CET 2007
 */
public class ALDossier extends BEntity {

    private static final long serialVersionUID = 7422350166897286211L;

    /** Table : JAFPDOS */

    /** copie1 - copie 1 décision (ECOP1) */
    private String copie1 = new String();
    /** copie2 - copie 2 décision (ECOP2) */
    private String copie2 = new String();
    /** copie3 - copie 3 décision (ECOP3) */
    private String copie3 = new String();
    /** copie4 - copie 4 décision (ECOP4) */
    private String copie4 = new String();
    /** copie5 - copie 5 décision (ECOP5) */
    private String copie5 = new String();
    /** debutActivite - début activité (EDACT) */
    private String debutActivite = new String();
    /** debutValidite - début validité (EDVAL) */
    private String debutValidite = new String();
    /** etatDossier - état dossier (EETAT) */
    private String etatDossier = new String();
    /** finActivite - fin activité (EFACT) */
    private String finActivite = new String();
    /** finValidite - fin validité (EFVAL) */
    private String finValidite = new String();
    /** idAllocataire - id allocataire (EIDP) */
    private String idAllocataire = new String();
    /** idCategorie - id catégorie (EIDC) */
    private String idCategorie = new String();
    /** idDossier - id dossier (clé primaire) (EID) */
    private String idDossier = new String();
    /** impressionDecision - impression décision (EIMDEC) */
    private String impressionDecision = new String();
    /** motifReduction - motif réduction (EMOTRD) */
    private String motifReduction = new String();
    /** nbrJourDebut - nombre de jour début (ENBJD) */
    private String nbrJourDebut = new String();
    /** nbrJourFin - nombre de jour de fin (ENBJF) */
    private String nbrJourFin = new String();
    /** NumeroAdressePostale - no adresse postale (EADRP) */
    private String NumeroAdressePostale = new String();
    /** numeroAffilie - no affilié (clé étrangère mappaffi) (EIDAF) */
    private String numeroAffilie = new String();
    /** retenueImpot - retenue impot (EIMPOT) */
    private String retenueImpot = new String();
    /** tauxOccupation - taux d'occupation (ETOCC) */
    private String tauxOccupation = new String();
    /** tauxReduction - taux de réduction (EREDUC) */
    private String tauxReduction = new String();
    /** typeAlllocataireAF - type allocataire AF (ETYPAL) */
    private String typeAlllocataireAF = new String();
    /** typeBonification - type de bonification du dossier (EBONIF) */
    private String typeBonification = new String();
    /** uniteCalcul - unité de calcul (EUNICA) */
    private String uniteCalcul = new String();
    /** utilisateurDossier - utilisateur dossier (EUTIL) */
    private String utilisateurDossier = new String();

    /**
     * Constructeur de la classe ALDossier
     */
    public ALDossier() {
        super();
    }

    /**
     * Méthode qui incrémente la clé primaire
     * 
     * @param transaction
     *            BTransaction transaction
     * @throws Exception
     *             exception
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDossier(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table JAFPDOS
     * 
     * @return String JAFPDOS
     */
    @Override
    protected String _getTableName() {
        return IALDossierDefTable.TABLE_NAME;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        NumeroAdressePostale = statement.dbReadNumeric(IALDossierDefTable._NUMERO_ADRESSE_POSTALE);
        typeBonification = statement.dbReadString(IALDossierDefTable.TYPE_BONIFICATION);
        copie1 = statement.dbReadString(IALDossierDefTable.COPIE1);
        copie2 = statement.dbReadString(IALDossierDefTable.COPIE2);
        copie3 = statement.dbReadString(IALDossierDefTable.COPIE3);
        copie4 = statement.dbReadString(IALDossierDefTable.COPIE4);
        copie5 = statement.dbReadString(IALDossierDefTable.COPIE5);
        debutActivite = statement.dbReadDateAMJ(IALDossierDefTable.DEBUT_ACTIVITE);
        debutValidite = statement.dbReadDateAMJ(IALDossierDefTable.DEBUT_VALIDITE);
        etatDossier = statement.dbReadString(IALDossierDefTable.ETAT_DOSSIER);
        finActivite = statement.dbReadDateAMJ(IALDossierDefTable.FIN_ACTIVITE);
        finValidite = statement.dbReadDateAMJ(IALDossierDefTable.FIN_VALIDITE);
        idDossier = statement.dbReadNumeric(IALDossierDefTable.ID_DOSSIER);
        numeroAffilie = statement.dbReadNumeric(IALDossierDefTable.NUMERO_AFFILIE);
        idCategorie = statement.dbReadString(IALDossierDefTable.ID_CATEGORIE);
        idAllocataire = statement.dbReadNumeric(IALDossierDefTable.ID_ALLOCATAIRE);
        impressionDecision = statement.dbReadString(IALDossierDefTable.IMPRESSION_DECISION);
        retenueImpot = statement.dbReadString(IALDossierDefTable.RETENUE_IMPOT);
        motifReduction = statement.dbReadString(IALDossierDefTable.MOTIF_REDUCTION);
        nbrJourDebut = statement.dbReadNumeric(IALDossierDefTable.NBR_JOUR_DEBUT);
        nbrJourFin = statement.dbReadNumeric(IALDossierDefTable.NBR_JOUR_FIN);
        tauxReduction = statement.dbReadNumeric(IALDossierDefTable.TAUX_REDUCTION);
        tauxOccupation = statement.dbReadNumeric(IALDossierDefTable.TAUX_OCCUPATION);
        typeAlllocataireAF = statement.dbReadString(IALDossierDefTable.TYPE_ALLLOCATAIRE_AF);
        uniteCalcul = statement.dbReadString(IALDossierDefTable.UNITE_CALCUL);
        utilisateurDossier = statement.dbReadString(IALDossierDefTable.UTILISATEUR_DOSSIER);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * Indique la clé principale LDossier() du fichier JAFPDOS
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écriture de la clé
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IALDossierDefTable.ID_DOSSIER,
                _dbWriteNumeric(statement.getTransaction(), getIdDossier(), "idDossier - id dossier (clé primaire)"));
    }

    /**
     * Ecriture des propriétés
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écritrues des propriétés
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(
                IALDossierDefTable._NUMERO_ADRESSE_POSTALE,
                _dbWriteNumeric(statement.getTransaction(), getNumeroAdressePostale(),
                        "NumeroAdressePostale - no adresse postale"));
        statement.writeField(
                IALDossierDefTable.TYPE_BONIFICATION,
                _dbWriteString(statement.getTransaction(), getTypeBonification(),
                        "typeBonification - type de bonification du dossier"));
        statement.writeField(IALDossierDefTable.COPIE1,
                _dbWriteString(statement.getTransaction(), getCopie1(), "copie1 - copie 1 décision"));
        statement.writeField(IALDossierDefTable.COPIE2,
                _dbWriteString(statement.getTransaction(), getCopie2(), "copie2 - copie 2 décision"));
        statement.writeField(IALDossierDefTable.COPIE3,
                _dbWriteString(statement.getTransaction(), getCopie3(), "copie3 - copie 3 décision"));
        statement.writeField(IALDossierDefTable.COPIE4,
                _dbWriteString(statement.getTransaction(), getCopie4(), "copie4 - copie 4 décision"));
        statement.writeField(IALDossierDefTable.COPIE5,
                _dbWriteString(statement.getTransaction(), getCopie5(), "copie5 - copie 5 décision"));
        statement.writeField(IALDossierDefTable.DEBUT_ACTIVITE,
                _dbWriteDateAMJ(statement.getTransaction(), getDebutActivite(), "debutActivite - début activité"));
        statement.writeField(IALDossierDefTable.DEBUT_VALIDITE,
                _dbWriteDateAMJ(statement.getTransaction(), getDebutValidite(), "debutValidite - début validité"));
        statement.writeField(IALDossierDefTable.ETAT_DOSSIER,
                _dbWriteString(statement.getTransaction(), getEtatDossier(), "etatDossier - état dossier"));
        statement.writeField(IALDossierDefTable.FIN_ACTIVITE,
                _dbWriteDateAMJ(statement.getTransaction(), getFinActivite(), "finActivite - fin activité"));
        statement.writeField(IALDossierDefTable.FIN_VALIDITE,
                _dbWriteDateAMJ(statement.getTransaction(), getFinValidite(), "finValidite - fin validité"));
        statement.writeField(IALDossierDefTable.ID_DOSSIER,
                _dbWriteNumeric(statement.getTransaction(), getIdDossier(), "idDossier - id dossier (clé primaire)"));
        statement.writeField(
                IALDossierDefTable.NUMERO_AFFILIE,
                _dbWriteNumeric(statement.getTransaction(), getNumeroAffilie(),
                        "numeroAffilie - no affilié (clé étrangère mappaffi)"));
        statement.writeField(IALDossierDefTable.ID_CATEGORIE,
                _dbWriteString(statement.getTransaction(), getIdCategorie(), "idCategorie - id catégorie"));
        statement.writeField(IALDossierDefTable.ID_ALLOCATAIRE,
                _dbWriteNumeric(statement.getTransaction(), getIdAllocataire(), "idAllocataire - id allocataire"));
        statement.writeField(
                IALDossierDefTable.IMPRESSION_DECISION,
                _dbWriteString(statement.getTransaction(), getImpressionDecision(),
                        "impressionDecision - impression décision"));
        statement.writeField(IALDossierDefTable.RETENUE_IMPOT,
                _dbWriteString(statement.getTransaction(), getRetenueImpot(), "retenueImpot - retenue impot"));
        statement.writeField(IALDossierDefTable.MOTIF_REDUCTION,
                _dbWriteString(statement.getTransaction(), getMotifReduction(), "motifReduction - motif réduction"));
        statement.writeField(IALDossierDefTable.NBR_JOUR_DEBUT,
                _dbWriteNumeric(statement.getTransaction(), getNbrJourDebut(), "nbrJourDebut - nombre de jour début"));
        statement.writeField(IALDossierDefTable.NBR_JOUR_FIN,
                _dbWriteNumeric(statement.getTransaction(), getNbrJourFin(), "nbrJourFin - nombre de jour de fin"));
        statement.writeField(IALDossierDefTable.TAUX_REDUCTION,
                _dbWriteNumeric(statement.getTransaction(), getTauxReduction(), "tauxReduction - taux de réduction"));
        statement.writeField(IALDossierDefTable.TAUX_OCCUPATION,
                _dbWriteNumeric(statement.getTransaction(), getTauxOccupation(), "tauxOccupation - taux d'occupation"));
        statement.writeField(
                IALDossierDefTable.TYPE_ALLLOCATAIRE_AF,
                _dbWriteString(statement.getTransaction(), getTypeAlllocataireAF(),
                        "typeAlllocataireAF - type allocataire AF"));
        statement.writeField(IALDossierDefTable.UNITE_CALCUL,
                _dbWriteString(statement.getTransaction(), getUniteCalcul(), "uniteCalcul - unité de calcul"));
        statement.writeField(
                IALDossierDefTable.UTILISATEUR_DOSSIER,
                _dbWriteString(statement.getTransaction(), getUtilisateurDossier(),
                        "utilisateurDossier - utilisateur dossier"));
    }

    /**
     * Renvoie la zone copie1 - copie 1 décision (ECOP1)
     * 
     * @return String copie1 - copie 1 décision
     */
    public String getCopie1() {
        return copie1;
    }

    /**
     * Renvoie la zone copie2 - copie 2 décision (ECOP2)
     * 
     * @return String copie2 - copie 2 décision
     */
    public String getCopie2() {
        return copie2;
    }

    /**
     * Renvoie la zone copie3 - copie 3 décision (ECOP3)
     * 
     * @return String copie3 - copie 3 décision
     */
    public String getCopie3() {
        return copie3;
    }

    /**
     * Renvoie la zone copie4 - copie 4 décision (ECOP4)
     * 
     * @return String copie4 - copie 4 décision
     */
    public String getCopie4() {
        return copie4;
    }

    /**
     * Renvoie la zone copie5 - copie 5 décision (ECOP5)
     * 
     * @return String copie5 - copie 5 décision
     */
    public String getCopie5() {
        return copie5;
    }

    /**
     * Renvoie la zone debutActivite - début activité (EDACT)
     * 
     * @return String debutActivite - début activité
     */
    public String getDebutActivite() {
        return debutActivite;
    }

    /**
     * Renvoie la zone debutValidite - début validité (EDVAL)
     * 
     * @return String debutValidite - début validité
     */
    public String getDebutValidite() {
        return debutValidite;
    }

    /**
     * Renvoie la zone etatDossier - état dossier (EETAT)
     * 
     * @return String etatDossier - état dossier
     */
    public String getEtatDossier() {
        return etatDossier;
    }

    /**
     * Renvoie la zone finActivite - fin activité (EFACT)
     * 
     * @return String finActivite - fin activité
     */
    public String getFinActivite() {
        return finActivite;
    }

    /**
     * Renvoie la zone finValidite - fin validité (EFVAL)
     * 
     * @return String finValidite - fin validité
     */
    public String getFinValidite() {
        return finValidite;
    }

    /**
     * Renvoie la zone idAllocataire - id allocataire (EIDP)
     * 
     * @return String idAllocataire - id allocataire
     */
    public String getIdAllocataire() {
        return idAllocataire;
    }

    /**
     * Renvoie la zone idCategorie - id catégorie (EIDC)
     * 
     * @return String idCategorie - id catégorie
     */
    public String getIdCategorie() {
        return idCategorie;
    }

    /**
     * Renvoie la zone idDossier - id dossier (clé primaire) (EID)
     * 
     * @return String idDossier - id dossier (clé primaire)
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * Renvoie la zone impressionDecision - impression décision (EIMDEC)
     * 
     * @return String impressionDecision - impression décision
     */
    public String getImpressionDecision() {
        return impressionDecision;
    }

    /**
     * Renvoie la zone motifReduction - motif réduction (EMOTRD)
     * 
     * @return String motifReduction - motif réduction
     */
    public String getMotifReduction() {
        return motifReduction;
    }

    /**
     * Renvoie la zone nbrJourDebut - nombre de jour début (ENBJD)
     * 
     * @return String nbrJourDebut - nombre de jour début
     */
    public String getNbrJourDebut() {
        return nbrJourDebut;
    }

    /**
     * Renvoie la zone nbrJourFin - nombre de jour de fin (ENBJF)
     * 
     * @return String nbrJourFin - nombre de jour de fin
     */
    public String getNbrJourFin() {
        return nbrJourFin;
    }

    /**
     * Renvoie la zone NumeroAdressePostale - no adresse postale (EADRP)
     * 
     * @return String NumeroAdressePostale - no adresse postale
     */
    public String getNumeroAdressePostale() {
        return NumeroAdressePostale;
    }

    /**
     * Renvoie la zone numeroAffilie - no affilié (clé étrangère mappaffi) (EIDAF)
     * 
     * @return String numeroAffilie - no affilié (clé étrangère mappaffi)
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * Renvoie la zone retenueImpot - retenue impot (EIMPOT)
     * 
     * @return String retenueImpot - retenue impot
     */
    public String getRetenueImpot() {
        return retenueImpot;
    }

    /**
     * Renvoie la zone tauxOccupation - taux d'occupation (ETOCC)
     * 
     * @return String tauxOccupation - taux d'occupation
     */
    public String getTauxOccupation() {
        return tauxOccupation;
    }

    /**
     * Renvoie la zone tauxReduction - taux de réduction (EREDUC)
     * 
     * @return String tauxReduction - taux de réduction
     */
    public String getTauxReduction() {
        return tauxReduction;
    }

    /**
     * Renvoie la zone typeAlllocataireAF - type allocataire AF (ETYPAL)
     * 
     * @return String typeAlllocataireAF - type allocataire AF
     */
    public String getTypeAlllocataireAF() {
        return typeAlllocataireAF;
    }

    /**
     * Renvoie la zone typeBonification - type de bonification du dossier (EBONIF)
     * 
     * @return String typeBonification - type de bonification du dossier
     */
    public String getTypeBonification() {
        return typeBonification;
    }

    /**
     * Renvoie la zone uniteCalcul - unité de calcul (EUNICA)
     * 
     * @return String uniteCalcul - unité de calcul
     */
    public String getUniteCalcul() {
        return uniteCalcul;
    }

    /**
     * Renvoie la zone utilisateurDossier - utilisateur dossier (EUTIL)
     * 
     * @return String utilisateurDossier - utilisateur dossier
     */
    public String getUtilisateurDossier() {
        return utilisateurDossier;
    }

    /**
     * La table ne contient pas de champ espion d'ou on ne vérifie pas la condition sur l'espion dans les ajouts et maj
     * de la table
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * Modifie la zone copie1 - copie 1 décision (ECOP1)
     * 
     * @param newCopie1
     *            - copie 1 décision String
     */
    public void setCopie1(String newCopie1) {
        copie1 = newCopie1;
    }

    /**
     * Modifie la zone copie2 - copie 2 décision (ECOP2)
     * 
     * @param newCopie2
     *            - copie 2 décision String
     */
    public void setCopie2(String newCopie2) {
        copie2 = newCopie2;
    }

    /**
     * Modifie la zone copie3 - copie 3 décision (ECOP3)
     * 
     * @param newCopie3
     *            - copie 3 décision String
     */
    public void setCopie3(String newCopie3) {
        copie3 = newCopie3;
    }

    /**
     * Modifie la zone copie4 - copie 4 décision (ECOP4)
     * 
     * @param newCopie4
     *            - copie 4 décision String
     */
    public void setCopie4(String newCopie4) {
        copie4 = newCopie4;
    }

    /**
     * Modifie la zone copie5 - copie 5 décision (ECOP5)
     * 
     * @param newCopie5
     *            - copie 5 décision String
     */
    public void setCopie5(String newCopie5) {
        copie5 = newCopie5;
    }

    /**
     * Modifie la zone debutActivite - début activité (EDACT)
     * 
     * @param newDebutActivite
     *            - début activité String
     */
    public void setDebutActivite(String newDebutActivite) {
        debutActivite = newDebutActivite;
    }

    /**
     * Modifie la zone debutValidite - début validité (EDVAL)
     * 
     * @param newDebutValidite
     *            - début validité String
     */
    public void setDebutValidite(String newDebutValidite) {
        debutValidite = newDebutValidite;
    }

    /**
     * Modifie la zone etatDossier - état dossier (EETAT)
     * 
     * @param newEtatDossier
     *            - état dossier String
     */
    public void setEtatDossier(String newEtatDossier) {
        etatDossier = newEtatDossier;
    }

    /**
     * Modifie la zone finActivite - fin activité (EFACT)
     * 
     * @param newFinActivite
     *            - fin activité String
     */
    public void setFinActivite(String newFinActivite) {
        finActivite = newFinActivite;
    }

    /**
     * Modifie la zone finValidite - fin validité (EFVAL)
     * 
     * @param newFinValidite
     *            - fin validité String
     */
    public void setFinValidite(String newFinValidite) {
        finValidite = newFinValidite;
    }

    /**
     * Modifie la zone idAllocataire - id allocataire (EIDP)
     * 
     * @param newIdAllocataire
     *            - id allocataire String
     */
    public void setIdAllocataire(String newIdAllocataire) {
        idAllocataire = newIdAllocataire;
    }

    /**
     * Modifie la zone idCategorie - id catégorie (EIDC)
     * 
     * @param newIdCategorie
     *            - id catégorie String
     */
    public void setIdCategorie(String newIdCategorie) {
        idCategorie = newIdCategorie;
    }

    /**
     * Modifie la zone idDossier - id dossier (clé primaire) (EID)
     * 
     * @param newIdDossier
     *            - id dossier (clé primaire) String
     */
    public void setIdDossier(String newIdDossier) {
        idDossier = newIdDossier;
    }

    /**
     * Modifie la zone impressionDecision - impression décision (EIMDEC)
     * 
     * @param newImpressionDecision
     *            - impression décision String
     */
    public void setImpressionDecision(String newImpressionDecision) {
        impressionDecision = newImpressionDecision;
    }

    /**
     * Modifie la zone motifReduction - motif réduction (EMOTRD)
     * 
     * @param newMotifReduction
     *            - motif réduction String
     */
    public void setMotifReduction(String newMotifReduction) {
        motifReduction = newMotifReduction;
    }

    /**
     * Modifie la zone nbrJourDebut - nombre de jour début (ENBJD)
     * 
     * @param newNbrJourDebut
     *            - nombre de jour début String
     */
    public void setNbrJourDebut(String newNbrJourDebut) {
        nbrJourDebut = newNbrJourDebut;
    }

    /**
     * Modifie la zone nbrJourFin - nombre de jour de fin (ENBJF)
     * 
     * @param newNbrJourFin
     *            - nombre de jour de fin String
     */
    public void setNbrJourFin(String newNbrJourFin) {
        nbrJourFin = newNbrJourFin;
    }

    /**
     * Modifie la zone NumeroAdressePostale - no adresse postale (EADRP)
     * 
     * @param newNumeroAdressePostale
     *            - no adresse postale String
     */
    public void setNumeroAdressePostale(String newNumeroAdressePostale) {
        NumeroAdressePostale = newNumeroAdressePostale;
    }

    /**
     * Modifie la zone numeroAffilie - no affilié (clé étrangère mappaffi) (EIDAF)
     * 
     * @param newNumeroAffilie
     *            - no affilié (clé étrangère mappaffi) String
     */
    public void setNumeroAffilie(String newNumeroAffilie) {
        numeroAffilie = newNumeroAffilie;
    }

    /**
     * Modifie la zone retenueImpot - retenue impot (EIMPOT)
     * 
     * @param newRetenueImpot
     *            - retenue impot String
     */
    public void setRetenueImpot(String newRetenueImpot) {
        retenueImpot = newRetenueImpot;
    }

    /**
     * Modifie la zone tauxOccupation - taux d'occupation (ETOCC)
     * 
     * @param newTauxOccupation
     *            - taux d'occupation String
     */
    public void setTauxOccupation(String newTauxOccupation) {
        tauxOccupation = newTauxOccupation;
    }

    /**
     * Modifie la zone tauxReduction - taux de réduction (EREDUC)
     * 
     * @param newTauxReduction
     *            - taux de réduction String
     */
    public void setTauxReduction(String newTauxReduction) {
        tauxReduction = newTauxReduction;
    }

    /**
     * Modifie la zone typeAlllocataireAF - type allocataire AF (ETYPAL)
     * 
     * @param newTypeAlllocataireAF
     *            - type allocataire AF String
     */
    public void setTypeAlllocataireAF(String newTypeAlllocataireAF) {
        typeAlllocataireAF = newTypeAlllocataireAF;
    }

    /**
     * Modifie la zone typeBonification - type de bonification du dossier (EBONIF)
     * 
     * @param newTypeBonification
     *            - type de bonification du dossier String
     */
    public void setTypeBonification(String newTypeBonification) {
        typeBonification = newTypeBonification;
    }

    /**
     * Modifie la zone uniteCalcul - unité de calcul (EUNICA)
     * 
     * @param newUniteCalcul
     *            - unité de calcul String
     */
    public void setUniteCalcul(String newUniteCalcul) {
        uniteCalcul = newUniteCalcul;
    }

    /**
     * Modifie la zone utilisateurDossier - utilisateur dossier (EUTIL)
     * 
     * @param newUtilisateurDossier
     *            - utilisateur dossier String
     */
    public void setUtilisateurDossier(String newUtilisateurDossier) {
        utilisateurDossier = newUtilisateurDossier;
    }
}
