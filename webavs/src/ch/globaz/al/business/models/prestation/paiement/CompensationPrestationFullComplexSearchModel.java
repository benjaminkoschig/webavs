package ch.globaz.al.business.models.prestation.paiement;

/**
 * Mod�le de recherche permettant d'effectuer des recherches sur le mod�le
 * {@link CompensationPrestationFullComplexModel}
 * 
 * Les crit�res de recherche sont contenus dans le mod�le de recherche parent. Ce mod�le doit uniquement red�finir la
 * m�thode <code>whichModelClass</code>
 * 
 * @author jts
 * 
 */
public class CompensationPrestationFullComplexSearchModel extends CompensationPrestationComplexSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public Class whichModelClass() {
        return CompensationPrestationFullComplexModel.class;
    }
}
