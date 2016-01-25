package ch.globaz.eavs.model.eahviv2011000104.common;

import ch.globaz.eavs.model.EAVSAbstractModel;

public abstract class CommonModel extends EAVSAbstractModel {
    private static final String TARGET_NAMESPACE = "eahv-iv-2011-000104";
    private static final String TARGET_URL = "http://www.eahv-iv.ch/xmlns/" + CommonModel.TARGET_NAMESPACE + "/";

    @Override
    public String getTargetNameSpace() {
        return CommonModel.TARGET_NAMESPACE;
    }

    @Override
    public String getTargetURL() {
        return CommonModel.TARGET_URL;
    }
}
