package globaz.corvus.db.recap.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:45:28 CET 2007
 */
public class RERecapElement extends BEntity {
    /** Table : REELMREC */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** cas - nb cas (nb rentes) (ZRNCAS) */
    private String cas = new String();
    /** codeRecap - code r�cap (IRERecapElementCode) (ZRLCOD) */
    private String codeRecap = new String();
    /** idRecapElement - id r�cap �l�ment (pk) (ZSIELR) */
    private String idRecapElement = new String();
    /** idRecapMensuelle - id r�cap mensuelle (fk) (ZSIRM) */
    private String idRecapMensuelle = new String();
    /** montant - montant r�cap (ZRMMON) */
    private String montant = new String();

    /**
     * Constructeur de la classe RERecapElement
     */
    public RERecapElement() {
        super();
    }

    /**
     * Constructeur de la classe RERecapElement
     */
    public RERecapElement(String noElem) {
        super();
        setCodeRecap(noElem);
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
        setIdRecapElement(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table REELMREC
     * 
     * @return String REELMREC
     */
    @Override
    protected String _getTableName() {
        return IRERecapElementDefTable.TABLE_NAME;
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
        idRecapElement = statement.dbReadNumeric(IRERecapElementDefTable.ID_RECAP_ELEMENT);
        idRecapMensuelle = statement.dbReadNumeric(IRERecapElementDefTable.ID_RECAP_MENSUELLE);
        montant = statement.dbReadNumeric(IRERecapElementDefTable.MONTANT);
        codeRecap = statement.dbReadNumeric(IRERecapElementDefTable.CODE_RECAP);
        cas = statement.dbReadNumeric(IRERecapElementDefTable.CAS);
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
     * Indique la cl� principale ERecapElement() du fichier REELMREC
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�criture de la cl�
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                IRERecapElementDefTable.ID_RECAP_ELEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdRecapElement(),
                        "idRecapElement - id r�cap �l�ment (pk)"));
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
                IRERecapElementDefTable.ID_RECAP_ELEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdRecapElement(),
                        "idRecapElement - id r�cap �l�ment (pk)"));
        statement.writeField(
                IRERecapElementDefTable.ID_RECAP_MENSUELLE,
                _dbWriteNumeric(statement.getTransaction(), getIdRecapMensuelle(),
                        "idRecapMensuelle - id r�cap mensuelle (fk)"));
        statement.writeField(IRERecapElementDefTable.MONTANT,
                _dbWriteNumeric(statement.getTransaction(), getMontant(), "montant - montant r�cap"));
        statement.writeField(
                IRERecapElementDefTable.CODE_RECAP,
                _dbWriteNumeric(statement.getTransaction(), getCodeRecap(),
                        "codeRecap - code r�cap (IRERecapElementCode)"));
        statement.writeField(IRERecapElementDefTable.CAS,
                _dbWriteNumeric(statement.getTransaction(), getCas(), "cas - nb cas (nb rente)"));
    }

    /**
     * Renvoie la zone cas - nb cas (nb rente) (ZRCCAS)
     * 
     * @return cas - nb cas (nb rente) String
     */
    public String getCas() {
        if (!JadeStringUtil.isBlankOrZero(cas)) {
            return cas;
        } else {
            return "0";
        }
    }

    /**
     * Renvoie la zone codeRecap - code r�cap (IRERecapElementCode) (ZRLCOD)
     * 
     * @return String codeRecap - code r�cap (IRERecapElementCode)
     */
    public String getCodeRecap() {
        return codeRecap;
    }

    /**
     * Renvoie la zone idRecapElement - id r�cap �l�ment (pk) (ZSIELR)
     * 
     * @return String idRecapElement - id r�cap �l�ment (pk)
     */
    public String getIdRecapElement() {
        return idRecapElement;
    }

    /**
     * Renvoie la zone idRecapMensuelle - id r�cap mensuelle (fk) (ZSIRM)
     * 
     * @return String idRecapMensuelle - id r�cap mensuelle (fk)
     */
    public String getIdRecapMensuelle() {
        return idRecapMensuelle;
    }

    /**
     * Renvoie la zone montant - montant r�cap (ZRMMON)
     * 
     * @return String montant - montant r�cap
     */
    public String getMontant() {
        return montant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * Modifie la zone cas - nb cas (nb rente) (ZRCCAS)
     * 
     * @param cas
     *            - nb cas (nb rente) String
     */
    public void setCas(String cas) {
        this.cas = cas;
    }

    /**
     * Modifie la zone codeRecap - code r�cap (IRERecapElementCode) (ZRLCOD)
     * 
     * @param newCodeRecap
     *            - code r�cap (IRERecapElementCode) String
     */
    public void setCodeRecap(String newCodeRecap) {
        codeRecap = newCodeRecap;
    }

    /**
     * Modifie la zone idRecapElement - id r�cap �l�ment (pk) (ZSIELR)
     * 
     * @param newIdRecapElement
     *            - id r�cap �l�ment (pk) String
     */
    public void setIdRecapElement(String newIdRecapElement) {
        idRecapElement = newIdRecapElement;
    }

    /**
     * Modifie la zone idRecapMensuelle - id r�cap mensuelle (fk) (ZSIRM)
     * 
     * @param newIdRecapMensuelle
     *            - id r�cap mensuelle (fk) String
     */
    public void setIdRecapMensuelle(String newIdRecapMensuelle) {
        idRecapMensuelle = newIdRecapMensuelle;
    }

    /**
     * Modifie la zone montant - montant r�cap (ZRMMON)
     * 
     * @param newMontant
     *            - montant r�cap String
     */
    public void setMontant(String newMontant) {
        montant = newMontant;
    }
}
