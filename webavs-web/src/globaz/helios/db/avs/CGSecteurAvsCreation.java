package globaz.helios.db.avs;

import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CGSecteurAvsCreation extends CGSecteurAVS implements globaz.framework.bean.FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FILENAME = "secteurs.xml";;
    private java.util.Vector comptes = new Vector();
    private java.lang.String secteur = null;

    /**
     * Commentaire relatif au constructeur CGSecteurAvsInfoCreation.
     */
    public CGSecteurAvsCreation() {
        super();
    }

    public void addComptesAdmin() {
        String cpt = getIdSecteurAVS() + "." + "9110.0000";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
    }

    public void addComptesBilan() {
        String cpt = "1990.120" + getIdSecteurAVS().substring(0, 1) + "." + getIdSecteurAVS().substring(1, 4) + "0";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
        cpt = "1990.220" + getIdSecteurAVS().substring(0, 1) + "." + getIdSecteurAVS().substring(1, 4) + "0";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }

        cpt = getIdSecteurAVS() + "." + "1201.0000";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
        cpt = getIdSecteurAVS() + "." + "2201.0000";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
        cpt = getIdSecteurAVS() + "." + "9200.0000";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
        cpt = getIdSecteurAVS() + "." + "9210.0000";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
    }

    public void addComptesExploitation() {
        String cpt = getIdSecteurAVS() + "." + "9000.0000";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
    }

    public void addComptesInvestissement() {
        String cpt = getIdSecteurAVS() + "." + "7900.0000";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
        cpt = getIdSecteurAVS() + "." + "8900.0000";
        if (!comptes.contains(cpt)) {
            comptes.add(cpt);
        }
    }

    public void addComptesTypeTache() {

        if (CS_AUTRE_TACHE_AGENCE.equals(getIdTypeTache())) {
            String cpt = getIdSecteurAVS() + "." + "2130.0000";
            if (!comptes.contains(cpt)) {
                comptes.add(cpt);
            }
        } else if (CS_AUTRE_TACHE_PROPRE.equals(getIdTypeTache())) {
            String cpt = getIdSecteurAVS() + "." + "2902.0000";
            if (!comptes.contains(cpt)) {
                comptes.add(cpt);
            }

        } else if (CS_CAF_AGENCE.equals(getIdTypeTache())) {
            String cpt = getIdSecteurAVS() + "." + "2130.0000";
            if (!comptes.contains(cpt)) {
                comptes.add(cpt);
            }

        } else if (CS_CAF_PROPRE.equals(getIdTypeTache())) {
            String cpt = getIdSecteurAVS() + "." + "2902.0000";
            if (!comptes.contains(cpt)) {
                comptes.add(cpt);
            }

        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 15:51:06)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void creeComptes(globaz.globall.db.BTransaction transaction, CGExerciceComptable exerciceComptable,
            String idClassificationCompte, String idClassificationSecteur) throws java.lang.Exception {

        if (comptes == null) {
            return;
        }
        Enumeration enumerate = comptes.elements();
        // creer les comptes
        while (enumerate.hasMoreElements()) {
            String compte = (String) enumerate.nextElement();
            CGPlanComptableViewBean plan = new CGPlanComptableViewBean();
            plan.setSession(getSession());
            plan.setIdExterne(compte);
            plan.setIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
            plan.setAlternateKey(CGPlanComptableViewBean.AK_EXTERNE);
            plan.retrieve(transaction);

            if (plan.isNew()) {
                plan.setAReouvrir(new Boolean(true));
                plan.setEstCompteAvs(true);

                // Tous les comptes du plan comptable AVS sont en monnaie
                // suisse.
                plan.setCodeISOMonnaie(CGCompte.CODE_ISO_CHF);
                plan.add(transaction);
            }
        }
    }

    /**
     * Method getIdTypeTache. Retourne l'id du type de tache correspondant au nom définit dans le fichier secteurs.xml
     * tache gérée : TACHE_FEDERALE; AUTRE_TACHE_PROPRE;
     * 
     * @param tache
     * @return String l'id du type de la tache. 0 si tâche non définie
     */
    protected String getIdTypeTache(String tache) {
        if ("TACHE_FEDERALE".equals(tache)) {
            return CS_TACHE_FEDERAL;
        } else if ("AUTRE_TACHE_PROPRE".equals(tache)) {
            return CS_AUTRE_TACHE_PROPRE;
        } else {
            return "0";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 15:33:01)
     * 
     * @return boolean
     * @param node
     *            org.w3c.dom.Node
     * @param name
     *            java.lang.String
     */
    private boolean hasAttribute(Node node, String name) {
        return ((node.getAttributes() != null) && (node.getAttributes().getNamedItem(name) != null)

        );
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 15:33:01)
     * 
     * @return boolean
     * @param node
     *            org.w3c.dom.Node
     * @param name
     *            java.lang.String
     */
    private boolean isAttributeTrue(Node node, String name) {
        return ((hasAttribute(node, name)) && (node.getAttributes().getNamedItem(name).getNodeValue()
                .equalsIgnoreCase("TRUE")));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 14:00:21)
     * 
     * @param secteur
     *            java.lang.String
     */

    public void lireSecteur(globaz.globall.db.BTransaction transaction) throws Exception {

        if (secteur == null) {
            throw (new Exception(getSession().getLabel("SECTEUR_AVS_SECTEUR_IS_NULL")));
        }

        String secteurMask = "";

        if (secteur.startsWith("900")) {
            secteurMask = "900X";
        } else if (secteur.startsWith("910")) {
            secteurMask = "910X";
        } else if (secteur.startsWith("914")) {
            secteurMask = "914X";
        } else if (secteur.startsWith("915")) {
            secteurMask = "915X";
        } else if (secteur.startsWith("999")) {
            secteurMask = "999X";
        } else {
            secteurMask = secteur;
        }

        InputStream inp = this.getClass().getResourceAsStream("/" + FILENAME);
        InputSource is = new InputSource(inp);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(is);

        if (document == null) {
            throw (new Exception(getSession().getLabel("SECTEUR_AVS_ERROR_PARSING_FILE") + FILENAME));
        }

        NodeList nodes = document.getElementsByTagName("secteur");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String secteurId = node.getAttributes().getNamedItem("id").getNodeValue();
            if ((secteurId != null) && (secteurId.equals(secteurMask))) {

                // attribut
                if (isAttributeTrue(node, "clotureManuelle")) {
                    setClotureManuelle(new Boolean(true));
                }
                if (isAttributeTrue(node, "compteBilan")) {
                    setCompteBilan(new Boolean(true));
                }
                if (isAttributeTrue(node, "compteExploitation")) {
                    setCompteExploitation(new Boolean(true));
                }
                if (isAttributeTrue(node, "compteAdministration")) {
                    setCompteAdministration(new Boolean(true));
                }
                if (isAttributeTrue(node, "compteInvestissement")) {
                    setCompteInvestissement(new Boolean(true));
                }
                if (isAttributeTrue(node, "compteResultat")) {
                    setCompteResultat(new Boolean(true));
                }

                if (hasAttribute(node, "idSecteurBilan")) {
                    setIdSecteurBilan(node.getAttributes().getNamedItem("idSecteurBilan").getNodeValue());
                }
                if (hasAttribute(node, "idSecteurResultat")) {
                    setIdSecteurResultat(node.getAttributes().getNamedItem("idSecteurResultat").getNodeValue());
                }
                if (hasAttribute(node, "typeTache")) {

                    setIdTypeTache(getIdTypeTache(node.getAttributes().getNamedItem("typeTache").getNodeValue()));
                }

                // comptes
                NodeList nlComptes = node.getChildNodes();
                for (int c = 0; c < nlComptes.getLength(); c++) {
                    if (nlComptes.item(c).getNodeName().equals("compte")) {
                        String idCompte = nlComptes.item(c).getAttributes().getNamedItem("id").getNodeValue();
                        comptes.add(idCompte);
                    }
                }
                break;
            }
        }

        CGPlanComptableAVSManager planManager = new CGPlanComptableAVSManager();
        planManager.setForType(CGPlanComptableAVS.TYPE_SECTEUR);
        planManager.setForNumeroCompteAVS(secteur.substring(0, 3));
        planManager.setSession(getSession());
        planManager.find(transaction);
        // par defaut : numero du secteur
        setLibelleDe(secteur);
        setLibelleFr(secteur);
        setLibelleIt(secteur);
        if (planManager.size() > 0) {
            CGPlanComptableAVS plan = (CGPlanComptableAVS) planManager.getEntity(0);
            setLibelleDe(plan.getLibelleDe());
            setLibelleFr(plan.getLibelleFr());
            setLibelleIt(plan.getLibelleIt());
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.09.2002 15:17:40)
     * 
     * @param newSecteur
     *            java.lang.String
     */
    public void setSecteur(java.lang.String newSecteur) {
        secteur = newSecteur;
    }
}
