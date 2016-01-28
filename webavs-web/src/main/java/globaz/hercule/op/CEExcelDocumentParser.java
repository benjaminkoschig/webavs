package globaz.hercule.op;

import globaz.hercule.op.CEExcelDataContainer.CELine;
import globaz.op.common.exception.OpNodeInstanciationException;
import globaz.op.common.model.element.Element;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.op.excelml.model.element.SsCell;
import globaz.op.excelml.model.element.SsNamedCell;
import globaz.op.excelml.model.element.SsRow;
import globaz.op.excelml.model.element.SsWorksheet;
import java.util.ArrayList;
import java.util.Iterator;

public class CEExcelDocumentParser {

    private static int countNamedCell(CELinesDefinition linesDef, SsRow current) {
        int number = 0;
        Element parent = (Element) current.getParentTag();
        current.setParentTag(null);
        SsNamedCell next = (SsNamedCell) ElementExtractor.nextElement(current, SsNamedCell.class);
        while (next != null) {
            if (!next.getName().startsWith("header")) {
                number++;
            } else {
                linesDef.registerHeader(next.getName());
            }
            next = (SsNamedCell) ElementExtractor.nextElement(next, SsNamedCell.class);
        }
        current.setParentTag(parent);
        return number;
    }

    private static SsRow getFirstRow(CELinesDefinition linesDef, ExcelmlWorkbook wk) {
        SsRow firstRow = null;
        // Prend le row et regarde si il y a plus que 1 namedCell, si oui -> on
        // parse, sinon regarde la suite
        SsRow current = getNextRowContainingNamedCell(wk.getRootElement());
        while (firstRow == null && current != null) {
            int rowPos = current.getParentTag().getChildNodes().getPosition(current);
            if (countNamedCell(linesDef, current) > 1) {
                firstRow = current;
            }
            if (firstRow == null) {
                // Comme current contient le NamedCell que l'on vient de parser,
                // faut prendre la row suivante pour basculer sur le prochain
                // namedCell
                Element nextRow = (Element) current.getParentTag().getChildNodes().getElement(rowPos + 1);
                if (nextRow != null) {
                    current = getNextRowContainingNamedCell(nextRow);
                } else {
                    current = null;
                }
            }
        }
        return firstRow;
    }

    private static SsRow getNextRowContainingNamedCell(Element currentNode) {
        SsRow nextRow = null;
        SsNamedCell current = (SsNamedCell) ElementExtractor.nextElement(currentNode, SsNamedCell.class);
        if (current != null) {
            nextRow = (SsRow) current.getParentTag().getParentTag();
        }
        return nextRow;
    }

    private static CELinesDefinition parseLines(ExcelmlWorkbook wk) {
        CELinesDefinition linesDef = new CELinesDefinition();
        SsRow firstRow = getFirstRow(linesDef, wk);
        if (firstRow != null) {
            linesDef.setFirstRow(firstRow);
            Element parent = (Element) firstRow.getParentTag();
            firstRow.setParentTag(null);
            SsNamedCell next = (SsNamedCell) ElementExtractor.nextElement(firstRow, SsNamedCell.class);
            while (next != null) {
                if (next != null) {
                    linesDef.registerLine(next.getName());
                }
                next = (SsNamedCell) ElementExtractor.nextElement(next, SsNamedCell.class);
            }
            firstRow.setParentTag(parent);
        }
        return linesDef;
    }

    public static CEExcelDataContainer parseWorkBook(ExcelmlWorkbook wk) throws OpNodeInstanciationException {
        CEExcelDataContainer dataContainer = new CEExcelDataContainer();
        // Défini le contenu standard d'une ligne
        CELinesDefinition linesDef = parseLines(wk);
        // Récupère la 1ère worksheet
        Iterator it = ((SsWorksheet) ((ArrayList) wk.getRootElement().getWorksheets()).get(0)).getTable().getRows()
                .iterator();
        boolean handle = false;
        while (it.hasNext()) {
            SsRow next = (SsRow) it.next();
            if (!handle) {
                // Parse if header
                Iterator cellIt = next.getCells().iterator();
                while (cellIt.hasNext()) {
                    SsCell current = (SsCell) cellIt.next();
                    if (current.getChildNodes().hasSome(SsNamedCell.class)) {
                        if (current.getNamedCell().getName().startsWith("header")) {
                            dataContainer.registerHeader(current.getNamedCell().getName(), current.getData()
                                    .getContent());
                            // FIXME fix BZ8420 : le parseur devrait pouvoir ignorer les SsNamedCell double de type
                            // <NamedCell ss:Name="_FilterDatabase"/>.
                        } else if (current.getNamedCell().getName().startsWith("_FilterDatabase")) {
                            throw new CEExcelFilterNotSupportedException(
                                    "NamedCell not supported detected: _FilterDatabase");
                        }
                    }
                }

                handle = next.equals(linesDef.getFirstRow());
            }
            if (handle) {
                CELine line = dataContainer.newLine();
                ArrayList cellList = (ArrayList) next.getCells();
                for (int i = 0; i < cellList.size(); i++) {
                    SsCell current = (SsCell) cellList.get(i);
                    if (linesDef.getLines().size() > i) {
                        line.registerCol(linesDef.getLabelFor(i), current.getData().getContent());
                    }
                }
                dataContainer.registerLine(line);
            }
        }
        // System.out.println(dataContainer);
        return dataContainer;
    }
}
