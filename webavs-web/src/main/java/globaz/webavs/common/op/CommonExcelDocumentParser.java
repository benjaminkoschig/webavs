package globaz.webavs.common.op;

import globaz.op.common.exception.OpNodeInstanciationException;
import globaz.op.common.model.common.Node;
import globaz.op.common.util.NodeList;
import globaz.op.excelml.model.attribute.SsHidden;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.op.excelml.model.element.SsCell;
import globaz.op.excelml.model.element.SsNamedCell;
import globaz.op.excelml.model.element.SsRow;
import globaz.op.excelml.model.element.SsWorksheet;
import globaz.webavs.common.op.CommonExcelDataContainer.CommonLine;
import globaz.webavs.common.op.CommonLinesDefinition.CommonLineDefinition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommonExcelDocumentParser {

    public static CommonExcelDataContainer parseWorkBook(ExcelmlWorkbook wk) throws OpNodeInstanciationException {
        CommonExcelDataContainer dataContainer = new CommonExcelDataContainer();
        // Défini le contenu standard d'une ligne
        CommonLinesDefinition linesDef = CreateColumnsDefinitions.createCommonLinesDefinition(wk);
        // Récupère la 1ère worksheet
        List<SsWorksheet> lstWorkSheet = (ArrayList<SsWorksheet>) wk.getRootElement().getWorksheets();
        Iterator<SsRow> it = lstWorkSheet.get(0).getTable().getRows().iterator();

        int rowPosition = 1;
        while (it.hasNext()) {
            SsRow nextRow = it.next();

            // Parse des headers
            if (rowPosition < linesDef.getPositionfirstRow()) {

                Iterator<SsCell> cellIt = nextRow.getCells().iterator();
                while (cellIt.hasNext()) {
                    SsCell current = cellIt.next();

                    SsNamedCell namedCell = resolveNamedCellWithPrefix(current, "header");
                    if (namedCell != null) {
                        dataContainer.registerHeader(namedCell.getName(), current.getData().getContent());
                    }
                }
            } else {

                if (!hasHidden(nextRow)) {

                    // Parse du contenu
                    CommonLine line = dataContainer.newLine();
                    List<SsCell> cellList = (ArrayList<SsCell>) nextRow.getCells();

                    for (CommonLineDefinition def : linesDef.getColumnsDefinitions()) {
                        for (int i = 0; i < cellList.size(); i++) {
                            SsCell current = cellList.get(i);
                            if (containNamedCell(def.getLabel(), current)) {
                                line.registerCol(def.getLabel().replace(CreateColumnsDefinitions.DEF_COL, ""), current
                                        .getData().getContent());
                            }
                        }
                    }

                    dataContainer.registerLine(line);
                }
            }

            rowPosition++;
        }

        return dataContainer;
    }

    private static boolean hasHidden(SsRow nextRow) {
        Iterator<Node> nodeRow = nextRow.getAttributesIterator();
        while (nodeRow.hasNext()) {
            Node node = nodeRow.next();
            if (SsHidden.TAG_NAME.equals(node.getTagName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean containNamedCell(String name, SsCell cell) {
        List<SsNamedCell> namedCells = getNamedCells(cell);
        for (SsNamedCell ssNamedCell : namedCells) {
            if (ssNamedCell.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static List<SsNamedCell> getNamedCells(SsCell cell) {
        NodeList listNode = cell.getChildNodes();
        return listNode.getNodes(SsNamedCell.class);
    }

    public static SsNamedCell resolveNamedCellWithPrefix(SsCell cell, String prefix) {
        List<SsNamedCell> namedCells = getNamedCells(cell);

        for (SsNamedCell namedCell : namedCells) {
            if (namedCell.getName().startsWith(prefix)) {
                return namedCell;
            }
        }

        return null;
    }

}
