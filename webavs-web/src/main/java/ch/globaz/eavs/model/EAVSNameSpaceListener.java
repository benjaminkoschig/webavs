package ch.globaz.eavs.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ch.globaz.eavs.exception.EAVSNoVersionNumberException;
import ch.globaz.eavs.utils.EAVSUtils;

public class EAVSNameSpaceListener {
    private Map nameSpaces = new HashMap();

    public StringBuffer getNameSpaces() {
        StringBuffer result = new StringBuffer();
        Set allKeys = nameSpaces.keySet();
        Iterator iter = allKeys.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) nameSpaces.get(key);
            result.append(" xmlns:" + key + "=\"" + value + "\"" + "\n");
        }
        return result;
    }

    public void onAddNode(EAVSAbstractModel aNode) {
        String key = aNode.getTargetNameSpace();
        String value = aNode.getTargetURL() + EAVSUtils.getPackageVersion(aNode);
        String oldValue = (String) nameSpaces.get(key);
        if (oldValue != null) {
            if (!oldValue.equals(value)) {
                throw new EAVSNoVersionNumberException("other version for the same namespace detected : \n" + value
                        + "\n" + oldValue);
            }
        } else {
            nameSpaces.put(key, value);
        }
    }
}
