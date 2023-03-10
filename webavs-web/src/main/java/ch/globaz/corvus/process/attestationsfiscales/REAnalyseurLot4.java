package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur d?tectant les familles faisant parties du lot 4.
 * </p>
 * <p>
 * Ce lot comporte les rentes survivant ou d'orphelin ayant eu des d?cisions (sans r?tro) durant l'ann?e fiscale voulue
 * </p>
 * 
 * @author PBA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot4 extends REAbstractAnalyseurLot1a4 {

    public REAnalyseurLot4(String annee) {
        super(annee, true, DomaineCodePrestation.SURVIVANT);
    }

    @Override
    public int getNumeroAnalyseur() {
        return 4;
    }
}
