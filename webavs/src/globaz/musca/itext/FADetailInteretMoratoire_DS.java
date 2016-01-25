package globaz.musca.itext;

// ITEXT
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CADetailInteretMoratoireManager;
import java.util.Iterator;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class FADetailInteretMoratoire_DS extends CADetailInteretMoratoireManager implements
        net.sf.jasperreports.engine.JRDataSource {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Iterator<?> container;
    private String dateVersement = "";
    private CADetailInteretMoratoire entity = null;

    public FADetailInteretMoratoire_DS() {
        super();
    }

    /**
     * Method _getAnneeCotisation.
     * 
     * @return String
     */
    private String _getAnneeCotisation() {

        String anneeCotisation = " ";
        if ((entity.getAnneeCotisation() != null) && !"0".equals(entity.getAnneeCotisation())) {
            anneeCotisation = entity.getAnneeCotisation();
        }

        return anneeCotisation;
    }

    /**
     * Method _getDateDebutFin.
     * 
     * @return String
     */
    private String _getDateDebutFin() {
        return entity.getDateDebut() + " - " + entity.getDateFin();
    }

    /**
     * Method _getMontantInteret.
     * 
     * @return String
     */
    private String _getMontantInteret() {
        return entity.getMontantInteret();
    }

    /**
     * Method _getMontantSoumis.
     * 
     * @return String
     */
    private String _getMontantSoumis() {
        return entity.getMontantSoumis();
    }

    /**
     * Method _getNbJours.
     * 
     * @return String
     */
    private String _getNbJours() {
        long nbJours = 0;
        try {
            nbJours = entity.getNbJours();
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.WARNING);
            setMessage("Impossible de calculer la durée de l'intérêt: " + e.getMessage());
        } finally {
            return String.valueOf(nbJours);
        }
    }

    /**
     * Method _getTaux.
     * 
     * @return String
     */
    private String _getTaux() {
        return entity.getTaux();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:33:47)
     */
    @Override
    protected void _init() {
        container = null;
    }

    /**
     * Copiez la méthode tel quel, permet la copy de l'objet Date de création : (01.04.2003 14:45:18)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Returns the dateVersement.
     * 
     * @return String
     */
    public String getDateVersement() {
        return dateVersement;
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     */
    @Override
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {
        // retourne chaque champ
        if (jrField.getName().equals("COL_1")) {
            return new Double(_getMontantSoumis());
            // Montant soumis
        }
        if (jrField.getName().equals("COL_2")) {
            return _getDateDebutFin(); // Date du... au...
        }

        if (jrField.getName().equals("COL_3")) {
            return _getNbJours(); // 360 jours dans une année
        }

        if (jrField.getName().equals("COL_4")) {
            return _getTaux().substring(0, 3) + "%"; // Taux
        }

        if (jrField.getName().equals("COL_5")) {
            return new Double(new FWCurrency(_getMontantInteret()).doubleValue());
            // montant des intérêts
        }

        if (jrField.getName().equals("COL_6")) {
            return _getAnneeCotisation(); // Année de cotisation
        }

        return null;
    }

    /**
     * Copier le contenu de cette méthode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entité.
     */
    @Override
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        entity = null;
        try {
            // Charge le container si pas encore chargé
            if (container == null) {
                this.find(0);
                container = getContainer().iterator();
            }
            if (container.hasNext()) {
                entity = (CADetailInteretMoratoire) container.next();
            }
            // lit le nouveau entity
            if (entity != null) {
                setDateVersement(new String(entity.getDateFin()));
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }

    /**
     * Sets the dateVersement.
     * 
     * @param dateVersement
     *            The dateVersement to set
     */
    public void setDateVersement(String dateVersement) {
        this.dateVersement = dateVersement;
    }

}