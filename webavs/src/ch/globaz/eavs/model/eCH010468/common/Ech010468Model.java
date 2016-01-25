package ch.globaz.eavs.model.eCH010468.common;

import ch.globaz.eavs.model.EAVSAbstractModel;

public abstract class Ech010468Model extends EAVSAbstractModel {
    private static final String TARGET_NAMESPACE = "eCH-0104-68";
    private static final String TARGET_URL = "http://www.ech.ch/xmlns/" + TARGET_NAMESPACE + "/";

    @Override
    public String getTargetNameSpace() {
        return Ech010468Model.TARGET_NAMESPACE;
    }

    @Override
    public String getTargetURL() {
        return Ech010468Model.TARGET_URL;
    }

}
