package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense;

import java.util.Date;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCLoiCantonaleProperty;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.ProxyCalculDates;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategiesFinalDepenseTotalReconnu.StrategieFinalDepenseTotalReconnu;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategiesFinalDepenseTotalReconnu.StrategieFinalDepenseTotalReconnu201501;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategiesFinalDepenseTotalReconnu.StrategieFinalDepenseTotalReconnuVS;

public class ProxyFinalDepenseTotalReconnu implements StrategieCalculFinalisation {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        // En fonction de la date postérieur au 01012015, on aiguille sur la nouvelle stratégie
        if (isForLVPC(dateDebut)) {
            new StrategieFinalDepenseTotalReconnu201501().calcule(donnee, context, dateDebut);
        } else if (isCantonVS()) {
            new StrategieFinalDepenseTotalReconnuVS().calcule(donnee, context, dateDebut);
        } else {
            new StrategieFinalDepenseTotalReconnu().calcule(donnee, context, dateDebut);
        }

    }

    // Définis si la stratégie est applicable pour la LVPC à partie de 2015
    private boolean isForLVPC(Date dateDebut) throws CalculException {

        try {
            return EPCLoiCantonaleProperty.VAUD.isLoiCantonPC()
                    && dateDebut.getTime() >= ProxyCalculDates.DEPENSE_TOTAL_RECONNUES_SWITCH_STRATEGY_DATE.timestamp;
            // Prop LVPC deprecated
            // return EPCProperties.LVPC.getBooleanValue()
            // && dateDebut.getTime() >= ProxyCalculDates.DEPENSE_TOTAL_RECONNUES_SWITCH_STRATEGY_DATE.timestamp;
        } catch (PropertiesException e) {
            throw new CalculException(e.getMessage());
        }
    }

    private boolean isCantonVS() throws CalculException {
        try {
            return EPCLoiCantonaleProperty.VALAIS.isLoiCantonPC();
        } catch (PropertiesException e) {
            throw new CalculException(e.getMessage());
        }
    }

}
