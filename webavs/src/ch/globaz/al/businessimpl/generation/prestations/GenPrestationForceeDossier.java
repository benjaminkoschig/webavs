package ch.globaz.al.businessimpl.generation.prestations;

import java.math.BigDecimal;
import java.util.ArrayList;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe de g�n�ration de prestation avec montant forc� au niveau du dossier. Lorsqu'un montant est forc� au niveau du
 * dossier, il est utilis� pour chaque mois de la p�riode g�n�r�
 * 
 * @author jts
 */
public class GenPrestationForceeDossier extends GenPrestationForcee {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations n�cessaires � la g�n�ration
     */
    public GenPrestationForceeDossier(ContextAffilie context) {
        super(context);
    }

    /**
     * Retourne le prochain montant � traiter
     * 
     * @return le prochain montant � traiter
     * 
     */
    @Override
    protected BigDecimal getNextMontant() {
        return montantsParMois.get(0);
    }

    /**
     * D�termine le montant par mois en fonction du montant forc� et du nombre de mois � g�n�rer
     */
    @Override
    protected void setMontantParMois() {
        montantsParMois = new ArrayList<BigDecimal>();
        montantsParMois.add(new BigDecimal(context.getContextDossier().getMontantForce()));
    }
}
