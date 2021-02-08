package globaz.apg.vb.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointDemande;
import globaz.apg.db.droits.APDroitLAPGJointDemandeManager;
import globaz.apg.servlet.IAPActions;
import globaz.apg.util.TypePrestation;
import globaz.apg.utils.APGUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author VRE
 */
public class APDroitLAPGJointDemandeViewBean extends APDroitLAPGJointDemande implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";
    private static final String LABEL_NON_DEFINITIFS = "JSP_NON_DEFINITIF";

    private transient APDroitLAPG droit = null;
    private APDroitAPGDTO dto;
    private transient Vector etatsDroit = null;
    private String nbPostit = "";
    private transient Vector orderBy = null;
    /**
     * Utilisé dans le cas ou l'on veut recréer une annonce liée à une prestation suite à une reprise de données Permet
     * d'insérer le BPID voulut pour l'annonce qui sera créé
     */
    private String pidAnnonce;
    private transient Vector responsables = null;
    private TypePrestation typePrestation;

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbPostit = statement.dbReadNumeric(APDroitLAPGJointDemandeListViewBean.FIELDNAME_COUNT_POSTIT);

        super._readProperties(statement);

        setIdTiers(statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS));
    }

    public String getActionRecapitulatif() {
        if (TypePrestation.TYPE_APG.equals(typePrestation)) {
            return IAPActions.ACTION_RECAPITUALATIF_DROIT_APG;
        } else {
            return IAPActions.ACTION_RECAPITUALATIF_DROIT_MAT;
        }
    }

    public String getCSTypePrestation() {
        return typePrestation.toCodeSysteme();
    }

    public String getCurrentUserId() {
        return getSession().getUserId();
    }

    /**
     * Méthode qui retourne le détail du requérant formaté
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {
        return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNomPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());

    }

    public APDroitAPGDTO getDto() {
        return dto;
    }

    public String getEtatDemandeLibelle() {
        return getSession().getCodeLibelle(getEtatDemande());
    }

    public Vector getEtatDroitData() {
        if (etatsDroit == null) {
            etatsDroit = PRCodeSystem.getLibellesPourGroupe(IAPDroitLAPG.CS_GROUPE_ETAT_DROIT_APG, getSession());

            // ajout des options custom
            etatsDroit.add(
                    0,
                    new String[] { APDroitLAPGJointDemandeManager.CLE_NON_DEFINITIFS,
                            getSession().getLabel(APDroitLAPGJointDemandeViewBean.LABEL_NON_DEFINITIFS) });
            etatsDroit.add(0, new String[] { APDroitLAPGJointDemandeManager.CLE_DROITS_TOUS, "" });
        }

        return etatsDroit;
    }

    public Vector getGenreDroitData() {
        Vector genresDroit = PRCodeSystem.getLibellesEtCodePourGroupe(IAPDroitLAPG.CS_GROUPE_GENRE_SERVICE_APG, getSession());

        for(int i=0; i<genresDroit.size(); i++){
            String[] a = (String[]) genresDroit.get(i);
            if(!APGUtils.isTypeAllocationPandemie(a[0])){
                genresDroit.remove(i);
                i--;
            }
        }
        genresDroit.add(0, new String[] { APDroitLAPGJointDemandeManager.CLE_GENRE_TOUS, "" });
        return genresDroit;
    }

    public String getEtatDroitLibelle() {
        return getSession().getCodeLibelle(getEtatDroit());
    }

    public String getGenreDroitLibelle() {
        return getSession().getCodeLibelle(getGenreService());
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }

    }

    /**
     * Méthode qui retourne le libellé du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé du sexe (homme ou femme)
     */
    public String getLibelleSexe() {
        return getSession().getCodeLibelle(getCsSexe());
    }

    public String getNbPostit() {
        return nbPostit;
    }

    public String getNomPrenom() {
        return getNom() + " " + getPrenom();
    }

    public String getNomPrenomGestionnaire() {
        return getPrenomGestionnaire() + " " + getNomGestionnaire();
    }

    public Vector getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector(4);
            orderBy.add(new String[] {
                    APDroitLAPGJointDemande.FIELDNAME_NOM + ", " + APDroitLAPGJointDemande.FIELDNAME_PRENOM + ", "
                            + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + " DESC",
                    getSession().getLabel("JSP_NOM") + ", " + getSession().getLabel("JSP_PRENOM") });
            orderBy.add(new String[] {
                    APDroitLAPGJointDemande.FIELDNAME_NUM_AVS + ", " + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + " DESC",
                    getSession().getLabel("JSP_NSS_ABREGE") });
            orderBy.add(new String[] { APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + " DESC",
                    getSession().getLabel("JSP_DATE_DEBUT") });
            orderBy.add(new String[] {
                    APDroitLAPG.FIELDNAME_IDDROIT_LAPG + " DESC" + ", " + APDroitLAPG.FIELDNAME_DATEDEBUTDROIT
                            + " DESC", getSession().getLabel("JSP_NO_DROIT_COURT") });
        }

        return orderBy;
    }

    public final String getPidAnnonce() {
        return pidAnnonce;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {userId, userFullName} des gestionnaires pour le type de prestation
     * défini pour ce view bean.
     * <p>
     * Le vecteur n'est créé qu'une seule fois pour chaque instance de ce view bean.
     * </p>
     * 
     * @return la valeur courante de l'attribut responsable data
     */
    public Vector getResponsableData() {
        // créer les responsables s'ils n'ont pas déjà été récupérés
        if (responsables == null) {
            try {
                responsables = PRGestionnaireHelper.getIdsEtNomsGestionnaires(typePrestation.toGroupeGestionnaire());
            } catch (Exception e) {
                _addError(getSession().getCurrentThreadTransaction(),
                        getSession().getLabel(APDroitLAPGJointDemandeViewBean.ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (responsables == null) {
                responsables = new Vector();
            }
            responsables.insertElementAt(new String[] { "", "" }, 0);
        }

        return responsables;
    }

    public String getTitreEcran() {
        if(getTypePrestation().equals(TypePrestation.TYPE_PATERNITE)) {
            return getSession().getLabel("JSP_TITRE_LISTE_PATERNITE");
        }else if(getTypePrestation().equals(TypePrestation.TYPE_PANDEMIE)) {
            return getSession().getLabel("JSP_TITRE_LISTE_PANDEMIE");
        }else if (getTypePrestation().equals(TypePrestation.TYPE_APG)) {
            return getSession().getLabel("JSP_TITRE_LISTE_APG");
        } else {
            return getSession().getLabel("JSP_TITRE_LISTE_MATERNITE");
        }
    }

    public TypePrestation getTypePrestation() {
        return typePrestation;
    }

    public boolean hasPostit() {
        return Integer.parseInt(nbPostit) > 0;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNoAVS())) {
            return "";
        }

        if (getNoAVS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NNSS, sinon false
     * 
     * @param noAvs
     * @return String (true ou false)
     */
    public String isNNSS(String noAvs) {

        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            return "";
        }

        if (noAvs.length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Retourne true si le droit possède au moins une prestation du genre passé en paramètre
     *
     * @param genre
     * @return boolean
     */
    public boolean hasPrestationOfGenre(int genre) throws Exception {
        if ((droit == null) || !droit.getIdDroit().equals(super.getIdDroit())) {
            droit = APGUtils.loadDroit(getSession(), super.getIdDroit(), super.getGenreService());
        }

        return droit.hasPrestationOfGenre(getSession().getCurrentThreadTransaction(), genre);
    }

    public APDroitLAPG loadDroit() throws Exception {
        if ((droit == null) || !droit.getIdDroit().equals(super.getIdDroit())) {
            droit = APGUtils.loadDroit(getSession(), super.getIdDroit(), super.getGenreService());
        }

        return droit;
    }

    /**
     * Recherche si tous les enfants liés au droit ont ete adopte et retourne true dans ce cas.
     * 
     * @return
     */
    public boolean retrieveIsDroitAdoption() {
        APEnfantMatListViewBean enfantsMat = new APEnfantMatListViewBean();
        enfantsMat.setSession(getSession());
        enfantsMat.setForIdDroitMaternite(getIdDroit());
        try {
            enfantsMat.find();

            for (Iterator iter = enfantsMat.iterator(); iter.hasNext();) {
                APEnfantMatViewBean enfantMat = (APEnfantMatViewBean) iter.next();
                if (!enfantMat.getIsAdoption().booleanValue()) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Commentaire EFL: pas une très bonne idée:
            // cela signifiera qu'en cas d'erreur,
            // isDroitAdoption renvoie true...
        }
        return true;
    }

    public void setDto(APDroitAPGDTO droitAPGDTO) {
        dto = droitAPGDTO;
    }

    public final void setPidAnnonce(String pidAnnonce) {
        this.pidAnnonce = pidAnnonce;
    }

    public void setTypePrestation(TypePrestation prestation) {
        typePrestation = prestation;
    }

}
