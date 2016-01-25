package globaz.tucana.db.statistique.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.tucana.exception.fw.TUFWDeleteException;
import globaz.tucana.exception.fw.TUFWException;
import globaz.tucana.exception.fw.TUFWFindException;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Wed Jun 28 11:01:20 CEST 2006
 */
public class TUTemporaire extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** agence - libellé de l'agence (STMPAG) */
    private String agence = new String();
    /** annee - année de la statistique (STMPAN) */
    private String annee = new String();
    /** cantonCourt - code ofs du canton (STMPCC) */
    private String cantonCourt = new String();
    /** cantonLong - libellé long du canton (STMPCL) */
    private String cantonLong = new String();
    /** categorie - libellé de la catégorie (STMPCT) */
    private String categorie = new String();
    /** Table : TUSPTMP */

    private boolean clearAll = false;
    /** dateCreation - date de création (STMPDC) */
    private String dateCreation = new String();
    /** groupe - libellé du groupe catégorie (STMPGR) */
    private String groupe = new String();
    /** idTemporaire - clé primaire du fichier tusptmp (STMPID) */
    private String idTemporaire = new String();
    /** moisAlpha - moisAlpha de la statistique (STMPMA) */
    private String moisAlpha = new String();
    /** moisNume - moisNume de la statistique (STMPMO) */
    private String moisNume = new String();

    /** total - montantnombre total (STMPTO) */
    private String total = new String();

    /**
     * Constructeur de la classe TUTemporaire
     */
    public TUTemporaire() {
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
        setIdTemporaire(this._incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table TUSPTMP
     * 
     * @return String TUSPTMP
     */
    @Override
    protected String _getTableName() {
        return ITUTemporaireDefTable.TABLE_NAME;
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
        agence = statement.dbReadString(ITUTemporaireDefTable.AGENCE);
        annee = statement.dbReadNumeric(ITUTemporaireDefTable.ANNEE);
        cantonCourt = statement.dbReadString(ITUTemporaireDefTable.CANTON_COURT);
        cantonLong = statement.dbReadString(ITUTemporaireDefTable.CANTON_LONG);
        categorie = statement.dbReadString(ITUTemporaireDefTable.CATEGORIE);
        dateCreation = statement.dbReadDateAMJ(ITUTemporaireDefTable.DATE_CREATION);
        groupe = statement.dbReadString(ITUTemporaireDefTable.GROUPE);
        idTemporaire = statement.dbReadNumeric(ITUTemporaireDefTable.ID_TEMPORAIRE);
        moisNume = statement.dbReadNumeric(ITUTemporaireDefTable.MOIS_NUME);
        moisAlpha = statement.dbReadString(ITUTemporaireDefTable.MOIS_ALPHA);
        total = statement.dbReadNumeric(ITUTemporaireDefTable.TOTAL);
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
     * Indique la clé principale UTemporaire() du fichier TUSPTMP
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écriture de la clé
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ITUTemporaireDefTable.ID_TEMPORAIRE, this._dbWriteNumeric(statement.getTransaction(),
                getIdTemporaire(), "idTemporaire - clé primaire du fichier tusptmp"));
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
        statement.writeField(ITUTemporaireDefTable.AGENCE,
                this._dbWriteString(statement.getTransaction(), getAgence(), "agence - libellé de l'agence"));
        statement.writeField(ITUTemporaireDefTable.ANNEE,
                this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee - année de la statistique"));
        statement.writeField(ITUTemporaireDefTable.CANTON_COURT,
                this._dbWriteString(statement.getTransaction(), getCantonCourt(), "cantonCourt - code ofs du canton"));
        statement
                .writeField(ITUTemporaireDefTable.CANTON_LONG, this._dbWriteString(statement.getTransaction(),
                        getCantonLong(), "cantonLong - libellé long du canton"));
        statement.writeField(ITUTemporaireDefTable.CATEGORIE,
                this._dbWriteString(statement.getTransaction(), getCategorie(), "categorie - libellé de la catégorie"));
        statement.writeField(ITUTemporaireDefTable.DATE_CREATION,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation - date de création"));
        statement.writeField(ITUTemporaireDefTable.GROUPE,
                this._dbWriteString(statement.getTransaction(), getGroupe(), "groupe - libellé du groupe catégorie"));
        statement.writeField(ITUTemporaireDefTable.ID_TEMPORAIRE, this._dbWriteNumeric(statement.getTransaction(),
                getIdTemporaire(), "idTemporaire - clé primaire du fichier tusptmp"));
        statement.writeField(ITUTemporaireDefTable.MOIS_NUME, this._dbWriteNumeric(statement.getTransaction(),
                getMoisNume(), "moisNume - moisNume de la statistique"));
        statement.writeField(ITUTemporaireDefTable.MOIS_ALPHA, this._dbWriteString(statement.getTransaction(),
                getMoisAlpha(), "moisAlpha - moisAlpha de la statistique"));
        statement.writeField(ITUTemporaireDefTable.TOTAL,
                this._dbWriteNumeric(statement.getTransaction(), getTotal(), "total - montantnombre total"));
    }

    /**
     * Permet de supprimer tous les enregistrements du fichier temporaire
     * 
     * @param transaction
     */
    public void clear(BTransaction transaction) throws TUFWException {

        // TUTemporaire entity = new TUTemporaire();
        // entity.setSession(transaction.getSession());
        // entity.setClearAll(true);
        // try {
        // entity.delete(transaction);
        // } catch (Exception e1) {
        // throw new
        // TUFWDeleteException("TUTemporaire.clear() : problème lors de l'effacement du fichier",
        // e1);
        // }

        TUTemporaireManager manager = new TUTemporaireManager();
        TUTemporaire entity = null;
        manager.setSession(transaction.getSession());
        try {
            manager.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new TUFWFindException("TUTemporaire.clear() : problème lors de l'effacement du fichier",
                    manager.getCurrentSqlQuery(), e);
        }
        for (int i = 0; i < manager.size(); i++) {
            entity = (TUTemporaire) manager.getEntity(i);
            try {
                entity.delete(transaction);
            } catch (Exception e1) {
                throw new TUFWDeleteException("TUTemporaire.clear() : problème lors de l'effacement du fichier", e1);
            }
        }
    }

    /**
     * Renvoie la zone agence - libellé de l'agence (STMPAG)
     * 
     * @return String agence - libellé de l'agence
     */
    public String getAgence() {
        return agence;
    }

    /**
     * Renvoie la zone annee - année de la statistique (STMPAN)
     * 
     * @return String annee - année de la statistique
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Renvoie la zone cantonCourt - code ofs du canton (STMPCC)
     * 
     * @return String cantonCourt - code ofs du canton
     */
    public String getCantonCourt() {
        return cantonCourt;
    }

    /**
     * Renvoie la zone cantonLong - libellé long du canton (STMPCL)
     * 
     * @return String cantonLong - libellé long du canton
     */
    public String getCantonLong() {
        return cantonLong;
    }

    /**
     * Renvoie la zone categorie - libellé de la catégorie (STMPCT)
     * 
     * @return String categorie - libellé de la catégorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Renvoie la zone dateCreation - date de création (STMPDC)
     * 
     * @return String dateCreation - date de création
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * Renvoie la zone groupe - libellé du groupe catégorie (STMPGR)
     * 
     * @return String groupe - libellé du groupe catégorie
     */
    public String getGroupe() {
        return groupe;
    }

    /**
     * Renvoie la zone idTemporaire - clé primaire du fichier tusptmp (STMPID)
     * 
     * @return String idTemporaire - clé primaire du fichier tusptmp
     */
    public String getIdTemporaire() {
        return idTemporaire;
    }

    /**
     * Renvoie la zone moisAlpha - moisAlpha de la statistique (STMPMA)
     * 
     * @return String moisAlpha - moisAlpha de la statistique
     */
    public String getMoisAlpha() {
        return moisAlpha;
    }

    /**
     * Renvoie la zone moisNume - moisNume de la statistique (STMPMO)
     * 
     * @return String moisNume - moisNume de la statistique
     */
    public String getMoisNume() {
        return moisNume;
    }

    @Override
    protected String getSqlDelete(BStatement statement) {
        if (clearAll) {
            try {
                if ((!statement.getTransaction().hasErrors()) && (!statement.getTransaction().hasWarnings())) {
                    // construction de l'instruction SQL
                    StringBuffer sqlBuffer = new StringBuffer("DELETE FROM ");
                    sqlBuffer.append(_getCollection());
                    sqlBuffer.append(_getTableName());
                    return sqlBuffer.toString();
                } else {
                    return null;
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(), e.toString());
                return null;
            }
        } else {
            return super.getSqlDelete(statement);
        }
    }

    /**
     * Renvoie la zone total - montantnombre total (STMPTO)
     * 
     * @return String total - montantnombre total
     */
    public String getTotal() {
        return total;
    }

    /**
     * Renvoie true si la totalité de la table doit être effacée
     * 
     * @return
     */
    public boolean isClearAll() {
        return clearAll;
    }

    /**
     * Modifie la zone agence - libellé de l'agence (STMPAG)
     * 
     * @param newAgence
     *            - libellé de l'agence String
     */
    public void setAgence(String newAgence) {
        agence = newAgence;
    }

    /**
     * Modifie la zone annee - année de la statistique (STMPAN)
     * 
     * @param newAnnee
     *            - année de la statistique String
     */
    public void setAnnee(String newAnnee) {
        annee = newAnnee;
    }

    /**
     * Modifie la zone cantonCourt - code ofs du canton (STMPCC)
     * 
     * @param newCantonCourt
     *            - code ofs du canton String
     */
    public void setCantonCourt(String newCantonCourt) {
        cantonCourt = newCantonCourt;
    }

    /**
     * Modifie la zone cantonLong - libellé long du canton (STMPCL)
     * 
     * @param newCantonLong
     *            - libellé long du canton String
     */
    public void setCantonLong(String newCantonLong) {
        cantonLong = newCantonLong;
    }

    /**
     * Modifie la zone categorie - libellé de la catégorie (STMPCT)
     * 
     * @param newCategorie
     *            - libellé de la catégorie String
     */
    public void setCategorie(String newCategorie) {
        categorie = newCategorie;
    }

    /**
     * Mettre à true si l'on désire supprimer l'ensemble des enregistrements de la table
     * 
     * @param b
     */
    public void setClearAll(boolean b) {
        clearAll = b;
    }

    /**
     * Modifie la zone dateCreation - date de création (STMPDC)
     * 
     * @param newDateCreation
     *            - date de création String
     */
    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    /**
     * Modifie la zone groupe - libellé du groupe catégorie (STMPGR)
     * 
     * @param newGroupe
     *            - libellé du groupe catégorie String
     */
    public void setGroupe(String newGroupe) {
        groupe = newGroupe;
    }

    /**
     * Modifie la zone idTemporaire - clé primaire du fichier tusptmp (STMPID)
     * 
     * @param newIdTemporaire
     *            - clé primaire du fichier tusptmp String
     */
    public void setIdTemporaire(String newIdTemporaire) {
        idTemporaire = newIdTemporaire;
    }

    /**
     * Modifie la zone moisAlpha - moisAlpha de la statistique (STMPMA)
     * 
     * @param newMoisAlpha
     *            - moisAlpha de la statistique String
     */
    public void setMoisAlpha(String newMoisAlpha) {
        moisAlpha = newMoisAlpha;
    }

    /**
     * Modifie la zone moisNume - moisNume de la statistique (STMPMO)
     * 
     * @param newMoisNume
     *            - moisNume de la statistique String
     */
    public void setMoisNume(String newMoisNume) {
        moisNume = newMoisNume;
    }

    /**
     * Modifie la zone total - montantnombre total (STMPTO)
     * 
     * @param newTotal
     *            - montantnombre total String
     */
    public void setTotal(String newTotal) {
        total = newTotal;
    }

}
