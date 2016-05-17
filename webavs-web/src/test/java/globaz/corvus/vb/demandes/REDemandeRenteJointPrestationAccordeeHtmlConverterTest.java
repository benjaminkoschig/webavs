package globaz.corvus.vb.demandes;

import static org.junit.Assert.*;
import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeHtmlConverter.CodeRente;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;

public class REDemandeRenteJointPrestationAccordeeHtmlConverterTest {

    @Test
    public void testStandardListsTransform() {

        List<String> chaines = new ArrayList<String>();
        chaines.add("Tutu");
        chaines.add("Toti");
        chaines.add("Zorro");

        Function<String, String> function = new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input + " - ";
            }
        };

        List<String> result = Lists.transform(chaines, function);

        assertTrue(result.get(0).equals("Tutu - "));
        assertTrue(result.get(2).equals("Zorro - "));
        assertFalse(result.get(1).equals("Zorro - "));
    }

    @Test
    public void testStandardListsTransformByPassFirst() {

        List<CodeRente> french = new ArrayList<CodeRente>();
        french.add(new CodeRente("un"));
        french.add(new CodeRente("deux"));
        french.add(new CodeRente("trois"));

        List<String> english = new ArrayList<String>();
        english.add("one");
        english.add("two");
        english.add("three");

        Map<CodeRente, String> combinedMap = REDemandeRenteJointPrestationAccordeeHtmlConverter
                .combineListsIntoOrderedMap(french, english);

        for (CodeRente key : combinedMap.keySet()) {
            System.out.println(key.getCodeRente());
            System.out.println(combinedMap.get(key));
        }

        EntryTransformer<CodeRente, String, String> flagPrefixer = new EntryTransformer<CodeRente, String, String>() {

            @Override
            public String transformEntry(CodeRente key, String value) {

                return key.getCodeRente() + " - ";
            }
        };

        Map<CodeRente, String> result = Maps.transformEntries(combinedMap, flagPrefixer);

        for (CodeRente key : result.keySet()) {
            System.out.println(key.getCodeRente());
            System.out.println(result.get(key));
        }

    }

    @Test
    public void testCodesPrestationsStandard() {
        List<String> codes = Arrays.asList(new String[] { "3456", "9824", "2332" });
        List<String> dates = Arrays.asList(new String[] { "12.01.2014", "12.01.2012", "12.11.2014" });
        String toTest = "<i>3456</i>-<i>9824</i>-<i>2332</i>";

        StringBuilder builder = REDemandeRenteJointPrestationAccordeeHtmlConverter.codesPrestations(codes, dates);

        System.out.println(builder);
        assertTrue(builder.toString().equals(toTest));

    }

    @Test
    public void testCodesPrestationsWithDateNullOrEmpty() {
        List<String> codes = Arrays.asList(new String[] { "3456", "9824", "2332" });
        List<String> dates = Arrays.asList(new String[] { null, "", "12.11.2014" });
        String toTest = "<b>3456</b>-<b>9824</b>-<i>2332</i>";

        StringBuilder builder = REDemandeRenteJointPrestationAccordeeHtmlConverter.codesPrestations(codes, dates);

        System.out.println(builder);
        assertTrue("toTest:" + toTest + ", builder:" + builder, builder.toString().equals(toTest));

    }

    @Test
    public void testCodesPrestationsWithCodeNullOrEmpty() {
        List<String> codes = Arrays.asList(new String[] { "3456", null, "" });
        List<String> dates = Arrays.asList(new String[] { "12.01.2014", "12.01.2012", "12.11.2014" });
        String toTest = "<i>3456</i>";

        StringBuilder builder = REDemandeRenteJointPrestationAccordeeHtmlConverter.codesPrestations(codes, dates);

        System.out.println(builder);
        assertTrue("toTest:" + toTest + ", builder:" + builder, builder.toString().equals(toTest));

    }

    @Test
    public void testCodesPrestationsWithDoublonsInCodes() {
        List<String> codes = Arrays.asList(new String[] { "3456", "3543", "3456" });
        List<String> dates = Arrays.asList(new String[] { "12.01.2014", "12.01.2012", "12.11.2014" });

        String toTest = "<i>3456</i>-<i>3543</i>-<i>3456</i>";

        StringBuilder builder = REDemandeRenteJointPrestationAccordeeHtmlConverter.codesPrestations(codes, dates);

        System.out.println(builder);
        assertTrue("toTest:" + toTest + ", builder:" + builder, builder.toString().equals(toTest));

    }

}
