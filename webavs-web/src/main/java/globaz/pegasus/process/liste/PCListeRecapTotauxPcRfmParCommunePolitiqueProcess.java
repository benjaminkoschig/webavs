package globaz.pegasus.process.liste;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.osiris.api.APIEcriture;
import globaz.pegasus.process.PCAbstractJob;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.CommunePolitiqueBean;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

/**
 * Une liste récapitulative des totaux par commune PC et RFM est générée au format Excel (xmlml).
 * La liste contient la somme des prestations versées et restituées par commune durant toute la période concernée
 * 
 * @author sco
 * 
 */
public class PCListeRecapTotauxPcRfmParCommunePolitiqueProcess extends PCAbstractJob {

    private static final long serialVersionUID = 1L;
    private static final String NUMERO_INFOROM = "6510PPC";
    private String dateMonthDebut;
    private String dateMonthFin;
    private Map<String, ContainerByTiers> recapTotauxByIdtiers = new HashMap<String, ContainerByTiers>();
    private Map<String, ContainerByCommunePolitique> recapTotauxByCommunePolitique = new HashMap<String, ContainerByCommunePolitique>();
    private String email = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected void process() throws Exception {

        try {

            // 1. Check les élements d'entrés
            checkValideArguments();

            // ---------------------------------
            // 2. Récupération des données
            // ---------------------------------
            findDataCompta();

            // ---------------------------------
            // 3. Consolidation
            // ---------------------------------
            // Ajout de la commune politique
            addCommunePolitique();

            // Regroupement par Commune politique
            groupDataByCommunePolitique();

            // Tri de la liste
            List<ContainerByCommunePolitique> listTotauxByCommunePolitiqueCollection = new ArrayList<ContainerByCommunePolitique>(
                    recapTotauxByCommunePolitique.values());
            Collections.sort(listTotauxByCommunePolitiqueCollection);

            // ---------------------------------
            // 4. Impression de la liste
            // ---------------------------------
            sendMailWithDoc(createExcelFile(listTotauxByCommunePolitiqueCollection));

        } catch (Exception e) {
            JadeLogger.error("An error occurred while generating prestation list by commune politique", e);
            sendMailError(e);
        }

    }

    private void findDataCompta() throws PropertiesException, JAException {

        String idRubriquePC = EPCProperties.COMMUNE_POLITIQUE_NUMERO_RUBRIQUE_PC.getValue();
        String idRubriqueRFM = EPCProperties.COMMUNE_POLITIQUE_NUMERO_RUBRIQUE_RFM.getValue();

        List<String> listIdRubriquePC = Arrays.asList(idRubriquePC.trim().split("\\s*,\\s*"));
        List<String> listIdRubriqueRFM = Arrays.asList(idRubriqueRFM.trim().split("\\s*,\\s*"));

        List<PaiementComptablePcRfmBean> listPaiementPcRfmFromRubrique = QueryExecutor.execute(
                getSqlOperationComptaFromIdRubrique(listIdRubriquePC, listIdRubriqueRFM),
                PaiementComptablePcRfmBean.class);

        for (PaiementComptablePcRfmBean bean : listPaiementPcRfmFromRubrique) {

            ContainerByTiers con = recapTotauxByIdtiers.get(bean.getIdTiers());
            if (con == null) {
                con = new ContainerByTiers(bean.getIdTiers());
            }

            if (listIdRubriquePC.contains(bean.getNumeroRubrique().trim())) {
                // Si crédit = sort de la caisse, donc paiement
                if (APIEcriture.CREDIT.equals(bean.getCodeDebitCredit())) {
                    con.addMontantPaiementPC(bean.getMontant());
                }

                if (APIEcriture.DEBIT.equals(bean.getCodeDebitCredit())) {
                    con.addMontantRestitutionPC(bean.getMontant());
                }
            } else if (listIdRubriqueRFM.contains(bean.getNumeroRubrique().trim())) {
                // Si crédit = sort de la caisse, donc paiement
                if (APIEcriture.CREDIT.equals(bean.getCodeDebitCredit())) {
                    con.addMontantPaiementRFM(bean.getMontant());
                }

                if (APIEcriture.DEBIT.equals(bean.getCodeDebitCredit())) {
                    con.addMontantRestitutionRFM(bean.getMontant());
                }
            } else {
                JadeLogger.warn(this, "Code référence inconnue : " + bean.getNumeroRubrique());
            }

            recapTotauxByIdtiers.put(bean.getIdTiers(), con);
        }

    }

    private void sendMailError(Exception e) {

        String subject = getSession().getLabel("PEGASUS_JAVA_LISTE_RECAP_TOTAUX_SUBJECT_MAIL");
        String body = getSession().getLabel("PEGASUS_JAVA_LISTE_RECAP_TOTAUX_BODY_MAIL_ERROR");

        body += "\n\n\n***** INFORMATIONS POUR GLOBAZ *****\n";
        body += stack2string(e);

        try {
            JadeSmtpClient.getInstance().sendMail(getEmail(), subject, body, null);
        } catch (Exception e1) {
            JadeLogger.error("Unabled to send mail to " + getEmail(), e);
        }

    }

    /**
     * Check des éléments d'entrés
     */
    public void checkValideArguments() {

        // Test si la date de début est présente
        if (dateMonthDebut == null || JadeStringUtil.isBlankOrZero(dateMonthDebut)) {
            throw new IllegalArgumentException(getSession().getLabel("PEGASUS_JAVA_LISTE_RECAP_TOTAUX_DATE_DEBUT_VIDE"));
        }

        // Test si date de début valide
        if (!Date.isValid(dateMonthDebut)) {
            throw new IllegalArgumentException(getSession().getLabel(
                    "PEGASUS_JAVA_LISTE_RECAP_TOTAUX_DATE_DEBUT_NON_VALIDE"));
        }

        // Test si date de début dans le futur
        if (new Date(dateMonthDebut).compareTo(new Date()) > 0) {
            throw new IllegalArgumentException(getSession()
                    .getLabel("PEGASUS_JAVA_LISTE_RECAP_TOTAUX_DATE_DEBUT_FUTUR"));
        }

        // Si une date de fin est présente
        if (dateMonthFin != null && !JadeStringUtil.isBlankOrZero(dateMonthFin)) {

            // test si valide
            if (!Date.isValid(dateMonthFin)) {
                throw new IllegalArgumentException(getSession().getLabel(
                        "PEGASUS_JAVA_LISTE_RECAP_TOTAUX_DATE_FIN_NON_VALIDE"));
            }

            // Test si date de début > date de fin
            if (new Date(dateMonthDebut).compareTo(new Date(dateMonthFin)) > 0) {
                throw new IllegalArgumentException(getSession().getLabel(
                        "PEGASUS_JAVA_LISTE_RECAP_TOTAUX_DATE_DEBUT_PLUS_DATE_FIN"));
            }
        }

    }

    private String getSqlOperationComptaFromIdRubrique(List<String> idRubriquePC, List<String> idRubriqueRFM)
            throws PropertiesException, JAException {

        StringBuilder sbIdRubriquePC = new StringBuilder();
        for (int i = 0; i < idRubriquePC.size(); i++) {
            sbIdRubriquePC.append("'");
            sbIdRubriquePC.append(idRubriquePC.get(i));
            sbIdRubriquePC.append("'");
            if (i < idRubriquePC.size() - 1) {
                sbIdRubriquePC.append(",");
            }
        }

        StringBuilder sbIdRubriqueRFM = new StringBuilder();
        for (int i = 0; i < idRubriqueRFM.size(); i++) {
            sbIdRubriqueRFM.append("'");
            sbIdRubriqueRFM.append(idRubriqueRFM.get(i));
            sbIdRubriqueRFM.append("'");
            if (i < idRubriqueRFM.size() - 1) {
                sbIdRubriqueRFM.append(",");
            }
        }

        String dateDebut = new Date(dateMonthDebut).getValueMonth();
        String dateFin = new Date().getValue();
        if (!JadeStringUtil.isEmpty(dateMonthFin)) {
            dateFin = new Date(dateMonthFin).getValueMonth() + "31";
        } else {
            dateMonthFin = new Date().getSwissMonthValue();
        }

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("    schema.CACPTAP.IDTIERS AS idtiers,  ");
        sql.append("    sum(schema.CAOPERP.MONTANT) as MONTANT,  ");
        sql.append("    schema.CAOPERP.CODEDEBITCREDIT as CODEDEBITCREDIT,  ");
        sql.append("    schema.CARUBRP.IDEXTERNE as NUMERORUBRIQUE  ");
        sql.append("FROM   schema.cacptap  ");
        sql.append("    INNER JOIN schema.CAOPERP ON schema.CAOPERP.IDCOMPTEANNEXE = schema.CACPTAP.IDCOMPTEANNEXE  ");
        sql.append("    INNER JOIN schema.CARUBRP on schema.CARUBRP.IDRUBRIQUE = schema.CAOPERP.IDCOMPTE  ");
        sql.append("WHERE  ");
        sql.append("  schema.cacptap.IDROLE = 517038 ");
        sql.append("  and schema.CAOPERP.date BETWEEN ").append(dateDebut).append("01 AND ");
        sql.append(dateFin);
        sql.append("  and schema.caoperp.ETAT = 205002 ");
        sql.append("  and schema.caoperp.IDTYPEOPERATION like 'E%' ");
        sql.append("  and schema.CARUBRP.IDEXTERNE in (" + sbIdRubriquePC.toString());
        if (!JadeStringUtil.isEmpty(sbIdRubriqueRFM.toString())) {
            sql.append(",").append(sbIdRubriqueRFM.toString());
        }
        sql.append(") GROUP BY schema.CACPTAP.IDTIERS, schema.CAOPERP.CODEDEBITCREDIT, schema.CARUBRP.IDEXTERNE");

        return sql.toString();
    }

    private void addCommunePolitique() {

        Set<String> setIdTiers = recapTotauxByIdtiers.keySet();
        Map<String, CommunePolitiqueBean> mapIdTiersCommunePolitique = new HashMap<String, CommunePolitiqueBean>();

        mapIdTiersCommunePolitique = PRTiersHelper.findCommunePolitique(setIdTiers, new Date().getDate(), getSession());

        for (String idTiers : setIdTiers) {
            CommunePolitiqueBean communePolitique = mapIdTiersCommunePolitique.get(idTiers);
            if (communePolitique != null) {
                ContainerByTiers con = recapTotauxByIdtiers.get(idTiers);
                con.setCommunePolitique(communePolitique);
            }
        }
    }

    private void groupDataByCommunePolitique() {
        Iterator<ContainerByTiers> values = recapTotauxByIdtiers.values().iterator();

        while (values.hasNext()) {
            ContainerByTiers value = values.next();

            ContainerByCommunePolitique con = recapTotauxByCommunePolitique.get(value.getCodeCommunePolitique());
            if (con == null) {
                con = new ContainerByCommunePolitique(value.getCommunePolitique());
            }

            con.addMontantPaiementPC(value.getMontantPaiementPC());
            con.addMontantRestitutionPC(value.getMontantRestitutionPC());
            con.addMontantPaiementRFM(value.getMontantPaiementRFM());
            con.addMontantRestitutionRFM(value.getMontantRestitutionRFM());

            recapTotauxByCommunePolitique.put(value.getCodeCommunePolitique(), con);
        }
    }

    private String createExcelFile(List<ContainerByCommunePolitique> containerCP) {

        SimpleOutputListBuilder simpleOut = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath(NUMERO_INFOROM).addList(containerCP)
                .classElementList(ContainerByCommunePolitique.class);

        String titre = NUMERO_INFOROM + "_" + getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_TITRE_RECAP_TOTAUX");
        String genererLe = getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_GENERE_LE");
        String periodeConcerne = getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_PERIODE_CONCERNE");

        Details paramsData = new Details();
        paramsData.add(genererLe, JACalendar.todayJJsMMsAAAA());
        paramsData.newLigne();
        paramsData.add(periodeConcerne, getDateMonthDebut() + " - " + getDateMonthFin());
        paramsData.newLigne();
        paramsData.add(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()),
                getSession().getUserId());

        File file = simpleOut.addTitle(titre, Align.LEFT).addHeaderDetails(paramsData).asXls().build();

        return file.getAbsolutePath();

    }

    private void sendMailWithDoc(String fileName) throws Exception {

        String[] tabFileName = { fileName };

        String subject = getSession().getLabel("PEGASUS_JAVA_LISTE_RECAP_TOTAUX_SUBJECT_MAIL");
        String body = getSession().getLabel("PEGASUS_JAVA_LISTE_RECAP_TOTAUX_BODY_MAIL_OK");

        JadeSmtpClient.getInstance().sendMail(getEmail(), subject, body, tabFileName);

    }

    public String getDateMonthDebut() {
        return dateMonthDebut;
    }

    public void setDateMonthDebut(String dateMonthDebut) {
        this.dateMonthDebut = dateMonthDebut;
    }

    public String getDateMonthFin() {
        return dateMonthFin;
    }

    public void setDateMonthFin(String dateMonthFin) {
        this.dateMonthFin = dateMonthFin;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("PEGASUS_JAVA_LISTE_RECAP_TOTAUX_DESCRIPTION");
    }

    @Override
    public String getName() {
        return "PCListeRecapTotauxPcRfmParCommunePolitiqueProcess";
    }

    public static String stack2string(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "------\r\n" + sw.toString() + "------\r\n";
        } catch (Exception e2) {
            return "bad stack2string";
        }
    }
}
