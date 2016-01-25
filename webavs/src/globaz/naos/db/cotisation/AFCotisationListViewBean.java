package globaz.naos.db.cotisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planCaisse.AFPlanCaisse;

public class AFCotisationListViewBean extends AFCotisationManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String totalAnnuel = "";
    private java.lang.String totalPeriodique = "";

    public AFAffiliation getAffiliation(int index) {

        return ((AFCotisation) getEntity(index)).getAffiliation();
    }

    public AFAssurance getAssurance(int index) {

        return ((AFCotisation) getEntity(index)).getAssurance();
    }

    public String getAssuranceId(int index) {

        return ((AFCotisation) getEntity(index)).getAssuranceId();
    }

    public AFPlanCaisse getCaisse(int index) {

        return ((AFCotisation) getEntity(index)).getCaisse();
    }

    public String getCotisationId(int index) {

        return ((AFCotisation) getEntity(index)).getCotisationId();
    }

    public String getDateDebut(int index) {

        return ((AFCotisation) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {

        return ((AFCotisation) getEntity(index)).getDateFinAffichage();
    }

    public String getDateFinMin(int index) {

        return ((AFCotisation) getEntity(index)).getDateFinMin();
    }

    public Boolean getMaisonMere(int index) {

        return ((AFCotisation) getEntity(index)).getMaisonMere();
    }

    public String getMasseAnnuelle(int index) {
        return ((AFCotisation) getEntity(index)).getMasseAnnuelle();
    }

    public String getMassePeriodicite(int index) {

        return ((AFCotisation) getEntity(index)).getMassePeriodicite();
    }

    public String getMontantAnnuel(int index) {
        float cumul = 0;
        float montant = 0;
        String montantAnnuel = JANumberFormatter.deQuote(((AFCotisation) getEntity(index)).getMontantAnnuel());
        String total = JANumberFormatter.deQuote(getTotalAnnuel());
        if (!JadeStringUtil.isDecimalEmpty(montantAnnuel)) {
            montant = Float.parseFloat(montantAnnuel);
        }
        if (!JadeStringUtil.isDecimalEmpty(total)) {
            cumul = Float.parseFloat(total);
        }
        cumul = new FWCurrency(cumul + montant).floatValue();
        setTotalAnnuel(Float.toString(cumul));

        return ((AFCotisation) getEntity(index)).getMontantAnnuel();
    }

    public String getMontantMensuel(int index) {

        return ((AFCotisation) getEntity(index)).getMontantMensuel();
    }

    public String getMontantPeriodicite(int index) {
        float cumul = 0;
        float montant = 0;
        String montantPeriodique = JANumberFormatter.deQuote(((AFCotisation) getEntity(index)).getMontantPeriodicite());
        String total = JANumberFormatter.deQuote(getTotalPeriodique());
        if (!JadeStringUtil.isDecimalEmpty(montantPeriodique)) {
            montant = Float.parseFloat(montantPeriodique);
        }
        if (!JadeStringUtil.isDecimalEmpty(total)) {
            cumul = Float.parseFloat(total);
        }
        cumul = new FWCurrency(cumul + montant).floatValue();
        setTotalPeriodique(Float.toString(cumul));

        return montantPeriodique;
    }

    public String getMotifFin(int index) {

        return ((AFCotisation) getEntity(index)).getMotifFin();
    }

    public String getPeriodicite(int index) {

        return ((AFCotisation) getEntity(index)).getPeriodicite();
    }

    public AFPlanAffiliation getPlanAffiliation(int index) {
        return ((AFCotisation) getEntity(index)).getPlanAffiliation();
    }

    public java.lang.String getTotalAnnuel() {
        return JANumberFormatter.fmt(totalAnnuel, true, true, true, 2);
    }

    public java.lang.String getTotalPeriodique() {
        return JANumberFormatter.fmt(totalPeriodique, true, true, true, 2);
    }

    public String getTraitementMoisAnnee(int index) {

        return ((AFCotisation) getEntity(index)).getTraitementMoisAnnee();
    }

    public void setTotalAnnuel(java.lang.String totalAnnuel) {
        this.totalAnnuel = totalAnnuel;
    }

    public void setTotalPeriodique(java.lang.String totalPeriodique) {
        this.totalPeriodique = totalPeriodique;
    }

}
