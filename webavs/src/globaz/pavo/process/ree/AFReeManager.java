package globaz.pavo.process.ree;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;

public class AFReeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        // Fields sur la table affiliation
        CEUtils.sqlAddField(sqlFields, "af.HTITIE as HTITIE");
        CEUtils.sqlAddField(sqlFields, "MAIAFF");
        CEUtils.sqlAddField(sqlFields, "MALNAF");
        CEUtils.sqlAddField(sqlFields, "MADDEB");
        CEUtils.sqlAddField(sqlFields, "MADFIN");
        CEUtils.sqlAddField(sqlFields, "MATMOT");
        CEUtils.sqlAddField(sqlFields, "MATTAF");
        CEUtils.sqlAddField(sqlFields, "MATBRA");
        CEUtils.sqlAddField(sqlFields, "MATJUR");
        CEUtils.sqlAddField(sqlFields, "MABEXO");
        CEUtils.sqlAddField(sqlFields, "MADFIC");
        CEUtils.sqlAddField(sqlFields, "MADFI1");
        CEUtils.sqlAddField(sqlFields, "MADFI2");
        CEUtils.sqlAddField(sqlFields, "MATDEC");
        CEUtils.sqlAddField(sqlFields, "MATMAS");
        CEUtils.sqlAddField(sqlFields, "MATMCO");
        CEUtils.sqlAddField(sqlFields, "MABIRR");
        CEUtils.sqlAddField(sqlFields, "MABOCC");
        CEUtils.sqlAddField(sqlFields, "MABMAI");
        CEUtils.sqlAddField(sqlFields, "MABLIQ");
        CEUtils.sqlAddField(sqlFields, "MABTRA");
        CEUtils.sqlAddField(sqlFields, "MABREP");
        CEUtils.sqlAddField(sqlFields, "MABREI");
        CEUtils.sqlAddField(sqlFields, "MAMMAP");
        CEUtils.sqlAddField(sqlFields, "MAMMAA");
        CEUtils.sqlAddField(sqlFields, "MATPER");
        CEUtils.sqlAddField(sqlFields, "MAIAVS");
        CEUtils.sqlAddField(sqlFields, "MALAVS");
        CEUtils.sqlAddField(sqlFields, "MAIAFA");
        CEUtils.sqlAddField(sqlFields, "MALAFA");
        CEUtils.sqlAddField(sqlFields, "MAILAA");
        CEUtils.sqlAddField(sqlFields, "MALLAA");
        CEUtils.sqlAddField(sqlFields, "MAILPP");
        CEUtils.sqlAddField(sqlFields, "MALLPP");
        CEUtils.sqlAddField(sqlFields, "MALFED");
        CEUtils.sqlAddField(sqlFields, "MAMTCO");
        CEUtils.sqlAddField(sqlFields, "MAMFCO");
        CEUtils.sqlAddField(sqlFields, "MALNAA");
        CEUtils.sqlAddField(sqlFields, "MAICPR");
        CEUtils.sqlAddField(sqlFields, "MAICPA");
        CEUtils.sqlAddField(sqlFields, "af.PSPY as PSPY");
        CEUtils.sqlAddField(sqlFields, "MATMCR");
        CEUtils.sqlAddField(sqlFields, "MABBMA");
        CEUtils.sqlAddField(sqlFields, "MADDSU");
        CEUtils.sqlAddField(sqlFields, "MADFSU");
        CEUtils.sqlAddField(sqlFields, "MADCRE");
        CEUtils.sqlAddField(sqlFields, "MADTEN");
        CEUtils.sqlAddField(sqlFields, "MADXDE");
        CEUtils.sqlAddField(sqlFields, "MADXFI");
        CEUtils.sqlAddField(sqlFields, "MATCDN");
        CEUtils.sqlAddField(sqlFields, "MATTAS");
        CEUtils.sqlAddField(sqlFields, "MABEAA");
        CEUtils.sqlAddField(sqlFields, "MADESC");
        CEUtils.sqlAddField(sqlFields, "MADESL");
        CEUtils.sqlAddField(sqlFields, "MADDEM");
        CEUtils.sqlAddField(sqlFields, "MADESM");
        CEUtils.sqlAddField(sqlFields, "MATCFA");
        CEUtils.sqlAddField(sqlFields, "ti.htitie as HTITIE_1");
        CEUtils.sqlAddField(sqlFields, "ti.hnipay as HNIPAY");
        CEUtils.sqlAddField(sqlFields, "HTTTIE");
        CEUtils.sqlAddField(sqlFields, "HTTTTI");
        CEUtils.sqlAddField(sqlFields, "HTLDE1");
        CEUtils.sqlAddField(sqlFields, "HTLDE2");
        CEUtils.sqlAddField(sqlFields, "HTLDE3");
        CEUtils.sqlAddField(sqlFields, "HTLDE4");
        CEUtils.sqlAddField(sqlFields, "HTTLAN");
        CEUtils.sqlAddField(sqlFields, "ti.pspy as PSPY_1");
        CEUtils.sqlAddField(sqlFields, "HTPPHY");
        CEUtils.sqlAddField(sqlFields, "HTPMOR");
        CEUtils.sqlAddField(sqlFields, "HTINAC");
        CEUtils.sqlAddField(sqlFields, "HTLDU1");
        CEUtils.sqlAddField(sqlFields, "HTLDU2");
        CEUtils.sqlAddField(sqlFields, "HTLDEC");
        CEUtils.sqlAddField(sqlFields, "HTLDUC");
        CEUtils.sqlAddField(sqlFields, "HTNTIE");
        CEUtils.sqlAddField(sqlFields, "HTPOLF");
        CEUtils.sqlAddField(sqlFields, "HTPOLD");
        CEUtils.sqlAddField(sqlFields, "HTPOLI");
        CEUtils.sqlAddField(sqlFields, "ti.htitie as HTITIE_2");
        CEUtils.sqlAddField(sqlFields, "HXNAVS");
        CEUtils.sqlAddField(sqlFields, "HXNAFF");
        CEUtils.sqlAddField(sqlFields, "HXNCON");
        CEUtils.sqlAddField(sqlFields, "HXAAVS");
        CEUtils.sqlAddField(sqlFields, "HXTGAF");
        CEUtils.sqlAddField(sqlFields, "HXDDAC");
        CEUtils.sqlAddField(sqlFields, "HXDFAC");
        CEUtils.sqlAddField(sqlFields, "pnavs.pspy as PSPY_2");
        CEUtils.sqlAddField(sqlFields, "pers.htitie as HTITIE_3");
        CEUtils.sqlAddField(sqlFields, "pers.hjiloc as HJILOC");
        CEUtils.sqlAddField(sqlFields, "HPDNAI");
        CEUtils.sqlAddField(sqlFields, "HPDDEC");
        CEUtils.sqlAddField(sqlFields, "HPTETC");
        CEUtils.sqlAddField(sqlFields, "HPTSEX");
        CEUtils.sqlAddField(sqlFields, "HPTCAN");
        CEUtils.sqlAddField(sqlFields, "HPDIST");
        CEUtils.sqlAddField(sqlFields, "pers.pspy as PSPY_3");
        CEUtils.sqlAddField(sqlFields, "adr.htitie as HTITIE_4");
        CEUtils.sqlAddField(sqlFields, "adr.HAIADR as HAIADR");
        CEUtils.sqlAddField(sqlFields, "HFIAPP");
        CEUtils.sqlAddField(sqlFields, "HETTAD");
        CEUtils.sqlAddField(sqlFields, "HEDEFA");
        CEUtils.sqlAddField(sqlFields, "adr.pspy as PSPY_4");
        CEUtils.sqlAddField(sqlFields, "HEIAAU");
        CEUtils.sqlAddField(sqlFields, "HEMOTI");
        CEUtils.sqlAddField(sqlFields, "HEDDAD");
        CEUtils.sqlAddField(sqlFields, "HEDFAD");
        CEUtils.sqlAddField(sqlFields, "HEIADR");
        CEUtils.sqlAddField(sqlFields, "HEIDEX");
        CEUtils.sqlAddField(sqlFields, "HESTAT");
        CEUtils.sqlAddField(sqlFields, "adrep.haiadr as HAIADR_1");
        CEUtils.sqlAddField(sqlFields, "adrep.hjiloc as HJILOC_1");
        CEUtils.sqlAddField(sqlFields, "HADFAD");
        CEUtils.sqlAddField(sqlFields, "HAIADU");
        CEUtils.sqlAddField(sqlFields, "HATLAN");
        CEUtils.sqlAddField(sqlFields, "HATTAD");
        CEUtils.sqlAddField(sqlFields, "HAATTE");
        CEUtils.sqlAddField(sqlFields, "HAADR1");
        CEUtils.sqlAddField(sqlFields, "HAADR2");
        CEUtils.sqlAddField(sqlFields, "HAADR3");
        CEUtils.sqlAddField(sqlFields, "HAADR4");
        CEUtils.sqlAddField(sqlFields, "HACPOS");
        CEUtils.sqlAddField(sqlFields, "HARUE");
        CEUtils.sqlAddField(sqlFields, "adrep.pspy as PSPY_5");
        CEUtils.sqlAddField(sqlFields, "HADDAD");
        CEUtils.sqlAddField(sqlFields, "HAMOTI");
        CEUtils.sqlAddField(sqlFields, "HANRUE");
        CEUtils.sqlAddField(sqlFields, "HAIRUE");
        CEUtils.sqlAddField(sqlFields, "locap.hjiloc as HJILOC_2");
        CEUtils.sqlAddField(sqlFields, "HJICAN");
        CEUtils.sqlAddField(sqlFields, "locap.hnipay as HNIPAY_1");
        CEUtils.sqlAddField(sqlFields, "HJPFIL");
        CEUtils.sqlAddField(sqlFields, "HJNOPO");
        CEUtils.sqlAddField(sqlFields, "HJTNPA");
        CEUtils.sqlAddField(sqlFields, "HJNPA");
        CEUtils.sqlAddField(sqlFields, "HJCNPA");
        CEUtils.sqlAddField(sqlFields, "HJLOCC");
        CEUtils.sqlAddField(sqlFields, "HJLOCA");
        CEUtils.sqlAddField(sqlFields, "HJLANG");
        CEUtils.sqlAddField(sqlFields, "HJLAND");
        CEUtils.sqlAddField(sqlFields, "HJAFTR");
        CEUtils.sqlAddField(sqlFields, "HJNAGG");
        CEUtils.sqlAddField(sqlFields, "HJNOFS");
        CEUtils.sqlAddField(sqlFields, "HJDVAL");
        CEUtils.sqlAddField(sqlFields, "HJNSEQ");
        CEUtils.sqlAddField(sqlFields, "HJDSUP");
        CEUtils.sqlAddField(sqlFields, "HJACTI");
        CEUtils.sqlAddField(sqlFields, "HJTDES");
        CEUtils.sqlAddField(sqlFields, "HJNPAR");
        CEUtils.sqlAddField(sqlFields, "HJCPOR");
        CEUtils.sqlAddField(sqlFields, "locap.pspy as PSPY_6");
        CEUtils.sqlAddField(sqlFields, "HJPROV");
        CEUtils.sqlAddField(sqlFields, "pays.hnipay as HNIPAY_2");
        CEUtils.sqlAddField(sqlFields, "HKIMON");
        CEUtils.sqlAddField(sqlFields, "HNCISO");
        CEUtils.sqlAddField(sqlFields, "HNCCEN");
        CEUtils.sqlAddField(sqlFields, "HNINDI");
        CEUtils.sqlAddField(sqlFields, "HNCFCP");
        CEUtils.sqlAddField(sqlFields, "HNCNTE");
        CEUtils.sqlAddField(sqlFields, "HNLFR");
        CEUtils.sqlAddField(sqlFields, "HNLAL");
        CEUtils.sqlAddField(sqlFields, "HNLIT");
        CEUtils.sqlAddField(sqlFields, "pays.pspy as PSPY_7");
        CEUtils.sqlAddField(sqlFields, "HNACTI");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + "AFAFFIP af");

        sqlFrom.append(" inner join " + _getCollection() + "titierp ti on ti.HTITIE = af.HTITIE ");
        sqlFrom.append(" inner join " + _getCollection() + "TIPAVSP pnavs on pnavs.HTITIE = ti.HTITIE ");
        sqlFrom.append(" inner join " + _getCollection() + "TIPERSP pers on pers.HTITIE = ti.HTITIE ");
        sqlFrom.append(" inner join "
                + _getCollection()
                + "TIAADRP  adr on adr.HTITIE = ti.HTITIE and adr.HFIAPP = 519004 and (adr.HEIDEX = '' or adr.heidex = malnaf) and adr.hedfad = 0 ");
        sqlFrom.append(" inner join " + _getCollection() + "TIADREP adrep on adrep.HAIADR = adr.HAIADR ");
        sqlFrom.append(" inner join " + _getCollection() + "TILOCAP locap on locap.HJILOC = adrep.HJILOC ");
        sqlFrom.append(" inner join " + _getCollection() + "TIPAYSP pays on pays.HNIPAY = locap.HNIPAY ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        CEUtils.sqlAddCondition(sqlWhere, " mattaf not in (804013, 804004, 804009, 19150036)");
        CEUtils.sqlAddCondition(sqlWhere, " (madfin = 0 or madfin >= 20050101) ");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFRee();
    }

}
