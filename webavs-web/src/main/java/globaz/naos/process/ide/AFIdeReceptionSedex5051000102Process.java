package globaz.naos.process.ide;

import java.util.ArrayList;
import java.util.List;
import ch.admin.bfs.xmlns.bfs_5051_000102._1.DebtRestructuringAgreementType;
import ch.admin.bfs.xmlns.bfs_5051_000102._1.Message;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.ide.IDEFaillite102Converter;

public class AFIdeReceptionSedex5051000102Process extends AFIdeAbstractReceptionSedex<Message> {

    static final String PACKAGE_CLASS_SEDEX_FAILLITE_102_MESSAGE = "ch.admin.bfs.xmlns.bfs_5051_000102._1";

    public List<IDEDataBean> convertMessageToIdeDataBean(Message message) {
        List<IDEDataBean> ideDataBeanList = new ArrayList<IDEDataBean>();
        for (DebtRestructuringAgreementType msgFaillite102 : message.getContent().getNachlassvertrag()) {
            ideDataBeanList.add(IDEFaillite102Converter.formatdata(msgFaillite102));
        }
        return ideDataBeanList;
    }

    public String getPackageClass() {
        return PACKAGE_CLASS_SEDEX_FAILLITE_102_MESSAGE;
    }

}
