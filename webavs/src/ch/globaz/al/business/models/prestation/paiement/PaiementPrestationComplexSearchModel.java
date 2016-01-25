package ch.globaz.al.business.models.prestation.paiement;

/**
 * Modèle de recherche permettant d'effectuer des recherches sur le modèle {@link PaiementPrestationComplexModel}
 * 
 * Les critères de recherche sont contenus dans le modèle de recherche parent. Ce modèle doit uniquement redéfinir la
 * méthode <code>whichModelClass</code>
 * 
 * @author jts
 * 
 */
public class PaiementPrestationComplexSearchModel extends CompensationPaiementPrestationComplexSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Class<PaiementPrestationComplexModel> whichModelClass() {
        return PaiementPrestationComplexModel.class;
    }
}
