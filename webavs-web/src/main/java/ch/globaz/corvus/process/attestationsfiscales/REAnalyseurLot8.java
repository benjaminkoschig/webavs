package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur détectant les familles faisant parties du lot 5.
 * </p>
 * <p>
 * Ce lot comporte les rentes de survivants ayant une décision avec rétro dans l'année en cours uniquement.
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
