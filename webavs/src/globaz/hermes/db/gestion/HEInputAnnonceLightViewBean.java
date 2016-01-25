package globaz.hermes.db.gestion;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.handler.HELotHandler;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEInputAnnonceLightViewBean extends HEAnnoncesViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception, HEInputAnnonceException {
        if (utilisateur.equals("")) {
            setUtilisateur(getSession().getUserName());
        }
        // La date du jour
        if (getDateAnnonce().trim().equals("")) {
            setDateAnnonce(JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_DDMMYYYY));
        }
        // Association avec un lot
        HELotListViewBean lots = new HELotListViewBean();
        // je set l'id du lot
        setIdLot(HELotHandler.getLotId(getIdLot(), getTypeLot(), getPrioriteLot(), getSession(), getTypeLot(),
                getDateAnnonce(), JadeStringUtil.isBlank(getIdProgramme()) ? getUtilisateur() : getIdProgramme(),
                transaction));
        // je set l'id de l'annonce
        setIdAnnonce(_incCounter(transaction, "0"));
        if (JAUtil.isStringEmpty(getRefUnique())) {
            setRefUnique(getIdAnnonce());
        }
        if (getStatut().trim().length() == 0) {
            setStatut(IHEAnnoncesViewBean.CS_EN_ATTENTE);
        }
        // le message
        setIdMessage("0");
        // le numéro AVS
        traiterChampNumAssure();
        // le numéro de l'annonce
        String[] genNum = { "11", "41", "42", "43", "44", "45", "46" };
        if (Arrays.asList(genNum).contains(getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01")) {
            if (getIdAnnonce().length() > 6) {
                put(IHEAnnoncesViewBean.NUMERO_ANNONCE,
                        getIdAnnonce().substring(getIdAnnonce().length() - 6, getIdAnnonce().length()));
            } else {
                put(IHEAnnoncesViewBean.NUMERO_ANNONCE, getIdAnnonce());
            }
        }

        // numero de caisse
        setNumeroCaisse(StringUtils.unPad(getField(IHEAnnoncesViewBean.NUMERO_CAISSE)) + "."
                + StringUtils.unPad(getField(IHEAnnoncesViewBean.NUMERO_AGENCE)));

        // remplissage du champs enregistrement
        initChampEnregistrementFromAttr();
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {

    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getCSStatutLibelle()
     */
    @Override
    public String getCSStatutLibelle() throws Exception {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getInformation()
     */
    @Override
    public String getInformation() throws Exception {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getInformation(String)
     */
    @Override
    public String getInformation(String userId) throws Exception {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getRevenu()
     */
    @Override
    public String getRevenu() throws Exception {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getRevenu(String)
     */
    @Override
    public String getRevenu(String userId) throws Exception {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getSqlForCopyAnnonce()
     */
    @Override
    public String getSqlForCopyAnnonce() {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getSqlForCopyRetour()
     */
    @Override
    public String getSqlForCopyRetour() {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getSqlForDeleteAnnoncesSerie()
     */
    @Override
    public String getSqlForDeleteAnnoncesSerie() {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#getSqlForDeleteAttentesSerie()
     */
    @Override
    public String getSqlForDeleteAttentesSerie() {
        return null;
    }

    /**
     * @see globaz.hermes.db.gestion.HEAnnoncesViewBean#isRevenuCache(String)
     */
    @Override
    public boolean isRevenuCache(String userId) {
        return false;
    }

}
