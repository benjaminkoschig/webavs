package globaz.prestation.beans;

import globaz.prestation.utils.PRDateUtils;

public class PRPeriode implements Comparable<PRPeriode> {
    private String dateDeDebut;
    private String dateDeFin;
    private String nbJour = "";
    private String tauxImposition = "";
    private String cantonImposition = "";

    public PRPeriode() {
    }

    public PRPeriode(String datedeDebut, String dateDeFin) {
        this.dateDeDebut = datedeDebut;
        this.dateDeFin = dateDeFin;
    }

    public PRPeriode(String dateDeDebut, String dateDeFin, String nbJour) {
        this.dateDeDebut = dateDeDebut;
        this.dateDeFin = dateDeFin;
        this.nbJour = nbJour;
    }

    public PRPeriode(String dateDeDebut, String dateDeFin, String nbJour, String tauxImposition, String cantonImposition) {
        this.dateDeDebut = dateDeDebut;
        this.dateDeFin = dateDeFin;
        this.nbJour = nbJour;
        this.tauxImposition = tauxImposition;
        this.cantonImposition = cantonImposition;
    }

    public final String getDateDeDebut() {
        return dateDeDebut;
    }

    public final String getDateDeFin() {
        return dateDeFin;
    }

    public final void setDateDeDebut(String dateDeDebut) {
        this.dateDeDebut = dateDeDebut;
    }

    public final void setDateDeFin(String dateDeFin) {
        this.dateDeFin = dateDeFin;
    }

    public String getNbJour() {
        return nbJour;
    }

    public void setNbJour(String nbJour) {
        this.nbJour = nbJour;
    }

    private Integer compareDates(String date1, String date2) {
        Integer result = null;
        if ((date1 != null) && (date2 != null)) {
            switch (PRDateUtils.compare(date1, date2)) {
                case BEFORE:
                    result = new Integer(1);
                    break;
                case EQUALS:
                    result = new Integer(0);
                    break;
                case AFTER:
                    result = new Integer(-1);
                    break;
                case INCOMPARABLE:
                default:
                    return result;
            }
        }
        return result;
    }

    /**
     * <strong>La méthode de tri est la suivante :</br> 1 : On compare les dates de début, si la comparaison n'est pas
     * possible ou si les dates sont égales, on compare les dates de fin. </br>Si la comparaison des dates de début et
     * date de fin n'a pas été possible, on retourne -1 (Cet object est plus petit que celui passé en
     * paramètres)</strong> </br>Return a negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.</br> <strong> Si la date de cet object est situé avant -> -1</br>
     * Si la date de cet objet est egale -> 0</br> Si la date de cet objet est situé après -> 1</br> Si aucune
     * comparaison de dates possible -> -1</br></strong>
     */
    @Override
    public int compareTo(PRPeriode o) {
        Integer result = compareDates(dateDeDebut, o.getDateDeDebut());
        if (result == null) {
            result = compareDates(dateDeFin, o.getDateDeFin());
        }
        // Si les 2 dates de début sont égales, on regarde les 2 dates de fin. Si la comparaison de la date de fin
        // échoue on garde le résultat de la comparaison des 2 dates de début
        else if (result == 0) {
            Integer resultFin = compareDates(dateDeFin, o.getDateDeFin());
            if (resultFin != null) {
                result = resultFin;
            }
        }
        // Si aucune comparaison possible
        if (result == null) {
            result = new Integer(-1);
        }
        return result.intValue();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (dateDeDebut != null) {
            builder.append(dateDeDebut);
            builder.append(", ");
        }
        if (dateDeFin != null) {
            builder.append(dateDeFin);
        }
        builder.append("]");
        return builder.toString();
    }

    public String getTauxImposition() {
        return tauxImposition;
    }

    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    public String getCantonImposition() {
        return cantonImposition;
    }

    public void setCantonImposition(String cantonImposition) {
        this.cantonImposition = cantonImposition;
    }
}
