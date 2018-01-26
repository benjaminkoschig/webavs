package globaz.naos.process.ide;

import java.util.List;
import globaz.naos.util.IDEDataBean;

interface AFIdeReceptionSedexInterface<T> {

    String getPackageClass();

    List<IDEDataBean> convertMessageToIdeDataBean(T message);
}