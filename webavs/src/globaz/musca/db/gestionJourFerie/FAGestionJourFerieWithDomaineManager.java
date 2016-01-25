package globaz.musca.db.gestionJourFerie;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author MMO
 * @since 3 aout. 2010
 */
public class FAGestionJourFerieWithDomaineManager extends FAGestionJourFerieManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Attributs
     */
    private String inIdDomaine = "";

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append(super._getFields(statement));
        sqlFields.append(", ");
        sqlFields.append(FAGestionJourFerieDomaine.F_ID_DOMAINE);

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(super._getFrom(statement));

        sqlFrom.append(FAGestionJourFerieManager.LEFT_JOIN);
        sqlFrom.append(_getCollection() + FAGestionJourFerieDomaine.TABLE_NAME);
        sqlFrom.append(FAGestionJourFerieManager.ON);
        sqlFrom.append(_getCollection() + FAGestionJourFerie.TABLE_NAME + "." + FAGestionJourFerie.F_ID_JOUR + "="
                + _getCollection() + FAGestionJourFerieDomaine.TABLE_NAME + "." + FAGestionJourFerieDomaine.F_ID_FERIE);

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return FAGestionJourFerie.F_DATE_JOUR;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(super._getWhere(statement));

        if (!JadeStringUtil.isBlank(getInIdDomaine())) {
            sqlAddCondition(sqlWhere, FAGestionJourFerieDomaine.F_ID_DOMAINE + " IN(" + getInIdDomaine() + ") ");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FAGestionJourFerie();
    }

    /**
     * getter
     */
    public String getInIdDomaine() {
        return inIdDomaine;
    }

    /**
     * setter
     */
    public void setInIdDomaine(String newInIdDomaine) {
        inIdDomaine = newInIdDomaine;
    }

}
