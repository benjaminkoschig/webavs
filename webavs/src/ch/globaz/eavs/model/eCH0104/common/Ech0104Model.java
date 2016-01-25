package ch.globaz.eavs.model.eCH0104.common;

import ch.globaz.eavs.model.EAVSAbstractModel;

public abstract class Ech0104Model extends EAVSAbstractModel {
    private static final String TARGET_NAMESPACE = "eCH-0104";
    private static final String TARGET_URL = "http://www.ech.ch/xmlns/" + TARGET_NAMESPACE + "/";

    @Override
    public String getTargetNameSpace() {
        return Ech0104Model.TARGET_NAMESPACE;
    }

    @Override
    public String getTargetURL() {
        return Ech0104Model.TARGET_URL;
    }

}
