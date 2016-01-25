package globaz.ij.db.prestations;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJPrestationRegles;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRCalcul;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJIJCalculee extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_CS_GENRE_READAPTATION = "XNTGRE";

    /**
     */
    public static final String FIELDNAME_CS_MOTIF = "XNTMOT";

    /**
     */
    public static final String FIELDNAME_CS_STATUT_PROFESSIONNEL = "XNTSTA";

    /**
     */
    public static final String FIELDNAME_CS_TYPE_BASE = "XNTTBA";

    /**
     */
    public static final String FIELDNAME_CS_TYPE_IJ = "XNTTIJ";

    /**
     */
    public static final String FIELDNAME_DATE_DEBUT_DROIT = "XNDDEB";

    /**
     */
    public static final String FIELDNAME_DATE_FIN_DROIT = "XNDFIN";

    /**
     */
    public static final String FIELDNAME_DATE_PRONONCE = "XNDPRO";

    /**
     */
    public static final String FIELDNAME_DATE_REVENU = "XNDREV";

    /**
     */
    public static final String FIELDNAME_DEMI_IJ_AC_BRUT = "XNMDIJ";

    /**
     */
    public static final String FIELDNAME_DIFFERENCE_REVENU = "XNMDRE";

    public static final String FIELDNAME_DROIT_PRESTATION_ENFANT = "XNBDPE";

    public static final String FIELDNAME_GARANTIE_R3 = "XNTGAR";

    /**
     */
    public static final String FIELDNAME_ID_IJ_CALCULEE = "XNIIJC";

    /**
     */
    public static final String FIELDNAME_ID_PRONONCE = "XNIPAI";

    /**
     */
    public static final String FIELDNAME_MONTANT_BASE = "XNMMBA";

    /**
     */
    public static final String FIELDNAME_NO_AVS = "XNNAVS";

    public static final String FIELDNAME_NO_REVISION = "XNLNOR";

    /**
     */
    public static final String FIELDNAME_OFFICE_AI = "XNLOAI";

    /**
     */
    public static final String FIELDNAME_POURCENT_DEGRE_INCAPACITE_TRAVAIL = "XNMINP";
    /**
     */
    public static final String FIELDNAME_REVENU_DETERMINANT = "XNMRED";

    /**
     */
    public static final String FIELDNAME_REVENU_JOURNALIER_READAPTATION = "XNMRJR";

    /**
     */
    public static final String FIELDNAME_SUPPLEMENT_PERSONNE_SEULE = "XNMSPS";

    /**
     */
    public static final String TABLE_NAME_IJ_CALCULEE = "IJCALCUL";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Charge une instance correcte de petite ou grande ij calculee suivant l'id et le type transmis.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param idIJCalculee
     *            DOCUMENT ME!
     * @param csTypeIJ
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final IJIJCalculee loadIJCalculee(BSession session, String idIJCalculee, String csTypeIJ)
            throws Exception {
        IJIJCalculee retValue;

        if (IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)) {
            retValue = new IJGrandeIJCalculee();
        } else if (IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ)) {
            retValue = new IJPetiteIJCalculee();
        } else {
            retValue = new IJIJCalculee();
        }

        retValue.setSession(session);
        retValue.setIdIJCalculee(idIJCalculee);
        retValue.retrieve();

        return retValue;
    }

    private String csGarantieR3 = "";
    private String csGenreReadaptation = "";

    private String csStatutProfessionnel = "";
    private String csTypeBase = "";
    private String csTypeIJ = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String datePrononce = "";
    private String dateRevenu = "";
    private String demiIJACBrut = "";
    private String differenceRevenu = "";
    private String idIJCalculee = "";
    private String idPrononce = "";
    private transient IJIndemniteJournaliere indemnitesJournalieresExternes;
    private transient IJIndemniteJournaliere indemnitesJournalieresInternes;
    private Boolean isDroitPrestationPourEnfant = Boolean.FALSE;

    private String montantBase = "";
    private String noAVS = "";
    private String noRevision = "";
    private String officeAI = "";

    private String pourcentDegreIncapaciteTravail = "";

    private transient IJPrononce prononce;
    private String revenuDeterminant = "";
    private String revenuJournalierReadaptation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String supplementPersonneSeule = "";

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // effacement des prestations
        IJPrestationManager prestationManager = new IJPrestationManager();

        prestationManager.setSession(getSession());
        prestationManager.setForIdIJCalculee(idIJCalculee);
        prestationManager.find(transaction);

        for (int i = 0; i < prestationManager.size(); i++) {
            IJPrestation prestation = (IJPrestation) prestationManager.get(i);

            IJPrestationRegles.annulerMiseEnLot(getSession(), transaction, prestation);
            prestation.delete(transaction);
        }

        // effacement des indemnites journalieres
        IJIndemniteJournaliereManager indemnites = new IJIndemniteJournaliereManager();

        indemnites.setForIdIJCalculee(idIJCalculee);
        indemnites.setSession(getSession());
        indemnites.find();

        for (int idIndemnite = 0; idIndemnite < indemnites.size(); ++idIndemnite) {
            ((IJIndemniteJournaliere) indemnites.get(idIndemnite)).delete(transaction);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idIJCalculee = this._incCounter(transaction, idIJCalculee, IJIJCalculee.TABLE_NAME_IJ_CALCULEE);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return IJIJCalculee.TABLE_NAME_IJ_CALCULEE;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idIJCalculee = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);
        noAVS = statement.dbReadString(IJIJCalculee.FIELDNAME_NO_AVS);
        officeAI = statement.dbReadString(IJIJCalculee.FIELDNAME_OFFICE_AI);
        csGenreReadaptation = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_CS_GENRE_READAPTATION);
        dateDebutDroit = statement.dbReadDateAMJ(IJIJCalculee.FIELDNAME_DATE_DEBUT_DROIT);
        datePrononce = statement.dbReadDateAMJ(IJIJCalculee.FIELDNAME_DATE_PRONONCE);
        dateFinDroit = statement.dbReadDateAMJ(IJIJCalculee.FIELDNAME_DATE_FIN_DROIT);
        csTypeBase = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_CS_TYPE_BASE);
        revenuDeterminant = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_REVENU_DETERMINANT, 2);
        dateRevenu = statement.dbReadDateAMJ(IJIJCalculee.FIELDNAME_DATE_REVENU);
        montantBase = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_MONTANT_BASE, 2);
        supplementPersonneSeule = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_SUPPLEMENT_PERSONNE_SEULE, 2);
        revenuJournalierReadaptation = statement
                .dbReadNumeric(IJIJCalculee.FIELDNAME_REVENU_JOURNALIER_READAPTATION, 2);
        csStatutProfessionnel = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_CS_STATUT_PROFESSIONNEL);
        pourcentDegreIncapaciteTravail = statement.dbReadNumeric(
                IJIJCalculee.FIELDNAME_POURCENT_DEGRE_INCAPACITE_TRAVAIL, 2);
        demiIJACBrut = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_DEMI_IJ_AC_BRUT, 2);
        idPrononce = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_ID_PRONONCE);
        csTypeIJ = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_CS_TYPE_IJ);
        csGarantieR3 = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_GARANTIE_R3);
        differenceRevenu = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_DIFFERENCE_REVENU);
        noRevision = statement.dbReadString(IJIJCalculee.FIELDNAME_NO_REVISION);
        isDroitPrestationPourEnfant = statement.dbReadBoolean(IJIJCalculee.FIELDNAME_DROIT_PRESTATION_ENFANT);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE,
                this._dbWriteNumeric(statement.getTransaction(), idIJCalculee));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE,
                this._dbWriteNumeric(statement.getTransaction(), idIJCalculee, "idIJCalculee"));
        statement.writeField(IJIJCalculee.FIELDNAME_NO_AVS,
                this._dbWriteString(statement.getTransaction(), noAVS, "noAVS"));
        statement.writeField(IJIJCalculee.FIELDNAME_OFFICE_AI,
                this._dbWriteString(statement.getTransaction(), officeAI, "officeAI"));
        statement.writeField(IJIJCalculee.FIELDNAME_CS_GENRE_READAPTATION,
                this._dbWriteNumeric(statement.getTransaction(), csGenreReadaptation, "csGenreReadaptation"));
        statement.writeField(IJIJCalculee.FIELDNAME_DATE_DEBUT_DROIT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutDroit, "dateDebutDroit"));
        statement.writeField(IJIJCalculee.FIELDNAME_DATE_PRONONCE,
                this._dbWriteDateAMJ(statement.getTransaction(), datePrononce, "datePrononce"));
        statement.writeField(IJIJCalculee.FIELDNAME_DATE_FIN_DROIT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinDroit, "dateFinDroit"));
        statement.writeField(IJIJCalculee.FIELDNAME_CS_TYPE_BASE,
                this._dbWriteNumeric(statement.getTransaction(), csTypeBase, "csTypeBase"));
        statement.writeField(IJIJCalculee.FIELDNAME_REVENU_DETERMINANT,
                this._dbWriteNumeric(statement.getTransaction(), revenuDeterminant, "revenuDeterminant"));
        statement.writeField(IJIJCalculee.FIELDNAME_DATE_REVENU,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRevenu, "dateRevenu"));
        statement.writeField(IJIJCalculee.FIELDNAME_MONTANT_BASE,
                this._dbWriteNumeric(statement.getTransaction(), montantBase, "montantBase"));
        statement.writeField(IJIJCalculee.FIELDNAME_SUPPLEMENT_PERSONNE_SEULE,
                this._dbWriteNumeric(statement.getTransaction(), supplementPersonneSeule, "supplementPersonneSeule"));
        statement.writeField(IJIJCalculee.FIELDNAME_REVENU_JOURNALIER_READAPTATION, this._dbWriteNumeric(
                statement.getTransaction(), revenuJournalierReadaptation, "revenuJournalierReadaptation"));
        statement.writeField(IJIJCalculee.FIELDNAME_CS_STATUT_PROFESSIONNEL,
                this._dbWriteNumeric(statement.getTransaction(), csStatutProfessionnel, "csStatutProfessionnel"));
        statement.writeField(IJIJCalculee.FIELDNAME_POURCENT_DEGRE_INCAPACITE_TRAVAIL, this._dbWriteNumeric(
                statement.getTransaction(), pourcentDegreIncapaciteTravail, "pourcentDegreIncapaciteTravail"));
        statement.writeField(IJIJCalculee.FIELDNAME_DEMI_IJ_AC_BRUT,
                this._dbWriteNumeric(statement.getTransaction(), demiIJACBrut, "demiIJACBrut"));
        statement.writeField(IJIJCalculee.FIELDNAME_ID_PRONONCE,
                this._dbWriteNumeric(statement.getTransaction(), idPrononce, "idPrononce"));
        statement.writeField(IJIJCalculee.FIELDNAME_CS_TYPE_IJ,
                this._dbWriteNumeric(statement.getTransaction(), csTypeIJ, "csTypeIJ"));
        statement.writeField(IJIJCalculee.FIELDNAME_GARANTIE_R3,
                this._dbWriteNumeric(statement.getTransaction(), csGarantieR3, "csGarantieR3"));
        statement.writeField(IJIJCalculee.FIELDNAME_DIFFERENCE_REVENU,
                this._dbWriteNumeric(statement.getTransaction(), differenceRevenu, "differenceRevenu"));

        statement.writeField(IJIJCalculee.FIELDNAME_NO_REVISION,
                this._dbWriteString(statement.getTransaction(), noRevision, "noRevision"));

        if (isDroitPrestationPourEnfant != null) {
            statement.writeField(IJIJCalculee.FIELDNAME_DROIT_PRESTATION_ENFANT, this._dbWriteBoolean(
                    statement.getTransaction(), isDroitPrestationPourEnfant, BConstants.DB_TYPE_BOOLEAN_CHAR,
                    "isDroitPrestationPourEnfant"));
        }
    }

    /**
     * @deprecated
     * @return
     */
    @Deprecated
    public String getCsGarantieR3() {
        return csGarantieR3;
    }

    /**
     * getter pour l'attribut cs genre readaptation.
     * 
     * @return la valeur courante de l'attribut cs genre readaptation
     */
    public String getCsGenreReadaptation() {
        return csGenreReadaptation;
    }

    /**
     * getter pour l'attribut cs statut professionnel.
     * 
     * @return la valeur courante de l'attribut cs statut professionnel
     */
    public String getCsStatutProfessionnel() {
        return csStatutProfessionnel;
    }

    /**
     * getter pour l'attribut cs type base.
     * 
     * @return la valeur courante de l'attribut cs type base
     */
    public String getCsTypeBase() {
        return csTypeBase;
    }

    /**
     * getter pour l'attribut cs type IJ.
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date debut droit.
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * getter pour l'attribut date fin droit.
     * 
     * @return la valeur courante de l'attribut date fin droit
     */
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    /**
     * getter pour l'attribut date prononce.
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getDatePrononce() {
        return datePrononce;
    }

    /**
     * getter pour l'attribut date revenu.
     * 
     * @return la valeur courante de l'attribut date revenu
     */
    public String getDateRevenu() {
        return dateRevenu;
    }

    /**
     * getter pour l'attribut demi IJACBrut.
     * 
     * @return la valeur courante de l'attribut demi IJACBrut
     */
    public String getDemiIJACBrut() {
        return demiIJACBrut;
    }

    /**
     * getter pour l'attribut difference revenu.
     * 
     * @return la valeur courante de l'attribut difference revenu
     */
    public String getDifferenceRevenu() {
        return differenceRevenu;
    }

    /**
     * getter pour l'attribut id IJIJCalculee.
     * 
     * @return la valeur courante de l'attribut id IJIJCalculee
     */
    public String getIdIJCalculee() {
        return idIJCalculee;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    public Boolean getIsDroitPrestationPourEnfant() {
        return isDroitPrestationPourEnfant;
    }

    /**
     * getter pour l'attribut montant base.
     * 
     * @return la valeur courante de l'attribut montant base
     */
    public String getMontantBase() {
        return montantBase;
    }

    /**
     * getter pour l'attribut no AVS.
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    public String getNoRevision() {
        return noRevision;
    }

    /**
     * getter pour l'attribut cs office AI.
     * 
     * @return la valeur courante de l'attribut cs office AI
     */
    public String getOfficeAI() {
        return officeAI;
    }

    /**
     * getter pour l'attribut pourcent degre incapacite travail.
     * 
     * @return la valeur courante de l'attribut pourcent degre incapacite travail
     */
    public String getPourcentDegreIncapaciteTravail() {
        return pourcentDegreIncapaciteTravail;
    }

    /**
     * getter pour l'attribut revenu determinant.
     * 
     * @return la valeur courante de l'attribut revenu determinant
     */
    public String getRevenuDeterminant() {
        return revenuDeterminant;
    }

    /**
     * getter pour l'attribut revenu journalier readaptation.
     * 
     * @return la valeur courante de l'attribut revenu journalier readaptation
     */
    public String getRevenuJournalierReadaptation() {
        return revenuJournalierReadaptation;
    }

    /**
     * getter pour l'attribut supplement personne seule.
     * 
     * @return la valeur courante de l'attribut supplement personne seule
     */
    public String getSupplementPersonneSeule() {
        return supplementPersonneSeule;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * Pour une base d'indémnisation donnée, détermine si cette ij et l'ij 'ij' donneraient lieu à exactement les mêmes
     * montants de prestations.
     * 
     * @param ij
     *            DOCUMENT ME!
     * 
     * @return vrai si les ij sont égales au niveau du calcul.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean isEgalPourCalcul(IJIJCalculee ij) throws Exception {
        // comparer les types d'IJ
        if (PRCalcul.isIEgaux(csGenreReadaptation, ij.csGenreReadaptation)
                && PRCalcul.isIEgaux(csStatutProfessionnel, ij.csStatutProfessionnel)
                && PRCalcul.isIEgaux(csTypeBase, ij.csTypeBase) && PRCalcul.isIEgaux(csTypeIJ, ij.csTypeIJ)
                && dateRevenu.equals(ij.dateRevenu)) {
            // comparer les montants
            if (PRCalcul.isDEgaux(demiIJACBrut, ij.demiIJACBrut) && PRCalcul.isDEgaux(montantBase, ij.montantBase)
                    && PRCalcul.isDEgaux(pourcentDegreIncapaciteTravail, ij.pourcentDegreIncapaciteTravail)
                    && PRCalcul.isDEgaux(revenuDeterminant, ij.revenuDeterminant)
                    && PRCalcul.isDEgaux(revenuJournalierReadaptation, ij.revenuJournalierReadaptation)
                    && PRCalcul.isDEgaux(supplementPersonneSeule, ij.supplementPersonneSeule)) {

                // Changement de l'IS dans les prononcé
                IJPrononce prononceOrigine = new IJPrononce();
                prononceOrigine.setIdPrononce(getIdPrononce());
                prononceOrigine.setSession(getSession());
                prononceOrigine.retrieve();

                IJPrononce prononceCorrection = new IJPrononce();
                prononceCorrection.setIdPrononce(ij.getIdPrononce());
                prononceCorrection.setSession(getSession());
                prononceCorrection.retrieve();

                if ((prononceOrigine.getSoumisImpotSource() != null)
                        && (prononceCorrection.getSoumisImpotSource() == null)) {
                    return false;
                } else if ((prononceOrigine.getSoumisImpotSource() == null)
                        && (prononceCorrection.getSoumisImpotSource() != null)) {
                    return false;
                } else if ((prononceOrigine.getSoumisImpotSource() != null)
                        && !prononceOrigine.getSoumisImpotSource().equals(prononceCorrection.getSoumisImpotSource())) {

                    return false;
                }

                if (IIJPrononce.CS_ALLOC_ASSIST.equals(prononceOrigine.getCsTypeIJ())
                        || IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(prononceOrigine.getCsTypeIJ())) {

                    IJIndemniteJournaliereManager indemnites = new IJIndemniteJournaliereManager();

                    IJIndemniteJournaliere i1 = null;
                    IJIndemniteJournaliere i2 = null;
                    indemnites.setSession(getSession());
                    indemnites.setForIdIJCalculee(idIJCalculee);
                    indemnites.find();

                    if (!indemnites.isEmpty()) {
                        i1 = (IJIndemniteJournaliere) indemnites.get(0);
                    }

                    indemnites.setForIdIJCalculee(ij.getIdIJCalculee());
                    indemnites.find();

                    if (!indemnites.isEmpty()) {
                        i2 = (IJIndemniteJournaliere) indemnites.get(0);
                    }

                    if ((i1 == null) && (i2 == null)) {
                        return true;
                    } else {
                        return i1.isEgalPourCalcul(i2);
                    }

                } else {
                    // comparer les indemnites journalieres
                    return (loadIndemnitesJournalieresExternes().isEgalPourCalcul(
                            ij.loadIndemnitesJournalieresExternes()) && loadIndemnitesJournalieresInternes()
                            .isEgalPourCalcul(ij.loadIndemnitesJournalieresInternes()));
                }
            }
        }

        return false;
    }

    /**
     * retourne vrai si cette ij calculee est pour une grande ij.
     * 
     * @return la valeur courante de l'attribut grande IJCalculee
     */
    public boolean isGrandeIJCalculee() {
        return IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ);
    }

    /**
     * charge les indemnites journalieres externes liees a cette ij calculee.
     * 
     * @return l'ij externe ou null si pas.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJIndemniteJournaliere loadIndemnitesJournalieresExternes() throws Exception {
        if ((indemnitesJournalieresExternes == null) && !JadeStringUtil.isIntegerEmpty(idIJCalculee)) {
            IJIndemniteJournaliereManager indemnites = new IJIndemniteJournaliereManager();

            indemnites.setSession(getSession());
            indemnites.setForIdIJCalculee(idIJCalculee);
            indemnites.setForCsTypeIndemnite(IIJMesure.CS_EXTERNE);
            indemnites.find();

            if (!indemnites.isEmpty()) {
                indemnitesJournalieresExternes = (IJIndemniteJournaliere) indemnites.get(0);
            }
        }

        return indemnitesJournalieresExternes;
    }

    /**
     * charge les indemnites journalieres internes liees a cette ij calculee.
     * 
     * @return l'ij externe ou null si pas.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJIndemniteJournaliere loadIndemnitesJournalieresInternes() throws Exception {
        if ((indemnitesJournalieresInternes == null) && !JadeStringUtil.isIntegerEmpty(idIJCalculee)) {
            IJIndemniteJournaliereManager indemnites = new IJIndemniteJournaliereManager();

            indemnites.setSession(getSession());
            indemnites.setForIdIJCalculee(idIJCalculee);
            indemnites.setForCsTypeIndemnite(IIJMesure.CS_INTERNE);
            indemnites.find();

            if (!indemnites.isEmpty()) {
                indemnitesJournalieresInternes = (IJIndemniteJournaliere) indemnites.get(0);
            }
        }

        return indemnitesJournalieresInternes;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return null ou le prononce pour cette ij calculee
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJPrononce loadPrononce(BITransaction transaction) throws Exception {
        if ((prononce == null) && !JadeStringUtil.isIntegerEmpty(idPrononce)) {
            prononce = IJPrononce.loadPrononce(getSession(), transaction, getIdPrononce(), getCsTypeIJ());
        }

        return prononce;
    }

    /**
     * @deprecated remplacé par noRevision
     * @param string
     */
    @Deprecated
    public void setCsGarantieR3(String string) {
        csGarantieR3 = string;
    }

    /**
     * setter pour l'attribut cs genre readaptation.
     * 
     * @param csGenreReadaptation
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsGenreReadaptation(String csGenreReadaptation) {
        this.csGenreReadaptation = csGenreReadaptation;
    }

    /**
     * setter pour l'attribut cs statut professionnel.
     * 
     * @param csStatutProfessionnel
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsStatutProfessionnel(String csStatutProfessionnel) {
        this.csStatutProfessionnel = csStatutProfessionnel;
    }

    /**
     * setter pour l'attribut cs type base.
     * 
     * @param csTypeBase
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeBase(String csTypeBase) {
        this.csTypeBase = csTypeBase;
    }

    /**
     * setter pour l'attribut cs type IJ.
     * 
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        this.csTypeIJ = csTypeIJ;
    }

    /**
     * setter pour l'attribut date debut droit.
     * 
     * @param dateDebutDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    /**
     * setter pour l'attribut date fin droit.
     * 
     * @param dateFinDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    /**
     * setter pour l'attribut date prononce.
     * 
     * @param datePrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setDatePrononce(String datePrononce) {
        this.datePrononce = datePrononce;
    }

    /**
     * setter pour l'attribut date revenu.
     * 
     * @param dateRevenu
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateRevenu(String dateRevenu) {
        this.dateRevenu = dateRevenu;
    }

    /**
     * setter pour l'attribut demi IJACBrut.
     * 
     * @param demiIJACBrut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDemiIJACBrut(String demiIJACBrut) {
        this.demiIJACBrut = demiIJACBrut;
    }

    /**
     * setter pour l'attribut difference revenu.
     * 
     * @param differenceRevenu
     *            une nouvelle valeur pour cet attribut
     */
    public void setDifferenceRevenu(String differenceRevenu) {
        this.differenceRevenu = differenceRevenu;
    }

    /**
     * setter pour l'attribut id IJIJCalculee.
     * 
     * @param idIJCalculee
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdIJCalculee(String idIJCalculee) {
        this.idIJCalculee = idIJCalculee;
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param idPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public void setIsDroitPrestationPourEnfant(Boolean isDroitPrestationPourEnfant) {
        this.isDroitPrestationPourEnfant = isDroitPrestationPourEnfant;
    }

    /**
     * setter pour l'attribut montant base.
     * 
     * @param montantBase
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBase(String montantBase) {
        this.montantBase = montantBase;
    }

    /**
     * setter pour l'attribut no AVS.
     * 
     * @param noAVS
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    public void setNoRevision(String noRevision) {
        this.noRevision = noRevision;
    }

    /**
     * setter pour l'attribut cs office AI.
     * 
     * @param csOfficeAI
     *            une nouvelle valeur pour cet attribut
     */
    public void setOfficeAI(String csOfficeAI) {
        officeAI = csOfficeAI;
    }

    /**
     * setter pour l'attribut pourcent degre incapacite travail.
     * 
     * @param pourcentDegreIncapaciteTravail
     *            une nouvelle valeur pour cet attribut
     */
    public void setPourcentDegreIncapaciteTravail(String pourcentDegreIncapaciteTravail) {
        this.pourcentDegreIncapaciteTravail = pourcentDegreIncapaciteTravail;
    }

    /**
     * setter pour l'attribut revenu determinant.
     * 
     * @param revenuDeterminant
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenuDeterminant(String revenuDeterminant) {
        this.revenuDeterminant = revenuDeterminant;
    }

    /**
     * setter pour l'attribut revenu journalier readaptation.
     * 
     * @param revenuJournalierReadaptation
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenuJournalierReadaptation(String revenuJournalierReadaptation) {
        this.revenuJournalierReadaptation = revenuJournalierReadaptation;
    }

    /**
     * setter pour l'attribut supplement personne seule.
     * 
     * @param supplementPersonneSeule
     *            une nouvelle valeur pour cet attribut
     */
    public void setSupplementPersonneSeule(String supplementPersonneSeule) {
        this.supplementPersonneSeule = supplementPersonneSeule;
    }
}
