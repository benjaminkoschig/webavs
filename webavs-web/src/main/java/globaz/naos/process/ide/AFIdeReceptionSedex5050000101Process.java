package globaz.naos.process.ide;

import java.util.ArrayList;
import java.util.List;
import ch.admin.bfs.xmlns.bfs_5050_000101._1.Message;
import ch.admin.bfs.xmlns.bfs_5050_000101._1.SHABMsgType;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.ide.IDEFoscConverter;

public class AFIdeReceptionSedex5050000101Process extends AFIdeAbstractReceptionSedex<Message> {

    static final String PACKAGE_CLASS_SEDEX_FOSC_MESSAGE = "ch.admin.bfs.xmlns.bfs_5050_000101._1";

    public List<IDEDataBean> convertMessageToIdeDataBean(Message message) {
        List<IDEDataBean> ideDataBeanList = new ArrayList<IDEDataBean>();
        for (SHABMsgType msgFosc : message.getContent().getSHABMeldung()) {
            ideDataBeanList.add(IDEFoscConverter.formatdata(msgFosc));
        }
        return ideDataBeanList;
    }

    public String getPackageClass() {
        return PACKAGE_CLASS_SEDEX_FOSC_MESSAGE;
    }

}
