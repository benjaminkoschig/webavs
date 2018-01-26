package globaz.naos.util.ide;

import ch.admin.bfs.xmlns.bfs_5050_000101._1.SHABMsgType;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceMappingUtil;

public class IDEFoscConverter {

    public static IDEDataBean formatdata(SHABMsgType msgFosc) {
        IDEDataBean dataBean = new IDEDataBean();

        dataBean.setTypeAnnonceIde(CodeSystem.TYPE_ANNONCE_IDE_FOSC);
        dataBean.setNumeroIDERemplacement(IDEServiceMappingUtil.getNumeroIDE(msgFosc.getUid()));
        dataBean.setNumeroIDE(IDEServiceMappingUtil.getNumeroIDE(msgFosc.getUid()));
        dataBean.setStatut(AFIDEUtil.translateCodeStatut(msgFosc.getMBSTATUSCD().intValue()));
        dataBean.setRaisonSociale(msgFosc.getNAMEOLDTX());
        dataBean.setRue(msgFosc.getSTREETTX());
        dataBean.setNpa(msgFosc.getZIP1CD());
        dataBean.setLocalite(msgFosc.getTOWNTX());
        dataBean.setCanton(msgFosc.getKANTONTX());
        dataBean.setNogaCode(msgFosc.getBurNoga2008());
        dataBean.setMessageSedex50(msgFosc.getMELDUNGTX());
        return dataBean;
    }

}
