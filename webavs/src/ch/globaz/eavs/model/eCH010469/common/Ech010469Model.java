package ch.globaz.eavs.model.eCH010469.common;

import ch.globaz.eavs.model.EAVSAbstractModel;

public abstract class Ech010469Model extends EAVSAbstractModel {
    private static final String TARGET_NAMESPACE = "eCH-0104-69";
    private static final String TARGET_URL = "http://www.ech.ch/xmlns/" + Ech010469Model.TARGET_NAMESPACE + "/";

    @Override
    public String getTargetNameSpace() {
        return Ech010469Model.TARGET_NAMESPACE;
    }

    @Override
    public String getTargetURL() {
        return Ech010469Model.TARGET_URL;
    }

}
