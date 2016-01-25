package ch.globaz.pegasus.businessimpl.services.models.mutation;

import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

public class MutationCategorieResolver {

    public enum RecapDomainePca {
        AI("AI"),
        AVS("AVS");

        private String code;

        RecapDomainePca(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    };

    private static Map<String, RecapDomainePca> map = new HashMap<String, RecapDomainePca>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(IPCPCAccordee.CS_TYPE_PC_INVALIDITE, RecapDomainePca.AI);
            put(IPCPCAccordee.CS_TYPE_PC_VIELLESSE, RecapDomainePca.AVS);
            put(IPCPCAccordee.CS_TYPE_PC_SURVIVANT, RecapDomainePca.AVS);
        }
    };

    private static Map<String, RecapDomainePca> mapByCodePresation = new HashMap<String, RecapDomainePca>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(PRCodePrestationPC._150.getCodePrestationAsString(), RecapDomainePca.AI);
            put(PRCodePrestationPC._110.getCodePrestationAsString(), RecapDomainePca.AVS);
            put(PRCodePrestationPC._113.getCodePrestationAsString(), RecapDomainePca.AVS);
        }
    };

    public static RecapDomainePca getCodeCategorie(String csTypePca) {
        return MutationCategorieResolver.map.get(csTypePca);
    }

    // public static String getCodeCategorie(String csTypePca) {
    // return MutationCategorieResolver.map.get(csTypePca);
    // }

    public static RecapDomainePca getCodeCategorieByCodePresation(String codePresation) {
        return MutationCategorieResolver.mapByCodePresation.get(codePresation);
    }
}
