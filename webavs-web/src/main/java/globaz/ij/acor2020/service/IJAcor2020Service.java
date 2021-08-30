package globaz.ij.acor2020.service;

import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IJAcor2020Service {

    public InHostType createInHost(String idPrononce) {
        InHostCreator inHostCreator = new InHostCreator();
        return inHostCreator.createInHost(idPrononce);
    }
}
