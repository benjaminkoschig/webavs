package ch.globaz.orion.businessimpl.services.partnerWeb;

import ch.globaz.common.listoutput.converterImplemented.CodeSystemeConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;

public interface ContactSalaire extends Contact {

    public static final String NUMERO_INFOROM = "0313CEB";

    @Column(name = "Etape", order = 10)
    @ColumnValueConverter(CodeSystemeConverter.class)
    public String getEtape();

    @Column(name = "RappelEnvoyer", order = 11)
    public Boolean isEnvoye();
}
