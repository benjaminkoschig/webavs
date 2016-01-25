package globaz.pavo.process.ree;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class AFLienRee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String line = new String();

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        StringBuffer buff = new StringBuffer();

        buff.append(statement.dbReadString("MWILIE") + ";");
        buff.append(statement.dbReadString("MAIAFF") + ";");
        buff.append(statement.dbReadString("AFA_MAIAFF") + ";");
        buff.append(statement.dbReadString("MWTLIE") + ";");
        buff.append(statement.dbReadString("MWDDEB") + ";");
        buff.append(statement.dbReadString("MWDFIN") + ";");
        buff.append(statement.dbReadString("PSPY"));

        line = buff.toString();
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

}
