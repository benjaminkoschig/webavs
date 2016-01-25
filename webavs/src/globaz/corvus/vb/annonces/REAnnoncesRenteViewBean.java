package globaz.corvus.vb.annonces;

import globaz.corvus.db.annonces.REAnnoncesRentePourEcran;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

public class REAnnoncesRenteViewBean extends REAnnoncesRentePourEcran implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getLibelleCodeTraitement() {
        return getSession().getCodeLibelle(getCsTraitement());
    }

    public String getLibelleEtat() {
        return getSession().getCodeLibelle(getCsEtatAnnonce());
    }

    public String getMoisRapportFormat() {
        return PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(getMoisRapport());
    }

    public String getMontantFormate() {
        return new FWCurrency(getMontant()).toStringFormat();
    }

    public String getPeriodes() {
        String dateDebut;
        String dateFin;

        if (JadeStringUtil.isBlank(getDateDebutDroit())) {
            dateDebut = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        } else {
            dateDebut = PRDateFormater.convertDate_MMAA_to_MMxAAAA(getDateDebutDroit());
        }

        dateFin = PRDateFormater.convertDate_MMAA_to_MMxAAAA(getDateFinDroit());
        return dateDebut + " - " + dateFin;
    }
}