package globaz.naos.process.ide;

import java.util.ArrayList;
import java.util.List;
import ch.admin.bfs.xmlns.bfs_5051_000103._1.DebtCollectionType;
import ch.admin.bfs.xmlns.bfs_5051_000103._1.Message;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.ide.IDEFaillite103Converter;

public class AFIdeReceptionSedex5051000103Process extends AFIdeAbstractReceptionSedex<Message> {

    static final String PACKAGE_CLASS_SEDEX_FAILLITE_103_MESSAGE = "ch.admin.bfs.xmlns.bfs_5051_000103._1";

    public List<IDEDataBean> convertMessageToIdeDataBean(Message message) {
        List<IDEDataBean> ideDataBeanList = new ArrayList<IDEDataBean>();
        for (DebtCollectionType msgFaillite103 : message.getContent().getSchuldbetreibung()) {
            ideDataBeanList.add(IDEFaillite103Converter.formatdata(msgFaillite103));
        }
        return ideDataBeanList;
    }

    public String getPackageClass() {
        return PACKAGE_CLASS_SEDEX_FAILLITE_103_MESSAGE;
    }

}
