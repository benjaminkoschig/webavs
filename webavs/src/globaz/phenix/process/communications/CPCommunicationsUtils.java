package globaz.phenix.process.communications;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CPCommunicationsUtils {
    private static final String TAG_ADD_CHEZ = "adrCtbChez";
    private static final String TAG_ADD_RUE = "adrCtbRue";
    private static final String TAG_AGR = "AGR";
    // Champs des communications
    private static final String TAG_ANNEE = "Annee1";
    private static final String TAG_ANNEE2 = "Annee2";
    private static final String TAG_ASSUJETISSEMENT_CTB = "perFis";
    private static final String TAG_CAPITAL = "Capital";
    private static final String TAG_CAPITAL_INVESTI = "totCapInvAVS";
    private static final String TAG_CODE_NAT_RENTE = "natRntPSAAVS";
    private static final String TAG_COMMENTAIRE_AGR = "rmqAvsAgr";
    private static final String TAG_COMMENTAIRE_PCI = "rmqAvsPci";
    private static final String TAG_COMMENTAIRE_PSA = "rmqAvsPSA";
    private static final String TAG_COMMENTAIRE_SAL = "rmqAvsSal";
    private static final String TAG_COMMUNICATIONS = "Communication";
    private static final String TAG_COTISATION = "Cotisation1";
    private static final String TAG_COTISATION2 = "Cotisation2";
    // private static final String TAG_DEPENSES_TV = "depPSAAVS";
    private static final String TAG_DATE_DEMANDE = "DateDemande";
    private static final String TAG_DATE_DET_CAPITAL_INVESTI = "datDetCapInvAVS";
    private static final String TAG_DATE_DET_FORTUNE = "datDetForPSAAVS";
    private static final String TAG_DATE_NAISSANCE = "datNai";
    private static final String TAG_DATEFORTUNE = "DateFortune";
    private static final String TAG_DATERECEPTION = "DateReceptionCom";
    private static final String TAG_DEB_ASSUJETISSEMENT = "datDebAsjAVS";
    private static final String TAG_DEB_PERIODE = "debPrdCcn";
    private static final String TAG_DEBUTEXERCICE = "DebutExercice1";
    private static final String TAG_DEBUTEXERCICE2 = "DebutExercice2";
    private static final String TAG_DEPENSES_TV = "RevSuiPSAAVS";
    private static final String TAG_DROIT_HABITATION = "drtHabGraRoyAVS";
    // Racines
    private static final String TAG_ENTETECOMMUNICATION = "EnteteCommunication";
    private static final String TAG_EXCES_LIQUID_PROF = "excLiqPrfOcc";
    private static final String TAG_FIN_ASSUJETISSEMENT = "datFinAsjAVS";
    private static final String TAG_FIN_PERIODE = "finPrdCcn";
    private static final String TAG_FINEXERCICE = "FinExercice1";
    private static final String TAG_FINEXERCICE2 = "FinExercice2";
    private static final String TAG_FORTUNE = "Fortune";
    private static final String TAG_FORTUNE_IMPOSABLE = "frtPSAAVS";
    private static final String TAG_GENREAFF = "GenreAffilie";
    private static final String TAG_GENRETAX = "GenreTaxation";
    private static final String TAG_GI_PROF_OCCAS = "giPrfOcc";
    // private static final String TAG_IMPOSITION_DEPENSE = "RevSuiPSAAVS";
    private static final String TAG_IMPOSITION_DEPENSE = "depPSAAVS";
    private static final String TAG_MONTANT_RENTE = "mtPSAAVS";
    private static final String TAG_NAT_COM_AGR = "natComAvsAgr";
    private static final String TAG_NAT_COM_PCI = "natComAvsPci";
    private static final String TAG_NAT_COM_PSA = "natComAvsPSA";
    private static final String TAG_NAT_COM_SAL = "natComAvsSal";
    // Champs de l'entête
    private static final String TAG_NBRERECEPTION = "NbreReceptionCom";
    private static final String TAG_NOMPRENOM = "NomPrenom";
    private static final String TAG_NUM_CAISSE_AGR = "numCaiAvsAgr";
    private static final String TAG_NUM_CAISSE_PCI = "numCaiAvsPci";
    private static final String TAG_NUM_CAISSE_PSA = "numCaiAvsPSA";
    private static final String TAG_NUM_CAISSE_SAL = "numCaiAvsSal";
    private static final String TAG_NUM_LOCALITE = "adrCtbLieu";
    private static final String TAG_NUMAFFILIE = "NumAffilie";
    private static final String TAG_NUMAVS = "NumAvs";
    private static final String TAG_NUMCAIAVSAGR = "numCaiAvsAgr";
    private static final String TAG_NUMCAIAVSSAL = "numCaiAvsSal";
    private static final String TAG_NUMCAISSE = "NumCaisse";
    private static final String TAG_NUMCONTRIBUABLE = "NumContribuable";
    private static final String TAG_NUMDEMANDE = "NumDemande";
    private static final String TAG_PCI = "PCI";
    private static final String TAG_PEN_ALIM_OBTENUE = "penAliObtAVS";
    private static final String TAG_PERIODEIFD = "prdFisCcn";
    private static final String TAG_PSA = "PSA";
    private static final String TAG_REV_ACT_IND = "revActIdpAvs";
    private static final String TAG_REVENU = "Revenu1";
    private static final String TAG_REVENU_ACTIVITES_LUCRATIVES = "revActLucPSAAVS";
    private static final String TAG_REVENU_NET = "revNet";
    private static final String TAG_REVENU2 = "Revenu2";
    private static final String TAG_REVNETAGR = "revNetAgr";
    private static final String TAG_SAL = "SAL";

    private static final String TAG_SALAIRE_COTISANT = "salCotAvs";
    private static final String TAG_SALAIRE_VERSE_CONJOINT = "salVerConAVS";
    private static final String TAG_TYPE_TAX = "typTaxAVS";
    private static final String TAG_VDGENREAFFILIE = "codTypCom";

    public static String getAddChez(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_ADD_CHEZ);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAddRue(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_ADD_RUE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return L'année de la communication
     */
    public static String getAnnee(Element communicationElement) {
        try {
            NodeList elementsAnnees = communicationElement.getElementsByTagName(TAG_ANNEE);
            Element elementsAnnee = (Element) elementsAnnees.item(0);
            return elementsAnnee.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return L'année2 de la communication dans le cas des anciennes cot. pers.
     */
    public static String getAnnee2(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_ANNEE2);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAssujetissementCtb(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_ASSUJETISSEMENT_CTB);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le montant du capital (String)
     */
    public static String getCapital(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_CAPITAL);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCapitalInvesti(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_CAPITAL_INVESTI);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCodeNatureRente(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_CODE_NAT_RENTE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCommentaire(Element communicationElement, String genreAffilie) {
        try {
            NodeList elements = null;
            if (genreAffilie.equals(TAG_PCI)) {
                elements = communicationElement.getElementsByTagName(TAG_COMMENTAIRE_PCI);
            } else if (genreAffilie.equals(TAG_PSA)) {
                elements = communicationElement.getElementsByTagName(TAG_COMMENTAIRE_PSA);
            } else if (genreAffilie.equals(TAG_AGR)) {
                elements = communicationElement.getElementsByTagName(TAG_COMMENTAIRE_AGR);
            } else if (genreAffilie.equals(TAG_SAL)) {
                elements = communicationElement.getElementsByTagName(TAG_COMMENTAIRE_SAL);
            }
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param doc
     *            DOM
     * @return communication (NodeList)
     */
    public static NodeList getCommunications(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(TAG_COMMUNICATIONS);
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le montant de cotisation (String)
     */
    public static String getCotisation(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_COTISATION);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le montant de cotisation2 (String)
     */
    public static String getCotisation2(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_COTISATION2);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDateDemande(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DATE_DEMANDE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDateDetCapitalInvesti(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DATE_DET_CAPITAL_INVESTI);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDateDetFortune(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DATE_DET_FORTUNE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return La date de la fortune (String)
     */
    public static String getDateFortune(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DATEFORTUNE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDateNaissance(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DATE_NAISSANCE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDateReceptionCom(NodeList elements) throws Exception {
        Element elementHeader = (Element) elements.item(0);
        NodeList elementsNbreRecept = elementHeader.getElementsByTagName(TAG_DATERECEPTION);
        Element elementNbreRecept = (Element) elementsNbreRecept.item(0);
        return elementNbreRecept.getFirstChild().getNodeValue();
    }

    public static String getDebutAssujetissement(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DEB_ASSUJETISSEMENT);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le début de l'exercice du capital (String)
     */
    public static String getDebutExercice(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DEBUTEXERCICE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le début de l'exercice2 du capital (String)
     */
    public static String getDebutExercice2(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DEBUTEXERCICE2);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDebutPeriode(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DEB_PERIODE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDepensesTV(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DEPENSES_TV);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDroitHabitation(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_DROIT_HABITATION);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param doc
     *            DOM
     * @return entete de communiction (NodeList)
     */
    public static NodeList getEnteteCommunication(Document doc) {
        return doc.getDocumentElement().getElementsByTagName(TAG_ENTETECOMMUNICATION);
    }

    public static String getExcedantLiquiditeProf(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_EXCES_LIQUID_PROF);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFinAssujetissement(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_FIN_ASSUJETISSEMENT);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return La fin de l'exercice (String)
     */
    public static String getFinExercice(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_FINEXERCICE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return La fin de l'exercice2 (String)
     */
    public static String getFinExercice2(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_FINEXERCICE2);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFinPeriode(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_FIN_PERIODE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le montant de la fortune (String)
     */
    public static String getFortune(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_FORTUNE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFortuneImposable(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_FORTUNE_IMPOSABLE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le code système du genre de l'affilié
     */
    public static String getGenreAffilie(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_GENREAFF);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le code système du genre de taxation (String)
     */
    public static String getGenreTaxation(Element communicationElement) {
        // TODO rechercher le code système
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_GENRETAX);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getGIprofOccasionnel(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_GI_PROF_OCCAS);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getImpositionDepense(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_IMPOSITION_DEPENSE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMontantRente(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_MONTANT_RENTE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getNatureCommunication(Element communicationElement, String genreAffilie) {
        try {
            NodeList elements = null;
            if (genreAffilie.equals(TAG_PCI)) {
                elements = communicationElement.getElementsByTagName(TAG_NAT_COM_PCI);
            } else if (genreAffilie.equals(TAG_PSA)) {
                elements = communicationElement.getElementsByTagName(TAG_NAT_COM_PSA);
            } else if (genreAffilie.equals(TAG_AGR)) {
                elements = communicationElement.getElementsByTagName(TAG_NAT_COM_AGR);
            } else if (genreAffilie.equals(TAG_SAL)) {
                elements = communicationElement.getElementsByTagName(TAG_NAT_COM_SAL);
            }
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param elements
     *            de type entête de communication
     * @return le nbre d'éléments receptionnés
     * @throws Exception
     */
    public static String getNbreReceptionCom(NodeList elements) throws Exception {
        Element elementHeader = (Element) elements.item(0);
        NodeList elementsNbreRecept = elementHeader.getElementsByTagName(TAG_NBRERECEPTION);
        Element elementNbreRecept = (Element) elementsNbreRecept.item(0);
        return elementNbreRecept.getFirstChild().getNodeValue();
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le nom, prénom (String)
     */
    public static String getNomPrenom(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NOMPRENOM);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le numéro affilié (String)
     */
    public static String getNumAffilie(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NUMAFFILIE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le numéro avs (String)
     */
    public static String getNumAVS(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NUMAVS);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le numéro de la caisse (String)
     */
    public static String getNumCaisse(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NUMCAISSE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getNumCaisse(Element communicationElement, String genreAffilie) {
        try {
            NodeList elements = null;
            if (genreAffilie.equals(TAG_PCI)) {
                elements = communicationElement.getElementsByTagName(TAG_NUM_CAISSE_PCI);
            } else if (genreAffilie.equals(TAG_PSA)) {
                elements = communicationElement.getElementsByTagName(TAG_NUM_CAISSE_PSA);
            } else if (genreAffilie.equals(TAG_AGR)) {
                elements = communicationElement.getElementsByTagName(TAG_NUM_CAISSE_AGR);
            } else if (genreAffilie.equals(TAG_SAL)) {
                elements = communicationElement.getElementsByTagName(TAG_NUM_CAISSE_SAL);
            }
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le montant de cotisation (String)
     */
    public static String getNumCaisseAgr(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NUMCAIAVSAGR);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le montant de cotisation (String)
     */
    public static String getNumCaisseSal(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NUMCAIAVSSAL);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le nunméro du contribuable (String)
     */
    public static String getNumContribuable(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NUMCONTRIBUABLE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le numéro de la demande (String)
     */
    public static String getNumDemande(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NUMDEMANDE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getNumLocalite(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_NUM_LOCALITE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getPensionAlimentaireObtenue(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_PEN_ALIM_OBTENUE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return La période fiscale IFD (String)
     */
    public static String getPeriodeFiscale(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_PERIODEIFD);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le revenu (String)
     * @throws Exception
     */
    public static String getRevenu(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_REVENU);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le revenu2 (String)
     */
    public static String getRevenu2(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_REVENU2);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getRevenuActiviteInd(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_REV_ACT_IND);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getRevenuActivitesLucratives(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_REVENU_ACTIVITES_LUCRATIVES);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getRevenuNet(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_REVENU_NET);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 
     * @param communicationElement
     *            element de type communication
     * @return Le montant de cotisation (String)
     */
    public static String getRevenuNetAgr(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_REVNETAGR);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSalaireCotisant(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_SALAIRE_COTISANT);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSalaireVerseConjoint(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_SALAIRE_VERSE_CONJOINT);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getTypeTax(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_TYPE_TAX);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getVdGenreAffilie(Element communicationElement) {
        try {
            NodeList elements = communicationElement.getElementsByTagName(TAG_VDGENREAFFILIE);
            Element element = (Element) elements.item(0);
            return element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }
}
