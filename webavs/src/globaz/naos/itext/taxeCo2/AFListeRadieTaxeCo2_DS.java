package globaz.naos.itext.taxeCo2;

// ITEXT
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.taxeCo2.AFTaxeCo2;
import globaz.naos.db.taxeCo2.AFTaxeCo2Manager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
class AFListeRadieTaxeCo2_DS extends AFTaxeCo2Manager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Iterator container = null;
    private AFTaxeCo2 entity;

    public AFListeRadieTaxeCo2_DS() {
        super();
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     */
    Map getFieldValues() throws net.sf.jasperreports.engine.JRException {

        Map row = new HashMap();
        try {
            row.put(FWIImportParametre.getCol(1), entity.getDescriptionTiers());
            row.put(FWIImportParametre.getCol(2), entity.getAnneeMasse());
            row.put(FWIImportParametre.getCol(3), entity.getAnneeRedistribution());
            // Description du décompte
            row.put(FWIImportParametre.getCol(4), new Double(new FWCurrency(entity.getMasse()).doubleValue()));
            row.put(FWIImportParametre.getCol(5), getSession().getCodeLibelle(entity.getMotifFin()));
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return row;
    }

    /**
     * Copier le contenu de cette méthode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entité.
     */
    boolean next() throws net.sf.jasperreports.engine.JRException {
        entity = null;
        try {
            // Charge le container si pas encore chargé
            if (container == null) {
                this.find(0);
                container = getContainer().iterator();
            }
            // lit le nouveau entity
            if (container.hasNext()) {
                entity = (AFTaxeCo2) container.next();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }
}
