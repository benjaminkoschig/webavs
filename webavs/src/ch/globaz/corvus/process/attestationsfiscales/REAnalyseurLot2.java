package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur d�tectant les familles faisant parties du lot 2.
 * </p>
 * <p>
 * Ce lot comporte les rentes survivant ou d'orphelin n'ayant pas eu de d�cision durant l'ann�e fiscale voulue
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
