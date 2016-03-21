package globaz.webavs.common.op;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonLinesDefinition {
    public class CommonLineDefinition implements Comparable<CommonLineDefinition> {
        private String label = null;

        private int position = -1;

        protected CommonLineDefinition(int position, String label) {
            super();
            this.position = position;
            this.label = label;
        }

        @Override
        public int compareTo(CommonLineDefinition obj) {
            return position - obj.position;
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

    private Integer positionfirstRow = null;

    private List<String> headers = null;
    private List<CommonLineDefinition> columnsDefinitions = null;

    public CommonLinesDefinition() {
        super();
        columnsDefinitions = new ArrayList<CommonLineDefinition>();
        headers = new ArrayList<String>();
    }

    public String getLabelFor(int position) {
        return columnsDefinitions.get(position).getLabel();
    }

    public List<CommonLineDefinition> getColumnsDefinitions() {
        return columnsDefinitions;
    }

    public void addHeader(String name) {
        headers.add(name);
    }

    public void addColumn(String name, int position) {
        columnsDefinitions.add(new CommonLineDefinition(position, name));
        Collections.sort(columnsDefinitions);
    }

    public Integer getPositionfirstRow() {
        return positionfirstRow;
    }

    public void setPositionfirstRow(Integer positionfirstRow) {
        this.positionfirstRow = positionfirstRow;
    }

    public List<String> getHeaders() {
        return headers;
    }
}
