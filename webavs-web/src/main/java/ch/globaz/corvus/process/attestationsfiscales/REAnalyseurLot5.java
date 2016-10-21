package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur d�tectant les familles faisant parties du lot 5.
 * </p>
 * <p>
 * Ce lot comporte les rentes de vieillesse et d'invalidit� ayant une d�cision avec r�tro dans l'ann�e en cours ET dans
 * les ann�e pr�c�dentes
 * </p>
 * 
 * @author LGA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot5 extends REAbstractAnalyseurLot5a8 {

    public REAnalyseurLot5(String annee) {
        super(annee, true, DomaineCodePrestation.AI, DomaineCodePrestation.VIEILLESSE);
    }

    @Override
    public int getNumeroAnalyseur() {
        return 5;
    }
}
