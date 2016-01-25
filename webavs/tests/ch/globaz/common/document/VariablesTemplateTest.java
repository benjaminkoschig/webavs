package ch.globaz.common.document;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class VariablesTemplateTest {

    @Test
    public void testSplit() throws Exception {
        List<String> values = newVariableTemplate().split("${toto} ${tata} - _ sjdi");
        assertEquals(2, values.size());
        assertEquals(values.get(0), "toto");
        assertEquals(values.get(1), "tata");
    }

    @Test
    public void testResoleveVarialbesNoDeclaredFounded() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("NAME", "Zoro");
        VariablesTemplate variablesTemplate = newVariableTemplate();
        List<String> values = variablesTemplate.resoleveVarialbesNoDeclared(map);
        assertEquals(1, values.size());
        assertEquals("AGE", values.get(0));
    }

    private VariablesTemplate newVariableTemplate() {
        VariablesTemplate variablesTemplate = new VariablesTemplate("Le nom: {NAME} est l':{AGE}");
        return variablesTemplate;
    }

    @Test
    public void testResoleveVarialbesNoDeclaredNotFounded() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("NAME", "Zoro");
        map.put("AGE", "150");
        VariablesTemplate variablesTemplate = newVariableTemplate();
        List<String> values = variablesTemplate.resoleveVarialbesNoDeclared(map);
        assertEquals(0, values.size());
    }

    @Test
    public void testFormatWithOutValue() throws Exception {
        VariablesTemplate variablesTemplate = newVariableTemplate();
        String formated = variablesTemplate.render(new HashMap<String, String>());
        assertEquals("Le nom: {NAME} est l':{AGE}", formated);
    }

    @Test
    public void testFormatWithValues() throws Exception {
        VariablesTemplate variablesTemplate = newVariableTemplate();
        Map<String, String> map = new HashMap<String, String>();
        map.put("NAME", "Zoro");
        map.put("AGE", "150");
        String formated = variablesTemplate.render(map);
        assertEquals("Le nom: Zoro est l':150", formated);
    }

    @Test
    public void testVariablesTemplateStringString() throws Exception {
        VariablesTemplate variablesTemplate = new VariablesTemplate("[NAME] [AGE]", "[?]");
        Map<String, String> map = new HashMap<String, String>();
        map.put("NAME", "Zoro");
        map.put("AGE", "150");
        String formated = variablesTemplate.render(map);
        assertEquals("Zoro 150", formated);
    }

    @Test(expected = RuntimeException.class)
    public void testFormatWithException() throws Exception {
        VariablesTemplate variablesTemplate = newVariableTemplate();
        Map<String, String> map = new HashMap<String, String>();
        map.put("NAME", "Zoro");
        variablesTemplate.render(map);
    }

    @Test
    public void testEscapeChars() throws Exception {
        VariablesTemplate variablesTemplate = newVariableTemplate();
        String resutl = variablesTemplate.escapeChars("{}[]", "{RRR}[TTT]");
        assertEquals("\\{RRR\\}\\[TTT\\]", resutl);
    }

    @Test
    public void testAvailableVaraibles() throws Exception {
        VariablesTemplate variablesTemplate = newVariableTemplate();
        assertEquals("NAME,AGE", variablesTemplate.availableVaraibles());
    }
}
