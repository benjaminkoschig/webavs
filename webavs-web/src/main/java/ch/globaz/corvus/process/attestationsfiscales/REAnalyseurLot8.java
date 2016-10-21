package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur d�tectant les familles faisant parties du lot 5.
 * </p>
 * <p>
 * Ce lot comporte les rentes de survivants ayant une d�cision avec r�tro dans l'ann�e en cours uniquement.
 * </p>
 * 
 * @author LGA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot8 extends REAbstractAnalyseurLot5a8 {

    public REAnalyseurLot8(String annee) {
        super(annee, false, DomaineCodePrestation.SURVIVANT);
    }

    @Override
    public int getNumeroAnalyseur() {
        return 8;
    }
}
