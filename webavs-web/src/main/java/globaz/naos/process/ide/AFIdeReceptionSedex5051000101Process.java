package globaz.naos.process.ide;

import java.util.ArrayList;
import java.util.List;
import ch.admin.bfs.xmlns.bfs_5051_000101._1.BankruptcyType;
import ch.admin.bfs.xmlns.bfs_5051_000101._1.Message;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.ide.IDEFaillite101Converter;

public class AFIdeReceptionSedex5051000101Process extends AFIdeAbstractReceptionSedex<Message> {

    static final String PACKAGE_CLASS_SEDEX_FAILLITE_101_MESSAGE = "ch.admin.bfs.xmlns.bfs_5051_000101._1";

    public List<IDEDataBean> convertMessageToIdeDataBean(Message message) {
        List<IDEDataBean> ideDataBeanList = new ArrayList<IDEDataBean>();
        for (BankruptcyType msgFaillite101 : message.getContent().getKonkursmeldung()) {
            ideDataBeanList.add(IDEFaillite101Converter.formatdata(msgFaillite101));
        }
        return ideDataBeanList;
    }

    public String getPackageClass() {
        return PACKAGE_CLASS_SEDEX_FAILLITE_101_MESSAGE;
    }

}
