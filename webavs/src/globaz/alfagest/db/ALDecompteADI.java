/*
 * Créé le 13 juin 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author jer
 * 
 *         Classe qui permet de récupérer un décompte ADI
 */
public class ALDecompteADI extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDecompte = null;
    private String idDossier = null;
    private String etatDecompte = null;
    private String idPrestationADI = null;

    @Override
    protected String _getTableName() {
        return "JAFPADD";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDecompte = statement.dbReadNumeric("A2ID");
        idDossier = statement.dbReadNumeric("A2IDD");
        idPrestationADI = statement.dbReadNumeric("A2IDE");
        etatDecompte = statement.dbReadString("A2ETAT");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("A2ID", _dbWriteNumeric(statement.getTransaction(), idDecompte, ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("A2ID", _dbWriteNumeric(statement.getTransaction(), idDecompte, ""));
        statement.writeField("A2IDD", _dbWriteNumeric(statement.getTransaction(), idDossier, ""));
        statement.writeField("A2IDE", _dbWriteNumeric(statement.getTransaction(), idPrestationADI, ""));
        statement.writeField("A2ETAT", _dbWriteString(statement.getTransaction(), etatDecompte, ""));

    }

    /**
     * @return
     */
    public String getEtatDecompte() {
        return etatDecompte;
    }

    /**
     * @return
     */
    public String getIdDecompte() {
        return idDecompte;
    }

    /**
     * @return
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return
     */
    public String getIdPrestationADI() {
        return idPrestationADI;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * @param string
     */
    public void setEtatDecompte(String string) {
        etatDecompte = string;
    }

    /**
     * @param string
     */
    public void setIdDecompte(String string) {
        idDecompte = string;
    }

    /**
     * @param string
     */
    public void setIdDossier(String string) {
        idDossier = string;
    }

    /**
     * @param string
     */
    public void setIdPrestationADI(String string) {
        idPrestationADI = string;
    }

}
