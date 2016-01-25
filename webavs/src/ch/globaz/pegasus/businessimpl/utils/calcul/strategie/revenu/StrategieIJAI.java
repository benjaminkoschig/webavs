/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 */
public class StrategieIJAI extends StrategieCalculRevenu {

    /**
     * @param montant
     * @param nbJour
     * @return
     */
    public static float calculeRevenu(String montant, String nbJour, String dateValidite) {
        float nbreJours = Float.parseFloat(nbJour);

        if (nbreJours == 0) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            String[] t = dateValidite.split("\\.");
            String year = t[t.length - 1];
            try {
                cal.setTime(dateFormat.parse(year));
            } catch (ParseException e) {
                new RuntimeException("Error during parassing this year:" + year + "for the StrategieIJAI(dateValidite:"
                        + dateValidite + ")", e);
            }
            nbreJours = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        }

        float somme = Math.round(Float.parseFloat(montant) * nbreJours);
        return somme;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeRevenu
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        String dateValidite = (String) context.get(Attribut.DATE_DEBUT_PERIODE);

        float somme = StrategieIJAI.calculeRevenu(donnee.getIJAIMontant(), donnee.getIJAIJours(), dateValidite);
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI, somme);
        return resultatExistant;
    }
}
