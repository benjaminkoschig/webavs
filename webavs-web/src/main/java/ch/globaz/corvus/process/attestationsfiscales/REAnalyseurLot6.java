package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur d�tectant les familles faisant parties du lot 5.
 * </p>
 * <p>
 * Ce lot comporte les rentes de vieillesse et d'invalidit� ayant une d�cision avec r�tro dans l'ann�e en cours
 * uniquement.
 * </p>
 * 
 * @author LGA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot6 extends REAbstractAnalyseurLot5a8 {

    public REAnalyseurLot6(String annee) {
        super(annee, false, DomaineCodePrestation.AI, DomaineCodePrestation.VIEILLESSE);
    }

    @Override
    public int getNumeroAnalyseur() {
        return 6;
    }
}
