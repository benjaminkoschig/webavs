/*
 * Créé le 15 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ccgc.affiliation;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.affiliation.INumberGenerator;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;

/**
 * @author dgi
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CCGCNoAffGenerator implements INumberGenerator {

    // public static String PYRIS_ROLE_ATTRIBUTE = "role";
    // public static String AFFILIE_CNT = "FERAFFNORM";
    // public static String AFFILIE_LPP_CNT = "FERAFFLPP";
    // public static String AFFILIE_SIEGE_MIN = "950.000-00";
    // public static String AFFILIE_MAX = "400.000-00";
    // public static String AFFILIE_DIV_CNT = "FERAFFDIV";
    // public static String ROLE_LPP = "19120065";
    // public static String ROLE_TIERS = "19120066";
    // public static String ROLE_DIV = "19120064";
    // public static String ROLE_SIEGE = "19120074";
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
        String dossier = null;
        String controle = null;
        String typeAffiliation = null;
        String controleP1 = null;
        String controleP2 = null;
        int posPoint;

        IFormatData affilieFormater = ((AFApplication) affiliation.getSession().getApplication()).getAffileFormater();
        if (JadeStringUtil.isEmpty(result)) {
            // affilié standart ou associé
            if (!JadeStringUtil.isEmpty(affiliation.getTypeAssocie())) {
                // associé ou commenditaire. Un numéro doit avoir été saisi
                String noSaisi = affiliation.getAffilieNumero();
                if (!JadeStringUtil.isEmpty(noSaisi)) {
                    // formatage: si -00 saisi, supprimer extension
                    int pos = noSaisi.indexOf('.');
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
                mgr.setOrder("MALNAF DESC");

                mgr.forIsTraitement(false);
                mgr.find(affiliation.getSession().getCurrentThreadTransaction());
                if (mgr.size() != 0) {
                    String noAff = ((AFAffiliation) mgr.getFirstEntity()).getAffilieNumero();
                    posPoint = noAff.indexOf('.');
                    dossier = noAff.substring(0, posPoint);// séléctionne le
                    // numéro de dossier
                    dossier = String.valueOf(Integer.parseInt(dossier) + 1);

                    // rajouter des 0 devant pour avoir 6 chiffres
                    int nbChiffreDossier = dossier.length();
                    for (int i = 0; i < (6 - nbChiffreDossier); i++) {
                        dossier = "0" + dossier;
                    }

                    // définir si cotisation paritaires ou personnelles
                    typeAffiliation = affiliation.getTypeAffiliation();
                    if (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_EMPLOY)
                            || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_PROVIS)
                            || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_LTN)
                            || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_BENEF_AF)
                            || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_EMPLOY_D_F)) {
                        controleP1 = "00";
                    } else if (typeAffiliation.equals(CodeSystem.TYPE_AFFILI_INDEP)
                            || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                            || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_TSE)
                            || typeAffiliation.equals(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                        controleP1 = "01";
                    }

                    // variable temporaire permettant de calculer le numéro de
                    // controle
                    String temp = new String(dossier);

                    temp = temp + controleP1;
                    int somme = 0;

                    for (int i = 0; i != temp.length(); i += 2) {
                        somme += Integer.parseInt(temp.substring(i, i + 2));
                    }

                    controleP2 = String.valueOf(somme);

                    if (controleP2.length() == 1) {
                        controleP2 = "0" + controleP2;
                    }

                    if (controleP2.length() == 3) {
                        controleP2 = String
                                .valueOf(Integer.parseInt(controleP2.charAt(0) + controleP2.substring(1, 2)));
                    }

                    controle = controleP1 + controleP2;
                    result = dossier + "." + controle;
                }
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
