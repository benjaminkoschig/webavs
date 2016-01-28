package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur détectant les familles faisant parties du lot 2.
 * </p>
 * <p>
 * Ce lot comporte les rentes survivant ou d'orphelin n'ayant pas eu de décision durant l'année fiscale voulue
 * </p>
 * 
 * @author PBA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot2 extends REAbstractAnalyseurLot {

    public REAnalyseurLot2(String annee) {
        super(annee, false, DomaineCodePrestation.SURVIVANT);
    }
}
