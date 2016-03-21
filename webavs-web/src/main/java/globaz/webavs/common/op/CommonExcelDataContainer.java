package globaz.webavs.common.op;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CommonExcelDataContainer {
    public class CommonLine {
        private HashMap<String, String> container = null;

        public CommonLine() {
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

    private Collection<CommonLine> lines = null;

    public CommonExcelDataContainer() {
        super();
        lines = new ArrayList<CommonLine>();
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

    public CommonLine newLine() {
        return new CommonLine();
    }

    public void registerHeader(String name, String value) {
        headerValues.put(name, value);
    }

    public void registerLine(CommonLine line) {
        if (line.container.size() != 0) {
            lines.add(line);
        }
    }

    public Iterator<CommonLine> returnLinesIterator() {
        Iterator<CommonLine> lineIt = lines.iterator();
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

        Iterator<CommonLine> lineIt = lines.iterator();
        while (lineIt.hasNext()) {
            str.append("\n******************************************************************\n");
            str.append(lineIt.next().toString());
        }
        return str.toString();
    }
}
