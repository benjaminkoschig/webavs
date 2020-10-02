package ch.globaz.pegasus.rpc.businessImpl.converter;

import ch.globaz.pegasus.business.constantes.EPCRegionLoyer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterRentRegion {
    private static final String RENT_REGION_BIG_CITY = "BIG_CITY";
    private static final String RENT_REGION_CITY = "CITY";
    private static final String RENT_REGION_BIG_COUNTRY = "COUNTRY";
    private static final Logger LOG = LoggerFactory.getLogger(ConverterRentRegion.class);

    public static String convert(EPCRegionLoyer codeRegion) {
        switch (codeRegion) {
            case REGION_1:
                return RENT_REGION_BIG_CITY;
            case REGION_2:
                return RENT_REGION_CITY;
            case REGION_3:
                return RENT_REGION_BIG_COUNTRY;
            default:
                throw new RpcBusinessException("code région introuvable");
        }
    }

}
