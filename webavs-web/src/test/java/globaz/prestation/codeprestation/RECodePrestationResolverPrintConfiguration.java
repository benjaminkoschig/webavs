package globaz.prestation.codeprestation;

import globaz.corvus.TestUtils;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;
import globaz.prestation.utils.PRStringFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Le but de cette classe est de lister dans la console tous les codes prestations existant dans les différents
 * domaines. Ceci affin de pouvoir controller les déclarations des codes prestations
 * 
 * @author lga
 */
public class RECodePrestationResolverPrintConfiguration {

    /**
     * Incrémente un compteur de 0 à 300 et recherche tous les code prestations déclarés dans cette plage
     */
    @Test
    public void printParCodes() {
        TestUtils.printSpacer();
        System.out.println("Recherche des codes prestations déclarés dans les rentes");
        printHeader();
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum e = RECodePrestationResolver.getEnumAssocieAuCodePrestation(String.valueOf(ctr));
            if (e != null) {
                printEnum(e);
            }
        }
    }

    @Test
    public void printParIPRCodePrestationEnum() {
        List<IPRCodePrestationEnum> enums = new ArrayList<IPRCodePrestationEnum>();

        enums.add(PRCodePrestationVieillesse._10);
        enums.add(PRCodePrestationSurvivant._13);
        enums.add(PRCodePrestationInvalidite._50);
        enums.add(PRCodePrestationAPI._81);
        enums.add(PRCodePrestationPC._110);
        enums.add(PRCodePrestationRFM._210);

        System.out.println(this.getClass().getSimpleName() + " : impression des code prestations déclarés");

        TestUtils.printSpacer();
        printHeader();
        print(PRCodePrestationInvalidite.values());

        TestUtils.printSpacer();
        printHeader();
        print(PRCodePrestationSurvivant.values());

        TestUtils.printSpacer();
        printHeader();
        print(PRCodePrestationVieillesse.values());

        TestUtils.printSpacer();
        printHeader();
        print(PRCodePrestationAPI.values());

        TestUtils.printSpacer();
        printHeader();
        print(PRCodePrestationPC.values());

        TestUtils.printSpacer();
        printHeader();
        print(PRCodePrestationRFM.values());
    }

    private void print(IPRCodePrestationEnum[] cpEnum) {
        for (IPRCodePrestationEnum code : cpEnum) {
            printEnum(code);
        }
    }

    /**
     * @param code
     */
    private void printEnum(IPRCodePrestationEnum code) {
        StringBuilder sb = new StringBuilder("[");
        sb.append(PRStringFormatter.indentLeft(code.getClass().getSimpleName() + "   ", 28));
        sb.append("]");
        TestUtils.indent(sb, 30);

        // Code as int
        sb.append("[");
        sb.append(PRStringFormatter.indentLeft(String.valueOf(code.getCodePrestation()), 4));
        sb.append("]");
        TestUtils.indent(sb, 38);

        // Code as string
        sb.append("[");
        sb.append(PRStringFormatter.indentLeft(String.valueOf(code.getCodePrestation()), 4));
        sb.append("]");
        TestUtils.indent(sb, 46);

        // Type
        sb.append("[" + PRStringFormatter.indentLeft(code.getTypeDePrestation().name(), 10) + "]");
        TestUtils.indent(sb, 58);

        // Domain
        sb.append("[" + PRStringFormatter.indentLeft(code.getDomainDePrestation().name(), 8) + "]");
        TestUtils.indent(sb, 68);

        // Principale
        sb.append("[" + PRStringFormatter.indentLeft(String.valueOf(code.isPrestationPrincipale()), 10) + "]");
        TestUtils.indent(sb, 80);

        // Ordinaire
        sb.append("[" + PRStringFormatter.indentLeft(String.valueOf(code.isPrestationOrdinaire()), 9) + "]");
        TestUtils.indent(sb, 91);

        // Enfant
        sb.append("[" + PRStringFormatter.indentLeft(String.valueOf(code.isPrestationPourEnfant()), 10) + "]");
        TestUtils.indent(sb, 104);
        System.out.println(sb.toString());
    }

    private void printHeader() {
        // Nom de la classe
        StringBuilder sb = new StringBuilder("[           Class            ]");
        TestUtils.indent(sb, 30);

        // Code as int
        sb.append("[Code]");
        TestUtils.indent(sb, 38);

        // Code as string
        sb.append("[Code]");
        TestUtils.indent(sb, 46);

        // Type
        sb.append("[   Type   ]");
        TestUtils.indent(sb, 58);

        // Domain
        sb.append("[ Domain ]");
        TestUtils.indent(sb, 68);

        // Principale
        sb.append("[Principale]");
        TestUtils.indent(sb, 80);

        // Ordinaire
        sb.append("[Ordinaire]");
        TestUtils.indent(sb, 91);

        // Enfant
        sb.append("[  Enfant  ]");
        TestUtils.indent(sb, 104);

        System.out.println(sb.toString());
    }

}
