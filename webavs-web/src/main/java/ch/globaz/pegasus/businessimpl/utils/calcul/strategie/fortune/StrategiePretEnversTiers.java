/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDonneeFinanciere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategiePretEnversTiers extends StrategieCalculFortune implements IStrategieDessaisissable {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeFortune
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isUsufruit(donnee.getPretEnversTiersCsTypePropriete())
                && !isNuProprietaire(donnee.getPretEnversTiersCsTypePropriete())) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_PRET_ENVERS_TIERS,
                    getMontant(donnee));

        }

        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontant(donnee);
    }

    private float getMontant(CalculDonneesCC donnee) throws CalculException {
        float somme = 0f;
        if (IPCDonneeFinanciere.CS_TYPE_PROPRIETE_PROPRIETAIRE.equals(donnee.getPretEnversTiersCsTypePropriete())) {

            somme = checkAmoutAndParseAsFloat(donnee.getPretEnversTiersMontant());

            String proprieteNumerateur = donnee.getPretEnversTiersPartProprieteNumerateur();
            String proprieteDenominateur = donnee.getPretEnversTiersPartProprieteDenominateur();

            if (!JadeStringUtil.isEmpty(proprieteDenominateur) && !JadeStringUtil.isEmpty(proprieteNumerateur)) {
                float part = checkAmoutAndParseAsFloat(proprieteNumerateur)
                        / checkAmoutAndParseAsFloat(proprieteDenominateur);
                somme = part * somme;
            }

        }
        return somme;
    }
}
