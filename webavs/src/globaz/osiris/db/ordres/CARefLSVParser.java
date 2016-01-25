package globaz.osiris.db.ordres;

import globaz.globall.api.BISession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntRole;
import globaz.osiris.parser.IntReferenceBVRParser;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (18.02.2002 15:33:30)
 * 
 * @author: Administrator
 */
public class CARefLSVParser extends CARefBVRParser implements IntReferenceBVRParser {
    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.02.2002 09:26:42)
     */
    public CARefLSVParser() {
        super();

        // Charger les param�tres du parser
        try {
            setPosIdExterneRole(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.posIdExterneRole")));
            setLenIdExterneRole(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.lenIdExterneRole")));
            setPosIdRole(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.posIdRole")));
            setLenIdRole(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.lenIdRole")));
            setValIdRole(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.valIdRole")));
            setPosIdExterneSection(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.posIdExterneSection")));
            setLenIdExterneSection(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.lenIdExterneSection")));
            setPosTypeSection(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.posTypeSection")));
            setLenTypeSection(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.lenTypeSection")));
            setValTypeSection(Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    "osiris.class.CAReferenceLSVParser.valTypeSection")));

            // Si le tiers n'est pas instanci�
            if (getRole() == null) {
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                BISession session = currentApplication.newSession();
                setRole((IntRole) globaz.globall.db.GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(session, IntRole.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
