package ch.globaz.orion.ws.affiliation;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.orion.process.EBDanPreRemplissage;
import globaz.orion.utils.EBDanUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.ws.enums.ModeDeclarationSalaireWebAvs;
import ch.globaz.orion.ws.enums.Role;
import ch.globaz.orion.ws.exceptions.WebAvsException;
import ch.globaz.orion.ws.service.AppAffiliationService;
import ch.globaz.orion.ws.service.UtilsService;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.queryexec.bridge.jade.SCM;

@WebService(endpointInterface = "ch.globaz.orion.ws.affiliation.WebAvsAffiliationService")
public class WebAvsAffiliationServiceImpl implements WebAvsAffiliationService {

    @Override
    public Integer findCategorieAffiliation(String numeroAffilie, String dateDebutPeriode, String dateFinPeriode) {
        Checkers.checkNotEmpty(numeroAffilie, "NumeroAffilie");
        Checkers.checkDateAvs(dateDebutPeriode, "DateDebutPeriode", false);
        Checkers.checkDateAvs(dateFinPeriode, "DateFinPeriode", false);

        return AppAffiliationService.findCategorieAffiliation(UtilsService.initSession(), numeroAffilie,
                dateDebutPeriode, dateFinPeriode);

    }

    @Override
    public List<Integer> findActiveSuiviCaisse(String numeroAffilie, String annee) {
        Checkers.checkNotNull(numeroAffilie, "numeroAffilie");
        Checkers.checkNotNull(annee, "annee");
        List<Integer> list = new ArrayList<Integer>();
        AFAffiliation aff;
        try {
            aff = EBDanUtils.findAffilie(UtilsService.initSession(), numeroAffilie, "31.12." + annee, "01.01." + annee);

            String idTierLpp = findCaisseValide(annee, aff, EBDanPreRemplissage.GENRE_CAISSE_LPP);
            if (!JadeStringUtil.isBlankOrZero(idTierLpp)) {
                list.add(Integer.valueOf(idTierLpp));
            } else {
                list.add(0);
            }
            String idTierLaa = findCaisseValide(annee, aff, EBDanPreRemplissage.GENRE_CAISSE_LAA);
            if (!JadeStringUtil.isBlankOrZero(idTierLaa)) {
                list.add(Integer.valueOf(idTierLaa));
            } else {
                list.add(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private String findCaisseValide(String annee, AFAffiliation aff, String typeCaisse) throws Exception {
        // On s'occupe du suivi LPP
        AFSuiviCaisseAffiliationManager caisseLppManager = new AFSuiviCaisseAffiliationManager();
        caisseLppManager.setSession(UtilsService.initSession());
        caisseLppManager.setForAffiliationId(aff.getAffiliationId());
        caisseLppManager.setForGenreCaisse(typeCaisse);
        caisseLppManager.setOrder("MYDDEB desc");
        caisseLppManager.setForAnnee(annee);
        caisseLppManager.find(BManager.SIZE_USEDEFAULT);

        if (!caisseLppManager.isEmpty()) {
            AFSuiviCaisseAffiliation caisse = (AFSuiviCaisseAffiliation) caisseLppManager.getFirstEntity();
            return caisse.getIdTiersCaisse();
        }
        return null;
    }

    @Override
    public AffiliationAdresse findAdresseCourrierAffilie(String numeroAffilie) throws WebAvsException {
        if (JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            throw new WebAvsException("numeroAffilie cannot be null or zero");
        }

        AdresseTiersDetail adresseTiersDetail = null;
        BSession session = UtilsService.initSession();
        try {
            // initialise un contexte et le start
            BSessionUtil.initContext(session, Thread.currentThread());

            // récupération de l'adresse de courrier
            adresseTiersDetail = AppAffiliationService.findAdresseAffilie(numeroAffilie,
                    AdresseService.CS_TYPE_COURRIER);
            if (adresseTiersDetail == null || adresseTiersDetail.getFields() == null
                    || adresseTiersDetail.getFields().isEmpty()) {
                return null;
            }

            Map<String, String> fieldsAdresse = adresseTiersDetail.getFields();
            return new AffiliationAdresse(fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_RUE),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_NUMERO),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_CASE_POSTALE),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_NPA),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_ATTENTION),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_D1),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_D3), null);
        } catch (Exception e) {
            JadeLogger.error(this.getClass(),
                    "Unable to find address for affilie :" + numeroAffilie + " -> " + e.getMessage());
            throw new WebAvsException("Unable to find address for affilie : " + numeroAffilie);
        } finally {
            BSessionUtil.stopUsingContext(Thread.currentThread());
        }
    }

    @Override
    public AffiliationAdresse findAdresseDomicileAffilie(String numeroAffilie) throws WebAvsException {
        if (JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            throw new WebAvsException("numeroAffilie cannot be null or zero");
        }

        AdresseTiersDetail adresseTiersDetail = null;
        BSession session = UtilsService.initSession();
        try {
            // initialise un contexte et le start
            BSessionUtil.initContext(session, Thread.currentThread());

            // récupération de l'adresse de domicile
            adresseTiersDetail = AppAffiliationService.findAdresseAffilie(numeroAffilie,
                    AdresseService.CS_TYPE_DOMICILE);
            if (adresseTiersDetail == null || adresseTiersDetail.getFields() == null
                    || adresseTiersDetail.getFields().isEmpty()) {
                return null;
            }

            Map<String, String> fieldsAdresse = adresseTiersDetail.getFields();
            return new AffiliationAdresse(fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_RUE),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_NUMERO),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_CASE_POSTALE),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_NPA),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_ATTENTION),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_D1),
                    fieldsAdresse.get(AdresseTiersDetail.ADRESSE_VAR_D3), null);
        } catch (Exception e) {
            JadeLogger.error(this.getClass(),
                    "Unable to find address for affilie :" + numeroAffilie + " -> " + e.getMessage());
            throw new WebAvsException("Unable to find address for affilie : " + numeroAffilie);
        } finally {
            BSessionUtil.stopUsingContext(Thread.currentThread());
        }
    }

    @Override
    public String findAdresseFormateeAffilie(String numeroAffilie) throws WebAvsException {
        if (JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            throw new WebAvsException("numeroAffilie cannot be null or zero");
        }

        BSession session = UtilsService.initSession();
        try {
            // initialise un contexte et le start
            BSessionUtil.initContext(session, Thread.currentThread());

            // récupération de l'adresse de courrier
            AdresseTiersDetail adresseCourrierTiersDetail = AppAffiliationService.findAdresseAffilie(numeroAffilie,
                    AdresseService.CS_TYPE_COURRIER);
            if (adresseCourrierTiersDetail != null && adresseCourrierTiersDetail.getFields() != null
                    && !adresseCourrierTiersDetail.getFields().isEmpty()) {
                return adresseCourrierTiersDetail.getAdresseFormate();
            }

            // si pas d'adresse de courrier, récupération de l'adresse de domicile
            AdresseTiersDetail adresseDomicileTiersDetail = AppAffiliationService.findAdresseAffilie(numeroAffilie,
                    AdresseService.CS_TYPE_DOMICILE);
            if (adresseDomicileTiersDetail != null && adresseDomicileTiersDetail.getFields() != null
                    && !adresseDomicileTiersDetail.getFields().isEmpty()) {
                return adresseDomicileTiersDetail.getAdresseFormate();
            }

            // si aucune adresse trouvée retourne null
            return null;
        } catch (Exception e) {
            JadeLogger.error(this.getClass(),
                    "Unable to find address for affilie :" + numeroAffilie + " -> " + e.getMessage());
            throw new WebAvsException("Unable to find address for affilie : " + numeroAffilie);
        } finally {
            BSessionUtil.stopUsingContext(Thread.currentThread());
        }
    }

    @Override
    public boolean checkAffiliationAndUpdateModeDeclaration(String numeroAffilie,
            ModeDeclarationSalaireWebAvs modeDeclarationSalaireWebAvs) throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null or empty");
        }

        BSession session = UtilsService.initSession();

        // recherche de l'affiliation
        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(session);
        affiliationManager.setForAffilieNumero(numeroAffilie);
        affiliationManager.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_EMPLOY,
                CodeSystem.TYPE_AFFILI_INDEP_EMPLOY, CodeSystem.TYPE_AFFILI_INDEP });
        affiliationManager.setFromDateFin(JACalendar.todayJJsMMsAAAA());

        try {
            affiliationManager.find(BManager.SIZE_USEDEFAULT);
            if (affiliationManager.size() > 0) {
                // récupération de l'affiliation
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getFirstEntity();

                // mise à jour du mode de déclaration de salaire de l'affiliation
                if (!CodeSystem.TYPE_AFFILI_INDEP.equals(affiliation.getTypeAffiliation())) {
                    updateModeDeclarationSalaire(session, affiliation, modeDeclarationSalaireWebAvs);
                }
                return true;
            } else {
                // aucun affiliation trouvée
                return false;
            }
        } catch (Exception e) {
            JadeLogger.error(this, "technical error when checkAffiliation for numeroAffilie : " + numeroAffilie);
            throw new WebAvsException("technical error when checkAffiliation for numeroAffilie : " + numeroAffilie);
        }
    }

    @Override
    public ModeDeclarationSalaireWebAvs findModeDeclarationSalairesAffilie(String numeroAffilie) throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie must be defined");
        }

        // récupération de l'affiliation
        AFAffiliation affiliation = AppAffiliationService.findAffiliation(numeroAffilie);
        if (affiliation == null) {
            throw new WebAvsException("unable to define mode. Affiliation not found for numeroAffilie : "
                    + numeroAffilie);
        }

        // récupération du mode de déclaration
        return determineModeDeclarationSalaires(affiliation);
    }

    @Override
    public List<Role> findRoleAffilie(String numeroAffilie) throws WebAvsException {
        if (JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie must be defined");
        }

        List<Role> roles = new ArrayList<Role>();

        BSession session = UtilsService.initSession();

        AFAffiliation affiliation;

        // recherche de l'affiliation
        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(session);
        affiliationManager.setForAffilieNumero(numeroAffilie);

        try {
            affiliationManager.find(BManager.SIZE_USEDEFAULT);
            if (affiliationManager.size() > 0) {
                // récupération de l'affiliation
                affiliation = (AFAffiliation) affiliationManager.getFirstEntity();
            } else {
                JadeLogger.error(AppAffiliationService.class, "affiliation not found : " + numeroAffilie);
                throw new WebAvsException("affiliation not found : " + numeroAffilie);
            }

            String typeAffiliation = affiliation.getTypeAffiliation();
            if (CodeSystem.TYPE_AFFILI_EMPLOY.equals(typeAffiliation)
                    || CodeSystem.TYPE_AFFILI_LTN.equals(typeAffiliation)) {
                roles.add(Role.EMPLOYEUR);
            } else if (CodeSystem.TYPE_AFFILI_INDEP.equals(typeAffiliation)) {
                roles.add(Role.INDEPENDANT);
            } else if (CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(typeAffiliation)) {
                roles.add(Role.EMPLOYEUR);
                roles.add(Role.INDEPENDANT);
            }

            // définir si l'affilié a le rôle AF
            if (hasRoleAf(numeroAffilie, session)) {
                roles.add(Role.AF);
            }

            return roles;
        } catch (Exception e) {
            JadeLogger.error(AppAffiliationService.class, "technical error when findAffiliation for numeroAffilie : "
                    + numeroAffilie);
            throw new WebAvsException("technical error when findAffiliation for numeroAffilie : " + numeroAffilie);
        }
    }

    @Override
    public InfosAffiliation retrieveInfosAffiliation(String numeroAffilie) throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null or empty");
        }

        AFAffiliation affiliation = AppAffiliationService.findAffiliation(numeroAffilie);
        ModeDeclarationSalaireWebAvs modeDeclarationSalaire = determineModeDeclarationSalaires(affiliation);
        AffiliationAdresse adresseCourrier = findAdresseCourrierAffilie(numeroAffilie);
        AffiliationAdresse adresseDomicile = findAdresseDomicileAffilie(numeroAffilie);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dateDebutAffiliation = null;
        Date dateFinAffiliation = null;
        try {
            if (!JadeStringUtil.isBlankOrZero(affiliation.getDateDebut())) {
                dateDebutAffiliation = sdf.parse(affiliation.getDateDebut());
            }
            if (!JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
                dateFinAffiliation = sdf.parse(affiliation.getDateFin());
            }
        } catch (Exception e) {
            throw new WebAvsException("unable to parse date", e);
        }

        return new InfosAffiliation(Integer.valueOf(affiliation.getAffiliationId()), affiliation.getAffilieNumero(),
                affiliation.getRaisonSociale(), modeDeclarationSalaire, dateDebutAffiliation, dateFinAffiliation,
                adresseCourrier, adresseDomicile);
    }

    @Override
    public Boolean announceMutationsAdressesAffiliation(String numeroAffilie, AffiliationAdresse adresseDomicile,
            AffiliationAdresse adresseCourrier, String remarqueDomicile, String remarqueCourrier, String email)
            throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null");
        }
        try {
            AFAffiliation affiliation = AppAffiliationService.findAffiliation(numeroAffilie);
            if (affiliation == null) {
                throw new IllegalStateException("affiliation not found " + numeroAffilie);
            }
            AffiliationAdresse adresseCourrierActuelle = findAdresseCourrierAffilie(numeroAffilie);
            AffiliationAdresse adresseDomicileActuelle = findAdresseDomicileAffilie(numeroAffilie);
            sendMailMutationsAdresses(adresseDomicile, adresseCourrier, numeroAffilie, affiliation.getRaisonSociale(),
                    adresseCourrierActuelle, adresseDomicileActuelle, remarqueDomicile, remarqueCourrier, email);
            return true;
        } catch (Exception e) {
            throw new WebAvsException("unable to send mail");
        }
    }

    private void sendMailMutationsAdresses(AffiliationAdresse adresseDomicile, AffiliationAdresse adresseCourrier,
            String numeroAffilie, String raisonSociale, AffiliationAdresse adresseCourrierActuelle,
            AffiliationAdresse adresseDomicileActuelle, String remarqueDomicile, String remarqueCourrier, String email)
            throws PropertiesException {
        String to = EBProperties.EMAIL_MUTATION_ADRESSE_AVS.getValue();
        if (JadeStringUtil.isEmpty(to)) {
            throw new PropertiesException("email adresse is empty");
        }

        String subject = "EBusiness : Mutations d'adresses pour l'affilié " + numeroAffilie + " - " + raisonSociale;

        StringBuilder body = new StringBuilder();
        body.append("<table width='800' border='0' cellspacing='0' style='padding:3px;'>");

        // en-tête
        body.append("<tr bgcolor='#4A4C4E'");
        body.append("<td width='50%'>");
        body.append("<b><font color='#FFFFFF'>ADRESSES ACTUELLES</b></font>");
        body.append("</td>");
        body.append("<td>");
        body.append("<b><font color='#FFFFFF'>ADRESSES TRANSMISES</b></font>");
        body.append("</td>");
        body.append("</tr>");

        // en-tête domicile
        body.append("<tr bgcolor='#CEE3F6'>");
        body.append("<td>");
        body.append("<b>Domicile</b>");
        body.append("</td>");
        body.append("<td>");
        body.append("<b>Domicile</b>");
        body.append("</td>");
        body.append("</tr>");

        // adresse domicile
        body.append("<tr valign='top' bgcolor='#CEE3F6'>");
        // actuelles
        body.append("<td>");
        if (adresseDomicileActuelle != null) {
            body.append(adresseDomicileActuelle.getAdresseFormatee(null));
        }
        body.append("</td>");
        // transmises
        body.append("<td>");
        if (adresseDomicile != null) {
            body.append(adresseDomicile.getAdresseFormatee(raisonSociale));
        }
        body.append("<br>");
        body.append("<br>");
        body.append("<b><i>Remarque : </b></i>");
        if (!JadeStringUtil.isEmpty(remarqueDomicile)) {
            body.append("<br><i>" + remarqueDomicile + "</i>");
        } else {
            body.append("<i> - </i>");
        }
        body.append("</td>");
        body.append("</tr>");

        // ligne vide
        body.append("<tr bgcolor='#CEE3F6'>");
        body.append("<td>");
        body.append("</td>");
        body.append("<td>");
        body.append("</td>");
        body.append("</tr>");

        // ligne vide
        body.append("<tr bgcolor='#E0F0FF'>");
        body.append("<td>");
        body.append("</td>");
        body.append("<td>");
        body.append("</td>");
        body.append("</tr>");

        // en-tête courrier
        body.append("<tr bgcolor='#E0F0FF'>");
        body.append("<td>");
        body.append("<b>Courrier</b>");
        body.append("</td>");
        body.append("<td>");
        body.append("<b>Courrier</b>");
        body.append("</td>");
        body.append("</tr>");

        // Adresse courrier
        body.append("<tr valign='top' bgcolor='#E0F0FF'>");
        // actuelles
        body.append("<td>");
        if (adresseCourrierActuelle != null) {
            body.append(adresseCourrierActuelle.getAdresseFormatee(null));
        }
        body.append("</td>");
        // transmises
        body.append("<td>");
        if (adresseCourrier != null) {
            body.append(adresseCourrier.getAdresseFormatee(raisonSociale));
        }
        body.append("<br>");
        body.append("<br>");
        body.append("<b><i>Remarque : </b></i>");
        if (!JadeStringUtil.isEmpty(remarqueCourrier)) {
            body.append("<br><i>" + remarqueCourrier + "</i>");
        } else {
            body.append("<i> - </i>");
        }
        body.append("</td>");
        body.append("</tr>");
        body.append("</table>");

        body.append("<br>");
        body.append("<br>");
        body.append("Numéro d'affilié: "+numeroAffilie);
        body.append("<br>");
        body.append("Adresse email: "+email);

        sendMail(to, subject, body.toString());
    }

    private static void sendMail(String email, String subject, String body) {
        try {
            JadeSmtpClient.getInstance().sendMail(email, subject, body, null);
        } catch (Exception e) {
            JadeLogger.error("Unabled to send mail to " + email, e);
        }
    }

    private boolean hasRoleAf(String numeroAffilie, BSession session) throws Exception {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null or empty");
        }

        // préparation de la requête de base
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select count(*) as nb from schema.AFAFFIP aff ");
        stringBuilder.append("inner join schema.AFADHEP adh on aff.MAIAFF=adh.MAIAFF ");
        stringBuilder.append("inner join schema.AFCOTIP coti on coti.MRIADH=adh.MRIADH ");
        stringBuilder.append("inner join schema.AFASSUP ass on coti.MBIASS=ass.MBIASS ");

        // préparation des paramètres
        List<String> params = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());
        params.add(numeroAffilie);
        params.add(today);

        // préparation de la clause where
        SQLWriter sqlWriter = SQLWriter.write().append(stringBuilder.toString())
                .where("malnaf='?' and (medfin=0 or medfin>?) and ass.MBTTYP=812002", params);

        // exécution de la requête
        BigDecimal nb = SCM.newInstance(BigDecimal.class).session(session).query(sqlWriter.toSql()).executeAggregate();
        return nb.compareTo(BigDecimal.ZERO) > 0;
    }

    private void updateModeDeclarationSalaire(BSession session, AFAffiliation aff,
            ModeDeclarationSalaireWebAvs modeDeclarationSalaireWebAvs) throws Exception {
        String modeDeclarationSalaireWebavs;

        // si le mode de déclaration est DAN
        if (ModeDeclarationSalaireWebAvs.DAN.equals(modeDeclarationSalaireWebAvs)) {
            // Si mode mixte (CCVD)
            if (CodeSystem.DECL_SAL_PRE_MIXTE.equals(aff.getDeclarationSalaire())
                    || CodeSystem.DECL_SAL_MIXTE_DAN.equals(aff.getDeclarationSalaire())) {
                modeDeclarationSalaireWebavs = CodeSystem.DECL_SAL_MIXTE_DAN;
            } else {
                modeDeclarationSalaireWebavs = CodeSystem.DS_DAN;
            }
        }

        // si le mode de déclaration est PUCS
        else if (ModeDeclarationSalaireWebAvs.PUCS.equals(modeDeclarationSalaireWebAvs)) {
            modeDeclarationSalaireWebavs = CodeSystem.DS_ENVOI_PUCS;
        }

        // si le mode de déclaration est autre que DAN ou PUCS, on ne met pas à jour l'affiliation
        else {
            return;
        }

        // mise à jour de l'affiliation (1-11)
        if (!aff.isNew() && !modeDeclarationSalaireWebavs.equals(aff.getDeclarationSalaire())) {
            aff.setDeclarationSalaire(modeDeclarationSalaireWebavs);
            aff.setSession(session);
            aff.wantCallValidate(false);
            aff.wantCallMethodAfter(false);
            aff.wantCallMethodBefore(false);
            aff.update(session.getCurrentThreadTransaction());
        }
    }

    private ModeDeclarationSalaireWebAvs determineModeDeclarationSalaires(AFAffiliation affiliation) {
        if (affiliation == null) {
            throw new IllegalArgumentException("affiliation is null");

        }
        String modeDeclarationSalaireWebAvsCs = affiliation.getDeclarationSalaire();

        ModeDeclarationSalaireWebAvs modeDeclarationSalaireWebAvs;
        // si mode PUCS
        if (CodeSystem.DS_ENVOI_PUCS.equals(modeDeclarationSalaireWebAvsCs)) {
            modeDeclarationSalaireWebAvs = ModeDeclarationSalaireWebAvs.PUCS;
        }
        // si mode DAN
        else if (CodeSystem.DECL_SAL_MIXTE_DAN.equals(modeDeclarationSalaireWebAvsCs)
                || CodeSystem.DS_DAN.equals(modeDeclarationSalaireWebAvsCs)) {
            modeDeclarationSalaireWebAvs = ModeDeclarationSalaireWebAvs.DAN;
        }
        // si autre mode
        else {
            modeDeclarationSalaireWebAvs = ModeDeclarationSalaireWebAvs.AUTRE;
        }

        return modeDeclarationSalaireWebAvs;
    }

}
