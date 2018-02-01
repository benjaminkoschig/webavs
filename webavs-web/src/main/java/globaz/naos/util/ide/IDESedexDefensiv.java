package globaz.naos.util.ide;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import ch.ech.xmlns.ech_0097._1.UidStructureType;
import ch.globaz.common.domaine.Date;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.IDEServiceMappingUtil;

/**
 * the only use of this class is to centralize the defensive data tolerance from SEDEX xsd (no data-restriction, size
 * limit, format validation)
 * 
 * @author cel
 *
 */
public class IDESedexDefensiv {
    private static final int MESSAGE_SIZE = 3072;
    private static final int STANDARD_STRING_SIZE = 255;
    private static final int NOGA_SIZE = 6;

    protected static String defendMessage(String message) {
        return StringUtils.left(message, MESSAGE_SIZE);
    }

    protected static String defendMessageFRDE(String messageFR, String messageDE) {
        StringBuilder message = new StringBuilder();
        if (messageFR != null && !messageFR.trim().isEmpty()) {
            message.append(messageFR);
            if (messageDE != null && !messageDE.trim().isEmpty()) {
                message.append(" / ");
                message.append(messageDE);
            }
        } else {
            if (messageDE != null && !messageDE.trim().isEmpty()) {
                message.append(messageDE);
            }
        }
        return defendMessage(message.toString());
    }

    protected static String defendUid(UidStructureType uidStructureType) {
        return uidStructureType != null ? IDEServiceMappingUtil.getNumeroIDE(uidStructureType) : "";
    }

    protected static String defendXmlDate(XMLGregorianCalendar xmlCalendar) {
        return (xmlCalendar != null ? getDateFromXmlCalendar(xmlCalendar).getSwissValue() : "");
    }

    private static Date getDateFromXmlCalendar(XMLGregorianCalendar xmlCalendar) {
        return new Date(new SimpleDateFormat("dd.MM.yyyy").format(xmlCalendar.toGregorianCalendar().getTime()));
    }

    protected static String defendStdString(String input) {
        return StringUtils.left(input, STANDARD_STRING_SIZE);
    }

    protected static String defendNoga(String noga) {
        return StringUtils.left(noga, NOGA_SIZE);
    }

    protected static String defendRaisonSociale(String entrepriseRS, String nomInde, String prenomInde) {
        return defendStdString(
                entrepriseRS == null || entrepriseRS.trim().isEmpty() ? nomInde + " " + prenomInde : entrepriseRS);
    }

    protected static String defendCodeStatut(BigDecimal statusCode) {
        return AFIDEUtil.translateCodeStatut(statusCode == null ? 0 : statusCode.intValue());
    }

    private static String avoidNull(String nullable) {
        return (nullable == null ? "" : nullable);
    }
}
