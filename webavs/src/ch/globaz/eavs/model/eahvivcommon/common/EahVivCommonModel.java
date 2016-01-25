package ch.globaz.eavs.model.eahvivcommon.common;

import ch.globaz.eavs.model.EAVSAbstractModel;

public abstract class EahVivCommonModel extends EAVSAbstractModel {
    private final static String TARGET_NAMESPACE = "eahv-iv-common";
    private final static String TARGET_URL = "http://www.eahv-iv.ch/xmlns/" + TARGET_NAMESPACE + "/";

    @Override
    public String getTargetNameSpace() {
        return EahVivCommonModel.TARGET_NAMESPACE;
    }

    @Override
    public String getTargetURL() {
        return EahVivCommonModel.TARGET_URL;
    }

}
