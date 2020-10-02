/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

import java.util.HashMap;

/**
 * @author ECO
 * 
 */
public class StrategieFraisGarde extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        if(context.contains(CalculContext.Attribut.REFORME) && !hasSup11Ans(context,donnee.getIdMembreFamilleSF())){
            String legende = donnee.getFraisGardeLibelle();

            TupleDonneeRapport tuple = this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAIS_GARDE_TOTAL,
                    (donnee.getFraisGardeMontant().equals("")? "0" : donnee.getFraisGardeMontant()));
            tuple.setLegende(legende);
        }

        return resultatExistant;
    }

    private boolean hasSup11Ans(CalculContext context, String idPersonne) throws CalculException {
        if(context.contains(CalculContext.Attribut.LIST_ENFANTS_SUP_11)){
            HashMap<String,String> map = (HashMap<String, String>) context.get(CalculContext.Attribut.LIST_ENFANTS_SUP_11);
            if(map.containsKey(idPersonne)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

}
