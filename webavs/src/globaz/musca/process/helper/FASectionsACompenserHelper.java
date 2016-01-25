package globaz.musca.process.helper;

import globaz.osiris.api.APISection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Classe : type_conteneur Description : Date de création: 10 sept. 04
 * 
 * @author scr
 */
public class FASectionsACompenserHelper {

    ArrayList<FASectionHelper> sectionsACompenser = null;

    /**
     * Constructor for FASectionsACompenserHelper.
     */
    public FASectionsACompenserHelper() {
    }

    public FASectionHelper getNextSectionACompenser() {

        Iterator<FASectionHelper> iter = sectionsACompenser.iterator();
        while (iter.hasNext()) {
            FASectionHelper element = iter.next();
            if (FASectionHelper.COMPENSEE != element.getStatus()) {
                return element;
            }
        }
        return null;
    }

    public void initSectionsACompenser(Collection<?> sections) {
        sectionsACompenser = new ArrayList<FASectionHelper>();
        Iterator<?> iter = sections.iterator();
        while (iter.hasNext()) {
            APISection section = (APISection) iter.next();
            FASectionHelper helper = new FASectionHelper(section);
            sectionsACompenser.add(helper);
        }
    }

    public void updateSection(FASectionHelper sectionHelper) {
        int currentIndex = sectionsACompenser.indexOf(sectionHelper);
        if (currentIndex != -1) {
            sectionsACompenser.set(currentIndex, sectionHelper);
        } else {
            sectionsACompenser.add(sectionHelper);
        }
    }
}
