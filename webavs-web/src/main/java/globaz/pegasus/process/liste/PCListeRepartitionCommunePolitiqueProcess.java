package globaz.pegasus.process.liste;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pegasus.process.PCAbstractJob;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.pegasus.business.domaine.pca.PcaEtat;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

/**
 * Permet l'impression de la liste des bénéficiaires PC (ou PC/Rente) en cours par commune politique
 * 
 * @author sco
 * 
 */
public class PCListeRepartitionCommunePolitiqueProcess extends PCAbstractJob {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_LISTE_PC_RENTE = "listePcRente";
    public static final String TYPE_LISTE_PC = "listePc";

    private String email = null;
    private String typeListe = "";

    private String labelNomDocument = "";
    private String labelGenereLe = "";
    private String labelSubjectMailOk = "";
    private String labelSubjectMailko = "";
    private String labelBodyMailOk = "";
    private String labelBodyMailError = "";
    private String labelDescription = "";
    private String labelUser = "";

    @Override
    protected void process() throws Exception {
        try {

            findAllLabel();

            String dateDuJourAMJ = (new JADate(JACalendar.todayJJsMMsAAAA())).toAMJ().toString();

            List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP = loadDataPC(dateDuJourAMJ);

            if (TYPE_LISTE_PC_RENTE.equalsIgnoreCase(typeListe)) {
                listBeneficiairePCCP.addAll(loadDataRente(dateDuJourAMJ));
            }

            addCommunePolitique(listBeneficiairePCCP);

            sendMailWithDoc(createExcelFile(regroupByCommunePolitique(listBeneficiairePCCP)));

        } catch (Exception e) {
            JadeLogger.error("An error occurred while generating prestation list by commune politique", e);
            sendMailError(e);
        }
    }

    private void sendMailError(Exception e) {

        labelBodyMailError += "\n\n\n***** INFORMATIONS POUR GLOBAZ *****\n";
        labelBodyMailError += stack2string(e);

        try {
            JadeSmtpClient.getInstance().sendMail(getEmail(), labelSubjectMailko, labelBodyMailError, null);
        } catch (Exception e1) {
            JadeLogger.error("Unabled to send mail to " + getEmail(), e);
        }
    }

    private void findAllLabel() {
        if (TYPE_LISTE_PC_RENTE.equalsIgnoreCase(typeListe)) {
            labelNomDocument = getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_TITRE_BENEFICIARE_PC_RENTE");
            labelSubjectMailOk = getSession().getLabel("PEGASUS_LISTE_MAIL_SUBJECT_BENEFICIARE_PC_RENTE_OK");
            labelSubjectMailko = getSession().getLabel("PEGASUS_LISTE_MAIL_SUBJECT_BENEFICIARE_PC_RENTE_ERROR");
            labelBodyMailOk = getSession().getLabel("PEGASUS_LISTE_BODY_SUBJECT_BENEFICIARE_PC_RENTE_OK");
            labelBodyMailError = getSession().getLabel("PEGASUS_LISTE_BODY_SUBJECT_BENEFICIARE_PC_RENTE_KO");
            labelDescription = getSession().getLabel("PEGASUS_LISTE_BENEFICIARE_PC_RENTE_DESCRIPTION");
        } else {
            labelNomDocument = getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_TITRE_BENEFICIARE_PC");
            labelSubjectMailOk = getSession().getLabel("PEGASUS_LISTE_MAIL_SUBJECT_BENEFICIARE_PC_OK");
            labelSubjectMailko = getSession().getLabel("PEGASUS_LISTE_MAIL_SUBJECT_BENEFICIARE_PC_ERROR");
            labelBodyMailOk = getSession().getLabel("PEGASUS_LISTE_BODY_SUBJECT_BENEFICIARE_PC_OK");
            labelBodyMailError = getSession().getLabel("PEGASUS_LISTE_BODY_SUBJECT_BENEFICIARE_PC_KO");
            labelDescription = getSession().getLabel("PEGASUS_LISTE_BENEFICIARE_PC_DESCRIPTION");
        }

        labelGenereLe = getSession().getLabel("PEGASUS_LISTE_EXCEL_CP_GENERE_LE");
        labelUser = getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey());
    }

    private Map<String, List<BeneficiairePCCommunePolitiquePojo>> regroupByCommunePolitique(
            List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP) {

        Map<String, List<BeneficiairePCCommunePolitiquePojo>> mapByCommunPolitique = new HashMap<String, List<BeneficiairePCCommunePolitiquePojo>>();

        for (BeneficiairePCCommunePolitiquePojo pojo : listBeneficiairePCCP) {

            List<BeneficiairePCCommunePolitiquePojo> sousListe = mapByCommunPolitique.get(pojo.getCommunePolitique());
            if (sousListe == null) {
                sousListe = new ArrayList<BeneficiairePCCommunePolitiquePojo>();
            }
            sousListe.add(pojo);
            mapByCommunPolitique.put(pojo.getCommunePolitique(), sousListe);
        }

        return mapByCommunPolitique;
    }

    /**
     * Récupération des données des PC
     * 
     * @param dateAMJ
     * @return
     */
    private List<BeneficiairePCCommunePolitiquePojo> loadDataPC(String dateAMJ) {

        List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCPPojo = new ArrayList<BeneficiairePCCommunePolitiquePojo>();

        listBeneficiairePCCPPojo = QueryExecutor.execute(getSqlSelectBeneficiairePC(dateAMJ),
                BeneficiairePCCommunePolitiquePojo.class);

        return listBeneficiairePCCPPojo;

    }

    /**
     * Récupération des données des rentes
     * 
     * @param dateAMJ
     * @return
     */
    private List<BeneficiairePCCommunePolitiquePojo> loadDataRente(String dateAMJ) {

        List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCPPojo = new ArrayList<BeneficiairePCCommunePolitiquePojo>();

        listBeneficiairePCCPPojo = QueryExecutor.execute(getSqlSelectBeneficiaireRente(dateAMJ),
                BeneficiairePCCommunePolitiquePojo.class);

        return listBeneficiairePCCPPojo;

    }

    private String createExcelFile(Map<String, List<BeneficiairePCCommunePolitiquePojo>> mapByCommunPolitique) {

        String filePath = Jade.getInstance().getPersistenceDir() + JadeUUIDGenerator.createStringUUID();
        Locale locale = new Locale(BSessionUtil.getSessionFromThreadContext().getIdLangueISO());

        SimpleOutputListBuilder simpleList = SimpleOutputListBuilder.newInstance().local(locale);

        Details paramsData = new Details();
        paramsData.add(labelGenereLe, JACalendar.todayJJsMMsAAAA());
        paramsData.newLigne();
        paramsData.add(labelUser, getSession().getUserId());

        Iterator<List<BeneficiairePCCommunePolitiquePojo>> ite = mapByCommunPolitique.values().iterator();
        while (ite.hasNext()) {

            List<BeneficiairePCCommunePolitiquePojo> sousListe = ite.next();
            String sheetName = sousListe.get(0).getCommunePolitique();
            simpleList.addList(sousListe).classElementList(BeneficiairePCCommunePolitiquePojo.class)
                    .addHeaderDetails(paramsData);

            if (!JadeStringUtil.isEmpty(sheetName)) {
                simpleList.addSubTitle(sheetName.replaceAll("\\*", "all"));
            }

            if (ite.hasNext()) {
                simpleList.jump();
            }
        }

        simpleList.addTitle(labelNomDocument, Align.LEFT);

        File file = simpleList.asXls().outputName(filePath).build();

        return file.getAbsolutePath();

    }

    /**
     * Ajout la commune politque a toutes les occurences de la liste
     * 
     * @param listBeneficiairePCCP
     */
    private void addCommunePolitique(List<BeneficiairePCCommunePolitiquePojo> listBeneficiairePCCP) {

        Set<String> setIdTiers = new HashSet<String>();
        Map<String, String> mapIdTiersCommunePolitique = new HashMap<String, String>();

        for (BeneficiairePCCommunePolitiquePojo aBeneficiairePCCP : listBeneficiairePCCP) {
            setIdTiers.add(aBeneficiairePCCP.getIdTiers());
        }

        mapIdTiersCommunePolitique = PRTiersHelper.getCommunePolitique(setIdTiers, new Date(), getSession());

        for (BeneficiairePCCommunePolitiquePojo aBeneficiairePCCP : listBeneficiairePCCP) {
            String communePolitique = mapIdTiersCommunePolitique.get(aBeneficiairePCCP.getIdTiers());
            if (!JadeStringUtil.isEmpty(communePolitique)) {
                aBeneficiairePCCP.setCommunePolitique(communePolitique);
            }
        }

        Collections.sort(listBeneficiairePCCP);

    }

    /**
     * Envoi du mail avec le document
     */
    private void sendMailWithDoc(String fileName) throws Exception {

        String[] tabFileName = { fileName };

        JadeSmtpClient.getInstance().sendMail(getEmail(), labelSubjectMailOk, labelBodyMailOk, tabFileName);
    }

    private String getSqlSelectBeneficiairePC(String dateAMJ) {

        StringBuilder sql = new StringBuilder("");

        sql.append(" SELECT tier.HTITIE as idTiers, pavs.HXNAVS as nss, tier.HTLDE1 as nom, tier.HTLDE2 as prenom, prac.ZTLCPR as codePrestation,  prac.ZTMPRE as montant ");
        sql.append(" FROM SCHEMA.REPRACC prac ");
        sql.append(" inner join SCHEMA.PCPCACC pcac on (prac.ZTIPRA = pcac.CUIPRA) ");
        sql.append(" inner join SCHEMA.TITIERP tier on (prac.ZTITBE = tier.HTITIE) ");
        sql.append(" inner join SCHEMA.TIPAVSP pavs on (pavs.htitie = tier.HTITIE) ");
        sql.append(" WHERE prac.ZTTGEN = " + IREPrestationAccordee.CS_GENRE_PC
                + " and (prac.ZTDFDR = 0 or prac.ZTDFDR >= " + dateAMJ + ") and pcac.CUTETA = "
                + PcaEtat.VALIDE.getValue());

        return sql.toString();
    }

    private String getSqlSelectBeneficiaireRente(String dateAMJ) {

        StringBuilder sql = new StringBuilder("");

        sql.append(" SELECT tier.HTITIE as idTiers, pavs.HXNAVS as nss, tier.HTLDE1 as nom, tier.HTLDE2 as prenom, prac.ZTLCPR as codePrestation,  prac.ZTMPRE as montantPrestation ");
        sql.append(" FROM SCHEMA.REPRACC prac ");
        sql.append(" inner join SCHEMA.REREACC reac on (prac.ZTIPRA = reac.YLIRAC) ");
        sql.append(" inner join SCHEMA.REBACAL baca on (reac.YLIBAC = baca.YIIBCA) ");
        sql.append(" inner join SCHEMA.TITIERP tier on (baca.YIITBC = tier.HTITIE) ");
        sql.append(" inner join SCHEMA.TIPAVSP pavs on (pavs.htitie = tier.HTITIE) ");
        sql.append(" WHERE prac.ZTTGEN = " + IREPrestationAccordee.CS_GENRE_RENTES
                + " and (prac.ZTDFDR = 0 or prac.ZTDFDR >= " + dateAMJ + ") ");

        return sql.toString();

    }

    public String getTypeListe() {
        return typeListe;
    }

    public void setTypeListe(String typeListe) {
        this.typeListe = typeListe;
    }

    @Override
    public String getDescription() {
        return labelDescription;
    }

    @Override
    public String getName() {
        return "PCListeRepartitionCommunePolitiqueProcess";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
