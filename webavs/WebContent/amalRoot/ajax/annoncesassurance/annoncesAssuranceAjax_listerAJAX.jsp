<%@page import="ch.globaz.amal.business.models.annonce.AnnoncesCaisse"%>
<%@page import="globaz.amal.vb.annoncesassurance.AMAnnoncesAssuranceAjaxViewBean"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
AMAnnoncesAssuranceAjaxViewBean viewBeanAnnonces=(AMAnnoncesAssuranceAjaxViewBean)request.getAttribute("viewBean");
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>
<liste>
	<%
	int cpt = 1;
	for(JadeAbstractModel model :viewBeanAnnonces.getSearchModel().getSearchResults()){
		AnnoncesCaisse annonce = (AnnoncesCaisse) model;

		%>
		<tr idEntity="<%=annonce.getNoCaisseMaladie()%>">
			<td><%=annonce.getDateAvisRip()%></td>
			<td>
				<a data-g-download="docType:csv,
	                    parametres:¦<%=annonce.getDateAvisRip()%>,<%=annonce.getNoCaisseMaladie()%>¦,
	                    serviceClassName:ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService,
	                    serviceMethodName:createFichierListeAnnonce,
	                    docName:listeSubsidesAnnonces"></a> 
			</td>
			<!-- PAS ENCORE ... un fichier cosama par année... il faudrait détecter les années pour proposer x boutons... 
			<td>
				<a data-g-download="docType:txt,
						displayOnlyImage:true,
	                    parametres:¦<%=annonce.getDateAvisRip()%>,<%=annonce.getNoCaisseMaladie()%>¦,
	                    serviceClassName:ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService,
	                    serviceMethodName:createFichierCosamaAnnonce,
	                    docName:cosamaSubsidesAnnonces"></a> 
			</td>
			 -->
		</tr>
<%
	cpt++;
	}%>
</liste>
<ct:serializeObject objectName="viewBeanAnnonces.simpleAnnonce" destination="xml"/>