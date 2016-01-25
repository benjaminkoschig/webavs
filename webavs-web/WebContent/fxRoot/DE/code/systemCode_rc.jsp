<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script>
	usrAction = "fx.code.systemCode.lister";
	<%if ((request.getParameter("forGroupLike")!= null)&&(!"".equals(request.getParameter("forGroupLike")))) {%>
		bFind = true;
	<%}
	bButtonNew &= "TRUE".equalsIgnoreCase(request.getParameter("droitMutation"));
	if (globaz.fx.common.application.servlet.FXMainServlet.isDevMode()) {
		// force à true si devMode actif
		bButtonNew = true;	
	}
	actionNew +="&selectedId="+request.getParameter("selectedId")+"&group="+request.getParameter("forGroupLike");	
	%>
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				<DIV style="width: 100%">
					<SPAN class="idEcran">FX0202</SPAN>
					Suche nach den Parametern
				</DIV><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							
							<td>Bezeichnung</td>						
							<td><input type="text" class="libelleLong" name="forLibelleLike"></td>
							<td style="width:1cm">&nbsp;</td>
							
							<td>Familie</td>						
							<td>
							<input type="text" 
								class="libelleLongDisabled" 
								readOnly name="forGroupLike" 
								value="<%=(request.getParameter("forGroupLike")==null)?"":request.getParameter("forGroupLike")%>">
							<%if ("FALSE".equalsIgnoreCase(request.getParameter("droitMutation"))) {%>
								<img src="<%=request.getContextPath()+"/images/cadenas.gif"%>">		
							<%}%>
							
							</td>
						</tr>
						<tr>
							<td>Wert</td>						
							<td><input type="text" class="libelleLong" name="forLibelleUtilisateurLike"></td>
							<td style="width:1cm">&nbsp;</td>
							<td>Code</td>						
							<td><input type="text" class="libelleLong" name="forCodeUtilisateur"></td>
						</tr>
						<tr>
							
							<td><input  name="forDroitMutation"   type="checkbox"  CHECKED></td>
							<td>Nur die veränderbaren Familien anzeigen</td>
							<td>&nbsp;</td>

							<td><input name="forActif"   type="checkbox"  ></td>
							<td>Nur die aktiven Parameter anzeigen </td>

						</tr>
						
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<ct:menuChange menuId="optionsBlank" displayId="options" showTab="menu"/>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>