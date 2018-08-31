package globaz.vulpecula.helpers.decomptesalaire;

import javax.annotation.processing.FilerException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.process.myprodis.ComparaisonMetierMyProdis;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.prestation.utils.DateException;
import globaz.vulpecula.vb.decomptesalaire.PTControlerMyProdisViewBean;

public class PTControlerMyProdisHelper extends FWHelper {

    private FilerException fileFormatException = new FilerException("The format file is incorrect");
    private BSession session;

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            this.session = (BSession) session;
            Periode periode;

            PTControlerMyProdisViewBean vb = (PTControlerMyProdisViewBean) viewBean;

            // Contrôle de la validité du fichier
            if (!fileNameIsValid(vb.getFilename())) {
                throw fileFormatException(this.session.getLabel("ERROR_FORMAT_FICHIER"));
            }

            periode = getPeriod(vb.getFilename());

            ComparaisonMetierMyProdis process = new ComparaisonMetierMyProdis();
            process.setEMailAddress(vb.getEmail());
            process.setFileName(vb.getDestination());
            process.setDateDebut(periode.getDateDebut().getValue());
            process.setDateFin(periode.getDateFin().getValue());
            process.setWantControleCP(vb.getWantControleCP());
            process.setWantControleSalaires(vb.getWantControleSalaires());

            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            JadeLogger.error(e, e.toString());
        }
    }

    /**
     * Méthode permettant de renvoyer une période selon le nom de fichier (Validé préalablement)
     *
     * @param filename Nom de fichier de base
     * @return
     * @throws DateException
     */
    public Periode getPeriod(String filename) throws DateException {
        String[] parts = filename.split("CAPAV_");
        Periode newPeriode = new Periode(new Date(parts[1].substring(0, 8)), new Date(parts[1].substring(9, 17)));

        // Contrôle de la validité de la période
        if (!isPeriodValid(newPeriode)) {
            throw new DateException(session.getLabel("ERROR_PERIOD_CONTROL_MYPRODIS"));
        }
        return newPeriode;
    }

    /**
     * Permet de contrôler que le fichier sélectionné par l'utilisateur soit correctement nommé
     * et se trouve dans le bon format (csv)
     *
     * @param filename Le Nom du fichier sélectionné par l'utilisateur
     * @throws Exception
     */
    public boolean fileNameIsValid(String filename) {
        return filename.matches(
                ".*CAPAV_(19|20)[0-9]{2}(0[1-9]|1[0-2])([0-2][1-9]|3[0-1])_(19|20)[0-9]{2}(0[1-9]|1[0-2])([0-2][1-9]|3[0-1])(.csv)$");

    }

    /**
     * Permet de contrôler que l'inteval de la période ne dépasse pas une année
     *
     * @param pPeriode La période à contrôler
     * @return Si l'interval est correct
     */
    public boolean isPeriodValid(Periode pPeriode) {
        return pPeriode.getDateFin().compareTo(pPeriode.getDateDebut().addYear(1)) <= 0;
    }

    /**
     * Permet de paramétrer le message de l'exception qui sera retournée.
     *
     * @param label Le méssage
     * @return Exception
     */
    private FilerException fileFormatException(String label) {
        return new FilerException(label);
    }
}
