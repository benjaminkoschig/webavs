package globaz.musca.itext.list;

// ITEXT
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAAfactViewBean;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
class FAListAfactsAQuittancer_DS extends FAAfactManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Iterator<?> container;
    private globaz.musca.db.facturation.FAAfact entity;

    public FAListAfactsAQuittancer_DS() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.04.2003 08:17:20)
     * 
     * @return java.lang.String
     */
    private String _getAQuittancer() {
        // marquer d'un X si la facture est $ quittancer
        if (entity.isAQuittancer().booleanValue()) {
            return "X";
        } else {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 16:03:05)
     * 
     * @return java.lang.String
     */
    private String _getDescriptionTiers() {

        // utiliser la méthode du viewBean pour avoir les infos du débiteur
        FAAfactViewBean viewBean = new FAAfactViewBean();
        viewBean.setSession(getSession());
        viewBean.setIdEnteteFacture(entity.getIdEnteteFacture());

        return viewBean.getDescriptionTiersForList();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:33:47)
     */
    @Override
    protected void _init() {
        container = null;
    }

    /**
     * Appele chaque champ du modèle
     */
    Map<String, Object> getFieldValues() throws net.sf.jasperreports.engine.JRException {
        // Verify si le passage change -> nouveau document pour l'impression de
        // liste de document
        if (!super.getForIdPassage().equalsIgnoreCase(entity.getIdPassage())) {
            super.setForIdPassage(entity.getIdPassage());
            _init();
        }
        Map<String, Object> row = new HashMap<String, Object>();
        row.put(FWIImportParametre.getCol(1), _getDescriptionTiers());
        row.put(FWIImportParametre.getCol(2), entity.getDescriptionDecompte()); // Description du décompte
        row.put(FWIImportParametre.getCol(3), entity.getIdExterneRubrique()); // Rubrique
        row.put(FWIImportParametre.getCol(4), entity.getLibelleSurFacture(getSession().getIdLangueISO())); // libellé
        row.put(FWIImportParametre.getCol(5), entity.getAnneeCotisation()); // Année de cotisation
        row.put(FWIImportParametre.getCol(6), new Double(new FWCurrency(entity.getMasseFacture()).doubleValue()));// Masse
        row.put(FWIImportParametre.getCol(7), new Double(new FWCurrency(entity.getTauxFacture()).doubleValue()));// Taux
        row.put(FWIImportParametre.getCol(8), new Double(entity.getMontantFactureToCurrency().doubleValue()));// Montant
        row.put(FWIImportParametre.getCol(9), _getAQuittancer()); // propriété à quittancer
        row.put(FWIImportParametre.getCol(10), entity.getUser());
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
                entity = (FAAfact) container.next();
            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage("Impossible de charger le container: " + e.getMessage());
            return false;
        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }

}
