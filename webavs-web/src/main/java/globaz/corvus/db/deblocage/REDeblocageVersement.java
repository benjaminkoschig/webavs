/*
 * Globaz SA.
 */
package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocage;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.corvus.db.lignedeblocageventilation.RELigneDeblocageVentilation;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.nnss.PRNSSUtil;
import ch.globaz.common.domaine.AdressePaiement;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.services.AdressePaiementLoader;

public class REDeblocageVersement extends BEntity {

    private static final long serialVersionUID = 1L;

    private String idCompteAnnexe;
    private String descriptionCompteAnnexe;
    private String idExterneCompteAnnexe;

    private String idExterneSextion;

    private String idRenteAccordee;
    private String codeRenteAccordee;
    private String idTiersAdressePaiement;
    private String idApplicationAdressePaiement;
    private String idTiersBeneficiaire;
    private String csSexe;
    private String dateNaissance;
    private String pays;

    private Montant montant;
    private String refPaiement;
    private RELigneDeblocage ligneDeblocage = new RELigneDeblocage();
    private RELigneDeblocageVentilation ligneDeblocageVentilation = new RELigneDeblocageVentilation();

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCompteAnnexe = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
        descriptionCompteAnnexe = statement.dbReadString("DESCRIPTION");
        idExterneCompteAnnexe = statement.dbReadString("IDEXTERNEROLE");

        csSexe = statement.dbReadNumeric("hptsex");
        dateNaissance = statement.dbReadDateAMJ("hpdnai");
        pays = statement.dbReadNumeric("hnipay");

        idExterneSextion = statement.dbReadString("IDEXTERNE");

        idRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        codeRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        ligneDeblocage.readFromStatement(statement, "ld");
        ligneDeblocageVentilation.readFromStatement(statement, "ldv");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not implemented
    }

    public String formatAdressePaiement() {
        return "";
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public Montant getMontant() {
        return ligneDeblocage.getMontant();
    }

    public RELigneDeblocageType getType() {
        return ligneDeblocage.getType();
    }

    public String getCodeRenteAccordee() {
        return codeRenteAccordee;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public String getIdTiersAdressePaiement() {
        return ligneDeblocage.getIdTiersAdressePaiement().toString();
    }

    public String getIdApplicationAdressePaiement() {
        return ligneDeblocage.getIdApplicationAdressePaiement().toString();
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public RELigneDeblocage getLigneDeblocage() {
        return ligneDeblocage;
    }

    public RELigneDeblocageVentilation getLigneDeblocageVentilation() {
        return ligneDeblocageVentilation;
    }

    public String getDescriptionCompteAnnexe() {
        return descriptionCompteAnnexe;
    }

    public String getIdExterneCompteAnnexe() {
        return idExterneCompteAnnexe;
    }

    public String formatInformationCompte() {
        return PRNSSUtil.formatDetailRequerantListe(getIdExterneCompteAnnexe(), getDescriptionCompteAnnexe(),
                getDateNaissance(), getSession().getCodeLibelle(getCsSexe()),
                getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getPays())));
    }

    public String formatInformationCompteForListe() {
        return getIdExterneCompteAnnexe() + "/ " + getDescriptionCompteAnnexe() + " / " + getDateNaissance() + " / "
                + getSession().getCodeLibelle(getCsSexe()) + " / "
                + getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getPays()));
    }

    public String getIdExterneSextion() {
        return idExterneSextion;
    }

    public String toStringEntity() {
        return "REDeblocageVersement [idCompteAnnexe=" + idCompteAnnexe + ", idRenteAccordee=" + idRenteAccordee
                + ", codeRenteAccordee=" + codeRenteAccordee + ", idTiersAdressePaiement=" + idTiersAdressePaiement
                + ", idApplicationAdressePaiement=" + idApplicationAdressePaiement + ", idTiersBeneficiaire="
                + idTiersBeneficiaire + ", montant=" + montant + ", refPaiement=" + refPaiement + ", ligneDeblocage="
                + ligneDeblocage + ", ligneDeblocageVentilation=" + ligneDeblocageVentilation + "]";
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getPays() {
        return pays;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public AdressePaiement loadAdressePaiement() {
        AdressePaiementLoader loader = new AdressePaiementLoader(getSession());
        return loader.searchAdressePaiement(getLigneDeblocage().getIdTiersAdressePaiement(), getLigneDeblocage()
                .getIdApplicationAdressePaiement());
    }

    public Montant getMontantVentil() {
        return ligneDeblocageVentilation.getMontant();
    }
}