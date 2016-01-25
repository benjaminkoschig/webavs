<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.home.PCTypeChambreAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.home.TypeChambre"%>
<%@page import="globaz.pegasus.utils.PCTypeChambreHelper"%>
<%
    Boolean first = true;
	PCTypeChambreAjaxViewBean  viewBean=(PCTypeChambreAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String selected="";
	String particularite="";
%>

	<liste>
		<%if(viewBean.iterator().hasNext()){%>
			<select name="idTypeChambre" class="typeChambre">
		
	        <%for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
	        	selected="";
	        	particularite="";
				TypeChambre donnee=((TypeChambre)itDonnee.next()); 
					/*String  designation = donnee.getSimpleTypeChambre().getDesignation()+
					" ("+objSession.getCodeLibelle(donnee.getSimpleTypeChambre().getCsCategorie())+") "; */
					String designation = PCTypeChambreHelper.getTypeChambreDescription(donnee,objSession);
					  /*if(tier!=null){
							designation= designation + " - " + tier; 
					  }*/
					%>
					<%if(first){%><option value=""> </option> <%}
					  if (viewBean.getForIdTier()!=null){
						if(viewBean.getForIdTier().equals(donnee.getPersonneEtendue().getId())){
							particularite="particularite='true'";
							selected ="selected='selected'";
						}
					  }
					%>
				<option value="<%=donnee.getSimpleTypeChambre().getId()%>" <%=selected+" "+particularite%>><%=designation%></option>
				
				<%first = false;}%>
	        </select>
	    <%}else{%>
	    	<messageListeVide><ct:FWLabel key="JSP_PC_TAXE_JOURNALIERE_LISTE_TYPE_CHAMBRE_VIDE"/></messageListeVide>
        <%}%>
	</liste>