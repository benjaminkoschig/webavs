/*
 * Globaz SA.
 */
package globaz.corvus.vb.lotdeblocage;

import globaz.corvus.db.deblocage.REDeblocageVersement;
import globaz.corvus.vb.lots.RELotViewBean;
import globaz.jade.log.JadeLogger;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.domaine.AdressePaiement;
import ch.globaz.common.services.AdressePaiementLoader;

public class RELotDeblocageViewBean extends REDeblocageVersement {

    private static final long serialVersionUID = 1L;
    private RELotViewBean lot;
    private String idLot;

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
        if ((lot == null) && !StringUtils.isEmpty(getIdLot())) {
            RELotViewBean l = new RELotViewBean();
            l.setSession(getSession());
            l.setIdLot(getIdLot());
            try {
                l.retrieve();
                lot = l;
            } catch (Exception e) {
                JadeLogger.error("", e);
            }
        }
        return lot != null;
    }

    public String getMontantACompenser() {
        if (getLigneDeblocage().isCompensation()) {
            return getLigneDeblocageVentilation().getMontant().getValue();
        }

        return "";
    }

    public String getAdresseDePaiement() {

        if (getLigneDeblocage().isMontantAPayer()) {
            try {
                AdressePaiementLoader loader = new AdressePaiementLoader(getSession());
                AdressePaiement adr = loader.searchAdressePaiement(getLigneDeblocage().getIdTiersAdressePaiement(),
                        getLigneDeblocage().getIdApplicationAdressePaiement());

                return getSession().getCodeLibelle(adr.getTiers().getTitre()) + " " + adr.formatInOneLine("<BR>");
            } catch (RuntimeException e) {
                JadeLogger.error("", e);
            }
        } else {
            return getSession().getCodeLibelle(getLigneDeblocage().getType().getValue());
        }

        return "";
    }

    public String getMontantAPayer() {
        if (getLigneDeblocage().isMontantAPayer()) {
            return getLigneDeblocageVentilation().getMontant().getValue();
        }

        return "";
    }

    public String getIdLot() {
        return idLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

}
