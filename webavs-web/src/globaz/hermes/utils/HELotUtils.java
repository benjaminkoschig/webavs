package globaz.hermes.utils;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

public class HELotUtils {

    /*
     * 30.03.2007 (dda) Note : Méthodes copiées en attente d'interface pour CGPeriodeComptableEnvoiAnnonces.
     */

    public String getLotFooter(BSession session, int nbRecords) throws Exception {
        String footer = new String("9901");
        footer += session.getApplication().getProperty("noCaisse") + session.getApplication().getProperty("noAgence");
        footer = JadeStringUtil.leftJustify(footer, 24);
        footer += "T0" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYY);
        footer += JadeStringUtil.rightJustify(String.valueOf(nbRecords), 6, '0');
        if ("true".equals(session.getApplication().getProperty("ftp.test"))) {
            footer += "TEST";
        } else {
            footer += "    ";
        }
        footer = JadeStringUtil.leftJustify(footer, 120);
        return footer;
    }

    public String getLotHeader(BSession session) throws Exception {
        String header = new String("0101");
        header += session.getApplication().getProperty("noCaisse") + session.getApplication().getProperty("noAgence");
        header = JadeStringUtil.leftJustify(header, 24);
        header += "T0" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYY);
        header = JadeStringUtil.leftJustify(header, 38);
        if ("true".equals(session.getApplication().getProperty("ftp.test"))) {
            header += "TEST";
        } else {
            header += "    ";
        }
        header = JadeStringUtil.leftJustify(header, 120);
        return header;
    }

}
