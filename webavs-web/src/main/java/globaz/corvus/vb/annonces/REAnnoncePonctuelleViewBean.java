/*
 * Créé le 17.07.2009
 */
package globaz.corvus.vb.annonces;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.jade.business.services.codesysteme.JadeCodeSystemeService;
import ch.globaz.prestation.domaine.CodePrestation;
import globaz.commons.nss.NSUtil;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.demandes.RESaisieDemandeRenteViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;

/**
 * @author scr
 */
public class REAnnoncePonctuelleViewBean extends PRAbstractViewBeanSupport {

    private String ancienNSS = "";
    private boolean besoinAnnonceRentesLieesSiModification = false;
    private String canton = "";
    private String cleInfirmite = "";
    private String codeEtatCivil = "";
    private Boolean codeRefugie = Boolean.FALSE;
    private boolean creerAnnonceRente = false;
    private boolean creerAnnoncesRentesLiees = false;
    private String cs1 = "";
    private String cs2 = "";
    private String cs3 = "";
    private String cs4 = "";
    private String cs5 = "";
    private String csCanton = "";
    private String csEtatCivil = "";
    private String csGenreDroitAPI = "";
    private String dateDebut = "";
    private String degreInvalidite = "";
    private String droitApplique = "";
    private List<JadeCodeSysteme> familleCsGenreDroitAPI;
    private String genrePrestation = "";
    private String idBaseCalcul = "";
    private String idDemandeRente = "";
    private String idRenteAccordee = "";
    private String idRenteCalculee = "";
    private String idTiersBeneficiaire = "";
    private String idTiersComplementaire1 = "";
    private String idTiersComplementaire2 = "";
    private String idTiersRequerant = "";
    private Boolean isInvaliditePrecoce = null;
    private Map<String, String> mapVerification = new HashMap<String, String>();
    private String montantPrestation = "";
    private String nssComplementaire1 = "";
    private String nssComplementaire2 = "";
    private String officeAI = "";
    private PRTiersWrapper officeAICantonal = null;
    private String RAM = "";
    private String revenuPrisEnCompte9 = "";
    private String survenanceEvenementAssure = "";
    private PRTiersWrapper tiersBeneficiaire = null;
    private String csEtatCivilSF = "";

    private void chargerAncienNSS() {

        TIHistoriqueAvsManager histoMng = new TIHistoriqueAvsManager();
        histoMng.setSession(getSession());
        histoMng.setForIdTiers(getIdTiersBeneficiaire());
        histoMng.orderByNumAvs();
        try {
            histoMng.find();
            if (histoMng.size() > 1) {
                String nssActuel = "";
                tiersBeneficiaire = PRTiersHelper.getTiersParId(getSession(), getIdTiersBeneficiaire());
                if (tiersBeneficiaire != null) {
                    nssActuel = tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                }
                for (int i = 0; i < histoMng.size(); i++) {
                    TIHistoriqueAvs x = ((TIHistoriqueAvs) histoMng.getEntity(i));
                    if ((x.getNumAvs().length() == 16) && !nssActuel.equals(x.getNumAvs())) {
                        setAncienNSS(JadeStringUtil.substring(x.getNumAvs(), 4));
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.toString();
            e.printStackTrace();
        }
    }

    public String getAncienNSS() {
        if ("".equals(ancienNSS)) {
            chargerAncienNSS();
        }
        return ancienNSS;
    }

    public String getCanton() {
        return canton;
    }

    public String getCleInfirmite() {
        return cleInfirmite;
    }

    public String getCodeEtatCivil() {
        return codeEtatCivil;
    }

    public String getCodeRefugie() {
        return codeRefugie ? "1" : "0";
    }

    public String getCs1() {
        return cs1;
    }

    public String getCs2() {
        return cs2;
    }

    public String getCs3() {
        return cs3;
    }

    public String getCs4() {
        return cs4;
    }

    public String getCs5() {
        return cs5;
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    public String getCsGenreDroitAPI() {
        return csGenreDroitAPI;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public String getDroitApplique() {
        return droitApplique;
    }

    public List<JadeCodeSysteme> getFamilleCsGenreDroitAPI() {
        return familleCsGenreDroitAPI;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getIdTiersComplementaire1() {
        return idTiersComplementaire1;
    }

    public String getIdTiersComplementaire2() {
        return idTiersComplementaire2;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public Boolean getIsInvaliditePrecoce() {
        return isInvaliditePrecoce;
    }

    public Langues getLangue() {
        return Langues.getLangueDepuisCodeIso(getSession().getIdLangueISO());
    }

    public Map<String, String> getMapOfficesAi() {
        SortedMap<String, String> sortedOffices = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        try {
            for (PRTiersWrapper office : PRTiersHelper.getAdministrationActiveForGenre(getSession(),
                    RESaisieDemandeRenteViewBean.CS_ADMIN_GENRE_OFFICE_AI)) {
                sortedOffices.put(office.getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION),
                        office.getProperty(PRTiersWrapper.PROPERTY_NOM));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sortedOffices;
    }

    public Map<String, String> getMapVerification() {
        return mapVerification;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    /**
     * Retourne le no de l'office AI du canton de la caisse
     *
     * @return
     */
    public String getNoOfficeAICantonal() {
        if (officeAICantonal == null) {
            officeAICantonal = getOfficeAICantonal();
        }

        if (officeAICantonal != null) {
            return officeAICantonal.getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION);
        }
        return "";
    }

    public String getNssComplementaire1() {
        return NSUtil.formatWithoutPrefixe(nssComplementaire1, nssComplementaire1.length() > 14 ? true : false);
    }

    public String getNssComplementaire2() {
        return NSUtil.formatWithoutPrefixe(nssComplementaire2, nssComplementaire2.length() > 14 ? true : false);
    }

    public String getOfficeAI() {
        return officeAI;
    }

    private PRTiersWrapper getOfficeAICantonal() {

        PRTiersWrapper[] officesAI = null;

        try {
            officesAI = PRTiersHelper.getAdministrationActiveForGenre(getSession(),
                    RESaisieDemandeRenteViewBean.CS_ADMIN_GENRE_OFFICE_AI);
        } catch (Exception e) {
            // erreur dans la recherche des offices AI
            e.printStackTrace();
            return null;
        }

        if (officesAI != null) {
            for (int i = 0; i < officesAI.length; i++) {

                try {
                    // si canton caisse
                    if (officesAI[i].getProperty(PRTiersWrapper.PROPERTY_ID_CANTON).equalsIgnoreCase(
                            getSession().getApplication().getProperty("default.canton.caisse.location"))) {
                        return officesAI[i];
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        }

        return null;
    }

    public String getRAM() {
        return RAM;
    }

    public String getRevenuPrisEnCompte9() {
        return revenuPrisEnCompte9;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(getSession());
    }

    public String getSurvenanceEvenementAssure() {
        return survenanceEvenementAssure;
    }

    public void init(BSession session, BTransaction transaction, RERenteAccordee ra, REBasesCalcul baseCalcul,
            String csEtatCivil, String csCanton, String idMembreFamille) throws Exception {

        REBasesCalcul bc = null;

        bc = new REBasesCalculDixiemeRevision();
        bc.setSession(session);
        bc.setIdBasesCalcul(baseCalcul.getIdBasesCalcul());
        bc.retrieve(transaction);
        if (bc.isNew()) {
            bc = new REBasesCalculNeuviemeRevision();
            bc.setSession(session);
            bc.setIdBasesCalcul(baseCalcul.getIdBasesCalcul());
            bc.retrieve(transaction);

            if (bc.isNew()) {
                return;
            }
        }

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(session);
        demandeRente.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demandeRente.setIdRenteCalculee(bc.getIdRenteCalculee());
        demandeRente.retrieve();

        if (!demandeRente.isNew()) {
            setIdDemandeRente(demandeRente.getIdDemandeRente());
        }

        idTiersBeneficiaire = ra.getIdTiersBeneficiaire();
        idBaseCalcul = bc.getIdBasesCalcul();
        idRenteCalculee = bc.getIdRenteCalculee();
        idRenteAccordee = ra.getId();
        genrePrestation = ra.getCodePrestation();
        montantPrestation = ra.getMontantPrestation();
        dateDebut = ra.getDateDebutDroit();
        codeEtatCivil = PRACORConst.csEtatCivilHeraToAcorForRentes(session, csEtatCivil);
        this.csEtatCivil = PRACORConst.csEtatCivilHeraToCsEtatCivil(csEtatCivil);
        csEtatCivilSF = csEtatCivil;
        canton = PRACORConst.csCantonToAcor(csCanton);
        this.csCanton = csCanton;
        codeRefugie = !JadeStringUtil.isEmpty(ra.getCodeRefugie()) && "1".equals(ra.getCodeRefugie());

        if (!JadeStringUtil.isBlankOrZero(genrePrestation)) {
            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(genrePrestation));
            if (codePrestation.isAPI()) {
                REDemandeRenteAPI demandeAPI = new REDemandeRenteAPI();
                demandeAPI.setSession(session);
                demandeAPI.setIdDemandeRente(demandeRente.getIdDemandeRente());
                demandeAPI.retrieve();

                if (!demandeAPI.isNew()) {
                    csGenreDroitAPI = ra.getCsGenreDroitApi();
                }
                try {
                    BSessionUtil.initContext(getSession(), this);
                    JadeCodeSystemeService codeSystemeService = JadeBusinessServiceLocator.getCodeSystemeService();
                    familleCsGenreDroitAPI = codeSystemeService
                            .getFamilleCodeSysteme(IREDemandeRente.CS_GROUPE_GENRE_DROIT_API);
                } finally {
                    BSessionUtil.stopUsingContext(this);
                }
            }
        }

        // Mise à jour des NSS complémentaire 1 et 2
        majTiersComplementaires(session, transaction, ra, idMembreFamille);

        cs1 = ra.getCodeCasSpeciaux1();
        cs2 = ra.getCodeCasSpeciaux2();
        cs3 = ra.getCodeCasSpeciaux3();
        cs4 = ra.getCodeCasSpeciaux4();
        cs5 = ra.getCodeCasSpeciaux5();

        if (bc instanceof REBasesCalculDixiemeRevision) {
            droitApplique = "10";
        } else {
            droitApplique = "9";
            revenuPrisEnCompte9 = ((REBasesCalculNeuviemeRevision) bc).getRevenuPrisEnCompte();
        }
        RAM = bc.getRevenuAnnuelMoyen();
        degreInvalidite = bc.getDegreInvalidite();
        cleInfirmite = bc.getCleInfirmiteAyantDroit();
        survenanceEvenementAssure = bc.getSurvenanceEvtAssAyantDroit();
        isInvaliditePrecoce = bc.isInvaliditePrecoce();
        officeAI = bc.getCodeOfficeAi();
    }

    public boolean isBesoinAnnonceRentesLieesSiModification() {
        return besoinAnnonceRentesLieesSiModification;
    }

    public boolean isCreerAnnonceRente() {
        return creerAnnonceRente;
    }

    public boolean isCreerAnnoncesRentesLiees() {
        return creerAnnoncesRentesLiees;
    }

    public Boolean isRefugie() {
        return codeRefugie;
    }

    public boolean isRenteAI() {
        if (JadeStringUtil.isBlankOrZero(getGenrePrestation())) {
            return false;
        }
        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(getGenrePrestation()));
        return codePrestation.isAI();
    }

    public boolean isRenteAPI() {
        if (JadeStringUtil.isBlankOrZero(getGenrePrestation())) {
            return false;
        }
        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(getGenrePrestation()));
        return codePrestation.isAPI();
    }

    /**
     * Mise a jours des tiers complementaire a l'aide de la situation familiale et de du tiersBeneficiaire de la RA
     *
     * Pour ayant droit enfant =======================
     *
     * rentes 14, 24, 34, 54, 74, 16, 26, 56, 76 -> tiersComplementaire1 pere (si conj. inc. 00000000000) ->
     * tiersComplementaire2 mere
     *
     * rentes 15, 25, 35, 45, 55, 75 -> tiersComplementaire1 mere -> tiersComplementaire2 pere (si conj. inc.
     * 00000000000)
     *
     * Autres ======
     *
     * 13, 23, 33, 53, 73 -> tiersComplementaire1 l'autre conjoint
     *
     *
     * 10, 20, 50, 70 -> si celibataire -> tiersComplementaire1 rien -> si marie ou veuf -> tiersComplementaire1 l'autre
     * conjoint -> si divorce -> tiersComplementaire1 le dernier ex-conjoint
     *
     *
     * @param session
     * @param transaction
     * @param ra
     */
    private void majTiersComplementaires(BSession session, BTransaction transaction, RERenteAccordee ra,
            String idMembreFamille) throws Exception {

        // Voir dans la situation familiale pour setter le NSS des champs complémentaire 1 et 2
        if(ra.contientCodeCasSpecial("60")){
            SFMembreFamille mbr = new SFMembreFamille();
            mbr.setSession(session);
            mbr.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            mbr.setIdTiers(ra.getIdTiersBeneficiaire());
            mbr.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
            mbr.retrieve();
            // si le tiers n'est pas trouvé dans le domaine rente, recherche dans le domaine standard
            if (mbr.isNew()) {
                mbr.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                mbr.retrieve();
            }

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(mbr.getIdMembreFamille());
            enf.retrieve();

            SFMembreFamille parentLPart1 = null;
            SFMembreFamille parentLPart2 = null;
            if (enf.getPere() != null) {
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getPere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    parentLPart1 = enf.getMere();
                }
            }

            // tiersComplementaire2 mere
            if (enf.getMere() != null) {
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getMere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    parentLPart2 = enf.getPere();
                }
            }
            if((parentLPart1 != null && parentLPart2 != null) && parentLPart1.getCsSexe().equals(parentLPart2.getCsSexe()) ){
                if(parentLPart1.getIdTiers().equals(ra.getIdTiersBaseCalcul())){
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, parentLPart1.getIdTiers());
                    nssComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    idTiersComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                    tw = PRTiersHelper.getTiersParId(session, parentLPart2.getIdTiers());
                    nssComplementaire2 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    idTiersComplementaire2 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                }
                if(parentLPart2.getIdTiers().equals(ra.getIdTiersBaseCalcul())){
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, parentLPart2.getIdTiers());
                    nssComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    idTiersComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                    tw = PRTiersHelper.getTiersParId(session, parentLPart1.getIdTiers());
                    nssComplementaire2 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    idTiersComplementaire2 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                }
            }

        }
        // Pour ayant droit enfant
        if (REGenresPrestations.GENRE_14.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_24.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_34.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_36.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_54.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_74.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_16.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_26.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_56.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_76.equals(ra.getCodePrestation())) {

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(idMembreFamille);
            enf.retrieve();

            // tiersComplementaire1 pere (si conj. inc. 00000000000)
            if (enf.getPere() != null) {
                enf.getPere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(enf.getPere().getIdMembreFamille())
                        || JadeStringUtil.isBlankOrZero(enf.getPere().getIdTiers())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, enf.getPere().getIdTiers());
                    nssComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    idTiersComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                }
            }

            // tiersComplementaire2 mere
            if (enf.getMere() != null) {
                enf.getMere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(enf.getMere().getIdMembreFamille())
                        || JadeStringUtil.isBlankOrZero(enf.getMere().getIdTiers())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, enf.getMere().getIdTiers());
                    nssComplementaire2 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    idTiersComplementaire2 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                }
            }

            // Pour ayant droit enfant
        } else if (REGenresPrestations.GENRE_15.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_25.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_35.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_45.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_55.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_75.equals(ra.getCodePrestation())) {

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(idMembreFamille);
            enf.retrieve();

            // tiersComplementaire1 mere
            if (enf.getMere() != null) {
                enf.getMere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(enf.getMere().getIdMembreFamille())
                        || JadeStringUtil.isBlankOrZero(enf.getMere().getIdTiers())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, enf.getMere().getIdTiers());
                    nssComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    idTiersComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                }
            }

            // tiersComplementaire2 pere (si conj. inc. 00000000000)
            if (enf.getPere() != null) {
                enf.getPere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(enf.getPere().getIdMembreFamille())
                        || JadeStringUtil.isBlankOrZero(enf.getPere().getIdTiers())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, enf.getPere().getIdTiers());
                    nssComplementaire2 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    idTiersComplementaire2 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

                }
            }

        } else if (REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_33.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_53.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_73.equals(ra.getCodePrestation())) {

            // Reprendre la situation familiale
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());

            // Reprendre les relations familiales
            ISFRelationFamiliale[] rfs = sf.getRelationsConjoints(ra.getIdTiersBeneficiaire(),
                    JACalendar.todayJJsMMsAAAA());

            // tiers complementaire1 = l'autre conjoint
            for (int j = 0; (rfs != null) && (j < rfs.length); j++) {

                ISFRelationFamiliale rf = rfs[j];

                String idMbrFamille1 = rf.getIdMembreFamilleHomme();
                ISFMembreFamille mf1 = sf.getMembreFamille(idMbrFamille1);
                String idTiers1 = mf1.getIdTiers();
                // Si le nssComplémentaire 1 est déjà renseignée, ne pas passer le if sinon le nssComplémentaire1 de
                // l'épouse
                // est écrasé par celui de l'ex-épouse
                if (!idTiers1.equals(ra.getIdTiersBeneficiaire()) && JadeStringUtil.isEmpty(nssComplementaire1)) {
                    if (!JadeStringUtil.isBlankOrZero(idTiers1)) {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers1);
                        nssComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                        idTiersComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                    }
                } else {
                    String idMbrFamille2 = rf.getIdMembreFamilleFemme();
                    ISFMembreFamille mf2 = sf.getMembreFamille(idMbrFamille2);
                    String idTiers2 = mf2.getIdTiers();
                    // Si le nssComplémentaire 1 est déjà renseignée, ne pas passer le if sinon le nssComplémentaire1 de
                    // l'épouse
                    // est écrasé par celui de l'ex-épouse
                    if (!idTiers2.equals(ra.getIdTiersBeneficiaire()) && JadeStringUtil.isEmpty(nssComplementaire1)) {
                        if (!JadeStringUtil.isBlankOrZero(idTiers2)) {
                            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers2);
                            nssComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                            idTiersComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                        }
                    }
                }
            }

        } else if (REGenresPrestations.GENRE_10.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_20.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_50.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())) {

            // si marie ou veuf -> tiersComplementaire1 l'autre conjoint
            // si divorce -> tiersComplementaire1 le dernier ex-conjoint

            // Reprendre la situation familiale
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());

            // Reprendre les relations familiales
            ISFRelationFamiliale[] rfs = sf.getRelationsConjoints(ra.getIdTiersBeneficiaire(),
                    JACalendar.todayJJsMMsAAAA());

            // tiers complementaire1 = l'autre conjoint
            for (int j = 0; (rfs != null) && (j < rfs.length); j++) {

                ISFRelationFamiliale rf = rfs[j];

                String idMbrFamille1 = rf.getIdMembreFamilleHomme();
                ISFMembreFamille mf1 = sf.getMembreFamille(idMbrFamille1);
                String idTiers1 = mf1.getIdTiers();
                // Si le nssComplémentaire 1 est déjà renseignée, ne pas passer le if sinon le nssComplémentaire1 de
                // l'épouse
                // est écrasé par celui de l'ex-épouse
                if (!idTiers1.equals(ra.getIdTiersBeneficiaire()) && JadeStringUtil.isEmpty(nssComplementaire1)) {
                    if (!JadeStringUtil.isBlankOrZero(idTiers1)) {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers1);
                        nssComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                        idTiersComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                    }
                } else {
                    String idMbrFamille2 = rf.getIdMembreFamilleFemme();
                    ISFMembreFamille mf2 = sf.getMembreFamille(idMbrFamille2);
                    String idTiers2 = mf2.getIdTiers();
                    // Si le nssComplémentaire 1 est déjà renseignée, ne pas passer le if sinon le nssComplémentaire1 de
                    // l'épouse
                    // est écrasé par celui de l'ex-épouse
                    if (!idTiers2.equals(ra.getIdTiersBeneficiaire()) && JadeStringUtil.isEmpty(nssComplementaire1)) {
                        if (!JadeStringUtil.isBlankOrZero(idTiers2)) {
                            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers2);
                            nssComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                            idTiersComplementaire1 = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                        }
                    }
                }

                // si celibataire -> tiersComplementaire1 rien
                if (rfs != null) {
                    if (rfs.length == 0) {
                        nssComplementaire1 = "";
                        idTiersComplementaire1 = "";
                    }
                }
            }
        }

    }

    public void setAncienNSS(String ancienNSS) {
        this.ancienNSS = ancienNSS;
    }

    public void setBesoinAnnonceRentesLieesSiModification(boolean besoinAnnonceRentesLieesSiModification) {
        this.besoinAnnonceRentesLieesSiModification = besoinAnnonceRentesLieesSiModification;
    }

    public void setBesoinAnnonceRentesLieesSiModification(String besoinAnnonceRentesLieesSiModification) {
        this.besoinAnnonceRentesLieesSiModification = Boolean.parseBoolean(besoinAnnonceRentesLieesSiModification);
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCleInfirmite(String cleInfirmite) {
        this.cleInfirmite = cleInfirmite;
    }

    public void setCodeEtatCivil(String codeEtatCivil) {
        this.codeEtatCivil = codeEtatCivil;
    }

    public void setCodeRefugie(String codeRefugie) {
        this.codeRefugie = !JadeStringUtil.isBlank(codeRefugie);
    }

    public void setCreerAnnonceRente(boolean creerAnnonceRente) {
        this.creerAnnonceRente = creerAnnonceRente;
    }

    public void setCreerAnnoncesRentesLiees(boolean creerAnnoncesRentesLiees) {
        this.creerAnnoncesRentesLiees = creerAnnoncesRentesLiees;
    }

    public void setCs1(String cs1) {
        this.cs1 = cs1;
    }

    public void setCs2(String cs2) {
        this.cs2 = cs2;
    }

    public void setCs3(String cs3) {
        this.cs3 = cs3;
    }

    public void setCs4(String cs4) {
        this.cs4 = cs4;
    }

    public void setCs5(String cs5) {
        this.cs5 = cs5;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsEtatCivil(String csEtatCivil) {
        this.csEtatCivil = csEtatCivil;
    }

    public void setCsGenreDroitAPI(String csGenreDroitAPI) {
        this.csGenreDroitAPI = csGenreDroitAPI;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    public void setDroitApplique(String droitApplique) {
        this.droitApplique = droitApplique;
    }

    public void setFamilleCsGenreDroitAPI(List<JadeCodeSysteme> familleCsGenreDroitAPI) {
        this.familleCsGenreDroitAPI = familleCsGenreDroitAPI;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setIdBaseCalcul(String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdRenteCalculee(String idRenteCalculee) {
        this.idRenteCalculee = idRenteCalculee;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIdTiersComplementaire1(String idTiersComplementaire1) {
        this.idTiersComplementaire1 = idTiersComplementaire1;
    }

    public void setIdTiersComplementaire2(String idTiersComplementaire2) {
        this.idTiersComplementaire2 = idTiersComplementaire2;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setIsInvaliditePrecoce(Boolean isInvaliditePrecoce) {
        this.isInvaliditePrecoce = isInvaliditePrecoce;
    }

    public void setMapVerification(Map<String, String> mapVerification) {
        this.mapVerification = mapVerification;
    }

    public void setMapVerification(String mapVerification) {
        if (!JadeStringUtil.isBlank(mapVerification)) {
            this.mapVerification = new Gson().fromJson(mapVerification, new TypeToken<Map<String, String>>() {
            }.getType());

            if (this.mapVerification == null) {
                this.mapVerification = new HashMap<String, String>();
            }
        } else {
            if (this.mapVerification == null) {
                this.mapVerification = new HashMap<String, String>();
            } else {
                this.mapVerification.clear();
            }
        }
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setNssComplementaire1(String nssComplementaire1) {
        this.nssComplementaire1 = nssComplementaire1;
    }

    public void setNssComplementaire2(String nssComplementaire2) {
        this.nssComplementaire2 = nssComplementaire2;
    }

    public void setOfficeAI(String officeAI) {
        this.officeAI = officeAI;
    }

    public void setRAM(String ram) {
        RAM = ram;
    }

    public void setRevenuPrisEnCompte9(String revenuPrisEnCompte9) {
        this.revenuPrisEnCompte9 = revenuPrisEnCompte9;
    }

    public void setSurvenanceEvenementAssure(String survenanceEvenementAssure) {
        this.survenanceEvenementAssure = survenanceEvenementAssure;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public String getCsEtatCivilSF() {
        return csEtatCivilSF;
    }

    public void setCsEtatCivilSF(String csEtatCivilSF) {
        this.csEtatCivilSF = csEtatCivilSF;
    }
}
