package globaz.corvus.vb.process;

import globaz.hermes.db.gestion.HELotListViewBean;
import globaz.hermes.db.gestion.HELotViewBean;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Iterator;

/**
 * 
 * @author SCR
 * 
 */

public class REComparaisonCentraleViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private String idLot = "";
    private String moisAnnee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdLot() {
        return idLot;
    }

    public String[] getLotHermesList() throws Exception {

        HELotListViewBean lotMgr = new HELotListViewBean();
        lotMgr.setForType(HELotViewBean.CS_TYPE_ADAPTATION_RENTES);
        lotMgr.setOrder("RMILOT DESC");
        lotMgr.setSession(getSession());
        lotMgr.find();

        String[] lotList = new String[lotMgr.size() * 2];
        int i = 0;

        for (Iterator iterator = lotMgr.iterator(); iterator.hasNext();) {
            HELotViewBean lot = (HELotViewBean) iterator.next();

            lotList[i] = lot.getIdLot();
            lotList[i + 1] = lot.getDateCentrale() + " - " + getSession().getCodeLibelle(lot.getCsTypeLibelle()) + " ("
                    + lot.getNbAnnonces() + ")";

            i = i + 2;
        }

        return lotList;

    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
