package ch.globaz.pegasus.businessimpl.utils.calcul;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;

public final class TypeRenteMap {
    public final static List<String> listeCsRenteInvalidite = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_50);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_51);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_52);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_53);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_54);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_55);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_56);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_70);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_71);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_72);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_73);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_74);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_75);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_76);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_IJAI);
            this.add(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_INVALIDITE);
            this.add(IPCApiAvsAi.CS_TYPE_API_81);
            this.add(IPCApiAvsAi.CS_TYPE_API_82);
            this.add(IPCApiAvsAi.CS_TYPE_API_83);
            this.add(IPCApiAvsAi.CS_TYPE_API_84);
            this.add(IPCApiAvsAi.CS_TYPE_API_88);
            this.add(IPCApiAvsAi.CS_TYPE_API_91);
            this.add(IPCApiAvsAi.CS_TYPE_API_92);
            this.add(IPCApiAvsAi.CS_TYPE_API_93);
        }
    };
    public final static List<String> listeCsRenteSurvivant = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_13);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_14);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_15);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_16);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_23);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_24);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_25);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_26);
            this.add(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_SURVIVANT);
        }
    };
    public final static List<String> listeCsRenteVieillesse = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_10);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_11);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_12);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_20);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_21);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_22);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_33);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_34);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_35);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_36);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_43);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_44);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_45);
            this.add(IPCRenteAvsAi.CS_TYPE_RENTE_46);
            this.add(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_VIEILLESSE);
            this.add(IPCApiAvsAi.CS_TYPE_API_85);
            this.add(IPCApiAvsAi.CS_TYPE_API_86);
            this.add(IPCApiAvsAi.CS_TYPE_API_87);
            this.add(IPCApiAvsAi.CS_TYPE_API_89);
            this.add(IPCApiAvsAi.CS_TYPE_API_94);
            this.add(IPCApiAvsAi.CS_TYPE_API_95);
            this.add(IPCApiAvsAi.CS_TYPE_API_96);
            this.add(IPCApiAvsAi.CS_TYPE_API_97);
        }
    };

}
