package globaz.hera.impl.ijai;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFPeriodeManager;
import globaz.hera.wrapper.SFPeriodeWrapper;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Implementation de l'interface ISFSituation Familliale, Il s'agit de l'implémentation pour le domaine ijai. Cette
 * classe ne peut pas être utilisée comme un BEntity, mais uniquement pour appeler les getter
 * 
 * @author scr
 */
public class SFSituationFamiliale extends globaz.hera.impl.standard.SFSituationFamiliale {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Définition de la date initiale de début (00000000) */
    private static final String DATE_DEBUT_INIT = "00000000";
    /** Définition de la date initiale de fin (99999999) */
    private static final String DATE_FIN_INIT = "99999999";

    public SFSituationFamiliale() {
        super();
        domaine = ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE;
    }

    /**
     * Ajoute les périodes du manager à la liste
     * 
     * @param lst
     * @param entity
     */
    private void addPeriodeList(ArrayList lst, SFPeriode entity) {
        JACalendarGregorian cal = new JACalendarGregorian();
        try {
            lst.add((new JADate(cal.addDays(entity.getDateDebut(), -1))).toStrAMJ());
            if (!JadeNumericUtil.isEmptyOrZero(entity.getDateFin())) {
                lst.add((new JADate(cal.addDays(entity.getDateFin(), 1))).toStrAMJ());
            }
        } catch (JAException e) {
            // si exception, on ne fait rien et on ne remonte rien
        }
    }

    /**
     * Concaténation des deux tableau de période
     * 
     * @param iPeriodesEtude
     * @param iPeriodesAF
     */
    private ISFPeriode[] concatenatesPeriode(ISFPeriode[] iPeriodesEtude, ISFPeriode[] iPeriodesAF) {
        int lenIPeriodeAF = iPeriodesAF != null ? iPeriodesAF.length : 0;
        int lenIPeriodeEtude = iPeriodesEtude != null ? iPeriodesEtude.length : 0;
        ISFPeriode[] iPeriodes = new ISFPeriode[lenIPeriodeAF + lenIPeriodeEtude];
        if (iPeriodesAF != null) {
            System.arraycopy(iPeriodesAF, 0, iPeriodes, 0, lenIPeriodeAF);
        }
        if (iPeriodesEtude != null) {
            System.arraycopy(iPeriodesEtude, 0, iPeriodes, lenIPeriodeAF, lenIPeriodeEtude);
        }
        return iPeriodes;
    }

    /**
     * Chargement du tableau avec période passée en paramètre
     * 
     * @param periodeMgr
     * @throws Exception
     */
    private SFPeriodeWrapper createWrapperAF(String idMembreFamille, String type, String dateDebut, String dateFin)
            throws Exception {
        SFPeriodeWrapper wrapper = new SFPeriodeWrapper();
        wrapper.setDateDebut(JACalendar.format(JADate.newDateFromAMJ(dateDebut), JACalendar.FORMAT_DDsMMsYYYY));
        wrapper.setDateFin(JACalendar.format(JADate.newDateFromAMJ(dateFin), JACalendar.FORMAT_DDsMMsYYYY));
        wrapper.setIdDetenteurBTE("");
        wrapper.setNoAvs(searchNoAVS(idMembreFamille));
        wrapper.setNoAvsDetenteurBTE("");
        wrapper.setPays("");
        wrapper.setType(PRACORConst.CA_PERIODE_REFUS_AF);
        return wrapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getPeriode(java.lang.String)
     */
    @Override
    public ISFPeriode[] getPeriodes(String idMembreFamille) throws Exception {
        return getPeriodes(idMembreFamille, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getPeriode(java.lang.String, java.lang.String)
     */
    @Override
    public ISFPeriode[] getPeriodes(String idMembreFamille, String typePeriode) throws Exception {
        if (typePeriode != null && !typePeriode.equalsIgnoreCase(ISFSituationFamiliale.CS_TYPE_PERIODE_REFUS_AF)
                && !typePeriode.equalsIgnoreCase(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE)) {
            throw new IJAIUndefinedTypePeriodeException(getClass().getName()
                    .concat(".getPeriodes() - type de période non implémentée : ").concat(typePeriode));
        }

        ISFPeriode[] iPeriodesEtude = null;
        ISFPeriode[] iPeriodesAF = null;
        if (typePeriode == null || typePeriode.equalsIgnoreCase(ISFSituationFamiliale.CS_TYPE_PERIODE_REFUS_AF)) {
            iPeriodesAF = treatmentPeriodeAF(idMembreFamille, ISFSituationFamiliale.CS_TYPE_PERIODE_REFUS_AF);
        }
        if (typePeriode == null || typePeriode.equalsIgnoreCase(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE)) {
            iPeriodesEtude = treatmenPeriodeEtude(idMembreFamille, ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE);
        }

        return concatenatesPeriode(iPeriodesEtude, iPeriodesAF);
    }

    /**
     * Initialisation de la liste période
     * 
     * @return
     */
    private ArrayList initListPeriode() {
        ArrayList lst = new ArrayList();
        lst.add(SFSituationFamiliale.DATE_DEBUT_INIT);
        lst.add(SFSituationFamiliale.DATE_FIN_INIT);
        return lst;
    }

    /**
     * Chargement des périodes pour le type de période défini
     * 
     * @param idMembreFamille
     * @param typePeriode
     * @return
     * @throws Exception
     */
    private SFPeriodeManager loadManager(String idMembreFamille, String typePeriode) throws Exception {
        SFPeriodeManager periodeMgr = new SFPeriodeManager();
        periodeMgr.setSession(getSession());
        periodeMgr.setForIdMembreFamille(idMembreFamille);
        if (!JadeStringUtil.isIntegerEmpty(typePeriode)) {
            periodeMgr.setForType(typePeriode);
        }
        periodeMgr.setForOrderBy(SFPeriodeManager.ORDER_TYPE_DATE);
        periodeMgr.find();
        return periodeMgr;
    }

    /**
     * Chargement du tableau pour la période AF
     * 
     * @param periodeMgr
     * @return
     * @throws Exception
     */
    private ISFPeriode[] loadTableAF(SFPeriodeManager periodeMgr) throws Exception {
        // Initialisation de la liste avec la période par défaut (00000000 -
        // 99999999)
        ArrayList lst = initListPeriode();
        // Lecture du manager pour traitement
        readManager(periodeMgr, lst);
        // Tri de la liste par ordre ascendant des dates
        Collections.sort(lst);
        int j = 0;
        ArrayList lstPeriode = new ArrayList();
        for (int i = 0; i + 1 < lst.size(); i++) {
            // Création du wrapper et ajout dans le tableau des périodes
            lstPeriode.add(createWrapperAF(periodeMgr.getForIdMembreFamille(), periodeMgr.getForType(),
                    (String) lst.get(i), (String) lst.get(i + 1)));
            i++;
            j++;
        }
        return (ISFPeriode[]) lstPeriode.toArray(new ISFPeriode[lstPeriode.size()]);
    }

    /**
     * Chargement de la table étude
     * 
     * @param periodeMgr
     * @return
     */
    private ISFPeriode[] loadTableEtude(SFPeriodeManager periodeMgr) {
        // On met la liste des periodes ds une tables d'interface
        ISFPeriode[] iPeriodes = new ISFPeriode[periodeMgr.size()];

        for (int i = 0; i < periodeMgr.size(); i++) {
            SFPeriode periode = (SFPeriode) periodeMgr.get(i);
            SFPeriodeWrapper wrapper = new SFPeriodeWrapper();
            wrapper.setDateDebut(periode.getDateDebut());
            wrapper.setDateFin(periode.getDateFin());
            wrapper.setIdDetenteurBTE(periode.getIdDetenteurBTE());
            wrapper.setCsTypeDeDetenteur(periode.getCsTypeDeDetenteur());
            wrapper.setNoAvs(periode.getNoAvs());
            wrapper.setNoAvsDetenteurBTE(periode.getNoAvsDetenteurBTE());
            wrapper.setPays(periode.getPays());
            wrapper.setType(getSession().getCode(periode.getType()));

            iPeriodes[i] = wrapper;
        }
        return iPeriodes;
    }

    /**
     * Traitement du manager
     * 
     * @param periodeMgr
     * @param lst
     */
    private void readManager(SFPeriodeManager periodeMgr, ArrayList lst) {
        for (int i = 0; i < periodeMgr.size(); i++) {
            addPeriodeList(lst, (SFPeriode) periodeMgr.getEntity(i));
        }
    }

    /**
     * Recherche du numéro AVS en fonction de l'idMembreFamille
     * 
     * @param idMembreFamille
     * @return
     * @throws Exception
     */
    private String searchNoAVS(String idMembreFamille) throws Exception {
        SFMembreFamille famille = new SFMembreFamille();
        famille.setSession(getSession());
        famille.setIdMembreFamille(idMembreFamille);
        famille.retrieve();
        return famille.getNss();
    }

    /**
     * Traitement du type de période étude
     * 
     * @param idMembreFamille
     * @param typePeriode
     * @return
     * @throws Exception
     */
    private ISFPeriode[] treatmenPeriodeEtude(String idMembreFamille, String typePeriode) throws Exception {
        ISFPeriode[] periodes = loadTableEtude(loadManager(idMembreFamille, typePeriode));
        return periodes;

    }

    /**
     * Traitement du type de période refus droits AF
     * 
     * @param idMembreFamille
     * @param typePeriode
     * @return
     * @throws Exception
     */
    private ISFPeriode[] treatmentPeriodeAF(String idMembreFamille, String typePeriode) throws Exception {
        ISFPeriode[] periodes = loadTableAF(loadManager(idMembreFamille, typePeriode));
        return periodes;
    }

}
