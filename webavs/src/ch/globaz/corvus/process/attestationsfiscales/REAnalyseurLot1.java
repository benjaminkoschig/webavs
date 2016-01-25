package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur d�tectant les familles faisant parties du lot 1.
 * </p>
 * <p>
 * Ce lot comporte les rentes vieillesse et invalidit� n'ayant pas eu de d�cision durant l'ann�e fiscale voulue
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
