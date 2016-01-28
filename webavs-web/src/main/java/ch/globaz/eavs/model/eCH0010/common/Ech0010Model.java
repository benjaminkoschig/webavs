package ch.globaz.eavs.model.eCH0010.common;

import ch.globaz.eavs.model.EAVSAbstractModel;

public abstract class Ech0010Model extends EAVSAbstractModel {
    private static final String TARGET_NAMESPACE = "eCH-0010";
    private static final String TARGET_URL = "http://www.ech.ch/xmlns/" + TARGET_NAMESPACE + "/";

    @Override
    public String getTargetNameSpace() {
        return TARGET_NAMESPACE;
    }

    @Override
    public String getTargetURL() {
        return Ech0010Model.TARGET_URL;
    }
}
