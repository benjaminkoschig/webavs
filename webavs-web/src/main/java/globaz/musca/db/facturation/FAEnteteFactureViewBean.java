package globaz.musca.db.facturation;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.external.IntRole;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TIRole;
import java.util.HashSet;

public class FAEnteteFactureViewBean extends FAEnteteFacture implements globaz.framework.bean.FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne le numéro du tiers selon le rôle ou une chaine vide Ex: Soit le n° avs, le n° affilié ou le n° de
     * contribuable
     */

    /*
     * Permet d'éviter de charger dans la liste des codes systèmes certains rôles
     */
    public static HashSet<String> getExceptRole() {
        HashSet<String> except = new HashSet<String>();
        // liste des cs qui ne devront pas figurér dans la liste
        except.add(IntRole.ROLE_BANQUE);
        except.add(IntRole.ROLE_CONTRIBUABLE);
        return except;
    }

    private java.lang.String action = null;
    private java.lang.String adresseCourrier = "";
    private java.lang.String adressePaiement = "";
    private java.lang.String nomPrenomFacture = "";

    private Boolean wantReferenceFacture = Boolean.FALSE;

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // Recherche de la remarque
        if (JadeStringUtil.isEmpty(this.getRemarque()) || JadeStringUtil.isBlank(this.getRemarque())) {
            setRemarque(FARemarque.getRemarque(getIdRemarque(), transaction));
        }
        if ("true".equalsIgnoreCase(FWFindParameter.findParameter(transaction, "1", "FAREFFACTU",
                JACalendar.todayJJsMMsAAAA(), "", 0))) {
            setWantReferenceFacture(Boolean.TRUE);
            if (!JadeStringUtil.isEmpty(getReferenceFacture())) {
                JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                JadeUser user = service.load(getReferenceFacture());
                if (user != null) {
                    setNomPrenomFacture(user.getFirstname() + " " + user.getLastname());
                }
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Contrôle du n° de tiers
        // Si rôle = Assuré => contrôle n° avs
        if (getIdRole().equalsIgnoreCase(TIRole.CS_ASSURE)) {
            TIHistoriqueAvs histAvs = new TIHistoriqueAvs();
            histAvs.setSession(getSession());
            try {
                String idCode = histAvs._ctrlCodeSaisi(statement.getTransaction(), getIdTiers(), getIdExterneRole());
                // Récupération des erreurs engendrées dans la méthode
                // _ctrlCodeSaisi()
                setIdTiers(idCode);
            } catch (Exception e) {
                _addError(statement.getTransaction(), "Erreur lors du contrôle de la saisie. ");
            }
        }
        // Si rôle = contribuable => contrôle n° de contribuable
        if (getIdRole().equalsIgnoreCase(TIRole.CS_CONTRIBUABLE)) {
            TIHistoriqueContribuable histContr = new TIHistoriqueContribuable();
            histContr.setSession(getSession());
            try {
                String idCode = histContr._ctrlCodeSaisi(statement.getTransaction(), getIdTiers(), getIdExterneRole());
                setIdTiers(idCode);
            } catch (Exception e) {
                _addError(statement.getTransaction(), "Erreur lors du contrôle de la saisie. ");
            }
        }
        /* contrôle que le user saisi existe */
        if (!JadeStringUtil.isEmpty(getReferenceFacture())
                && "true".equalsIgnoreCase(FWFindParameter.findParameter(statement.getTransaction(), "1", "FAREFFACTU",
                        JACalendar.todayJJsMMsAAAA(), "", 0))) {
            JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
            JadeUser user = service.load(getReferenceFacture());
            if (user == null) {
                _addError(statement.getTransaction(), getSession().getLabel("REF_FACTURE_INCONNUE"));
            }
        }
        super._validate(statement);
    }

    public java.lang.String getAction() {
        return action;
    }

    public java.lang.String getAdresseCourrier() {
        return adresseCourrier;
    }

    public java.lang.String getAdressePaiement() {
        return adressePaiement;
    }

    /**
     * Retourne l'adresse de courrier du tiers ou une chaîne vide Ex: Soit le n° avs, le n° affilié ou le n° de
     * contribuable
     */
    public java.lang.String getAdressePrincipaleCourrier(BTransaction trans) throws Exception {
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(getIdAdresse())) {
            TIAdresse adresse = new TIAdresse();
            adresse.setISession(trans.getISession());
            try {
                return adresse.getDetailAdresse(getIdAdresse());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Retourne le numéro du tiers selon le rôle ou une chaine vide Ex: Soit le n° avs, le n° affilié ou le n° de
     * contribuable
     */
    public java.lang.String getAdressePrincipalePaiement(BTransaction trans) throws Exception {
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(getIdAdressePaiement())) {
            TIAdressePaiement adresse = new TIAdressePaiement();
            adresse.setSession(trans.getSession());
            try {
                adresse = adresse.getAdressePaiement(adresse.getSession(), getIdAdressePaiement());
                return adresse.getDetailPaiement(adresse.getSession());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public java.lang.String getNomPrenomFacture() {
        return nomPrenomFacture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:44:37)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNomTiers() {
        // Récupérer le tiers
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(getIdExterneRole())) {
            return super.getNomTiers();
        } else {
            return "";
        }
    }

    public String giveHtmlForImageModeImpression(String theContextPath) {

        if (JadeStringUtil.isEmpty(theContextPath)
                || (!isNonImprimable() && !FAEnteteFacture.CS_MODE_IMP_PASIMPZERO
                        .equalsIgnoreCase(getIdCSModeImpression()))
                || FAEnteteFacture.CS_MODE_IMP_STANDARD.equalsIgnoreCase(getIdCSModeImpression())) {
            return "";
        }

        String thePathImage = theContextPath;

        if (FAEnteteFacture.CS_MODE_IMP_PASIMPZERO.equalsIgnoreCase(getIdCSModeImpression())) {
            thePathImage = thePathImage + "/images/small-printer-no.gif";
        } else {
            thePathImage = thePathImage + "/images/small-printer-double.gif";
        }

        return "<IMG src=\"" + thePathImage + "\">";
    }

    public String giveIdCSModeImpression() {
        if (!JadeStringUtil.isBlankOrZero(getIdCSModeImpression())) {
            return getIdCSModeImpression();
        } else if (isNonImprimable()) {
            return FAEnteteFacture.CS_MODE_IMP_SEPAREE;
        } else {
            return FAEnteteFacture.CS_MODE_IMP_STANDARD;
        }
    }

    /*
     * Traitement après lecture
     */
    public Boolean isWantReferenceFacture() {
        return wantReferenceFacture;
    }

    public void rechercheIdTiersPourAffilie(BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isBlank(getIdExterneRole())) {
            // formatage du numero selon caisse
            try {
                TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                IFormatData affilieFormater = app.getAffileFormater();
                if (affilieFormater != null) {
                    setIdExterneRole(affilieFormater.format(getIdExterneRole()));
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
            // Recherche des affiliation pour ce numéro
            AFAffiliationManager affiManager = new AFAffiliationManager();
            affiManager.setSession(getSession());
            affiManager.setForAffilieNumero(getIdExterneRole());
            if (!JadeStringUtil.isBlank(getIdTiers())) {
                // l'id tiers est récupéré seulement quand on utilise la
                // PopupList
                affiManager.setForIdTiers(getIdTiers());
            }
            affiManager.find();
            if (affiManager.size() == 0) {
                if (!JadeStringUtil.isBlank(getIdTiers())) {
                    // Si pas trouver avec l'idTiers et numAffilié on recherche
                    // seulement avec l'idTiers
                    setIdTiers("");
                    affiManager.setForIdTiers("");
                    affiManager.setForAffilieNumero(getIdExterneRole());
                    affiManager.find(transaction);
                }
            } else if (affiManager.size() == 1) {
                setIdTiers(((AFAffiliation) affiManager.getEntity(0)).getIdTiers());
            } else {
                // Si l'id tiers n'a pas été renseigné, il se peut qu'un même
                // numéro d'affilié soit attribué à 2 tiers différents
                if (JadeStringUtil.isBlank(getIdTiers())) {
                    // Enregistrement du premier id tiers trouvé dans une
                    // variable d'aide
                    String premierIdTiers = ((AFAffiliation) affiManager.getFirstEntity()).getIdTiers();
                    boolean trouveAutreIdTiers = false;
                    int i = 0;
                    while ((i < affiManager.size()) && !trouveAutreIdTiers) {
                        AFAffiliation affiliation = (AFAffiliation) affiManager.getEntity(i);
                        if (!affiliation.getIdTiers().equals(premierIdTiers)) {
                            trouveAutreIdTiers = true;
                        }
                        i++;
                    }
                    if (trouveAutreIdTiers) {
                        _addError(transaction,
                                "Plusieurs tiers ont ce numéro d'affilié. Veuillez sélectionner un tiers dans la liste proposée.");
                    } else {
                        setIdTiers(premierIdTiers);
                    }
                }
            }

        }
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    public void setAdresseCourrier(java.lang.String newAdresseCourrier) {
        adresseCourrier = newAdresseCourrier;
    }

    public void setAdressePaiement(java.lang.String newAdressePaiement) {
        adressePaiement = newAdressePaiement;
    }

    public void setNomPrenomFacture(java.lang.String nomPrenomFacture) {
        this.nomPrenomFacture = nomPrenomFacture;
    }

    public void setWantReferenceFacture(Boolean wantReferenceFacture) {
        this.wantReferenceFacture = wantReferenceFacture;
    }
}
