package globaz.hercule.process.statOfas;

import globaz.globall.db.BSession;
import globaz.hercule.process.statOfas.jaxb.Data;
import globaz.hercule.process.statOfas.jaxb.NqCantonTyp;
import globaz.hercule.process.statOfas.jaxb.ObjectFactory;
import globaz.hercule.process.statOfas.jaxb.QuestionnaireTyp;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class CEStatOfasXmlFile {
    public static String createFile(BSession session, CEStatOfasStructure sos, CEStatOfasStructure2 sos2,
            CEStatOfasNbEmpCat nbEmpCat) throws Exception {
        String path = Jade.getInstance().getSharedDir();
        String fileName = "statOfasCE.xml";
        File xmlFile = new File(path + File.separatorChar + fileName);

        String noCaisse = session.getApplication().getProperty("noCaisse");
        String noAgence = session.getApplication().getProperty("noAgence");
        String nqCanton = session.getApplication().getProperty("nqCantonStatOfas");

        // création du contexte
        JAXBContext context = JAXBContext.newInstance(Data.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ObjectFactory fabrique = new ObjectFactory();
        Data data = fabrique.createData();

        Data.Insurance insurance = fabrique.createDataInsurance();
        insurance.setNqNr(noAgence + "." + noCaisse);

        // si propriété pas définit ou ne correspond à rien on utilise CH
        if (!JadeStringUtil.isBlank(nqCanton)) {
            insurance.setNqCanton(NqCantonTyp.valueOf(nqCanton));
        } else {
            insurance.setNqCanton(NqCantonTyp.CH);
        }
        // insertion des résultats statistiques
        QuestionnaireTyp qt = fabrique.createQuestionnaireTyp();
        qt.setZustaendigePerson("");
        qt.setTelnr("");
        qt.setEmail("");
        qt.setBemerkungen("");
        qt.setOrtDatum("");
        CEStatOfasXmlFile.fillFields(sos, sos2, nbEmpCat, qt);

        insurance.setQuestionnaire(qt);
        data.getInsurance().add(insurance);

        m.marshal(data, xmlFile);
        return xmlFile.getPath();
    }

    /**
     * Rempli les champs du formulaire avec les valeurs contenu dans la structure
     * 
     * @param sos
     * @param qt
     */
    private static void fillFields(CEStatOfasStructure sos, CEStatOfasStructure2 sos2, CEStatOfasNbEmpCat nbEmpCat,
            QuestionnaireTyp qt) {
        // Nombre de cas par année
        // année 1
        // cat. 0
        qt.setV01A1(sos.getNbCas("1", "0", "0"));
        qt.setV01B1(sos.getNbCas("1", "0", "1"));
        qt.setV01C1(sos.getNbCas("1", "0", "2"));
        qt.setV01D1(sos.getNbCasTotalForAnneeAndCategorie("1", "0"));

        // année 1
        // cat. 1
        qt.setV01E1(sos.getNbCas("1", "1", "0"));
        qt.setV01F1(sos.getNbCas("1", "1", "1"));
        qt.setV01G1(sos.getNbCas("1", "1", "2"));
        qt.setV01H1(sos.getNbCasTotalForAnneeAndCategorie("1", "1"));

        // année 1
        // cat. 2
        qt.setV01I1(sos.getNbCas("1", "2", "0"));
        qt.setV01J1(sos.getNbCas("1", "2", "1"));
        qt.setV01K1(sos.getNbCas("1", "2", "2"));
        qt.setV01L1(sos.getNbCasTotalForAnneeAndCategorie("1", "2"));

        // année 1
        // cat. 3
        qt.setV01M1(sos.getNbCas("1", "3", "0"));
        qt.setV01N1(sos.getNbCas("1", "3", "1"));
        qt.setV01O1(sos.getNbCas("1", "3", "2"));
        qt.setV01P1(sos.getNbCasTotalForAnneeAndCategorie("1", "3"));

        // année 1
        // cat. 4
        qt.setV01Q1(sos.getNbCas("1", "4", "0"));
        qt.setV01R1(sos.getNbCas("1", "4", "1"));
        qt.setV01S1(sos.getNbCas("1", "4", "2"));
        qt.setV01T1(sos.getNbCasTotalForAnneeAndCategorie("1", "4"));

        // année 1
        // cat. 0-4
        qt.setV01U1(sos.getNbCasTotalForAnneeAndResultatControl("1", "0"));
        qt.setV01V1(sos.getNbCasTotalForAnneeAndResultatControl("1", "1"));
        qt.setV01W1(sos.getNbCasTotalForAnneeAndResultatControl("1", "2"));
        qt.setV01X1(sos.getNbCasTotalForAnnee("1"));

        // année 2
        // cat. 0
        qt.setV02A1(sos.getNbCas("2", "0", "0"));
        qt.setV02B1(sos.getNbCas("2", "0", "1"));
        qt.setV02C1(sos.getNbCas("2", "0", "2"));
        qt.setV02D1(sos.getNbCasTotalForAnneeAndCategorie("2", "0"));

        // année 2
        // cat. 1
        qt.setV02E1(sos.getNbCas("2", "1", "0"));
        qt.setV02F1(sos.getNbCas("2", "1", "1"));
        qt.setV02G1(sos.getNbCas("2", "1", "2"));
        qt.setV02H1(sos.getNbCasTotalForAnneeAndCategorie("2", "1"));

        // année 2
        // cat. 2
        qt.setV02I1(sos.getNbCas("2", "2", "0"));
        qt.setV02J1(sos.getNbCas("2", "2", "1"));
        qt.setV02K1(sos.getNbCas("2", "2", "2"));
        qt.setV02L1(sos.getNbCasTotalForAnneeAndCategorie("2", "2"));

        // année 2
        // cat. 3
        qt.setV02M1(sos.getNbCas("2", "3", "0"));
        qt.setV02N1(sos.getNbCas("2", "3", "1"));
        qt.setV02O1(sos.getNbCas("2", "3", "2"));
        qt.setV02P1(sos.getNbCasTotalForAnneeAndCategorie("2", "3"));

        // année 2
        // cat. 4
        qt.setV02Q1(sos.getNbCas("2", "4", "0"));
        qt.setV02R1(sos.getNbCas("2", "4", "1"));
        qt.setV02S1(sos.getNbCas("2", "4", "2"));
        qt.setV02T1(sos.getNbCasTotalForAnneeAndCategorie("2", "4"));

        // année 2
        // cat. 0-4
        qt.setV02U1(sos.getNbCasTotalForAnneeAndResultatControl("2", "0"));
        qt.setV02V1(sos.getNbCasTotalForAnneeAndResultatControl("2", "1"));
        qt.setV02W1(sos.getNbCasTotalForAnneeAndResultatControl("2", "2"));
        qt.setV02X1(sos.getNbCasTotalForAnnee("2"));

        // année 3
        // cat. 0
        qt.setV03A1(sos.getNbCas("3", "0", "0"));
        qt.setV03B1(sos.getNbCas("3", "0", "1"));
        qt.setV03C1(sos.getNbCas("3", "0", "2"));
        qt.setV03D1(sos.getNbCasTotalForAnneeAndCategorie("3", "0"));

        // année 3
        // cat. 1
        qt.setV03E1(sos.getNbCas("3", "1", "0"));
        qt.setV03F1(sos.getNbCas("3", "1", "1"));
        qt.setV03G1(sos.getNbCas("3", "1", "2"));
        qt.setV03H1(sos.getNbCasTotalForAnneeAndCategorie("3", "1"));

        // année 3
        // cat. 2
        qt.setV03I1(sos.getNbCas("3", "2", "0"));
        qt.setV03J1(sos.getNbCas("3", "2", "1"));
        qt.setV03K1(sos.getNbCas("3", "2", "2"));
        qt.setV03L1(sos.getNbCasTotalForAnneeAndCategorie("3", "2"));

        // année 3
        // cat. 3
        qt.setV03M1(sos.getNbCas("3", "3", "0"));
        qt.setV03N1(sos.getNbCas("3", "3", "1"));
        qt.setV03O1(sos.getNbCas("3", "3", "2"));
        qt.setV03P1(sos.getNbCasTotalForAnneeAndCategorie("3", "3"));

        // année 3
        // cat. 4
        qt.setV03Q1(sos.getNbCas("3", "4", "0"));
        qt.setV03R1(sos.getNbCas("3", "4", "1"));
        qt.setV03S1(sos.getNbCas("3", "4", "2"));
        qt.setV03T1(sos.getNbCasTotalForAnneeAndCategorie("3", "4"));

        // année 3
        // cat. 0-4
        qt.setV03U1(sos.getNbCasTotalForAnneeAndResultatControl("3", "0"));
        qt.setV03V1(sos.getNbCasTotalForAnneeAndResultatControl("3", "1"));
        qt.setV03W1(sos.getNbCasTotalForAnneeAndResultatControl("3", "2"));
        qt.setV03X1(sos.getNbCasTotalForAnnee("3"));

        // année 4
        // cat. 0
        qt.setV04A1(sos.getNbCas("4", "0", "0"));
        qt.setV04B1(sos.getNbCas("4", "0", "1"));
        qt.setV04C1(sos.getNbCas("4", "0", "2"));
        qt.setV04D1(sos.getNbCasTotalForAnneeAndCategorie("4", "0"));

        // année 4
        // cat. 1
        qt.setV04E1(sos.getNbCas("4", "1", "0"));
        qt.setV04F1(sos.getNbCas("4", "1", "1"));
        qt.setV04G1(sos.getNbCas("4", "1", "2"));
        qt.setV04H1(sos.getNbCasTotalForAnneeAndCategorie("4", "1"));

        // année 4
        // cat. 2
        qt.setV04I1(sos.getNbCas("4", "2", "0"));
        qt.setV04J1(sos.getNbCas("4", "2", "1"));
        qt.setV04K1(sos.getNbCas("4", "2", "2"));
        qt.setV04L1(sos.getNbCasTotalForAnneeAndCategorie("4", "2"));

        // année 4
        // cat. 3
        qt.setV04M1(sos.getNbCas("4", "3", "0"));
        qt.setV04N1(sos.getNbCas("4", "3", "1"));
        qt.setV04O1(sos.getNbCas("4", "3", "2"));
        qt.setV04P1(sos.getNbCasTotalForAnneeAndCategorie("4", "3"));

        // année 4
        // cat. 4
        qt.setV04Q1(sos.getNbCas("4", "4", "0"));
        qt.setV04R1(sos.getNbCas("4", "4", "1"));
        qt.setV04S1(sos.getNbCas("4", "4", "2"));
        qt.setV04T1(sos.getNbCasTotalForAnneeAndCategorie("4", "4"));

        // année 4
        // cat. 0-4
        qt.setV04U1(sos.getNbCasTotalForAnneeAndResultatControl("4", "0"));
        qt.setV04V1(sos.getNbCasTotalForAnneeAndResultatControl("4", "1"));
        qt.setV04W1(sos.getNbCasTotalForAnneeAndResultatControl("4", "2"));
        qt.setV04X1(sos.getNbCasTotalForAnnee("4"));

        // année 5
        // cat. 0
        qt.setV05A1(sos.getNbCas("5", "0", "0"));
        qt.setV05B1(sos.getNbCas("5", "0", "1"));
        qt.setV05C1(sos.getNbCas("5", "0", "2"));
        qt.setV05D1(sos.getNbCasTotalForAnneeAndCategorie("5", "0"));

        // année 5
        // cat. 1
        qt.setV05E1(sos.getNbCas("5", "1", "0"));
        qt.setV05F1(sos.getNbCas("5", "1", "1"));
        qt.setV05G1(sos.getNbCas("5", "1", "2"));
        qt.setV05H1(sos.getNbCasTotalForAnneeAndCategorie("5", "1"));

        // année 5
        // cat. 2
        qt.setV05I1(sos.getNbCas("5", "2", "0"));
        qt.setV05J1(sos.getNbCas("5", "2", "1"));
        qt.setV05K1(sos.getNbCas("5", "2", "2"));
        qt.setV05L1(sos.getNbCasTotalForAnneeAndCategorie("5", "2"));

        // année 5
        // cat. 3
        qt.setV05M1(sos.getNbCas("5", "3", "0"));
        qt.setV05N1(sos.getNbCas("5", "3", "1"));
        qt.setV05O1(sos.getNbCas("5", "3", "2"));
        qt.setV05P1(sos.getNbCasTotalForAnneeAndCategorie("5", "3"));

        // année 5
        // cat. 4
        qt.setV05Q1(sos.getNbCas("5", "4", "0"));
        qt.setV05R1(sos.getNbCas("5", "4", "1"));
        qt.setV05S1(sos.getNbCas("5", "4", "2"));
        qt.setV05T1(sos.getNbCasTotalForAnneeAndCategorie("5", "4"));

        // année 5
        // cat. 0-4
        qt.setV05U1(sos.getNbCasTotalForAnneeAndResultatControl("5", "0"));
        qt.setV05V1(sos.getNbCasTotalForAnneeAndResultatControl("5", "1"));
        qt.setV05W1(sos.getNbCasTotalForAnneeAndResultatControl("5", "2"));
        qt.setV05X1(sos.getNbCasTotalForAnnee("5"));

        // année 6
        // cat. 0
        qt.setV06A1(sos.getNbCas("6", "0", "0"));
        qt.setV06B1(sos.getNbCas("6", "0", "1"));
        qt.setV06C1(sos.getNbCas("6", "0", "2"));
        qt.setV06D1(sos.getNbCasTotalForAnneeAndCategorie("6", "0"));

        // année 6
        // cat. 1
        qt.setV06E1(sos.getNbCas("6", "1", "0"));
        qt.setV06F1(sos.getNbCas("6", "1", "1"));
        qt.setV06G1(sos.getNbCas("6", "1", "2"));
        qt.setV06H1(sos.getNbCasTotalForAnneeAndCategorie("6", "1"));

        // année 6
        // cat. 2
        qt.setV06I1(sos.getNbCas("6", "2", "0"));
        qt.setV06J1(sos.getNbCas("6", "2", "1"));
        qt.setV06K1(sos.getNbCas("6", "2", "2"));
        qt.setV06L1(sos.getNbCasTotalForAnneeAndCategorie("6", "2"));

        // année 6
        // cat. 3
        qt.setV06M1(sos.getNbCas("6", "3", "0"));
        qt.setV06N1(sos.getNbCas("6", "3", "1"));
        qt.setV06O1(sos.getNbCas("6", "3", "2"));
        qt.setV06P1(sos.getNbCasTotalForAnneeAndCategorie("6", "3"));

        // année 6
        // cat. 4
        qt.setV06Q1(sos.getNbCas("6", "4", "0"));
        qt.setV06R1(sos.getNbCas("6", "4", "1"));
        qt.setV06S1(sos.getNbCas("6", "4", "2"));
        qt.setV06T1(sos.getNbCasTotalForAnneeAndCategorie("6", "4"));

        // année 6
        // cat. 0-4
        qt.setV06U1(sos.getNbCasTotalForAnneeAndResultatControl("6", "0"));
        qt.setV06V1(sos.getNbCasTotalForAnneeAndResultatControl("6", "1"));
        qt.setV06W1(sos.getNbCasTotalForAnneeAndResultatControl("6", "2"));
        qt.setV06X1(sos.getNbCasTotalForAnnee("6"));

        // année 7
        // cat. 0
        qt.setV07A1(sos.getNbCas("7", "0", "0"));
        qt.setV07B1(sos.getNbCas("7", "0", "1"));
        qt.setV07C1(sos.getNbCas("7", "0", "2"));
        qt.setV07D1(sos.getNbCasTotalForAnneeAndCategorie("7", "0"));

        // année 7
        // cat. 1
        qt.setV07E1(sos.getNbCas("7", "1", "0"));
        qt.setV07F1(sos.getNbCas("7", "1", "1"));
        qt.setV07G1(sos.getNbCas("7", "1", "2"));
        qt.setV07H1(sos.getNbCasTotalForAnneeAndCategorie("7", "1"));

        // année 7
        // cat. 2
        qt.setV07I1(sos.getNbCas("7", "2", "0"));
        qt.setV07J1(sos.getNbCas("7", "2", "1"));
        qt.setV07K1(sos.getNbCas("7", "2", "2"));
        qt.setV07L1(sos.getNbCasTotalForAnneeAndCategorie("7", "2"));

        // année 7
        // cat. 3
        qt.setV07M1(sos.getNbCas("7", "3", "0"));
        qt.setV07N1(sos.getNbCas("7", "3", "1"));
        qt.setV07O1(sos.getNbCas("7", "3", "2"));
        qt.setV07P1(sos.getNbCasTotalForAnneeAndCategorie("7", "3"));

        // année 7
        // cat. 4
        qt.setV07Q1(sos.getNbCas("7", "4", "0"));
        qt.setV07R1(sos.getNbCas("7", "4", "1"));
        qt.setV07S1(sos.getNbCas("7", "4", "2"));
        qt.setV07T1(sos.getNbCasTotalForAnneeAndCategorie("7", "4"));

        // année 7
        // cat. 0-4
        qt.setV07U1(sos.getNbCasTotalForAnneeAndResultatControl("7", "0"));
        qt.setV07V1(sos.getNbCasTotalForAnneeAndResultatControl("7", "1"));
        qt.setV07W1(sos.getNbCasTotalForAnneeAndResultatControl("7", "2"));
        qt.setV07X1(sos.getNbCasTotalForAnnee("7"));

        // année 8
        // cat. 0
        qt.setV08A1(sos.getNbCas("8", "0", "0"));
        qt.setV08B1(sos.getNbCas("8", "0", "1"));
        qt.setV08C1(sos.getNbCas("8", "0", "2"));
        qt.setV08D1(sos.getNbCasTotalForAnneeAndCategorie("8", "0"));

        // année 8
        // cat. 1
        qt.setV08E1(sos.getNbCas("8", "1", "0"));
        qt.setV08F1(sos.getNbCas("8", "1", "1"));
        qt.setV08G1(sos.getNbCas("8", "1", "2"));
        qt.setV08H1(sos.getNbCasTotalForAnneeAndCategorie("8", "1"));

        // année 8
        // cat. 2
        qt.setV08I1(sos.getNbCas("8", "2", "0"));
        qt.setV08J1(sos.getNbCas("8", "2", "1"));
        qt.setV08K1(sos.getNbCas("8", "2", "2"));
        qt.setV08L1(sos.getNbCasTotalForAnneeAndCategorie("8", "2"));

        // année 8
        // cat. 3
        qt.setV08M1(sos.getNbCas("8", "3", "0"));
        qt.setV08N1(sos.getNbCas("8", "3", "1"));
        qt.setV08O1(sos.getNbCas("8", "3", "2"));
        qt.setV08P1(sos.getNbCasTotalForAnneeAndCategorie("8", "3"));

        // année 8
        // cat. 4
        qt.setV08Q1(sos.getNbCas("8", "4", "0"));
        qt.setV08R1(sos.getNbCas("8", "4", "1"));
        qt.setV08S1(sos.getNbCas("8", "4", "2"));
        qt.setV08T1(sos.getNbCasTotalForAnneeAndCategorie("8", "4"));

        // année 8
        // cat. 0-4
        qt.setV08U1(sos.getNbCasTotalForAnneeAndResultatControl("8", "0"));
        qt.setV08V1(sos.getNbCasTotalForAnneeAndResultatControl("8", "1"));
        qt.setV08W1(sos.getNbCasTotalForAnneeAndResultatControl("8", "2"));
        qt.setV08X1(sos.getNbCasTotalForAnnee("8"));

        // année 9
        // cat. 0
        qt.setV09A1(sos.getNbCas("9", "0", "0"));
        qt.setV09B1(sos.getNbCas("9", "0", "1"));
        qt.setV09C1(sos.getNbCas("9", "0", "2"));
        qt.setV09D1(sos.getNbCasTotalForAnneeAndCategorie("9", "0"));

        // année 9
        // cat. 1
        qt.setV09E1(sos.getNbCas("9", "1", "0"));
        qt.setV09F1(sos.getNbCas("9", "1", "1"));
        qt.setV09G1(sos.getNbCas("9", "1", "2"));
        qt.setV09H1(sos.getNbCasTotalForAnneeAndCategorie("9", "1"));

        // année 9
        // cat. 2
        qt.setV09I1(sos.getNbCas("9", "2", "0"));
        qt.setV09J1(sos.getNbCas("9", "2", "1"));
        qt.setV09K1(sos.getNbCas("9", "2", "2"));
        qt.setV09L1(sos.getNbCasTotalForAnneeAndCategorie("9", "2"));

        // année 9
        // cat. 3
        qt.setV09M1(sos.getNbCas("9", "3", "0"));
        qt.setV09N1(sos.getNbCas("9", "3", "1"));
        qt.setV09O1(sos.getNbCas("9", "3", "2"));
        qt.setV09P1(sos.getNbCasTotalForAnneeAndCategorie("9", "3"));

        // année 9
        // cat. 4
        qt.setV09Q1(sos.getNbCas("9", "4", "0"));
        qt.setV09R1(sos.getNbCas("9", "4", "1"));
        qt.setV09S1(sos.getNbCas("9", "4", "2"));
        qt.setV09T1(sos.getNbCasTotalForAnneeAndCategorie("9", "4"));

        // année 9
        // cat. 0-4
        qt.setV09U1(sos.getNbCasTotalForAnneeAndResultatControl("9", "0"));
        qt.setV09V1(sos.getNbCasTotalForAnneeAndResultatControl("9", "1"));
        qt.setV09W1(sos.getNbCasTotalForAnneeAndResultatControl("9", "2"));
        qt.setV09X1(sos.getNbCasTotalForAnnee("9"));

        // ###################################################################

        // Masse salariale par année
        // année 1
        // cat. 0
        qt.setV10A1(sos.getMasseSalarialeToInt("1", "0", "0"));
        qt.setV10B1(sos.getMasseSalarialeToInt("1", "0", "1"));

        // année 1
        // cat. 1
        qt.setV10C1(sos.getMasseSalarialeToInt("1", "1", "0"));
        qt.setV10D1(sos.getMasseSalarialeToInt("1", "1", "1"));

        // année 1
        // cat. 2
        qt.setV10E1(sos.getMasseSalarialeToInt("1", "2", "0"));
        qt.setV10F1(sos.getMasseSalarialeToInt("1", "2", "1"));

        // année 1
        // cat. 3
        qt.setV10G1(sos.getMasseSalarialeToInt("1", "3", "0"));
        qt.setV10H1(sos.getMasseSalarialeToInt("1", "3", "1"));

        // année 1
        // cat. 4
        qt.setV10I1(sos.getMasseSalarialeToInt("1", "4", "0"));
        qt.setV10J1(sos.getMasseSalarialeToInt("1", "4", "1"));

        // // année 1
        // // cat. 0-4
        qt.setV10K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("1", "0"));
        qt.setV10L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("1", "1"));

        // Masse salariale par année
        // année 2
        // cat. 0
        qt.setV11A1(sos.getMasseSalarialeToInt("2", "0", "0"));
        qt.setV11B1(sos.getMasseSalarialeToInt("2", "0", "1"));

        // année 2
        // cat. 1
        qt.setV11C1(sos.getMasseSalarialeToInt("2", "1", "0"));
        qt.setV11D1(sos.getMasseSalarialeToInt("2", "1", "1"));

        // année 2
        // cat. 2
        qt.setV11E1(sos.getMasseSalarialeToInt("2", "2", "0"));
        qt.setV11F1(sos.getMasseSalarialeToInt("2", "2", "1"));

        // année 2
        // cat. 3
        qt.setV11G1(sos.getMasseSalarialeToInt("2", "3", "0"));
        qt.setV11H1(sos.getMasseSalarialeToInt("2", "3", "1"));

        // année 2
        // cat. 4
        qt.setV11I1(sos.getMasseSalarialeToInt("2", "4", "0"));
        qt.setV11J1(sos.getMasseSalarialeToInt("2", "4", "1"));

        // // année 2
        // // cat. 0-4
        qt.setV11K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("2", "0"));
        qt.setV11L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("2", "1"));

        // Masse salariale par année
        // année 3
        // cat. 0
        qt.setV12A1(sos.getMasseSalarialeToInt("3", "0", "0"));
        qt.setV12B1(sos.getMasseSalarialeToInt("3", "0", "1"));

        // année 3
        // cat. 1
        qt.setV12C1(sos.getMasseSalarialeToInt("3", "1", "0"));
        qt.setV12D1(sos.getMasseSalarialeToInt("3", "1", "1"));

        // année 3
        // cat. 2
        qt.setV12E1(sos.getMasseSalarialeToInt("3", "2", "0"));
        qt.setV12F1(sos.getMasseSalarialeToInt("3", "2", "1"));

        // année 3
        // cat. 3
        qt.setV12G1(sos.getMasseSalarialeToInt("3", "3", "0"));
        qt.setV12H1(sos.getMasseSalarialeToInt("3", "3", "1"));

        // année 3
        // cat. 4
        qt.setV12I1(sos.getMasseSalarialeToInt("3", "4", "0"));
        qt.setV12J1(sos.getMasseSalarialeToInt("3", "4", "1"));

        // // année 3
        // // cat. 0-4
        qt.setV12K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("3", "0"));
        qt.setV12L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("3", "1"));

        // Masse salariale par année
        // année 4
        // cat. 0
        qt.setV13A1(sos.getMasseSalarialeToInt("4", "0", "0"));
        qt.setV13B1(sos.getMasseSalarialeToInt("4", "0", "1"));

        // année 4
        // cat. 1
        qt.setV13C1(sos.getMasseSalarialeToInt("4", "1", "0"));
        qt.setV13D1(sos.getMasseSalarialeToInt("4", "1", "1"));

        // année 4
        // cat. 2
        qt.setV13E1(sos.getMasseSalarialeToInt("4", "2", "0"));
        qt.setV13F1(sos.getMasseSalarialeToInt("4", "2", "1"));

        // année 4
        // cat. 3
        qt.setV13G1(sos.getMasseSalarialeToInt("4", "3", "0"));
        qt.setV13H1(sos.getMasseSalarialeToInt("4", "3", "1"));

        // année 4
        // cat. 4
        qt.setV13I1(sos.getMasseSalarialeToInt("4", "4", "0"));
        qt.setV13J1(sos.getMasseSalarialeToInt("4", "4", "1"));

        // // année 4
        // // cat. 0-4
        qt.setV13K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("4", "0"));
        qt.setV13L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("4", "1"));

        // Masse salariale par année
        // année 5
        // cat. 0
        qt.setV14A1(sos.getMasseSalarialeToInt("5", "0", "0"));
        qt.setV14B1(sos.getMasseSalarialeToInt("5", "0", "1"));

        // année 5
        // cat. 1
        qt.setV14C1(sos.getMasseSalarialeToInt("5", "1", "0"));
        qt.setV14D1(sos.getMasseSalarialeToInt("5", "1", "1"));

        // année 5
        // cat. 2
        qt.setV14E1(sos.getMasseSalarialeToInt("5", "2", "0"));
        qt.setV14F1(sos.getMasseSalarialeToInt("5", "2", "1"));

        // année 5
        // cat. 3
        qt.setV14G1(sos.getMasseSalarialeToInt("5", "3", "0"));
        qt.setV14H1(sos.getMasseSalarialeToInt("5", "3", "1"));

        // année 5
        // cat. 4
        qt.setV14I1(sos.getMasseSalarialeToInt("5", "4", "0"));
        qt.setV14J1(sos.getMasseSalarialeToInt("5", "4", "1"));

        // // année 5
        // // cat. 0-4
        qt.setV14K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("5", "0"));
        qt.setV14L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("5", "1"));

        // Masse salariale par année
        // année 6
        // cat. 0
        qt.setV15A1(sos.getMasseSalarialeToInt("6", "0", "0"));
        qt.setV15B1(sos.getMasseSalarialeToInt("6", "0", "1"));

        // année 6
        // cat. 1
        qt.setV15C1(sos.getMasseSalarialeToInt("6", "1", "0"));
        qt.setV15D1(sos.getMasseSalarialeToInt("6", "1", "1"));

        // année 6
        // cat. 2
        qt.setV15E1(sos.getMasseSalarialeToInt("6", "2", "0"));
        qt.setV15F1(sos.getMasseSalarialeToInt("6", "2", "1"));

        // année 6
        // cat. 3
        qt.setV15G1(sos.getMasseSalarialeToInt("6", "3", "0"));
        qt.setV15H1(sos.getMasseSalarialeToInt("6", "3", "1"));

        // année 6
        // cat. 4
        qt.setV15I1(sos.getMasseSalarialeToInt("6", "4", "0"));
        qt.setV15J1(sos.getMasseSalarialeToInt("6", "4", "1"));

        // // année 6
        // // cat. 0-4
        qt.setV15K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("6", "0"));
        qt.setV15L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("6", "1"));

        // Masse salariale par année
        // année 7
        // cat. 0
        qt.setV16A1(sos.getMasseSalarialeToInt("7", "0", "0"));
        qt.setV16B1(sos.getMasseSalarialeToInt("7", "0", "1"));

        // année 7
        // cat. 1
        qt.setV16C1(sos.getMasseSalarialeToInt("7", "1", "0"));
        qt.setV16D1(sos.getMasseSalarialeToInt("7", "1", "1"));

        // année 7
        // cat. 2
        qt.setV16E1(sos.getMasseSalarialeToInt("7", "2", "0"));
        qt.setV16F1(sos.getMasseSalarialeToInt("7", "2", "1"));

        // année 7
        // cat. 3
        qt.setV16G1(sos.getMasseSalarialeToInt("7", "3", "0"));
        qt.setV16H1(sos.getMasseSalarialeToInt("7", "3", "1"));

        // année 7
        // cat. 4
        qt.setV16I1(sos.getMasseSalarialeToInt("7", "4", "0"));
        qt.setV16J1(sos.getMasseSalarialeToInt("7", "4", "1"));

        // // année 7
        // // cat. 0-4
        qt.setV16K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("7", "0"));
        qt.setV16L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("7", "1"));

        // Masse salariale par année
        // année 8
        // cat. 0
        qt.setV17A1(sos.getMasseSalarialeToInt("8", "0", "0"));
        qt.setV17B1(sos.getMasseSalarialeToInt("8", "0", "1"));

        // année 8
        // cat. 1
        qt.setV17C1(sos.getMasseSalarialeToInt("8", "1", "0"));
        qt.setV17D1(sos.getMasseSalarialeToInt("8", "1", "1"));

        // année 8
        // cat. 2
        qt.setV17E1(sos.getMasseSalarialeToInt("8", "2", "0"));
        qt.setV17F1(sos.getMasseSalarialeToInt("8", "2", "1"));

        // année 8
        // cat. 3
        qt.setV17G1(sos.getMasseSalarialeToInt("8", "3", "0"));
        qt.setV17H1(sos.getMasseSalarialeToInt("8", "3", "1"));

        // année 8
        // cat. 4
        qt.setV17I1(sos.getMasseSalarialeToInt("8", "4", "0"));
        qt.setV17J1(sos.getMasseSalarialeToInt("8", "4", "1"));

        // // année 8
        // // cat. 0-4
        qt.setV17K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("8", "0"));
        qt.setV17L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("8", "1"));

        // Masse salariale par année
        // année 9
        // cat. 0
        qt.setV18A1(sos.getMasseSalarialeToInt("9", "0", "0"));
        qt.setV18B1(sos.getMasseSalarialeToInt("9", "0", "1"));

        // année 9
        // cat. 1
        qt.setV18C1(sos.getMasseSalarialeToInt("9", "1", "0"));
        qt.setV18D1(sos.getMasseSalarialeToInt("9", "1", "1"));

        // année 9
        // cat. 2
        qt.setV18E1(sos.getMasseSalarialeToInt("9", "2", "0"));
        qt.setV18F1(sos.getMasseSalarialeToInt("9", "2", "1"));

        // année 9
        // cat. 3
        qt.setV18G1(sos.getMasseSalarialeToInt("9", "3", "0"));
        qt.setV18H1(sos.getMasseSalarialeToInt("9", "3", "1"));

        // année 9
        // cat. 4
        qt.setV18I1(sos.getMasseSalarialeToInt("9", "4", "0"));
        qt.setV18J1(sos.getMasseSalarialeToInt("9", "4", "1"));

        // // année 9
        // // cat. 0-4
        qt.setV18K1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("9", "0"));
        qt.setV18L1(sos.getMasseSalarialeTotalForAnneeAndResultatControl("9", "1"));

        // ###################################################################

        // rubrique 3.1
        // cat. 0
        qt.setV19A1(sos2.getNbCas("0", "0"));
        qt.setV19B1(sos2.getNbCas("0", "1"));
        qt.setV19C1(sos2.getNbCas("0", "2"));
        qt.setV19D1(sos2.getNbCasTotalReviseurExternes("0"));
        qt.setV19E1(sos2.getNbCas("0", "3"));
        qt.setV19F1(sos2.getNbCasTotal("0"));

        // rubrique 3.1
        // cat. 1
        qt.setV19G1(sos2.getNbCas("1", "0"));
        qt.setV19H1(sos2.getNbCas("1", "1"));
        qt.setV19I1(sos2.getNbCas("1", "2"));
        qt.setV19J1(sos2.getNbCasTotalReviseurExternes("1"));
        qt.setV19K1(sos2.getNbCas("1", "3"));
        qt.setV19L1(sos2.getNbCasTotal("1"));

        // rubrique 3.1
        // cat. 2
        qt.setV19M1(sos2.getNbCas("2", "0"));
        qt.setV19N1(sos2.getNbCas("2", "1"));
        qt.setV19O1(sos2.getNbCas("2", "2"));
        qt.setV19P1(sos2.getNbCasTotalReviseurExternes("2"));
        qt.setV19Q1(sos2.getNbCas("2", "3"));
        qt.setV19R1(sos2.getNbCasTotal("2"));

        // rubrique 3.1
        // cat. 3
        qt.setV19S1(sos2.getNbCas("3", "0"));
        qt.setV19T1(sos2.getNbCas("3", "1"));
        qt.setV19U1(sos2.getNbCas("3", "2"));
        qt.setV19V1(sos2.getNbCasTotalReviseurExternes("3"));
        qt.setV19W1(sos2.getNbCas("3", "3"));
        qt.setV19X1(sos2.getNbCasTotal("3"));

        // rubrique 3.1
        // cat. 4
        qt.setV19Y1(sos2.getNbCas("4", "0"));
        qt.setV19Z1(sos2.getNbCas("4", "1"));
        qt.setV19AA1(sos2.getNbCas("4", "2"));
        qt.setV19AB1(sos2.getNbCasTotalReviseurExternes("4"));
        qt.setV19AC1(sos2.getNbCas("4", "3"));
        qt.setV19AD1(sos2.getNbCasTotal("4"));

        // rubrique 3.1
        // cat. 0-4
        qt.setV19AE1(sos2.getNbCasTotalForReviseur("0"));
        qt.setV19AF1(sos2.getNbCasTotalForReviseur("1"));
        qt.setV19AG1(sos2.getNbCasTotalForReviseur("2"));
        qt.setV19AH1(sos2.getNbCasTotalReviseurExternesForAllCategorie());
        qt.setV19AI1(sos2.getNbCasTotalForReviseur("3"));
        qt.setV19AJ1(sos2.getNbCasTotalReviseurForAllCategorie());

        // rubrique 3.3
        qt.setV22A1(nbEmpCat.getNbCas("0"));
        qt.setV22B1(nbEmpCat.getNbCas("1"));
        qt.setV22C1(nbEmpCat.getNbCas("2"));
        qt.setV22D1(nbEmpCat.getNbCas("3"));
        qt.setV22E1(nbEmpCat.getNbCas("4"));
        qt.setV22F1(nbEmpCat.getNbCasTotal());
    }
}
