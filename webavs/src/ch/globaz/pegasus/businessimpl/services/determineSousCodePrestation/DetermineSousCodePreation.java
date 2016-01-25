package ch.globaz.pegasus.businessimpl.services.determineSousCodePrestation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;

public abstract class DetermineSousCodePreation {

    /**
     * Permet de trouve la class qui vas être utilisé pour définir les sous type genre de prestation. Si la propriété
     * common.prestation.sousTypesGenrePrestationActif est à false on ne devrait pas utiliser cette class. Sinon on va
     * déterminer le nom de la class a utiliser en ce basant sur cette propriété pegasus.pegasus.cantonCaisse. Ce qui
     * veut dire que lorsque la propriété common.prestation.sousTypesGenrePrestationActif est a true il faut créer un
     * class spécifique.
     * 
     * @return la Class qui doit définir le sous type genre de prestation
     * @throws JadeApplicationException
     */
    public static DetermineSousCodePreation factory() throws JadeApplicationException {
        String nameClass = null;
        DetermineSousCodePreation sousCode = null;
        String cantonCaisse = EPCProperties.CANTON_CAISSE.getValue();
        if (CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getBooleanValue()) {

            try {
                nameClass = JadeStringUtil.firstLetterToUpperCase(cantonCaisse.toLowerCase()) + "SousCodePrestation";
                sousCode = (DetermineSousCodePreation) Class.forName(
                        "ch.globaz.pegasus.businessimpl.services.determineSousCodePrestation." + nameClass)
                        .newInstance();
            } catch (InstantiationException e) {
                throw new CalculException("Unable to insantiat the class: " + nameClass + " ");
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
                    + DetermineSousCodePreation.class.getName() + ") must not be used");
        }
        return sousCode;
    }

    public abstract String detetrmineSousCode(PeriodePCAccordee periode, boolean isConjoint)
            throws JadeApplicationException;
}
