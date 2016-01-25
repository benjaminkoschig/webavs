package globaz.osiris.impl.print.itext.list;

import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import java.util.Iterator;
import java.util.Vector;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author dda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class CAProcessImprRappelCSC extends FWIScriptDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected final static String CS_SATELLITE = "509024";
    protected static final int NUM_SATELLITE_LENGTH = 2;

    protected CAApplication application = null;
    protected JasperPrint dispositionsLegales = null;
    protected JasperPrint extraitCompteDoc = null;

    protected ICADocumentRappelCSC rappel = null;
    protected Iterator rappelIt = null;

    /**
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAProcessImprRappelCSC(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    public CAProcessImprRappelCSC(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.07.2003 07:32:07)
     * 
     * @return java.lang.String
     */
    protected String _getAdresseCourrier() {
        return rappel.getLigneAdresse();
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _headerText() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        return ((super.size() > 0) && !super.isAborted());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIDocument#bindData(String)
     */
    @Override
    public void bindData(String arg0) throws Exception {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2003 07:17:28)
     * 
     * @param data
     *            java.lang.Object
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void bindObject(Object data) throws java.lang.Exception {
        rappelIt = ((Vector) data).iterator();
    }

    protected CAApplication getApplication() throws Exception {
        if (application == null) {
            application = (CAApplication) getSession().getApplication();
        }

        return application;
    }

    protected String getNumAffilieAndSatellite(ICADocumentRappelCSC rappel) {
        String tmpNumAffilie = rappel.getIdExterneRole();
        if (!JadeStringUtil.isBlank(rappel.getIdTriFormate())) {
            tmpNumAffilie += " / ";
            tmpNumAffilie += rappel.getIdTriFormate();
        }
        return tmpNumAffilie;
    }

    protected String getSatelliteLocalite(ICADocumentRappelCSC rappel, String isoLangue) throws Exception {
        String codeAdministation;

        if ((!JadeStringUtil.isBlank(rappel.getIdTriFormate()))
                && (rappel.getIdTriFormate().length() >= CAProcessImprRappelCSC.NUM_SATELLITE_LENGTH)) {
            codeAdministation = rappel.getIdTriFormate().substring(
                    rappel.getIdTriFormate().length() - CAProcessImprRappelCSC.NUM_SATELLITE_LENGTH,
                    rappel.getIdTriFormate().length());

            if (JadeStringUtil.isBlank(codeAdministation)) {
                codeAdministation = CACSCSatellites.CODE_GENEVE;
            }
        } else {
            codeAdministation = CACSCSatellites.CODE_GENEVE;
        }

        CACSCSatellites readXml = new CACSCSatellites();
        return readXml.getLocation(codeAdministation.trim(), isoLangue.toLowerCase());
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        return rappelIt.hasNext();
    }

    /**
     * Méthode appelé pour lancer l'exportation du document Par défaut ne pas utiliser car déjà implémenté par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire Date de création : (17.02.2003 14:44:15)
     */
    @Override
    public void returnDocument() throws FWIException {
        super.imprimerListDocument();
    }
}
