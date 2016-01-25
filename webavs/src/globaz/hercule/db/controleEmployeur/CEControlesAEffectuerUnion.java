package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * <p>
 * Un entity contenant toutes les informations necessaires � l'impression des contr�les � effectuer pour les contr�les
 * employeurs
 * </p>
 * <p>
 * Cet entity est un peu special, il ne peut qu'etre lu. Le seul moyen d'en obtenir une instance correcte est de passer
 * par le manager correspondant.
 * </p>
 * 
 * @author hpe
 * @since Cr�� le 14 f�vr. 07
 */

public class CEControlesAEffectuerUnion extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idAffiliation = "";

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffiliation = statement.dbReadNumeric("MAIAFF");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

}
