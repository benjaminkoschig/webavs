package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.util.AFUtil;
import globaz.pavo.db.compte.CIEcriture;

public class AFLineCiManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee;
    private String forIdAffilie;
    private String forIdCompteIndividuel;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer("");

        AFUtil.sqlAddField(sqlFields, "KBMMON");
        AFUtil.sqlAddField(sqlFields, "KBNMOD");
        AFUtil.sqlAddField(sqlFields, "KBNMOF");
        AFUtil.sqlAddField(sqlFields, "KBTGEN");
        AFUtil.sqlAddField(sqlFields, "KAIIND");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIECRIP";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        // WHERE KBNANN = année
        AFUtil.sqlAddCondition(sqlWhere, "KBNANN = " + getForAnnee());

        // AND (KBTGEN IN (310001 ,310006) OR (KBTGEN = 310007 and KBTSPE = (312003)))
        AFUtil.sqlAddCondition(sqlWhere, "(KBTGEN IN (" + CIEcriture.CS_CIGENRE_1 + " ," + CIEcriture.CS_CIGENRE_6
                + ") OR (KBTGEN = " + CIEcriture.CS_CIGENRE_7 + " and KBTSPE = (312003)))");

        // AND KBTCPT IN (303001,303002,303004)
        AFUtil.sqlAddCondition(sqlWhere, "KBTCPT IN (303001,303002,303004)");

        // id Compte individuelle
        AFUtil.sqlAddCondition(sqlWhere, "KAIIND = " + getForIdCompteIndividuel());

        // On restreint sur l'affilié
        AFUtil.sqlAddCondition(sqlWhere, "KBITIE = " + getForIdAffilie());

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFLineCi();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    public String getForIdCompteIndividuel() {
        return forIdCompteIndividuel;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdAffilie(String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;
    }

    public void setForIdCompteIndividuel(String forIdCompteIndividuel) {
        this.forIdCompteIndividuel = forIdCompteIndividuel;
    }

}
