package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.vb.decisions.REDecisionJointDemandeRenteViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class REDecisionJointDemandeRenteManager extends PRAbstractManager implements
        BIGenericManager<REDecisionJointDemandeRente> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String derniereDecision = "";
    private String forCsEtatDecisions = "";
    private String forCsGenreRenteAccodee = "";
    private String forCsGenreRenteAccodeeIn = "";
    private String forCsGenreRenteAccodeeNotIn = "";
    private String forCsSexe = "";
    private String forCsTypeDecision = "";
    private String forCsTypeDemandeRente = "";
    private String forDateNaissance = "";
    private String forDepuisDebutDroit = "";
    private String forDepuisValidation = "";
    private String forIdDecision = "";
    private Long forIdDemandeRente = null;
    private String forIdsRentesAccordeesIn = "";
    private String forIdTiersBeneficiaire = "";
    private String forNoDemandeRente = "";
    private String forNoDemandeRenteIn = "";
    private transient String fromClause = null;
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";
    private String likePreparePar = "";
    private String likeValidePar = "";

    @Override
    protected String _getWhere(BStatement statement) {
        String schema = _getCollection();
        StringBuilder sql = new StringBuilder();

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(PRNSSUtil.getWhereNSS(schema, getLikeNumeroAVS(), getLikeNumeroAVSNNSS()));
        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(IPRConstantesExternes.TABLE_PERSONNE).append(".")
                    .append(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_SEXE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(IPRConstantesExternes.TABLE_TIERS).append(".")
                    .append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_NOM_FOR_SEARCH);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(IPRConstantesExternes.TABLE_TIERS).append(".")
                    .append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_PRENOM_FOR_SEARCH);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema + REDemandeRenteJointDemande.TABLE_PERSONNE).append(".")
                    .append(REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE).append("=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));

        }

        if (!JadeStringUtil.isEmpty(likePreparePar)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            // Correction bug 4952
            sql.append("(");

            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_PREPARE_PAR);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(), "%" + JadeStringUtil.toLowerCase(likePreparePar)
                    + "%"));

            sql.append(" OR ");

            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_PREPARE_PAR);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(), "%" + JadeStringUtil.toUpperCase(likePreparePar)
                    + "%"));

            sql.append(")");
        }

        if (!JadeStringUtil.isEmpty(likeValidePar)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            // Correction bug 4952
            sql.append("(");

            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_VALIDE_PAR);
            sql.append(" like ");
            sql.append(this._dbWriteString(statement.getTransaction(), "%" + JadeStringUtil.toLowerCase(likeValidePar)
                    + "%"));

            sql.append(" OR ");

            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_VALIDE_PAR);
            sql.append(" like ");
            sql.append(this._dbWriteString(statement.getTransaction(), "%" + JadeStringUtil.toUpperCase(likeValidePar)
                    + "%"));

            sql.append(")");
        }

        if (!JadeStringUtil.isEmpty(forCsTypeDecision)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_TYPE_DECISION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeDecision));
        }

        if (!JadeStringUtil.isEmpty(forCsGenreRenteAccodee)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
            sql.append("=");
            sql.append(this._dbWriteString(statement.getTransaction(), forCsGenreRenteAccodee));
        }

        if (!JadeStringUtil.isEmpty(forCsGenreRenteAccodeeIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
            sql.append(" IN (").append(forCsGenreRenteAccodeeIn).append(") ");
        }

        if (!JadeStringUtil.isEmpty(forCsGenreRenteAccodeeNotIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
            sql.append(" NOT IN (").append(forCsGenreRenteAccodeeNotIn).append(" ) ");
        }

        if (!JadeStringUtil.isEmpty(forDepuisValidation)) {
            StringBuilder forDepuisValidationBuilder = new StringBuilder();
            try {
                if (sql.length() != 0) {
                    forDepuisValidationBuilder.append(" AND ");
                }
                forDepuisValidationBuilder.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                        .append(REDecisionEntity.FIELDNAME_DATE_VALIDATION);
                forDepuisValidationBuilder.append(">=");
                forDepuisValidationBuilder.append(this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(forDepuisValidation)));
            } catch (Exception ex) {
                forDepuisValidationBuilder = new StringBuilder();
                _addError(statement.getTransaction(), "Erreur de conversion de date");
                ex.printStackTrace();
            }
            sql.append(forDepuisValidationBuilder.toString());
        }

        if (!JadeStringUtil.isEmpty(forDepuisDebutDroit)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            sql.append(">=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(),
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forDepuisDebutDroit)));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDecisions)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            if (REDecisionJointDemandeRenteViewBean.LABEL_NON_VALIDE.equals(forCsEtatDecisions)) {
                forCsEtatDecisions = IREDecision.CS_ETAT_ATTENTE + " , " + IREDecision.CS_ETAT_PREVALIDE;
            }
            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_ETAT);
            sql.append(" IN (").append(forCsEtatDecisions).append(")");
        }

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_ID_DECISION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isEmpty(forNoDemandeRente)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forNoDemandeRente));
        }

        if (!JadeStringUtil.isEmpty(forNoDemandeRenteIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE);
            sql.append(" IN (").append(forNoDemandeRenteIn).append(")");
        }

        if (!JadeStringUtil.isEmpty(forIdsRentesAccordeesIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(schema).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
            sql.append(" IN ( ").append(forIdsRentesAccordeesIn).append(" ) ");
        }

        if (!JadeStringUtil.isEmpty(derniereDecision)) {
            try {
                RELotManager lotMgr = new RELotManager();
                lotMgr.setSession(getSession());
                lotMgr.setForCsType(IRELot.CS_TYP_LOT_DECISION);
                lotMgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
                lotMgr.setOrderBy(RELot.FIELDNAME_ID_LOT + " DESC");
                lotMgr.find(statement.getTransaction(), 1);

                if (!lotMgr.isEmpty()) {
                    RELot lastLot = (RELot) lotMgr.get(0);

                    REPrestationsManager prestMgr = new REPrestationsManager();
                    prestMgr.setForIdLot(lastLot.getIdLot());
                    prestMgr.setSession(getSession());
                    prestMgr.find(statement.getTransaction(), BManager.SIZE_NOLIMIT);

                    if (!prestMgr.isEmpty()) {

                        String idsDecision = "";

                        for (Iterator<REPrestations> iterator = prestMgr.iterator(); iterator.hasNext();) {
                            REPrestations prest = iterator.next();

                            idsDecision += prest.getIdDecision();

                            if (iterator.hasNext()) {
                                idsDecision += ", ";
                            }
                        }

                        if (sql.length() != 0) {
                            sql.append(" AND ");
                        }

                        sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                                .append(REDecisionEntity.FIELDNAME_ID_DECISION);
                        sql.append(" IN ( ").append(idsDecision).append(" ) ");
                    }
                }
            } catch (Exception ex) {
                _addError(statement.getTransaction(),
                        "Erreur dans la construction de la requête pour la dernière décision");
                ex.printStackTrace();
            }
        }

        if (!JadeStringUtil.isBlank(forCsTypeDemandeRente)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                    .append(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeDemandeRente));
        }

        if (!JadeStringUtil.isBlank(forIdTiersBeneficiaire)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiersBeneficiaire));
        }

        if ((forIdDemandeRente != null) && (forIdDemandeRente.intValue() != 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(schema).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(".")
                    .append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE).append("=").append(forIdDemandeRente);
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDecisionJointDemandeRente();
    }

    @Override
    public List<REDecisionJointDemandeRente> getContainerAsList() {
        List<REDecisionJointDemandeRente> list = new ArrayList<REDecisionJointDemandeRente>();
        for (int i = 0; i < size(); i++) {
            list.add((REDecisionJointDemandeRente) get(i));
        }
        return list;
    }

    public String getDerniereDecision() {
        return derniereDecision;
    }

    public String getForCsEtatDecisions() {
        return forCsEtatDecisions;
    }

    public String getForCsGenreRenteAccodee() {
        return forCsGenreRenteAccodee;
    }

    public String getForCsGenreRenteAccodeeIn() {
        return forCsGenreRenteAccodeeIn;
    }

    public String getForCsGenreRenteAccodeeNotIn() {
        return forCsGenreRenteAccodeeNotIn;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsTypeDecision() {
        return forCsTypeDecision;
    }

    public String getForCsTypeDemandeRente() {
        return forCsTypeDemandeRente;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDepuisDebutDroit() {
        return forDepuisDebutDroit;
    }

    public String getForDepuisValidation() {
        return forDepuisValidation;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public Long getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public String getForIdsRentesAccordeesIn() {
        return forIdsRentesAccordeesIn;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    public String getForNoDemandeRente() {
        return forNoDemandeRente;
    }

    public String getForNoDemandeRenteIn() {
        return forNoDemandeRenteIn;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public String getLikePreparePar() {
        return likePreparePar;
    }

    public String getLikeValidePar() {
        return likeValidePar;
    }

    @Override
    public String getOrderByDefaut() {
        return REDecisionEntity.FIELDNAME_ID_DECISION;
    }

    public void setDerniereDecision(String derniereDecision) {
        this.derniereDecision = derniereDecision;
    }

    public void setForCsEtatDecisions(String forCsEtatDecisions) {
        this.forCsEtatDecisions = forCsEtatDecisions;
    }

    public void setForCsGenreRenteAccodee(String forCsGenreRenteAccodee) {
        this.forCsGenreRenteAccodee = forCsGenreRenteAccodee;
    }

    public void setForCsGenreRenteAccodeeIn(String forCsGenreRenteAccodeeIn) {
        this.forCsGenreRenteAccodeeIn = forCsGenreRenteAccodeeIn;
    }

    public void setForCsGenreRenteAccodeeNotIn(String forCsGenreRenteAccodeeNotIn) {
        this.forCsGenreRenteAccodeeNotIn = forCsGenreRenteAccodeeNotIn;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsTypeDecision(String forCsTypeDecision) {
        this.forCsTypeDecision = forCsTypeDecision;
    }

    public void setForCsTypeDemandeRente(String forCsTypeDemandeRente) {
        this.forCsTypeDemandeRente = forCsTypeDemandeRente;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDepuisDebutDroit(String forDepuisDebutDroit) {
        this.forDepuisDebutDroit = forDepuisDebutDroit;
    }

    public void setForDepuisValidation(String forDepuisValidation) {
        this.forDepuisValidation = forDepuisValidation;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdDemandeRente(Long idDemandeRente) {
        forIdDemandeRente = idDemandeRente;
    }

    public void setForIdsRentesAccordeesIn(String forIdsRentesAccordeesIn) {
        this.forIdsRentesAccordeesIn = forIdsRentesAccordeesIn;
    }

    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    public void setForNoDemandeRente(String forNoDemandeRente) {
        this.forNoDemandeRente = forNoDemandeRente;
    }

    public void setForNoDemandeRenteIn(String forNoDemandeRenteIn) {
        this.forNoDemandeRenteIn = forNoDemandeRenteIn;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public void setLikePreparePar(String likePreparePar) {
        this.likePreparePar = likePreparePar;
    }

    public void setLikeValidePar(String likeValidePar) {
        this.likeValidePar = likeValidePar;
    }
}
