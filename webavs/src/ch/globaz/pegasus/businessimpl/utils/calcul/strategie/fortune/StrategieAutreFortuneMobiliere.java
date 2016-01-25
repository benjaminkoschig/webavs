/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutreFortuneMobiliere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategieAutreFortuneMobiliere extends StrategieCalculFortune implements IStrategieDessaisissable {

    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isUsufruit(donnee.getAutreFortuneMobiliereCsTypePropriete())
                && !isNuProprietaire(donnee.getAutreFortuneMobiliereCsTypePropriete())) {
            float montant = getMontant(donnee);
            String cleFortuneMobiliere = getCleFortuneMobiliere(donnee);
            this.getOrCreateChild(resultatExistant, cleFortuneMobiliere, montant);

        }

        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontant(donnee);
    }

    private String getCleFortuneMobiliere(CalculDonneesCC donnee) throws CalculException {
        String cleFortuneMobiliere = null;
        if (isProprietaire(donnee.getAutreFortuneMobiliereCsTypePropriete())) {

            final String csTypeFortune = donnee.getAutreFortuneMobiliereCsTypeFortune();

            if (IPCAutreFortuneMobiliere.CS_TYPE_FORTUNE_TABLEAUX.equals(csTypeFortune)) {
                cleFortuneMobiliere = IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TABLEAUX;
            } else if (IPCAutreFortuneMobiliere.CS_TYPE_FORTUNE_BIJOUX.equals(csTypeFortune)) {
                cleFortuneMobiliere = IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_BIJOUX;
            } else if (IPCAutreFortuneMobiliere.CS_TYPE_FORTUNE_METAUX_PRECIEUX.equals(csTypeFortune)) {
                cleFortuneMobiliere = IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_METAUX_PRECIEUX;
            } else if (IPCAutreFortuneMobiliere.CS_TYPE_FORTUNE_TIMBRES.equals(csTypeFortune)) {
                cleFortuneMobiliere = IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TIMBRES;
            } else if (IPCAutreFortuneMobiliere.CS_TYPE_FORTUNE_AUTRES.equals(csTypeFortune)) {
                cleFortuneMobiliere = IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_AUTRE;

                // TupleDonneeRapport tupleAutre =
                // this.getOrCreateChild(resultatExistant, cleFortuneMobiliere,
                // 0f);
                //
                // getOrCreateChild(tupleAutre,donnee.getAutreFortuneMobiliereMontant(),);

            } else {
                throw new CalculException("Unknown csTypeFortune from AutreFortuneMobiliere : " + csTypeFortune);
            }

        }
        return cleFortuneMobiliere;
    }

    private float getMontant(CalculDonneesCC donnee) throws CalculException {
        float montant = 0f;
        if (isProprietaire(donnee.getAutreFortuneMobiliereCsTypePropriete())) {

            float fraction = checkAmoutAndParseAsFloat(donnee.getAutreFortuneMobiliereFractionNumerateur())
                    / checkAmoutAndParseAsFloat(donnee.getAutreFortuneMobiliereFractionDenominateur());

            montant = checkAmountAndParseAsFloat(donnee.getAutreFortuneMobiliereMontant()) * fraction;
        }
        return montant;
    }

}
