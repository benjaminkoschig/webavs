package globaz.naos.util.ide;

import ch.admin.bfs.xmlns.bfs_5051_000104._1.NoticeToObligeesType;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;

public class IDEFaillite104Converter {

    public static IDEDataBean formatdata(NoticeToObligeesType msgFaillite) {
        IDEDataBean dataBean = new IDEDataBean();

        dataBean.setTypeAnnonceIde(CodeSystem.TYPE_ANNONCE_IDE_FAILLITE);
        dataBean.setNumeroIDERemplacement(IDESedexDefensiv.defendUid(msgFaillite.getFirmClosedUid()));
        dataBean.setNumeroIDE(IDESedexDefensiv.defendUid(msgFaillite.getFirmClosedUid()));
        dataBean.setRaisonSociale(IDESedexDefensiv.defendStdString(msgFaillite.getORGANISATIONTX()));
        dataBean.setRue(IDESedexDefensiv.defendStdString(msgFaillite.getRIGHTSPLACESTREET()));
        dataBean.setNpa(IDESedexDefensiv.defendStdString(msgFaillite.getPOSTALCODETX()));
        dataBean.setLocalite(IDESedexDefensiv.defendStdString(msgFaillite.getTOWNTX()));
        dataBean.setCanton(IDESedexDefensiv.defendStdString(msgFaillite.getCANTONCD()));
        dataBean.setMessageSedex50(IDESedexDefensiv.defendMessageFRDE(msgFaillite.getFR(), msgFaillite.getDE()));
        return dataBean;
    }

}
