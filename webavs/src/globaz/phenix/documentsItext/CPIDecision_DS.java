package globaz.phenix.documentsItext;

// ITEXT
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationListViewBean;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAgenceCommunale;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.toolbox.CPToolBox;
import java.math.BigDecimal;
import java.util.Iterator;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * Ce DS (DataSource) représente le data source pour les contributions dues Pour iText, cette classe gère l'impression
 * de la partie [detail] du document Lié au document CP_DECISION_xxx.jasper
 * 
 * Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class CPIDecision_DS extends CPCotisationListViewBean implements JRDataSource, globaz.phenix.util.Constante {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String chfCol1 = "";
    private String chfCol2 = "";
    private String chfTot1 = "";
    private String chfTot2 = "";
    private globaz.osiris.db.comptes.CACompteAnnexe compteAnnexe = null;
    private Iterator<?> container = null;
    private double cotiAnnuel = 0.0;
    private double cotiFacture = 0.0;
    // Champ du document
    private String cotiLibelle = "";
    private double cotiPeriodique = 0.0;
    private CPDecisionAgenceCommunale decision = null;
    public ICTDocument document = null;
    public ICTDocument[] documents = null;
    private globaz.phenix.db.principale.CPCotisation entity = null;
    private String etat = "";
    private int idLangue = 0;
    private String idRubrique = "";
    String langue = "";
    private String periodiciteAffiliation = "";
    public ICTDocument res[] = null;

    public CPIDecision_DS() {
        super();
    }

    /**
     * Copiez la méthode tel quel, permet la copy de l'objet Date de création : (01.04.2003 14:45:18)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Formate le texte. Remplace un {0} par la variable passée en paramètre
     * 
     * @param paragraphe
     * @return
     * @throws Exception
     */
    public StringBuffer format(StringBuffer paragraphe, String varTemp) {
        StringBuffer res = new StringBuffer("");
        String chaineModifiee = paragraphe.toString();
        ;
        int index1 = chaineModifiee.indexOf("{");
        int index2 = chaineModifiee.indexOf("}");
        if ((index1 != 1) && (index2 != -1)) {
            String chaineARemplacer = chaineModifiee.substring(index1, index2 + 1);
            // remplacement de la variable par sa valeur (varTemp)
            if (varTemp == "") {
                varTemp = " ";
            }
            res.append(CPToolBox.replaceString(paragraphe.toString(), chaineARemplacer, varTemp));
        } else {
            res.append(paragraphe.toString());
        }
        return res;
    }

    public String getChfCol1() {
        return chfCol1;
    }

    public String getChfCol2() {
        return chfCol2;
    }

    public String getChfTot1() {
        return chfTot1;
    }

    public String getChfTot2() {
        return chfTot2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.07.2003 13:53:36)
     * 
     * @return globaz.osiris.db.comptes.CACompteAnnexe
     */
    public globaz.osiris.db.comptes.CACompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * @return
     */
    public CPDecisionAgenceCommunale getDecision() {
        return decision;
    }

    /**
     * @return
     */
    public String getEtat() {
        return etat;
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     */
    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        try {
            if (jrField.getName().equals("LIBELLE_COTI")) {
                return cotiLibelle;
            }
            if (jrField.getName().equals("COTI_ANNUELLE")) {
                return new Double(cotiAnnuel);
            }
            if (jrField.getName().equals("COTI_PERIODIQUE")) {
                return new Double(cotiPeriodique);
            }
            // !!!! Aller chercher le montant déjà facturé en comptabilité
            // (OSIRIS) !!!!
            if (jrField.getName().equals("COTI_FACTUREE")) {
                return new Double(cotiFacture);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            throw new JRException(e);
        }
        return null;
    }

    /**
     * Returns the idLangue.
     * 
     * @return String
     */
    public int getIdLangue() {
        return idLangue;
    }

    public String getLangue() {
        return langue;
    }

    /**
     * Retourne le montant déjà facturé pour une assurance Date de création : (02.05.2003 11:59:50)
     * 
     * @parm java.lang.String dateImpression
     * @return java.lang.String
     */
    public java.lang.String getMontantDejaFacture(CPDecision decision) throws Exception {
        // Rubrique
        float montantDejaCotisePourSalarie = 0;
        if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(entity.getGenreCotisation())
                || CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(entity.getGenreCotisation())) {
            // Si salarié dans la même année
            CPDonneesBase donneesBase = new CPDonneesBase();
            donneesBase.setSession(getSession());
            donneesBase.setIdDecision(decision.getIdDecision());
            donneesBase.retrieve();
            if ((donneesBase != null) && !donneesBase.isNew()) {
                if (!JadeStringUtil.isBlank(donneesBase.getCotisationSalarie())) {
                    montantDejaCotisePourSalarie = Float.parseFloat(JANumberFormatter.deQuote(donneesBase
                            .getCotisationSalarie()));
                    if (CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(entity.getGenreCotisation())) {
                        // Calculer les frais sur la coti salarié et l'ajouter
                        float taux = Float.parseFloat(JANumberFormatter.deQuote(entity.getTaux()));
                        montantDejaCotisePourSalarie = (new FWCurrency(montantDejaCotisePourSalarie).floatValue() * taux) / 100;
                        montantDejaCotisePourSalarie = JANumberFormatter.round(montantDejaCotisePourSalarie, 0.05, 2,
                                JANumberFormatter.NEAR);
                    }
                }
            }
        }// Extraction du compte annexe
        if (getCompteAnnexe() != null) {
            String montantFac = "";
            // Si la décision est comptabilisé => prendre le montant qui
            // correspont à la facture (stocké au moment de la facturation)
            // Ceci permet de réimprimer une décision. Sinon prendre le montant
            // en compta.
            if (getEtat().equalsIgnoreCase(CPDecision.CS_FACTURATION)
                    || getEtat().equalsIgnoreCase(CPDecision.CS_PB_COMPTABILISATION)) {
                montantFac = entity.getMontantFacture();
            } else {
                if (CPDecision.CS_REDUCTION.equalsIgnoreCase(getDecision().getTypeDecision())) {
                    // PO 7085
                    CPDecision decEnCours = CPDecision._returnDecisionBase(getSession(), this.decision.getIdDecision());
                    if (decEnCours != null) {
                        montantFac = CPCotisation._returnCotisation(getSession(), decEnCours.getIdDecision(),
                                entity.getGenreCotisation()).getMontantAnnuel();
                    }
                } else if (getCompteAnnexe() != null) {
                    montantFac = CPToolBox.rechMontantFacture(getSession(), null,
                            getCompteAnnexe().getIdCompteAnnexe(), idRubrique, getDecision().getAnneeDecision());
                }
            }
            float montantDejaFacture = Float.parseFloat(JANumberFormatter.deQuote(montantFac));
            if (getEtat().equalsIgnoreCase(CPDecision.CS_FACTURATION)
                    || getEtat().equalsIgnoreCase(CPDecision.CS_PB_COMPTABILISATION)) {
                return (montantDejaFacture) + "";
            } else {
                return (montantDejaFacture + montantDejaCotisePourSalarie) + "";
            }
        } else {
            return "0";
        }
    }

    /**
     * Retourne le montant de cotisation pour la période de décision Date de création : (10.09.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantPeriode() {
        return entity.getMontantAnnuel();
    }

    /**
     * Returns the periodiciteAffiliation.
     * 
     * @return String
     */
    public String getPeriodiciteAffiliation() {
        return periodiciteAffiliation;
    }

    /**
     * Récupère les textes du catalogue de texte
     * 
     * @author: sel Créé le : 13 déc. 06
     * @return
     * @throws Exception
     */
    public StringBuffer getTexte(int niveau, int position, String vartTemp) {
        StringBuffer resString = new StringBuffer("");
        try {
            if (document == null) {
                getSession().addError(getSession().getLabel("PAS_TEXTE_DEFAUT"));
            } else {
                ICTListeTextes listeTextes = documents[0].getTextes(niveau);
                resString.append(listeTextes.getTexte(position));
            }
        } catch (Exception e3) {
            getSession().addError(getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return format(resString, vartTemp);
    }

    /**
     * Copier le contenu de cette méthode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entité.
     */
    @Override
    public boolean next() throws JRException {
        entity = null;
        try {
            if (container == null) {
                this.find(0);
                container = getContainer().iterator();
            }
            // lit le nouveau entity
            if ((container != null) && container.hasNext()) {
                entity = (CPCotisation) container.next();
                if (!prepareRow(entity)) {
                    return next();
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }

    private boolean prepareRow(CPCotisation entity) {
        try {
            AFCotisation coti = new AFCotisation();
            coti.setSession(getSession());
            coti.setCotisationId(entity.getIdCotiAffiliation());
            coti.retrieve();
            // PO 2434 -> On remplit la périodicité avec celle de la première
            // cotisation créé
            // donc avs si il y a puis FAD etc...
            if (JadeStringUtil.isEmpty(getPeriodiciteAffiliation())) {
                setPeriodiciteAffiliation(coti.getPeriodicite());
            }
            // Si coti = cotisation AF => rechercher cotisation CGS général et
            // cotisation CGS autre pour cumuler les montants
            CPCotisation cotiCGSGeneral = new CPCotisation();
            CPCotisation cotiCGSAutre = new CPCotisation();
            if (entity.getGenreCotisation().equalsIgnoreCase(CodeSystem.TYPE_ASS_COTISATION_AF)) {
                // Recherche cotiCGSGénéral
                cotiCGSGeneral.setSession(getSession());
                cotiCGSGeneral.setAlternateKey(2);
                cotiCGSGeneral.setIdDecision(entity.getIdDecision());
                cotiCGSGeneral.setGenreCotisation(CodeSystem.TYPE_ASS_CPS_GENERAL);
                cotiCGSGeneral.retrieve();
                // Recherche cotiCGSAutre
                cotiCGSAutre.setSession(getSession());
                cotiCGSAutre.setAlternateKey(2);
                cotiCGSAutre.setIdDecision(entity.getIdDecision());
                cotiCGSAutre.setGenreCotisation(CodeSystem.TYPE_ASS_CPS_AUTRE);
                cotiCGSAutre.retrieve();
            }
            String taux = " ";
            String periodeCoti = " ";
            String langue = AFUtil.toLangueIso(decision.getLangue());
            // retourne chaque champ
            if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(entity.getTaux()))) {
                BigDecimal varTaux = new BigDecimal(JANumberFormatter.deQuote(entity.getTaux()));
                if (!cotiCGSGeneral.isNew()) {
                    // Cumul CGS Général
                    varTaux = varTaux.add(new BigDecimal(JANumberFormatter.deQuote(cotiCGSGeneral.getTaux())));
                }
                if (!cotiCGSAutre.isNew()) {
                    // Cumul CGS Autre
                    varTaux = varTaux.add(new BigDecimal(JANumberFormatter.deQuote(cotiCGSAutre.getTaux())));
                }
                taux = " (" + varTaux + "%)";
            }
            // Afficher la période si celle-ci est différente de la décision
            // mais appartenant à l'année de décision
            int anneeDecision = JACalendar.getYear(decision.getDebutDecision());
            int anneeDebutCotisation = JACalendar.getYear(entity.getDebutCotisation());
            int anneeFinCotisation = JACalendar.getYear(entity.getFinCotisation());
            if ((anneeDecision == anneeDebutCotisation) || (anneeDecision == anneeFinCotisation)) {
                if (!entity.getDebutCotisation().equalsIgnoreCase(decision.getDebutDecision())
                        || !entity.getFinCotisation().equals(decision.getFinDecision())) {
                    String textePeriode = getSession().getApplication().getLabel("TEXT_DEBUTPERIODE", langue);
                    if (!JadeStringUtil.isEmpty(textePeriode)) {
                        periodeCoti = " " + textePeriode + " ";
                    }
                    periodeCoti += JACalendar.getMonth(entity.getDebutCotisation()) + "-"
                            + JACalendar.format(entity.getFinCotisation(), JACalendar.FORMAT_MMsYYYY);
                }
            }
            // Mettre la période de coti en gras
            if (!JadeStringUtil.isEmpty(periodeCoti)) {
                periodeCoti = CPToolBox.formatGras(getSession(), periodeCoti);
            }
            cotiLibelle = "";
            cotiAnnuel = 0.0;
            cotiPeriodique = 0.0;
            cotiFacture = 0.0;
            if (!coti.isNew()) {
                idRubrique = coti.getAssurance().getRubriqueId();
                // Cotissation Libelle
                if (entity.getCotisationMinimum().equals(Boolean.TRUE)
                        && CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(entity.getGenreCotisation())) {
                    String libelleCotiMin = getTexte(2, 6, " ").toString();
                    cotiLibelle = getTexte(2, 1, libelleCotiMin).toString();
                } else {
                    switch (getIdLangue()) {
                        case CPIDecision_Doc.CS_ALLEMAND:
                            cotiLibelle = getTexte(2, 1, coti.getAssurance().getAssuranceLibelleAl()).toString();
                            break;
                        case CPIDecision_Doc.CS_ITALIEN:
                            cotiLibelle = getTexte(2, 1, coti.getAssurance().getAssuranceLibelleIt()).toString();
                            break;
                        default:
                            cotiLibelle = getTexte(2, 1, coti.getAssurance().getAssuranceLibelleFr()).toString();
                    }
                }
                cotiLibelle = format(new StringBuffer(cotiLibelle), taux).toString();
                cotiLibelle = format(new StringBuffer(cotiLibelle), periodeCoti).toString();
                boolean montantPeriodique = true;
                CPDecision decision = new CPDecision();
                decision.setSession(getSession());
                decision.setIdDecision(entity.getIdDecision());
                decision.retrieve();
                if ((decision != null) && !decision.isNew()) {
                    // Annuler le if suivant à cause de client qui encodent des
                    // acomptes périodiques - PO 4242
                    // if (Integer.parseInt(decision.getAnneeDecision()) <
                    // JACalendar.getYear(JACalendar.todayJJsMMsAAAA())) {
                    // montantPeriodique = false;
                    // }
                }
                // if (decision.getFacturation().equals(Boolean.TRUE)) {
                // Cotisation Annuelle
                if (JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(entity.getMontantAnnuel()))
                        || decision.getSpecification().equals(CPDecision.CS_FRANCHISE)) {
                    cotiAnnuel = 0.0;
                    cotiPeriodique = 0.0;
                } else {
                    cotiAnnuel = Double.parseDouble(JANumberFormatter.deQuote(getMontantPeriode()));
                    if (!cotiCGSGeneral.isNew()) {
                        // Cumul CGS Général
                        cotiAnnuel = new BigDecimal(cotiAnnuel
                                + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSGeneral.getMontantAnnuel())))
                                .doubleValue();
                    }
                    if (!cotiCGSAutre.isNew()) {
                        // Cumul CGS Autre
                        cotiAnnuel = new BigDecimal(cotiAnnuel
                                + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSAutre.getMontantAnnuel())))
                                .doubleValue();
                    }
                    if (montantPeriodique) {
                        // Cotisation Periodique
                        if (coti.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                            cotiPeriodique = Double.parseDouble(JANumberFormatter.deQuote(entity
                                    .getMontantTrimestriel()));
                            if (!cotiCGSGeneral.isNew()) {
                                // Cumul CGS Général
                                cotiPeriodique = new BigDecimal(cotiPeriodique
                                        + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSGeneral
                                                .getMontantTrimestriel()))).doubleValue();
                            }
                            if (!cotiCGSAutre.isNew()) {
                                // Cumul CGS Autre
                                cotiPeriodique = new BigDecimal(cotiPeriodique
                                        + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSAutre
                                                .getMontantTrimestriel()))).doubleValue();
                            }
                        } else if (coti.getPeriodicite().equalsIgnoreCase("802003")) {
                            cotiPeriodique = Double
                                    .parseDouble(JANumberFormatter.deQuote(entity.getMontantSemestriel()));
                            if (!cotiCGSGeneral.isNew()) {
                                // Cumul CGS Général
                                cotiPeriodique = new BigDecimal(cotiPeriodique
                                        + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSGeneral
                                                .getMontantSemestriel()))).doubleValue();
                            }
                            if (!cotiCGSAutre.isNew()) {
                                // Cumul CGS Autre
                                cotiPeriodique = new BigDecimal(cotiPeriodique
                                        + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSAutre
                                                .getMontantSemestriel()))).doubleValue();
                            }
                        } else if (coti.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) {
                            // Annuelle (déjà indiquée dans la colonne
                            // précédente)
                            cotiPeriodique = Double.parseDouble(JANumberFormatter.deQuote(entity.getMontantAnnuel()));
                            if (!cotiCGSGeneral.isNew()) {
                                // Cumul CGS Général
                                cotiPeriodique = new BigDecimal(cotiPeriodique
                                        + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSGeneral
                                                .getMontantAnnuel()))).doubleValue();
                            }
                            if (!cotiCGSAutre.isNew()) {
                                // Cumul CGS Autre
                                cotiPeriodique = new BigDecimal(
                                        cotiPeriodique
                                                + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSAutre
                                                        .getMontantAnnuel()))).doubleValue();
                            }
                        } else {
                            cotiPeriodique = Double.parseDouble(JANumberFormatter.deQuote(entity.getMontantMensuel()));
                            if (!cotiCGSGeneral.isNew()) {
                                // Cumul CGS Général
                                cotiPeriodique = new BigDecimal(cotiPeriodique
                                        + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSGeneral
                                                .getMontantMensuel()))).doubleValue();
                            }
                            if (!cotiCGSAutre.isNew()) {
                                // Cumul CGS Autre
                                cotiPeriodique = new BigDecimal(
                                        cotiPeriodique
                                                + Double.parseDouble(JANumberFormatter.deQuote(cotiCGSAutre
                                                        .getMontantMensuel()))).doubleValue();
                            }
                        }
                    }
                }
                // !!!! non complémentaire: Aller chercher le montant déjà
                // facturé en comptabilité (OSIRIS) !!!!
                if (!getDecision().getComplementaire().booleanValue()
                        && ((Integer.parseInt(decision.getAnneeDecision()) < JACalendar.getYear(JACalendar
                                .todayJJsMMsAAAA())) || CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(entity
                                .getPeriodicite()))) {
                    String mntDejaFact = JANumberFormatter.deQuote(getMontantDejaFacture(decision));
                    cotiFacture = 0.0;
                    if (!"".equals(mntDejaFact)) {
                        cotiFacture = Double.parseDouble(mntDejaFact);
                        if (!cotiCGSGeneral.isNew()) {
                            // Cumul CGS Général
                            if (getEtat().equalsIgnoreCase(CPDecision.CS_FACTURATION)
                                    || getEtat().equalsIgnoreCase(CPDecision.CS_PB_COMPTABILISATION)) {
                                // PO 1952
                                entity.setMontantFacture(cotiCGSGeneral.getMontantFacture());
                            } else {
                                AFCotisation cotiAfCPSGen = new AFCotisation();
                                cotiAfCPSGen.setSession(getSession());
                                cotiAfCPSGen.setCotisationId(cotiCGSGeneral.getIdCotiAffiliation());
                                cotiAfCPSGen.retrieve();
                                idRubrique = cotiAfCPSGen.getAssurance().getRubriqueId();
                            }
                            cotiFacture = new BigDecimal(cotiFacture
                                    + Double.parseDouble(JANumberFormatter.deQuote(getMontantDejaFacture(decision))))
                                    .doubleValue();
                        }
                        if (!cotiCGSAutre.isNew()) {
                            // Cumul CGS Autre
                            if (getEtat().equalsIgnoreCase(CPDecision.CS_FACTURATION)
                                    || getEtat().equalsIgnoreCase(CPDecision.CS_PB_COMPTABILISATION)) {
                                // PO 1952
                                entity.setMontantFacture(cotiCGSAutre.getMontantFacture());
                            } else {
                                AFCotisation cotiAfCPSAut = new AFCotisation();
                                cotiAfCPSAut.setSession(getSession());
                                cotiAfCPSAut.setCotisationId(cotiCGSAutre.getIdCotiAffiliation());
                                cotiAfCPSAut.retrieve();
                                idRubrique = cotiAfCPSAut.getAssurance().getRubriqueId();
                            }
                            cotiFacture = new BigDecimal(cotiFacture
                                    + Double.parseDouble(JANumberFormatter.deQuote(getMontantDejaFacture(decision))))
                                    .doubleValue();
                        }
                    }
                } else {
                    cotiFacture = 0.0d;
                }
                /*
                 * } else { this.cotiAnnuel = 0.0; this.cotiPeriodique = 0.0; this.cotiFacture = 0.0d; }
                 */
            } else {
                getSession().addWarning(
                        getSession().getLabel("CP_MSG_0208") + " - " + decision.getNumeroAffilie() + " - "
                                + getDecision().getAnneeDecision());
            }
            // PO 8752 - Ne pas imprimer lorsque l'AC2 est à zéro
            if (CodeSystem.TYPE_ASS_COTISATION_AC2.equalsIgnoreCase(entity.getGenreCotisation()) && (cotiFacture == 0)
                    && (cotiAnnuel == 0)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return false;
    }

    public void setChfCol1(String chfCol1) {
        this.chfCol1 = chfCol1;
    }

    public void setChfCol2(String chfCol2) {
        this.chfCol2 = chfCol2;
    }

    public void setChfTot1(String chfTot1) {
        this.chfTot1 = chfTot1;
    }

    public void setChfTot2(String chfTot2) {
        this.chfTot2 = chfTot2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.07.2003 13:53:36)
     * 
     * @param newCompteAnnexe
     *            globaz.osiris.db.comptes.CACompteAnnexe
     */
    public void setCompteAnnexe(globaz.osiris.db.comptes.CACompteAnnexe newCompteAnnexe) {
        compteAnnexe = newCompteAnnexe;
    }

    /**
     * @param communale
     */
    public void setDecision(CPDecisionAgenceCommunale communale) {
        decision = communale;
    }

    public void setDocument(ICTDocument document) {
        this.document = document;
    }

    public void setDocuments(ICTDocument[] documents) {
        this.documents = documents;
    }

    /**
     * @param string
     */
    public void setEtat(String string) {
        etat = string;
    }

    /**
     * Sets the idLangue.
     * 
     * @param idLangue
     *            The idLangue to set
     */
    public void setIdLangue(int idLangue) {
        this.idLangue = idLangue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    /**
     * Sets the periodiciteAffiliation.
     * 
     * @param periodiciteAffiliation
     *            The periodiciteAffiliation to set
     */
    public void setPeriodiciteAffiliation(String periodiciteAffiliation) {
        this.periodiciteAffiliation = periodiciteAffiliation;
    }

    public void setRes(ICTDocument[] res) {
        this.res = res;
    }

}
