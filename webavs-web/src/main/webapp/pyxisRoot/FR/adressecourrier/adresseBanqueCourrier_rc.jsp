<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	actionNew = servletContext + mainServletPath + "?userAction=pyxis.adressecourrier.avoirAdresse.afficher&_method=add";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew  += "&idTiers=" + ((request.getParameter("idTiers")!=null)?request.getParameter("idTiers"):"") ;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.adressecourrier.avoirAdresse.lister";
</SCRIPT>
<%
	globaz.pyxis.db.tiers.TIBanqueViewBean viewBean = (globaz.pyxis.db.tiers.TIBanqueViewBean)session.getAttribute ("viewBean");							
%>




<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Adresse de courrier d'une banque
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
		<TR>
                   <TD nowrap width="100">Banque</TD>
                   <TD nowrap width="95%">
              	   <INPUT type="text" name="description" value="<%=viewBean.getDetailBanqueHorizontale()%>" class="inputDisabled" readonly>
			   <INPUT type="hidden" name="forIdTiers" value='<%=viewBean.getIdTiers()%>' >

		    </TD>
		    <TD>
                        <INPUT type="hidden" name="selectedId" value='<%=request.getParameter("selectedId")%>' >
                  </TD>
             </TR>
		<tr>
		 <TD colspan="2"><input type="checkbox" name="inclureHistorique">inclure l'historique des modifications dans la recherche
			</TD>
		</tr>

	 				
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>

		
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>