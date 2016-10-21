package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur détectant les familles faisant parties du lot 5.
 * </p>
 * <p>
 * Ce lot comporte les rentes de vieillesse et d'invalidité ayant une décision avec rétro dans l'année en cours ET dans
 * les année précédentes
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
