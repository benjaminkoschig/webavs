package globaz.pavo.print.list;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class CIComparaisStatistique_DS implements JRDataSource {

    private boolean isFirst = true;

    public CIComparaisStatistique_DS() {
        super();

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        if (jrField.getName().equals("COL_14")) {
            return "TEST";
        }
        return "test";
    }

    @Override
    public boolean next() throws JRException {
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

}
