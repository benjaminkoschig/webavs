package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.mixte;

import ch.globaz.pegasus.business.constantes.EPCLienParentePA;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.RentePereMere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieCalculRevenu;

/**
 * Strategie pension alimentaire mixte Sert � g�rer les d�ductions des rentes d'enfants lors de versement de pension
 * li�s au p�re ou � la m�re
 * 
 * @author sce
 * 
 */
public class StrategiePensionAlimentaireMixte extends StrategieCalculRevenu {

    private static final float NB_MOIS = 12;

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        // ON r�cup�re le genre de rente, si ce n'est pas une rente enfant, on ne traite pas la d�duction
        String csCodeGenreRente = resultatExistant.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_ENFANT);
        Boolean deductionEnfant = donnee.getPensionAlimentaireIsDeductionsRenteEnfant();

        if ((csCodeGenreRente != null) && deductionEnfant) {
            // on recup�re le montant de la pension
            float montantPension = StrategiePensionAlimentaireMixte.NB_MOIS
                    * checkAmountAndParseAsFloat(donnee.getPensionAlimentaireMontant());

            // Si le montant diff�rent de 0, on traite
            if (montantPension > 0.0f) {
                // recupetaion du montant de la rente avs/ai
                float montantAvsAi = resultatExistant.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL);

                // lien de parent�
                String lienParente = donnee.getPensionAlimentaireLienParente();

                // Si pere ou mere
                if (lienParente.equals(EPCLienParentePA.LIEN_MERE.getCsLienParente())) {
                    // Si la rente est sujette � d�duction
                    if (RentePereMere.isCsPresentsInEnumByType(RentePereMere.TYPE_MERE, csCodeGenreRente)) {
                        montantPension = Math.max(montantPension - montantAvsAi, 0.0f);
                    }
                } else if (lienParente.equals(EPCLienParentePA.LIEN_PERE.getCsLienParente())) {
                    // Si la rente est sujette � d�duction
                    if (RentePereMere.isCsPresentsInEnumByType(RentePereMere.TYPE_PERE, csCodeGenreRente)) {
                        montantPension = Math.max(montantPension - montantAvsAi, 0.0f);
                    }
                }

            }

            ecraseChildExistant(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_PENSION_ALIM_RECUE,
                    montantPension);
        }

        return resultatExistant;
    }

}
