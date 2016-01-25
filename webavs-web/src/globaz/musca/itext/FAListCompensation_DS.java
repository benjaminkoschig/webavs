package globaz.musca.itext;

// ITEXT
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAAfactViewBean;
import globaz.musca.db.facturation.FAEnteteFacture;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
class FAListCompensation_DS extends FAAfactManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Iterator<?> container = null;
    private globaz.musca.db.facturation.FAEnteteFacture decompte = new FAEnteteFacture();
    private globaz.musca.db.facturation.FAEnteteFacture decompteCompense = new FAEnteteFacture();
    private globaz.musca.db.facturation.FAAfact entity;

    public FAListCompensation_DS() {
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
     * Insérez la description de la méthode ici. Date de création : (11.04.2003 11:21:23)
     * 
     * @return java.lang.String
     */
    private String _getDebiteurCompense() {
        /*
         * si le tiers à compenser est indéfini ou le même que le tiers du décompte, ne pas afficher les informations du
         * tiers à compenser
         */
        if (JadeStringUtil.isBlank(entity.getIdExterneDebiteurCompensation())
                || entity.getIdExterneRole().equalsIgnoreCase(entity.getIdExterneDebiteurCompensation())) {
            return "./.";
        } else {
            // créér une nouvelle entete de facture dummy pour utiliser la
            // méthode getDescription pour
            // le débiteur compensé
            decompteCompense.setSession(getSession());
            decompteCompense.setIdRole(entity.getIdRoleDebiteurCompensation());
            decompteCompense.setIdExterneRole(entity.getIdExterneDebiteurCompensation());
            decompteCompense.setIdTiers(entity.getIdTiersDebiteurCompensation());
            return decompteCompense.getDescriptionTiersForList();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.04.2003 13:55:40)
     * 
     * @return java.lang.String
     */
    private String _getDescriptionDecompteCompense() {
        // utiliser le dummy decompteCompense
        decompteCompense.setIdExterneFacture(entity.getIdExterneFactureCompensation());
        decompteCompense.setIdTypeFacture(entity.getIdTypeFactureCompensation());
        decompteCompense.setIdTiers(entity.getIdTiersDebiteurCompensation());
        decompteCompense.setIdRole(entity.getIdRoleDebiteurCompensation());
        decompteCompense.setIdExterneRole(entity.getIdExterneDebiteurCompensation());
        decompteCompense.setSession(getSession());

        return decompteCompense.getDescriptionDecompteComp(getSession().getIdLangueISO());
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

    private String _getMontantSCompen() {
        double montant;
        decompte.setSession(getSession());
        decompte.setIdEntete(entity.getIdEnteteFacture());
        try {
            decompte.retrieve();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        if (entity.isAQuittancer().booleanValue()) {
            return decompte.getTotalFacture();
        } else {
            double totalFacture = new FWCurrency(entity.getTotalFacture()).doubleValue();
            double montantCompen = new FWCurrency(entity.getMontantFacture()).doubleValue();
            montant = (totalFacture - montantCompen);
            return new FWCurrency(montant).toString();
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:33:47)
     */
    @Override
    protected void _init() {
        container = null;
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
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
        row.put(FWIImportParametre.getCol(2), entity.getDescriptionDecompte());
        // Description du décompte
        row.put(FWIImportParametre.getCol(3), new Double(new FWCurrency(_getMontantSCompen()).doubleValue()));
        row.put(FWIImportParametre.getCol(4), new Double(new FWCurrency(entity.getMontantFacture()).doubleValue()));
        // Montant
        row.put(FWIImportParametre.getCol(5), _getDebiteurCompense());
        // Débiteur compensé
        row.put(FWIImportParametre.getCol(6), _getDescriptionDecompteCompense());
        row.put(FWIImportParametre.getCol(7), _getAQuittancer());
        // propriété à quittancer
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
            JadeLogger.error(this, e);
        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }
}
