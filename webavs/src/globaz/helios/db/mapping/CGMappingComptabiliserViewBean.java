/*
 * Créé le Dec 12, 2005, dda
 */
package globaz.helios.db.mapping;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGCentreCharge;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMandatManager;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author dda
 * 
 */
public class CGMappingComptabiliserViewBean extends CGMappingComptabiliser implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String getMandatDescription(BSession session, String idMandat) {
        if (!JadeStringUtil.isIntegerEmpty(idMandat)) {
            CGMandat mandat = new CGMandat();
            mandat.setSession(session);

            mandat.setIdMandat(idMandat);

            try {
                mandat.retrieve();
            } catch (Exception e) {
                return "";
            }

            return mandat.getLibelle();
        } else {
            return "";
        }
    }

    public static String getSelectMandatBlock(BSession session, String name, String selectedIdMandat,
            String jsFunctionName) {
        String select = "<select name=\"" + name + "\" onChange=\"" + jsFunctionName + "()\">";

        CGMandatManager manager = new CGMandatManager();
        manager.setSession(session);
        manager.setOrderby(CGMandat.FIELD_IDMANDAT);

        try {
            manager.find();
        } catch (Exception e) {
            return "";
        }

        for (int i = 0; i < manager.size(); i++) {
            CGMandat mandat = (CGMandat) manager.get(i);

            if (mandat.getIdMandat().equals(selectedIdMandat)) {
                select += "<option value=\"" + mandat.getIdMandat() + "\" SELECTED>" + mandat.getLibelle()
                        + "</option>";
            } else {
                select += "<option value=\"" + mandat.getIdMandat() + "\">" + mandat.getLibelle() + "</option>";
            }
        }

        select += "</select>";

        return select;
    }

    /**
     * Le mandat en paramètre est-il un mandat AVS ?<br/>
     * Utilisé pour l'aide auto-complete sur les numéros de comptes.
     * 
     * @param session
     * @param idMandat
     * @return
     */
    public static boolean isMandatAVS(BSession session, String idMandat) {
        if (!JadeStringUtil.isIntegerEmpty(idMandat)) {
            CGMandat mandat = new CGMandat();
            mandat.setSession(session);

            mandat.setIdMandat(idMandat);

            try {
                mandat.retrieve();
            } catch (Exception e) {
                return false;
            }

            return mandat.isEstComptabiliteAVS().booleanValue();
        } else {
            return false;
        }
    }

    private String compteContreEcritureDestinationLibelle = new String();

    private String compteDestinationLibelle = new String();

    private String compteSourceLibelle = new String();

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        if ((!JadeStringUtil.isIntegerEmpty(getIdCompteSource())) && JadeStringUtil.isBlank(getCompteSourceLibelle())) {
            setCompteSourceLibelle(getPlanComptable(getSession(), getIdCompteSource(), getIdMandatSource())
                    .getLibelle());
        }

        if ((!JadeStringUtil.isIntegerEmpty(getIdCompteDestination()))
                && JadeStringUtil.isBlank(getCompteDestinationLibelle())) {
            setCompteDestinationLibelle(getPlanComptable(getSession(), getIdCompteDestination(),
                    getIdMandatDestination()).getLibelle());
        }

        if ((!JadeStringUtil.isIntegerEmpty(getIdContreEcritureDestination()))
                && JadeStringUtil.isBlank(getCompteContreEcritureDestinationLibelle())) {
            setCompteContreEcritureDestinationLibelle(getPlanComptable(getSession(), getIdContreEcritureDestination(),
                    getIdMandatDestination()).getLibelle());
        }
    }

    public String getCentreChargeDestinationLibelle() {
        return getCentreChargeLibelle(getIdCentreChargeDestination(), getIdMandatDestination());
    }

    public String getCentreChargeLibelle(String idCentreCharge, String idMandat) {
        if (!JadeStringUtil.isIntegerEmpty(idCentreCharge)) {
            CGCentreCharge centreCharge = new CGCentreCharge();
            centreCharge.setSession(getSession());

            centreCharge.setIdCentreCharge(idCentreCharge);
            centreCharge.setIdMandat(idMandat);

            try {
                centreCharge.retrieve();
            } catch (Exception e) {
                return "";
            }

            if (!centreCharge.isNew()) {
                return centreCharge.getLibelle();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getCentreChargeSourceLibelle() {
        return getCentreChargeLibelle(getIdCentreChargeSource(), getIdMandatSource());
    }

    public String getCompteContreEcritureDestinationLibelle() {
        return compteContreEcritureDestinationLibelle;
    }

    public String getCompteDestinationLibelle() {
        return compteDestinationLibelle;
    }

    public String getCompteSourceLibelle() {
        return compteSourceLibelle;
    }

    public String getDefaultIdMandatDestination() {
        if (JadeStringUtil.isIntegerEmpty(getIdMandatDestination())) {
            CGMandatManager manager = new CGMandatManager();
            manager.setSession(getSession());
            manager.setOrderby(CGMandat.FIELD_IDMANDAT);

            try {
                manager.find();
            } catch (Exception e) {
                return "";
            }

            CGMandat mandat = (CGMandat) manager.getFirstEntity();

            return mandat.getIdMandat();
        } else {
            return getIdMandatDestination();
        }
    }

    public String getDefaultIdMandatSource() {
        if (JadeStringUtil.isIntegerEmpty(getIdMandatSource())) {
            CGMandatManager manager = new CGMandatManager();
            manager.setSession(getSession());
            manager.setOrderby(CGMandat.FIELD_IDMANDAT);

            try {
                manager.find();
            } catch (Exception e) {
                return "";
            }

            CGMandat mandat = (CGMandat) manager.getFirstEntity();

            return mandat.getIdMandat();
        } else {
            return getIdMandatSource();
        }
    }

    public void setCompteContreEcritureDestinationLibelle(String compteContreEcritureDestinationLibelle) {
        this.compteContreEcritureDestinationLibelle = compteContreEcritureDestinationLibelle;
    }

    public void setCompteDestinationLibelle(String compteDestinationLibelle) {
        this.compteDestinationLibelle = compteDestinationLibelle;
    }

    public void setCompteSourceLibelle(String compteSourceLibelle) {
        this.compteSourceLibelle = compteSourceLibelle;
    }
}
