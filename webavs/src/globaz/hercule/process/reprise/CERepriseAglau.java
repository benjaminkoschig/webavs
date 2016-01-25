package globaz.hercule.process.reprise;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.service.CEAffiliationService;
import globaz.naos.db.affiliation.AFAffiliation;
import java.io.BufferedReader;
import java.util.StringTokenizer;

public class CERepriseAglau extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // TODO Auto-generated method stub
        BufferedReader fileIn = new BufferedReader(new java.io.FileReader("d://aglau/CE 20110519.csv"));
        StringTokenizer tokens;
        String line = null;
        while ((line = fileIn.readLine()) != null) {
            tokens = new StringTokenizer(line, ";");
            String noAffilie = tokens.nextToken();
            String dateEffective = "01.01." + tokens.nextToken();
            String periodeDebut = tokens.nextToken();
            String periodeFin = tokens.nextToken();
            AFAffiliation aff = CEAffiliationService.findAffilie(getSession(), noAffilie, periodeDebut, periodeFin);
            if ((aff == null) || aff.isNew()) {
                System.out.println("Numéro affilié invalide : " + noAffilie);
                continue;
            }
            String reviseur = tokens.nextToken();
            /*
             * Code 1 CCVD 2 SUVA 3 DI Fichier excel 0 D. Irminger 1 CCVD 2 SUVA 3 Autre
             */
            if ("0".equals(reviseur)) {
                reviseur = "1";
            } else if ("3".equals(reviseur)) {
                reviseur = "";
            }
            String resultat = "";
            if (tokens.hasMoreTokens()) {
                resultat = tokens.nextToken();
            }
            boolean difference = false;
            String motif = "";
            difference = "1051".equals(resultat) || "1053".equals(resultat);
            if ("1051".equals(resultat)) {
                difference = true;
                motif = "810001";
            }
            if ("1053".equals(resultat)) {
                difference = true;
                motif = "810002";
            }
            CEControleEmployeurManager mgr = new CEControleEmployeurManager();
            mgr.setSession(getSession());
            mgr.setForAffiliationId(aff.getAffiliationId());
            mgr.setForDateDebutControle(periodeDebut);
            mgr.setForDateFinControle(periodeFin);
            if (mgr.getCount() > 0) {
                continue;
            }

            CEControleEmployeur controle = new CEControleEmployeur();
            controle.setSession(getSession());
            controle.setDateDebutControle(periodeDebut);
            controle.setDateFinControle(periodeFin);
            controle.setIdReviseur(reviseur);
            controle.setNumAffilie(aff.getAffilieNumero());
            controle.setAffiliationId(aff.getAffiliationId());
            controle.setDateEffective(dateEffective);
            controle.setErreur(new Boolean(difference));
            controle.setFlagDernierRapport(new Boolean(true));
            controle.setDebitCredit(motif);
            controle.setGenreControle(CEControleEmployeur.CS_GENRE_CONTROLE_PERIODIQUE);
            controle.setDatePrevue(dateEffective);
            controle.add(getTransaction());
            if (getTransaction().hasErrors()) {
                System.out.println(noAffilie + " : " + getTransaction().getErrors());
                getTransaction().clearErrorBuffer();
                getTransaction().rollback();
            } else {
                getTransaction().commit();
                // System.out.println("added : " + aff.getAffilieNumero());
            }

        }

        return !isOnError();
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.UPDATE_LONG;
    }

}
