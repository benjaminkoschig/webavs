// créé le 24 mars 2010
package globaz.cygnus.db.conventions;

/**
 * author fha
 */
public class RFMontantsConventionDetail extends RFMontantsConvention {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFMontantsConventionDetail() {
        super();
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFMontantsConvention.TABLE_NAME;
    }

}
