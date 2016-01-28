package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;

public interface ICalculDonneesHorsDroitBuilder {

    /**
     * Chargement des donn�es dans le provider avec le search model pass� en param�tre
     * 
     * @param searchModel
     */
    public ArrayList<?> loadFor(JadeAbstractSearchModel searchModel);
}
