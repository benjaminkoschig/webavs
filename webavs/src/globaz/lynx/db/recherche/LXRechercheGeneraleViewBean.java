package globaz.lynx.db.recherche;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;

public class LXRechercheGeneraleViewBean extends LXRechercheGenerale {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LXSocieteDebitrice societe;

    public String getBaseFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getBase()), true, true, false, 2);
    }

    public String getBaseProvisoireFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getBaseProvisoire()), true, true, false, 2);
    }

    public String getMouvementFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getMouvement()), true, true, false, 2);
    }

    public String getMouvementProvisoireFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getMouvementProvisoire()), true, true, false, 2);
    }

    public String getNomSociete() {
        if (getSociete() != null) {
            return getSociete().getNom();
        }
        return "";
    }

    /**
     * Return la société débitrice.
     * 
     * @return
     */
    public LXSocieteDebitrice getSociete() {
        retrieveSociete();

        return societe;
    }

    public String getSoldeFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getSolde()), true, true, false, 2);
    }

    public String getSoldeProvisoireFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getSoldeProvisoire()), true, true, false, 2);
    }

    /**
     * Retrouve la societe si pas encore chargée.
     */
    private void retrieveSociete() {
        if (!JadeStringUtil.isIntegerEmpty(getIdSociete()) && societe == null) {
            try {
                societe = new LXSocieteDebitrice();
                societe.setSession(getSession());
                societe.setIdSociete(getIdSociete());
                societe.retrieve();

                if (societe.hasErrors() || societe.isNew()) {
                    societe = null;
                    return;
                }
            } catch (Exception e) {
                // nothing
            }
        }
    }

}
