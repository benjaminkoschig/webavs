/*
 * Créé le 10 décembre 2010
 */

package globaz.cygnus.vb.paiement;

import globaz.corvus.db.lots.RELot;
import globaz.cygnus.db.paiement.RFLot;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BSession;

/**
 * @author FHA
 * @Revision JJE 16.08.2011
 */
public class RFLotViewBean extends RELot {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // private BSpy creationSpy = null;
    private String idGestionnaire = "";

    private String idLotRFM = "";

    // private BSpy spy = null;

    // ~ Constructor ------------------------------------------------------------------------------------------------
    public RFLotViewBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void _retrieve() throws Exception {

        this.retrieve();

        if (!isNew()) {

            RFLot rfLot = new RFLot();
            rfLot.setSession((BSession) getISession());
            rfLot.setIdLot(getId());

            rfLot.retrieve();

            if (!isNew()) {

                setIdLotRFM(rfLot.getIdLot());
                setIdGestionnaire(rfLot.getIdGestionnaire());

            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "_retrieve()", "RFLotViewBean");
            }

        } else {
            RFUtils.setMsgErreurInattendueViewBean(this, "_retrieve()", "RFLotViewBean");
        }

    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdLotRFM() {
        return idLotRFM;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdLotRFM(String idLotRFM) {
        this.idLotRFM = idLotRFM;
    }

}