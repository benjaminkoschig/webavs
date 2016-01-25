/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

/**
 * @author ECO
 * 
 */
public class StrategieFinalFortuneMobiliere implements StrategieCalculFinalisation {

    private final static String[] champs = { IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_NUMERAIRES,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TABLEAUX, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_BIJOUX,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_METAUX_PRECIEUX, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_AUTRE,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_COMPTE_BANCAIRE, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TITRES,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_PRET_ENVERS_TIERS,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_CAPITAL_LPP, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_ASSURANCE_VIE,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_ASSURANCE_RENTE_VIAGERE,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_MARCHANDISES_STOCK, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_BETAIL,
            IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_VEHICULES };

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation. StrategieCalculFinalisation
     * #calcule(ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport,
     * ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext, java.util.Date)
     */
    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {

        float somme = 0;
        for (String champ : StrategieFinalFortuneMobiliere.champs) {
            somme += donnee.getValeurEnfant(champ);
        }

        donnee.getEnfants().put(IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TOTAL,
                new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TOTAL, somme));
    }

}
