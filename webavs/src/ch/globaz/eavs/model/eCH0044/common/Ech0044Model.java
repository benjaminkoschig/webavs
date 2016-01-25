package ch.globaz.eavs.model.eCH0044.common;

import ch.globaz.eavs.model.EAVSAbstractModel;

public abstract class Ech0044Model extends EAVSAbstractModel {
    private static final String TARGET_NAMESPACE = "eCH-0044";
    private static final String TARGET_URL = "http://www.ech.ch/xmlns/" + TARGET_NAMESPACE + "/";

    @Override
    public String getTargetNameSpace() {
        return TARGET_NAMESPACE;
    }

    @Override
    public String getTargetURL() {
        return Ech0044Model.TARGET_URL;
    }

}
