package globaz.hercule.op;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CEExcelDataContainer {
    public class CELine {
        private HashMap<String, String> container = null;

        public CELine() {
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

    private Collection<CELine> lines = null;

    public CEExcelDataContainer() {
        super();
        lines = new ArrayList<CELine>();
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

    public CELine newLine() {
        return new CELine();
    }

    public void registerHeader(String name, String value) {
        headerValues.put(name, value);
    }

    public void registerLine(CELine line) {
        if (line.container.size() != 0) {
            lines.add(line);
        }
    }

    public Iterator<CELine> returnLinesIterator() {
        Iterator<CELine> lineIt = lines.iterator();
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

        Iterator<CELine> lineIt = lines.iterator();
        while (lineIt.hasNext()) {
            str.append("\n******************************************************************\n");
            str.append(lineIt.next().toString());
        }
        return str.toString();
    }
}
