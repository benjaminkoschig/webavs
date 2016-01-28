package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur détectant les familles faisant parties du lot 3.
 * </p>
 * <p>
 * Ce lot comporte les rentes vieillesse et invalidité ayant eu des décisions durant l'année fiscale voulue
 * </p>
 * 
 * @author PBA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot3 extends REAbstractAnalyseurLot {

    public REAnalyseurLot3(String annee) {
        super(annee, true, DomaineCodePrestation.AI, DomaineCodePrestation.VIEILLESSE);
    }
}
