package ch.globaz.eavs.model.eCH0058.common;

import ch.globaz.eavs.model.EAVSAbstractModel;

public abstract class Ech0058Model extends EAVSAbstractModel {
    private static final String TARGET_NAMESPACE = "eCH-0058";
    private static final String TARGET_URL = "http://www.ech.ch/xmlns/" + TARGET_NAMESPACE + "/";

    @Override
    public String getTargetNameSpace() {
        return TARGET_NAMESPACE;
    }

    @Override
    public String getTargetURL() {
        return Ech0058Model.TARGET_URL;
    }

}
