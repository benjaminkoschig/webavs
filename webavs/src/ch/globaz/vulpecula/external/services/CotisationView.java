package ch.globaz.vulpecula.external.services;

/**
 * Container utilisé pour l'affichage des cotisations dans l'écran des Postes de Travails
 * 
 * @since WebBMS 0.5
 */
public class CotisationView {
    private String id;
    private String dateDebut;
    private String dateFin;

    public CotisationView(String id, String dateDebut, String dateFin) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

}
