package globaz.orion.process.importpucs;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.process.byitem.ProcessItem;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.orion.db.EBPucsFileEntity;
import ch.globaz.xmlns.eb.dan.DanStatutEnum;
import ch.globaz.xmlns.eb.pucs.PucsStatusEnum;
import com.google.common.base.Joiner;

public class PucsItem extends ProcessItem {

    protected final PucsFile pucsFile;
    protected final AFAffiliation affiliation;
    protected final BSession session;
    protected final String idJob;

    public PucsItem(PucsFile pucsFile, AFAffiliation affiliation, BSession session, String idJob) {
        this.pucsFile = pucsFile;
        this.affiliation = affiliation;
        this.session = session;
        this.idJob = idJob;
    }

    public PucsFile getPucsFile() {
        return pucsFile;
    }

    @Override
    public String getDescription() {
        if (pucsFile != null) {
            return "Id:" + pucsFile.getFilename() + ", provenance:" + pucsFile.getProvenance();
        }
        return null;
    }

    private static Integer resolveCodeSecuriteMax(AFAffiliation affiliation, File file, BSession session) {
        String codeSecurity = affiliation.getAccesSecurite();
        // On utilise la dernier num?ro du code pour avoir le niveau
        Integer codeSecurites = Integer.parseInt(codeSecurity.substring(codeSecurity.length() - 1));

        ElementsDomParser parser = new ElementsDomParser(file);

        List<String> listNss = parser.findValues("Person SV-AS-Number");
        List<List<String>> splitedNSSList = QueryExecutor.splitBy1000(listNss);
        for (List<String> splitedNss : splitedNSSList) {
            String nssIn = '\'' + Joiner.on("','").skipNulls().join(splitedNss).replace(".", "") + '\'';
            BigDecimal code = QueryExecutor.executeAggregate("select max(KATSEC) from schema.CIINDIP where KANAVS in ("
                    + nssIn + ")", session);
            if (!BigDecimal.ZERO.equals(code)) {
                Integer ciSecurity = Integer.parseInt(code.toString().substring(codeSecurity.length() - 1));
                if (codeSecurites < ciSecurity) {
                    codeSecurites = ciSecurity;
                }
            }
        }

        return codeSecurites;
    }

    protected static void save(PucsFile pucsFile, AFAffiliation affiliation, String idJob, BSession session)
            throws Exception {
        EBPucsFileEntity entity = new EBPucsFileEntity();
        if (affiliation != null) {
            Integer codeSecurite = resolveCodeSecuriteMax(affiliation, pucsFile.getFile(), session);
            entity.setNiveauSecurite(codeSecurite);
            entity.setIdAffiliation(affiliation.getAffiliationId());
        }
        entity.setSession(session);
        entity.setIdJob(idJob);

        entity.setFile(pucsFile.getFile());
        entity.setAfSeul(pucsFile.isAfSeul());
        entity.setAnneeDeclaration(Integer.valueOf(pucsFile.getAnneeDeclaration()));
        if (JadeStringUtil.isEmpty(pucsFile.getAnneeVersement())) {
            entity.setAnneeVersement(entity.getAnneeDeclaration());
        } else {
            entity.setAnneeVersement(Integer.valueOf(pucsFile.getAnneeVersement()));
        }
        if (pucsFile.getCurrentStatus() != null) {
            entity.setStatut(Integer.valueOf(pucsFile.getCurrentStatus().getValue()));
        }
        entity.setDateReception(new Date(pucsFile.getDateDeReception()).getDate());
        entity.setDuplicate(pucsFile.isDuplicate());
        entity.setNbSalaire(Integer.valueOf(pucsFile.getNbSalaires()));
        entity.setNomAffilie(pucsFile.getNomAffilie());
        entity.setNumeroAffilie(pucsFile.getNumeroAffilie());
        entity.setForTest(pucsFile.isForTest());
        entity.setProvenance(pucsFile.getProvenance().getValue());
        entity.setSalaireInferieurLimite(pucsFile.hasSalaireInferieurLimite());
        entity.setSizeFileInKo(pucsFile.getSizeFileInKo());
        entity.setTotalControle(new Montant(pucsFile.getTotalControle()).getBigDecimalValue());
        entity.setIdFileName(pucsFile.getFilename());
        entity.setSearchString(createSearchString(pucsFile));
        entity.setDateValidation(new Date(pucsFile.getDateDeReception()).getDate());
        entity.setNomValidation(pucsFile.getNomValidation());
        entity.setCertifieExact(pucsFile.getCertifieExact());
        entity.setTypeDeclaration(pucsFile.getTypeDeclaration().getValue());
        entity.add();
    }

    static String createSearchString(PucsFile pucsFile) {
        String concatenedString = fromNullable(pucsFile.getNumeroAffilie())
                + fromNullable(pucsFile.getDateDeReception()) + fromNullable(pucsFile.getAnneeDeclaration())
                + fromNullable(pucsFile.getFilename()) + fromNullable(pucsFile.getNomAffilie())
                + fromNullable(pucsFile.getTotalControle()) + fromNullable(pucsFile.getNbSalaires());
        return JadeStringUtil.convertSpecialChars(concatenedString).toUpperCase();
    }

    static String fromNullable(String str) {
        return str == null ? "" : str;
    }

    @Override
    public void treat() throws Exception {
        if (pucsFile == null) {
            this.addErrors("PROCESS_IMPORT_PUCSINDB_PUCFILE_NULL");
        } else {
            if (affiliation == null || affiliation.isNew()) {
                this.addErrors("PROCESS_IMPORT_PUCSINDB_AFFILIATION_NOT_FOUND", pucsFile.getNumeroAffilie(),
                        pucsFile.getAnneeDeclaration());
            }
        }
        try {
            if (!hasError()) {
                try {
                    String filePath = PucsServiceImpl.retrieveFile(pucsFile.getFilename(), pucsFile.getProvenance());
                    pucsFile.setFile(new File(filePath));
                    save(pucsFile, affiliation, idJob, session);
                } catch (Exception e) {
                    if (pucsFile.getProvenance().isPucs()) {
                        PucsServiceImpl.updateStatusPucs(pucsFile.getFilename(), PucsStatusEnum.REJECTED, session);
                    } else if (pucsFile.getProvenance().isDan()) {
                        DanServiceImpl.updateStatusDan(pucsFile.getFilename(), DanStatutEnum.EN_ERREUR, session);
                    }
                    catchException(e);
                }
            }
        } finally {
            EBPucsFileEntity.deleteFileOnWorkDirectory(pucsFile.getFilename());
        }
    }
}
