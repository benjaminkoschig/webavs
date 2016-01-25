package globaz.tucana.db.parametrage.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Classe repr�sentant les cat�gories appartenant � un groupe
 * 
 * @author fgo date de cr�ation : 22 juin 06
 * @version : version 1.0
 * 
 */
public class TUGroupeCategorie extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** csCategorie - id code syst�me (cs : tu_categ) (CSCATEG) */
    private String csCategorie = new String();
    /** csGroupeRubrique - id code syst�me (cs : tu_grrubr (CSGRRU) */
    private String csGroupeRubrique = new String();
    /** Table : TUBPGRC */

    /** csType - code syst�me type (cs : tu_tygrp) (CSTYPE) */
    private String csType = new String();
    /** idGroupeCategorie - cl� primaire du fichier TUBPGRC (BGRCID) */
    private String idGroupeCategorie = new String();

    /**
     * Constructeur de la classe TUGroupeCategorie
     */
    public TUGroupeCategorie() {
        super();
    }

    /**
     * M�thode qui incr�mente la cl� primaire
     * 
     * @param transaction
     *            BTransaction transaction
     * @throws Exception
     *             exception
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdGroupeCategorie(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table TUBPGRC
     * 
     * @return String TUBPGRC
     */
    @Override
    protected String _getTableName() {
        return ITUGroupeCategorieDefTable.TABLE_NAME;
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idGroupeCategorie = statement.dbReadNumeric(ITUGroupeCategorieDefTable.ID_GROUPE_CATEGORIE);
        csCategorie = statement.dbReadNumeric(ITUGroupeCategorieDefTable.CS_CATEGORIE);
        csGroupeRubrique = statement.dbReadNumeric(ITUGroupeCategorieDefTable.CS_GROUPE_RUBRIQUE);
        csType = statement.dbReadNumeric(ITUGroupeCategorieDefTable.CS_TYPE);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * Indique la cl� principale UGroupeCategorie() du fichier TUBPGRC
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�criture de la cl�
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                ITUGroupeCategorieDefTable.ID_GROUPE_CATEGORIE,
                _dbWriteNumeric(statement.getTransaction(), getIdGroupeCategorie(),
                        "idGroupeCategorie - cl� primaire du fichier TUBPGRC"));
    }

    /**
     * Ecriture des propri�t�s
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�critrues des propri�t�s
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(
                ITUGroupeCategorieDefTable.ID_GROUPE_CATEGORIE,
                _dbWriteNumeric(statement.getTransaction(), getIdGroupeCategorie(),
                        "idGroupeCategorie - cl� primaire du fichier TUBPGRC"));
        statement.writeField(
                ITUGroupeCategorieDefTable.CS_CATEGORIE,
                _dbWriteNumeric(statement.getTransaction(), getCsCategorie(),
                        "csCategorie - id code syst�me (cs : tu_categ)"));
        statement.writeField(
                ITUGroupeCategorieDefTable.CS_GROUPE_RUBRIQUE,
                _dbWriteNumeric(statement.getTransaction(), getCsGroupeRubrique(),
                        "csGroupeRubrique - id code syst�me (cs : tu_grrubr"));
        statement.writeField(ITUGroupeCategorieDefTable.CS_TYPE,
                _dbWriteNumeric(statement.getTransaction(), getCsType(), "csType - id code syst�me (cs : tu_tygrp"));
    }

    /**
     * Renvoie la zone csCategorie - id code syst�me (cs : tu_categ) (CSCATEG)
     * 
     * @return String csCategorie - id code syst�me (cs : tu_categ)
     */
    public String getCsCategorie() {
        return csCategorie;
    }

    /**
     * Renvoie la zone csGroupeRubrique - id code syst�me (cs : tu_grrubr (CSGRRU)
     * 
     * @return String csGroupeRubrique - id code syst�me (cs : tu_grrubr
     */
    public String getCsGroupeRubrique() {
        return csGroupeRubrique;
    }

    /**
     * Renvoie la zone csType - code syst�me type (cs : tu_tygrp) (CSTYPE)
     * 
     * @return String csType - code syst�me type (cs : tu_tygrp)
     */
    public String getCsType() {
        return csType;
    }

    /**
     * Renvoie la zone idGroupeCategorie - cl� primaire du fichier TUBPGRC (BGRCID)
     * 
     * @return String idGroupeCategorie - cl� primaire du fichier TUBPGRC
     */
    public String getIdGroupeCategorie() {
        return idGroupeCategorie;
    }

    /**
     * Modifie la zone csCategorie - id code syst�me (cs : tu_categ) (CSCATEG)
     * 
     * @param newCsCategorie
     *            - id code syst�me (cs : tu_categ) String
     */
    public void setCsCategorie(String newCsCategorie) {
        csCategorie = newCsCategorie;
    }

    /**
     * Modifie la zone csGroupeRubrique - id code syst�me (cs : tu_grrubr (CSGRRU)
     * 
     * @param newCsGroupeRubrique
     *            - id code syst�me (cs : tu_grrubr String
     */
    public void setCsGroupeRubrique(String newCsGroupeRubrique) {
        csGroupeRubrique = newCsGroupeRubrique;
    }

    /**
     * Modifie la zone csType - code syst�me type (cs : tu_tygrp) (CSTYPE)
     * 
     * @param newCsType
     *            - code syst�me type (cs : tu_tygrp) String
     */
    public void setCsType(String newCsType) {
        csType = newCsType;
    }

    /**
     * Modifie la zone idGroupeCategorie - cl� primaire du fichier TUBPGRC (BGRCID)
     * 
     * @param newIdGroupeCategorie
     *            - cl� primaire du fichier TUBPGRC String
     */
    public void setIdGroupeCategorie(String newIdGroupeCategorie) {
        idGroupeCategorie = newIdGroupeCategorie;
    }
}
