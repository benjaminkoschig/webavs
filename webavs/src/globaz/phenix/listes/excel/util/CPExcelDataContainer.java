package globaz.phenix.listes.excel.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CPExcelDataContainer {
    public class CPLine {
        private HashMap<String, String> container = null;

        public CPLine() {
            super();
            container = new HashMap<String, String>();
        }

        public Iterator<String> getColumnNames() {
            return container.keySet().iterator();
        }

        public String getColValue(String name) {
            return container.get(name);
        }

        public void registerCol(String name, String value) {
            // TODO CHanger avec JadeStringUtil.isEmpty()
            if (value != null) {
                container.put(name, value);
            }
        }

        public HashMap<String, String> returnLineHashMap() {
            return container;
        }

        @Override
        public String toString() {
            return container.toString();
        }

    }

    private HashMap<String, String> headerValues = null;

    private Collection<CPLine> lines = null;

    public CPExcelDataContainer() {
        super();
        lines = new ArrayList<CPLine>();
        headerValues = new HashMap<String, String>();
    }

    public Iterator<String> getHeaderKeys() {
        return headerValues.keySet().iterator();
    }

    public String getHeaderValue(String key) {
        return headerValues.get(key);
    }

    public int getSize() {
        int taille = 0;
        taille = lines.size();
        return taille;
    }

    public CPLine newLine() {
        return new CPLine();
    }

    public void registerHeader(String name, String value) {
        headerValues.put(name, value);
    }

    public void registerLine(CPLine line) {
        if (line.container.size() != 0) {
            lines.add(line);
        }
    }

    public Iterator<CPLine> returnLinesIterator() {
        Iterator<CPLine> lineIt = lines.iterator();
        return lineIt;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("************************** HEADER **********************************);");
        Iterator<String> it = getHeaderKeys();
        while (it.hasNext()) {
            String name = it.next();
            str.append(" - " + name + " : " + getHeaderValue(name));
        }

        Iterator<CPLine> lineIt = lines.iterator();
        while (lineIt.hasNext()) {
            str.append("\n******************************************************************\n");
            str.append(lineIt.next().toString());
        }
        return str.toString();
    }
}
