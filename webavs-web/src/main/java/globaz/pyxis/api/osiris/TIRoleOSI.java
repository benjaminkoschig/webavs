package globaz.pyxis.api.osiris;

import globaz.alfagest.format.ALRecupNoDossierAF;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JADate;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.util.TIToolBox;
import globaz.webavs.common.CommonProperties;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * Date de création : (12.02.2003 18:11:52) Date de modification : 17.08.2006
 * 
 */
public class TIRoleOSI implements IntRole {
    private static final int LENGTH_NUMERO_NSS = 13;

    private TITiersOSI _tiersOSI = null;
    private IAFAffiliation affiliation = null;
    private String idExterne = "";
    private String idRole = "";
    private TITiersViewBean tiers = new TITiersViewBean();

    /**
     * Commentaire relatif au constructeur TIRoleOSI.
     */
    public TIRoleOSI() {
        super();
    }

    /**
     * Déformatter une chaîne contenant un numéro Date de création : (21.02.2003 15:52:59)
     * 
     * @return String
     * @param toUnformat
     *            String
     */
    private String _unformat(String toUnformat) {
        // Si vide ou null, on retourne la chaîne entrante
        if (JadeStringUtil.isBlank(toUnformat)) {
            return toUnformat;
        }
        // Eliminer les blancs
        toUnformat = toUnformat.trim();
        // Conserver les caractères alphanumeriques uniquement
        String s = "";
        for (int i = 0; i < toUnformat.length(); i++) {
            if (Character.isLetterOrDigit(toUnformat.charAt(i))) {
                s = s + toUnformat.charAt(i);
            }
        }
        return s;
    }

    /**
     * Date de création : (21.05.2002 17:07:31)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void checkIdExterneRoleFormat(String idRole, String idExterne) throws Exception {
        if (JadeStringUtil.isBlank(idRole)) {
            throw new Exception("Le rôle doit être renseigné");
        }
        if (JadeStringUtil.isBlank(idExterne)) {
            throw new Exception("Le numéro externe doit être renseigné");
        }

        if (!idRole.equals(IntRole.ROLE_AFFILIE) && !idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                && !idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)
                && !idRole.equals(IntRole.ROLE_ADMINISTRATEUR)
                && ((NSUtil.unFormatAVS(idExterne).length() > 11) && idExterne.startsWith("756"))) {
            String nnss = NSUtil.formatAVSNew(NSUtil.unFormatAVS(idExterne), true);
            if (!nnss.equals(idExterne)) {
                throw new Exception("NNSS mal formaté.");
            }
        } else if (idRole.equals(IntRole.ROLE_ASSURE)) {
            // Contrôle de l'assuré
            JAUtil.checkAvs(idExterne);
        } else if (idRole.equals(IntRole.ROLE_AFFILIE) || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
            ((IFormatData) Class.forName(
                    ((BSession) getISession()).getApplication().getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE))
                    .newInstance()).check(idExterne);
        } else if (idRole.equals(IntRole.ROLE_AF)) {
            // On ne fait rien de spécial
        }

    }

    /**
     * Date de création : (22.02.2002 08:33:37)
     * 
     * @return String
     * @param idRole
     *            String
     * @param idExterneRole
     *            String
     */
    @Override
    public String formatIdExterneRole(String idRole, String idExterneRole) {
        if (IntRole.ROLE_ADMINISTRATEUR.equals(idRole)) {
            // TODO : on formatte comment ?
            return idExterneRole.trim();
        }
        // Supprimer les caractères de formattage
        String idExterneRoleFormatte = idExterneRole;
        idExterneRole = _unformat(idExterneRole);
        // Assuré (no. AVS)
        if (idRole.equals(IntRole.ROLE_ASSURE) || idRole.equals(IntRole.ROLE_RENTIER)
                || idRole.equals(IntRole.ROLE_PCF)) {
            if (NSUtil.unFormatAVS(idExterneRole).length() > 11) {
                return NSUtil.formatAVSNew(NSUtil.unFormatAVS(idExterneRole), true);
            } else {
                return JAStringFormatter.formatAVS(idExterneRole);
            }
            // APG, IJAI ou AF
        } else if (idRole.equals(IntRole.ROLE_APG) || idRole.equals(IntRole.ROLE_IJAI)
                || idRole.equals(IntRole.ROLE_AF)) {
            try {
                TIApplication remoteApp = (TIApplication) GlobazSystem
                        .getApplication(TIApplication.DEFAULT_APPLICATION_PYXIS);
                if (idExterneRoleFormatte.equals(remoteApp.getAffileFormater().format(idExterneRole))) {
                    return idExterneRoleFormatte;
                }
            } catch (Exception e1) {
                // Correction de bug pour les numéros AVS du valais
                // On essaie de formater le numéro d'affilié pour voir si c'est un numéro d'affilié et non un NSS
                // Comportement normal après cette ligne (avant entrée en production du valais)
            }
            if (idExterneRole.length() > 10) {
                if (NSUtil.unFormatAVS(idExterneRole).length() > 11) {
                    return NSUtil.formatAVSNew(NSUtil.unFormatAVS(idExterneRole), true);
                } else {
                    return JAStringFormatter.formatAVS(idExterneRole);
                }
            } else if (idRole.equals(IntRole.ROLE_AF)) {
                return idExterneRole;
            } else {
                return giveAffilieNumeroFormate(idExterneRole);
            }
            // Affilié
        } else if (idRole.equals(IntRole.ROLE_AFFILIE) || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
            // return JAStringFormatter.format(idExterneRole, " . ");
            return giveAffilieNumeroFormate(idExterneRole);
        } else if (idRole.equals(IntRole.ROLE_DEBITEUR)) {
            return JadeStringUtil.rightJustify(idExterneRole, 7, '0');
        } else if (IntRole.ROLE_BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES.equals(idRole)) {
            if (_unformat(idExterneRole).length() == LENGTH_NUMERO_NSS) {
                return NSUtil.formatAVSNew(NSUtil.unFormatAVS(idExterneRole), true);
            } else {
                return giveAffilieNumeroFormate(idExterneRole);
            }
        } else {
            return idExterneRole;
        }
    }

    /**
     * @param idExterneRole
     * @return
     */
    private String giveAffilieNumeroFormate(String idExterneRole) {
        try {
            TIApplication remoteApp = (TIApplication) GlobazSystem
                    .getApplication(TIApplication.DEFAULT_APPLICATION_PYXIS);
            return remoteApp.getAffileFormater().format(idExterneRole);
        } catch (Exception e) {
            e.printStackTrace();
            return idExterneRole;
        }
    }

    /**
     * Renvoie le dernier affilié
     * 
     * @return l'affilié courant
     */
    @Override
    public IAFAffiliation getAffiliation() {
        return this.getAffiliation(getIdExterne());
    }

    @Override
    public IAFAffiliation getAffiliation(String idExterneRole) {
        if ((affiliation == null) || !affiliation.getAffilieNumero().equals(idExterneRole)) {
            try {
                BSession sessionAF = (BSession) GlobazSystem.getApplication("NAOS").newSession();
                getISession().connectSession(sessionAF);
                IAFAffiliation affiliation = (IAFAffiliation) sessionAF.getAPIFor(IAFAffiliation.class);
                Hashtable<String, String> criteres = new Hashtable<String, String>();
                if (IntRole.ROLE_AFFILIE_PARITAIRE.equals(getIdRole())) {
                    criteres.put(IAFAffiliation.FIND_FOR_LISTTYPEAFFILIATION, IAFAffiliation.TYPES_AFFILI_PARITAIRES);
                } else if (IntRole.ROLE_AFFILIE_PERSONNEL.equals(getIdRole())) {
                    criteres.put(IAFAffiliation.FIND_FOR_LISTTYPEAFFILIATION, IAFAffiliation.TYPES_AFFILI_PERSONNELLES);
                }
                criteres.put(IAFAffiliation.FIND_FOR_IDTIERS, getIdTiers());
                criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, idExterneRole);
                IAFAffiliation[] affiliations = affiliation.findAffiliation(criteres);
                // s'il y a plusieurs résultats on prend celui le plus récent
                if ((affiliations != null) && (affiliations.length > 0)) {
                    // Ajout des Affiliations dans le TreeMap
                    // int numeroAffiliationAPrendre = 0;
                    // String date = null;
                    TreeMap<String, IAFAffiliation> affiliationsSort = new TreeMap<String, IAFAffiliation>();
                    for (int i = 0; i < affiliations.length; i++) {
                        JADate tmp = new JADate(affiliations[i].getDateDebut());
                        affiliationsSort.put(tmp.toStrAMJ(), affiliations[i]);
                    }
                    this.affiliation = affiliationsSort.get(affiliationsSort.lastKey());
                }
            } catch (Exception e) {
                affiliation = null;
            }
        }
        return affiliation;
    }

    /**
     * Renvoie la date de début d'affiliation au format jj.mm.aaaa. Date de création : (27.11.2001 15:15:33) Date de
     * modification : 17.08.2006
     * 
     * @return String : la date de début d'affiliation
     */
    @Override
    public String getDateDebut() {
        if (this.getAffiliation() != null) {
            return this.getAffiliation().getDateDebut();
        } else {
            return "";
        }
    }

    /**
     * Retourne la date de début d'affiliation au format jj.mm.aaaa pour un idExterneRole donné En cas de problème
     * retourne ""
     * 
     * @param idExterneRole
     * @return String : la date de début d'affiliation
     */
    @Override
    public String getDateDebut(String idExterneRole) {
        if (this.getAffiliation(idExterneRole) != null) {
            return this.getAffiliation(idExterneRole).getDateDebut();
        } else {
            return "";
        }
    }

    /**
     * Renvoie la date de début et de fin d'affiliation. Date de création : (20.12.2001 09:30:15)
     * 
     * @return String
     */
    @Override
    public String getDateDebutDateFin() {
        return this.getDateDebut() + "-" + this.getDateFin();
    }

    @Override
    public String getDateDebutDateFin(String idExterneRole) {
        return this.getDateDebut(idExterneRole) + "-" + this.getDateFin(idExterneRole);
    }

    /**
     * Renvoie la date de fin d'affiliation au format jj.mm.aaaa. Date de création : (27.11.2001 15:15:46) Date de
     * modification : 17.08.2006
     * 
     * @return String : la date de début d'affiliation
     */
    @Override
    public String getDateFin() {
        if (this.getAffiliation() != null) {
            return this.getAffiliation().getDateFin();
        } else {
            return "";
        }
    }

    /**
     * Retourne la date de fin d'affiliation au format jj.mm.aaaa pour un idExterneRole donné En cas de problème
     * retourne ""
     * 
     * @param idExterneRole
     * @return String : la date de fin d'affiliation
     */
    @Override
    public String getDateFin(String idExterneRole) {
        if (this.getAffiliation(idExterneRole) != null) {
            return this.getAffiliation(idExterneRole).getDateFin();
        } else {
            return "";
        }
    }

    /**
     * Date de création : (30.01.2002 11:40:08)
     * 
     * @return String
     */
    @Override
    public String getDescription() {
        return this.getDescription(tiers.getSession().getIdLangueISO());
    }

    /**
     * Date de création : (27.11.2001 15:16:35)
     * 
     * @return String
     * @param codeISOLangue
     *            String
     */
    @Override
    public String getDescription(String codeISOLangue) {
        try {
            FWParametersUserCode uc = new FWParametersUserCode();
            uc.setISession(getISession());
            uc.setIdLangue("F");
            // Choisir la langue
            if (codeISOLangue.equalsIgnoreCase("de")) {
                uc.setIdLangue("D");
            } else if (codeISOLangue.equalsIgnoreCase("it")) {
                uc.setIdLangue("I");
            }
            // Récupérer le code
            uc.setIdCodeSysteme(idRole);
            uc.retrieve();
            if (uc.isNew()) {
                return "";
            } else {
                return uc.getLibelle();
            }
        } catch (Exception e) {
            System.out.println("TIRoleOSI.getDescription(): Exception raised: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Renvoie l'id unique de l'entité
     * 
     * @return l'id unique de l'entité
     */
    @Override
    public String getId() {
        return idRole;
    }

    /**
     * Retourne l'id de la catégorie si le rôle est de type affilié et que tiers n'est pas null Retourne 0 dans les
     * autres cas
     */
    @Override
    public String getIdCategorie() {
        return this.getIdCategorie(getIdExterne());
    }

    /**
     * Retourne l'id de la catégorie si le rôle est de type affilié et que tiers n'est pas null Retourne 0 dans les
     * autres cas
     */
    @Override
    public String getIdCategorie(String idExterneRole) {
        // si role de type affilié
        if (getIdRole().equals(IntRole.ROLE_AFFILIE) || getIdRole().equals(IntRole.ROLE_AFFILIE_PARITAIRE)
                || getIdRole().equals(IntRole.ROLE_AFFILIE_PERSONNEL)) {
            if (this.getAffiliation(idExterneRole) != null) {
                return this.getAffiliation(idExterneRole).getTypeAffiliation();
            } else {
                return "0";
            }
        }
        return "0";
    }

    /**
     * Date de création : (27.11.2001 15:14:42) Date de modification :
     * 17.08.2006
     * 
     * @return String
     */
    @Override
    public String getIdExterne() {

        if (!JadeStringUtil.isBlank(idExterne)) {
            return idExterne;
        }

        // Affilié
        if (idRole.equals(IntRole.ROLE_AFFILIE) || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
            idExterne = tiers.getNumAffilieActuel();
            return idExterne;
        } else if (idRole.equals(IntRole.ROLE_ASSURE)) {
            idExterne = tiers.getNumAvsActuel();
            return idExterne;
        } else {
            return "";
        }
    }

    /**
     * Date de création : (27.11.2001 15:13:11)
     * 
     * @return String
     */
    @Override
    public String getIdRole() {
        return idRole;
    }

    /**
     * Date de création : (27.11.2001 15:17:29)
     * 
     * @return String
     */
    @Override
    public String getIdTiers() {
        return tiers.getIdTiers();
    }

    /**
     * Renvoie la session en cours
     * 
     * @return la session en cours
     */
    @Override
    public globaz.globall.api.BISession getISession() {
        return tiers.getISession();
    }

    /**
     * Renvoie la date de dernière modification de l'objet (format DD.MM.YYYY).
     * 
     * @return la date de dernière modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedDate() {
        if (tiers.getSpy() == null) {
            return null;
        }
        return tiers.getSpy().getDate();
    }

    /**
     * Renvoie l'heure de dernière modification de l'objet (format HH:MM:SS).
     * 
     * @return l'heure de dernière modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedTime() {
        if (tiers.getSpy() == null) {
            return null;
        }
        return tiers.getSpy().getTime();
    }

    /**
     * Renvoie l'id du dernier utilisateur qui a modifié l'objet.
     * 
     * @return l'id du dernier utilisateur qui a modifié l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedUser() {
        if (tiers.getSpy() == null) {
            return null;
        }
        return tiers.getSpy().getUser();
    }

    /**
     * @see globaz.osiris.external.IntRole#getMotifFin()
     */
    @Override
    public String getMotifFin() {
        if (this.getAffiliation() != null) {
            return this.getAffiliation().getMotifFin();
        } else {
            return "";
        }
    }

    /**
     * @see globaz.osiris.external.IntRole#getPeriodicite()
     */
    @Override
    public String getPeriodicite() {
        if (this.getAffiliation() != null) {
            return this.getAffiliation().getPeriodicite();
        } else {
            return "";
        }
    }

    /**
     * Date de création : (27.11.2001 15:17:56)
     * 
     * @return String
     */
    @Override
    public IntTiers getTiers() {
        try {
            // Rechargement si null
            if (_tiersOSI == null) {
                _tiersOSI = new TITiersOSI();
                _tiersOSI.setISession(getISession());
                _tiersOSI.retrieve(getIdTiers());
            }
        } catch (Exception e) {
            System.out.println("TIRoleOSI.getTiers(): exception raised: " + e.getMessage());
            e.printStackTrace();
            _tiersOSI = null;
        }
        return _tiersOSI;
    }

    /**
     * Indique si l'entité est nouvelle (i.e. n'existe pas dans la BD)
     * 
     * @return true si l'entité n'existe pas dans la BD; false sinon
     */
    @Override
    public boolean isNew() {
        return tiers.isNew();
    }

    /**
     * Date de création : (06.11.2002 16:36:09)
     * 
     * @return boolean
     */
    @Override
    public boolean isOnError() {
        return tiers.hasErrors();
    }

    /**
     * Date de création : (27.11.2001 15:11:50)
     */
    @Override
    public void retrieve(globaz.globall.api.BITransaction transaction, String idRole, String idExterne)
            throws Exception {
        // Sauver le role
        this.idRole = idRole;
        // Reset du tiers
        _tiersOSI = null;
        // On reçoit un numéro d'affilié
        TIPersonneAvsManager pavsMgr = new TIPersonneAvsManager();
        if (idRole.equals(IntRole.ROLE_AFFILIE) || idRole.equals(IntRole.ROLE_AFFILIE_PARITAIRE)
                || idRole.equals(IntRole.ROLE_AFFILIE_PERSONNEL)) {
            pavsMgr.setForNumAffilieActuel(idExterne);
            // tiers.setNumAffilieActuel(idExterne);
            // tiers.setAlternateKey(2);
            // tiers.retrieve(transaction);
            // On reçoit un numéro d'assuré
        } else if (idRole.equals(IntRole.ROLE_ASSURE) || idRole.equals(IntRole.ROLE_RENTIER)) {
            pavsMgr.setForNumAvsActuel(idExterne);
            // tiers.setNumAvsActuel(idExterne);
            // tiers.setAlternateKey(1);
            // tiers.retrieve(transaction);
            // Autres cas, non gérés
        } else if (idRole.equals(IntRole.ROLE_APG) || idRole.equals(IntRole.ROLE_IJAI)
                || idRole.equals(IntRole.ROLE_AF)) {
            if (idExterne.length() > 10) {
                pavsMgr.setForNumAvsActuel(idExterne);
                // tiers.setNumAvsActuel(idExterne);
                // tiers.setAlternateKey(1);
                // tiers.retrieve(transaction);
            } else if (idRole.equals(IntRole.ROLE_AF)) {
                // TODO sch Voir comment on doit faire pour récupérer le numéro
                // de dossier
                getTiers().getISession();
                try {
                    pavsMgr.setForIdTiers(ALRecupNoDossierAF.getIdTiersAVS(idExterne, TIToolBox.getCollection(),
                            (BSession) getTiers().getISession()));
                } catch (Exception e) {
                    throw new Exception("TIRoleOSI.retrieve(): Role not supported");
                }

            } else {
                pavsMgr.setForNumAffilieActuel(idExterne);
                // tiers.setNumAffilieActuel(idExterne);
                // tiers.setAlternateKey(2);
                // tiers.retrieve(transaction);
            }
        } else if (idRole.equals(IntRole.ROLE_DEBITEUR) || idRole.equals(IntRole.ROLE_FCF)
                || idRole.equals(IntRole.ROLE_AMC)) {
            pavsMgr.setForIdTiers(idExterne);
        } else if (idRole.equals(IntRole.ROLE_ADMINISTRATEUR)) {
            pavsMgr.setForNumAffilieActuel(idExterne);
            // tiers.setNumAffilieActuel(idExterne);
            // tiers.setAlternateKey(2);
            // tiers.retrieve(transaction);
        } else {
            throw new Exception("TIRoleOSI.retrieve(): Role not supported : " + idRole);
        }
        pavsMgr.setISession(getISession());
        pavsMgr.find(transaction);
        if (pavsMgr.size() <= 0) {
            throw new Exception("TIRoleOSI.retrieve(): Unable to retrieve role for id: " + idExterne);
        } else if (pavsMgr.size() > 1) {
            String idTiersOld = null;
            for (int i = 0; i < pavsMgr.size(); i++) {
                TITiersViewBean pers = (TITiersViewBean) pavsMgr.getEntity(i);
                if (idTiersOld == null) {
                    idTiersOld = pers.getIdTiers();
                }
                if (!idTiersOld.equals(pers.getIdTiers())) {
                    throw new Exception("TIRoleOSI.retrieve(): Unable to retrieve role for id: " + idExterne);
                }
                idTiersOld = pers.getIdTiers();
            }
        }
        tiers = (TITiersViewBean) pavsMgr.get(0);
    }

    /**
     * Date de création : (27.11.2001 15:11:50)
     */
    @Override
    public void retrieve(String idRole, String idExterne) throws Exception {
        this.retrieve(null, idRole, idExterne);
    }

    /**
     * @see globaz.osiris.external.IntRole#retrieve(String, String, BiTransaction)
     */
    @Override
    public void retrieve(String idTiers, String idRole, BITransaction transaction) throws Exception {
        // Sauver le role
        this.idRole = idRole;
        // Reset du tiers
        _tiersOSI = null;
        // retrieve du tiers
        tiers.setIdTiers(idTiers);
        tiers.setISession(getISession());
        tiers.retrieve(transaction);
    }

    /**
     * Modifie la session en cours
     * 
     * @param newISession
     *            la nouvelle session
     */
    @Override
    public void setISession(globaz.globall.api.BISession newSession) {
        tiers.setISession(newSession);
    }
}
