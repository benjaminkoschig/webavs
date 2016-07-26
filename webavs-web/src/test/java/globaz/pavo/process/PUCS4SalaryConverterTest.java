package globaz.pavo.process;

import static org.fest.assertions.api.Assertions.*;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.swissdec.schema.sd._20130514.salarydeclarationconsumercontainer.DeclareSalaryConsumerType;

public class PUCS4SalaryConverterTest extends WebServicesTestBase {
    PUCS4SalaryConverter test;

    private DeclareSalaryConsumerType unmarshallDeclareSalaryConsumerTypeFromSoapBody(String path)
            throws SAXException, IOException, ParserConfigurationException, JAXBException {
        Element element = getSoapBodyPayloadElement(path);
        JAXBContext jc = JAXBContext.newInstance(DeclareSalaryConsumerType.class);
        DeclareSalaryConsumerType value = jc.createUnmarshaller().unmarshal(element, DeclareSalaryConsumerType.class)
                .getValue();
        return value;
    }

    @Before
    public void setUp() {
        test = new PUCS4SalaryConverter();
    }

    @Test
    public void testConvert() throws Exception {
        DeclarationSalaire converted = parseAndDisplay("/globaz/pavo/process/268.1083-Commune de Bure-AF_SEUL_1.xml");
        assertThat(converted.getMontantCaf()).isEqualTo(Montant.valueOf(10248505.5));
        assertThat(converted.getMontantCaf("SG")).isEqualTo(Montant.valueOf(10248505.5));
        assertThat(converted.getMontantCaf("JU")).isNull();

        System.out.println(converted);

        converted = parseAndDisplay("/globaz/pavo/process/FICHIER TEST-701.1054_Paloba SA_1.xml");
        System.out.println(converted);

        converted = parseAndDisplay("/globaz/pavo/process/MIXD-2451002-1450788697373_201606011132206641_1.xml");
        assertThat(converted.getNbSalaire()).isEqualTo(389);
        System.out.println(converted);
    }

    private DeclarationSalaire parseAndDisplay(String file)
            throws SAXException, IOException, ParserConfigurationException, JAXBException {
        DeclareSalaryConsumerType value = unmarshallDeclareSalaryConsumerTypeFromSoapBody(file);
        assertThat(value).isNotNull();

        DeclarationSalaire converted = test.convert(value);

        for (Employee employe : converted.getEmployees()) {
            System.out.println(employe.getDateNaissance());
            System.out.println(employe.getNss());
        }

        return converted;
    }

}
