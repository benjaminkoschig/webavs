package globaz.ij.acor.adapter.plat;

import globaz.commons.nss.NSUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.ij.acor.adapter.IJAttestationsJoursAdapter;
import globaz.ij.api.prononces.IIJSituationProfessionnelle;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJGrandeIJ;
import globaz.ij.db.prononces.IJPetiteIJ;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJRevenu;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;
import globaz.ij.module.IJSalaireFilter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFichierIJPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /*
     * En ce qui concerne les identifiants temporaires il faudrait qu'ils soient de la forme : TMP_GUI_xn où x
     * correspond au type de l'objet en question donc... i pour une IJ, d pour un décompte, r pour un revenu, b pour un
     * bénéficiaire p pour un paiement.
     * 
     * Le n est un nombre. Donc par exemple pour une IJ TMP_GUI_i1, ou pour un décompte TMP_GUI_d1.
     */

    private static final String PREFIXE_GUI = "TMP_gui_";

    private final static int STATE_DEBUT = 10;
    private final static int STATE_FIN = 100;
    private final static int STATE_REC_ASSURE = 20;
    private final static int STATE_REC_BASE_CALCUL = 40;

    private final static int STATE_REC_DECOMPTE = 50;
    private final static int STATE_REC_IJ = 30;
    private HashMap comptes;

    // Les identifiants du fichier IJ doivent débuter par TMP_gui pour que ACOR interprête les données du HOST de
    // la même manière que si elles étaient saisie depuis le GUI. Ainsi, les champs saisis sont editable dans ACOR.
    // Utile en cas de saisie libre

    private HashMap indices;

    private Iterator sitPros;

    private int state = IJFichierIJPrinter.STATE_DEBUT;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * }
     * 
     * Crée une nouvelle instance de la classe IJFichierIJPrinter.
     * 
     * @param parent DOCUMENT ME!
     * 
     * @param nomFichier DOCUMENT ME!
     */
    public IJFichierIJPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    private IJACORPrononceAdapter adapter() {
        return (IJACORPrononceAdapter) parent;

    }

    private String cle(IJEmployeur employeur) {
        return employeur.getIdAffilie() + "_" + employeur.getIdTiers();
    }

    @Override
    public boolean hasLignes() throws PRACORException {

        switch (state) {
            case STATE_DEBUT:
                state = IJFichierIJPrinter.STATE_REC_ASSURE;
                return true;
            case STATE_REC_ASSURE:
                state = IJFichierIJPrinter.STATE_REC_IJ;
                return true;
            case STATE_REC_IJ:
                state = IJFichierIJPrinter.STATE_REC_BASE_CALCUL;
                return true;
            case STATE_REC_BASE_CALCUL:
                if (adapter().getPrononce() instanceof IJGrandeIJ) {
                    if (!situationsProfessionnelles().hasNext()) {
                        state = IJFichierIJPrinter.STATE_REC_DECOMPTE;
                    }
                } else {
                    state = IJFichierIJPrinter.STATE_REC_DECOMPTE;
                }
                return true;

            case STATE_REC_DECOMPTE:
                // this.state = IJFichierIJPrinter.STATE_FIN;
                return false;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#printLigne(java.lang.StringBuffer )
     */
    private String nssAssure() throws PRACORException {
        // Recherche du tiers requérant...

        PRTiersWrapper tw = null;
        String nssRequerant = "";
        String idTiersRequerant = "";
        try {
            idTiersRequerant = adapter().getPrononce().loadDemande(null).getIdTiers();
            tw = PRTiersHelper.getTiersParId(getSession(), idTiersRequerant);
            nssRequerant = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            nssRequerant = NSUtil.unFormatAVS(nssRequerant);

        } catch (Exception e) {
            throw new PRACORException("NSS Requérant non trouvé pour idTiers : " + idTiersRequerant);
        }
        return nssRequerant;
    }

    private StringBuffer printDecompte(StringBuffer cmd) throws PRACORException {

        try {
            // Décompte
            if (!(adapter() instanceof IJACORBaseIndemnisationAdapter)) {
                return cmd;
            }

            IJBaseIndemnisation base = ((IJACORBaseIndemnisationAdapter) adapter()).getBaseIndemnistation();
            IJIJCalculee ij = ((IJACORBaseIndemnisationAdapter) adapter()).getIjCalculee();
            IJAttestationsJoursAdapter attestationsJours = new IJAttestationsJoursAdapter(base, ij);

            // IJPrononce prononceGIJ = null;
            // prononceGIJ = new IJGrandeIJ();
            // prononceGIJ.setSession(this.getSession());
            // prononceGIJ.setIdPrononce(this.adapter().getPrononce().getIdPrononce());
            // prononceGIJ.retrieve();
            //
            // IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) this.situationsProfessionnelles()
            // .next();
            // if (sitPro == null) {
            // sitPro = new IJSituationProfessionnelle();
            // }
            //
            // IJSalaireFilter salaire = new IJSalaireFilter(this.getSession(), sitPro);
            //
            // // pour forcer un salaire mensuel (ACOR n'aime pas quand c'est pas
            // // mensuel)
            // // desactive car erreur arrondi:
            // // salaire.promouvoirSalaire(IIJSituationProfessionnelle.CS_MENSUEL);

            // 1.$r
            this.writeChaineSansFinDeChamp(cmd, "$d;");

            // 2.ID_DECOMPTE
            this.writeChaineSansFinDeChamp(cmd, IJFichierIJPrinter.PREFIXE_GUI + "d" + base.getIdBaseIndemisation()
                    + ";");

            // 3.ID_PRONONCE_IJ
            this.writeChaineSansFinDeChamp(cmd, IJFichierIJPrinter.PREFIXE_GUI + "i"
                    + adapter().getPrononce().getIdPrononce() + ";");

            // 4.Statut (1 == normal)
            this.writeChaineSansFinDeChamp(cmd, "1;");

            // 5.id du décompte remplacé
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 6.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 7.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 8.
            this.writeChaineSansFinDeChamp(cmd,
                    PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(attestationsJours.getDateDebutPeriode()) + ";");

            // 9.
            this.writeChaineSansFinDeChamp(cmd,
                    PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(attestationsJours.getDateFinPeriode()) + ";");

            // 10.nombre de jours en tant qu'interne
            if (JadeStringUtil.isBlankOrZero(attestationsJours.getNbJoursInternes())) {
                this.writeChaineSansFinDeChamp(cmd, "0;");
            } else {
                this.writeChaineSansFinDeChamp(cmd, attestationsJours.getNbJoursInternes() + ";");
            }

            // 11.nombre de jours en tant qu'externe
            if (JadeStringUtil.isBlankOrZero(attestationsJours.getNbJoursExternes())) {
                this.writeChaineSansFinDeChamp(cmd, "0;");
            } else {
                this.writeChaineSansFinDeChamp(cmd, attestationsJours.getNbJoursExternes() + ";");
            }

            // 12.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 13.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 14.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 15.
            this.writeChaineSansFinDeChamp(cmd, "FALSE;");

            // 16.

            if (!JadeStringUtil.isBlankOrZero(attestationsJours.getAttestationsJours())) {

                this.writeChaineSansFinDeChamp(cmd, attestationsJours.getAttestationsJours() + ";");

            } else {
                int nbrJoursInterne = 0;
                int nbrJoursExterne = 0;
                JADate dd = new JADate(attestationsJours.getDateDebutPeriode());
                JADate df = new JADate(attestationsJours.getDateFinPeriode());

                JACalendar cal = new JACalendarGregorian();
                long nbrJoursDansPeriode = cal.daysBetween(dd, df) + 1;

                if (!JadeStringUtil.isBlankOrZero(attestationsJours.getNbJoursInternes())) {
                    nbrJoursInterne = Integer.parseInt(attestationsJours.getNbJoursInternes());
                }
                if (!JadeStringUtil.isBlankOrZero(attestationsJours.getNbJoursExternes())) {
                    nbrJoursExterne = Integer.parseInt(attestationsJours.getNbJoursExternes());
                }
                String detailJour = "";

                for (int i = 0; i < nbrJoursInterne; i++) {
                    detailJour += "1";
                }
                for (int i = 0; i < nbrJoursExterne; i++) {
                    detailJour += "2";
                }
                long diff = nbrJoursDansPeriode - nbrJoursExterne - nbrJoursInterne;

                for (int i = 0; i < diff; i++) {
                    detailJour += ".";
                }
                this.writeChaineSansFinDeChamp(cmd, detailJour + ";");

            }

            if (JadeStringUtil.isBlankOrZero(attestationsJours.getNbJoursInterruption())) {
                // 17.
                this.writeChaineSansFinDeChamp(cmd, "0;");
                // 18.
                this.writeChaineSansFinDeChamp(cmd, "0;");
            } else {
                // 17.
                this.writeChaineSansFinDeChamp(cmd, attestationsJours.getNbJoursInterruption() + ";");
                // 18.
                this.writeChaineSansFinDeChamp(cmd,
                        PRACORConst.csMotifInterruptionToAcor(getSession(), base.getCsMotifInterruption()) + ";");
            }

            // 19.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 20.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 21.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 22.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 23.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 24.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 25.
            this.writeChaineSansFinDeChamp(cmd, "0;");

            // 26.
            this.writeChaineSansFinDeChamp(cmd, "FALSE;");

            // 27.
            this.writeChaineSansFinDeChamp(cmd, "FALSE;");

            // 28.
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // 29.
            this.writeChaineSansFinDeChamp(cmd, ".;");
        } catch (Exception e) {
            throw new PRACORException(e.toString());
        }

        return cmd;
    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        switch (state) {
            case STATE_REC_ASSURE:
                // 1. assuré
                this.writeChaineSansFinDeChamp(cmd, "$a;");
                // 2. NSS
                this.writeChaineSansFinDeChamp(cmd, nssAssure() + ";");
                this.writeChaineSansFinDeChamp(cmd, "0;0;0;");
                break;
            case STATE_REC_IJ:
                cmd = printRECIJ(cmd);
                break;
            case STATE_REC_BASE_CALCUL:
                cmd = printRECBaseCalcul(cmd);
                break;
            case STATE_REC_DECOMPTE:
                cmd = printDecompte(cmd);
                break;
        }
    }

    private StringBuffer printRECBaseCalcul(StringBuffer cmd) throws PRACORException {

        try {
            // Grande IJ
            if (adapter().getPrononce().isGrandeIJ()) {
                IJPrononce prononceGIJ = null;
                prononceGIJ = new IJGrandeIJ();
                prononceGIJ.setSession(getSession());
                prononceGIJ.setIdPrononce(adapter().getPrononce().getIdPrononce());
                prononceGIJ.retrieve();

                IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) situationsProfessionnelles().next();
                if (sitPro == null) {
                    sitPro = new IJSituationProfessionnelle();
                }

                IJSalaireFilter salaire = new IJSalaireFilter(getSession(), sitPro);

                // pour forcer un salaire mensuel (ACOR n'aime pas quand c'est pas
                // mensuel)
                // desactive car erreur arrondi:
                // salaire.promouvoirSalaire(IIJSituationProfessionnelle.CS_MENSUEL);

                // 1.$r
                this.writeChaineSansFinDeChamp(cmd, "$r;");

                // 2.ID_REV
                this.writeChaineSansFinDeChamp(cmd,
                        IJFichierIJPrinter.PREFIXE_GUI + "r" + sitPro.getIdSituationProfessionnelle() + ";");

                // 3.ID_IJ
                this.writeChaineSansFinDeChamp(cmd, IJFichierIJPrinter.PREFIXE_GUI + "i" + prononceGIJ.getIdPrononce()
                        + ";");

                if ((sitPro == null) || sitPro.isNew()) {
                    // 4.Revenu
                    this.writeChaineSansFinDeChamp(cmd, ".;");
                    // 5.Année correspondante
                    this.writeChaineSansFinDeChamp(cmd, ".;");
                    // 6.Type
                    this.writeChaineSansFinDeChamp(cmd, ".;");
                    // 7.Nbr. heures/sem
                    this.writeChaineSansFinDeChamp(cmd, ".;");
                    // 8. no affilié
                    this.writeChaineSansFinDeChamp(cmd, ".;");
                    // 9. nom employeur
                    this.writeChaineSansFinDeChamp(cmd, ".;");

                } else {
                    // 4.Revenu
                    this.writeChaineSansFinDeChamp(cmd, salaire.getMontantSalaireArrondi() + ";");
                    // 5.Année correspondante
                    this.writeChaineSansFinDeChamp(cmd, sitPro.getAnneeCorrespondante() + ";");
                    // 6.Type
                    this.writeChaineSansFinDeChamp(cmd,
                            PRACORConst.csPeriodiciteSalaireIJToAcor(getSession(), salaire.getCsPeriodiciteSalaire())
                                    + ";");
                    // 7.Nbr. heures/sem
                    this.writeChaineSansFinDeChamp(cmd, salaire.getNombreHeuresSemaines() + ";");
                    // 8. no affilié
                    this.writeChaineSansFinDeChamp(cmd, sitPro.loadEmployeur().loadNumero() + ";");
                    // 9. nom employeur
                    this.writeChaineSansFinDeChamp(cmd, sitPro.loadEmployeur().loadNom());

                }

                // suffixer avec un indice s'il y a plusieurs contrats avec cet
                // employeur.
                String cle = cle(sitPro.loadEmployeur());
                Integer compte = (Integer) comptes.get(cle);

                if ((compte != null) && (compte.intValue() > 1)) {
                    if (indices == null) {
                        indices = new HashMap();
                    }

                    Integer indice = (Integer) indices.get(cle);

                    if (indice == null) {
                        indice = new Integer(1);
                    } else {
                        indice = new Integer(indice.intValue() + 1);
                    }

                    indices.put(cle, indice);

                    this.writeChaineSansFinDeChamp(cmd, " (" + indice.intValue() + ");");
                } else {
                    this.writeChaineSansFinDeChamp(cmd, ";");
                }

            }
            // Petite IJ
            else {

                IJPetiteIJ prononcePIJ = null;
                prononcePIJ = new IJPetiteIJ();
                prononcePIJ.setSession(getSession());
                prononcePIJ.setIdPrononce(adapter().getPrononce().getIdPrononce());
                prononcePIJ.retrieve();

                IJRevenu revenu = prononcePIJ.loadRevenu();
                // Dernier revenu ou manque déterminant
                if ((revenu == null) || JadeStringUtil.isBlankOrZero(revenu.getRevenu())) {
                    return cmd;
                }

                // 1.$r
                this.writeChaineSansFinDeChamp(cmd, "$r;");

                // 2.ID_REV TMP_gui_r1 -- il faut obligatoirement une valeur après le '_r' sans quoi, le calcul est ok
                // Mais la sauvegarde du cas en fichier .acor ne permet plus son édition.
                this.writeChaineSansFinDeChamp(cmd, IJFichierIJPrinter.PREFIXE_GUI + "r1;");

                // 3.ID_IJ
                this.writeChaineSansFinDeChamp(cmd, IJFichierIJPrinter.PREFIXE_GUI + "i" + prononcePIJ.getIdPrononce()
                        + ";");

                IJSalaireFilter salaire = new IJSalaireFilter(getSession(), revenu);
                // 4.Revenu
                if (JadeStringUtil.isBlankOrZero(salaire.getMontantSalaireArrondi())) {
                    this.writeChaineSansFinDeChamp(cmd, ".;");
                } else {
                    this.writeChaineSansFinDeChamp(cmd, salaire.getMontantSalaireArrondi() + ";");
                }

                // 5.Année correspondante
                this.writeChaineSansFinDeChamp(cmd, revenu.getAnnee() + ";");
                // 6.Type
                this.writeChaineSansFinDeChamp(cmd,
                        PRACORConst.csPeriodiciteSalaireIJToAcor(getSession(), salaire.getCsPeriodiciteSalaire()) + ";");
                // 7.Nbr. heures/sem
                this.writeChaineSansFinDeChamp(cmd, salaire.getNombreHeuresSemaines() + ";");
                // 8. no affilié
                this.writeChaineSansFinDeChamp(cmd, ".;");
                // 9. nom employeur
                this.writeChaineSansFinDeChamp(cmd, ".;");
            }
        } catch (Exception e) {
            throw new PRACORException(e.toString());
        }
        return cmd;
    }

    private StringBuffer printRECIJ(StringBuffer cmd) throws PRACORException {

        IJPrononce prononceType = null;
        try {
            if (adapter().getPrononce().isGrandeIJ()) {
                prononceType = new IJGrandeIJ();
                prononceType.setSession(getSession());
                prononceType.setIdPrononce(adapter().getPrononce().getIdPrononce());
                prononceType.retrieve();
            } else {
                prononceType = new IJPetiteIJ();
                prononceType.setSession(getSession());
                prononceType.setIdPrononce(adapter().getPrononce().getIdPrononce());
                prononceType.retrieve();

            }

            boolean isGrandeIJ = false;

            this.writeChaineSansFinDeChamp(cmd, "$i;");

            this.writeChaineSansFinDeChamp(cmd, IJFichierIJPrinter.PREFIXE_GUI + "i" + prononceType.getIdPrononce()
                    + ";");

            if (prononceType.isGrandeIJ()) {
                this.writeChaineSansFinDeChamp(cmd, "1;");
                isGrandeIJ = true;
            } else {
                this.writeChaineSansFinDeChamp(cmd, "2;");
            }
            this.writeChaineSansFinDeChamp(cmd, prononceType.getOfficeAI() + ";");

            String datePrononce;
            try {
                datePrononce = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prononceType.getDatePrononce());
            } catch (JAException e) {
                datePrononce = "31.12.2999";
            }
            this.writeChaineSansFinDeChamp(cmd, datePrononce + ";");

            this.writeChaineSansFinDeChamp(cmd, prononceType.getNoDecisionAI() + ";");
            // Etablissement, hopital, médecion
            this.writeChaineSansFinDeChamp(cmd, "Agent d'exécution;");
            // Status : 1 = valide
            this.writeChaineSansFinDeChamp(cmd, "1;");
            // Id de l'IJ remplacée
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // Genre de réadaptation
            this.writeChaineSansFinDeChamp(cmd, getSession().getCode(prononceType.getCsGenre()) + ";");
            // Date début / fin
            String dateDebP;
            String dateDebF = "99999999";
            try {
                dateDebP = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prononceType.getDateDebutPrononce());
                if (!JadeStringUtil.isBlankOrZero(prononceType.getDateFinPrononce())) {
                    dateDebF = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(prononceType.getDateFinPrononce());
                }
            } catch (JAException e) {
                dateDebP = "31.12.2999";
                dateDebF = "31.12.2999";
            }

            this.writeChaineSansFinDeChamp(cmd, dateDebP + ";");
            this.writeChaineSansFinDeChamp(cmd, dateDebF + ";");

            // Revenu déterminant
            // TODO : A calculer... ???
            this.writeChaineSansFinDeChamp(cmd, "." + ";");
            // Date du revenu déterminant
            this.writeChaineSansFinDeChamp(cmd, dateDebP + ";");

            // Revenu durant la réadaptation (mensuel)
            IJRevenu revenuReadaptation = adapter().getPrononce().loadRevenuReadaptation(null);
            if (revenuReadaptation == null) {
                revenuReadaptation = new IJRevenu();
            }
            IJSalaireFilter salaireREA = new IJSalaireFilter(getSession(), revenuReadaptation);

            salaireREA.promouvoirSalaire(IIJSituationProfessionnelle.CS_MENSUEL);
            if (JadeStringUtil.isBlankOrZero(salaireREA.getMontantSalaireArrondi())) {
                this.writeChaineSansFinDeChamp(cmd, ".;");
            } else {
                this.writeChaineSansFinDeChamp(cmd, salaireREA.getMontantSalaireArrondi() + ";");
            }
            // Montant de base (sans indemnité enfant)
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // Montant de l'indemnité pour enfant
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // Nombre d'enfant comptant l'indemnité
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // Revenu journalier durant la réadaptation
            this.writeChaineSansFinDeChamp(cmd, ".;");
            String cs;
            // Formation
            if (isGrandeIJ) {
                this.writeChaineSansFinDeChamp(cmd, ".;");
            } else {
                cs = ((IJPetiteIJ) prononceType).getCsSituationAssure();
                this.writeChaineSansFinDeChamp(cmd, getSession().getCode(cs) + ";");
            }

            // Status
            cs = prononceType.getCsStatutProfessionnel();
            this.writeChaineSansFinDeChamp(cmd, getSession().getCode(cs) + ";");
            // Demi IJ de l'AC
            this.writeChaineSansFinDeChamp(cmd, prononceType.getDemiIJAC() + ";");
            // Deduction pour rente AI
            this.writeChaineSansFinDeChamp(cmd, "0;");

            try {
                if (!prononceType.getMontantGarantiAAReduit().booleanValue()) {
                    // garantie AA non réduite
                    this.writeChaineSansFinDeChamp(cmd, prononceType.getMontantGarantiAA() + ";");
                    // garantie AA réduite
                    this.writeChaineSansFinDeChamp(cmd, "0;");

                } else {
                    // garantie AA non réduite
                    this.writeChaineSansFinDeChamp(cmd, "0;");
                    // garantie AA réduite
                    this.writeChaineSansFinDeChamp(cmd, prononceType.getMontantGarantiAA() + ";");
                }
            } catch (Exception e) {
                // Ne doit jamais arrivé.
                e.printStackTrace();
                // garantie AA non réduite
                this.writeChaineSansFinDeChamp(cmd, "0;");
                // garantie AA réduite
                this.writeChaineSansFinDeChamp(cmd, "0;");
            }
            // montant jrn de l'indemnité ext.
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // Déduction des frais d'entretien
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // montant jrn de l'indemnité int.
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // garantie révision 3, 4
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // type de saisie 1 ou 2 (1 = expert; 2 = saisie libre)
            this.writeChaineSansFinDeChamp(cmd, "1;");

            // Toujours vide, utile lors du calcul du décompte uniquement.
            // Canton IS
            this.writeChaineSansFinDeChamp(cmd, ".;");
            // Taux IS
            this.writeChaineSansFinDeChamp(cmd, ".;");

            // cs = prononceType.getCsCantonImpositionSource();
            // if (JadeStringUtil.isBlankOrZero(cs)) {
            // this.writeChaineSansFinDeChamp(cmd, ".;");
            // this.writeChaineSansFinDeChamp(cmd, ".;");
            // } else {
            // this.writeChaineSansFinDeChamp(cmd, this.getSession().getCode(cs) + ";");
            // this.writeChaineSansFinDeChamp(cmd, prononceType.getTauxImpositionSource() + ";");
            // }

        } catch (Exception e) {
            throw new PRACORException(e.toString());
        }
        return cmd;
    }

    private Iterator situationsProfessionnelles() throws PRACORException {
        if (comptes == null) {
            comptes = new HashMap();

            try {
                IJSituationProfessionnelleManager mgr = new IJSituationProfessionnelleManager();

                mgr.setISession(getSession());
                mgr.setForIdPrononce(adapter().getPrononce().getIdPrononce());
                mgr.find();

                // reperer les contrats multiples
                for (Iterator sitPros = mgr.iterator(); sitPros.hasNext();) {
                    IJSituationProfessionnelle sitPro = (IJSituationProfessionnelle) sitPros.next();
                    String cle = cle(sitPro.loadEmployeur());
                    Integer compte = (Integer) comptes.get(cle);

                    if (compte != null) {
                        compte = new Integer(compte.intValue() + 1);
                    } else {
                        compte = new Integer(1);
                    }

                    comptes.put(cle, compte);
                }

                sitPros = mgr.iterator();
            } catch (Exception e) {
                throw new PRACORException("Impossible de charger les situations professionnelles", e);
            }
        }

        return sitPros;
    }
}
