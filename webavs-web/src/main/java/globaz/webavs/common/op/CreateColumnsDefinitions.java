package globaz.webavs.common.op;

import globaz.op.common.model.common.Node;
import globaz.op.common.model.element.Element;
import globaz.op.excelml.model.attribute.SsName;
import globaz.op.excelml.model.attribute.SsRefersTo;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.op.excelml.model.element.SsNamedRange;
import globaz.op.excelml.model.element.SsNames;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateColumnsDefinitions {

    public static final String DEF_COL = "col_";
    public static final String DEF_HEADER = "header";
    public static final String DEF_ROW_DEFINITION_HEADER = "row_tableDefinition";
    private static final Pattern patternColumn = Pattern.compile("!C(\\d*)$");
    private static final Pattern patternFirstRow = Pattern.compile("!R(\\d*)$");

    static Map<String, String> resolveDefNameRange(ExcelmlWorkbook wk, String cle) {
        Element element = ElementExtractor.nextElement(wk.getRootElement(), SsNames.class);
        Element elementRange = ElementExtractor.nextElement(element, SsNamedRange.class);
        Map<String, String> map = new HashMap<String, String>();
        while (elementRange != null) {
            String refersTo = null;
            String name = null;
            Iterator<Node> iteNode = elementRange.getAttributesIterator();
            while (iteNode.hasNext()) {
                globaz.op.common.model.common.Node attriubutNode = iteNode.next();
                if (SsRefersTo.TAG_NAME.equals(attriubutNode.getTagName())) {
                    refersTo = attriubutNode.getContent();
                }

                if (SsName.TAG_NAME.equals(attriubutNode.getTagName())) {
                    name = attriubutNode.getContent();
                }
            }

            if (name.startsWith(cle)) {
                map.put(name, refersTo);
            }

            elementRange = ElementExtractor.nextElement(elementRange, SsNamedRange.class);
        }
        return map;
    }

    public static CommonLinesDefinition createCommonLinesDefinition(ExcelmlWorkbook wk) {
        CommonLinesDefinition columnsDef = new CommonLinesDefinition();
        createColumnsDefinition(wk, columnsDef);
        createCellsDefinition(wk, columnsDef);
        columnsDef.setPositionfirstRow(reslolveFirstRowPosition(wk));

        return columnsDef;
    }

    static Integer reslolveFirstRowPosition(ExcelmlWorkbook wk) {
        Map<String, String> mapRange = resolveDefNameRange(wk, DEF_ROW_DEFINITION_HEADER);
        if (mapRange.isEmpty()) {
            throw new RuntimeException("Unable to find the first row. The name of the first row was not set : "
                    + DEF_ROW_DEFINITION_HEADER);
        }

        if (mapRange.size() > 1) {
            throw new RuntimeException("Unable to find the first row. Too many first row : "
                    + DEF_ROW_DEFINITION_HEADER);
        }

        for (Entry<String, String> entry : mapRange.entrySet()) {
            return resolveRowPosition(entry.getValue()) + 1; // On ajoute 1 car la first row débute juste aprés le
                                                             // header
        }

        return null;
    }

    static void createColumnsDefinition(ExcelmlWorkbook wk, CommonLinesDefinition columnsDef) {
        Map<String, String> mapRange = resolveDefNameRange(wk, DEF_COL);
        if (mapRange.isEmpty()) {
            throw new RuntimeException("Unable to create column defintion. No element cell begin with " + DEF_COL);
        }
        for (Entry<String, String> entry : mapRange.entrySet()) {
            columnsDef.addColumn(entry.getKey(), resolveColumnPosition(entry.getValue()));
        }
    }

    static void createCellsDefinition(ExcelmlWorkbook wk, CommonLinesDefinition columnsDef) {
        Map<String, String> mapHeader = resolveDefNameRange(wk, DEF_HEADER);
        for (Entry<String, String> entry : mapHeader.entrySet()) {
            columnsDef.addHeader(entry.getKey());
        }
    }

    static Integer resolveColumnPosition(String refersTo) {
        Matcher m = patternColumn.matcher(refersTo);
        if (m.find()) {
            return Integer.valueOf(m.group(1));
        }
        return null;
    }

    static Integer resolveRowPosition(String refersTo) {
        Matcher m = patternFirstRow.matcher(refersTo);
        if (m.find()) {
            return Integer.valueOf(m.group(1));
        }
        return null;
    }
}
