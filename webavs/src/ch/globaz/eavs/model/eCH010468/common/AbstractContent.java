package ch.globaz.eavs.model.eCH010468.common;

import java.util.ArrayList;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractContent extends Ech010468Model implements EAVSNonFinalNode {

    public abstract ArrayList getAnnonces();

    public abstract void setAnnonces(ArrayList _header);

}
