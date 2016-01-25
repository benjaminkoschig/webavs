package globaz.osiris.db.interets;

import globaz.globall.db.BStatement;
import globaz.musca.db.facturation.FAEnteteFacture;

/**
 * Permet d'avoir un tri sur les numéros d'affilié lors de l'impression des décisions d'intérêt dans MUSCA (03.06.2008)
 * 
 * @author: MAR
 */
public class CAInteretMoratoireImpressionManager extends CAInteretMoratoireManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CAInteretMoratoireManager.
     */
    public CAInteretMoratoireImpressionManager() {
        super();
    }

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement) + " INNER JOIN " + _getCollection() + FAEnteteFacture.TABLE_FAENTFP + " ON("
                + _getCollection() + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDENTETEFACTURE + "="
                + _getCollection() + CAInteretMoratoire.TABLE_CAIMDCP + "." + CAInteretMoratoire.FIELD_IDSECTIONFACTURE
                + ")";
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return _getCollection() + FAEnteteFacture.TABLE_FAENTFP + "." + FAEnteteFacture.FIELD_IDEXTERNEROLE + " ASC";
    }

}
