package globaz.hera.utils;

import globaz.globall.db.BSession;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriodeManager;
import globaz.hera.exception.SFBusinessException;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utilitaire permettant de r�cup�rer les p�riodes d'un membre de famille (avec filtrage par type de p�riode, fen�tre de
 * dates, etc...)
 * 
 * @author PBA
 */
public class SFPeriodeUtils {

    /**
     * <p>
     * Recherche les p�riodes (du type voulue) de ce membre de famille.<br/>
     * Les p�riodes seront tri�es afin que la premi�re p�riode de la liste (<code>get(0)</code>) soit celle n'ayant pas
     * de date de fin, ou la date de fin la plus r�cente (la plus grande)
     * </p>
     * 
     * @param session
     *            afin de pouvoir lancer des requ�tes sur la base de donn�e
     * @param idMembreFamille
     *            l'ID du membre famille dont on veut avoir les p�riodes
     * @param csTypePeriode
     *            un ou plusieurs codes syst�me d�crivant les types de p�riodes voulus
     * @return une liste tri�e des p�riodes du membre de famille
     * @throws SFBusinessException
     *             si une erreur survient lors de la recherche des p�riodes
     */
    public static List<ISFPeriode> getPeriodesDuMembreFamille(BSession session, String idMembreFamille,
            String... csTypePeriode) throws SFBusinessException {

        // r�cup�ration des p�riodes du membre de famille
        SFPeriodeManager manager = new SFPeriodeManager();
        manager.setSession(session);
        manager.setForIdMembreFamille(idMembreFamille);

        // filtre sur les types de p�riodes s'il y a lieu
        if ((csTypePeriode != null) && (csTypePeriode.length > 0)) {
            manager.setForCsTypePeriodeIn(Arrays.asList(csTypePeriode));
        }

        try {
            manager.find();
        } catch (Exception ex) {
            StringBuilder error = new StringBuilder();

            error.append(session.getLabel("_ERROR_CHARGEMENT_PERIODES_DU_MEMBRE"));
            error.append(" : \n");
            error.append(ex.getMessage());

            throw new SFBusinessException(error.toString());
        }

        // cr�ation de la collection qui sera retourn�e
        List<ISFPeriode> periodes = new ArrayList<ISFPeriode>();
        for (int i = 0; i < manager.size(); i++) {
            ISFPeriode unePeriode = (ISFPeriode) manager.get(i);
            periodes.add(unePeriode);
        }

        // tri de la collection afin d'avoir la p�riode sans date de fin, ou avec la date de fin la plus r�cente en
        // premier (en utilisant par exemple get(0))
        Collections.sort(periodes, new Comparator<ISFPeriode>() {

            @Override
            public int compare(ISFPeriode periode1, ISFPeriode periode2) {

                if (JadeStringUtil.isBlankOrZero(periode1.getDateFin())) {
                    return 1;
                }
                if (JadeStringUtil.isBlankOrZero(periode2.getDateFin())) {
                    return -1;
                }

                JadePeriodWrapper periodeWrapper1 = new JadePeriodWrapper(periode1.getDateDebut(), periode1
                        .getDateFin());
                JadePeriodWrapper periodeWrapper2 = new JadePeriodWrapper(periode2.getDateDebut(), periode2
                        .getDateFin());

                return periodeWrapper2.compareTo(periodeWrapper1);
            }
        });

        return periodes;
    }

    /**
     * <p>
     * Recherche les p�riodes (du type voulue) de ce tiers dans la situation familiale.<br/>
     * Les p�riodes seront tri�es afin que la premi�re p�riode de la liste (<code>get(0)</code>) soit celle n'ayant pas
     * de date de fin, ou la date de fin la plus r�cente (la plus grande)
     * </p>
     * <p>
     * Cette m�thode se base sur {@link #getPeriodesDuMembreFamille(BSession, String, String...)} en cherchant au
     * pr�alable ce tiers dans les membres de famille (dans le domaine voulue, puis dans le domaine standard si non
     * trouv�)
     * </p>
     * 
     * @param session
     *            afin de pouvoir lancer des requ�tes sur la base de donn�e
     * @param idTiers
     *            l'ID du tiers dont on veut avoir les p�riodes
     * @param csDomaineApplication
     *            code syst�me (trouv� dans {@link ISFSituationFamiliale}) d�crivant le domaine d'application dans
     *            lequel on veut recherche ce tier dans la situation familiale (une recherche dans le domaine standard
     *            sera faite si le membre famille est introuvable dans le de domaine donn�). Si seul le domaine standard
     *            est voulu, mettre <code>null</code> ou le code syst�me du domaine standard
     * @param csTypePeriode
     *            un ou plusieurs codes syst�me d�crivant les types de p�riodes voulus
     * @return une liste tri�e contenant les p�riodes du tiers
     * @throws SFBusinessException
     *             si une erreur survient lors de la recherche du membre de famille correspondant au tier, ou si une
     *             erreur survient lors de la recherche des p�riodes du membre de famille
     */
    public static List<ISFPeriode> getPeriodesDuTiers(BSession session, String idTiers, String csDomaineApplication,
            String... csTypePeriode) throws SFBusinessException {

        SFMembreFamille membreFamille = new SFMembreFamille();
        membreFamille.setSession(session);
        membreFamille.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        membreFamille.setIdTiers(idTiers);
        if (!JadeStringUtil.isBlankOrZero(csDomaineApplication)) {
            membreFamille.setCsDomaineApplication(csDomaineApplication);
        }

        try {
            membreFamille.retrieve();

            if (membreFamille.isNew() && !JadeStringUtil.isBlankOrZero(csDomaineApplication)
                    && !ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(csDomaineApplication)) {
                membreFamille.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                membreFamille.retrieve();
            }
        } catch (Exception ex) {
            StringBuilder error = new StringBuilder();

            error.append(session.getLabel("ERROR_CHARGEMENT_MEMBRE_FAMILLE"));
            error.append(" : \n");
            error.append(ex.getMessage());

            throw new SFBusinessException(error.toString());
        }

        if (membreFamille.isNew()) {
            StringBuilder error = new StringBuilder();

            error.append(session.getLabel("ERROR_MEMBRE_FAMILLE_INTROUVABLE")).append(idTiers);

            throw new SFBusinessException(error.toString());
        }

        return SFPeriodeUtils.getPeriodesDuMembreFamille(session, membreFamille.getIdMembreFamille(), csTypePeriode);
    }
}
