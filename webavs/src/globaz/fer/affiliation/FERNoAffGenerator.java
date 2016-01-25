/*
 * Créé le 15 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.fer.affiliation;

import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.format.IFormatData;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pyxis.api.ITIRole;
import java.util.Hashtable;

/**
 * @author dgi
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class FERNoAffGenerator implements INumberGenerator {

    public static String AFFILIE_MAX = "400.000-00";
    // public static String AFFILIE_CNT = "FERAFFNORM";
    // public static String AFFILIE_LPP_CNT = "FERAFFLPP";
    public static String AFFILIE_SIEGE_MIN = "950.000-00";
    public static String PYRIS_ROLE_ATTRIBUTE = "role";
    public static String ROLE_DIV = "19120064";
    // public static String AFFILIE_DIV_CNT = "FERAFFDIV";
    public static String ROLE_LPP = "19120065";
    public static String ROLE_SIEGE = "19120074";
    public static String ROLE_TIERS = "19120066";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.affiliation.INumberGenerator#generate(globaz.naos.db.affiliation .AFAffiliation)
     */
    @Override
    public String generateBeforeAdd(AFAffiliation affiliation) throws Exception {
        if (affiliation == null || JadeStringUtil.isEmpty(affiliation.getIdTiers())) {
            return "";
        }
        if (!affiliation.isNew()) {
            return affiliation.getAffilieNumero();
        }
        String result = "";
        boolean fromRoles = false;
        // recherche du compteur en fonction du rôle
        BISession sessionTiers = GlobazSystem.getApplication("PYXIS").newSession(affiliation.getSession());
        ITIRole role = (ITIRole) sessionTiers.getAPIFor(ITIRole.class);
        Hashtable criteres = new Hashtable();
        criteres.put(ITIRole.FIND_FOR_IDTIERS, affiliation.getIdTiers());
        // effectuer la recherche en fonction de la date de début d'affiliation
        if (JadeStringUtil.isEmpty(affiliation.getDateDebut())) {
            criteres.put(ITIRole.FIND_FOR_DATE_ENTRE_DEBUT_ET_FIN, JACalendar.todayJJsMMsAAAA());
        } else {
            criteres.put(ITIRole.FIND_FOR_DATE_ENTRE_DEBUT_ET_FIN, affiliation.getDateDebut());
        }
        Object[] roles = role.find(criteres);
        if (roles != null && roles.length != 0) {
            for (int i = 0; i < roles.length && JadeStringUtil.isEmpty(result); i++) {
                String type = (String) ((GlobazValueObject) roles[i]).getProperty(PYRIS_ROLE_ATTRIBUTE);
                if (ROLE_LPP.equals(type)) {
                    // affilié PP seul, doit être saisie entièrement par
                    // l'utilisateur
                    result = affiliation.getAffilieNumero();
                    fromRoles = true;
                } else if (ROLE_SIEGE.equals(type) || ROLE_TIERS.equals(type)) {
                    // Tiers/siège
                    // recherche du prochain numéro valable
                    AFAffiliationManager mgr = new AFAffiliationManager();
                    mgr.setSession(affiliation.getSession());
                    mgr.setFromAffilieNumero(AFFILIE_SIEGE_MIN);
                    mgr.forIsTraitement(false);
                    mgr.setOrder("MALNAF DESC");
                    mgr.find(affiliation.getSession().getCurrentThreadTransaction());
                    if (mgr.size() != 0) {
                        String noAff = ((AFAffiliation) mgr.getFirstEntity()).getAffilieNumero();
                        result = String.valueOf(Integer.parseInt(noAff.substring(0, 3) + noAff.substring(4, 7)) + 1);
                    }
                    fromRoles = true;
                } else if (ROLE_DIV.equals(type)) {
                    // affilié comptes divers, doit être saisie entièrement par
                    // l'utilisateur
                    result = affiliation.getAffilieNumero();
                    fromRoles = true;
                }
            }
        }
        IFormatData affilieFormater = ((AFApplication) affiliation.getSession().getApplication()).getAffileFormater();
        if (JadeStringUtil.isEmpty(result) && !fromRoles) {
            // affilié standart ou associé
            if (!JadeStringUtil.isEmpty(affiliation.getTypeAssocie())) {
                // associé ou commenditaire. Un numéro doit avoir été saisi
                String noSaisi = affiliation.getAffilieNumero();
                if (!JadeStringUtil.isEmpty(noSaisi)) {
                    // formatage: si -00 saisi, supprimer extension
                    int pos = noSaisi.indexOf('-');
                    if (pos != -1) {
                        // supression extension
                        noSaisi = noSaisi.substring(0, pos);
                    }
                    noSaisi = affilieFormater.format(noSaisi);
                    AFAffiliationManager mgr = new AFAffiliationManager();
                    mgr.setSession(affiliation.getSession());
                    mgr.setLikeAffilieNumero(noSaisi);
                    mgr.find();
                    boolean parentFound = false;
                    int associe = 0;
                    for (int i = 0; i < mgr.size(); i++) {
                        String noAff = ((AFAffiliation) mgr.getEntity(i)).getAffilieNumero();
                        try {
                            int extension = Integer.parseInt(noAff.substring(noAff.length() - 2, noAff.length()));
                            if (extension == 0) {
                                // affiliation parente
                                parentFound = true;
                            } else if (extension > associe) {
                                // maj dernier associé
                                associe = extension;
                            }
                        } catch (Exception ex) {
                            // impossible de lire l'extension -> ignore le cas
                            continue;
                        }
                    } // end for
                    if (parentFound) {
                        // création nouveau numéro
                        result = noSaisi + "-" + JadeStringUtil.rightJustifyInteger(String.valueOf(associe + 1), 2);
                    }
                } // if noSaisi
            } else {
                // affiliation normal
                // recherche du prochain numéro valable
                AFAffiliationManager mgr = new AFAffiliationManager();
                mgr.setSession(affiliation.getSession());
                mgr.setToAffilieNumero(AFFILIE_MAX);
                mgr.forIsTraitement(false);
                mgr.setOrder("MALNAF DESC");
                mgr.find(affiliation.getSession().getCurrentThreadTransaction());
                if (mgr.size() != 0) {
                    String noAff = ((AFAffiliation) mgr.getFirstEntity()).getAffilieNumero();
                    result = String.valueOf(Integer.parseInt(noAff.substring(0, 3) + noAff.substring(4, 7)) + 1);
                }
            }
        }
        if (!JadeStringUtil.isEmpty(result)) {
            // formatage

            result = affilieFormater.format(result);
            if (result.indexOf('-') == -1) {
                result += "-00";
            }
        }
        return result;
    }

    @Override
    public String generateBeforeDisplay(AFAffiliation affiliation) throws Exception {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.naos.affiliation.INumberGenerator#isEditable(globaz.naos.db. affiliation.AFAffiliation)
     */
    @Override
    public boolean isEditable(AFAffiliation affiliation) throws Exception {

        return true;
    }

}
