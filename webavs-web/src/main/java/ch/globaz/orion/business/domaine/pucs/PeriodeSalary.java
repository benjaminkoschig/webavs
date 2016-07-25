package ch.globaz.orion.business.domaine.pucs;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.domaine.Date;

public class PeriodeSalary implements Comparable<PeriodeSalary> {

    private final Date dateDebut;
    private final Date dateFin;

    private PeriodeSalary(PeriodeSalaryBuilder builder) {
        dateDebut = builder.dateDebut;
        dateFin = builder.dateFin;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public static class PeriodeSalaryBuilder {
        private Date dateDebut;
        private Date dateFin;

        public PeriodeSalaryBuilder dateDebut(String dateDebut) {
            this.dateDebut = convertAsDate(dateDebut);
            return this;
        }

        public PeriodeSalaryBuilder dateFin(String dateFin) {
            this.dateFin = convertAsDate(dateFin);
            return this;
        }

        public PeriodeSalary build() {
            return new PeriodeSalary(this);
        }

        private static Date convertAsDate(String value) {
            if (StringUtils.isNotBlank(value)) {
                try {
                    XMLGregorianCalendar dateXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
                    return new Date(dateXml.toGregorianCalendar().getTime());
                } catch (DatatypeConfigurationException e) {
                    throw new RuntimeException("Error parsing date", e);
                }
            }
            return null;
        }
    }

    @Override
    public int compareTo(PeriodeSalary periode) {
        if (equals(periode)) {
            return 0;
        }

        Date dateDebutAutrePeriode = periode.getDateDebut();
        Date dateFinAutrePeriode = periode.getDateFin();

        if (dateFin == null && dateFinAutrePeriode == null) {
            if (dateDebut.before(dateDebutAutrePeriode)) {
                return -1;
            } else {
                return 1;
            }
        }

        if (dateDebut.after(dateDebutAutrePeriode)) {
            return 1;
        } else if (dateDebut.before(dateDebutAutrePeriode)) {
            return -1;
        }

        if (dateFin == null) {
            return 1;
        } else if (dateFinAutrePeriode == null) {
            return -1;
        }
        if (dateFin.after(dateFinAutrePeriode)) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
        result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PeriodeSalary other = (PeriodeSalary) obj;
        if (dateDebut == null) {
            if (other.dateDebut != null) {
                return false;
            }
        } else if (!dateDebut.equals(other.dateDebut)) {
            return false;
        }
        if (dateFin == null) {
            if (other.dateFin != null) {
                return false;
            }
        } else if (!dateFin.equals(other.dateFin)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(PeriodeSalary.class.getName());
        toStringBuilder.append("(");
        toStringBuilder.append("[").append(getDateDebut().getSwissValue()).append(" - ")
                .append(getDateFin().getSwissValue()).append("]");
        toStringBuilder.append(")");
        return toStringBuilder.toString();
    }
}
