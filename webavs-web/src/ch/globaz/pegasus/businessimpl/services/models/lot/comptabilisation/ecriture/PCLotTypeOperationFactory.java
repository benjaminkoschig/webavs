package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.api.lots.IRELot;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * 
 * Enum effectuant le mapping entre le type de lot et l'operation de compatabilisation. Joue également le role de
 * Factory en founisssant l'implémentation adéquate de l'interface <code>GenerateOperations<code>
 * 
 * @author sce
 * @version 1.12
 * @see ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GenerateOperations
 */
public enum PCLotTypeOperationFactory {

    DEBLOCAGE(IRELot.CS_TYP_LOT_DEBLOCAGE_RA, GenerateOperationsBlocage.class, AfterPersisterDeblocageOperation.class),
    DECISION(IRELot.CS_TYP_LOT_DECISION, GenerateOperationsApresCalcul.class, null),
    DECISION_RESTITUTION(IRELot.CS_TYP_LOT_DECISION_RESTITUTION, GenerateOperationsApresCalcul.class, null);

    /**
     * Retourne l'instance d'enum correspondant au <code>csTypelotOperation</code> passé en paramètre.
     * 
     * @param csTypeLotOperation
     * @return
     * @thrwow IllegalAtgumentException si <code>csTypelotOperation</code> null, ou non présent dans l'enum
     */
    public static PCLotTypeOperationFactory csTypeOf(String csTypeLotOperation) {

        if (csTypeLotOperation == null) {
            throw new IllegalArgumentException("The csTypeLot to match the enum cannot be null [TypeOperation]");
        }

        PCLotTypeOperationFactory returnType = null;

        for (PCLotTypeOperationFactory type : PCLotTypeOperationFactory.values()) {
            if (type.getCsTypeLotForOperation().equals(csTypeLotOperation)) {
                returnType = type;
            }
        }

        if (returnType == null) {
            throw new IllegalArgumentException("The csTypeLot to match the enum is not present: [" + csTypeLotOperation
                    + "] [TypeOperation]");
        }

        return returnType;
    }

    private String csTypelotOperation = null;
    private Class<? extends AfterPersisterOperations> persistClass = null;
    private Class<? extends GenerateOperations> treatClass = null;

    PCLotTypeOperationFactory(String csTypeLotOperation, Class<? extends GenerateOperations> treatClass,
            Class<? extends AfterPersisterOperations> persistClass) {
        csTypelotOperation = csTypeLotOperation;
        this.treatClass = treatClass;
        this.persistClass = persistClass;
    }

    public void afterPersist(SimpleLot simpleLot) throws PegasusException, JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {
        if (persistClass != null) {
            try {
                persistClass.newInstance().afterPersist(simpleLot);
            } catch (InstantiationException e) {
                throw new RuntimeException("Unalbled to instancied the class", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unalbled to instancied the class illegalAccess", e);
            }
        }
    }

    String getCsTypeLotForOperation() {
        return csTypelotOperation;
    }

    public GenerateOperations getTreatImplementation() {
        try {
            return treatClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unalbled to instancied the class", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unalbled to instancied the class illegalAccess", e);
        }
    }
}
