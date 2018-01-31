package globaz.naos.util.ide;

import ch.admin.bfs.xmlns.bfs_5051_000103._1.DebtCollectionType;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;

public class IDEFaillite103Converter {

    public static IDEDataBean formatdata(DebtCollectionType msgFaillite) {
        IDEDataBean dataBean = new IDEDataBean();

        dataBean.setTypeAnnonceIde(CodeSystem.TYPE_ANNONCE_IDE_FAILLITE);
        dataBean.setNumeroIDERemplacement(IDESedexDefensiv.defendUid(msgFaillite.getUid()));
        dataBean.setNumeroIDE(IDESedexDefensiv.defendUid(msgFaillite.getUid()));
        dataBean.setRaisonSociale(IDESedexDefensiv.defendRaisonSociale(msgFaillite.getOrganisationName(),
                msgFaillite.getOfficialName(), msgFaillite.getFirstName()));
        dataBean.setRue(IDESedexDefensiv.defendStdString(msgFaillite.getSTREET()));
        dataBean.setNpa(IDESedexDefensiv.defendStdString(msgFaillite.getZIPCODE()));
        dataBean.setLocalite(IDESedexDefensiv.defendStdString(msgFaillite.getCITY()));
        dataBean.setCanton(IDESedexDefensiv.defendStdString(msgFaillite.getCANTONCD()));
        dataBean.setNaissance(IDESedexDefensiv.defendXmlDate(msgFaillite.getBIRTHDATE()));
        dataBean.setMessageSedex50(IDESedexDefensiv.defendMessageFRDE(msgFaillite.getFR(), msgFaillite.getDE()));
        return dataBean;
    }

}
