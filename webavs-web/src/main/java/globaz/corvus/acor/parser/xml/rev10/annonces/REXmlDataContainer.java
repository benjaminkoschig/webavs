package globaz.corvus.acor.parser.xml.rev10.annonces;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SCR
 * 
 */
public class REXmlDataContainer {

    REAnnonce09XmlDataStructure[] annonces09 = null;
    REAnnonce10XmlDataStructure[] annonces10 = null;

    private List lAnnonce09 = new ArrayList();
    private List lAnnonce10 = new ArrayList();

    public void addAnnonces09(REAnnonce09XmlDataStructure annonces09) {
        if (annonces09 == null) {
            return;
        }

        lAnnonce09.add(annonces09);
    }

    public void addAnnonces10(REAnnonce10XmlDataStructure annonces10) {
        if (annonces10 == null) {
            return;
        }
        lAnnonce10.add(annonces10);
    }

    public REAnnonce09XmlDataStructure[] getAnnonces09() {
        return (REAnnonce09XmlDataStructure[]) lAnnonce09.toArray(new REAnnonce09XmlDataStructure[lAnnonce09.size()]);
    }

    public REAnnonce10XmlDataStructure[] getAnnonces10() {
        return (REAnnonce10XmlDataStructure[]) lAnnonce10.toArray(new REAnnonce10XmlDataStructure[lAnnonce10.size()]);
    }

}
