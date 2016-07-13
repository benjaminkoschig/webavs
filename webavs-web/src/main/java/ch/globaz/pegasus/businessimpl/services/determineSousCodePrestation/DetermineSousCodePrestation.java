package ch.globaz.pegasus.businessimpl.services.determineSousCodePrestation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;

public abstract class DetermineSousCodePrestation {

    private static final String PACKAGE = "ch.globaz.pegasus.businessimpl.services.determineSousCodePrestation.";

    /**
     * Retourne une instance de la classe à utiliser pour définir les sous types genre de prestation. Si la
     * propriété common.prestation.sousTypesGenrePrestationActif est à false on ne devrait pas utiliser cette classe.
     * Sinon on va déterminer le nom de la class à utiliser en se basant sur la propriété pegasus.pegasus.cantonCaisse.
     * En résumé, lorsque la propriété common.prestation.sousTypesGenrePrestationActif est a true il faut créer une
     * classe spécifique.
     * 
     * @return la Classe qui doit définir le sous type genre de prestation
     * @throws JadeApplicationException
     */
    public static DetermineSousCodePrestation factory() throws JadeApplicationException {
        String nameClass = null;
        DetermineSousCodePrestation sousCode = null;
        String cantonCaisse = EPCProperties.CANTON_CAISSE.getValue();
        if (CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getBooleanValue()) {
            try {
                nameClass = JadeStringUtil.firstLetterToUpperCase(cantonCaisse.toLowerCase()) + "SousCodePrestation";
                sousCode = (DetermineSousCodePrestation) Class.forName(PACKAGE + nameClass).newInstance();
            } catch (InstantiationException e) {
                throw new CalculException("Unable to instantiate the class: " + nameClass + " ");
            } catch (IllegalAccessException e) {
                throw new CalculException("IllegalAcess to this class: " + nameClass + " ");
            } catch (ClassNotFoundException e) {
                throw new CalculException(
                        "Unable to find this class: "
                                + nameClass
                                + " This class is usde to find witch 'sousTypeGenrePrestation' we must use because the properties : "
                                + CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getPropertyName()
                                + " is defined to true. ");
            }
        } else {
            throw new CalculException("The properties: "
                    + CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getPropertyName() + "is false and this class("
                    + DetermineSousCodePrestation.class.getName() + ") must not be used");
        }
        return sousCode;
    }

    /**
     * Permet de déterminer le code système de la rubrique de restitution à  utiliser.
     * 
     * @param periode
     * @param isConjoint
     * @return
     * @throws JadeApplicationException
     */
    public abstract String determineSousCode(PeriodePCAccordee periode, boolean isConjoint)
            throws JadeApplicationException;
}
