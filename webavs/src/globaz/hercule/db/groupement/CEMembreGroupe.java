package globaz.hercule.db.groupement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author JMC
 * @since 2 juin 2010
 */
public class CEMembreGroupe extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeCouvertureMinimal = "";
    private String idAffiliation = "";
    private String idGroupe = "";
    private String idMembre = "";
    private String libelleGroupe = "";

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMembre = statement.dbReadNumeric(CEMembre.FIELD_IDMEMBRE);
        idGroupe = statement.dbReadNumeric(CEMembre.FIELD_IDGROUPE);
        idAffiliation = statement.dbReadNumeric(CEMembre.FIELD_IDAFFILIATION);
        libelleGroupe = statement.dbReadString(CEGroupe.FIELD_LIBELLE);
        anneeCouvertureMinimal = statement.dbReadNumeric(CEGroupe.FIELD_ANNEE_COUVERTURE_MINIMAL);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CEMembre.FIELD_IDMEMBRE, _dbWriteNumeric(statement.getTransaction(), getIdMembre(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getAnneeCouvertureMinimal() {
        return anneeCouvertureMinimal;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdGroupe() {
        return idGroupe;
    }

    public String getIdMembre() {
        return idMembre;
    }

    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAnneeCouvertureMinimal(String anneeCouvertureMinimal) {
        this.anneeCouvertureMinimal = anneeCouvertureMinimal;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
    }

    public void setIdMembre(String idMembre) {
        this.idMembre = idMembre;
    }

    public void setLibelleGroupe(String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }
}
