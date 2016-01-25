/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCIJAPG;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;

/**
 * @author ECO
 */
public class StrategieIJAPG extends StrategieCalculRevenu {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeRevenu
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        final int duree = (Integer) context.get(CalculContext.Attribut.DUREE_ANNEE);

        String genre = donnee.getIJAPGGenre();

        float montant = this.calculeRevenu(donnee.getIJAPGMontant(), donnee.getIJAPGMontantChomage(),
                donnee.getIJAPGnbJours(), donnee.getIJAPGtauxAVS(), donnee.getIJAPGtauxAA(), donnee.getIJAPGLPP(),
                donnee.getIJAPGgainIntAnnuel(),
                ((ControlleurVariablesMetier) context.get(Attribut.MENSUALISATION_IJ_CHOMAGE)).getValeurCourante(),
                duree);

        // float montant = this.checkAmountAndParseAsFloat(donnee.getIJAPGMontant()) * duree;

        if (IPCIJAPG.CS_GENRE_PRESTATION_IJ_APG.equals(genre)) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_APG, montant);
        } else if (IPCIJAPG.CS_GENRE_PRESTATION_IJ_AA.equals(genre)) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAA, montant);
        } else if (IPCIJAPG.CS_GENRE_PRESTATION_IJ_AM.equals(genre)) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAM, montant);
        } else if (IPCIJAPG.CS_GENRE_PRESTATION_IJ_CHOMAGE.equals(genre)) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_CHOMAGE, montant);
        } else if (IPCIJAPG.CS_GENRE_PRESTATION_IJ_LAMAL.equals(genre)) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAMAL, montant);
        } else if (IPCIJAPG.CS_GENRE_PRESTATION_IJ_AUTRES.equals(genre)) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_IJ, montant);
            // TODO ajouter champ "texte libre"
        }

        return resultatExistant;
    }

    public float calculeRevenu(String ijApgMontant, String montantChomage, String nbJours, String tauxAvs,
            String tauxAa, String lpp, String gainIntAnnuel, String mensualisationIjChomage, final int duree) {
        float montant;
        // Gestion montant chomage ou standard
        if (JadeStringUtil.isBlankOrZero(ijApgMontant)) {
            Float joursMensualisation = Float.parseFloat(mensualisationIjChomage);
            montant = getAndComputeMontantIGAPGForAC(montantChomage, nbJours, tauxAvs, tauxAa, lpp, gainIntAnnuel,
                    joursMensualisation);
        } else {
            montant = checkAmountAndParseAsFloat(ijApgMontant) * duree;
        }
        return montant;
    }

    /**
     * Calcul spécifique pour allocation chomage
     * 
     * @return
     */
    private float getAndComputeMontantIGAPGForAC(String montantChomage, String nbJours, String tauxAvs, String tauxAa,
            String lpp, String gainIntAnnuel, Float joursMensualisation) {
        // Montant brut AC - obligatoire
        float montantBrutAC = Float.parseFloat(montantChomage);

        // Nombre jours concernées pour LPP - obligatoire
        Integer nbreJours = Integer.parseInt(nbJours);

        // nbre jours mensualisation
        float nbreJoursMensualisation = joursMensualisation;// 21.7f;

        // **************************** Annualisation IJ ************************
        float montantIJAnnualise = montantBrutAC * 12 * nbreJoursMensualisation;
        // montantIJAnnualise.setScale(2, BigDecimal.ROUND_DOWN);

        // **************************** Calcul des déductions ************************
        float deductionAvs = 0f;
        // Si taux avs diff de 0
        if (!JadeStringUtil.isBlankOrZero(tauxAvs)) {
            deductionAvs = (montantIJAnnualise * Float.parseFloat(tauxAvs)) / 100.0f;
        }

        float deductionAA = 0f;
        if (!JadeStringUtil.isBlankOrZero(tauxAa)) {
            deductionAA = (montantIJAnnualise * Float.parseFloat(tauxAa)) / 100.0f;
        }
        float montantLPP = 0f;

        if (!JadeStringUtil.isBlankOrZero(lpp)) {
            montantLPP = ((Float.parseFloat(lpp) / nbreJours) * nbreJoursMensualisation) * 12;
        }
        float deductionsTotal = deductionAvs + deductionAA + montantLPP;

        // **************************** Sous total avant gain intermédiaire ************************
        float montantAnnuelIjNEt = montantIJAnnualise - deductionsTotal;

        // **************************** Total avec gain intermédiaire ************************
        float gainIntermediaireAnnuel = 0f;
        if (!JadeStringUtil.isBlankOrZero(gainIntAnnuel)) {
            gainIntermediaireAnnuel = Float.parseFloat(gainIntAnnuel);
        }
        montantAnnuelIjNEt = montantAnnuelIjNEt - gainIntermediaireAnnuel;

        return montantAnnuelIjNEt;

    }
}
