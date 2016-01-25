package ch.globaz.pegasus.businessimpl.services.models.lot;

/**
 * Factory fournissant des instances métier permettant de gérer la comptabilisation des lots.<br />
 * La classe utilise un enum afin de gérer l'instance à retourner
 * 
 * @author sce
 * @see ch.globaz.pegasus.businessimpl.services.models.lot.PCTypeLot
 * @see ch.globaz.pegasus.businessimpl.services.models.lot.PCLotBusinessAbstract
 * @version 1.11.12 (CCJU), 1.12 Inforom
 */
public class PCLotBusinessFactory {

    /**
     * Retourne une instance en fonction du type passé en paramètre<br />
     * Une valeur null passé en paramètre générera un NullPointerException
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

                // ne peut pas arriver, le null est détecté avant, et on ne pourra passer en argument qu'une instance de
                // PCTYpeLot
            default:
                return null;

        }
    }
}
