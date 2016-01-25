package globaz.osiris.db.ordres.format.utils;

import globaz.framework.filetransfer.FWAsciiFileRecordDescriptor;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;

public class CAOrdreFormatterUtils {

    public static final String COMMUNICATION1 = "communication1";

    public static final String COMMUNICATION2 = "communication2";
    public static final String COMMUNICATION3 = "communication3";
    public static final String COMMUNICATION4 = "communication4";
    public static final int LSV_COMMUNICATION_MAX_LENGTH = 35;

    /**
     * Préparation du record de Communications LSV
     * 
     * @param or
     * @throws Exception
     */
    public static void initLSVRecordCommunications(CAOperationOrdreRecouvrement or, FWAsciiFileRecordDescriptor record)
            throws Exception {
        String tmpMotif = or.getMotif();
        if (tmpMotif.length() > CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH) {
            record.setFieldValue(CAOrdreFormatterUtils.COMMUNICATION1,
                    tmpMotif.substring(0, CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH));
            if (tmpMotif.length() > 2 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH) {
                record.setFieldValue(CAOrdreFormatterUtils.COMMUNICATION2, tmpMotif.substring(
                        CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH,
                        2 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH));

                if (tmpMotif.length() > 3 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH) {
                    record.setFieldValue(CAOrdreFormatterUtils.COMMUNICATION3, tmpMotif.substring(
                            2 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH,
                            3 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH));

                    if (tmpMotif.length() > 4 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH) {
                        record.setFieldValue(CAOrdreFormatterUtils.COMMUNICATION4, tmpMotif.substring(
                                3 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH,
                                4 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH));
                    } else {
                        record.setFieldValue(
                                CAOrdreFormatterUtils.COMMUNICATION4,
                                tmpMotif.substring(3 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH,
                                        tmpMotif.length()));
                    }
                } else {
                    record.setFieldValue(
                            CAOrdreFormatterUtils.COMMUNICATION3,
                            tmpMotif.substring(2 * CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH,
                                    tmpMotif.length()));
                }
            } else {
                record.setFieldValue(CAOrdreFormatterUtils.COMMUNICATION2,
                        tmpMotif.substring(CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, tmpMotif.length()));
            }
        } else {
            record.setFieldValue(CAOrdreFormatterUtils.COMMUNICATION1, or.getMotif());
        }
    }

}
