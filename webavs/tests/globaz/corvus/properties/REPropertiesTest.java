package globaz.corvus.properties;

import globaz.corvus.TestUtils;
import globaz.corvus.application.REApplication;
import globaz.corvus.utils.AbstractTestCaseWithContext;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public class REPropertiesTest extends AbstractTestCaseWithContext {

    @Test
    @Ignore
    public void test() {
        Assert.assertTrue(REProperties.values()[0].getApplicationName()
                .equals(REApplication.DEFAULT_APPLICATION_CORVUS));

        // Contrôle des properties à double
        for (IProperties property : REProperties.values()) {
            for (IProperties property2 : REProperties.values()) {
                if (!property.equals(property2)) {
                    Assert.assertTrue(!property.getPropertyName().equals(property2.getPropertyName()));
                }
            }
        }

        // System.out.println(this.getClass().getSimpleName() + " : impression de toutes les propriétés Corvus");
        // TestUtils.printSpacer();
        // for (IProperties property : REProperties.values()) {
        // StringBuilder sb = new StringBuilder();
        // sb.append(((Enum<?>) property).name());
        // TestUtils.indent(sb, 50);
        // sb.append("[");
        // sb.append(property.getPropertyName());
        // sb.append("]");
        // TestUtils.indent(sb, 100);
        // sb.append("[");
        // sb.append(property.getDescription());
        // sb.append("]");
        // System.out.println(sb.toString());
        // }

        System.out
                .println(this.getClass().getSimpleName()
                        + " : impression des valeurs déclarées de toutes les propriétés Corvus existantes pour l'environnement : "
                        + getEnvironnementName());
        TestUtils.printSpacer();
        for (IProperties property : REProperties.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(((Enum<?>) property).name());
            TestUtils.indent(sb, 50);
            sb.append("[");
            sb.append(property.getPropertyName());
            sb.append("]");
            TestUtils.indent(sb, 100);
            sb.append("[");
            String propVal = null;
            try {
                propVal = property.getValue();
            } catch (PropertiesException e) {
                propVal = "";
            }
            sb.append(propVal);
            sb.append("]");
            TestUtils.indent(sb, 160);
            sb.append("[");
            sb.append(property.getDescription());
            sb.append("]");
            System.out.println(sb.toString());
        }
    }

}
