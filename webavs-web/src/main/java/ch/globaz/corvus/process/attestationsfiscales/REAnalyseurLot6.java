package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur détectant les familles faisant parties du lot 5.
 * </p>
 * <p>
 * Attestations fiscales des rentes de vieillesse et d'invalidité ayant une décision dans l'année avec rétro dans
 * l'année en cours et précédentes.
 * </p>
 * 
 * @author LGA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot6 extends REAbstractAnalyseurLot5a8 {

    public REAnalyseurLot6(String annee) {
        super(annee, false, DomaineCodePrestation.AI, DomaineCodePrestation.VIEILLESSE);
    }

    @Override
    public int getNumeroAnalyseur() {
        return 6;
    }
}
