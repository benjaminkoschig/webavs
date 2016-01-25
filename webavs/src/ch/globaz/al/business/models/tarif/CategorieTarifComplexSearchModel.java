package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.al.business.constantes.ALCSTarif;

/**
 * Modèle de recherche pour le modèle {@link CategorieTarifComplexModel}
 * 
 * @author jts
 * 
 */
public class CategorieTarifComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur la catégorie de tarif (code système)
     */
    private String forCategorieTarif = null;
    /**
     * Recherche sur la legislatif de tarif (code système)
     */
    private String forLegislationTarif = null;

    /**
     * @return the forCategorieTarif
     */
    public String getForCategorieTarif() {
        return forCategorieTarif;
    }

    public String getForLegislationTarif() {
        return forLegislationTarif;
    }

    /**
     * @param forCategorieTarif
     *            the forCategorieTarif to set
     */
    public void setForCategorieTarif(String forCategorieTarif) {
        this.forCategorieTarif = forCategorieTarif;
    }

    /**
     * @param forLegislationTarif
     * @see ALCSTarif#LEGISLATION_AGRICOLE
     * @see ALCSTarif#LEGISLATION_CAISSE
     * @see ALCSTarif#LEGISLATION_CANTONAL
     * @see ALCSTarif#LEGISLATION_FEDERAL
     */
    public void setForLegislationTarif(String forLegislationTarif) {
        this.forLegislationTarif = forLegislationTarif;
    }

    @Override
    public Class<CategorieTarifComplexModel> whichModelClass() {
        return CategorieTarifComplexModel.class;
    }
}
