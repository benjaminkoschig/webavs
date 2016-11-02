package globaz.orion.process.importpucs;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.process.byitem.ProcessItem;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;
import ch.globaz.orion.db.EBPucsFileEntity;
import com.google.common.base.Joiner;

public class PucsItem extends ProcessItem {

    protected final PucsFile pucsFile;
    protected final List<AFAffiliation> affiliations;
    protected final BSession session;
    protected final String idJob;

    public PucsItem(PucsFile pucsFile, List<AFAffiliation> affiliations, BSession session, String idJob) {
        this.pucsFile = pucsFile;
        this.affiliations = affiliations;
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
        // On utilise la dernier numéro du code pour avoir le niveau
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
        if (pucsFile.getCurrentStatus() != null) {
            entity.setStatut(Integer.valueOf(pucsFile.getCurrentStatus().getValue()));
        }
        entity.setDateReception(new Date(pucsFile.getDateDeReception()).getDate());
        entity.setDuplicate(pucsFile.isDuplicate());
        entity.setHandlingUser(pucsFile.getHandlingUser());
        entity.setNbSalaire(Integer.valueOf(pucsFile.getNbSalaires()));
        entity.setNomAffilie(pucsFile.getNomAffilie());
        entity.setNumeroAffilie(pucsFile.getNumeroAffilie());
        entity.setProvenance(pucsFile.getProvenance().getValue());
        entity.setSalaireInferieurLimite(pucsFile.hasSalaireInferieurLimite());
        entity.setSizeFileInKo(pucsFile.getSizeFileInKo());
        entity.setTotalControle(new Montant(pucsFile.getTotalControle()).getBigDecimalValue());
        entity.setIdFileName(pucsFile.getFilename());
        entity.setSearchString(createSearchString(pucsFile));
        PucsServiceImpl.userHasRight(affiliation, session);
        entity.add();
    }

    static String createSearchString(PucsFile pucsFile) {
        String concatenedString = fromNullable(pucsFile.getNumeroAffilie())
                + fromNullable(pucsFile.getDateDeReception()) + fromNullable(pucsFile.getAnneeDeclaration())
                + fromNullable(pucsFile.getFilename()) + fromNullable(pucsFile.getNomAffilie())
                + fromNullable(pucsFile.getTotalControle());
        return JadeStringUtil.convertSpecialChars(concatenedString).toUpperCase();
    }

    static String fromNullable(String str) {
        return str == null ? "" : str;
    }

    @Override
    public void treat() throws Exception {
        AFAffiliation affiliation = null;
        if (pucsFile == null) {
            this.addErrors("PROCESS_IMPORT_PUCSINDB_PUCFILE_NULL");
        } else {
            if (affiliations == null || affiliations.isEmpty()) {
                this.addErrors("PROCESS_IMPORT_PUCSINDB_AFFILIATION_NOT_FOUND", pucsFile.getNumeroAffilie(),
                        pucsFile.getAnneeDeclaration());
            } else if (affiliations.size() > 1) {
                HashSet<String> ids = new HashSet<String>();
                for (AFAffiliation aff : affiliations) {
                    ids.add(aff.getId());
                }
                this.addErrors("PROCESS_IMPORT_PUCSINDB_AFFILIATION_TO_MANY", pucsFile.getNumeroAffilie(),
                        Joiner.on(";").join(ids));
            } else {
                affiliation = affiliations.get(0);
            }
        }
        if (!hasError()) {
            save(pucsFile, affiliation, idJob, session);
        }
    }
}
