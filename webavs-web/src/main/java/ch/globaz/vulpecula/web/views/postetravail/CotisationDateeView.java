package ch.globaz.vulpecula.web.views.postetravail;

public class CotisationDateeView {
    private final String id;
    private final String dateDebut;
    private final String dateFin;
    private final boolean valide;

    public CotisationDateeView(CotisationDatee cotisationDatee) {
        id = cotisationDatee.getId();
        dateDebut = cotisationDatee.getDateDebut().getSwissValue();
        dateFin = cotisationDatee.getDateFin() != null ? cotisationDatee.getDateFin().getSwissValue() : null;
        valide = cotisationDatee.isValide();
    }

    public String getId() {
        return id;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public boolean isValide() {
        return valide;
    }
}
