package ch.globaz.al.businessimpl.generation.prestations;

import java.math.BigDecimal;
import java.util.ArrayList;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe de génération de prestation avec montant forcé au niveau du dossier. Lorsqu'un montant est forcé au niveau du
 * dossier, il est utilisé pour chaque mois de la période généré
 * 
 * @author jts
 */
public class GenPrestationForceeDossier extends GenPrestationForcee {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations nécessaires à la génération
     */
    public GenPrestationForceeDossier(ContextAffilie context) {
        super(context);
    }

    /**
     * Retourne le prochain montant à traiter
     * 
     * @return le prochain montant à traiter
     * 
     */
    @Override
    protected BigDecimal getNextMontant() {
        return montantsParMois.get(0);
    }

    /**
     * Détermine le montant par mois en fonction du montant forcé et du nombre de mois à générer
     */
    @Override
    protected void setMontantParMois() {
        montantsParMois = new ArrayList<BigDecimal>();
        montantsParMois.add(new BigDecimal(context.getContextDossier().getMontantForce()));
    }
}
