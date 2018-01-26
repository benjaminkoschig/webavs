package globaz.naos.util.ide;

import java.text.SimpleDateFormat;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.admin.bfs.xmlns.bfs_5051_000104._1.NoticeToObligeesType;
import ch.globaz.common.domaine.Date;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceMappingUtil;

public class IDEFaillite104Converter {

    public static IDEDataBean formatdata(NoticeToObligeesType msgFaillite) {
        IDEDataBean dataBean = new IDEDataBean();

        dataBean.setTypeAnnonceIde(CodeSystem.TYPE_ANNONCE_IDE_FAILLITE);
        dataBean.setNumeroIDERemplacement(msgFaillite.getFirmClosedUid() != null
                ? IDEServiceMappingUtil.getNumeroIDE(msgFaillite.getFirmClosedUid())
                : "");
        dataBean.setNumeroIDE(msgFaillite.getFirmClosedUid() != null
                ? IDEServiceMappingUtil.getNumeroIDE(msgFaillite.getFirmClosedUid())
                : "");
        dataBean.setRaisonSociale(msgFaillite.getORGANISATIONTX());
        dataBean.setRue(msgFaillite.getRIGHTSPLACESTREET());
        dataBean.setNpa(msgFaillite.getPOSTALCODETX());
        dataBean.setLocalite(msgFaillite.getTOWNTX());
        dataBean.setCanton(msgFaillite.getCANTONCD());
        dataBean.setMessageSedex50(msgFaillite.getFR() + " / " + msgFaillite.getDE());
        return dataBean;
    }

    private static Date getDateFromXmlCalendar(XMLGregorianCalendar xmlCalendar) {
        return new Date(new SimpleDateFormat("dd.MM.yyyy").format(xmlCalendar.toGregorianCalendar().getTime()));
    }
}
