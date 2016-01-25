package ch.globaz.al.business.models.prestation.paiement;

/**
 * Mod�le de recherche permettant d'effectuer des recherches sur le mod�le {@link PaiementPrestationComplexModel}
 * 
 * Les crit�res de recherche sont contenus dans le mod�le de recherche parent. Ce mod�le doit uniquement red�finir la
 * m�thode <code>whichModelClass</code>
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
