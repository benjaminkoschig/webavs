package globaz.webavs.common.op;

import static org.fest.assertions.api.Assertions.*;
import globaz.hercule.utils.CEExcelmlUtils;
import java.util.Map;
import org.junit.Test;

public class CreateColumnsDefinitionsTest {

    private String FILE_TEST = "src/test/resources/ch/globaz/common/op/columnsDefinitionTest.xml";

    @Test
    public void testResolveDefNameRange() throws Exception {
        Map<String, String> map = CreateColumnsDefinitions.resolveDefNameRange(CEExcelmlUtils.loadPath(FILE_TEST),
                CreateColumnsDefinitions.DEF_COL);

        assertThat(map).hasSize(17);
    }

    @Test
    public void testResolveColumnPositionEmptyValue() throws Exception {
        assertThat(CreateColumnsDefinitions.resolveColumnPosition("")).isNull();
    }

    @Test
    public void testResolveColumnPosition() throws Exception {
        assertThat(CreateColumnsDefinitions.resolveColumnPosition("=Feuil1!C14")).isEqualTo(14);
    }

    @Test
    public void testResolveColumnPositionError() throws Exception {
        assertThat(CreateColumnsDefinitions.resolveColumnPosition("=Feuil1!C84R")).isNull();
    }

    @Test
    public void testResolveColumnPositionREF() throws Exception {
        assertThat(CreateColumnsDefinitions.resolveColumnPosition("=Feuil1!#REF!")).isNull();
    }

    @Test
    public void testReslolveFirstRowPosition() throws Exception {
        CommonLinesDefinition columnsDef = new CommonLinesDefinition();

        assertThat(CreateColumnsDefinitions.reslolveFirstRowPosition(CEExcelmlUtils.loadPath(FILE_TEST))).isEqualTo(8);
    }

    @Test
    public void testResolveRowPosition() throws Exception {
        assertThat(CreateColumnsDefinitions.resolveRowPosition("Feuil1!R8")).isEqualTo(8);
    }

    @Test
    public void testCreateCommonLinesDefinition() throws Exception {
        CommonLinesDefinition columnsDef = CreateColumnsDefinitions.createCommonLinesDefinition(CEExcelmlUtils
                .loadPath(FILE_TEST));

        assertThat(columnsDef.getPositionfirstRow()).isEqualTo(8);
        assertThat(columnsDef.getColumnsDefinitions().size()).isEqualTo(17);
        assertThat(columnsDef.getHeaders().size()).isEqualTo(9);

    }

    @Test
    public void testCreateCellsDefinition() throws Exception {
        CommonLinesDefinition columnsDef = new CommonLinesDefinition();
        CreateColumnsDefinitions.createCellsDefinition(CEExcelmlUtils.loadPath(FILE_TEST), columnsDef);

        assertThat(columnsDef.getHeaders().size()).isEqualTo(9);
    }

    @Test
    public void testCreateColumnsDefinition() throws Exception {
        CommonLinesDefinition columnsDef = new CommonLinesDefinition();
        CreateColumnsDefinitions.createColumnsDefinition(CEExcelmlUtils.loadPath(FILE_TEST), columnsDef);

        assertThat(columnsDef.getColumnsDefinitions().size()).isEqualTo(17);
    }
}
