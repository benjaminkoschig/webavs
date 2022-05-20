package globaz.corvus.db.echeances;

import globaz.jade.client.util.JadePeriodWrapper;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;

/**
 * Conteneur de données pour une période d'étude ou de certificat de vie d'un tiers, dans le cadre des échéances
 * 
 * @author PBA
 */
public class REPeriodeEcheances implements IREPeriodeEcheances {

    private String csTypePeriode;
    private String idPeriode;
    private JadePeriodWrapper periode;
    private String idRecueillant;

    public REPeriodeEcheances() {
        this("", "", "", "");
    }

    public REPeriodeEcheances(String idPeriode, String dateDebut, String dateFin, String csTypePeriode) {
        this(idPeriode, dateDebut, dateFin, csTypePeriode, "");
    }

    public REPeriodeEcheances(String idPeriode, String dateDebut, String dateFin, String csTypePeriode, String idRecueillant) {
        this.idPeriode = idPeriode;
        periode = new JadePeriodWrapper(dateDebut, dateFin);
        this.csTypePeriode = csTypePeriode;
        this.idRecueillant = idRecueillant;
    }

    /**
     * Tri des périodes par date de fin.<br/>
     * Si les dates de fin sont les mêmes, tri par date de début.<br/>
     * Si une des dates n'a pas de date de fin, sera mise en bout de liste.
     */
    @Override
    public int compareTo(IREPeriodeEcheances uneAutrePeriode) {
        return periode.compareTo(uneAutrePeriode.getPeriode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof REPeriodeEcheances) {
            return ((IREPeriodeEcheances) obj).getIdPeriode().equals(getIdPeriode());
        }
        return false;
    }

    @Override
    public String getCsTypePeriode() {
        return csTypePeriode;
    }

    @Override
    public String getDateDebut() {
        return periode.getDateDebut();
    }

    @Override
    public String getDateFin() {
        return periode.getDateFin();
    }

    @Override
    public String getIdPeriode() {
        return idPeriode;
    }

    @Override
    public String getIdRecueillant() {
        return idRecueillant;
    }

    @Override
    public JadePeriodWrapper getPeriode() {
        return periode;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(idPeriode);
    }

    public void setCsTypePeriode(String csTypePeriode) {
        this.csTypePeriode = csTypePeriode;
    }

    public void setDateDebut(String dateDebut) {
        periode = new JadePeriodWrapper(dateDebut, getDateFin());
    }

    public void setDateFin(String dateFin) {
        periode = new JadePeriodWrapper(getDateDebut(), dateFin);
    }

    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    public void setIdRecueillant(String idRecueillant) {
        this.idRecueillant = idRecueillant;
    }
}
