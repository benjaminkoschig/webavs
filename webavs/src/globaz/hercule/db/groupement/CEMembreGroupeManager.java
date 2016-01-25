package globaz.hercule.db.groupement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jpa
 * @since 29 juin 2010
 */
public class CEMembreGroupeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeCouvertureDifferente = "";
    private String forAnneeCouverture = "";
    private String forIdAffiliation = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return CEMembre.FIELD_IDAFFILIATION;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + CEMembre.TABLE_CEMEMP + " as membre ");
        sqlFrom.append(" left join " + _getCollection() + CEGroupe.TABLE_CEGRPP + " as groupe on groupe."
                + CEGroupe.FIELD_IDGROUPE + " = membre." + CEMembre.FIELD_IDGROUPE);

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isEmpty(getAnneeCouvertureDifferente())) {
            CEUtils.sqlAddCondition(sqlWhere, CEGroupe.FIELD_ANNEE_COUVERTURE_MINIMAL + " <> "
                    + getAnneeCouvertureDifferente());
        }

        if (!JadeStringUtil.isEmpty(getForAnneeCouverture())) {
            CEUtils.sqlAddCondition(sqlWhere, CEGroupe.FIELD_ANNEE_COUVERTURE_MINIMAL + " = " + getForAnneeCouverture());
        }

        if (!JadeStringUtil.isEmpty(getForIdAffiliation())) {
            CEUtils.sqlAddCondition(sqlWhere, "membre.MAIAFF = " + getForIdAffiliation());
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEMembreGroupe();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getAnneeCouvertureDifferente() {
        return anneeCouvertureDifferente;
    }

    public String getForAnneeCouverture() {
        return forAnneeCouverture;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAnneeCouvertureDifferente(String anneeCouvertureDifferente) {
        this.anneeCouvertureDifferente = anneeCouvertureDifferente;
    }

    public void setForAnneeCouverture(String forAnneeCouverture) {
        this.forAnneeCouverture = forAnneeCouverture;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }
}
