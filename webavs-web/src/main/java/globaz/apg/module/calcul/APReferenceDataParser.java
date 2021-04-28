/*
 * Cr�� le 25 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul;

import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.rev2005.APReferenceDataAPG;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import globaz.prestation.api.IPRDemande;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 * 
 *         <p>
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 *         </p>
 */
public class APReferenceDataParser {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String ATTRIBUT_CALCULATEUR_CLASS_NAME = "calculateur";
    private static final String ATTRIBUT_FROM = "from";
    private static final String ATTRIBUT_NAME = "name";
    private static final String ATTRIBUT_REFERENCE_DATA_CLASS_NAME = "reference-data";
    private static final String ATTRIBUT_REVISION = "revision";
    private static final String ATTRIBUT_TO = "to";
    private static final String ATTRIBUT_VALUE = "value";
    private static final String ATTRIBUTE_PRESTATION_TYPE = "type";
    private static final APReferenceDataParser INSTANCE = new APReferenceDataParser();
    private static final String TAG_ATTRIBUT = "attribut";
    private static final String TAG_CLASS = "class";

    private static final String TAG_PRESTATION = "prestation";
    public static final String CALCUL_APGREFERENCE_DATA_XML = "calculAPGReferenceData.xml";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prestationType
     *            DOCUMENT ME!
     * @param startDate
     *            DOCUMENT ME!
     * @param endDate
     *            DOCUMENT ME!
     * @param dateRevision
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final IAPReferenceDataPrestation loadReferenceData(BSession session, String prestationType,
            JADate startDate, JADate endDate, JADate dateRevision) throws Exception {
        // r�vision
        List refData = INSTANCE.loadReferencesData(session, CALCUL_APGREFERENCE_DATA_XML, prestationType, startDate,
                endDate, endDate);

        if (refData.isEmpty()) {
            throw new Exception("impossible de charger le fichier de reference pour le calcul");
        }

        return (IAPReferenceDataPrestation) refData.get(0);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe APReferenceDataParser.
     */
    public APReferenceDataParser() {
        super();
    }

    /**
     * Set dynamiquement les champs du bean par rapport aux attributs de la map.
     * 
     * <p>
     * Les setter du bean, doivent mapper les champs du fichiers xml, stock� dans la Map attributs. Ex. Si le fichier
     * xml contient un attribut nomm� : KA0min, la Map 'attributs' aura une entr�e : KA0min + valeur Le bean devra donc
     * avoir une methode setKA0min(paramType) pour que cette valeur soit sett�e.
     * </p>
     * 
     * @param bean
     * @param attributs
     * 
     * @return
     * 
     * @throws Exception
     */
    private Object invokeSetter(Object bean, Map attributs) throws Exception {
        String methodName = null;
        String parameterName = null;
        String parameterValue = null;
        Method[] methods;
        Object[] args = new Object[1];
        Class[] paramTypes = new Class[1];

        // r�cup�re la liste des m�thodes de l'objet
        methods = bean.getClass().getMethods();

        // on parcourt toutes les m�thodes pour traiter celles qui commencent
        // par <code>set</code>
        for (int i = 0; i < methods.length; i++) {
            args[0] = null;

            // r�cup�re le nom de la m�thode
            methodName = methods[i].getName();

            // v�rifie si la m�thode commence par "set"
            if ((methodName.length() > 3) && (methodName.startsWith("set"))) {
                // on construit le nom de la propri�t�
                parameterName = methodName.substring(3);

                // on r�cup�re la valeur du param�tre dans la map
                if (attributs.containsKey(parameterName)) {
                    parameterValue = (String) attributs.get(parameterName);

                    if (JadeStringUtil.isEmpty(parameterValue)) {
                        parameterValue = null;
                    }
                } else {
                    continue;
                }

                // on r�cup�re le type du param�tre
                paramTypes = methods[i].getParameterTypes();

                // on ne prend que les m�thodes setXXX avec un seul param�tre
                if (paramTypes.length == 1) {
                    // convertit les valeurs de types primitifs en objets
                    if (paramTypes[0].getName().equals("int")) {
                        if (parameterValue != null) {
                            args[0] = Integer.valueOf(parameterValue);
                        }
                    } else if (paramTypes[0].getName().equals("long")) {
                        if (parameterValue != null) {
                            args[0] = Long.valueOf(parameterValue);
                        }
                    } else if (paramTypes[0].getName().equals("short")) {
                        if (parameterValue != null) {
                            args[0] = Short.valueOf(parameterValue);
                        }
                    } else if (paramTypes[0].getName().equals("double")) {
                        if (parameterValue != null) {
                            args[0] = Double.valueOf(parameterValue);
                        }
                    } else if (paramTypes[0].getName().equals("float")) {
                        if (parameterValue != null) {
                            args[0] = Float.valueOf(parameterValue);
                        }
                    } else if (paramTypes[0].getName().equals("byte")) {
                        if (parameterValue != null) {
                            args[0] = Byte.valueOf(parameterValue);
                        }
                    } else if (paramTypes[0].getName().equals("char")) {
                        if (parameterValue != null) {
                            args[0] = new Character(parameterValue.charAt(0));
                        }
                    } else if ((paramTypes[0].getName().equals("boolean"))
                            || (paramTypes[0].getName().equals("java.lang.Boolean"))) {
                        if ((parameterValue != null) && (parameterValue.equals("on"))) {
                            args[0] = Boolean.TRUE;
                        } else {
                            args[0] = Boolean.FALSE;
                        }
                    } else if ((paramTypes[0].getName().equals("java.lang.String"))) {
                        args[0] = parameterValue;
                    } else if (paramTypes[0].getName().equals("globaz.framework.util.FWCurrency")) {
                        if (parameterValue != null) {
                            args[0] = new FWCurrency(parameterValue);
                        }
                    } else if (paramTypes[0].getName().equals("java.math.BigDecimal")) {
                        if (parameterValue != null) {
                            args[0] = new BigDecimal(parameterValue);
                        }
                    } else {
                        args[0] = null;
                    }

                    // on appelle la m�thode pour fixer la valeur de la
                    // propri�t�
                    if (args[0] != null) {
                        methods[i].invoke(bean, args);
                    }
                }
            }
        }

        return bean;
    }

    /**
     * Charge le fichier xml servant de r�f�rence pour le calcul des prestations APG
     * 
     * @param session
     * @param fileName
     *            Le nom du fichier � charger
     * @param prestationType
     *            Le type de la prestation
     * @param startDate
     *            date de d�but pour le calcul de la prestation
     * @param endDate
     *            date de fin pour le calcul de la prestation
     * @param dateRevision
     *            DOCUMENT ME!
     * 
     * @return Une liste de IAPReferenceDataPrestation, donn�e de r�f�rence pour le calcul de la (des) prestation(s). La
     *         valeur retourn�e n'est jamais null, mais la liste peut �tre vide, si aucune prestation trouv�e
     * 
     * @throws Exception
     *             En cas d'erreur
     */

    public List loadReferencesData(BSession session, String fileName, String prestationType, JADate startDate,
            JADate endDate, JADate dateRevision) throws Exception {
        InputStream inp = APReferenceDataParser.class.getResourceAsStream("/" + fileName);
        InputSource is = new InputSource(inp);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(is);

        List result = new ArrayList();

        if (document == null) {
            throw (new Exception("Impossible de lire le fichier : " + fileName));
        }

        NodeList nodes = document.getElementsByTagName(TAG_PRESTATION);

        IAPReferenceDataPrestation instance = null;
        String from = null;
        String to = null;
        String className = null;
        String calculateurClassName = null;
        String noRevision = null;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String type = node.getAttributes().getNamedItem(ATTRIBUTE_PRESTATION_TYPE).getNodeValue();

            if ((prestationType != null) && (prestationType.equals(type))) {
                // r�cup�ration des attribut
                if ((node.getAttributes() != null) && (node.getAttributes().getNamedItem(ATTRIBUT_FROM) != null)) {
                    from = node.getAttributes().getNamedItem(ATTRIBUT_FROM).getNodeValue();
                }

                if ((node.getAttributes() != null) && (node.getAttributes().getNamedItem(ATTRIBUT_TO) != null)) {
                    to = node.getAttributes().getNamedItem(ATTRIBUT_TO).getNodeValue();
                }

                if ((node.getAttributes() != null) && (node.getAttributes().getNamedItem(ATTRIBUT_REVISION) != null)) {
                    noRevision = node.getAttributes().getNamedItem(ATTRIBUT_REVISION).getNodeValue();
                }

                // Pour le choix de la r�vision � prendre en compte, on se base
                // sur la date de r�vision
                if (BSessionUtil.compareDateFirstGreaterOrEqual(session, JACalendar.format(dateRevision), from)
                        && BSessionUtil.compareDateFirstLowerOrEqual(session, JACalendar.format(dateRevision), to)) {
                }
                // On passe � la prestation suivante
                else {
                    continue;
                }

                // classes � instancier...
                NodeList childs = node.getChildNodes();
                Map attrMap = new HashMap();

                for (int c = 0; c < childs.getLength(); c++) {
                    if (childs.item(c).getNodeName().equals(TAG_CLASS)) {
                        className = childs.item(c).getAttributes().getNamedItem(ATTRIBUT_REFERENCE_DATA_CLASS_NAME)
                                .getNodeValue();
                        calculateurClassName = childs.item(c).getAttributes()
                                .getNamedItem(ATTRIBUT_CALCULATEUR_CLASS_NAME).getNodeValue();
                    } else if (childs.item(c).getNodeName().equals(TAG_ATTRIBUT)) {
                        attrMap.put(childs.item(c).getAttributes().getNamedItem(ATTRIBUT_NAME).getNodeValue(), childs
                                .item(c).getAttributes().getNamedItem(ATTRIBUT_VALUE).getNodeValue());
                    }
                }

                Class c = Class.forName(className);
                instance = (IAPReferenceDataPrestation) c.newInstance();

                invokeSetter(instance, attrMap);
                instance.setDateDebut(startDate);
                instance.setDateFin(endDate);
                instance.setCalculateurClassName(calculateurClassName);
                instance.setNoRevision(noRevision);
                result.add(instance);
            }
        }

        return result;
    }

    /**
     * IAPReferenceDataPrestation Donn�e de r�f�rence pour calculer la prestation
     * 
     * @param session
     * @param fileName
     *            Le nom du fichier � charger
     * @param prestationType
     *            Le type de la prestation
     * @param revision
     *            No de r�vision � prendre en compte pour le calcul de la prestation (force la r�vision)
     * 
     * @return IAPReferenceDataPrestation Donn�e de r�f�rence pour calculer la prestation
     * 
     * @throws Exception
     *             en cas d'erreur, ou si pas de r�vision trouv�e
     * @throws APCalculException
     *             DOCUMENT ME!
     */
    public IAPReferenceDataPrestation loadReferencesData(BSession session, String fileName, String prestationType,
            String revision) throws Exception {
        if ((revision == null) || revision.equals(IAPDroitAPG.CS_REVISION_STANDARD)) {
            throw new APCalculException("Num�ro de r�vision non valide !!! " + revision);
        }

        InputStream inp = APReferenceDataParser.class.getResourceAsStream("/" + fileName);
        InputSource is = new InputSource(inp);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(is);

        if (document == null) {
            throw new APCalculException("Impossible de lire le fichier : " + fileName);
        }

        String from = null;
        String to = null;

        NodeList nodes = document.getElementsByTagName(TAG_PRESTATION);

        IAPReferenceDataPrestation instance = null;
        String className = null;
        String calculateurClassName = null;
        String noRevision = null;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String type = node.getAttributes().getNamedItem(ATTRIBUTE_PRESTATION_TYPE).getNodeValue();

            if ((prestationType != null) && (prestationType.equals(type))) {
                if ((node.getAttributes() != null) && (node.getAttributes().getNamedItem(ATTRIBUT_REVISION) != null)) {
                    noRevision = node.getAttributes().getNamedItem(ATTRIBUT_REVISION).getNodeValue();
                }

                // Pas la bonne r�vision, passe � la suivante
                if (!revision.equals(noRevision)) {
                    continue;
                }

                // r�cup�ration des attribut
                if ((node.getAttributes() != null) && (node.getAttributes().getNamedItem(ATTRIBUT_FROM) != null)) {
                    from = node.getAttributes().getNamedItem(ATTRIBUT_FROM).getNodeValue();
                }

                if ((node.getAttributes() != null) && (node.getAttributes().getNamedItem(ATTRIBUT_TO) != null)) {
                    to = node.getAttributes().getNamedItem(ATTRIBUT_TO).getNodeValue();
                }

                // classes � instancier...
                NodeList childs = node.getChildNodes();
                Map attrMap = new HashMap();

                for (int c = 0; c < childs.getLength(); c++) {
                    if (childs.item(c).getNodeName().equals(TAG_CLASS)) {
                        className = childs.item(c).getAttributes().getNamedItem(ATTRIBUT_REFERENCE_DATA_CLASS_NAME)
                                .getNodeValue();
                        calculateurClassName = childs.item(c).getAttributes()
                                .getNamedItem(ATTRIBUT_CALCULATEUR_CLASS_NAME).getNodeValue();
                    } else if (childs.item(c).getNodeName().equals(TAG_ATTRIBUT)) {
                        attrMap.put(childs.item(c).getAttributes().getNamedItem(ATTRIBUT_NAME).getNodeValue(), childs
                                .item(c).getAttributes().getNamedItem(ATTRIBUT_VALUE).getNodeValue());
                    }
                }

                Class c = Class.forName(className);
                instance = (IAPReferenceDataPrestation) c.newInstance();

                invokeSetter(instance, attrMap);
                instance.setNoRevision(noRevision);
                instance.setDateDebut(new JADate(from));
                instance.setDateFin(new JADate(to));
                instance.setCalculateurClassName(calculateurClassName);

                return instance;
            }
        }

        return null;
    }
}
