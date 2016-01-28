package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri May 05 15:07:58 CEST 2006
 */
public class TUNoPassage extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int KEY_ALTERNATE_BOUCLEMENT = 1;
    /** csApplication - cs application tu_appli (CSAPPL) */
    private String csApplication = new String();
    /** Table : TUBPNP */
    /** idBouclement - cl� primaire du fichier bouclement (BBOUID) */
    private String idBouclement = new String();
    /**
     * idNoPassage - repr�sentera la cl� primaire du fichier une fois g�n�r�e (BPNPID)
     */
    private String idNoPassage = new String();
    /** noPassage - num�ro de passage (BPNPNP) */
    private String noPassage = new String();

    /**
     * Constructeur de la classe TUNoPassage
     */
    public TUNoPassage() {
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
        setIdNoPassage(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table TUBPNP
     * 
     * @return String TUBPNP
     */
    @Override
    protected String _getTableName() {
        return ITUNoPassageDefTable.TABLE_NAME;
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
        idBouclement = statement.dbReadNumeric(ITUNoPassageDefTable.ID_BOUCLEMENT);
        idNoPassage = statement.dbReadNumeric(ITUNoPassageDefTable.ID_NO_PASSAGE);
        noPassage = statement.dbReadNumeric(ITUNoPassageDefTable.NO_PASSAGE);
        csApplication = statement.dbReadNumeric(ITUNoPassageDefTable.CS_APPLICATION);
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case 1:
                // Cl� altern�e num�ro 1 : idBouclement, idPassage
                statement.writeKey(ITUNoPassageDefTable.ID_BOUCLEMENT,
                        _dbWriteNumeric(statement.getTransaction(), getIdBouclement(), ""));
                statement.writeKey(ITUNoPassageDefTable.CS_APPLICATION,
                        _dbWriteNumeric(statement.getTransaction(), getCsApplication(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * Indique la cl� principale UNoPassage() du fichier TUBPNP
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�criture de la cl�
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                ITUNoPassageDefTable.ID_NO_PASSAGE,
                _dbWriteNumeric(statement.getTransaction(), getIdNoPassage(),
                        "idNoPassage - repr�sentera la cl� primaire du fichier une fois g�n�r�e"));
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
                ITUNoPassageDefTable.ID_BOUCLEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(),
                        "idBouclement - cl� primaire du fichier bouclement"));
        statement.writeField(
                ITUNoPassageDefTable.ID_NO_PASSAGE,
                _dbWriteNumeric(statement.getTransaction(), getIdNoPassage(),
                        "idNoPassage - repr�sentera la cl� primaire du fichier une fois g�n�r�e"));
        statement.writeField(ITUNoPassageDefTable.NO_PASSAGE,
                _dbWriteNumeric(statement.getTransaction(), getNoPassage(), "noPassage - num�ro de passage"));
        statement.writeField(
                ITUNoPassageDefTable.CS_APPLICATION,
                _dbWriteNumeric(statement.getTransaction(), getCsApplication(),
                        "csApplication - cs application tu_appli"));
    }

    /**
     * Renvoie la zone csApplication - cs application tu_appli (CSAPPL)
     * 
     * @return String csApplication - cs application tu_appli
     */
    public String getCsApplication() {
        return csApplication;
    }

    /**
     * Renvoie la zone idBouclement - cl� primaire du fichier bouclement (BBOUID)
     * 
     * @return String idBouclement - cl� primaire du fichier bouclement
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Renvoie la zone idNoPassage - repr�sentera la cl� primaire du fichier une fois g�n�r�e (BPNPID)
     * 
     * @return String idNoPassage - repr�sentera la cl� primaire du fichier une fois g�n�r�e
     */
    public String getIdNoPassage() {
        return idNoPassage;
    }

    /**
     * Renvoie la zone noPassage - num�ro de passage (BPNPNP)
     * 
     * @return String noPassage - num�ro de passage
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * Modifie la zone csApplication - cs application tu_appli (CSAPPL)
     * 
     * @param newCsApplication
     *            - cs application tu_appli String
     */
    public void setCsApplication(String newCsApplication) {
        csApplication = newCsApplication;
    }

    /**
     * Modifie la zone idBouclement - cl� primaire du fichier bouclement (BBOUID)
     * 
     * @param newIdBouclement
     *            - cl� primaire du fichier bouclement String
     */
    public void setIdBouclement(String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    /**
     * Modifie la zone idNoPassage - repr�sentera la cl� primaire du fichier une fois g�n�r�e (BPNPID)
     * 
     * @param newIdNoPassage
     *            - repr�sentera la cl� primaire du fichier une fois g�n�r�e String
     */
    public void setIdNoPassage(String newIdNoPassage) {
        idNoPassage = newIdNoPassage;
    }

    /**
     * Modifie la zone noPassage - num�ro de passage (BPNPNP)
     * 
     * @param newNoPassage
     *            - num�ro de passage String
     */
    public void setNoPassage(String newNoPassage) {
        noPassage = newNoPassage;
    }
}
