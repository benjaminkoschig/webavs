package globaz.osiris.db.print;

import globaz.osiris.vb.CAAbstractPersistenceViewBean;

/**
 * @author dda
 */
public class CAListCumulCotisationsParAnneeViewBean extends CAAbstractPersistenceViewBean {

    private String fromDateValeur = "";
    private String fromIdExterne = "";
    private String toDateValeur = "";
    private String toIdExterne = "";
    private String typeImpression = "pdf";

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    public String getFromDateValeur() {
        return fromDateValeur;
    }

    public String getFromIdExterne() {
        return fromIdExterne;
    }

    public String getToDateValeur() {
        return toDateValeur;
    }

    public String getToIdExterne() {
        return toIdExterne;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    @Override
    public void retrieve() throws Exception {

    }

    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    public void setToDateValeur(String toDateValeur) {
        this.toDateValeur = toDateValeur;
    }

    public void setToIdExterne(String toIdExterne) {
        this.toIdExterne = toIdExterne;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

    @Override
    public void update() throws Exception {

    }

}
