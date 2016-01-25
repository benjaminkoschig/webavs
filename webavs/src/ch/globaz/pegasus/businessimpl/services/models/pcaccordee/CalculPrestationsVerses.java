package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.vo.decompte.PCAccordeeDecompteVO;

public class CalculPrestationsVerses {

    private String idDemande = null;
    private BigDecimal montantRetro = new BigDecimal(0); // pour vérification
    private BigDecimal montantVerse = null;
    private String noVersion = null;
    private List<PCAccordeeDecompteVO> pcaVo = new ArrayList<PCAccordeeDecompteVO>();

    public CalculPrestationsVerses(String noVersionDroit, String idDemande) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, PmtMensuelException,
            DecisionException {

        noVersion = noVersionDroit;
        this.idDemande = idDemande;
        montantVerse = new BigDecimal(0);
        calculMontantVerse();

    }

    private void calculMontantVerse() throws PCAccordeeException, PmtMensuelException,
            JadeApplicationServiceNotAvailableException, DecisionException, JadePersistenceException {

        // recupération de la map des montant retrocatifs
        CalculMontantRetroActif montantRetro = new CalculMontantRetroActif(idDemande, noVersion);
        Map<String, String> mapMontantRetro = montantRetro.getMapMontantVerse();
        pcaVo = montantRetro.getListPcaVoReq();
        this.montantRetro = montantRetro.getTotalRetro();

        for (Entry<String, String> entry : mapMontantRetro.entrySet()) {
            montantVerse = montantVerse.add(new BigDecimal(entry.getValue()));
        }

    }

    public BigDecimal getMontantRetro() {
        return montantRetro;
    }

    public BigDecimal getMontantVerse() {
        return montantVerse;
    }

    public List<PCAccordeeDecompteVO> getPcaVo() {
        return pcaVo;
    }

    public void setMontantRetro(BigDecimal montantRetro) {
        this.montantRetro = montantRetro;
    }

    public void setMontantVerse(BigDecimal montantVerse) {
        this.montantVerse = montantVerse;
    }

}
