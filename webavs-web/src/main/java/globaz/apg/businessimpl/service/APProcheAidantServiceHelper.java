package globaz.apg.businessimpl.service;

import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.common.util.Dates;
import ch.globaz.queryexec.bridge.jade.SCM;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.globall.db.BSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class APProcheAidantServiceHelper {

    private final BSession session;

    @SuppressWarnings({"Convert2MethodRef", "java:S1612"})
    public void changementDateDebutDroitsPourLesDroitsQuiOnUneDateDeDebutPlusRecente(APDroitProcheAidant droitProcheAidant) {
        droitProcheAidant.resolveDateDebutDelaiCadre().ifPresent(date -> {
            List<IdDroit> idDroits = chercherDroitQuiSonLieeAvecLeMemeEnfant(droitProcheAidant.getIdDroit());
            idDroits.stream()
                    .map(this::findDroitProcheAidant)
                    .filter(droit -> Dates.toDate(droit.getDateDebutDroit()).isAfter(date))
                    .forEach(droit -> {
                        droit.setDateDebutDroit(Dates.formatSwiss(date));
                        Exceptions.checkedToUnChecked(() -> droit.update());
                    });
        });
    }

    @SuppressWarnings({"Convert2MethodRef", "java:S1612"})
    private APDroitProcheAidant findDroitProcheAidant(IdDroit idDroit) {
        APDroitProcheAidant droitProcheAidant = new APDroitProcheAidant();
        droitProcheAidant.setSession(session);
        droitProcheAidant.setId(idDroit.getId().toString());
        droitProcheAidant.setIdDroit(idDroit.getId().toString());
        Exceptions.checkedToUnChecked(() -> droitProcheAidant.retrieve());
        return droitProcheAidant;
    }

    private List<IdDroit> chercherDroitQuiSonLieeAvecLeMemeEnfant(String idDroitProcheAidant) {
        SQLWriter sqlWriter = SQLWriter.writeWithSchema()
                                       .append("select distinct schema.APDROIP.VAIDRO as id")
                                       .append("from schema.APDROIP")
                                       .append("inner join schema.APDROITPROCHEAIDANT ON schema.APDROITPROCHEAIDANT.ID_DROIT = schema.APDROIP.VAIDRO")
                                       .append("inner join schema.APSIFMP ON schema.APSIFMP.VQIDRM = schema.APDROIP.VAIDRO")
                                       .append("where schema.APSIFMP.VQLAVS = (select schema.APSIFMP.VQLAVS as nss_enfant")
                                       .append("from schema.APDROIP")
                                       .append("inner join schema.APDROITPROCHEAIDANT")
                                       .append("ON schema.APDROITPROCHEAIDANT.ID_DROIT = schema.APDROIP.VAIDRO")
                                       .append("inner join schema.APSIFMP ON schema.APSIFMP.VQIDRM = schema.APDROIP.VAIDRO")
                                       .append("where schema.APDROIP.VAIDRO = ?)", idDroitProcheAidant);

        return SCM.newInstance(IdDroit.class).query(sqlWriter.toSql()).session(session).execute();
    }

    @Data
    public static class IdDroit {
        private Integer id;
    }
}
