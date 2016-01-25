package globaz.pavo.process.ree;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class AFRee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String line = new String();

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        StringBuffer buff = new StringBuffer();

        buff.append(statement.dbReadString("HTITIE") + ";");
        buff.append(statement.dbReadString("MAIAFF") + ";");
        buff.append(statement.dbReadString("MALNAF") + ";");
        buff.append(statement.dbReadString("MADDEB") + ";");
        buff.append(statement.dbReadString("MADFIN") + ";");
        buff.append(statement.dbReadString("MATMOT") + ";");
        buff.append(statement.dbReadString("MATTAF") + ";");
        buff.append(statement.dbReadString("MATBRA") + ";");
        buff.append(statement.dbReadString("MATJUR") + ";");
        buff.append(statement.dbReadString("MABEXO") + ";");
        buff.append(statement.dbReadString("MADFIC") + ";");
        buff.append(statement.dbReadString("MADFI1") + ";");
        buff.append(statement.dbReadString("MADFI2") + ";");
        buff.append(statement.dbReadString("MATDEC") + ";");
        buff.append(statement.dbReadString("MATMAS") + ";");
        buff.append(statement.dbReadString("MATMCO") + ";");
        buff.append(statement.dbReadString("MABIRR") + ";");
        buff.append(statement.dbReadString("MABOCC") + ";");
        buff.append(statement.dbReadString("MABMAI") + ";");
        buff.append(statement.dbReadString("MABLIQ") + ";");
        buff.append(statement.dbReadString("MABTRA") + ";");
        buff.append(statement.dbReadString("MABREP") + ";");
        buff.append(statement.dbReadString("MABREI") + ";");
        buff.append(statement.dbReadString("MAMMAP") + ";");
        buff.append(statement.dbReadString("MAMMAA") + ";");
        buff.append(statement.dbReadString("MATPER") + ";");
        buff.append(statement.dbReadString("MAIAVS") + ";");
        buff.append(statement.dbReadString("MALAVS") + ";");
        buff.append(statement.dbReadString("MAIAFA") + ";");
        buff.append(statement.dbReadString("MALAFA") + ";");
        buff.append(statement.dbReadString("MAILAA") + ";");
        buff.append(statement.dbReadString("MALLAA") + ";");
        buff.append(statement.dbReadString("MAILPP") + ";");
        buff.append(statement.dbReadString("MALLPP") + ";");
        buff.append(statement.dbReadString("MALFED") + ";");
        buff.append(statement.dbReadString("MAMTCO") + ";");
        buff.append(statement.dbReadString("MAMFCO") + ";");
        buff.append(statement.dbReadString("MALNAA") + ";");
        buff.append(statement.dbReadString("MAICPR") + ";");
        buff.append(statement.dbReadString("MAICPA") + ";");
        buff.append(statement.dbReadString("PSPY") + ";");
        buff.append(statement.dbReadString("MATMCR") + ";");
        buff.append(statement.dbReadString("MABBMA") + ";");
        buff.append(statement.dbReadString("MADDSU") + ";");
        buff.append(statement.dbReadString("MADFSU") + ";");
        buff.append(statement.dbReadString("MADCRE") + ";");
        buff.append(statement.dbReadString("MADTEN") + ";");
        buff.append(statement.dbReadString("MADXDE") + ";");
        buff.append(statement.dbReadString("MADXFI") + ";");
        buff.append(statement.dbReadString("MATCDN") + ";");
        buff.append(statement.dbReadString("MATTAS") + ";");
        buff.append(statement.dbReadString("MABEAA") + ";");
        buff.append(statement.dbReadString("MADESC") + ";");
        buff.append(statement.dbReadString("MADESL") + ";");
        buff.append(statement.dbReadString("MADDEM") + ";");
        buff.append(statement.dbReadString("MADESM") + ";");
        buff.append(statement.dbReadString("MATCFA") + ";");
        buff.append(statement.dbReadString("HTITIE_1") + ";");
        buff.append(statement.dbReadString("HNIPAY") + ";");
        buff.append(statement.dbReadString("HTTTIE") + ";");
        buff.append(statement.dbReadString("HTTTTI") + ";");
        buff.append(statement.dbReadString("HTLDE1") + ";");
        buff.append(statement.dbReadString("HTLDE2") + ";");
        buff.append(statement.dbReadString("HTLDE3") + ";");
        buff.append(statement.dbReadString("HTLDE4") + ";");
        buff.append(statement.dbReadString("HTTLAN") + ";");
        buff.append(statement.dbReadString("PSPY_1") + ";");
        buff.append(statement.dbReadString("HTPPHY") + ";");
        buff.append(statement.dbReadString("HTPMOR") + ";");
        buff.append(statement.dbReadString("HTINAC") + ";");
        buff.append(statement.dbReadString("HTLDU1") + ";");
        buff.append(statement.dbReadString("HTLDU2") + ";");
        buff.append(statement.dbReadString("HTLDEC") + ";");
        buff.append(statement.dbReadString("HTLDUC") + ";");
        buff.append(statement.dbReadString("HTNTIE") + ";");
        buff.append(statement.dbReadString("HTPOLF") + ";");
        buff.append(statement.dbReadString("HTPOLD") + ";");
        buff.append(statement.dbReadString("HTPOLI") + ";");
        buff.append(statement.dbReadString("HTITIE_2") + ";");
        buff.append(statement.dbReadString("HXNAVS") + ";");
        buff.append(statement.dbReadString("HXNAFF") + ";");
        buff.append(statement.dbReadString("HXNCON") + ";");
        buff.append(statement.dbReadString("HXAAVS") + ";");
        buff.append(statement.dbReadString("HXTGAF") + ";");
        buff.append(statement.dbReadString("HXDDAC") + ";");
        buff.append(statement.dbReadString("HXDFAC") + ";");
        buff.append(statement.dbReadString("PSPY_2") + ";");
        buff.append(statement.dbReadString("HTITIE_3") + ";");
        buff.append(statement.dbReadString("HJILOC") + ";");
        buff.append(statement.dbReadString("HPDNAI") + ";");
        buff.append(statement.dbReadString("HPDDEC") + ";");
        buff.append(statement.dbReadString("HPTETC") + ";");
        buff.append(statement.dbReadString("HPTSEX") + ";");
        buff.append(statement.dbReadString("HPTCAN") + ";");
        buff.append(statement.dbReadString("HPDIST") + ";");
        buff.append(statement.dbReadString("PSPY_3") + ";");
        buff.append(statement.dbReadString("HTITIE_4") + ";");
        buff.append(statement.dbReadString("HAIADR") + ";");
        buff.append(statement.dbReadString("HFIAPP") + ";");
        buff.append(statement.dbReadString("HETTAD") + ";");
        buff.append(statement.dbReadString("HEDEFA") + ";");
        buff.append(statement.dbReadString("PSPY_4") + ";");
        buff.append(statement.dbReadString("HEIAAU") + ";");
        buff.append(statement.dbReadString("HEMOTI") + ";");
        buff.append(statement.dbReadString("HEDDAD") + ";");
        buff.append(statement.dbReadString("HEDFAD") + ";");
        buff.append(statement.dbReadString("HEIADR") + ";");
        buff.append(statement.dbReadString("HEIDEX") + ";");
        buff.append(statement.dbReadString("HESTAT") + ";");
        buff.append(statement.dbReadString("HAIADR_1") + ";");
        buff.append(statement.dbReadString("HJILOC_1") + ";");
        buff.append(statement.dbReadString("HADFAD") + ";");
        buff.append(statement.dbReadString("HAIADU") + ";");
        buff.append(statement.dbReadString("HATLAN") + ";");
        buff.append(statement.dbReadString("HATTAD") + ";");
        buff.append(statement.dbReadString("HAATTE") + ";");
        buff.append(statement.dbReadString("HAADR1") + ";");
        buff.append(statement.dbReadString("HAADR2") + ";");
        buff.append(statement.dbReadString("HAADR3") + ";");
        buff.append(statement.dbReadString("HAADR4") + ";");
        buff.append(statement.dbReadString("HACPOS") + ";");
        buff.append(statement.dbReadString("HARUE") + ";");
        buff.append(statement.dbReadString("PSPY_5") + ";");
        buff.append(statement.dbReadString("HADDAD") + ";");
        buff.append(statement.dbReadString("HAMOTI") + ";");
        buff.append(statement.dbReadString("HANRUE") + ";");
        buff.append(statement.dbReadString("HAIRUE") + ";");
        buff.append(statement.dbReadString("HJILOC_2") + ";");
        buff.append(statement.dbReadString("HJICAN") + ";");
        buff.append(statement.dbReadString("HNIPAY_1") + ";");
        buff.append(statement.dbReadString("HJPFIL") + ";");
        buff.append(statement.dbReadString("HJNOPO") + ";");
        buff.append(statement.dbReadString("HJTNPA") + ";");
        buff.append(statement.dbReadString("HJNPA") + ";");
        buff.append(statement.dbReadString("HJCNPA") + ";");
        buff.append(statement.dbReadString("HJLOCC") + ";");
        buff.append(statement.dbReadString("HJLOCA") + ";");
        buff.append(statement.dbReadString("HJLANG") + ";");
        buff.append(statement.dbReadString("HJLAND") + ";");
        buff.append(statement.dbReadString("HJAFTR") + ";");
        buff.append(statement.dbReadString("HJNAGG") + ";");
        buff.append(statement.dbReadString("HJNOFS") + ";");
        buff.append(statement.dbReadString("HJDVAL") + ";");
        buff.append(statement.dbReadString("HJNSEQ") + ";");
        buff.append(statement.dbReadString("HJDSUP") + ";");
        buff.append(statement.dbReadString("HJACTI") + ";");
        buff.append(statement.dbReadString("HJTDES") + ";");
        buff.append(statement.dbReadString("HJNPAR") + ";");
        buff.append(statement.dbReadString("HJCPOR") + ";");
        buff.append(statement.dbReadString("PSPY_6") + ";");
        buff.append(statement.dbReadString("HJPROV") + ";");
        buff.append(statement.dbReadString("HNIPAY_2") + ";");
        buff.append(statement.dbReadString("HKIMON") + ";");
        buff.append(statement.dbReadString("HNCISO") + ";");
        buff.append(statement.dbReadString("HNCCEN") + ";");
        buff.append(statement.dbReadString("HNINDI") + ";");
        buff.append(statement.dbReadString("HNCFCP") + ";");
        buff.append(statement.dbReadString("HNCNTE") + ";");
        buff.append(statement.dbReadString("HNLFR") + ";");
        buff.append(statement.dbReadString("HNLAL") + ";");
        buff.append(statement.dbReadString("HNLIT") + ";");
        buff.append(statement.dbReadString("PSPY_7") + ";");
        buff.append(statement.dbReadString("HNACTI"));

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
