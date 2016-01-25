/*
 * Cr�� le 6 mars 07
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.inscriptions.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author JMC
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CIDeclarationParametrage extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** KOTCHA */
    private String champ = "";
    /** KOLDEB */
    private String debut = "";
    /** KOID */
    private String declarationParamatrageId = "";
    /** KOLFIN */
    private String fin = "";
    /** KOTDEC */
    private String typeImport = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        // TODO Raccord de m�thode auto-g�n�r�
        return "CIDECLP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        declarationParamatrageId = statement.dbReadNumeric("KOID");
        typeImport = statement.dbReadNumeric("KOTDEC");
        champ = statement.dbReadNumeric("KOTCHA");
        debut = statement.dbReadString("KOLDEB");
        fin = statement.dbReadString("KOLFIN");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Raccord de m�thode auto-g�n�r�

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KOID", _dbWriteNumeric(statement.getTransaction(), declarationParamatrageId, ""));

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Raccord de m�thode auto-g�n�r�

    }

    /**
     * @return
     */
    public String getChamp() {
        return champ;
    }

    /**
     * @return
     */
    public String getDebut() {
        return debut;
    }

    /**
     * @return
     */
    public String getDeclarationParamatrageId() {
        return declarationParamatrageId;
    }

    /**
     * @return
     */
    public String getFin() {
        return fin;
    }

    /**
     * @return
     */
    public String getTypeImport() {
        return typeImport;
    }

    /**
     * @param string
     */
    public void setChamp(String string) {
        champ = string;
    }

    /**
     * @param string
     */
    public void setDebut(String string) {
        debut = string;
    }

    /**
     * @param string
     */
    public void setDeclarationParamatrageId(String string) {
        declarationParamatrageId = string;
    }

    /**
     * @param string
     */
    public void setFin(String string) {
        fin = string;
    }

    /**
     * @param string
     */
    public void setTypeImport(String string) {
        typeImport = string;
    }

}
