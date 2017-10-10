package globaz.prestation.utils.compta;

import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import globaz.prestation.enums.codeprestation.soustype.PRSousTypeCodePrestationPC;
import globaz.prestation.enums.codeprestation.soustype.PRSousTypeCodePrestationRFM;

public class PRRubriqueComptableResolverTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println("Tests du genre de prestation PC 110");
            for (PRSousTypeCodePrestationPC stcp : PRSousTypeCodePrestationPC.values()) {
                String genrePrestation = PRCodePrestationPC._110.getCodePrestationAsString();
                String csRubriqueCompty = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                        genrePrestation, stcp.getSousTypeCodePrestationAsString(), false);
                PRRubriqueComptableResolverTest.print(genrePrestation, stcp.getSousTypeCodePrestationAsString(),
                        csRubriqueCompty);
            }

            System.out.println("Tests du genre de prestation PC 113");
            for (PRSousTypeCodePrestationPC stcp : PRSousTypeCodePrestationPC.values()) {
                String genrePrestation = PRCodePrestationPC._113.getCodePrestationAsString();
                String csRubriqueCompty = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                        genrePrestation, stcp.getSousTypeCodePrestationAsString(), false);
                PRRubriqueComptableResolverTest.print(genrePrestation, stcp.getSousTypeCodePrestationAsString(),
                        csRubriqueCompty);
            }

            System.out.println("Tests du genre de prestation PC 150");
            for (PRSousTypeCodePrestationPC stcp : PRSousTypeCodePrestationPC.values()) {
                String genrePrestation = PRCodePrestationPC._150.getCodePrestationAsString();
                String csRubriqueCompty = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                        genrePrestation, stcp.getSousTypeCodePrestationAsString(), false);
                PRRubriqueComptableResolverTest.print(genrePrestation, stcp.getSousTypeCodePrestationAsString(),
                        csRubriqueCompty);
            }

            System.out.println("Tests du genre de prestation RFM 210");
            for (PRSousTypeCodePrestationRFM stcp : PRSousTypeCodePrestationRFM.values()) {
                String genrePrestation = PRCodePrestationRFM._210.getCodePrestationAsString();
                String csRubriqueCompty = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                        genrePrestation, stcp.getSousTypeCodePrestationAsString(), false);
                PRRubriqueComptableResolverTest.print(genrePrestation, stcp.getSousTypeCodePrestationAsString(),
                        csRubriqueCompty);
            }

            System.out.println("Tests du genre de prestation RFM 213");
            for (PRSousTypeCodePrestationRFM stcp : PRSousTypeCodePrestationRFM.values()) {
                String genrePrestation = PRCodePrestationRFM._213.getCodePrestationAsString();
                String csRubriqueCompty = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                        genrePrestation, stcp.getSousTypeCodePrestationAsString(), false);
                PRRubriqueComptableResolverTest.print(genrePrestation, stcp.getSousTypeCodePrestationAsString(),
                        csRubriqueCompty);
            }

            System.out.println("Tests du genre de prestation RFM 250");
            for (PRSousTypeCodePrestationRFM stcp : PRSousTypeCodePrestationRFM.values()) {
                String genrePrestation = PRCodePrestationRFM._250.getCodePrestationAsString();
                String csRubriqueCompty = PRRubriqueComptableResolver.getCSRubriqueComptablePCRFMStandard(
                        genrePrestation, stcp.getSousTypeCodePrestationAsString(), false);
                PRRubriqueComptableResolverTest.print(genrePrestation, stcp.getSousTypeCodePrestationAsString(),
                        csRubriqueCompty);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void print(String codePrestation, String sousTypeGenrePrestation, String csRubriqueCompta) {
        System.out.println("Genre de prestation [" + codePrestation + "], sous-type genre prestation = ["
                + sousTypeGenrePrestation + "], rubrique comptable = [" + csRubriqueCompta + "]");
    }
}
