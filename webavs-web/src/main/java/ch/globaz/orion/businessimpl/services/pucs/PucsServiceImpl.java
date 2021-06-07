package ch.globaz.orion.businessimpl.services.pucs;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.exceptions.OrionPucsException;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.services.pucs.PucsService;
import ch.globaz.orion.businessimpl.services.ServicesProviders;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.service.EBPucsFileService;
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
import ch.globaz.xmlns.eb.pucs.PucsStatusEnum;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.star.uno.RuntimeException;

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
            throw new OrionPucsException("Impossible de télécharger le ficheir PUCS", e);
        }
    }

    public static void updateStatusPucs(String idsPucsEntry, PucsStatusEnum pucsStatusEnum, BSession session) {
        PucsServiceImpl.updateStatusPucs(Arrays.asList(idsPucsEntry), pucsStatusEnum, session);
    }

    public static void updateStatusPucs(List<String> idsPucsEntry, PucsStatusEnum pucsStatusEnum, BSession session) {
        try {

            List<Integer> ids = Lists.transform(idsPucsEntry, new Function<String, Integer>() {
                @Override
                public Integer apply(String i) {
                    return Integer.valueOf(i);
                }
            });

            User usr = ServicesProviders.partnerWebServiceProvide(session).readActivUserOrAdminByLoginName(
                    session.getUserId());
            ServicesProviders.pucsServiceProvide(session).updateStatusPucs(ids, usr.getUserId(), pucsStatusEnum);
        } catch (Exception e) {
            throw new RuntimeException("Impossible d'obtenir le contenu du fichier", e);
        }
    }

    public static PucsEntrySummary getPucsEntry(String idPucsEntry, BSession session) throws OrionPucsException {
        try {
            return ServicesProviders.pucsServiceProvide(session).getPucsEntrySummary(Integer.parseInt(idPucsEntry));
        } catch (Exception e) {
            throw new OrionPucsException("Impossible d'obtenir le contenu du fichier", e);
        }
    }

    public static List<PucsEntrySummary> listPucsFile(int size, String likeAffilie, String forAnnee,
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
            throw new OrionPucsException("Impossible de mettre à jour le statut", e);
        }

    }

    /**
     * Permet de récupérer le fichier pucs dans le back web
     * 
     * @param id
     * @param provenance
     * @param etatSwissDecPucsFile
     * @param workDirectory
     * @param bSession
     * @return
     */
    public static String retrieveFile(String id, DeclarationSalaireProvenance provenance, String workDirectory,
            String loginName, String userEmail, String langueIso) {
        byte[] fileContent = null;
        String filePath = workDirectory + id + ".xml";
        try {
            if (provenance.isPucs()) {
                fileContent = PucsServiceImpl.downloadFile(id, provenance.getValue(), loginName, userEmail, langueIso);
            } else if (provenance.isDan()) {
                fileContent = DanServiceImpl.downloadFile(id, provenance.getValue(), loginName, userEmail, langueIso);
            } else if (provenance.isSwissDec()) {
                String file = JadeFsFacade.readFile(EBProperties.PUCS_SWISS_DEC_DIRECTORY.getValue() + id + ".xml");
                JadeFsFacade.copyFile(file, workDirectory + id + ".xml");
            }

            if (fileContent != null) {
                OutputStreamWriter os = null;
                CharsetEncoder utf8 = Charset.forName("UTF-8").newEncoder();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(filePath);
                    os = new OutputStreamWriter(fileOutputStream, utf8);
                    os.write(new String(fileContent, utf8.charset()));
                    os.flush();
                } finally {
                    if (os != null) {
                        os.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
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

    @Override
    public String pucFileLisible(String id, String provenance, String etatSwissDecPucsFile) {
        return pucFileLisiblePdf(id, DeclarationSalaireProvenance.valueOf(provenance),
                BSessionUtil.getSessionFromThreadContext());
    }

    @Override
    public String pucFileLisibleXml(String id, String provenance, String etatSwissDecPucsFile) {
        File f = EBPucsFileService.retriveFile(id, BSessionUtil.getSessionFromThreadContext());
        return JadeFilenameUtil.normalizePathComponents(f.getAbsolutePath());
    }

    @Override
    public String pucFileLisibleXls(String id, String provenance, String etatSwissDecPucsFile) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        Locale locale = buildLocale(session);

        @SuppressWarnings("squid:S2095" /*Dans ce cas le close est fait par la fonction build, il n'y a pas besoin de faire le close*/)
        SimpleOutputListBuilder builder = SimpleOutputListBuilder.newInstance().local(locale).asXls();
        ElementsDomParser parser = new ElementsDomParser(EBPucsFileService.retriveFileAsInputStream(id, session));
        PucsFile pucsfile = EBPucsFileService.read(id, session);
        File file = out(DeclarationSalaireProvenance.valueOf(provenance), builder, parser, pucsfile, session);
        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    public static String pucFileLisiblePdfByXml(File fileXml, DeclarationSalaireProvenance provenance, BSession session) {
        Locale locale = buildLocale(session);
        @SuppressWarnings("squid:S2095" /*Dans ce cas le close est fait par la fonction build, il n'y a pas besoin de faire le close*/)

        SimpleOutputListBuilder builder = SimpleOutputListBuilder.newInstance().asPdf().local(locale);
        ElementsDomParser parser = new ElementsDomParser(fileXml);
        File file = out(provenance, builder, parser, session);
        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    public static String pucFileLisiblePdf(String id, DeclarationSalaireProvenance provenance, BSession session) {
        Locale locale = buildLocale(session);
        @SuppressWarnings("squid:S2095" /*Dans ce cas le close est fait par la fonction build, il n'y a pas besoin de faire le close*/)
        SimpleOutputListBuilder builder = SimpleOutputListBuilder.newInstance().asPdf().local(locale);
        ElementsDomParser parser = new ElementsDomParser(EBPucsFileService.retriveFileAsInputStream(id, session));
        PucsFile pucsfile = EBPucsFileService.read(id, session);
        File file = out(provenance, builder, parser, pucsfile, session);
        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    @Override
    public String pucsFileLisibleForEbusiness(String id, DeclarationSalaireProvenance provenance, String format,
            String loginName, String userEmail, String langue) throws OrionPucsException {
        Locale locale = new Locale(langue);
        @SuppressWarnings("squid:S2095" /*Dans ce cas le close est fait par la fonction build, il n'y a pas besoin de faire le close*/)

        SimpleOutputListBuilder builder = SimpleOutputListBuilder.newInstance();
        BSession session = UtilsService.initSession();

        if ("pdf".equals(format)) {
            builder.asPdf().local(locale);
        } else if ("xls".equals(format)) {
            builder.asXls().local(locale);
        } else {
            throw new IllegalArgumentException("the format " + format + " is not allowed");
        }

        PucsFile pucsFileWebAvs = EBPucsFileService.readByFilename(id, session);

        File file = null;

        if (pucsFileWebAvs != null) {
            String idPucsWebAvs = pucsFileWebAvs.getIdDb();
            file = outForEbusiness(idPucsWebAvs, provenance, builder, langue);
        } else {
            throw new OrionPucsException("Aucun fichier PUCS trouvé pour l'id " + id);
        }

        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    @Override
    public String pucsFileLisibleForEbusinessFromByteCode(byte[] pucsFile, DeclarationSalaireProvenance provenance,
            String format, String langue) throws OrionPucsException {
        try {
            Locale locale = new Locale(langue);

            @SuppressWarnings("squid:S2095" /*Dans ce cas le close est par la fonction build, il n'y a pas besoin de faire le close*/)
            SimpleOutputListBuilder simpleOutputListbuilder = SimpleOutputListBuilder.newInstance();
            BSession session = UtilsService.initSession();
            if ("pdf".equals(format)) {
                simpleOutputListbuilder.asPdf().local(locale);
            } else if ("xls".equals(format)) {
                simpleOutputListbuilder.asXls().local(locale);
            } else {
                throw new IllegalArgumentException("the format " + format + " is not allowed");
            }
            // parsing du fichier PUCS
            InputStream inputStreamPucsFile = new ByteArrayInputStream(pucsFile);
            ElementsDomParser parser = new ElementsDomParser(inputStreamPucsFile);

            // création du fichier
            File file = out(provenance, simpleOutputListbuilder, parser, session);
            return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
        } catch (Exception e) {
            throw new OrionPucsException("Unable to generate file", e);
        }
    }

    public static String pucFileLisiblePdf(DeclarationSalaireProvenance provenance, ElementsDomParser parser,
            PucsFile pucsFile, BSession session) {
        Locale locale = buildLocale(session);
        @SuppressWarnings("squid:S2095" /*Dans ce cas le close est fait par la fonction build, il n'y a pas besoin de faire le close*/)

        SimpleOutputListBuilder builder = SimpleOutputListBuilder.newInstance().asPdf().local(locale);
        File file = out(provenance, builder, parser, pucsFile, session);

        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    public static Integer findIdInstitution(int type, int idPucs, BSession session) throws EBDanException_Exception {
        PUCSService service = ServicesProviders.pucsServiceProvide(session);
        LienInstitution lienInstitution = service.findPucsInstitutionForUtilisation(idPucs, type);
        if (lienInstitution != null) {
            return lienInstitution.getIdInstitution();
        }
        return null;
    }

    private static File outForEbusiness(String id, DeclarationSalaireProvenance provenance,
            SimpleOutputListBuilder generator, String langueIso) {
        BSession session = UtilsService.initSession();

        ElementsDomParser parser = buildElementDomParser(id, session);
        return out(provenance, generator, parser, session);
    }

    public static ElementsDomParser buildElementDomParser(String id, BSession session) {
        return new ElementsDomParser(EBPucsFileService.retriveFileAsInputStream(id, session));
    }

    private static String getWorkDir() {
        return Jade.getInstance().getHomeDir() + "work/";
    }

    private static File out(DeclarationSalaireProvenance provenance, SimpleOutputListBuilder builder,
            ElementsDomParser parser, BSession session) {
        return out(provenance, builder, parser, null, session);
    }

    private static File out(DeclarationSalaireProvenance provenance, SimpleOutputListBuilder builder,
            ElementsDomParser parser, PucsFile file, BSession session) {

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

        if (file != null && file.getDateValidation() != null && file.getCertifieExact()) {

            paramsData.add(session.getLabel("CERTIFIE_EXACT_LE"), new Date(file.getDateValidation()).getSwissValue()
                    + " " + session.getLabel("CERTIFIE_EXACT_PAR") + " " + file.getNomValidation());
            paramsData.newLigne();
        }

        String name = Jade.getInstance().getPersistenceDir() + ds.getNumeroAffilie() + "list_"
                + JadeUUIDGenerator.createStringUUID();

        builder.addList(list)
                .classElementList(SalaryForList.class)
                .addHeaderDetails(paramsData)
                .addTitle(
                        session.getLabel("TITRE_LIST_SALAIRE") + "(" + PucsServiceImpl.NUMERO_INFORM_PUCS_LISIBLE + ")",
                        Align.RIGHT).translater(translater);

        return builder.outputName(name).build();
    }

    public static String retrieveFile(String id, DeclarationSalaireProvenance provenance, BSession bSession) {
        return retrieveFile(id, provenance, getWorkDir(), bSession.getUserId(), bSession.getUserEMail(),
                bSession.getIdLangueISO());
    }

    public static String retrieveFile(String id, DeclarationSalaireProvenance provenance) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        return retrieveFile(id, provenance, getWorkDir(), session.getUserId(), session.getUserEMail(),
                session.getIdLangueISO());
    }

    private static Locale buildLocale(BSession session) {
        return new Locale(session.getIdLangueISO());
    }

    public static boolean userHasRight(AFAffiliation afAffiliation, BSession session) {
        boolean hasRight;
        if (afAffiliation == null) {
            hasRight = false;
        } else {
            hasRight = AFAffiliationServices.hasRightAccesSecurity(afAffiliation, session);

        }
        return hasRight;
    }
}
