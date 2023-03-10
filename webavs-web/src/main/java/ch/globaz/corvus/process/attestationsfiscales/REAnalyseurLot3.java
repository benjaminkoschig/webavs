package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur d?tectant les familles faisant parties du lot 3.
 * </p>
 * <p>
 * Ce lot comporte les rentes vieillesse et invalidit? ayant eu des d?cisions (sans r?tro) durant l'ann?e fiscale voulue
 * </p>
 * 
 * @author PBA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot3 extends REAbstractAnalyseurLot1a4 {

    public REAnalyseurLot3(String annee) {
        super(annee, true, DomaineCodePrestation.AI, DomaineCodePrestation.VIEILLESSE);
    }

    @Override
    public int getNumeroAnalyseur() {
        return 3;
    }
}
