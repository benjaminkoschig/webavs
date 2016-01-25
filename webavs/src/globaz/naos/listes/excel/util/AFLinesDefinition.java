package globaz.naos.listes.excel.util;

import globaz.op.excelml.model.element.SsRow;
import java.util.ArrayList;
import java.util.Collections;

public class AFLinesDefinition {
    public class CELineDefinition implements Comparable {
        private String label = null;

        private int position = -1;

        protected CELineDefinition(int position, String label) {
            super();
            this.position = position;
            this.label = label;
        }

        @Override
        public int compareTo(Object obj) {
            return obj instanceof CELineDefinition ? position - ((CELineDefinition) obj).position : -1;
        }

        public String getLabel() {
            return label;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public String toString() {
            return "Line N°" + getPosition() + " - Label : " + getLabel();
        }
    }

    private SsRow firstRow = null;

    private ArrayList headers = null;
    private ArrayList lines = null;

    protected AFLinesDefinition() {
        super();
        lines = new ArrayList();
        headers = new ArrayList();
    }

    public SsRow getFirstRow() {
        return firstRow;
    }

    protected String getLabelFor(int position) {
        return ((CELineDefinition) getLines().get(position)).getLabel();
    }

    protected ArrayList getLines() {
        Collections.sort(lines);
        return lines;
    }

    protected void registerHeader(String name) {
        headers.add(name);
    }

    protected void registerLine(String value) {
        lines.add(new CELineDefinition(lines.size(), value));
    }

    public void setFirstRow(SsRow firstRow) {
        this.firstRow = firstRow;
    }
}
