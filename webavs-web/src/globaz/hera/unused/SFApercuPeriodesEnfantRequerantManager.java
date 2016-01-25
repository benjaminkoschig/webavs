/*
 * ado Créé le 4 oct. 05
 */
package globaz.hera.unused;

// package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ado
 * 
 *         4 oct. 05
 */
public class SFApercuPeriodesEnfantRequerantManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_TABLE_ENFANT = "ENFANT";
    public static final String ALIAS_TABLE_MEMBRE_FAMILLE = "FAMILLE";

    public static final String ALIAS_TABLE_PERIODE = "PERIODE";
    public static final String ALIAS_TABLE_RELATION = "REL";

    public static final String ALIAS_TABLE_TIERS = "T1";
    public static final String ALIAS_TABLE_TIERS_AVS = "T2";

    public static final String ALIAS_TABLE_TIERS_PERS = "T3";
    public static final String TABLE_ENFANT = "SFENFANT";

    public static final String TABLE_MEMBRE_FAMILLE = "SFMBRFAM";
    public static final String TABLE_PERIODE = "SFPERIOD";

    public static final String TABLE_RELATION = "SFREFARE";
    public static final String TABLE_TIERS = "TITIERP";

    public static final String TABLE_TIERS_AVS = "TIPAVSP";
    public static final String TABLE_TIERS_PERS = "TIPERSP";

    public static void main(String[] args) {
        try {
            BSession session = new BSession("HERA");
            session.connect("globazf", "ssiiadm");
            SFApercuPeriodesEnfantRequerantManager er = new SFApercuPeriodesEnfantRequerantManager();
            er.setSession(session);
            er.setForIdRequerant("104");
            er.find();
            for (int i = 0; i < er.getSize(); i++) {
                System.out.println(er.getEntity(i) + "\n\n\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }

    private String forIdRequerant = "";

    private String forTypePeriode = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + TABLE_RELATION + " as " + ALIAS_TABLE_RELATION + "," + _getCollection()
                + TABLE_ENFANT + " as " + ALIAS_TABLE_ENFANT + "," + _getCollection() + TABLE_MEMBRE_FAMILLE + " as "
                + ALIAS_TABLE_MEMBRE_FAMILLE + ", " + _getCollection() + TABLE_PERIODE + " as " + ALIAS_TABLE_PERIODE
                + ", " + _getCollection() + TABLE_TIERS + " as " + ALIAS_TABLE_TIERS + ", " + _getCollection()
                + TABLE_TIERS_AVS + " as " + ALIAS_TABLE_TIERS_AVS + ", " + _getCollection() + TABLE_TIERS_PERS
                + " as " + ALIAS_TABLE_TIERS_PERS + " ";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = ALIAS_TABLE_RELATION + ".wfimef=" + ALIAS_TABLE_ENFANT + ".wiimef and " + ALIAS_TABLE_ENFANT
                + ".wiimef=" + ALIAS_TABLE_MEMBRE_FAMILLE + ".wgimef and " + ALIAS_TABLE_RELATION + ".wfimef="
                + ALIAS_TABLE_PERIODE + ".WHIDMF and " + ALIAS_TABLE_MEMBRE_FAMILLE + ".wgitie=" + ALIAS_TABLE_TIERS
                + ".htitie and " + ALIAS_TABLE_MEMBRE_FAMILLE + ".wgitie=" + ALIAS_TABLE_TIERS_AVS + ".htitie and "
                + ALIAS_TABLE_MEMBRE_FAMILLE + ".wgitie=" + ALIAS_TABLE_TIERS_PERS + ".htitie";

        if (!JadeStringUtil.isEmpty(getForIdRequerant())) {
            where += " AND " + ALIAS_TABLE_RELATION + ".WFIREQ=" + getForIdRequerant();
        }
        if (!JadeStringUtil.isEmpty(getForTypePeriode())) {
            where += " AND " + ALIAS_TABLE_PERIODE + ".WHTTYP=" + getForTypePeriode();
        }

        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFApercuPeriodesEnfantRequerant();
    }

    /**
     * @return
     */
    public String getForIdRequerant() {
        return forIdRequerant;
    }

    /**
     * @return
     */
    public String getForTypePeriode() {
        return forTypePeriode;
    }

    /**
     * @param string
     */
    public void setForIdRequerant(String string) {
        forIdRequerant = string;
    }

    /**
     * @param string
     */
    public void setForTypePeriode(String string) {
        forTypePeriode = string;
    }

}
