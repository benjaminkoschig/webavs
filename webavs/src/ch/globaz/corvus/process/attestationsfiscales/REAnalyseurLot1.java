package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur détectant les familles faisant parties du lot 1.
 * </p>
 * <p>
 * Ce lot comporte les rentes vieillesse et invalidité n'ayant pas eu de décision durant l'année fiscale voulue
 * </p>
 * 
 * @author PBA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot1 extends REAbstractAnalyseurLot {

    public REAnalyseurLot1(String annee) {
        super(annee, false, DomaineCodePrestation.AI, DomaineCodePrestation.VIEILLESSE);
    }
}
