package ch.globaz.al.businessimpl.ctrlexport;

import ch.globaz.al.utils.ALFomationUtils;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALConstLoisExport;
import ch.globaz.al.business.exceptions.ctrlexport.ALCtrlExportException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe utilitaire pour le contr�le des droits exportables
 * 
 * @author GMO
 * 
 */
public class PrestationExportableController {
    /** Conteneur pour les pays membres de l'europe (UE/AELE) */
    private static Map<String, Map<String, String>> europeCountries = new HashMap<String, Map<String, String>>();

    /** Fichier de configuration des r�gles d'exportation des allocations */
    public final static String EXPORT_RULES_FILENAME = "allocExportationRules.xml";
    /** Conteneur pour les r�gles d�finies dans le fichier de config */
    private static Map<Integer, ExportRule> rules = new HashMap<Integer, ExportRule>();
    /** R�gle d'exportation utilis�e */
    private static ExportRule usedRule = null;
    /** Document XML contenant les r�gle d'exportation */
    public static Document xmlExportRules = null;

    /**
     * Retourne la r�gle utilis� pour le dossier soumis et le droit soumis au contr�le Utilis� uniquement pour les tests
     * auto
     * 
     * @return la r�gle utilis�e
     */
    public static ExportRule getUsedRule() {
        return PrestationExportableController.usedRule;
    }

    /**
     * D�termine si les pays membres de l'europe sont d�j� charg�s ou non
     * 
     * @return <code>true</code> les pays ont �t� charg�s, <code>false</code> sinon
     */
    public static boolean isCountriesLoaded() {
        return !PrestationExportableController.europeCountries.isEmpty();
    }

    /**
     * V�rifie si un pays est membre de l'europe ou non
     * 
     * @param _pays
     *            - code pays � v�rifier
     * @return true ou false
     */
    private static boolean isInEurope(String _pays) {
        // Dans le workflow de l'application pays de toute fa�on d�j� charg�,
        // mais pas dans le cadre des tests unitaires
        if (!PrestationExportableController.isCountriesLoaded()) {
            try {
                // loadEuropeCountriesDB();
                PrestationExportableController.loadEuropeCountries();
            } catch (Exception e) {
                System.out
                        .println("PrestationExportableController - isInEurope(): Impossible de charger les pays de UE et AELE");
            }
        }
        Map<String, String> mapAELE = PrestationExportableController.europeCountries.get("AELE");
        Map<String, String> mapUE = PrestationExportableController.europeCountries.get("UE");
        boolean isAELE = false;
        boolean isUE = false;
        if (mapAELE != null) {
            isAELE = mapAELE.containsKey(_pays);
        }
        if (mapUE != null) {
            isUE = mapUE.containsKey(_pays);
        }
        return (isAELE | isUE);
    }

    /**
     * D�termine si les r�gles sont d�j� charg�es ou non
     * 
     * @return boolean
     */
    public static boolean isRulesLoaded() {
        return !PrestationExportableController.rules.isEmpty();
    }

    /**
     * Charge les pays d�finis comme membres de l'europe (UE ou AELE) depuis le fichier de configuration
     * 
     */
    public static void loadEuropeCountries() {

        NodeList nodeList = null;

        Map<String, String> countriesUE = new HashMap<String, String>();
        Map<String, String> countriesAELE = new HashMap<String, String>();

        nodeList = PrestationExportableController.xmlExportRules.getElementsByTagName("europe");
        for (int i = 0; i < nodeList.getLength(); i++) {

            // si on a pas encore de correspondance avec le pays et que si on
            // est
            // sur le noeud par d�faut => on entre, mais on met pas found = true
            // sinon on ne va pas plus tester les autres noeuds

            int nbPays = 0;
            if (nodeList.item(i).hasChildNodes()) {
                nbPays = nodeList.item(i).getChildNodes().getLength();
            }
            for (int j = 0; j < nbPays; j++) {
                Node paysNode = nodeList.item(i).getChildNodes().item(j);
                String paysName = "";
                String paysGroup = "";
                String paysCode = "";
                String paysStart = "";
                if (paysNode.getNodeName().equals("pays")) {

                    if (paysNode.hasAttributes()) {
                        paysName = paysNode.getAttributes().getNamedItem("name").getNodeValue();
                        paysGroup = paysNode.getAttributes().getNamedItem("group").getNodeValue();
                        paysCode = paysNode.getFirstChild().getNodeValue();
                        if (paysNode.getAttributes().getNamedItem("start") != null) {
                            paysStart = paysNode.getAttributes().getNamedItem("start").getNodeValue();
                        }
                        // si pas de date d�fini dans le fichier, on consid�re
                        // le pays d�j� membre
                        if (paysStart.equals("")) {
                            paysStart = "01.01.1900";// date dans le pass�
                        }

                        // si pays startDate est ant�rieur � la date du jour ou
                        // �gal, on prend en compte le pays
                        // comme pays de l'europe

                        if (JadeDateUtil.isDateAfter(JadeDateUtil.getGlobazFormattedDate(new Date()), paysStart)
                                || JadeDateUtil.areDatesEquals(JadeDateUtil.getGlobazFormattedDate(new Date()),
                                        paysStart)) {
                            if (paysGroup.equals("UE")) {
                                countriesUE.put(paysCode, paysName);
                            }
                            if (paysGroup.equals("AELE")) {
                                countriesAELE.put(paysCode, paysName);
                            }
                        }

                    }
                }
            }

        }
        PrestationExportableController.europeCountries.clear();
        PrestationExportableController.europeCountries.put("UE", countriesUE);
        PrestationExportableController.europeCountries.put("AELE", countriesAELE);

    }

    /**
     * Charge les pays d�finis comme membres de l'europe depuis la base de donn�es
     * 
     */
    public static void loadEuropeCountriesDB() {
        Map countriesUE = new HashMap();
        Map countriesAELE = new HashMap();
        // variables pour la gestion de la date d'entr�e dans l'europe
        JADate nowDate = null;

        // PaysComplexModel paysComplexModel = new PaysComplexModel();
        // PaysSearchComplexModel paysSearchComplexModel = new
        // PaysSearchComplexModel();
        // paysSearchComplexModel = TIBusinessServiceLocator.getAdresseService()
        // .findPays(paysSearchComplexModel);
        //
        // for (int i = 0; i < paysSearchComplexModel.getSize(); i++) {
        // try {
        // paysComplexModel = (PaysComplexModel) paysSearchComplexModel
        // .getSearchResults()[i];
        // // si il y a une date d�finie (<>0), on regarde si elle est dans
        // // le pass�
        // // si non => pays pas encore membre
        //
        // // on passe le code pays comme cl� et comme valeur
        // // car on n'a pas le nom entier du pays dans la
        // // table(pas utile)
        // // et on garde la m�me structure que pour la version XML
        // if (paysComplexModel.getPaysMembreModel().getGroupe().equals(
        // "UE")) {
        // countriesUE.put(paysComplexModel.getPaysMembreModel()
        // .getCodeIso(), paysComplexModel
        // .getPaysMembreModel().getCodeIso());
        // }
        //
        // if (paysComplexModel.getPaysMembreModel().getGroupe().equals(
        // "AELE")) {
        // countriesAELE.put(paysComplexModel.getPaysMembreModel()
        // .getCodeIso(), paysComplexModel
        // .getPaysMembreModel().getCodeIso());
        // }
        //
        // if (!paysComplexModel.getPaysMembreModel().getDateAdhesion()
        // .equals("00000000")) {
        //
        // } else {
        //
        // }
        // } catch (Exception e) {
        // System.out
        // .println("af.prestation.export.control.PrestationExportableController - loadEuropeCountriesDB():"
        // + e.getMessage());
        // throw e;
        // }
        //
        // }
        // europeCountries.clear();
        // europeCountries.put("UE", countriesUE);
        // europeCountries.put("AELE", countriesAELE);
    }

    /**
     * Charge les r�gles d'exportation d�finies dans le fichier de configuration
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void loadRules() throws JadeApplicationException {

        NodeList nodeList = null;
        ExportRule rule = null;

        nodeList = PrestationExportableController.xmlExportRules.getElementsByTagName("enfant");

        for (int i = 0; i < nodeList.getLength(); i++) {
            rule = new ExportRule();
            // pour chaque noeud enfant, on extrait le pays d'origine d�fini de
            // l'alloc correspondant

            if (nodeList.item(i).getParentNode().hasAttributes()) {
                String attrName = nodeList.item(i).getParentNode().getAttributes().item(0).getNodeName();
                // TODO (lot 2) prendre en compte tous les attributs car si on
                // en ajoute, plus forc�ment "from" en 0
                if (attrName.equals("from")) {
                    String nationalites = nodeList.item(i).getParentNode().getAttributes().item(0).getNodeValue();
                    String[] nationalitesTab = nationalites.split(",");
                    for (int j = 0; j < nationalitesTab.length; j++) {

                        String codePays = nationalitesTab[j];
                        if (nationalitesTab[j].indexOf("(") > 0) {
                            codePays = nationalitesTab[j].substring(0, nationalitesTab[j].indexOf("("));
                        }

                        rule.addNationalite(codePays);

                    }
                }
            }
            // on extrait les pays d�finis pour l'enfant
            if (nodeList.item(i).hasAttributes()) {
                for (int j = 0; j < nodeList.item(i).getAttributes().getLength(); j++) {
                    String attrName = nodeList.item(i).getAttributes().item(j).getNodeName();
                    // attribut peut �tre soit pays
                    if (attrName.equals("pays")) {
                        String residences = nodeList.item(i).getAttributes().item(j).getNodeValue();
                        String[] residencesTab = residences.split(",");
                        for (int k = 0; k < residencesTab.length; k++) {
                            String codePays = residencesTab[k];
                            if (residencesTab[k].indexOf("(") > 0) {
                                codePays = residencesTab[k].substring(0, residencesTab[k].indexOf("("));
                            }

                            rule.addResidence(codePays);

                        }
                    }
                    // soit numero
                    if (attrName.equals("numero")) {
                        rule.setNum(Integer.parseInt(nodeList.item(i).getAttributes().item(j).getNodeValue()));
                    }
                }

            }

            // on extrait les lois appliqu�es ou non en fonction
            NodeList lois = nodeList.item(i).getChildNodes();
            for (int j = 0; j < lois.getLength(); j++) {

                if (lois.item(j).getNodeName().equals("lafam16")) {
                    if (lois.item(j).getFirstChild().getNodeValue().equals("1")) {
                        rule.setStatus(true, ALConstLoisExport.LOI_LAFAM_FIN_ENFANT);
                    } else {
                        rule.setStatus(false, ALConstLoisExport.LOI_LAFAM_FIN_ENFANT);
                    }
                }
                if (lois.item(j).getNodeName().equals("lafam1625")) {
                    if (lois.item(j).getFirstChild().getNodeValue().equals("1")) {
                        rule.setStatus(true, ALConstLoisExport.LOI_LAFAM_FORMATION_25);
                    } else {
                        rule.setStatus(false, ALConstLoisExport.LOI_LAFAM_FORMATION_25);
                    }
                }
                if (lois.item(j).getNodeName().equals("lfa16")) {
                    if (lois.item(j).getFirstChild().getNodeValue().equals("1")) {
                        rule.setStatus(true, ALConstLoisExport.LOI_LFA_FIN_ENFANT);
                    } else {
                        rule.setStatus(false, ALConstLoisExport.LOI_LFA_FIN_ENFANT);
                    }
                }
                if (lois.item(j).getNodeName().equals("lfa1625")) {
                    if (lois.item(j).getFirstChild().getNodeValue().equals("1")) {
                        rule.setStatus(true, ALConstLoisExport.LOI_LFA_FORMATION_25);
                    } else {
                        rule.setStatus(false, ALConstLoisExport.LOI_LFA_FORMATION_25);
                    }
                }
                if (lois.item(j).getNodeName().equals("lfaMenage")) {
                    if (lois.item(j).getFirstChild().getNodeValue().equals("1")) {
                        rule.setStatus(true, ALConstLoisExport.LOI_LFA_MENAGE);
                    } else {
                        rule.setStatus(false, ALConstLoisExport.LOI_LFA_MENAGE);
                    }
                }

            }
            PrestationExportableController.rules.put(new Integer(i), rule);
        }
    }

    /**
     * Choisit la r�gle la plus ad�quate en fonction de l'origine de l'allocataire et la r�sidence de son enfant
     * 
     * @param allocPays
     *            - pays d'origine de l'allocataire (code pays)
     * @param enfantPays
     *            - pays de r�sidence de l'enfant (code pays)
     * @param enfantEurope
     *            - indique si l'enfant vit dans l'europe ou pas (si enfantPays non d�fini(MEN,...))
     * @return la r�gle s�lectionn�e
     */
    private static ExportRule selectRule(String allocPays, String enfantPays, boolean enfantEurope) {
        ExportRule currentRule = null;
        List<ExportRule> tmpPossibleRules = new ArrayList<ExportRule>();
        List<ExportRule> tmpPossibleRules2 = new ArrayList<ExportRule>();

        for (int i = 0; i < PrestationExportableController.rules.size(); i++) {
            currentRule = PrestationExportableController.rules.get(new Integer(i));

            if (currentRule.containsNationalite(allocPays)) {
                tmpPossibleRules.add(currentRule);
            }
        }
        // si on a pas trouv� d'alloc avec le pays mentionn�, on cherche avec
        // l'europe si le pays est dedans
        if ((tmpPossibleRules.size() == 0) && PrestationExportableController.isInEurope(allocPays)) {
            for (int i = 0; i < PrestationExportableController.rules.size(); i++) {
                currentRule = PrestationExportableController.rules.get(new Integer(i));
                if (currentRule.containsNationalite("EUROPE")) {
                    tmpPossibleRules.add(currentRule);
                }
            }
        }
        // si on a pas trouv� d'alloc avec l'europe, on prend ceux dont l'alloc
        // from est vide, il correspond a tous
        if (tmpPossibleRules.size() == 0) {
            for (int i = 0; i < PrestationExportableController.rules.size(); i++) {
                currentRule = PrestationExportableController.rules.get(new Integer(i));
                if (currentRule.isNationaliteEmpty()) {
                    tmpPossibleRules.add(currentRule);
                }
            }
        }

        // -------------------------------------------
        // dans les r�gles possibles d'allocataire, on regarde les pays enfant
        // qui correspondent
        for (int i = 0; i < tmpPossibleRules.size(); i++) {
            currentRule = tmpPossibleRules.get(i);
            if (currentRule.containsResidence(enfantPays)) {
                tmpPossibleRules2.add(currentRule);
            }
        }
        // si on a pas trouv� d'alloc avec le pays mentionn�, on cherche avec
        // l'europe si le pays est dedans
        if ((tmpPossibleRules2.size() == 0) && (PrestationExportableController.isInEurope(enfantPays) || enfantEurope)) {
            for (int i = 0; i < tmpPossibleRules.size(); i++) {
                currentRule = tmpPossibleRules.get(i);
                if (currentRule.containsResidence("EUROPE")) {
                    tmpPossibleRules2.add(currentRule);
                }
            }
        }
        // si on a pas trouv� d'enfant avec l'europe, on prend ceux dont enfant
        // pays est vide, il correspond a tous
        if (tmpPossibleRules2.size() == 0) {
            for (int i = 0; i < tmpPossibleRules.size(); i++) {
                if (currentRule.isResidenceEmpty()) {
                    tmpPossibleRules2.add(currentRule);
                }
            }
        }

        return tmpPossibleRules2.get(0);
    }

    /**
     * Contr�le si la prestation est exportable ou non selon les param�tres
     * 
     * @param _dossier
     *            le dossier contenant le droit
     * @param _droit
     *            le droit � contr�ler
     * @param dateCtrl
     *            la date pour laquelle contr�ler le droit
     * @return <code>true</code> si la prestation est exportable, <code>false</code> sinon
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean control(DossierComplexModel _dossier, DroitComplexModel _droit, String dateCtrl)
            throws JadeApplicationException {

        if (_dossier == null) {
            throw new ALCtrlExportException("PrestationExportableController#control : _dossier is null");
        }

        if (_droit == null) {
            throw new ALCtrlExportException("PrestationExportableController#control : _droit is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateCtrl)) {
            throw new ALCtrlExportException("PrestationExportableController#control : " + dateCtrl
                    + " is not a valid date");
        }
        // avant lafam, pas de ctrl d'exportation, donc tjrs exportable
        if (!ALImplServiceLocator.getCalculService().isDateLAFam(dateCtrl)) {
            return true;
        }

        String allocPays = "";
        String enfantPays = "";
        String typeAlloc = "";

        // on d�tecte en premier lieu si on a tout ce qu'il nous faut
        try {
            allocPays = _dossier.getAllocataireComplexModel().getPaysModel().getCodeIso();

            String idEnfantPays = _droit.getEnfantComplexModel().getEnfantModel().getIdPaysResidence();

            enfantPays = findCodeISOEnfant(idEnfantPays);

            typeAlloc = _dossier.getDossierModel().getActiviteAllocataire();

        } catch (JadePersistenceException e) {
            if (JadeStringUtil.isEmpty(allocPays)) {
                throw new ALCtrlExportException(
                        "PrestationExportableController#control : Impossible de r�cup�rer la nationalit� de l'allocataire",
                        e);
            } else if (JadeStringUtil.isEmpty(enfantPays)) {
                throw new ALCtrlExportException(
                        "PrestationExportableController#control : Impossible de r�cup�rer le pays de r�sidence de l'enfant",
                        e);
            } else {
                throw new ALCtrlExportException(
                        "PrestationExportableController#control : Impossible de r�cup�rer le type d'allocataire", e);
            }

        }

        // si l'allocataire est non actif, pas la peine de charger les r�gles,
        // le droit n'est pas exportable
        if (ALCSDossier.ACTIVITE_NONACTIF.equals(typeAlloc) && !"CH".equals(enfantPays)) {
            return false;
        }

        // on calcule l'�ge pour d�termine quel loi d'export on applique
        int age = 0;
        // crit�re �ge si le droit n'est pas de type m�nage
        if (!ALCSDroit.TYPE_MEN.equals(_droit.getDroitModel().getTypeDroit())) {
            age = getDateBetween(_droit, dateCtrl);
        }
        int loi = 0;
        try {
            if (age < ALFomationUtils.getAgeFormation(_droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                    .getDateNaissance())) {
                if (isAgricole(_dossier)) {
                    loi = ALConstLoisExport.LOI_LFA_FIN_ENFANT;
                } else {
                    loi = ALConstLoisExport.LOI_LAFAM_FIN_ENFANT;
                }
            } else if (age < 25) {
                if (isAgricole(_dossier)) {
                    loi = ALConstLoisExport.LOI_LFA_FORMATION_25;
                } else {
                    loi = ALConstLoisExport.LOI_LAFAM_FORMATION_25;
                }
            }
            // sinon c'est qu'il a plus de 25 ans et pas besoin de ctrl
            else {
                return true;
            }

            // Pour les allocations MEN, elles sont exportables si le pays
            // r�sidence
            // allocataire est en CH, sinon v�rifier la r�gle pour les dossiers
            // agricoles
            if (ALCSDroit.TYPE_MEN.equals(_droit.getDroitModel().getTypeDroit())) {
                if ("CH".equals(_dossier.getAllocataireComplexModel().getPaysResidenceModel().getCodeIso())) {
                    return true;
                } else {

                    if (isAgricole(_dossier)) {
                        loi = ALConstLoisExport.LOI_LFA_MENAGE;

                    } else {
                        return false;
                    }

                }
            }

        } catch (JadeApplicationException | JadePersistenceException e) {
            throw new ALCtrlExportException("PrestationExportableController#control:unable to determine law to check",
                    e);
        }

        // dans le cas d'un menage, on a pas l'enfant pays...il faut donc le
        // m�moriser
        if (loi == ALConstLoisExport.LOI_LFA_MENAGE) {
            PrestationExportableController.usedRule = PrestationExportableController.selectRule(allocPays, enfantPays,
                    true);
        } else {
            PrestationExportableController.usedRule = PrestationExportableController.selectRule(allocPays, enfantPays,
                    false);
        }
        return PrestationExportableController.usedRule.getStatus(loi);
    }

    protected String findCodeISOEnfant(String idEnfantPays) throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        String enfantPays;
        PaysSearchSimpleModel searchPaysEnfant = new PaysSearchSimpleModel();
        searchPaysEnfant.setForIdPays(idEnfantPays);
        searchPaysEnfant = TIBusinessServiceLocator.getAdresseService().findPays(searchPaysEnfant);

        enfantPays = ((PaysSimpleModel) searchPaysEnfant.getSearchResults()[0]).getCodeIso();
        return enfantPays;
    }

    /***
     * Calcul l'�ge de la personne li�e au {@link DroitComplexModel} associ�
     * 
     * @param droit
     * @param dateControle
     *            sous regime de la loi sur l'exportabilit� des solutions
     * @return int repr�sentant l'�ge de la personne
     */
    protected int getDateBetween(DroitComplexModel droit, String dateControle) {
        return JadeDateUtil.getNbYearsBetween(droit.getEnfantComplexModel().getPersonneEtendueComplexModel()
                .getPersonne().getDateNaissance(), dateControle, JadeDateUtil.FULL_DATE_COMPARISON);
    }

    protected boolean isAgricole(DossierComplexModel dossier) throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException {
        return ALServiceLocator.getDossierBusinessService().isAgricole(
                dossier.getDossierModel().getActiviteAllocataire());
    }

    /**
     * Retourne si au moins un des enfants du dossier vit en europe
     * 
     * @param _dossier
     *            le dossier v�rifi�
     * @return true/false
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private boolean isEnfantResidenceEurope(DossierComplexModel _dossier) throws JadeApplicationException,
            JadePersistenceException {
        boolean result = false;
        DroitComplexSearchModel searchDroitsDossier = new DroitComplexSearchModel();
        searchDroitsDossier.setForIdDossier(_dossier.getId());
        searchDroitsDossier = ALServiceLocator.getDroitComplexModelService().search(searchDroitsDossier);

        for (int i = 0; i < searchDroitsDossier.getSize(); i++) {

            result = PrestationExportableController.isInEurope(((DroitComplexModel) searchDroitsDossier
                    .getSearchResults()[i]).getEnfantComplexModel().getPaysModel().getCodeIso());
            if (result) {
                return true;
            }
        }

        return result;
    }
}
