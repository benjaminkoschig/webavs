package ch.globaz.al.businessimpl.services.calcul;

import globaz.globall.db.BSession;
import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import ch.globaz.al.business.models.tarif.TarifComplexModel;
import ch.globaz.al.business.models.tarif.TarifComplexSearchModel;
import ch.globaz.al.businessimpl.services.calcul.utils.TestTarifsUtils;
import ch.globaz.al.utils.ALTestCaseJU4;
import ch.globaz.al.utils.ContextProvider;

/**
 * Classe permettant le test des tarifs se trouvant en base de données. Les cas de test se trouvent dans le fichier
 * tarifsTests.xml à la racine du projet aftest
 * 
 * @author jts
 */
@RunWith(Parameterized.class)
public class TarifsTest extends ALTestCaseJU4 {

    @Parameters
    public static List<Object[]> getParametres() {
        return Arrays.asList(new Object[][] {
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2009_FED.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2015_FED.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2009_AGR.xml" },

                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2008_CANT.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2009_CANT.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2009_CANT_ZH.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2012_CANT_GE.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2013_CANT_VD_FR.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2014_CANT_VD.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2014_CANT_VD_DROITS_ACQUIS.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2015_CANT_VD.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2015_CANT_VD_DROITS_ACQUIS.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2015_CANT_NE.xml" },
                { "src/ch/globaz/al/businessimpl/services/calcul/res/tarifs_2015_CANT_SZ.xml" } });
    }

    /**
     * Chemin du fichier contenant les cas de test
     */
    private String pFile;

    public TarifsTest(String pFile) {
        this.pFile = pFile;
    }

    /**
     * Test des tarifs
     */
    @Ignore
    @Test
    public void testTarifs() {

        Document document = null;
        NodeList nodeList = null;
        NodeList nodeListLeg = null;
        NodeList nodeListCat = null;
        NodeList nodeListTest = null;
        String date = null;
        double montant = 0.0;
        double res = 0.0;
        HashSet<String> setLeg = new HashSet<String>();
        HashSet<String> setCat = new HashSet<String>();
        String csLegislation = null;
        String csCategorie = null;
        String age = null;
        TarifComplexSearchModel tarifs = null;
        boolean hasError = false;

        int count = 0;
        int countFail = 0;

        try {
            document = JadeXmlReader.parseFile(pFile);
            tarifs = new TarifComplexSearchModel();
            nodeList = document.getElementsByTagName("periode");

            // boucle des périodes (date)
            for (int i = 0; i < nodeList.getLength(); i++) {
                date = nodeList.item(i).getAttributes().getNamedItem("date").getNodeValue();

                nodeListLeg = nodeList.item(i).getChildNodes();

                // boucle des législations
                for (int j = 0; j < nodeListLeg.getLength(); j++) {

                    if (nodeListLeg.item(j).getNodeName().equals("legislation")) {
                        // récupération de la législation
                        csLegislation = TestTarifsUtils.getCSLegislation(nodeListLeg.item(j).getAttributes()
                                .getNamedItem("cs").getNodeValue());
                        setLeg.clear();
                        setLeg.add(csLegislation);

                        nodeListCat = nodeListLeg.item(j).getChildNodes();

                        // boucle des catégories
                        for (int k = 0; k < nodeListCat.getLength(); k++) {
                            if (nodeListCat.item(k).getNodeName().equals("categorie")) {

                                // récupération de la catégorie
                                csCategorie = TestTarifsUtils.getCSCategorie(nodeListCat.item(k).getAttributes()
                                        .getNamedItem("cs").getNodeValue());
                                setCat.clear();
                                setCat.add(csCategorie);

                                nodeListTest = nodeListCat.item(k).getChildNodes();

                                // boucle des tests
                                for (int l = 0; l < nodeListTest.getLength(); l++) {
                                    if (nodeListTest.item(l).getNodeName().equals("test")) {
                                        count++;
                                        NamedNodeMap attr = nodeListTest.item(l).getAttributes();

                                        // type de prestation
                                        tarifs.setForTypePrestation(TestTarifsUtils.getCSPrestations(attr.getNamedItem(
                                                "type").getNodeValue()));

                                        // date
                                        tarifs.setForValidite(date);

                                        // législation
                                        tarifs.setInLegislations(setLeg);

                                        // catégorie
                                        tarifs.setInCategoriesTarif(setCat);

                                        // capable exercer
                                        if (null != attr.getNamedItem("capableExercer")) {
                                            tarifs.setForCapableExercer(new Boolean(attr.getNamedItem("capableExercer")
                                                    .getNodeValue()));
                                        } else {
                                            // tarifs.setForCapableExercer(new Boolean(false));
                                            tarifs.setForCapableExercer(null);
                                        }

                                        // catégorie de résident
                                        if (null != attr.getNamedItem("residence")) {
                                            tarifs.setForCategorieResident(TestTarifsUtils.getCSResident(attr
                                                    .getNamedItem("residence").getNodeValue()));
                                        } else {
                                            tarifs.setForCategorieResident(null);
                                        }

                                        // âge
                                        if (null != attr.getNamedItem("age")) {

                                            age = attr.getNamedItem("age").getNodeValue();
                                            tarifs.setForCritereAge(age);
                                        } else {
                                            tarifs.setForCritereAge(null);
                                        }

                                        // nombre
                                        if (null != attr.getNamedItem("nbr")) {
                                            tarifs.setForCritereNombre(attr.getNamedItem("nbr").getNodeValue());
                                        } else {
                                            tarifs.setForCritereNombre(null);
                                        }

                                        // rang
                                        if (null != attr.getNamedItem("rng")) {
                                            tarifs.setForCritereRang(attr.getNamedItem("rng").getNodeValue());
                                        } else {
                                            tarifs.setForCritereRang(null);
                                        }

                                        // revenu indépendant
                                        if (null != attr.getNamedItem("revInd")) {
                                            tarifs.setForCritereRevenuIndependant(attr.getNamedItem("revInd")
                                                    .getNodeValue());
                                        } else {
                                            tarifs.setForCritereRevenuIndependant(null);
                                        }

                                        // revenu non-actif
                                        if (null != attr.getNamedItem("revNa")) {
                                            tarifs.setForCritereRevenuNonActif(attr.getNamedItem("revNa")
                                                    .getNodeValue());
                                        } else {
                                            tarifs.setForCritereRevenuNonActif(null);
                                        }

                                        JadePersistenceManager.search(tarifs);

                                        if (tarifs.getSize() == 0) {
                                            montant = 0.0;
                                        } else {
                                            montant = Double
                                                    .parseDouble(((TarifComplexModel) tarifs.getSearchResults()[0])
                                                            .getPrestationTarifModel().getMontant().trim());
                                        }

                                        res = Double.parseDouble(attr.getNamedItem("montant").getNodeValue().trim());

                                        if (res == montant) {
                                            System.out.println(pFile + " / "
                                                    + attr.getNamedItem("testId").getNodeValue() + " : OK");
                                            // si le résultat n'est pas correct,
                                            // affichage d'un message dans la
                                            // console
                                        } else {

                                            BSession session = ContextProvider.getSession();
                                            System.out.println();

                                            System.out.println(pFile
                                                    + " / "
                                                    + attr.getNamedItem("testId").getNodeValue()
                                                    + " : NOK, "
                                                    + "valeur : "
                                                    + montant
                                                    + ", valeur attendue : "
                                                    + attr.getNamedItem("montant").getNodeValue()
                                                    + " ("
                                                    + date
                                                    + ", "
                                                    + session.getCodeLibelle(csLegislation)
                                                    + ", "
                                                    + session.getCodeLibelle(csCategorie)
                                                    + ", "
                                                    + session.getCodeLibelle(tarifs.getForTypePrestation())
                                                    + ", "
                                                    + (tarifs.getForCapableExercer() != null ? (tarifs
                                                            .getForCapableExercer().booleanValue() ? "capable exercer"
                                                            : "incapable exercer") : "-") + ", "
                                                    + session.getCodeLibelle(tarifs.getForCategorieResident()) + ", "
                                                    + age + ")");
                                            hasError = true;
                                            countFail++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("Nombre de cas de test : " + count);
            System.out.println("Nombre d'échecs : " + countFail);

            if (hasError) {
                Assert.fail("Un ou plusieurs résultats ne sont pas corrects");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }
}