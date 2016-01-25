package globaz.apg.db.alfa;

import globaz.apg.api.alfa.IAPBouclementAlfa;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.pojo.wrapper.APBouclementAlfaWrapper;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un entity contenant toutes les informations necessaires au bouclement ALFA des caisses horlogeres.
 * </p>
 * 
 * <p>
 * Cet entity est un peu special, il ne peut qu'etre lu. Le seul moyen d'en obtenir une instance correcte est de passer
 * par le manager correspondant.
 * </p>
 * 
 * @author vre
 * @see globaz.apg.db.alfa.APBouclementAlfaManager
 */
public class APBouclementAlfa extends BEntity implements IAPBouclementAlfa {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final JACalendar CALENDAR = new JACalendarGregorian();
    public static final String FIELDNAME_COTISATIONS_ASSURANCES = "VHMCOT";
    public static final String FIELDNAME_COTISATIONS_IMPOTS = "VHMIMP";
    public static final String FIELDNAME_MONTANTS_BRUTS = "VHMMOB";

    public static final String FIELDNAME_NB_JOURS = "VHNNJS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idAffilie = "";
    private String idDroit = "";
    private String idPrestation = "";
    private String montantBrutACM = "";
    private String montantCotisationsACM = "";
    private String montantImpotsACM = "";
    private String nombreJoursCouvertsACM = "";
    private String typeDemande = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
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
        typeDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_TYPE_DEMANDE);
        nombreJoursCouvertsACM = statement.dbReadNumeric(FIELDNAME_NB_JOURS);
        montantBrutACM = statement.dbReadNumeric(FIELDNAME_MONTANTS_BRUTS);
        montantCotisationsACM = statement.dbReadNumeric(FIELDNAME_COTISATIONS_ASSURANCES);
        montantImpotsACM = statement.dbReadNumeric(FIELDNAME_COTISATIONS_IMPOTS);
        idAffilie = statement.dbReadString(APRepartitionPaiements.FIELDNAME_IDAFFILIE);
        idDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        idPrestation = statement.dbReadNumeric(APPrestation.FIELDNAME_IDPRESTATIONAPG);
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
        _addError(statement.getTransaction(), "interdit d'ajouter");
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
        // ON NE FAIT RIEN
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
        // ON NE FAIT RIEN
    }

    /**
     * getter pour l'id Affilie
     * 
     * @return l'idAffilie
     */
    @Override
    public String getIdAffilie() {
        return idAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.alfa.IAPBouclementAlfa#getIdDroit()
     */
    @Override
    public String getIdDroit() {
        return idDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.alfa.IAPBouclementAlfa#getIdPrestation()
     */
    @Override
    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * getter pour l'attribut montant brut ACM.
     * 
     * @return la valeur courante de l'attribut montant brut ACM
     */
    @Override
    public String getMontantBrutACM() {
        return montantBrutACM;
    }

    /**
     * getter pour l'attribut montant cotisations ACM.
     * 
     * @return la valeur courante de l'attribut montant cotisations ACM
     */
    @Override
    public String getMontantCotisationsACM() {
        return montantCotisationsACM;
    }

    /**
     * getter pour l'attribut montant impots ACM.
     * 
     * @return la valeur courante de l'attribut montant impots ACM
     */
    @Override
    public String getMontantImpotsACM() {
        return montantImpotsACM;
    }

    /**
     * getter pour l'attribut nombre jours couverts ACM.
     * 
     * @return la valeur courante de l'attribut nombre jours couverts ACM
     */
    @Override
    public String getNombreJoursCouvertsACM() {
        return nombreJoursCouvertsACM;
    }

    /**
     * getter pour l'attribut type.
     * 
     * @return la valeur courante de l'attribut type
     */
    @Override
    public String getType() {
        if (IPRDemande.CS_TYPE_APG.equals(typeDemande)) {
            return TYPE_APG;
        } else if (IPRDemande.CS_TYPE_MATERNITE.equals(typeDemande)) {
            return TYPE_AMAT;
        }

        return null;
    }

    /**
     * @param mois
     *            DOCUMENT ME!
     * @param annee
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IAPBouclementAlfa[] load(String mois, String annee) throws Exception {
        APBouclementAlfaManager bouclements = new APBouclementAlfaManager();
        JADate date = new JADate(1, JadeStringUtil.toInt(mois), JadeStringUtil.toInt(annee));
        String dateDebutPeriode = date.toStr(".");

        date.setDay(CALENDAR.daysInMonth(date.getMonth(), date.getYear()));
        bouclements.setForPeriode(dateDebutPeriode, date.toStr("."));
        bouclements.setSession(getSession());
        bouclements.find(BManager.SIZE_NOLIMIT);

        if (bouclements.isEmpty()) {
            return new APBouclementAlfaWrapper[0];
        } else {
            APBouclementAlfaWrapper[] result = new APBouclementAlfaWrapper[bouclements.size()];
            IAPBouclementAlfa[] bouclementsAlfa = (IAPBouclementAlfa[]) bouclements.getContainer().toArray(
                    new IAPBouclementAlfa[bouclements.size()]);

            for (int i = 0; i < bouclements.size(); i++) {
                result[i] = new APBouclementAlfaWrapper();
                result[i].setIdAffilie(bouclementsAlfa[i].getIdAffilie());
                result[i].setIdDroit(bouclementsAlfa[i].getIdDroit());
                result[i].setIdPrestation(bouclementsAlfa[i].getIdPrestation());
                result[i].setMontantBrutACM(bouclementsAlfa[i].getMontantBrutACM());
                result[i].setMontantCotisationsACM(bouclementsAlfa[i].getMontantCotisationsACM());
                result[i].setMontantImpotsACM(bouclementsAlfa[i].getMontantImpotsACM());
                result[i].setNombreJoursCouvertsACM(bouclementsAlfa[i].getNombreJoursCouvertsACM());
                result[i].setType(bouclementsAlfa[i].getType());
            }

            return result;
        }
    }

    /**
     * setter pour l'id Affilie
     * 
     * @param String
     *            de l'id Affilie
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * @param string
     */
    public void setIdDroit(String string) {
        idDroit = string;
    }

    /**
     * @param string
     */
    public void setIdPrestation(String string) {
        idPrestation = string;
    }

    /**
     * setter pour l'attribut montant brut ACM.
     * 
     * @param montantBrutACM
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrutACM(String montantBrutACM) {
        this.montantBrutACM = montantBrutACM;
    }

    /**
     * setter pour l'attribut montant cotisations ACM.
     * 
     * @param montantCotisationsACM
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantCotisationsACM(String montantCotisationsACM) {
        this.montantCotisationsACM = montantCotisationsACM;
    }

    /**
     * setter pour l'attribut montant impots ACM.
     * 
     * @param montantImpotsACM
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantImpotsACM(String montantImpotsACM) {
        this.montantImpotsACM = montantImpotsACM;
    }

    /**
     * setter pour l'attribut nombre jours couverts ACM.
     * 
     * @param nombreJoursCouvertsACM
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursCouvertsACM(String nombreJoursCouvertsACM) {
        this.nombreJoursCouvertsACM = nombreJoursCouvertsACM;
    }

}
