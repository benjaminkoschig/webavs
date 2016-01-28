package ch.globaz.pegasus.businessimpl.services.models.lot;

/**
 * Factory fournissant des instances m�tier permettant de g�rer la comptabilisation des lots.<br />
 * La classe utilise un enum afin de g�rer l'instance � retourner
 * 
 * @author sce
 * @see ch.globaz.pegasus.businessimpl.services.models.lot.PCTypeLot
 * @see ch.globaz.pegasus.businessimpl.services.models.lot.PCLotBusinessAbstract
 * @version 1.11.12 (CCJU), 1.12 Inforom
 */
public class PCLotBusinessFactory {

    /**
     * Retourne une instance en fonction du type pass� en param�tre<br />
     * Une valeur null pass� en param�tre g�n�rera un NullPointerException
     * 
     * @param instance de {@link ch.globaz.pegasus.businessimpl.services.models.lot.PCTypeLot}
     * @return instance de type PCLotBusinessAbstract
     *         {@link ch.globaz.pegasus.businessimpl.services.models.lot.PCLotBusinessAbstract}
     * 
     */
    public static PCLotBusinessAbstract newInstance(PCTypeLot typeLot) {

        if (typeLot == null) {
            throw new IllegalArgumentException(
                    "The type lot cannot be null! Cannot return an instance of PClotBusinessAbstract ["
                            + PCLotBusinessFactory.class.getCanonicalName() + "]");
        }

        switch (typeLot) {

            case DEBLOCAGE:
                return new CreateEcritureAndOvForLotDecision();

            case DECISION:
                return new CreateEcritureAndOvForLotDeblocage();

                // ne peut pas arriver, le null est d�tect� avant, et on ne pourra passer en argument qu'une instance de
                // PCTYpeLot
            default:
                return null;

        }
    }
}
