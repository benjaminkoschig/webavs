/*
 * Cr�� le 10 novembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author jje
 * 
 *         Test si les d�lais de 15 mois apr�s la date de r�ception et de 12 mois entre la date de r�ception et le d�c�s
 *         sont respect�s
 */
public class RFVerificationDelaisDemandeService {

    /**
     * 
     * @param dates
     * @param session
     * @return Set<String> labelsErrors
     * @throws Exception
     */
    public Set<String[]> verifierDelais_Deces_15Mois(RFVerificationDelaisDemandeData dates, BSession session,
            Boolean isNonRetro) throws Exception {

        JACalendar cal = new JACalendarGregorian();
        Set<String[]> labelsErrors = new HashSet<String[]>();

        // D�lai de 15 mois d�pass�
        JADate dateReception = new JADate(dates.getDateReception());
        JADate dateFacture = new JADate("01."
                + PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dates.getDateFacture()));
        JADate dateFacturePlus15mois = cal.addDays(cal.addMonths(dateFacture, 16), -1);

        if (cal.compare(dateReception, dateFacturePlus15mois) == JACalendar.COMPARE_FIRSTUPPER && isNonRetro) {
            labelsErrors.add(new String[] { IRFMotifsRefus.ID_DELAI_15_MOIS_DEPASSE, dates.getMontantAccepte() });
        }

        // D�lai d�c�s 12 mois d�pass�
        if (!JadeStringUtil.isBlank(dates.getDateDeces())) {
            JADate dateDecesPlus12mois = cal
                    .addDays(cal.addMonths(
                            new JADate("01." + PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dates.getDateDeces())),
                            13), -1);

            if (cal.compare(dateReception, dateDecesPlus12mois) == JACalendar.COMPARE_FIRSTUPPER && isNonRetro) {
                labelsErrors.add(new String[] { IRFMotifsRefus.ID_DELAI_DECES_DEPASSE, dates.getMontantAccepte() });
            }
        }

        return labelsErrors;
    }
}
