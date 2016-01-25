package ch.globaz.eavs.model.eCH010469.common;

import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractMessage extends Ech010469Model implements EAVSNonFinalNode {

    public abstract AbstractContent getContent();

    public abstract AbstractHeader getHeader();

    public abstract void setContent(EAVSAbstractModel _content);

    public abstract void setHeader(EAVSAbstractModel _header);

}
