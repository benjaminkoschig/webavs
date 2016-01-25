package globaz.aquila.db.access.batch.transition;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 17-nov-2004
 */
public class CO041SaisirActeDefautBien extends CODefaultTransitionAction {

    private String dateReceptionRDV;
    private String numeroADB;

    public String getDateReceptionRDV() {
        return dateReceptionRDV;
    }

    public String getNumeroADB() {
        return numeroADB;
    }

    public void setDateReceptionRDV(String string) {
        dateReceptionRDV = string;
    }

    public void setNumeroADB(String string) {
        numeroADB = string;
    }
}
