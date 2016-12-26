package globaz.corvus.db.retenues;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;

/**
 * @author HPE
 */
public class RERetenuesPaiement extends BEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CANTON_IMPOSITION = "YQLCAN";
    public static final String FIELDNAME_DATE_DEBUT_RETENUE = "YQDDER";
    public static final String FIELDNAME_DATE_FIN_RETENUE = "YQDDFR";
    public static final String FIELDNAME_GENRE_RETENUE = "YQTGEN";
    public static final String FIELDNAME_ID_DOMAINE_APPLICATIF = "YQIDOA";
    public static final String FIELDNAME_ID_EXTERNE = "YQIEXT";
    public static final String FIELDNAME_ID_PARENT_RETENU = "YQIPAR";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE = "YQIRAC";
    public static final String FIELDNAME_ID_RETENUE = "YQIRET";
    public static final String FIELDNAME_ID_RUBRIQUE = "YQIRUB";
    public static final String FIELDNAME_ID_TIERS_ADR_PMT = "YQITAP";
    public static final String FIELDNAME_ID_TYPE_SECTION = "YQITSC";
    public static final String FIELDNAME_MONTANT_DEJA_RETENU = "YQMMDR";
    public static final String FIELDNAME_MONTANT_RETENU_MENS = "YQMREM";
    public static final String FIELDNAME_MONTANT_TOTAL_A_RETENIR = "YQMTAR";
    public static final String FIELDNAME_NO_FACTURE = "YQLNFA";
    public static final String FIELDNAME_REF_INTERNE = "YQLRIN";
    public static final String FIELDNAME_ROLE = "YQNROL";
    public static final String FIELDNAME_TAUX_IMPOSITION = "YQMTAU";
    public static final String FIELDNAME_TYPE_RETENU = "YQTTYP";
    public static final String TABLE_NAME_RETENUES = "RERETEN";

    private String cantonImposition = "";
    private String csGenreRetenue = "";
    private String csTypeRetenue = "";
    private String dateDebutRetenue = "";
    private String dateFinRetenue = "";
    private String idDomaineApplicatif = "";
    private String idExterne = "";
    private String idParentRetenue = "";
    private String idRenteAccordee = "";
    private String idRetenue = "";
    private String idRubrique = "";
    private String idTiersAdressePmt = "";
    private String idTypeSection = "";
    private String montantDejaRetenu = "";
    private String montantRetenuMensuel = "";
    private String montantTotalARetenir = "";
    private String noFacture = "";
    private String referenceInterne = "";
    private RERenteAccordee renteAccordee = null;
    private String role = "";
    private transient CASection section = null;
    private String tauxImposition = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRetenue(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return RERetenuesPaiement.TABLE_NAME_RETENUES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRetenue = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ID_RETENUE);
        idRenteAccordee = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ID_RENTE_ACCORDEE);
        idParentRetenue = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ID_PARENT_RETENU);
        idTypeSection = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ID_TYPE_SECTION);
        csTypeRetenue = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_TYPE_RETENU);
        idTiersAdressePmt = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ID_TIERS_ADR_PMT);
        idDomaineApplicatif = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ID_DOMAINE_APPLICATIF);
        noFacture = statement.dbReadString(RERetenuesPaiement.FIELDNAME_NO_FACTURE);
        idExterne = statement.dbReadString(RERetenuesPaiement.FIELDNAME_ID_EXTERNE);
        role = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ROLE);
        montantRetenuMensuel = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_MONTANT_RETENU_MENS);
        montantDejaRetenu = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_MONTANT_DEJA_RETENU);
        montantTotalARetenir = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_MONTANT_TOTAL_A_RETENIR);
        dateDebutRetenue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE));
        csGenreRetenue = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_GENRE_RETENUE);
        idRubrique = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ID_RUBRIQUE);
        dateFinRetenue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE));
        referenceInterne = statement.dbReadString(RERetenuesPaiement.FIELDNAME_REF_INTERNE);
        tauxImposition = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_TAUX_IMPOSITION);
        cantonImposition = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_CANTON_IMPOSITION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // format attendu : mm.aaaa
        // format reçu :
        // - mm.aaaa
        // - jjmmaaaa
        // - 01.0610 (cas d'une saisi du genre : 010610)
        String s = getDateDebutRetenue();

        s = PRDateFormater.convertDate_MMxAAAA_to_AAAA(s);
        if (JadeStringUtil.isBlankOrZero(s)) {
            _addError(statement.getTransaction(), getSession().getLabel("VALID_RET_DATE_FORMAT_ERROR"));
            return;
        } else if (Integer.valueOf(s).intValue() < 1800) {
            _addError(statement.getTransaction(), getSession().getLabel("VALID_RET_DATE_FORMAT_ERROR"));
            return;
        }

        // Teste que la date de début soit au moins égal à la date du dernier
        // pmt + 1 mois
        // tant que le montant total déjà retenu est égal à 0
        if (new FWCurrency(getMontantDejaRetenu()).isZero()) {
            JACalendarGregorian cal = new JACalendarGregorian();
            JADate dateDernierPmtPlus1 = cal.addMonths(new JADate(REPmtMensuel.getDateDernierPmt(getSession())), 1);

            JADate dateDebutRetenue = new JADate(getDateDebutRetenue());

            if (cal.compare(dateDernierPmtPlus1, dateDebutRetenue) == JACalendar.COMPARE_SECONDLOWER) {
                _addError(statement.getTransaction(), getSession().getLabel("VALID_RET_PMT_DATE_DEBUT"));
            }
        }

        // Teste que la date de fin ne soit pas antérieure au mois en cours
        if (!JadeNumericUtil.isEmptyOrZero(getDateFinRetenue())) {
            JACalendarGregorian cal = new JACalendarGregorian();
            JADate vDate = new JADate("01" + JadeStringUtil.removeChar(getDateFinRetenue(), '.'));
            JADate vDateJour = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
            vDateJour.setDay(1);
            if (cal.compare(vDate, vDateJour) == JACalendar.COMPARE_FIRSTLOWER) {
                _addError(statement.getTransaction(), getSession().getLabel("VALID_RET_PMT_DATE_FIN"));
            }
        }

        if (IRERetenues.CS_TYPE_FACTURE_FUTURE.equals(getCsTypeRetenue())) {

            // On set la référence interne avec l'idTiers du bénéficiaire...
            String idExterne = getIdExterne();
            PRTiersWrapper tw = PRTiersHelper.getTiers(getSession(), idExterne);
            String idTiers = null;
            if (tw != null) {
                idTiers = tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
            } else {
                IPRAffilie aff = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), idExterne);
                if (aff != null) {
                    idTiers = aff.getIdTiers();
                }
            }
            if (idTiers == null) {
                _addError(statement.getTransaction(), getSession().getLabel("ERREUR_AUCUN_TIERS_TROUVE_POUR_NO")
                        + idExterne);
            }
            setReferenceInterne(idTiers);
        }

        // pour le type facture existante :
        // Soit idTypeSection + noFacture sont renseigné
        // Soit referenceInterne est renseigné (contient idSection)
        if (IRERetenues.CS_TYPE_FACTURE_EXISTANTE.equals(getCsTypeRetenue())) {
            if (JadeStringUtil.isBlankOrZero(getNoFacture()) && JadeStringUtil.isBlankOrZero(getIdTypeSection())) {

                if (JadeStringUtil.isBlankOrZero(getReferenceInterne())) {
                    _addError(statement.getTransaction(), getSession().getLabel("VALID_RET_PMT_NO_FACTURE"));
                }
            }
        }

        // pour le type impot a la source le canton d'imposition est obligatoire
        if (IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(getCsTypeRetenue())) {
            if (JadeStringUtil.isIntegerEmpty(getCantonImposition())) {
                _addError(statement.getTransaction(), getSession().getLabel("VALID_RET_PMT_CANTON_IMPOSITION"));
            }
        }

        // Contrôle que le montant à retenir soit supérieur à 0 pour les cas de retenue qui ne sont pas de type Impôts à
        // la source
        if (!IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(getCsTypeRetenue())) {

            if (JadeStringUtil.isBlank(getMontantRetenuMensuel())) {
                _addError(statement.getTransaction(), getSession().getLabel("MONTANT_RETENUE_MENSUEL_INVALID"));
            }

            BigDecimal zero = new BigDecimal("0.00");
            BigDecimal montantARetenir = new BigDecimal(JANumberFormatter.deQuote(getMontantRetenuMensuel()));

            if (montantARetenir.compareTo(zero) <= 0) {
                _addError(statement.getTransaction(), getSession().getLabel("MONTANT_RETENUE_MENSUEL_INVALID"));
            }
        }

    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RERetenuesPaiement.FIELDNAME_ID_RETENUE,
                this._dbWriteNumeric(statement.getTransaction(), idRetenue, "idRetenue"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RERetenuesPaiement.FIELDNAME_ID_RETENUE,
                this._dbWriteNumeric(statement.getTransaction(), idRetenue, "idRetenue"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_ID_RENTE_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idRenteAccordee, "idRenteAccordee"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_ID_PARENT_RETENU,
                this._dbWriteNumeric(statement.getTransaction(), idParentRetenue, "idParentRetenue"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_ID_TYPE_SECTION,
                this._dbWriteNumeric(statement.getTransaction(), idTypeSection, "idTypeSection"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_TYPE_RETENU,
                this._dbWriteNumeric(statement.getTransaction(), csTypeRetenue, "csTypeRetenue"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_ID_TIERS_ADR_PMT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAdressePmt, "idTiersAdressePmt"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_ID_DOMAINE_APPLICATIF,
                this._dbWriteNumeric(statement.getTransaction(), idDomaineApplicatif, "idDomaineApplicatif"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_NO_FACTURE,
                this._dbWriteString(statement.getTransaction(), noFacture, "noFacture"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_ID_EXTERNE,
                this._dbWriteString(statement.getTransaction(), idExterne, "idExterne"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_ROLE,
                this._dbWriteNumeric(statement.getTransaction(), role, "role"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_MONTANT_RETENU_MENS,
                this._dbWriteNumeric(statement.getTransaction(), montantRetenuMensuel, "montantRetenuMensuel"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_MONTANT_DEJA_RETENU,
                this._dbWriteNumeric(statement.getTransaction(), montantDejaRetenu, "montantDejaRetenu"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_MONTANT_TOTAL_A_RETENIR,
                this._dbWriteNumeric(statement.getTransaction(), montantTotalARetenir, "montantTotalARetenir"));
        statement.writeField(
                RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebutRetenue), "dateDebutRetenue"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_GENRE_RETENUE,
                this._dbWriteNumeric(statement.getTransaction(), csGenreRetenue, "csGenreRetenue"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_ID_RUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idRubrique, "idRubrique"));
        statement.writeField(
                RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFinRetenue), "dateFinRetenue"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_REF_INTERNE,
                this._dbWriteString(statement.getTransaction(), referenceInterne, "referenceInterne"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_TAUX_IMPOSITION,
                this._dbWriteNumeric(statement.getTransaction(), tauxImposition, "tauxImposition"));
        statement.writeField(RERetenuesPaiement.FIELDNAME_CANTON_IMPOSITION,
                this._dbWriteNumeric(statement.getTransaction(), cantonImposition, "cantonImposition"));
    }

    public String getCantonImposition() {
        return cantonImposition;
    }

    public String getCsGenreRetenue() {
        return csGenreRetenue;
    }

    public String getCsTypeRetenue() {
        return csTypeRetenue;
    }

    public String getDateDebutRetenue() {
        return dateDebutRetenue;
    }

    public String getDateFinRetenue() {
        return dateFinRetenue;
    }

    public String getIdDomaineApplicatif() {
        return idDomaineApplicatif;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdParentRetenue() {
        return idParentRetenue;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdRetenue() {
        return idRetenue;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    public String getIdTypeSection() {
        return idTypeSection;
    }

    public String getMontantDejaRetenu() {
        return montantDejaRetenu;
    }

    public String getMontantRetenuMensuel() {
        return montantRetenuMensuel;
    }

    public String getMontantTotalARetenir() {
        return montantTotalARetenir;
    }

    public String getNoFacture() {
        return noFacture;
    }

    public String getReferenceInterne() {
        return referenceInterne;
    }

    /**
     * Récupère la rente accordée
     * 
     * @return the renteAccordee
     */
    public RERenteAccordee getRenteAccordee() {
        if (JadeNumericUtil.isEmptyOrZero(getIdRenteAccordee())) {
            return new RERenteAccordee();
        }
        if (renteAccordee == null) {
            return loadRenteAccordee(getIdRenteAccordee());
        } else {
            return renteAccordee;
        }
    }

    /**
     * Récupère la rente accordée
     * 
     * @return the renteAccordee
     */
    public RERenteAccordee getRenteAccordee(String idRA) {
        if (JadeNumericUtil.isEmptyOrZero(idRA)) {
            return new RERenteAccordee();
        }
        if (getIdRenteAccordee().equals(idRA)) {
            if (renteAccordee == null) {
                return loadRenteAccordee(idRA);
            } else {
                return renteAccordee;
            }
        } else {
            return loadRenteAccordee(idRA);
        }
    }

    public String getRole() {
        return role;
    }

    public String getTauxImposition() {
        return tauxImposition;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * Chargement de la rente accordée
     * 
     * @param idRA
     * @return
     * @throws Exception
     */
    private RERenteAccordee loadRenteAccordee(String idRA) {
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(idRA);
        try {
            ra.retrieve();
        } catch (Exception e) {
            return ra;
        }
        return ra;
    }

    public void setCantonImposition(String string) {
        cantonImposition = string;
    }

    public void setCsGenreRetenue(String string) {
        csGenreRetenue = string;
    }

    public void setCsTypeRetenue(String string) {
        csTypeRetenue = string;
    }

    /**
     * Modification de la date de début de la retenue
     * 
     * @param newDateDebutRetenue
     */
    public void setDateDebutRetenue(String newDateDebutRetenue) {
        dateDebutRetenue = newDateDebutRetenue;
    }

    /**
     * Modification de la date de fin de la retenue
     * 
     * @param newDateFinRetenue
     */
    public void setDateFinRetenue(String newDateFinRetenue) {
        dateFinRetenue = newDateFinRetenue;
    }

    public void setIdDomaineApplicatif(String string) {
        idDomaineApplicatif = string;
    }

    public void setIdExterne(String string) {
        idExterne = string;
    }

    public void setIdParentRetenue(String idParentRetenue) {
        this.idParentRetenue = idParentRetenue;
    }

    public void setIdRenteAccordee(String string) {
        idRenteAccordee = string;
    }

    public void setIdRetenue(String string) {
        idRetenue = string;
    }

    public void setIdRubrique(String string) {
        idRubrique = string;
    }

    public void setIdTiersAdressePmt(String string) {
        idTiersAdressePmt = string;
    }

    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    public void setMontantDejaRetenu(String string) {
        montantDejaRetenu = string;
    }

    public void setMontantRetenuMensuel(String string) {
        montantRetenuMensuel = string;
    }

    public void setMontantTotalARetenir(String string) {
        montantTotalARetenir = string;
    }

    public void setNoFacture(String string) {
        noFacture = string;
    }

    public void setReferenceInterne(String string) {
        referenceInterne = string;
    }

    public void setRole(String string) {
        role = string;
    }

    public void setTauxImposition(String string) {
        tauxImposition = string;
    }
}
