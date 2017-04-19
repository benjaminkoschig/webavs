package globaz.lyra.vb.echeances;

import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheance;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class LYEcheanceLineViewBean {

    private LYSimpleEcheance echeance;

    public LYEcheanceLineViewBean(LYSimpleEcheance echeance) {
        super();

        this.echeance = echeance;
    }

    public final String getCreationSpy() {
        return echeance.getCreationSpy();
    }

    public String getCsDomaineApplicatif() {
        return echeance.getCsDomaineApplicatif();
    }

    public String getDescriptionEcheance() {
        return echeance.getDescriptionEcheance();
    }

    public String getDescriptionEcheance_fr() {
        return echeance.getDescriptionEcheance_fr();
    }

    public String getDescriptionEcheance_de() {
        return echeance.getDescriptionEcheance_de();
    }

    public String getDescriptionEcheance_it() {
        return echeance.getDescriptionEcheance_it();
    }

    public LYSimpleEcheance getEcheance() {
        return echeance;
    }

    public String getId() {
        return echeance.getId();
    }

    public String getIdEcheance() {
        return echeance.getIdEcheance();
    }

    public String getJspProcessEcheance() {
        return echeance.getJspProcessEcheance();
    }

    public String getLibelleDomaineApplicatif() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(getCsDomaineApplicatif());
    }

    public String getMoisCourant() {
        return new SimpleDateFormat("MM.yyyy").format(Calendar.getInstance().getTime());
    }

    public String getMoisCourantPCF() {
        String moisActuelle = new SimpleDateFormat("MM.yyyy").format(Calendar.getInstance().getTime());
        String dateDernierPaiement;
        try {
            dateDernierPaiement = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            if (JadeStringUtil.isBlank(dateDernierPaiement) || "02.1970".equals(dateDernierPaiement)) {
                return moisActuelle;
            }
        } catch (Exception ex) {
            return moisActuelle;
        }
        return dateDernierPaiement;
    }

    public String getMoisCourantRente() {
        // return new SimpleDateFormat("MM.yyyy").format(Calendar.getInstance().getTime());
        // On doit afficher la date du prochain paiement
        return REPmtMensuel.getDateDernierPmt(BSessionUtil.getSessionFromThreadContext());
    }

    public String getNumeroOrdre() {
        return echeance.getNumeroOrdre();
    }

    public String getProcessEcheance() {
        return echeance.getProcessEcheance();
    }

    public String getSpy() {
        return echeance.getSpy();
    }
}
