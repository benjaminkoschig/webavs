package ch.globaz.pegasus.business.models.externalmodule;

import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.AdaptationParameter;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ComptabilisationParameter;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.ExternalModuleParameters;

public enum ExternalJobActionSource {
    COMPTABILISATION(ComptabilisationParameter.class),
    ADAPTATION(AdaptationParameter.class),
    RELANCE_REST(null);

    Class<? extends ExternalModuleParameters> parameterClass;

    ExternalJobActionSource(Class<? extends ExternalModuleParameters> parameterClass) {
        this.parameterClass = parameterClass;
    }

    public Class<? extends ExternalModuleParameters> getParameterClass() {
        if (null == parameterClass) {
            throw new NullPointerException("The paramater class is null for: " + toString());
        }

        return parameterClass;
    }

}
