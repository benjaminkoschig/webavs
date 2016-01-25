package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Insérez la description du type ici. Date de création : (21.03.2003 15:09:18)
 * 
 * @author: Administrator
 */
public class HEAttenteEnvoiChampsViewBean extends HEAttenteRetourChampsOptimizedViewBean implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEAttenteEnvoiChampsViewBean.
     */
    public HEAttenteEnvoiChampsViewBean() {
        super();
    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la lecture de l'entité dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        valeur = statement.dbReadString("CHAMP"); // VALEUR
        idChamp = statement.dbReadNumeric("IDCHAMP"); // RDTCHA
        libelleChamp = statement.dbReadString("CHAMPLIB"); // PCOLUT
        longueur = statement.dbReadNumeric("RDNLON"); // LONGUEUR
        statut = statement.dbReadNumeric("RNTSTA"); // RNTSTA
    }
}
