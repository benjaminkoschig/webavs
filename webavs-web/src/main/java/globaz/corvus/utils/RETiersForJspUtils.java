package globaz.corvus.utils;

import globaz.corvus.application.REApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * <p>
 * Permet de formatter les valeurs usuelles concernant les tiers dans les JSP.<br/>
 * Créer afin d'éviter un copier coller inutile de ces méthodes dans la plupart des viewBean ou listViewBean.
 * </p>
 * <p>
 * Les méthodes ne sont pas statiques, mais accessible via une instance ({@link #getInstance(BSession)}), de manière à
 * pouvoir les tester efficacement avec un test unitaire (voir RETiersForJspUtilsTest dans le projet __TestUnit).
 * </p>
 * 
 * @author PBA
 */
public class RETiersForJspUtils {

    private static Map<String, Map<String, String>> pays = null;

    /**
     * Construit et retourne une instance de l'utilitaire avec la session passée en paramètre
     * 
     * @param session
     * @return
     */
    public static RETiersForJspUtils getInstance(BSession session) {
        return new RETiersForJspUtils(session);
    }

    private BSession session;

    private RETiersForJspUtils(BSession session) {
        this.session = session;
    }

    public String getDetailsTiers(PersonneAVS tiers, boolean surDeuxLignes, boolean avecBalisesHtml) {

        if (tiers == null) {
            if (REApplication.DEFAULT_APPLICATION_CORVUS.equals(getSession().getApplicationId())) {
                return getSession().getLabel("ERREUR_AUCUN_TIERS_DEFINI");
            } else {
                return "";
            }
        }

        return getDetailsTiers(tiers.getNss().toString(), tiers.getNom(), tiers.getPrenom(), tiers.getDateNaissance(),
                tiers.getDateDeces(), tiers.getSexe().getCodeSysteme().toString(), tiers.getPays().getId().toString(),
                surDeuxLignes, avecBalisesHtml);
    }

    /**
     * Format les détails du tiers sur une seule ligne de la manière suivante :
     * <ul>
     * <li>
     * <code>756.****.****.** ( Date décès ) / Nom Prénom / Date de naissance / F ou H / Pays</code></li>
     * </ul>
     * 
     * @param tiers
     * @return
     */
    public String getDetailsTiers(PRTiersWrapper tiers) {
        return this.getDetailsTiers(tiers, false, true);
    }

    public String getDetailsTiers(PRTiersWrapper tiers, boolean surDeuxLignes) {
        return this.getDetailsTiers(tiers, surDeuxLignes, true);
    }

    /**
     * Format les détails du tiers sur une ou deux lignes (selon le paramètre surDeuxLignes).<br/>
     * Soit les format suivants :
     * <ul>
     * <li>Une ligne : <br/>
     * <code>756.****.****.** ( Date décès )/ Nom Prénom / Date de naissance / F ou H / Pays</code></li>
     * </li>
     * <li>Deux lignes :<br/>
     * <code>756.****.****.** ( Date décès )<br/> Nom Prénom / Date de naissance / F ou H / Pays</code></li></li>
     * </ul>
     * 
     * @param tiers
     * @param surDeuxLignes
     * @return
     */
    public String getDetailsTiers(PRTiersWrapper tiers, boolean surDeuxLignes, boolean avecBaliseHTML) {
        if (tiers == null) {
            if (REApplication.DEFAULT_APPLICATION_CORVUS.equals(getSession().getApplicationId())) {
                return getSession().getLabel("ERREUR_AUCUN_TIERS_DEFINI");
            } else {
                return "";
            }
        }

        String idPays = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS);
        if (JadeStringUtil.isBlank(idPays)) {
            idPays = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE);
        }

        return getDetailsTiers(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                tiers.getProperty(PRTiersWrapper.PROPERTY_NOM), tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES), tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE),
                idPays, surDeuxLignes, avecBaliseHTML);
    }

    public String getDetailsTiers(String nss, String nom, String prenom, String dateNaissance, String dateDeces,
            String csSexe, String idPays, boolean surDeuxLignes, boolean avecBaliseHTML) {

        StringBuilder details = new StringBuilder();

        if (avecBaliseHTML) {
            details.append("<b>");
        }
        details.append(nss);
        if (avecBaliseHTML) {
            details.append("</b>");
        }

        if (!JadeStringUtil.isBlank(dateDeces)) {
            details.append("( ");
            if (avecBaliseHTML) {
                details.append("<span style=font-family:wingdings>U</span> ");
            } else {
                details.append("† "); // caractères CP152 pour une croix
            }
            details.append(dateDeces).append(" )");
        }
        if (surDeuxLignes) {
            if (avecBaliseHTML) {
                details.append("<br/>");
            } else {
                details.append("\n");
            }
        } else {
            details.append(" / ");
        }
        details.append(nom).append(" ").append(prenom);
        details.append(" / ");
        details.append(dateNaissance);
        details.append(" / ");
        details.append(getLibelleCourtSexe(csSexe));

        if (!JadeStringUtil.isBlank(idPays)) {
            details.append(" / ");
            details.append(getLibellePays(idPays));
        }

        return details.toString();
    }

    public String getLibelleCourtSexe(String csSexe) {
        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    public String getLibellePays(String codeCentraleDuPays) {
        if (RETiersForJspUtils.pays == null) {
            RETiersForJspUtils.pays = new HashMap<String, Map<String, String>>();

            try {
                TIPaysManager paysManager = new TIPaysManager();
                paysManager.setSession(session);
                paysManager.find(BManager.SIZE_NOLIMIT);

                for (int i = 0; i < paysManager.size(); i++) {
                    TIPays unPays = (TIPays) paysManager.get(i);
                    Map<String, String> libelleParCodeISO = new HashMap<String, String>();
                    libelleParCodeISO.put("FR", unPays.getLibelleFr());
                    libelleParCodeISO.put("DE", unPays.getLibelleDe());
                    libelleParCodeISO.put("IT", unPays.getLibelleIt());
                    RETiersForJspUtils.pays.put(unPays.getCodeCentrale(), libelleParCodeISO);
                }
            } catch (Exception ex) {
                return "";
            }
        }
        Map<String, String> pays = RETiersForJspUtils.pays.get(codeCentraleDuPays);
        if (pays == null) {
            return "";
        }
        return pays.get(getSession().getIdLangueISO().toUpperCase());
    }

    public BSession getSession() {
        return session;
    }
}
