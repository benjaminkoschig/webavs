package globaz.naos.process.ide;

import java.util.ArrayList;
import java.util.List;
import ch.admin.bfs.xmlns.bfs_5051_000104._1.Message;
import ch.admin.bfs.xmlns.bfs_5051_000104._1.NoticeToObligeesType;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.ide.IDEFaillite104Converter;

public class AFIdeReceptionSedex5051000104Process extends AFIdeAbstractReceptionSedex<Message> {

    static final String PACKAGE_CLASS_SEDEX_FAILLITE_104_MESSAGE = "ch.admin.bfs.xmlns.bfs_5051_000104._1";

    public List<IDEDataBean> convertMessageToIdeDataBean(Message message) {
        List<IDEDataBean> ideDataBeanList = new ArrayList<IDEDataBean>();
        for (NoticeToObligeesType msgFaillite104 : message.getContent().getSchuldenruf()) {
            ideDataBeanList.add(IDEFaillite104Converter.formatdata(msgFaillite104));
        }
        return ideDataBeanList;
    }

    public String getPackageClass() {
        return PACKAGE_CLASS_SEDEX_FAILLITE_104_MESSAGE;
    }

}
