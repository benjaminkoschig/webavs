/*
 * Cr�� le 12 f�vr. 07
 */
package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Attributions des points pour le contr�le employeur</H1>
 * 
 * @author jpa
 */
public class AFAttributionPtsCumulMasse extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String cumulMasse = "";
    private String typeAssurance = "";

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        cumulMasse = statement.dbReadString("CUMULMASSE");
        typeAssurance = statement.dbReadNumeric("MBTTYP");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getCumulMasse() {
        return cumulMasse;
    }

    public String getTypeAssurance() {
        return typeAssurance;
    }

    public void setCumulMasse(String cumulMasse) {
        this.cumulMasse = cumulMasse;
    }

    public void setTypeAssurance(String typeAssurance) {
        this.typeAssurance = typeAssurance;
    }
}
