/*
 * Globaz SA
 */
package globaz.corvus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererListeOrdresVersementsViewBean extends PRAbstractViewBeanSupport {

    private String descriptionLot = "";
    private String eMailAddress = "";
    private String idLot = "";
    private String csTypeLot;

    public String getDescriptionLot() {
        return descriptionLot;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdLot() {
        return idLot;
    }

    public void setDescriptionLot(String descriptionLot) {
        this.descriptionLot = descriptionLot;
    }

    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public String getCsTypeLot() {
        return csTypeLot;
    }

    public void setCsTypeLot(String csTypeLot) {
        this.csTypeLot = csTypeLot;
    }

}
