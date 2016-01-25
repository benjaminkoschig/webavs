package globaz.corvus.acor.adapter.plat;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierAssuresPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REFichierAssuresPrinter extends PRAbstractFichierAssuresPrinter {

    public static final String CODE_CANTON_ETRANGER = "505027";
    public static final String CANTON_ET_PAYS_INCONNU = "999";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final int STATE_DEBUT = -2;
    private static final int STATE_FAMILLE = 2;

    private static final int STATE_FIN = -1;
    private static final int STATE_REQUERANT = 1;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Iterator assures;
    private Object membre;
    private int state = REFichierAssuresPrinter.STATE_DEBUT;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REFichierAssuresPrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public REFichierAssuresPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private REACORDemandeAdapter adapter() {
        return (REACORDemandeAdapter) parent;
    }

    private void doSaisieChampsConjoint9_19(StringBuffer cmd) throws PRACORException {
        // ---------------------------------------------------------
        // Recherche d'une demande AI du conjoint, non validée
        // ---------------------------------------------------------
        String idTiersConjoint = ((ISFMembreFamilleRequerant) membre).getIdTiers();
        REDemandeRenteJointDemandeManager mgr = new REDemandeRenteJointDemandeManager();
        mgr.setSession(getSession());
        mgr.setForIdTiersRequ(idTiersConjoint);
        // ?"*ç"?*ç"?*ç?"ç* La prochaine fois, utiliser un code sysème !!!
        // mgr.setForCsEtatDemande(REDemandeRenteJointDemande.LABEL_NON_VALIDE);

        mgr.setForCsEtatDemandeIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL + ", "
                + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE + ", "
                + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);

        mgr.setForCsType(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE);
        mgr.setOrderBy(REDemandeRente.FIELDNAME_DATE_DEBUT + " DESC ");
        mgr.setForCsTypeCalcul(IREDemandeRente.CS_TYPE_CALCUL_STANDARD);

        try {

            mgr.find();

            // ---------------------------------------------------------
            // Traitement des paramètres du conjoint s'il possède une demande de
            // type AI
            // ---------------------------------------------------------
            if (!mgr.isEmpty()) {
                REDemandeRenteJointDemande elm = (REDemandeRenteJointDemande) mgr.getFirstEntity();
                REDemandeRente demConjoint = REDemandeRente.loadDemandeRente(getSession(), null,
                        elm.getIdDemandeRente(), elm.getCsTypeDemande());

                // ---------------------------------------------------------
                // Paramètres d'invalidité
                // ---------------------------------------------------------

                // 9. code office AI
                this.writeChaine(cmd, ((REDemandeRenteInvalidite) demConjoint).getCodeOfficeAI());

                List periodesInvalidite;
                try {
                    periodesInvalidite = ((REDemandeRenteInvalidite) demConjoint).getPeriodesInvalidite();
                } catch (Exception e) {
                    throw new PRACORException(e.toString());
                }
                int counter = 0;
                for (Iterator iter = periodesInvalidite.iterator(); iter.hasNext();) {
                    counter++;
                    REPeriodeInvalidite element = (REPeriodeInvalidite) iter.next();
                    if (counter > 1) {
                        this.writeChaineSansFinDeChamp(cmd, "/");
                    }
                    // 10. Début invalidité
                    // 11. Fin invalidité
                    // 12. Degré invalidité
                    this.writeDate(cmd, element.getDateDebutInvalidite());
                    this.writeDate(cmd, element.getDateFinInvalidite());
                    this.writeChaine(cmd, element.getDegreInvalidite());
                }

                // 13. Genre d'infirmité, atteinte fonctionnelle
                this.writeEntierSansFinDeChamp(cmd,
                        getSession().getCode(((REDemandeRenteInvalidite) demConjoint).getCsInfirmite()));
                this.writeChaine(cmd, getSession().getCode(((REDemandeRenteInvalidite) demConjoint).getCsAtteinte()));

                // 14. Pourcentage de réduction
                this.writeEntier(cmd, ((REDemandeRenteInvalidite) demConjoint).getPourcentageReduction());

                // Retrieve des informations complémentaires
                PRInfoCompl infoCompl = new PRInfoCompl();
                try {
                    infoCompl.setSession(getSession());
                    infoCompl.setIdInfoCompl(demConjoint.getIdInfoComplementaire());
                    infoCompl.retrieve();
                } catch (Exception e) {
                    getSession().addError(getSession().getLabel("ERREUR_INFOS_COMP"));
                }

                // 15. cas pénible AI
                if (infoCompl.getIsCasPenibleAI().booleanValue()) {
                    this.writeEntier(cmd, "1");
                } else {
                    this.writeEntier(cmd, "0");
                }

                // 16. Veto prestation
                this.writeChampVide(cmd);
                // 17. Refugie
                this.writeChampVide(cmd);
                // 18. Date prononcé dernière révision AI //Spécifique Caisse
                // Suisse
                this.writeChampVide(cmd);
                // 19. Age flexible de la retraite.
                this.writeChampVide(cmd);
            }

            // ---------------------------------------------------------
            // Recherche d'une demande Vieillesse du conjoint, Validée
            // ---------------------------------------------------------
            // ?"*ç"?*ç"?*ç?"ç* La prochaine fois, utiliser un code sysème !!!

            mgr.setForCsEtatDemandeIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE + ", "
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
            // On reset ce champ, précédemment setté.
            mgr.setForCsEtatDemande(null);
            mgr.setForCsType(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE);
            mgr.setOrderBy(REDemandeRente.FIELDNAME_DATE_DEBUT + " DESC ");
            mgr.setForCsTypeCalcul(IREDemandeRente.CS_TYPE_CALCUL_STANDARD);
            mgr.find();

            // Si pas de demande vieillesse validée, on tente de récupérer une
            // demande vieillesse non validée.
            if (mgr.isEmpty()) {
                mgr.setForCsEtatDemandeIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL + ", "
                        + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE + ", "
                        + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);

                mgr.find();
            }

            // ---------------------------------------------------------
            // Traitement des paramètres du conjoint s'il possède une demande de
            // type Vieillesse
            // ---------------------------------------------------------
            if (!mgr.isEmpty()) {
                REDemandeRenteJointDemande elm = (REDemandeRenteJointDemande) mgr.getFirstEntity();
                REDemandeRenteVieillesse demConjoint = (REDemandeRenteVieillesse) REDemandeRente.loadDemandeRente(
                        getSession(), null, elm.getIdDemandeRente(), elm.getCsTypeDemande());

                // 9. code office AI
                this.writeChaine(cmd, "000000");

                for (int idChamp = 10; idChamp < 19; ++idChamp) {
                    this.writeChampVide(cmd);
                }

                // 19. Age flexible de la retraite.
                // Pour tous les conjoints/ex-conjoints...
                /*
                 * Si cet assuré a une demande de rente vieillesse, on la récupère et repérer si elle est soumise à une
                 * anticipation ou à un ajournement.
                 * 
                 * Codes valables pour le champ 19 : 0 = âge légal 1 = 1 année d'anticipation 2 = 2 années
                 * d'anticipation 99999999 = ajournement en cours aaaammjj = date de la révocation de l'ajournement.
                 */
                try {
                    // Insertion dans le fichier
                    if (demConjoint != null) {
                        if (demConjoint.getIsAjournementRequerant().booleanValue()) {
                            if (JadeStringUtil.isBlankOrZero(demConjoint.getDateRevocationRequerant())) {
                                this.writeEntier(cmd, "99999999");
                            } else {
                                try {
                                    this.writeEntier(cmd, PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(demConjoint
                                            .getDateRevocationRequerant()));
                                } catch (Exception e) {
                                    throw new PRACORException(e.toString());
                                }
                            }
                        } else {
                            if (!JadeStringUtil.isBlankOrZero(demConjoint.getDateRevocationRequerant())) {
                                this.writeEntier(cmd, PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(demConjoint
                                        .getDateRevocationRequerant()));
                            } else {
                                this.writeEntier(
                                        cmd,
                                        PRACORConst.csAnneAnticipationToAcor(getSession(),
                                                demConjoint.getCsAnneeAnticipation()));
                            }
                        }
                    } else {
                        this.writeChampVide(cmd);
                    }

                } catch (Exception e) {
                    this.writeChampVide(cmd);
                }
            }

            else {
                // 9. code office AI
                this.writeChaine(cmd, "000000");

                for (int idChamp = 10; idChamp < 20; ++idChamp) {
                    this.writeChampVide(cmd);
                }
            }

        } catch (Exception e) {
            getSession().addError(e.toString());
            throw new PRACORException(e.toString());
        }

    }

    private void doSaisieChampsRequerant9_23(StringBuffer cmd) throws PRACORException {

        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(adapter().getDemande().getCsTypeDemandeRente())) {
            REDemandeRenteInvalidite ri = (REDemandeRenteInvalidite) adapter().getDemande();

            // 9. code office AI
            this.writeChaine(cmd, ri.getCodeOfficeAI());

            List periodesInvalidite;
            try {
                periodesInvalidite = ri.getPeriodesInvalidite();
            } catch (Exception e) {
                throw new PRACORException(e.toString());
            }
            int counter = 0;
            for (Iterator iter = periodesInvalidite.iterator(); iter.hasNext();) {
                counter++;
                REPeriodeInvalidite element = (REPeriodeInvalidite) iter.next();
                if (counter > 1) {
                    this.writeChaineSansFinDeChamp(cmd, "/");
                }
                // 10. Début invalidité
                // 11. Fin invalidité
                // 12. Degré invalidité
                this.writeDate(cmd, element.getDateDebutInvalidite());
                this.writeDate(cmd, element.getDateFinInvalidite());
                this.writeChaine(cmd, element.getDegreInvalidite());
            }

            // 13. Genre d'infirmité, atteinte fonctionnelle
            this.writeEntierSansFinDeChamp(cmd, getSession().getCode(ri.getCsInfirmite()));
            this.writeChaine(cmd, getSession().getCode(ri.getCsAtteinte()));

            // 14. Pourcentage de réduction
            this.writeEntier(cmd, ri.getPourcentRedFauteGrave());
        } else {

            // 9. code office AI
            this.writeChaine(cmd, "000000");

            // 10. Début invalidité
            this.writeChampVide(cmd);
            // 11. Fin invalidité
            this.writeChampVide(cmd);
            // 12. Degré invalidité
            this.writeChampVide(cmd);
            // 13. Genre d'infirmité, atteinte fonctionnelle
            this.writeChampVide(cmd);
            // 14. Pourcentage de réduction
            this.writeChampVide(cmd);
        }

        // Retrieve des informations complémentaires
        PRInfoCompl infoCompl = new PRInfoCompl();
        try {
            infoCompl.setSession(getSession());
            infoCompl.setIdInfoCompl(adapter().getDemande().getIdInfoComplementaire());
            infoCompl.retrieve();
        } catch (Exception e) {
            getSession().addError(getSession().getLabel("ERREUR_INFOS_COMP"));
        }

        // 15. cas pénible AI
        if (infoCompl.getIsCasPenibleAI().booleanValue()) {
            this.writeEntier(cmd, "1");
        } else {
            this.writeEntier(cmd, "0");
        }

        // 16. Veto prestation
        this.writeEntier(cmd, PRACORConst.csVetoToAcor(getSession(), infoCompl.getCsVetoPrestation()));

        // 17. Refugie
        this.writeBoolean(cmd, infoCompl.getIsRefugie().booleanValue());

        // 18. Date prononcé dernière révision AI //Spécifique Caisse Suisse
        this.writeChampVide(cmd);

        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(adapter().getDemande().getCsTypeDemandeRente())) {
            // Cas d'ajournement de la rente viellesse

            if (((REDemandeRenteVieillesse) adapter().getDemande()).getIsAjournementRequerant().booleanValue()) {
                if (JadeStringUtil.isBlankOrZero(((REDemandeRenteVieillesse) adapter().getDemande())
                        .getDateRevocationRequerant())) {
                    this.writeEntier(cmd, "99999999");
                } else {
                    try {
                        this.writeEntier(cmd, PRDateFormater
                                .convertDate_JJxMMxAAAA_to_AAAAMMJJ(((REDemandeRenteVieillesse) adapter().getDemande())
                                        .getDateRevocationRequerant()));
                    } catch (Exception e) {
                        throw new PRACORException(e.toString());
                    }
                }

            } else {
                this.writeEntier(cmd, PRACORConst.csAnneAnticipationToAcor(getSession(),
                        ((REDemandeRenteVieillesse) adapter().getDemande()).getCsAnneeAnticipation()));
            }
        } else {
            this.writeChampVide(cmd);
        }

        // Champs 20-23
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(adapter().getDemande().getCsTypeDemandeRente())) {
            REDemandeRenteInvalidite ri = (REDemandeRenteInvalidite) adapter().getDemande();

            // Champ 20
            this.writeChampVide(cmd);

            // Champ 21
            this.writeEntier(cmd, ri.getPourcentRedNonCollaboration());

            // Champ 22
            this.writeDateAAAAMM(cmd, ri.getDateDebutRedNonCollaboration());

            // Champ 23
            this.writeDateAAAAMM(cmd, ri.getDateFinRedNonCollaboration());

        }

    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {
        switch (state) {
            case STATE_DEBUT:
                state = REFichierAssuresPrinter.STATE_REQUERANT;

                break;
            case STATE_REQUERANT:
                state = REFichierAssuresPrinter.STATE_FAMILLE;

                break;
        }
        switch (state) {
            case STATE_REQUERANT:
                membre = adapter().requerant(adapter().idTiersAssure(), adapter().getDateDeterminante());

                return true;
            case STATE_FAMILLE:
                if (assures == null) {
                    assures = adapter().famille();
                }

                if (assures.hasNext()) {
                    membre = assures.next();

                    return true;
                } else {
                    state = REFichierAssuresPrinter.STATE_FIN;
                }

                break;
        }

        return false;
    }

    protected void printDebutLigneAssure(StringBuffer cmd, Object membreO) throws PRACORException {
        if (state == REFichierAssuresPrinter.STATE_REQUERANT) {
            ISFMembreFamille membre = (ISFMembreFamille) membreO;

            this.printDebutLigneAssure(cmd, membre.getNss(), membre.getNom(), membre.getPrenom(), membre.getCsSexe(),
                    membre.getDateNaissance(), membre.getDateDeces(), membre.getCsEtatCivil(),
                    membre.getCsNationalite(), membre.getCsCantonDomicile(), membre.getPays());
        } else {
            ISFMembreFamilleRequerant membre = (ISFMembreFamilleRequerant) membreO;

            // Workaround, ACOR impose une date de naissance à tous les membres
            // de la famille.
            // Pour le conjoint inconnu, on force sa date de naissance au
            // 01.01.1970
            String dn = null;
            if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(membre.getIdMembreFamille())) {
                dn = "01.01.1970";
            } else {
                dn = membre.getDateNaissance();
            }
            this.printDebutLigneAssure(cmd, membre.getNss(), membre.getNom(), membre.getPrenom(), membre.getCsSexe(),
                    dn, membre.getDateDeces(), membre.getCsEtatCivil(), membre.getCsNationalite(),
                    membre.getCsCantonDomicile(), membre.getPays());
        }
    }

    private void printDebutLigneAssure(StringBuffer cmd, String noAVS, String nom, String prenom, String csSexe,
            String dateNaissance, String dateDeces, String csEtatCivil, String csNationalite, String csCantonDomicile,
            String codePays) throws PRACORException {
        // 1. numéro AVS de l'assuré
        this.writeAVS(cmd, noAVS);

        String nomComplet = nom + "," + prenom;
        // Supression des caractères spéciaux dans le nom de l'assure, car si
        // existant
        // le fichier batch généré va s'interrompre, car non supporté par la
        // commande DOS : ECHO
        nomComplet = nomComplet.replace('&', ' ');
        nomComplet = nomComplet.replace('<', ' ');
        nomComplet = nomComplet.replace('>', ' ');
        nomComplet = nomComplet.replace('\'', ' ');
        nomComplet = nomComplet.replace('"', ' ');

        // 2. nom et prénom de l'assuré
        this.writeChaine(cmd, nomComplet);

        // 3. sexe de l'assuré
        this.writeChaine(cmd, PRACORConst.csSexeToAcor(csSexe));

        // 4. date de naissance de l'assuré
        this.writeDate(cmd, dateNaissance);

        // 5. date de décès de l'assuré
        this.writeDate(cmd, dateDeces);

        // 6. etat civil de l'assuré (code RR)
        this.writeEntier(cmd, PRACORConst.csEtatCivilHeraToAcorForRentes(getSession(), csEtatCivil));

        String nat = "";
        // 7. code du pays de nationalité de l'assuré (code OFAS)
        if (!JadeStringUtil.isIntegerEmpty(csNationalite)) {
            nat = PRACORConst.csEtatToAcor(csNationalite);
        } else {
            nat = PRACORConst.CA_ORIGINE_INCONNU;
        }

        this.writeChaine(cmd, nat);

        // 8. code pays ou canton de domicile de l'assuré.
        /**
         * en premier on prend le canton, ne deuxieme lieu le pays. si aucun des 2.. a voir (prendre le pays du
         * requérant)
         */
        if (!JadeStringUtil.isIntegerEmpty(csCantonDomicile)) {

            // Le canton peut désigner 'Etranger', dans ce cas il faut reprendre le pays
            if (CODE_CANTON_ETRANGER.equals(csCantonDomicile)) {

                // Si le code pays est vide
                if (JadeStringUtil.isIntegerEmpty(codePays)) {
                    this.writeChaine(cmd, CANTON_ET_PAYS_INCONNU);
                } else {
                    this.writeChaine(cmd, codePays);
                }
            } else {
                this.writeChaine(cmd, PRACORConst.csCantonToAcor(csCantonDomicile));
            }
        } else {
            if (!JadeStringUtil.isIntegerEmpty(codePays)) {
                this.writeChaine(cmd, codePays);
            } else {
                try {
                    PRDemande dr = adapter().getDemande().loadDemandePrestation(null);
                    PRTiersWrapper requerant = PRTiersHelper.getTiersAdresseParId(getSession(), dr.getIdTiers());
                    this.writeChaine(cmd,
                            PRACORConst.csCantonToAcor(requerant.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON)));
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                    this.writeChaine(cmd, CANTON_ET_PAYS_INCONNU);
                }
            }
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        throw new PRACORException("Unsupported method Deprecated.");

    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {

        this.printDebutLigneAssure(cmd, membre);

        if (state == REFichierAssuresPrinter.STATE_REQUERANT) {
            doSaisieChampsRequerant9_23(cmd);
        }

        // Traitement des conjoints / enfants !!!
        else {
            // Si conjoint.....
            if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(((ISFMembreFamilleRequerant) membre)
                    .getRelationAuRequerant())) {
                doSaisieChampsConjoint9_19(cmd);
            }
            // Enfants
            else {
                // 9. code office AI
                this.writeChaine(cmd, "000000");

                for (int idChamp = 10; idChamp < 20; ++idChamp) {
                    this.writeChampVide(cmd);
                }
            }
        }
    }

}
