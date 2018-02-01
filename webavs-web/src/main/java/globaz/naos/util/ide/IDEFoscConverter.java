package globaz.naos.util.ide;

import ch.admin.bfs.xmlns.bfs_5050_000101._1.SHABMsgType;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;

public class IDEFoscConverter {

    public static IDEDataBean formatdata(SHABMsgType msgFosc) {
        IDEDataBean dataBean = new IDEDataBean();

        dataBean.setTypeAnnonceIde(CodeSystem.TYPE_ANNONCE_IDE_FOSC);
        dataBean.setNumeroIDERemplacement(IDESedexDefensiv.defendUid(msgFosc.getUid()));
        dataBean.setNumeroIDE(IDESedexDefensiv.defendUid(msgFosc.getUid()));
        dataBean.setStatut(IDESedexDefensiv.defendCodeStatut(msgFosc.getMBSTATUSCD()));
        dataBean.setRaisonSociale(IDESedexDefensiv.defendStdString(msgFosc.getNAMEOLDTX()));
        dataBean.setRue(IDESedexDefensiv.defendStdString(msgFosc.getSTREETTX()));
        dataBean.setNpa(IDESedexDefensiv.defendStdString(msgFosc.getZIP1CD()));
        dataBean.setLocalite(IDESedexDefensiv.defendStdString(msgFosc.getTOWNTX()));
        dataBean.setCanton(IDESedexDefensiv.defendStdString(msgFosc.getKANTONTX()));
        dataBean.setNogaCode(IDESedexDefensiv.defendNoga(msgFosc.getBurNoga2008()));
        dataBean.setMessageSedex50(IDESedexDefensiv.defendMessage(msgFosc.getMELDUNGTX()));
        return dataBean;
    }

}
