package globaz.cygnus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class RFListeRecapitulativePaiementsViewBean extends PRAbstractViewBeanSupport {

    public String datePeriode = null;
    public String eMailAdr = null;

    public String getDatePeriode() {
        if (datePeriode == null) {
            datePeriode = "";
        }
        return datePeriode;
    }

    public String geteMailAdr() {
        return eMailAdr;
    }

    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }

    public void seteMailAdr(String eMailAdr) {
        this.eMailAdr = eMailAdr;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
