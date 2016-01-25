/**
 * class CPDecisionsAvecMiseEnCompteManager écrit le 19/01/05 par JPA
 * 
 * class entité pour les décisions avec mise en compte
 * 
 * @author JPA
 **/
package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPLienCommentaireRemarqueType extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String emplacement = "";
    private String idCommentaire = "";
    private String idLienCommentaireRemarque = "";
    private String idRemarqueType = "";
    private String langue = "";
    private String texte = "";

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLienCommentaireRemarque = statement.dbReadNumeric("ISICRE");
        idCommentaire = statement.dbReadNumeric("IMTICO");
        idRemarqueType = statement.dbReadNumeric("IOIDRE");
        langue = statement.dbReadNumeric("IOTLAN");
        emplacement = statement.dbReadNumeric("IOTEMP");
        texte = statement.dbReadString("IOTEXT");
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

    public String getEmplacement() {
        return emplacement;
    }

    public String getIdCommentaire() {
        return idCommentaire;
    }

    public String getIdLienCommentaireRemarque() {
        return idLienCommentaireRemarque;
    }

    public String getIdRemarqueType() {
        return idRemarqueType;
    }

    public String getLangue() {
        return langue;
    }

    public String getTexte() {
        return texte;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public void setIdCommentaire(String idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public void setIdLienCommentaireRemarque(String idLienCommentaireRemarque) {
        this.idLienCommentaireRemarque = idLienCommentaireRemarque;
    }

    public void setIdRemarqueType(String idRemarqueType) {
        this.idRemarqueType = idRemarqueType;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }
}
