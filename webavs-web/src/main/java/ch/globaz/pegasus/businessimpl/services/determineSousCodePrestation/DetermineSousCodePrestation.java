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
     * Retourne une instance de la classe � utiliser pour d�finir les sous types genre de prestation. Si la
     * propri�t� common.prestation.sousTypesGenrePrestationActif est � false on ne devrait pas utiliser cette classe.
     * Sinon on va d�terminer le nom de la class � utiliser en se basant sur la propri�t� pegasus.pegasus.cantonCaisse.
     * En r�sum�, lorsque la propri�t� common.prestation.sousTypesGenrePrestationActif est a true il faut cr�er une
     * classe sp�cifique.
     * 
     * @return la Classe qui doit d�finir le sous type genre de prestation
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
     * Permet de d�terminer le code syst�me de la rubrique de restitution � utiliser.
     * 
     * @param periode
     * @param isConjoint
     * @return
     * @throws JadeApplicationException
     */
    public abstract String determineSousCode(PeriodePCAccordee periode, boolean isConjoint)
            throws JadeApplicationException;
}
