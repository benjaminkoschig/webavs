package globaz.corvus.db.echeances;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREEnfantEcheances;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;
import ch.globaz.corvus.business.models.echeances.IRERelationEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;

/**
 * Conteneur pour les échéances dans les rentes pour l'ancienne persistence.<br/>
 * <br/>
 * Sera utilisé de deux manières différentes :
 * <ul>
 * <li>Pour charger les données brutes depuis la base de données dans {@link #_readProperties(BStatement)} (la recherche
 * se fait par {@link REEcheancesManager#find()})</li>
 * <li>Pour contenir les données regroupées par rente, triées dans la méthode
 * {@link REEcheancesManager#_afterFind(globaz.globall.db.BTransaction)}
 * 
 * @author PBA
 */
public class REEcheancesEntity extends BEntity implements IREEcheances {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csSexeTiers;
    private String dateDecesTiers;
    private String dateNaissanceTiers;
    private SortedSet<IREEnfantEcheances> enfantsDuTiers;
    private boolean hasRenteVeuvePerdure;
    private String idTiers;
    private String nomTiers;
    private String nssTiers;
    private SortedSet<IREPeriodeEcheances> periodes;
    private String prenomTiers;
    private List<IRERelationEcheances> relations;
    private Set<IRERenteEcheances> rentesDuTiers;
    /**
     * Utilisé uniquement pour le Valais
     */
    private String communePolitique = "";

    public REEcheancesEntity() {
        super();

        csSexeTiers = "";
        dateDecesTiers = "";
        dateNaissanceTiers = "";
        enfantsDuTiers = new TreeSet<IREEnfantEcheances>();
        hasRenteVeuvePerdure = false;
        idTiers = "";
        nomTiers = "";
        nssTiers = "";
        periodes = new TreeSet<IREPeriodeEcheances>();
        prenomTiers = "";
        relations = new ArrayList<IRERelationEcheances>();
        rentesDuTiers = new HashSet<IRERenteEcheances>();
    }

    @Override
    protected String _getTableName() {
        return RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        csSexeTiers = statement.dbReadNumeric(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE_CS_SEXE);
        dateDecesTiers = statement.dbReadDateAMJ(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE_DATE_DECES);
        dateNaissanceTiers = statement.dbReadDateAMJ(REEcheancesManager.ALIAS_BENEFICIAIRE_PERSONNE_DATE_NAISSANCE);
        idTiers = statement.dbReadNumeric(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS_ID_TIERS);
        nomTiers = statement.dbReadString(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS_NOM);
        nssTiers = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenomTiers = statement.dbReadString(REEcheancesManager.ALIAS_BENEFICIAIRE_TIERS_PRENOM);

        RERenteJoinDemandeEcheance uneRenteDuTiers = new RERenteJoinDemandeEcheance();
        uneRenteDuTiers.setIdPrestationAccordee(statement
                .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_ID_PRESTATION_ACCORDEE));
        uneRenteDuTiers.setIdTiersBeneficiaire(idTiers);
        uneRenteDuTiers.setDateRevocationAjournement(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERenteAccordee.FIELDNAME_DATE_REVOCATION_AJOURNEMENT)));
        uneRenteDuTiers.setCodePrestation(statement
                .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_CODE_PRESTATION));
        uneRenteDuTiers.setCsEtat(statement.dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_CS_ETAT));
        uneRenteDuTiers.setCsEtatDemandeRente(statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_ETAT));
        uneRenteDuTiers.setDateEcheance(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_DATE_ECHEANCE)));
        uneRenteDuTiers.setDateDebutDroit(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_DATE_DEBUT)));
        uneRenteDuTiers.setDateFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_DATE_FIN)));
        uneRenteDuTiers.setIsPrestationBloquee(statement
                .dbReadBoolean(REEcheancesManager.ALIAS_PRESTATION_BENEFICIAIRE_IS_PRESTATION_BLOQUEE));
        uneRenteDuTiers.setAnneeAnticipation(statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ANNEE_ANTICIPATION));
        uneRenteDuTiers.setCsTypeInfoComplementaire(statement.dbReadNumeric(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL));

        Set<String> codesCasSpeciaux = new HashSet<String>();
        String codeCasSpecial_1 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1);
        if (!JadeStringUtil.isBlank(codeCasSpecial_1)) {
            codesCasSpeciaux.add(codeCasSpecial_1);
        }
        String codeCasSpecial_2 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2);
        if (!JadeStringUtil.isBlank(codeCasSpecial_2)) {
            codesCasSpeciaux.add(codeCasSpecial_2);
        }
        String codeCasSpecial_3 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3);
        if (!JadeStringUtil.isBlank(codeCasSpecial_3)) {
            codesCasSpeciaux.add(codeCasSpecial_3);
        }
        String codeCasSpecial_4 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4);
        if (!JadeStringUtil.isBlank(codeCasSpecial_4)) {
            codesCasSpeciaux.add(codeCasSpecial_4);
        }
        String codeCasSpecial_5 = statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5);
        if (!JadeStringUtil.isBlank(codeCasSpecial_5)) {
            codesCasSpeciaux.add(codeCasSpecial_5);
        }

        uneRenteDuTiers.setCodesCasSpeciaux(codesCasSpeciaux);

        uneRenteDuTiers.setCsGenreDroitApi(statement.dbReadNumeric(RERenteAccordee.FIELDNAME_CS_GENRE_DROIT_API));

        rentesDuTiers.add(uneRenteDuTiers);

        String csTypePeriode = statement.dbReadNumeric(SFPeriode.FIELD_TYPE);
        if (!JadeStringUtil.isBlankOrZero(csTypePeriode)) {
            periodes.add(new REPeriodeEcheances(statement.dbReadNumeric(SFPeriode.FIELD_IDPERIODE), statement
                    .dbReadDateAMJ(SFPeriode.FIELD_DATEDEBUT), statement.dbReadDateAMJ(SFPeriode.FIELD_DATEFIN),
                    csTypePeriode));
        }

        String dateNaissanceConjoint = statement
                .dbReadDateAMJ(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_NAISSANCE);
        if (JadeStringUtil.isBlank(dateNaissanceConjoint)) {
            dateNaissanceConjoint = statement.dbReadDateAMJ(REEcheancesManager.ALIAS_CONJOINT_PERSONNE_DATE_NAISSANCE);
        }
        if (!JadeStringUtil.isBlank(dateNaissanceConjoint)) {
            RERelationEcheances relation = new RERelationEcheances();
            relation.setDateNaissanceConjoint(dateNaissanceConjoint);
            relation.setIdTiersConjoint(statement.dbReadNumeric(REEcheancesManager.ALIAS_CONJOINT_TIERS_ID_TIERS));
            relation.setNomConjoint(statement.dbReadString(REEcheancesManager.ALIAS_CONJOINT_TIERS_NOM));
            relation.setPrenomConjoint(statement.dbReadString(REEcheancesManager.ALIAS_CONJOINT_TIERS_ID_PRENOM));
            relation.setCsSexeConjoint(statement.dbReadNumeric(REEcheancesManager.ALIAS_CONJOINT_PERSONNE_CS_SEXE));

            String dateDecesConjoint = statement
                    .dbReadDateAMJ(REEcheancesManager.ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_DECES);
            if (JadeStringUtil.isBlank(dateDecesConjoint)) {
                dateDecesConjoint = statement.dbReadDateAMJ(REEcheancesManager.ALIAS_CONJOINT_PERSONNE_DATE_DECES);
            }
            relation.setDateDecesConjoint(dateDecesConjoint);

            String csTypeRelation = statement.dbReadNumeric(SFRelationConjoint.FIELD_TYPERELATION);
            if (!JadeStringUtil.isBlankOrZero(csTypeRelation)) {

                relation.setCsTypeRelation(csTypeRelation);
                relation.setIdRelation(statement.dbReadNumeric(SFRelationConjoint.FIELD_IDRELATIONCONJOINT));
                relation.setDateDebut(statement.dbReadDateAMJ(SFRelationConjoint.FIELD_DATEDEBUT));
                relation.setDateFin(statement.dbReadDateAMJ(SFRelationConjoint.FIELD_DATEFIN));

                String idPrestationConjoint = statement
                        .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_ID_PRESTATION_ACCORDEE);
                if (!JadeStringUtil.isBlankOrZero(idPrestationConjoint)) {
                    RERenteJoinDemandeEcheance renteConjoint = new RERenteJoinDemandeEcheance();
                    renteConjoint.setIdPrestationAccordee(idPrestationConjoint);
                    renteConjoint.setIdTiersBeneficiaire(relation.getIdTiersConjoint());
                    renteConjoint.setCsEtat(statement
                            .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_CS_ETAT));
                    renteConjoint.setCodePrestation(statement
                            .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_CODE_PRESTATION));
                    renteConjoint.setDateDebutDroit(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                            .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_DATE_DEBUT)));
                    renteConjoint.setDateFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                            .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_CONJOINT_DATE_FIN)));

                    relation.getRentesDuConjoint().add(renteConjoint);
                }
                relations.add(relation);
            }
        }

        String idMembreFamilleEnfant = statement
                .dbReadNumeric(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_ID_MEMBRE_FAMILLE);
        if (!JadeStringUtil.isBlankOrZero(idMembreFamilleEnfant)) {

            String idTiersEnfant = statement.dbReadNumeric(REEcheancesManager.ALIAS_ENFANT_TIERS_ID_TIERS);

            REEnfantEcheances enfant = new REEnfantEcheances();

            if (!JadeStringUtil.isBlankOrZero(idTiersEnfant)) {
                enfant.setIdTiers(idTiersEnfant);
                enfant.setDateDeces(statement.dbReadDateAMJ(REEcheancesManager.ALIAS_ENFANT_PERSONNE_DATE_DECES));
                enfant.setDateNaissance(statement
                        .dbReadDateAMJ(REEcheancesManager.ALIAS_ENFANT_PERSONNE_DATE_NAISSANCE));
                enfant.setNom(statement.dbReadString(REEcheancesManager.ALIAS_ENFANT_TIERS_NOM));
                enfant.setPrenom(statement.dbReadString(REEcheancesManager.ALIAS_ENFANT_TIERS_PRENOM));

                String idPrestationAccordee = statement
                        .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_ENFANT_ID_PRESTATION_ACCORDEE);
                if (!JadeStringUtil.isBlankOrZero(idPrestationAccordee)) {
                    RERenteJoinDemandeEcheance renteEnfant = new RERenteJoinDemandeEcheance();
                    renteEnfant.setIdPrestationAccordee(idPrestationAccordee);
                    renteEnfant.setIdTiersBeneficiaire(idTiersEnfant);
                    renteEnfant.setCsEtat(statement.dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_ENFANT_CS_ETAT));
                    renteEnfant.setCodePrestation(statement
                            .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_ENFANT_CODE_PRESTATION));
                    renteEnfant.setIsPrestationBloquee(statement
                            .dbReadBoolean(REEcheancesManager.ALIAS_PRESTATION_ENFANT_IS_BLOQUEE));
                    renteEnfant.setDateDebutDroit(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                            .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_ENFANT_DATE_DEBUT)));
                    renteEnfant.setDateFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                            .dbReadNumeric(REEcheancesManager.ALIAS_PRESTATION_ENFANT_DATE_FIN)));

                    enfant.getRentes().add(renteEnfant);
                }
            } else {
                enfant.setDateDeces(statement.dbReadDateAMJ(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_DATE_DECES));
                enfant.setDateNaissance(statement
                        .dbReadDateAMJ(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_DATE_NAISSANCE));
                enfant.setNom(statement.dbReadString(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_NOM));
                enfant.setPrenom(statement.dbReadString(REEcheancesManager.ALIAS_ENFANT_MEMBRE_FAMILLE_PRENOM));
            }
            enfantsDuTiers.add(enfant);
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("Entité de visualisation, n'est pas sauvegardable en base de données");
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ITITiersDefTable.ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("Entité de visualisation, n'est pas sauvegardable en base de données");
    }

    /**
     * Comparaison appliquant le même tri que la clause ORDER BY du manager (tri par ordre alphabétique des tiers)
     */
    @Override
    public int compareTo(IREEcheances o) {
        int compareNom = JadeStringUtil.convertSpecialChars(o.getNomTiers()).compareTo(
                JadeStringUtil.convertSpecialChars(getNomTiers()));
        if (compareNom != 0) {
            return -1 * compareNom;
        }

        int comparePrenom = JadeStringUtil.convertSpecialChars(o.getPrenomTiers()).compareTo(
                JadeStringUtil.convertSpecialChars(getPrenomTiers()));
        if (comparePrenom != 0) {
            return -1 * comparePrenom;
        }

        return getIdTiers().compareTo(o.getIdTiers());
    }

    @Override
    public String getCsSexeTiers() {
        return csSexeTiers;
    }

    @Override
    public String getDateDecesTiers() {
        return dateDecesTiers;
    }

    @Override
    public String getDateNaissanceTiers() {
        return dateNaissanceTiers;
    }

    @Override
    public SortedSet<IREEnfantEcheances> getEnfantsDuTiers() {
        return enfantsDuTiers;
    }

    @Override
    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public String getNomTiers() {
        return nomTiers;
    }

    @Override
    public String getNssTiers() {
        return nssTiers;
    }

    @Override
    public SortedSet<IREPeriodeEcheances> getPeriodes() {
        return periodes;
    }

    @Override
    public String getPrenomTiers() {
        return prenomTiers;
    }

    @Override
    public List<IRERelationEcheances> getRelations() {
        return relations;
    }

    @Override
    public Set<IRERenteEcheances> getRentesDuTiers() {
        return rentesDuTiers;
    }

    @Override
    public boolean hasPrestationBloquee() {
        for (IRERenteEcheances rente : rentesDuTiers) {
            if (rente.isPrestationBloquee()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasRenteVeuvePerdure() {
        return hasRenteVeuvePerdure;
    }

    public void setCsSexeTiers(String csSexeTiers) {
        this.csSexeTiers = csSexeTiers;
    }

    public void setDateDecesTiers(String dateDecesTiers) {
        this.dateDecesTiers = dateDecesTiers;
    }

    public void setDateNaissanceTiers(String dateNaissanceTiers) {
        this.dateNaissanceTiers = dateNaissanceTiers;
    }

    public void setEnfantsDuTiers(SortedSet<IREEnfantEcheances> enfantsDuTiers) {
        this.enfantsDuTiers = enfantsDuTiers;
    }

    public void setHasRenteVeuvePerdure(boolean hasRenteVeuvePerdure) {
        this.hasRenteVeuvePerdure = hasRenteVeuvePerdure;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setPeriodes(SortedSet<IREPeriodeEcheances> periodes) {
        this.periodes = periodes;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    public void setRelations(List<IRERelationEcheances> relations) {
        this.relations = relations;
    }

    public void setRentesDuTiers(SortedSet<IRERenteEcheances> rentesDuTiers) {
        this.rentesDuTiers = rentesDuTiers;
    }

    public final String getCommunePolitique() {
        return communePolitique;
    }

    public final void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }
}
