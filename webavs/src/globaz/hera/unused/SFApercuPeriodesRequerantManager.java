/*
 * ado Créé le 4 oct. 05
 */
package globaz.hera.unused;

// package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFApercuRequerantManager;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ado
 * 
 *         4 oct. 05
 */
public class SFApercuPeriodesRequerantManager extends SFApercuRequerantManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String TABLE_PERIODE = "SFPERIOD";

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {

            BSession session = new BSession("HERA");
            session.connect("globazf", "ssiiadm");
            SFApercuPeriodesRequerantManager periodes = new SFApercuPeriodesRequerantManager();
            periodes.setSession(session);
            periodes.setForIdRequerant("104");
            periodes.find();
            for (int i = 0; i < periodes.getSize(); i++) {
                System.out.println(periodes.getEntity(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private String forIdRequerant = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String getFrom = _getCollection() + TABLE_PERIODE + "," + SFApercuRequerant.createFromClause(_getCollection());
        return getFrom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.db.famille.SFMembreFamilleManager#_getWhere(globaz.globall .db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // WEBAVSP.SFPERIOD.WHIDMF=SFMBRFAM.WGIMEF and
        // WEBAVSP.SFPERIOD.WHIDMF=224
        String where = super._getWhere(statement);
        where += _getCollection() + "SFPERIOD.WHIDMF=SFMBRFAM.WGIMEF ";
        if (!JadeStringUtil.isEmpty(getForIdRequerant())) {
            where += " and WDIREQ=" + getForIdRequerant();
        }
        return where;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFApercuPeriodesRequerant();
    }

    /**
     * @return
     */
    public String getForIdRequerant() {
        return forIdRequerant;
    }

    /**
     * @param string
     */
    public void setForIdRequerant(String string) {
        forIdRequerant = string;
    }

}
