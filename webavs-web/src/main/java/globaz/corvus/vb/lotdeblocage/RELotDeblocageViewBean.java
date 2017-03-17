/*
 * Globaz SA.
 */
package globaz.corvus.vb.lotdeblocage;

import globaz.corvus.db.lots.RELot;
import globaz.corvus.vb.lots.RELotViewBean;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class RELotDeblocageViewBean extends RELot {

    private static final long serialVersionUID = 1L;
    private RELotViewBean lot;

    private String idRentePrestation;

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        super._readProperties(statement);

        idRentePrestation = statement.dbReadNumeric("ID_RENTE_PRESTATION");
    }

    public String getLotDescription() {

        if (loadLot()) {
            return lot.getIdLot() + " - " + lot.getDescription();
        } else {
            return "";
        }
    }

    public String getCsEtatLotLibelle() {
        if (loadLot()) {
            return lot.getCsEtatLotLibelle();
        } else {
            return "";
        }
    }

    public String getCsTypeLotLibelle() {
        if (loadLot()) {
            return lot.getCsTypeLotLibelle();
        } else {
            return "";
        }

    }

    private boolean loadLot() {
        if ((lot == null) && !JadeStringUtil.isIntegerEmpty(getIdLot())) {
            RELotViewBean l = new RELotViewBean();
            l.setSession(getSession());
            l.setIdLot(getIdLot());
            try {
                l.retrieve();
                lot = l;
            } catch (Exception e) {
                // on ne fait rien
            }
        }
        return lot != null;
    }

    public String getDescriptionCompteAnnexe() {
        return "CompteAnnexe 1";
    }

    public String getSection() {
        return "Section";
    }

    public String getMontantACompenser() {
        return "";
    }

    public String getAdresseDePaiement() {
        return "";
    }

    public String getMontantAPayer() {
        return "";
    }

    public String getIdRentePrestation() {
        return idRentePrestation;
    }

    public void setIdRentePrestation(String idRentePrestation) {
        this.idRentePrestation = idRentePrestation;
    }
}
