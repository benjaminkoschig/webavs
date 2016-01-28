package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleJoursAppoint extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJoursAppoint = null;
    private String idPCAccordee = null;
    private String montantJournalier = null;
    private String nbrJoursAppoint = null;
    private String dateEntreHome = null;
    private String montantTotal = null;

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    @Override
    public String getId() {
        return idJoursAppoint;
    }

    public String getDateEntreHome() {
        return dateEntreHome;
    }

    public void setDateEntreHome(String dateEntreHome) {
        this.dateEntreHome = dateEntreHome;
    }

    public String getIdJoursAppoint() {
        return idJoursAppoint;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getMontantJournalier() {
        return montantJournalier;
    }

    public String getNbrJoursAppoint() {
        return nbrJoursAppoint;
    }

    @Override
    public void setId(String id) {
        idJoursAppoint = id;

    }

    public void setIdJoursAppoint(String idJoursAppoint) {
        this.idJoursAppoint = idJoursAppoint;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setMontantJournalier(String montantJournalier) {
        this.montantJournalier = montantJournalier;
    }

    public void setNbrJoursAppoint(String nbrJoursAppoint) {
        this.nbrJoursAppoint = nbrJoursAppoint;
    }

    @Override
    public String toString() {
        return "SimpleJoursAppoint [idJoursAppoint=" + idJoursAppoint + ", idPCAccordee=" + idPCAccordee
                + ", montantJournalier=" + montantJournalier + ", nbrJoursAppoint=" + nbrJoursAppoint
                + ", dateEntreHome=" + dateEntreHome + ", montantTotal=" + montantTotal + "]";
    }

}
