package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;

/**
 * 
 * @author sco
 * @since 17 Aout 2011
 */
public class DSCIForNSSReclamationManager extends BManager {

    private static final long serialVersionUID = 7191015234941013376L;
    private String forIdDeclaration;
    private String notInTypeCompte;

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer();

        CEUtils.sqlAddField(sqlFields, "KANAVS");
        CEUtils.sqlAddField(sqlFields, "KALNOM");
        CEUtils.sqlAddField(sqlFields, "KBMMON");
        CEUtils.sqlAddField(sqlFields, "KBNMOD");
        CEUtils.sqlAddField(sqlFields, "KBNMOF");
        CEUtils.sqlAddField(sqlFields, "KBTEXT");
        CEUtils.sqlAddField(sqlFields, "TENANN");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        /*
         * select * from webavs.dsindp insc join webavs.ciecrip ec on ec.kbiecr = insc.kbiecr join webavs.cindip ci on
         * ci.kaiind = ec.kaiind where kaireg = 309004 and insc.taidde = ??
         */
        return _getCollection() + "dsindp insc join " + _getCollection()
                + "ciecrip ec on ec.kbiecr = insc.kbiecr join " + _getCollection()
                + "ciindip ci on ci.kaiind = ec.kaiind";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        CEUtils.sqlAddCondition(sqlWhere, "ci.kaireg = " + CICompteIndividuel.CS_REGISTRE_PROVISOIRE);

        if (!JadeStringUtil.isBlank(getForIdDeclaration())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "insc.taidde = " + this._dbWriteNumeric(statement.getTransaction(), getForIdDeclaration()));
        }

        // Pas égal à un type de décision (provisoire, définitive...)
        if (getNotInTypeCompte().length() != 0) {
            CEUtils.sqlAddCondition(sqlWhere, "ec.kbtcpt not in (" + getNotInTypeCompte() + ")");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new DSCIForNSSReclamation();
    }

    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    public String getNotInTypeCompte() {
        return notInTypeCompte;
    }

    public void setForIdDeclaration(String forIdDeclaration) {
        this.forIdDeclaration = forIdDeclaration;
    }

    public void setNotInTypeCompte(String notInTypeCompte) {
        this.notInTypeCompte = notInTypeCompte;
    }
}
