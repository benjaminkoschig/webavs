package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author sce
 * @version %I%, %G%
 */
public class MonnaieEtrangereBuilder implements ICalculDonneesHorsDroitBuilder {

    @Override
    public ArrayList<MonnaieEtrangere> loadFor(JadeAbstractSearchModel monnaiesSearch) {

        // LIste des variables métier
        Map<String, MonnaieEtrangere> listeMonnaies = new HashMap<String, MonnaieEtrangere>();

        for (globaz.jade.persistence.model.JadeAbstractModel absDonnee : monnaiesSearch.getSearchResults()) {

            ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere donnee = (ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere) absDonnee;
            final String strDateDebut = "01." + donnee.getSimpleMonnaieEtrangere().getDateDebut();
            Date vmDateDebut = JadeDateUtil.getGlobazDate(strDateDebut);
            String csTypeMonnaieEtrangere = donnee.getSimpleMonnaieEtrangere().getCsTypeMonnaie();

            // Si la liste ne contient pas la monnaie, on instancie un nouvel objet Monnaieetrangere
            if (!listeMonnaies.containsKey(csTypeMonnaieEtrangere)) {
                MonnaieEtrangere monnaie = new MonnaieEtrangere();
                monnaie.setCsTypeMonnaieEtrangere(csTypeMonnaieEtrangere);
                monnaie.getMonnaiesEtrangeres()
                        .put(vmDateDebut.getTime(), donnee.getSimpleMonnaieEtrangere().getTaux());
                listeMonnaies.put(csTypeMonnaieEtrangere, monnaie);
            } else {
                // La variable metier a deja ete crée on ajoute une autre periode
                MonnaieEtrangere monnaie = listeMonnaies.get(csTypeMonnaieEtrangere);
                monnaie.getMonnaiesEtrangeres()
                        .put(vmDateDebut.getTime(), donnee.getSimpleMonnaieEtrangere().getTaux());
            }

        }

        return new ArrayList<MonnaieEtrangere>(listeMonnaies.values());
    }

}
