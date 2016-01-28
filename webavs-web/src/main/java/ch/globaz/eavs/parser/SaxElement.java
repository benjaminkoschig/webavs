package ch.globaz.eavs.parser;

import java.util.ArrayList;
import ch.globaz.eavs.model.EAVSAbstractModel;

class SaxElement {
    ArrayList liste = new ArrayList();

    EAVSAbstractModel getParent(EAVSAbstractModel anObj) {
        if (anObj == null) {
            return null;
        }
        EAVSAbstractModel result = null;
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i) == anObj) {
                if (i > 0) {
                    result = (EAVSAbstractModel) liste.get(i - 1);
                }
                break;
            }
        }
        return result;
    }

    EAVSAbstractModel peek() {
        int taille = liste.size();
        EAVSAbstractModel model = (EAVSAbstractModel) liste.get(taille);
        return model;
    }

    EAVSAbstractModel pop() {
        int taille = liste.size();
        EAVSAbstractModel model = (EAVSAbstractModel) liste.get(taille - 1);
        liste.remove(taille - 1);
        return model;
    }

    void push(EAVSAbstractModel model) {
        liste.add(model);
    }
}
