package globaz.pavo.db.inscriptions.declaration;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.process.CIDeclaration;
import globaz.pavo.util.CIUtil;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;

/**
 * @author oca
 * 
 *         Chargement des données de déclaration de salaire depuis un fichier XML (format PUCS) Remarque : Je vais
 *         utiliser DOM par parser le fichier XML car il me semble plus pratique que SAX pour l'implementation des
 *         methodes next() et hasNext() de ICIDeclarationIterator
 * 
 *         A FAIRE, seul une petite partie pour test est implementée - oca 22.12.2003
 * 
 *         fichier xml utilisé pour les tests : ! il n'est pas completement conforme a la norme PUCS
 * 
 */

/*
 * <?xml version="1.0" ?> <SalaryDeclarations> <SalaryDeclaration> <Company> <Name> <HR-RC-Name></HR-RC-Name>
 * <ComplementaryLine></ComplementaryLine> </Name> <staff> <Person> <Particulars> <Lastname>Carnal</Lastname>
 * <Firstname>Olivier</Firstname> <Social-InsuranceIdentification> <AHV-AVS-Number>254.73.143.111</AHV-AVS-Number>
 * </Social-InsuranceIdentification> </Particulars> <AHV-AVS-Salaries> <AHV-AVS-Salary> <AccountingTime>
 * <ContributionPeriod> <from>01.01.2000</from> <until>01.02.2000</until> </ContributionPeriod> </AccountingTime>
 * <AHV-AVS-Income>100</AHV-AVS-Income> </AHV-AVS-Salary> <AHV-AVS-Salary> <AccountingTime> <ContributionPeriod>
 * <from>01.02.2000</from> <until>01.03.2000</until> </ContributionPeriod> </AccountingTime>
 * <AHV-AVS-Income>200</AHV-AVS-Income> </AHV-AVS-Salary> </AHV-AVS-Salaries> </Person> <Person> <Particulars>
 * <Lastname>Toto</Lastname> <Firstname>Titi</Firstname> <Social-InsuranceIdentification>
 * <AHV-AVS-Number>111.11.111.111</AHV-AVS-Number> </Social-InsuranceIdentification> </Particulars> <AHV-AVS-Salaries>
 * <AHV-AVS-Salary> <AccountingTime> <ContributionPeriod> <from>01.01.2000</from> <until>01.02.2000</until>
 * </ContributionPeriod> </AccountingTime> <AHV-AVS-Income>300</AHV-AVS-Income> </AHV-AVS-Salary> </AHV-AVS-Salaries>
 * </Person> </staff> </Company> <AccountingPeriod>2000</AccountingPeriod> </SalaryDeclaration> </SalaryDeclarations>
 */
public class CIDeclarationPUCSIterator implements ICIDeclarationIterator {

    // liste contenant tout les elements declarationSalary
    private static NodeList contributions = null;

    private static NodeList salaryDeclaration = null;

    private boolean traitementAFSeul = false;

    private boolean ccju = false;
    // index de l'objet a traité
    private int currentElement = 0;
    private int currentElementTotaux = 0;

    // Document XML pour traitement
    private Document document = null;

    // pour lecture du fichier XML
    private FileInputStream fi = null;
    // nom du fichier XML ou format PUCS
    private String filename = "";
    // pour lecture du fichier XML
    private InputSource is = null;

    // permet de savoir si il faut lancer la methode init() ou si c'est deja fait
    private boolean isReady = false;
    private boolean isReadyDate = false;
    private boolean isReadyTotaux = false;
    // nombre total d'objets à traiter dans le fichier XML
    private int nbElements = 0;
    private int nbElementsTotaux = 0;
    private String provenance = "";
    private boolean pucs2 = false;
    private BSession session = null;
    private TreeMap<String, String> tableNom = new TreeMap<String, String>();

    // Pour pouvoir remplir les information qui ne sont pas disponible dans le support.
    private BTransaction transaction = null;

    private String typeImport = CIDeclaration.CS_PUCS_II;

    /**
     * Rempli un objet CIDeclaration à partir d'un element Contribution
     * 
     */
    private CIDeclarationRecord _fillBean(Element contribution) {

        // pour exemple :
        Element periodeContr = (Element) contribution.getElementsByTagNameNS("*", "AccountingTime").item(0);
        if (traitementAFSeul) {
            periodeContr = (Element) contribution.getElementsByTagNameNS("*", "FAK-CAF-Period").item(0);
        }
        String from = getElementByTagName(periodeContr, "from");

        String until = getElementByTagName(periodeContr, "until");

        Element person = (Element) contribution.getParentNode().getParentNode();
        String montantAF = getElementByTagName(person, "FAK-CAF-ContributorySalary");

        String workSpaceCanton = getElementByTagName(person, "WorkplaceCanton");

        if (JadeStringUtil.isBlankOrZero(workSpaceCanton)) {
            workSpaceCanton = getElementByTagName(person, "FAK-CAF-WorkplaceCanton");
        }

        String workCategorie = "";
        if (ccju || DeclarationSalaireProvenance.fromValueWithOutException(provenance).isDan()) {
            workCategorie = getElementByTagName(person, "WorkCategorie");
        }

        Element particular = (Element) person.getElementsByTagNameNS("*", "Particulars").item(0);
        String nom = getElementByTagName(particular, "Lastname");
        String prenom = getElementByTagName(particular, "Firstname");

        Element rensAvs = ((Element) particular.getElementsByTagNameNS("*", "Social-InsuranceIdentification").item(0));

        String noAvs = getNumAvs(particular, rensAvs);

        String montantEcr = getElementByTagName(contribution, "AHV-AVS-Income");

        String montantAC = getElementByTagName(contribution, "ALV-AC-Income");

        String montantAC2 = getElementByTagName(contribution, "ALVZ-ACS-Income");

        Element company = (Element) person.getParentNode().getParentNode();
        String numeroAffilie = "";
        String codeRetour = "";
        if (!pucs2) {
            Element socialInsurances = (Element) company.getElementsByTagNameNS("*", "Social-Insurances").item(0);
            Element socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "Social-Insurance")
                    .item(0);
            try {
                numeroAffilie = findNumeroAffilie(socialInsurance);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {

            Element socialInsurances = (Element) company.getElementsByTagNameNS("*", "Insurances").item(0);
            // Adpatation pour PUCS4
            if (socialInsurances == null) {
                socialInsurances = (Element) company.getElementsByTagNameNS("*", "Institutions").item(0);
            }

            Element socialInsurance = null;
            try {
                socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "AHV-AVS").item(0);
                if (socialInsurance == null) {
                    socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "FAK-CAF").item(0);
                }
                numeroAffilie = findNumeroAffilie(socialInsurance);
            } catch (Exception e) {
                try {
                    socialInsurance = null;
                    socialInsurances = null;
                    socialInsurances = (Element) company.getElementsByTagNameNS("*", "Social-Insurances").item(0);
                    socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "Social-Insurance")
                            .item(0);
                    Element customer = (Element) socialInsurances.getElementsByTagNameNS("*",
                            "AHV-AVS-CustomerIdentification").item(0);
                    numeroAffilie = findNumeroAffilie(customer);
                } catch (Exception e2) {
                    // TODO : handle exception
                }

            }
            if (ccju) {
                try {
                    codeRetour = getElementByTagName(socialInsurance, "AK-CC-ReturnCode");
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
        // mapping fichier XML - objet CIDeclarationRecord
        // -----------------------------------------------

        // A faire : n'est pas dans le fichier XML, il faudra faire un mapping...
        // String numeroAffilie = "";
        CIDeclarationRecord dec = setDeclarationRecord(from, until, montantAF, workSpaceCanton, workCategorie, nom,
                prenom, noAvs, montantEcr, montantAC, montantAC2, company, numeroAffilie, codeRetour);
        return dec;

    }

    private AFAffiliation _getAffiliation(globaz.globall.db.BTransaction transaction, String noAffilie, String annee,
            int nb) throws Exception {

        AFAffiliation affiliationRet = null;
        try {
            int anneeControle = Integer.parseInt(annee);
            globaz.naos.db.affiliation.AFAffiliationListViewBean vBean = new globaz.naos.db.affiliation.AFAffiliationListViewBean();
            vBean.setForAffilieNumero(noAffilie);
            vBean.setForTypesAffParitaires();
            vBean.setForIdTiers("");
            vBean.setSession(transaction.getSession());
            vBean.find(transaction);

            for (int i = 0; i < vBean.size(); i++) {
                AFAffiliation affiliation = (AFAffiliation) vBean.getEntity(i);

                int anneeDebAffiliation = globaz.globall.util.JACalendar.getYear(affiliation.getDateDebut());
                int anneeFinAffiliation = globaz.globall.util.JACalendar.getYear(affiliation.getDateFin());
                if (((anneeControle >= anneeDebAffiliation) && (anneeControle <= anneeFinAffiliation))
                        || ((anneeControle >= anneeDebAffiliation) && (anneeFinAffiliation == 0))) {
                    affiliationRet = affiliation;
                    nb++;
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        if (nb > 1) {
            throw new Exception(getTransaction().getSession().getLabel("DT_PLUSIEURS_PERIODE_AFFILIE") + " ("
                    + noAffilie + "-" + annee + ")");

        } else {
            return affiliationRet;
        }
    }

    public boolean _hasNextJournal() throws Exception {

        try {
            if (!isReadyTotaux) {
                _initTotal();
            }

            if (currentElementTotaux < nbElementsTotaux) {
                // il y a encore un/des element(s) declarationSalary
                return true;
            }

            // il n'y a plus d'element declarationSalary
            close();
            return false;
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public boolean _hasNextVide() throws Exception {

        try {

            if (currentElementTotaux < nbElementsTotaux) {
                // il y a encore un/des element(s) declarationSalary
                return true;
            }

            // il n'y a plus d'element declarationSalary
            close();
            return false;
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    /**
     * Initialisation des structures pour traiter le fichier XML
     */
    private void _init() throws Exception {
        if (CIDeclaration.CS_PUCS_II.equals(typeImport)) {
            pucs2 = true;
        } else if (CIDeclaration.CS_PUCS_CCJU.equals(typeImport)) {
            pucs2 = true;
            ccju = true;
        }

        if (!isReady) {
            fi = new FileInputStream(getFilename());
            is = new InputSource(fi);

            try {

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setNamespaceAware(true);
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                document = documentBuilder.parse(is);
                CIDeclarationPUCSIterator.contributions = document.getElementsByTagNameNS("*", "AHV-AVS-Salary");
                nbElements = CIDeclarationPUCSIterator.contributions.getLength();
                if (nbElements == 0) {
                    fi = new FileInputStream(getFilename());
                    is = new InputSource(fi);
                    documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    document = documentBuilder.parse(is);
                    CIDeclarationPUCSIterator.contributions = document.getElementsByTagNameNS("*", "FAK-CAF-Salary");
                    nbElements = CIDeclarationPUCSIterator.contributions.getLength();
                    if (nbElements > 0) {
                        traitementAFSeul = true;
                    }
                }
                currentElement = 0;

            } catch (ParserConfigurationException e) {
                close();
                JadeLogger.error(this, e);
            }

            isReady = true;
        }
    }

    private void _initReceptionVide() throws Exception {
        if (CIDeclaration.CS_PUCS_II.equals(typeImport)) {
            pucs2 = true;
        } else if (CIDeclaration.CS_PUCS_CCJU.equals(typeImport)) {
            pucs2 = true;
            ccju = true;
        }

        fi = new FileInputStream(getFilename());
        is = new InputSource(fi);

        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(is);
            CIDeclarationPUCSIterator.salaryDeclaration = document.getElementsByTagNameNS("*", "SalaryDeclaration");
            nbElementsTotaux = CIDeclarationPUCSIterator.salaryDeclaration.getLength();
            currentElementTotaux = 0;

        } catch (ParserConfigurationException e) {
            close();
            JadeLogger.error(this, e);
        }

    }

    private void _initTotal() throws Exception {
        if (CIDeclaration.CS_PUCS_II.equals(typeImport)) {
            pucs2 = true;
        } else if (CIDeclaration.CS_PUCS_CCJU.equals(typeImport)) {
            pucs2 = true;
            ccju = true;
        }
        if (!isReady) {
            fi = new FileInputStream(getFilename());
            is = new InputSource(fi);

            try {

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setNamespaceAware(true);
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                document = documentBuilder.parse(is);
                CIDeclarationPUCSIterator.salaryDeclaration = document.getElementsByTagNameNS("*", "SalaryDeclaration");
                nbElementsTotaux = CIDeclarationPUCSIterator.salaryDeclaration.getLength();
                currentElementTotaux = 0;

            } catch (ParserConfigurationException e) {
                close();
                JadeLogger.error(this, e);
            }

            isReadyTotaux = true;
        }

    }

    /**
     * 
     * Fermeture des fichier utilisés
     * 
     */
    @Override
    public void close() {
        if (fi != null) {
            try {
                fi.close();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        isReady = false;
    }

    /**
     * Recherche du numéro d'affilié
     * 2 balises si AVS ou AF
     * 
     * @param socialInsurance
     * @return
     * @throws Exception
     */
    private String findNumeroAffilie(Element socialInsurance) throws Exception {
        String numAffilie = CIUtil.UnFormatNumeroAffilie(session,
                getElementByTagName(socialInsurance, "AK-CC-CustomerNumber"));
        if (JadeStringUtil.isBlankOrZero(numAffilie)) {
            numAffilie = CIUtil.UnFormatNumeroAffilie(session,
                    getElementByTagName(socialInsurance, "FAK-CAF-CustomerNumber"));
        }
        return numAffilie;
    }

    @Override
    public String getDateReception() {

        try {
            new TreeMap<Object, Object>();
            if (CIDeclaration.CS_PUCS_II.equals(typeImport)) {
                pucs2 = true;
            }
            if (!isReadyDate) {
                fi = new FileInputStream(getFilename());
                is = new InputSource(fi);

                try {

                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    documentBuilderFactory.setNamespaceAware(true);
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    document = documentBuilder.parse(is);
                    NodeList dateReceptionList = document.getElementsByTagNameNS("*", "TransmissionDate");
                    String dateReception = dateReceptionList.item(0).getFirstChild().getNodeValue();
                    dateReception = JadeStringUtil.removeChar(dateReception, '-');
                    if (dateReception.length() > 8) {
                        dateReception = dateReception.substring(0, 8);
                    }
                    JADate date = new JADate();
                    date.fromAMJ(new BigDecimal(dateReception));
                    dateReception = date.toStr(".");
                    return dateReception;
                } catch (ParserConfigurationException e) {
                    close();
                    JadeLogger.error(this, e);
                }
            }
        } catch (Exception e) {

        }
        isReadyDate = true;
        return "";
    }

    private String getElementByTagName(Element elem, String tagName) {
        String valeurString = "";
        try {
            // Element afSalaries = (Element) person.getElementsByTagNameNS("*", "FAK-CAF-Salaries").item(0);
            valeurString = ((Element) elem.getElementsByTagNameNS("*", tagName).item(0)).getFirstChild().getNodeValue()
                    .trim();
        } catch (Exception e) {

        }
        return valeurString;
    }

    /**
     * Returns the filename.
     * 
     * @return String
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#getNbSalaires()
     */
    @Override
    public TreeMap<String, Object> getNbSalaires() {
        try {
            _initTotal();

            TreeMap<String, Object> map = new TreeMap<String, Object>();
            while (_hasNextJournal()) {
                try {
                    Element salary = (Element) CIDeclarationPUCSIterator.salaryDeclaration.item(currentElementTotaux);
                    String countributionPeriod = "";
                    if (pucs2) {
                        Element general = null;
                        try {
                            general = ((Element) salary.getElementsByTagNameNS("*",
                                    "GeneralSalaryDeclarationDescription").item(0));
                        } catch (Exception e) {

                        }
                        if (general == null) {
                            countributionPeriod = getElementByTagName(salary, "AccountingPeriod");
                        } else {
                            countributionPeriod = getElementByTagName(general, "AccountingPeriod");
                        }
                    } else {
                        countributionPeriod = getElementByTagName(salary, "AccountingPeriod");
                    }
                    Element company = (Element) salary.getElementsByTagNameNS("*", "Company").item(0);
                    Element salaryCounter = (Element) salary.getElementsByTagNameNS("*", "SalaryCounters").item(0);
                    String nbSalaires = getElementByTagName(salaryCounter, "NumberOf-AHV-AVS-Salary-Tags");
                    Element socialInsurances = null;
                    Element socialInsurance = null;
                    String numeroAffilie = "";
                    if (!pucs2) {
                        socialInsurances = (Element) company.getElementsByTagNameNS("*", "Social-Insurances").item(0);
                        socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "Social-Insurance")
                                .item(0);
                    } else {
                        try {
                            socialInsurances = (Element) company.getElementsByTagNameNS("*", "Insurances").item(0);
                            // Adpatation pour PUCS4
                            if (socialInsurances == null) {
                                socialInsurances = (Element) company.getElementsByTagNameNS("*", "Institutions")
                                        .item(0);
                            }
                            socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "AHV-AVS").item(0);
                            numeroAffilie = findNumeroAffilie(socialInsurance);
                        } catch (Exception e) {
                            try {
                                socialInsurance = null;
                                socialInsurances = null;
                                socialInsurances = (Element) company.getElementsByTagNameNS("*", "Social-Insurances")
                                        .item(0);
                                socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*",
                                        "Social-Insurance").item(0);
                                Element customer = (Element) socialInsurances.getElementsByTagNameNS("*",
                                        "AHV-AVS-CustomerIdentification").item(0);
                                numeroAffilie = findNumeroAffilie(customer);
                            } catch (Exception e2) {
                                // TODO: handle exception
                            }
                        }
                    }
                    // System.out.println(nbSalaires+" "+countributionPeriod +" "+numeroAffilie);
                    currentElementTotaux++;
                    map.put(countributionPeriod + numeroAffilie, nbSalaires);
                } catch (Exception e) {
                    currentElementTotaux++;
                }
            }

            return map;

        } catch (Exception e) {
            JadeLogger.info(this, e);
        }

        return null;
    }

    @Override
    public TreeMap<String, String> getNoAffiliePourReception() throws Exception {
        _initReceptionVide();
        TreeMap<String, String> map = new TreeMap<String, String>();
        while (_hasNextVide()) {
            String noAffilie = "";
            String countributionPeriod = "";
            try {
                Element salary = (Element) CIDeclarationPUCSIterator.salaryDeclaration.item(currentElementTotaux);
                Element company = (Element) salary.getElementsByTagNameNS("*", "Company").item(0);
                Element socialInsurances = (Element) company.getElementsByTagNameNS("*", "Insurances").item(0);
                Element socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "AHV-AVS").item(0);

                noAffilie = findNumeroAffilie(socialInsurance);

                if (pucs2) {
                    Element general = null;
                    try {
                        general = ((Element) salary.getElementsByTagNameNS("*", "GeneralSalaryDeclarationDescription")
                                .item(0));
                    } catch (Exception e) {
                        general = ((Element) salary.getElementsByTagNameNS("*", "SalaryDeclaration").item(0));
                    }
                    countributionPeriod = getElementByTagName(general, "AccountingPeriod");
                } else {
                    countributionPeriod = getElementByTagName(salary, "AccountingPeriod");
                }

                String codeRetour = getElementByTagName(socialInsurance, "AK-CC-ReturnCode");

                map.put(codeRetour + countributionPeriod + noAffilie, null);
                currentElementTotaux++;
            } catch (Exception e) {

            }
        }
        return map;
    }

    private String getNumAvs(Element particular, Element rensAvs) {
        String noAvs = getElementByTagName(rensAvs, "SV-AS-Number");
        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            Element noAvsEl = ((Element) particular.getElementsByTagNameNS("*", "SV-AS-AHV-AVS-Number").item(0));
            noAvs = getElementByTagName(noAvsEl, "SV-AS-Number");
        }
        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            noAvs = getElementByTagName(rensAvs, "AHV-AVS-Number");
        }
        if ("null".equals(noAvs) || JadeStringUtil.isBlankOrZero(noAvs)) {
            noAvs = "000.00.000.000";
        }
        return noAvs;
    }

    public String getProvenance() {
        return provenance;
    }

    @Override
    public BSession getSession() {
        return session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#getTotauxJournaux()
     */
    @Override
    public TreeMap<String, Object> getTotauxJournaux() {

        TreeMap<String, Object> map = new TreeMap<String, Object>();
        try {
            _initTotal();

            while (_hasNextJournal()) {
                try {
                    Element salary = (Element) CIDeclarationPUCSIterator.salaryDeclaration.item(currentElementTotaux);
                    String countributionPeriod = getElementByTagName(salary, "AccountingPeriod");
                    if (pucs2) {
                        Element general = null;
                        try {
                            general = ((Element) salary.getElementsByTagNameNS("*",
                                    "GeneralSalaryDeclarationDescription").item(0));
                        } catch (Exception e) {
                            // general = ((Element)salary.getElementsByTagNameNS("*","SalaryDeclaration").item(0));
                        }
                        if (general != null) {
                            countributionPeriod = getElementByTagName(general, "AccountingPeriod");
                        }
                    }

                    Element totals = (Element) salary.getElementsByTagNameNS("*", "SalaryTotals").item(0);
                    String total = getElementByTagName(totals, "Total-AHV-AVS-Incomes");
                    Element company = (Element) salary.getElementsByTagNameNS("*", "Company").item(0);
                    Element socialInsurances = null;
                    Element socialInsurance = null;
                    String numeroAffilie = "";
                    if (!pucs2) {
                        socialInsurances = (Element) company.getElementsByTagNameNS("*", "Social-Insurances").item(0);
                        socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "Social-Insurance")
                                .item(0);
                    } else {
                        try {
                            socialInsurances = (Element) company.getElementsByTagNameNS("*", "Insurances").item(0);
                            // Adpatation pour PUCS4
                            if (socialInsurances == null) {
                                socialInsurances = (Element) company.getElementsByTagNameNS("*", "Institutions")
                                        .item(0);
                            }
                            socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*", "AHV-AVS").item(0);
                            numeroAffilie = findNumeroAffilie(socialInsurance);
                        } catch (Exception e) {

                            try {
                                socialInsurance = null;
                                socialInsurances = null;
                                socialInsurances = (Element) company.getElementsByTagNameNS("*", "Social-Insurances")
                                        .item(0);
                                socialInsurance = (Element) socialInsurances.getElementsByTagNameNS("*",
                                        "Social-Insurance").item(0);
                                Element customer = (Element) socialInsurances.getElementsByTagNameNS("*",
                                        "AHV-AVS-CustomerIdentification").item(0);
                                numeroAffilie = findNumeroAffilie(customer);
                            } catch (Exception e2) {

                            }
                        }
                    }
                    // System.out.println(total+" "+countributionPeriod +" "+numeroAffilie);
                    map.put(countributionPeriod + numeroAffilie, total);
                    currentElementTotaux++;
                    // map.put(salaryDeclaration, map);
                } catch (Exception e) {
                    currentElementTotaux++;
                }
            }

        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            JadeLogger.error(this, e);
        }

        return map;
    }

    /**
     * Returns the transaction.
     * 
     * @return BTransaction
     */
    @Override
    public BTransaction getTransaction() {
        return transaction;
    }

    /**
     * 
     * Retourne true si il y a encore des elements (Contribution) a traiter et false sinon
     */
    @Override
    public boolean hasNext() throws Exception {

        try {
            if (!isReady) {
                _init();
            }

            if (currentElement < nbElements) {
                // il y a encore un/des element(s) declarationSalary
                return true;
            }

            // il n'y a plus d'element declarationSalary
            close();
            return false;
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    public boolean isCcju() {
        return ccju;
    }

    /**
     * 
     * traite un element contribution et le retourne sous forme d'un objet declaration
     * 
     * oca
     */
    @Override
    public CIDeclarationRecord next() throws Exception {

        try {
            if (!isReady) {
                _init();
            }
        } catch (Exception e) {
            close();
            throw e;
        }

        Element contribution = (Element) CIDeclarationPUCSIterator.contributions.item(currentElement);
        currentElement++;

        CIDeclarationRecord dec = _fillBean(contribution);
        return dec;
    }

    public void setCcju(boolean ccju) {
        this.ccju = ccju;
    }

    /**
     * Set des informations de la déclaration de salaire
     * 
     * @param from
     * @param until
     * @param montantAF
     * @param workSpaceCanton
     * @param workCategorie
     * @param nom
     * @param prenom
     * @param noAvs
     * @param montantEcr
     * @param montantAC
     * @param montantAC2
     * @param company
     * @param numeroAffilie
     * @param codeRetour
     * @return
     */
    private CIDeclarationRecord setDeclarationRecord(String from, String until, String montantAF,
            String workSpaceCanton, String workCategorie, String nom, String prenom, String noAvs, String montantEcr,
            String montantAC, String montantAC2, Element company, String numeroAffilie, String codeRetour) {
        CIDeclarationRecord dec = new CIDeclarationRecord();

        // Recherche info affilié transmis pour swissdec
        setInfoPourTraitementSwissdec(company, dec);

        if (ccju) {
            dec.setReturnCode(codeRetour);
        }
        // A faire
        String annee = "";
        int moisDebut = 0;
        int moisFin = 0;
        int jourDebut = 0;
        int jourFin = 0;
        if (from.indexOf("-") > 0) {
            if (from.length() > 7) {
                moisDebut = Integer.parseInt(from.substring(5, 7));
                jourDebut = Integer.parseInt(from.substring(8));
                annee = from.substring(0, 4);
            }
            if (until.length() > 7) {
                moisFin = Integer.parseInt(until.substring(5, 7));
                jourFin = Integer.parseInt(until.substring(8));
            }

        } else {
            if (from.length() > 7) {
                moisDebut = Integer.parseInt(from.substring(4, 6));
                jourDebut = Integer.parseInt(from.substring(6, 8));
                annee = from.substring(0, 4);
            }
            if (until.length() > 7) {
                moisFin = Integer.parseInt(until.substring(4, 6));
                jourFin = Integer.parseInt(until.substring(6, 8));
            }

        }
        // String montantEcr = "";
        boolean montantPositif = true; // / ?

        try {

            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            IFormatData affilieFormater = application.getAffileFormater();
            String numeroAffilieFormate = affilieFormater.format(numeroAffilie);

            if (affilieFormater != null) {

            }
            // Set du traitement AF
            // si traitement AF => categorie = AF sinon AVS
            if (traitementAFSeul) {
                dec.setCategorieAffilie("AF");
            } else {
                dec.setCategorieAffilie("AVS");
            }
            if (tableNom.containsKey("numeroAffilieFormate")) {
                dec.setNomAffilie(tableNom.get(numeroAffilieFormate));
            } else {

                if (transaction != null) {

                    // date de debut et de fin de l'affilié
                    try {
                        int nbAffiliation = 0;
                        AFAffiliation affiliation = _getAffiliation(transaction, numeroAffilieFormate, annee,
                                nbAffiliation);
                        if (affiliation != null) {
                            dec.setDebutAffiliation(affiliation.getDateDebut());
                            dec.setFinAffiliation(affiliation.getDateFin());
                            dec.setNomAffilie(affiliation.getTiersNom());

                        } else {
                            if (nbAffiliation == 0) {
                                dec.setNomAffilie(getTransaction().getSession().getLabel("DT_AUNCUNE_PERIODE_AFFILIE"));
                            }
                        }

                    } catch (Exception e) {
                        dec.setNomAffilie(getTransaction().getSession().getLabel("DT_PROB_PERIODES_AFFILIE"));
                    }
                }
            }
        } catch (Exception e) {
        }

        dec.setNumeroAffilie(numeroAffilie);
        dec.setNumeroAvs(CIUtil.unFormatAVS(noAvs));
        dec.setNomPrenom(nom + "," + prenom);
        dec.setAnnee(annee);
        dec.setMoisDebut(moisDebut);
        dec.setJourDebut(jourDebut);
        dec.setJourFin(jourFin);
        dec.setMoisFin(moisFin);
        if (montantEcr.trim().startsWith("-")) {
            montantPositif = false;
            if (JadeStringUtil.isBlankOrZero(montantEcr)) {
                montantEcr = "0.00";
            }
            BigDecimal montant = new BigDecimal(montantEcr.trim());
            montant.setScale(2);
            montant = montant.abs();
            montantEcr = montant.toString();
        }
        dec.setMontantEcr(montantEcr);
        dec.setMontantPositif(montantPositif);
        dec.setMontantAc(montantAC);
        dec.setMontantAc2(montantAC2);
        dec.setMontantAf(montantAF);
        dec.setCodeCanton(workSpaceCanton);
        dec.setCategoriePers(workCategorie.toLowerCase());
        return dec;
    }

    /**
     * Sets the filename.
     * 
     * @param filename
     *            The filename to set
     */
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    private void setInfoPourTraitementSwissdec(Element company, CIDeclarationRecord dec) {
        if (DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance())) {
            // Le nom peut être différent entre celui indiqué pour l'affilié et celui référencé dans la description de
            // l'affilié
            String nomAffilieExcel = "";
            NodeList requestContext = document.getElementsByTagNameNS("*", "RequestContext");
            Element request = (Element) requestContext.item(currentElementTotaux);
            Element elCompanyName = (Element) request.getElementsByTagNameNS("*", "CompanyName").item(0);
            String nomCompany = getElementByTagName(elCompanyName, "HR-RC-Name");
            String nomDescription = getElementByTagName(company, "HR-RC-Name");
            if (JadeStringUtil.isNull(nomCompany) || JadeStringUtil.isEmpty(nomCompany)) {
                nomAffilieExcel = nomDescription;
            } else if (JadeStringUtil.isNull(nomDescription) || JadeStringUtil.isEmpty(nomDescription)) {
                nomAffilieExcel = nomCompany;
            } else if (nomCompany.equalsIgnoreCase(nomDescription)) {
                nomAffilieExcel = nomDescription;
            } else {
                // Noms différents => Concaténer les noms ( excel ne reconnaît pas le \n)
                nomAffilieExcel = nomCompany + "\n" + nomDescription;
            }

            dec.setAffilieFichierNom(nomAffilieExcel);
            dec.setAffilieFichierStreet(getElementByTagName(company, "Street"));
            dec.setAffilieFichierNpa(getElementByTagName(company, "ZIP-Code"));
            dec.setAffilieFichierCity(getElementByTagName(company, "City"));

            CIDeclarationPUCSIterator.salaryDeclaration = document.getElementsByTagNameNS("*", "SalaryDeclaration");
            Element salary = (Element) CIDeclarationPUCSIterator.salaryDeclaration.item(currentElementTotaux);
            Element generalSalarydeclarationDescription = (Element) salary.getElementsByTagNameNS("*",
                    "GeneralSalaryDeclarationDescription").item(0);
            Element contactPerson = (Element) generalSalarydeclarationDescription.getElementsByTagNameNS("*",
                    "ContactPerson").item(0);

            dec.setAffilieContactPersonName(getElementByTagName(contactPerson, "Name"));
            dec.setAffilieContactPersonEmail(getElementByTagName(contactPerson, "EmailAddress"));
            dec.setAffilieContactPersonPhone(getElementByTagName(contactPerson, "PhoneNumber"));
        }
    }

    @Override
    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    @Override
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Sets the transaction.
     * 
     * @param transaction
     *            The transaction to set
     */
    @Override
    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#setTypeImport(java.lang.String)
     */
    @Override
    public void setTypeImport(String type) {
        typeImport = type;

    }

    /**
     * retourne le nombre de record contenu dans le fichier, -1 en cas de probleme
     */
    @Override
    public int size() throws Exception {
        int size = -1;

        try {
            if (!isReady) {
                _init();
                size = nbElements;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            close();
            throw e;
        }

        return size;

    }

}
