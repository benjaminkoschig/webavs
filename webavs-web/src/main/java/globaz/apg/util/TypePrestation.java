/*
 * Créé le 10 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.util;

import globaz.apg.application.APApplication;
import globaz.apg.servlet.IAPActions;
import globaz.globall.api.GlobazSystem;
import globaz.jade.log.JadeLogger;
import globaz.prestation.api.IPRDemande;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Cette classe facilite la gestion du type de prestation en encapsulant le code système du type de prestation. Comme il
 * n'est pas possible d'instancier cette classe, elle se comporte un peu comme un type énuméré.
 * 
 * @author vre
 */
public class TypePrestation implements Serializable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final HashMap ACTIONS = new HashMap();

    private static final HashMap GROUPES = new HashMap();

    /**
     * une instance de TypePrestation identifiant la partie APG de l'application
     */
    public static final TypePrestation TYPE_APG = new TypePrestation(IPRDemande.CS_TYPE_APG);

    private static final TypePrestation TYPE_INCONNU = new TypePrestation("");

    /**
     * une instance de TypePrestation identifiant la partie MATERNITE de l'application
     */
    public static final TypePrestation TYPE_MATERNITE = new TypePrestation(IPRDemande.CS_TYPE_MATERNITE);

    public static final TypePrestation TYPE_PANDEMIE = new TypePrestation(IPRDemande.CS_TYPE_PANDEMIE);

    static {
        ACTIONS.put(IPRDemande.CS_TYPE_APG, IAPActions.ACTION_SAISIE_CARTE_APG);
        ACTIONS.put(IPRDemande.CS_TYPE_MATERNITE, IAPActions.ACTION_SAISIE_CARTE_AMAT);
        ACTIONS.put(IPRDemande.CS_TYPE_PANDEMIE, IAPActions.ACTION_SAISIE_CARTE_PAN);
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * retourne l'instance de TypePrestation.
     * 
     * @param typePrestation
     *            un identifiant du type de prestation (soit APG ou MATERNITE).
     * 
     * @return l'instance de TypePrestation correspondante (jamais null).
     */
    public static final TypePrestation typePrestationInstanceFor(String typePrestation) {
        switch (typePrestation) {
            case "APG":
            return TYPE_APG;
            case "MATERNITE":
            return TYPE_MATERNITE;
            case "PANDEMIE":
                return TYPE_PANDEMIE;
            default:
        return TYPE_INCONNU;
    }
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * retourne l'instance de TypePrestation selon le code système.
     * 
     * @param csTypePrestation
     *            un identifiant du type de prestation (soit APG ou MATERNITE).
     * 
     * @return l'instance de TypePrestation correspondante (jamais null).
     */
    public static final TypePrestation typePrestationInstanceForCS(String csTypePrestation) {
        switch (csTypePrestation) {
            case IPRDemande.CS_TYPE_APG:
            return TYPE_APG;
            case IPRDemande.CS_TYPE_MATERNITE:
            return TYPE_MATERNITE;
            case IPRDemande.CS_TYPE_PANDEMIE:
                return TYPE_PANDEMIE;
            default:
        return TYPE_INCONNU;
    }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String id;

    private TypePrestation(String id) {
        this.id = id;
    }

    /**
     * vérifie l'égalité de deux type de prestations.
     * 
     * @param arg0
     *            le type à comparer avec celui-ci.
     * 
     * @return vrai si arg0 est une instance de TypePrestation non nulle et représentant le même type.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg0) {
        boolean retValue = false;

        if ((arg0 != null) && (arg0 instanceof TypePrestation)) {
            retValue = id.equals(((TypePrestation) arg0).id);
        }

        return retValue;
    }

    /**
     * retourne vrai si le type de prestation est connu (APG OU MATERNITE).
     * 
     * @return vrai si le type de prestation est connu.
     */
    public boolean isConnu() {
        return TYPE_INCONNU != this;
    }

    private String loadNomGroupe(String cle) {
        try {
            return ((APApplication) GlobazSystem.getApplication(APApplication.DEFAULT_APPLICATION_APG))
                    .getProperty(cle);
        } catch (Exception e) {
            JadeLogger.error(this, "Impossible de charger l'application " + APApplication.DEFAULT_APPLICATION_APG);

            return null;
        }
    }

    /**
     * retourne le code système associé à ce type de prestation.
     * 
     * @return le code système associé à ce type de prestation.
     */
    public String toCodeSysteme() {
        return id;
    }

    /**
     * Retourne le nom du groupe des gestionnaires pour ce type de prestation.
     * 
     * @return le nom du groupe des gestionnaires pour ce type de prestation ou chaîne vide si le type est inconnu.
     */
    public String toGroupeGestionnaire() {
        if (GROUPES.isEmpty()) {
            synchronized (GROUPES) {
                GROUPES.put(IPRDemande.CS_TYPE_APG, loadNomGroupe(APApplication.PROPERTY_GROUPE_APG_GESTIONNAIRE));
                GROUPES.put(IPRDemande.CS_TYPE_MATERNITE, loadNomGroupe(APApplication.PROPERTY_GROUPE_MATERNITE_GESTIONNAIRE));
                GROUPES.put(IPRDemande.CS_TYPE_PANDEMIE, loadNomGroupe(APApplication.PROPERTY_GROUPE_APG_GESTIONNAIRE));
            }
        }

        String retValue = (String) GROUPES.get(id);

        if (retValue == null) {
            return "";
        }

        return retValue;
    }

    /**
     * retourne le code système associé à ce type de prestation.
     * 
     * @return le code système associé à ce type de prestation ou chaîne vide si le type est inconnu.
     */
    @Override
    public String toString() {
        return id;
    }

    /**
     * Renvoie la chaine action associée avec ce type de prestation. Par exemple 'apg.droits.droitMaternite'
     * 
     * @return une chaine userAction ou une chaine vide si le type est inconnu.
     */
    public String toUserAction() {
        String retValue = (String) ACTIONS.get(id);

        if (retValue == null) {
            return "";
        }

        return retValue;
    }
}
