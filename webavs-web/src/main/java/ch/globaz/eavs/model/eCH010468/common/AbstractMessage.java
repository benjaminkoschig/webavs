package ch.globaz.eavs.model.eCH010468.common;

import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractMessage extends Ech010468Model implements EAVSNonFinalNode {

    public abstract AbstractContent getContent();

    public abstract AbstractHeader getHeader();

    public abstract void setAbstractContent(EAVSAbstractModel _content);

    public abstract void setAbstractHeader(EAVSAbstractModel _header);

}
