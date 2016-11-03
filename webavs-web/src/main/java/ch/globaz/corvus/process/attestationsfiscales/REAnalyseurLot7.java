package ch.globaz.corvus.process.attestationsfiscales;

import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * <p>
 * Analyseur d�tectant les familles faisant parties du lot 5.
 * </p>
 * <p>
 * Attestations fiscales des rentes de survivants ayant une d�cision dans l'ann�e avec r�tro dans l'ann�e en cours
 * uniquement.
 * </p>
 * 
 * @author LGA
 * @see REAbstractAnalyseurLot
 */
public class REAnalyseurLot7 extends REAbstractAnalyseurLot5a8 {

    public REAnalyseurLot7(String annee) {
        super(annee, true, DomaineCodePrestation.SURVIVANT);
    }

    @Override
    public int getNumeroAnalyseur() {
        return 7;
    }
}
