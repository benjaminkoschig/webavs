package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;

public interface ICalculDonneesHorsDroitBuilder {

    /**
     * Chargement des données dans le provider avec le search model passé en paramètre
     * 
     * @param searchModel
     */
    public ArrayList<?> loadFor(JadeAbstractSearchModel searchModel);
}
