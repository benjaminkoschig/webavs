package ch.globaz.orion.businessimpl.services.pucs;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.exceptions.OrionPucsException;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.services.pucs.PucsService;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.ws.service.UtilsService;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.converter.Translater;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;
import ch.globaz.xmlns.eb.dan.EBDanException_Exception;
import ch.globaz.xmlns.eb.partnerweb.User;
import ch.globaz.xmlns.eb.pucs.LienInstitution;
import ch.globaz.xmlns.eb.pucs.PUCSService;
import ch.globaz.xmlns.eb.pucs.PucsEntrySummary;
import ch.globaz.xmlns.eb.pucs.PucsSearchOrderByEnum;

public class PucsServiceImpl implements PucsService {

    public static final String NUMERO_INFORM_PUCS_LISIBLE = "0315CEB";

    public static byte[] downloadFile(String id, String type, String loginName, String userEmail, String langueIso)
            throws OrionPucsException {
        try {
            Checkers.checkNotNull(id, "id");
            Checkers.checkNotNull(type, "type");

            User usr = ServicesProviders.partnerWebServiceProvide(loginName, userEmail, langueIso)
                    .readActivUserOrAdminByLoginName(loginName);

            return ServicesProviders.pucsServiceProvide(loginName, userEmail, langueIso).getPucsFile(
                    Integer.parseInt(id), usr.getUserId());

        } catch (Exception e) {
            throw new OrionPucsException("Impossible de t�l�charger le ficheir PUCS", e);
        }

    }

    public static PucsEntrySummary getPucsEntry(String idPucsEntry, BSession session) throws OrionPucsException {
        try {
            return ServicesProviders.pucsServiceProvide(session).getPucsEntrySummary(Integer.parseInt(idPucsEntry));
        } catch (Exception e) {
            throw new OrionPucsException("Impossible d'obtenir le contenu du fichier", e);
        }
    }

    public static List<PucsEntrySummary> listPucsFile(String type, int size, String likeAffilie, String forAnnee,
            String dateSoumission, BSession session) throws OrionPucsException {
        try {
            return ServicesProviders.pucsServiceProvide(session).getPucsSummariesForWebAvs(size, likeAffilie, forAnnee,
                    dateSoumission, PucsSearchOrderByEnum.AFFILIE_ORDER);
        } catch (Exception e) {
            throw new OrionPucsException("Impossible de lister les fichires PUCS", e);
        }

    }

    public static void notifyFinished(String id, BSession session) throws OrionPucsException {
        try {
            User usr = ServicesProviders.partnerWebServiceProvide(session).readActivUserOrAdminByLoginName(
                    session.getUserId());
            ServicesProviders.pucsServiceProvide(session).acceptPucs(Integer.parseInt(id), usr.getUserId());
        } catch (Exception e) {
            throw new OrionPucsException("Impossible de mettre � jour le statut", e);
        }

    }

    @Override
    public List<PucsEntrySummary> listPucsFile(String type, int size, String likeAffilie, String forAnnee,
            String dateSoumission, PucsSearchOrderByEnum orderby, BSession session) throws OrionPucsException {

        return null;
    }

    /**
     * Permet de r�cup�rer le fichier pucs dans le back web
     * 
     * @param id
     * @param provenance
     * @param etatSwissDecPucsFile
     * @param workDirectory
     * @param bSession
     * @return
     */
    public static String retrieveFile(String id, DeclarationSalaireProvenance provenance,
            EtatSwissDecPucsFile etatSwissDecPucsFile, String workDirectory, String loginName, String userEmail,
            String langueIso) {
        byte[] fileContent = null;
        String filePath = workDirectory + id + ".xml";
        try {
            if (provenance.isPucs()) {
                fileContent = PucsServiceImpl.downloadFile(id, provenance.getValue(), loginName, userEmail, langueIso);
            } else if (provenance.isDan()) {
                fileContent = DanServiceImpl.downloadFile(id, provenance.getValue(), loginName, userEmail, langueIso);
            } else if (provenance.isSwissDec()) {
                String file = JadeFsFacade.readFile(etatSwissDecPucsFile.getPath() + id + ".xml");
                JadeFsFacade.copyFile(file, workDirectory + id + ".xml");
            }

            if (fileContent != null) {
                OutputStreamWriter os = null;
                CharsetEncoder utf8 = Charset.forName("UTF-8").newEncoder();
                try {
                    os = new OutputStreamWriter(new FileOutputStream(filePath), utf8);
                    os.write(new String(fileContent, utf8.charset()));
                    os.flush();
                } finally {
                    if (os != null) {
                        os.close();
                    }
                }
            }
        } catch (OrionPucsException e) {
            throw new CommonTechnicalException(e);
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
        return filePath;
    }

    public static String retrieveFile(String id, DeclarationSalaireProvenance provenance,
            EtatSwissDecPucsFile etatSwissDecPucsFile, String workDirectory) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        return retrieveFile(id, provenance, etatSwissDecPucsFile, workDirectory, session.getUserId(),
                session.getUserEMail(), session.getIdLangueISO());
    }

    @Override
    public String pucFileLisible(String id, String provenance, String etatSwissDecPucsFile) {
        return pucFileLisiblePdf(id, DeclarationSalaireProvenance.valueOf(provenance),
                EtatSwissDecPucsFile.valueOf(etatSwissDecPucsFile), BSessionUtil.getSessionFromThreadContext());
    }

    @Override
    public String pucFileLisibleXls(String id, String provenance, String etatSwissDecPucsFile) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        Locale locale = buildLocale(session);

        SimpleOutputListBuilder builder = SimpleOutputListBuilder.newInstance().local(locale).asXls();

        File file = out(id, DeclarationSalaireProvenance.valueOf(provenance),
                EtatSwissDecPucsFile.valueOf(etatSwissDecPucsFile), builder, session);
        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    @Override
    public String pucFileLisibleXml(String id, String provenance, String etatSwissDecPucsFile) {
        String pathFile = retrieveFile(id, DeclarationSalaireProvenance.valueOf(provenance),
                EtatSwissDecPucsFile.valueOf(etatSwissDecPucsFile));
        return JadeFilenameUtil.normalizePathComponents(pathFile);
    }

    public static String pucFileLisiblePdf(String id, DeclarationSalaireProvenance provenance,
            EtatSwissDecPucsFile etatSwissDecPucsFile, BSession session) {
        Locale locale = buildLocale(session);
        SimpleOutputListBuilder builder = SimpleOutputListBuilder.newInstance().asPdf().local(locale);

        File file = out(id, provenance, etatSwissDecPucsFile, builder, session);
        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    @Override
    public String pucsFileLisibleForEbusiness(String id, DeclarationSalaireProvenance provenance, String format,
            String loginName, String userEmail, String langue) {
        Locale locale = new Locale(langue);
        SimpleOutputListBuilder builder;

        if ("pdf".equals(format)) {
            builder = SimpleOutputListBuilder.newInstance().asPdf().local(locale);
        } else if ("xls".equals(format)) {
            builder = SimpleOutputListBuilder.newInstance().asXls().local(locale);
        } else {
            throw new IllegalArgumentException("the format " + format + " is not allowed");
        }
        File file = outForEbusiness(id, provenance, null, builder, loginName, userEmail, langue);
        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    public static String pucFileLisiblePdf(DeclarationSalaireProvenance provenance, ElementsDomParser parser,
            BSession session) {

        Locale locale = buildLocale(session);
        SimpleOutputListBuilder builder = SimpleOutputListBuilder.newInstance().asPdf().local(locale);

        File file = out(provenance, builder, parser, session);
        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    public static Integer findIdInstitution(int type, int idPucs, BSession session) throws EBDanException_Exception {
        PUCSService service = null;

        service = ServicesProviders.pucsServiceProvide(session);
        LienInstitution lienInstitution = service.findPucsInstitutionForUtilisation(idPucs, type);
        if (lienInstitution != null) {
            return lienInstitution.getIdInstitution();
        }
        return null;
    }

    private static File out(String id, DeclarationSalaireProvenance provenance,
            EtatSwissDecPucsFile etatSwissDecPucsFile, SimpleOutputListBuilder generator, BSession session) {
        ElementsDomParser parser = buildElementDomParser(id, provenance, etatSwissDecPucsFile, session.getUserId(),
                session.getUserEMail(), session.getIdLangueISO());
        return out(provenance, generator, parser, session);
    }

    private static File outForEbusiness(String id, DeclarationSalaireProvenance provenance,
            EtatSwissDecPucsFile etatSwissDecPucsFile, SimpleOutputListBuilder generator, String loginName,
            String userEmail, String langueIso) {
        ElementsDomParser parser = buildElementDomParser(id, provenance, etatSwissDecPucsFile, loginName, userEmail,
                langueIso);
        BSession session = UtilsService.initSession();
        return out(provenance, generator, parser, session);
    }

    public static ElementsDomParser buildElementDomParser(String id, DeclarationSalaireProvenance provenance,
            EtatSwissDecPucsFile etatSwissDecPucsFile, String loginName, String userEmail, String langueIso) {
        String pathFile = retrieveFile(id, provenance, etatSwissDecPucsFile,
                (Jade.getInstance().getHomeDir() + "work/"), loginName, userEmail, langueIso);
        ElementsDomParser parser = new ElementsDomParser(pathFile);
        try {
            JadeFsFacade.delete(pathFile);
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
        return parser;
    }

    private static File out(DeclarationSalaireProvenance provenance, SimpleOutputListBuilder builder,
            ElementsDomParser parser, BSession session) {

        Translater translater = new LabelTranslater(session, "DS_PUCS");
        DeclarationSalaire ds = DeclarationSalaireBuilder.build(parser, provenance);

        List<SalaryForList> list = DeclarationSalaireBuilder.buildForList(ds.getEmployees());

        Details paramsData = new Details();

        paramsData.add(session.getLabel("NO_AFFILIE"), ds.getNumeroAffilie());
        paramsData.add(session.getLabel("RAISON_SOCILALE"), ds.getNom());

        paramsData.newLigne();
        paramsData.add(session.getLabel("DECLARATION"), String.valueOf(ds.getAnnee()));

        if (ds.isAfSeul()) {
            paramsData.add(session.getLabel("AF_SEUL"), session.getLabel("OUI"));
        } else {
            paramsData.add(session.getLabel("AF_SEUL"), session.getLabel("NON"));
        }
        paramsData.add(session.getLabel("NB_SALAIRE"), String.valueOf(ds.getEmployees().size()));
        paramsData.add(session.getLabel("PUCS_TYPE"), session.getLabel(provenance.getLabel()));

        paramsData.newLigne();
        paramsData.add(session.getLabel("MONTANT_AVS"), ds.getMontantAvs().toStringFormat());
        paramsData.add(session.getLabel("MONTANT_AC1"), ds.getMontantAc1().toStringFormat());
        paramsData.add(session.getLabel("MONTANT_AC2"), ds.getMontantAc2().toStringFormat());
        paramsData.add(session.getLabel("MONTANT_CAF"), ds.getMontantCaf().toStringFormat());

        paramsData.newLigne();
        String name = (Jade.getInstance().getPersistenceDir() + ds.getNumeroAffilie() + "list_" + JadeUUIDGenerator
                .createStringUUID());

        builder.addList(list)
                .classElementList(SalaryForList.class)
                .addHeaderDetails(paramsData)
                .addTitle(
                        session.getLabel("TITRE_LIST_SALAIRE") + "(" + PucsServiceImpl.NUMERO_INFORM_PUCS_LISIBLE + ")",
                        Align.RIGHT).translater(translater);

        File file = builder.outputName(name).build();

        return file;
    }

    public static String retrieveFile(String id, DeclarationSalaireProvenance provenance,
            EtatSwissDecPucsFile etatSwissDecPucsFile, BSession bSession) {
        return retrieveFile(id, provenance, etatSwissDecPucsFile, (Jade.getInstance().getHomeDir() + "work/"),
                bSession.getUserId(), bSession.getUserEMail(), bSession.getIdLangueISO());
    }

    private static String retrieveFile(String id, DeclarationSalaireProvenance provenance,
            EtatSwissDecPucsFile etatSwissDecPucsFile) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        return retrieveFile(id, provenance, etatSwissDecPucsFile, (Jade.getInstance().getHomeDir() + "work/"),
                session.getUserId(), session.getUserEMail(), session.getIdLangueISO());
    }

    private static Locale buildLocale(BSession session) {
        Locale locale = new Locale(session.getIdLangueISO());
        return locale;
    }

    public static boolean userHasRight(PucsFile pucsFile, EtatSwissDecPucsFile etatSwissDecPucsFile, BSession session) {
        boolean hasRight;
        try {
            // String path = PucsServiceImpl.retrieveFile(pucsFile.getId(), pucsFile.getProvenance(),
            // etatSwissDecPucsFile, session);
            // ElementsDomParser parser = new ElementsDomParser(path);

            List<AFAffiliation> affiliations = AFAffiliationServices.searchAffiliationByNumeros(
                    Arrays.asList(pucsFile.getNumeroAffilie().trim()), session);
            if (affiliations.isEmpty()) {
                hasRight = false;
            } else {
                hasRight = AFAffiliationServices.hasRightAccesSecurity(affiliations.get(0), session);

                // hasRight = userHasRight(affiliations.get(0), parser, session);
            }

            // JadeFsFacade.delete(path);
        } catch (Exception e) {
            throw new CommonTechnicalException(pucsFile.toString(), e);
        }
        return hasRight;
    }

    public static boolean userHasRight(AFAffiliation affiliation, ElementsDomParser parser, BSession session) {
        boolean hasSecurity = true;
        if (affiliation != null) {
            // List<String> listNss = parser.findValues("Person SV-AS-Number");
            // List<List<String>> splitedNSSList = split(listNss, 1000);
            // for (List<String> splitedNss : splitedNSSList) {
            // String nssIn = '\'' + Joiner.on("','").skipNulls().join(splitedNss).replace(".", "") + '\'';
            // BigDecimal code = QueryExecutor.executeAggregate(
            // "select max(KATSEC) from schema.CIINDIP where KANAVS in (" + nssIn + ")", session);
            // if (!BigDecimal.ZERO.equals(code)) {
            // String codeSecurity = code.toString();
            // Integer ciSecurity = Integer.parseInt(codeSecurity.substring(codeSecurity.length() - 1));
            // hasSecurity = AFAffiliationServices.hasRightAccesSecurity(ciSecurity, session);
            // if (hasSecurity) {
            // break;
            // }
            // }
            // }

            // if (hasSecurity) {
            hasSecurity = AFAffiliationServices.hasRightAccesSecurity(affiliation, session);
            // }
        }
        return hasSecurity;
    }

}
