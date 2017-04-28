package globaz.phenix.itext.taxation.definitive;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitive;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitiveManager;
import globaz.pyxis.db.tiers.TITiers;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Pourcentage;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.queryexec.bridge.jade.SCM;

class LoadTaxationsDefinitives {

    private final BSession session;

    public LoadTaxationsDefinitives(BSession session) {
        this.session = session;
    }

    public List<TaxationDefinitiveForList> load(final ListTaxationsDefinitivesCriteria criteria) {
        SQLWriter sqlWriter = SQLWriter.write();

        // Select
        constructClauseSelect(sqlWriter);

        // From
        constructClauseFrom(sqlWriter, criteria);

        // Where
        constructClauseWhere(sqlWriter, criteria);

        // Order by
        constructClauseOrder(sqlWriter);

        // Exécution de la requête
        final List<CPTaxationDefinitive> taxations = SCM.newInstance(CPTaxationDefinitive.class).session(session)
                .query(sqlWriter.toSql()).execute();

        // Transformation de la persistence -> métier
        final List<TaxationDefinitiveForList> listeOutput = new ArrayList<TaxationDefinitiveForList>();
        for (CPTaxationDefinitive elem : taxations) {
            listeOutput.add(mappingDataWithPojo(elem, criteria.getInclureAffilieRadie()));
        }

        return listeOutput;
    }

    private void constructClauseSelect(final SQLWriter writer) {
        writer.select("avs.hxnavs as nss, affiliation.malnaf as no_Affilie, "
                + "affiliation.madfin as date_Fin_Affiliation, situationprof.vfmaat as annee_Taxation, "
                + "prestation.vhddeb as date_Debut, prestation.vhdfin as date_Fin, doc.ihmdca as revenu_Determinant, "
                + "droit.vatgse as code_Service, situationprof.vfmrin as revenu_Independant, demande.wattde as type_Prestation ");
    }

    private void constructClauseFrom(final SQLWriter writer, final ListTaxationsDefinitivesCriteria criteria) {
        if (isAPGAmat(criteria)) {
            constructClauseFromSinceAPGAmatToCP(writer);
        } else {
            constructClauseFromSinceCPToAPGAmat(writer);
        }
    }

    private boolean isAPGAmat(final ListTaxationsDefinitivesCriteria criteria) {
        // Si il n'y a pas de n° passage et année de taxation à vide, alors on va faire une requête de l'APG au CP
        // Autrement que ce soit depuis facturation ou autre, on fait du CP aux APG
        return JadeStringUtil.isEmpty(criteria.getNoPassage()) && JadeStringUtil.isEmpty(criteria.getAnneeTaxationCP())
                && JadeStringUtil.isEmpty(criteria.getDateDebutDecisionsCP())
                && JadeStringUtil.isEmpty(criteria.getDateFinDecisionsCP());
    }

    private void constructClauseFromSinceCPToAPGAmat(final SQLWriter writer) {
        writer.from("schema.cpdecip decision");
        writer.join("schema.afaffip affiliation on decision.maiaff = affiliation.maiaff");
        writer.join("schema.cpdocap doc on doc.iaidec = decision.iaidec");
        writer.join("schema.titierp tiers on tiers.htitie = affiliation.htitie");
        writer.join("schema.tipavsp avs on avs.htitie = tiers.htitie");
        writer.leftJoin("schema.prdemap demande on demande.waitie = affiliation.htitie");
        writer.leftJoin("schema.apdroip droit on droit.vaidem = demande.waidem");
        writer.leftJoin("schema.appresp prestation on prestation.vhidro = droit.vaidro");
        writer.leftJoin("schema.apsiprp situationprof ON situationprof.vfidro = droit.vaidro");
    }

    private void constructClauseFromSinceAPGAmatToCP(final SQLWriter writer) {
        writer.from("schema.apsiprp situationprof");
        writer.join("schema.apdroip droit ON situationprof.vfidro = droit.vaidro");
        writer.join("schema.appresp prestation on prestation.vhidro = droit.vaidro");
        writer.join("schema.prdemap demande on droit.vaidem = demande.waidem");
        writer.join("schema.afaffip affiliation on demande.waitie = affiliation.htitie");
        writer.join("schema.titierp tiers on tiers.htitie = affiliation.htitie");
        writer.join("schema.tipavsp avs on avs.htitie = tiers.htitie");
        writer.leftJoin("schema.cpdecip decision on decision.maiaff = affiliation.maiaff");
        writer.leftJoin("schema.cpdocap doc on doc.iaidec = decision.iaidec");
    }

    private void constructClauseWhere(final SQLWriter writer, final ListTaxationsDefinitivesCriteria criteria) {

        clauseWhereForCommon(writer, criteria);

        clauseWhereForAPGAmat(writer, criteria);

        clauseWhereForDecisionCP(writer, criteria);
    }

    private void clauseWhereForCommon(final SQLWriter writer, final ListTaxationsDefinitivesCriteria criteria) {
        // la décision doit être active
        writer.where("decision.iaacti = '1'");

        // le type de calcul doit être de type fortune totale ou revenu net
        writer.and("doc.ihidca IN ( ?, ? )", CPTaxationDefinitiveManager.CS_TYPE_CALC_FORTUNE_TOTALE,
                CPTaxationDefinitiveManager.CS_TYPE_CALC_REVENU_NET);

        // la prestation doit être en êtat définitive
        writer.and("prestation.vhteta = ?", CPTaxationDefinitiveManager.CS_ETAT_PRESTATION_DEFINITIF);

        // la décision doit être en état définitive ou rectificative ou remise ou réduction
        writer.and("decision.iattde IN ( ?, ?, ?, ? )", CPTaxationDefinitiveManager.CS_DECISION_DEFINITIVE,
                CPTaxationDefinitiveManager.CS_DECISION_REMISE, CPTaxationDefinitiveManager.CS_DECISION_RECTIFICATIVE,
                CPTaxationDefinitiveManager.CS_DECISION_REDUCTION);

        // l'affiliation doit être différent de non actif et non actif provisoire
        writer.and("affiliation.mattaf NOT IN (?,?)", CPTaxationDefinitiveManager.CS_TYPE_AFF_NON_ACTIF,
                CPTaxationDefinitiveManager.CS_TYPE_AFF_NON_ACTIF_PROVISOIRE);

        // filtre sur les no affilié plus grand ou égale
        if (!JadeStringUtil.isEmpty(criteria.getStartWithNoAffilie())) {
            writer.and("affiliation.malnaf >= '?'", criteria.getStartWithNoAffilie());
        }

        // filtre sur les no affilié plus petit ou égale
        if (!JadeStringUtil.isEmpty(criteria.getEndWithNoAffilie())) {
            writer.and("affiliation.malnaf <= '?'", criteria.getEndWithNoAffilie());
        }

        // le numéro de passage
        if (!JadeStringUtil.isEmpty(criteria.getNoPassage())) {
            writer.and("decision.ebipas = ?", criteria.getNoPassage());
            addRequestAnneeTaxation(writer);
        }
    }

    private void clauseWhereForDecisionCP(final SQLWriter writer, final ListTaxationsDefinitivesCriteria criteria) {
        // l'année de taxation CP
        if (!JadeStringUtil.isEmpty(criteria.getAnneeTaxationCP())) {
            final String anneeTaxation = criteria.getAnneeTaxationCP();

            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append("(situationprof.vfmaat is null and prestation.vhddeb >= ? and prestation.vhdfin <= ?)");
            builder.append(" or ");
            builder.append("(situationprof.vfmaat is not null and situationprof.vfmaat = decision.iaanne)");
            builder.append(")");

            writer.and("decision.iaanne = ?", anneeTaxation);
            writer.and(builder.toString(), anneeTaxation + "0101", anneeTaxation + "1231");
        }

        if (!JadeStringUtil.isEmpty(criteria.getDateDebutDecisionsCP())
                || !JadeStringUtil.isEmpty(criteria.getDateFinDecisionsCP())) {

            // Date de début décision CP
            if (!JadeStringUtil.isEmpty(criteria.getDateDebutDecisionsCP())) {
                Date debutDecision = new Date(criteria.getDateDebutDecisionsCP());
                writer.and("decision.iadfac >= ?", debutDecision.getValue());
            }

            // Date de fin décision CP
            if (!JadeStringUtil.isEmpty(criteria.getDateFinDecisionsCP())) {
                Date finDecision = new Date(criteria.getDateFinDecisionsCP());
                writer.and("decision.iadfac <= ?", finDecision.getValue());
            }
            addRequestAnneeTaxation(writer);
        }
    }

    private void clauseWhereForAPGAmat(final SQLWriter writer, final ListTaxationsDefinitivesCriteria criteria) {
        // l'année du droit APG/AMAT
        if (!JadeStringUtil.isEmpty(criteria.getAnneeDroit())) {
            writer.and("decision.iaanne = ?", criteria.getAnneeDroit());
            writer.and("droit.vadddr >= ?", criteria.getAnneeDroit() + "0101");
            writer.and("droit.vadddr <= ?", criteria.getAnneeDroit() + "1231");
        }

        if (!JadeStringUtil.isEmpty(criteria.getDateDebutDecompte())
                || !JadeStringUtil.isEmpty(criteria.getDateFinDecompte())) {

            // Date de début Décompte APG/AMAT
            if (!JadeStringUtil.isEmpty(criteria.getDateDebutDecompte())) {
                Date debutDecompte = new Date(criteria.getDateDebutDecompte());
                writer.and("prestation.vhdpmt >= ?", debutDecompte.getValue());
            }

            // Date de fin Décompte APG/AMAT
            if (!JadeStringUtil.isEmpty(criteria.getDateFinDecompte())) {
                Date finDecompte = new Date(criteria.getDateFinDecompte());
                writer.and("prestation.vhdpmt <= ?", finDecompte.getValue());
            }

            addRequestAnneeTaxation(writer);
        }

        // Ne pas inclure les affiliés radiés, si case à cocher non coché
        if (!criteria.getInclureAffilieRadie()) {
            writer.and("(affiliation.madfin is null or affiliation.madfin = ?)", "0");
        }

        if (isAPGAmat(criteria)) {
            writer.and("situationprof.VFBIND = '?'", 1);
        }
    }

    private void addRequestAnneeTaxation(final SQLWriter writer) {
        writer.and("((situationprof.vfmaat is null and prestation.vhddeb >= decision.iaddeb and prestation.vhdfin <= decision.iadfin) or (situationprof.vfmaat is not null and decision.iaanne = situationprof.vfmaat))");
    }

    private void constructClauseOrder(final SQLWriter writer) {
        writer.append(" order by avs.hxnavs ASC");
    }

    private TaxationDefinitiveForList mappingDataWithPojo(CPTaxationDefinitive elem, boolean withRadiation) {

        final String nss = elem.getNss();
        final String numAffilie = elem.getNoAffilie();
        final String designation = getDesignation(session, elem);
        final Date dateDebutPrestation = new Date(elem.getDateDebut());
        final Date dateFinPrestation = new Date(elem.getDateFin());
        final String typePrestation = session.getCode(elem.getTypePrestation());
        final double ecartType = computEcart(elem).roundFloorInt().doubleValue();

        final Integer nbJoursIndemnisation = dateDebutPrestation.getNbDaysBetween(dateFinPrestation) + 1;

        final Montant montantDeterminant = new Montant(
                !JadeStringUtil.isEmpty(elem.getRevenuDeterminant()) ? elem.getRevenuDeterminant() : "0");

        final Montant montantIndependant = new Montant(
                !JadeStringUtil.isEmpty(elem.getRevenuIndependant()) ? elem.getRevenuIndependant() : "0");

        final String codeService = !JadeStringUtil.isEmpty(elem.getCodeService()) ? CodeSystemUtils
                .searchCodeSystemTraduction(elem.getCodeService(), session, session.getIdLangueISO())
                .getCodeUtilisateur() : "";

        final Integer anneeTaxation = !JadeStringUtil.isEmpty(elem.getAnneeTaxation()) ? Integer.parseInt(elem
                .getAnneeTaxation()) : dateDebutPrestation.getYear();

        TaxationDefinitiveForList taxationDefinitive;
        if (withRadiation) {
            final String radie = !JadeStringUtil.isBlankOrZero(elem.getDateFinAffiliation()) ? session.getLabel("OUI")
                    : session.getLabel("NON");
            taxationDefinitive = new TaxationDefinitiveWithRadieForList(radie);
        } else {
            taxationDefinitive = new TaxationDefinitiveWithoutRadieForList();
        }

        taxationDefinitive.setNss(nss);
        taxationDefinitive.setNumAffillie(numAffilie);
        taxationDefinitive.setDesignation(designation);
        taxationDefinitive.setDateDebut(dateDebutPrestation.getSwissValue());
        taxationDefinitive.setDateFin(dateFinPrestation.getSwissValue());
        taxationDefinitive.setRevenuDefinitif(montantDeterminant);
        taxationDefinitive.setApgSurLeRevenu(montantIndependant);
        taxationDefinitive.setEcart(ecartType);
        taxationDefinitive.setNbJoursIndamnisation(nbJoursIndemnisation);
        taxationDefinitive.setCodeService(codeService);
        taxationDefinitive.setAnneeTaxation(anneeTaxation);
        taxationDefinitive.setType(typePrestation);

        return taxationDefinitive;
    }

    private String getDesignation(BSession session, CPTaxationDefinitive elem) {
        String designation = " ";
        try {
            AFAffiliationManager affMgr = new AFAffiliationManager();
            affMgr.setForAffilieNumero(elem.getNoAffilie());
            affMgr.setOrder(AFAffiliation.FIELDNAME_AFF_DDEBUT + " DESC");
            affMgr.setSession(session);
            affMgr.setForTypesAffPersonelles();
            affMgr.find();

            if (!affMgr.isEmpty()) {
                AFAffiliation affiliation = (AFAffiliation) affMgr.getFirstEntity();

                TITiers tiers = new TITiers();
                tiers.setIdTiers(affiliation.getIdTiers());
                tiers.setSession(session);
                tiers.retrieve();

                if (!tiers.isNew() && !JadeStringUtil.isBlankOrZero(tiers.getDesignation1())) {
                    designation += tiers.getDesignation1() + " ";
                }
                if (!tiers.isNew() && !JadeStringUtil.isBlankOrZero(tiers.getDesignation2())) {
                    designation += tiers.getDesignation2() + " ";
                }
            }

        } catch (Exception e) {
            JadeLogger.debug(e, e.getMessage());
            designation = "";
        }
        return designation;
    }

    private Pourcentage computEcart(CPTaxationDefinitive elem) {
        BigDecimal revenuDeterminant = BigDecimal.valueOf(0.00);
        BigDecimal revenuIndependant = BigDecimal.valueOf(0.00);
        BigDecimal ecart = null;

        try {

            if (!JadeStringUtil.isEmpty(elem.getRevenuDeterminant())) {
                revenuDeterminant = new BigDecimal(elem.getRevenuDeterminant());
            }
            if (!JadeStringUtil.isEmpty(elem.getRevenuIndependant())) {
                revenuIndependant = new BigDecimal(elem.getRevenuIndependant());
            }

            ecart = new BigDecimal(revenuIndependant.toString());

            if (revenuDeterminant.doubleValue() > 0.00) {
                ecart = ecart.divide(revenuDeterminant, BigDecimal.ROUND_HALF_EVEN);
                ecart = ecart.multiply(new BigDecimal(100));
                ecart = ecart.subtract(new BigDecimal(100));
            } else {
                ecart = new BigDecimal(100);
            }
        } catch (Exception e) {
            JadeLogger.debug(e, e.getMessage());
            ecart = new BigDecimal(0);
        }

        return new Pourcentage(ecart.doubleValue());
    }

}
