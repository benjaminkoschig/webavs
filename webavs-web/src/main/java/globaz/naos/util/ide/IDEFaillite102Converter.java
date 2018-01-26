package globaz.naos.util.ide;

import java.text.SimpleDateFormat;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.admin.bfs.xmlns.bfs_5051_000102._1.DebtRestructuringAgreementType;
import ch.globaz.common.domaine.Date;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceMappingUtil;

public class IDEFaillite102Converter {

    public static IDEDataBean formatdata(DebtRestructuringAgreementType msgFaillite) {
        IDEDataBean dataBean = new IDEDataBean();

        dataBean.setTypeAnnonceIde(CodeSystem.TYPE_ANNONCE_IDE_FAILLITE);
        dataBean.setNumeroIDERemplacement(
                msgFaillite.getUid() != null ? IDEServiceMappingUtil.getNumeroIDE(msgFaillite.getUid()) : "");
        dataBean.setNumeroIDE(
                msgFaillite.getUid() != null ? IDEServiceMappingUtil.getNumeroIDE(msgFaillite.getUid()) : "");
        dataBean.setRaisonSociale(msgFaillite.getOrganisationName() == null
                ? msgFaillite.getOfficialName() + " " + msgFaillite.getFirstName()
                : msgFaillite.getOrganisationName());
        dataBean.setRue(msgFaillite.getSTREET());
        dataBean.setNpa(msgFaillite.getZIPCODE());
        dataBean.setLocalite(msgFaillite.getCITY());
        dataBean.setCanton(msgFaillite.getCANTONCD());
        dataBean.setNaissance(
                msgFaillite.getBIRTHDATE() != null ? getDateFromXmlCalendar(msgFaillite.getBIRTHDATE()).getSwissValue()
                        : "");
        dataBean.setMessageSedex50(msgFaillite.getFR() + " / " + msgFaillite.getDE());
        return dataBean;
    }

    private static Date getDateFromXmlCalendar(XMLGregorianCalendar xmlCalendar) {
        return new Date(new SimpleDateFormat("dd.MM.yyyy").format(xmlCalendar.toGregorianCalendar().getTime()));
    }
}
