package globaz.corvus.utils.enumere.genre.prestations;

import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import java.util.ArrayList;
import java.util.List;

/**
 * Groupement des prestations pour récupérer le bénéficiaire principal
 * 
 * @author SCR
 */
public abstract class REGenrePrestationEnum {

    public static final List<String> groupe1 = new ArrayList<String>();
    public static final List<String> groupe2 = new ArrayList<String>();
    public static final List<String> groupe3 = new ArrayList<String>();
    public static final List<String> groupe4 = new ArrayList<String>();
    public static final List<String> groupe5 = new ArrayList<String>();
    public static final List<String> groupe6 = new ArrayList<String>();

    public static final List<String> groupeEnfant = new ArrayList<String>();
    public static final List<String> rentesAVS = new ArrayList<String>();

    static {
        REGenrePrestationEnum.groupe1.add(REGenresPrestations.GENRE_10);
        REGenrePrestationEnum.groupe1.add(REGenresPrestations.GENRE_13);
        REGenrePrestationEnum.groupe1.add(REGenresPrestations.GENRE_20);
        REGenrePrestationEnum.groupe1.add(REGenresPrestations.GENRE_23);
        REGenrePrestationEnum.groupe1.add(REGenresPrestations.GENRE_50);
        REGenrePrestationEnum.groupe1.add(REGenresPrestations.GENRE_70);
        REGenrePrestationEnum.groupe1.add(REGenresPrestations.GENRE_72);

        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_81);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_82);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_83);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_84);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_85);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_86);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_87);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_88);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_89);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_91);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_92);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_93);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_94);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_95);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_96);
        REGenrePrestationEnum.groupe2.add(REGenresPrestations.GENRE_97);

        // PC
        for (PRCodePrestationPC code : PRCodePrestationPC.values()) {
            REGenrePrestationEnum.groupe3.add(code.getCodePrestationAsString());
        }

        REGenrePrestationEnum.groupe4.add(REGenresPrestations.GENRE_33);
        REGenrePrestationEnum.groupe4.add(REGenresPrestations.GENRE_53);
        REGenrePrestationEnum.groupe4.add(REGenresPrestations.GENRE_73);

        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_14);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_15);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_16);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_24);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_25);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_26);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_34);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_35);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_36);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_45);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_54);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_55);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_56);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_74);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_75);
        REGenrePrestationEnum.groupe5.add(REGenresPrestations.GENRE_76);

        // RFM
        for (PRCodePrestationRFM code : PRCodePrestationRFM.values()) {
            REGenrePrestationEnum.groupe6.add(code.getCodePrestationAsString());
        }

        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_14);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_15);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_16);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_24);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_25);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_26);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_34);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_35);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_36);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_44);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_45);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_46);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_54);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_55);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_56);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_74);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_75);
        REGenrePrestationEnum.groupeEnfant.add(REGenresPrestations.GENRE_76);

        REGenrePrestationEnum.rentesAVS.addAll(REGenrePrestationEnum.groupe1);
        REGenrePrestationEnum.rentesAVS.addAll(REGenrePrestationEnum.groupe4);
        REGenrePrestationEnum.rentesAVS.addAll(REGenrePrestationEnum.groupeEnfant);
    }

}
