<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0011";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew  += "&idTiers=" + ((request.getParameter("idTiers")!=null)?request.getParameter("idTiers"):"") ;
	rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.tiers.compositionTiers.lister";
bFind = true;
</SCRIPT>
<%
globaz.pyxis.db.tiers.TITiers viewBean = (globaz.pyxis.db.tiers.TITiers  )session.getAttribute ("viewBean");							
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Verbindungen zwischen Partner<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

		<TR>
	    	<td nowrap>
	    		<input name="navLink" class="navLinkTiers"  value="Partner" accesskey="2" type="button" style="cursor:hand;border : 0 0 0 0;color:blue;text-decoration:underline;background : #B3C4DB;margin : 0 0 0 0;padding : 0 0 0 0;font-weight:normal;font-size: 8pt"
				onclick="location.href='<%=request.getContextPath()%>\\pyxis?userAction=pyxis.tiers.tiers.diriger&idTiers=<%=viewBean.getIdTiers()%>'"> [ALT + 2]</td>
            <TD nowrap colspan="2">
				<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
	     	</TD>
		</TR>
		<TR>
			<TD nowrap width="128"></TD>
            <TD nowrap colspan="2">
				<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocaliteLong()%>" readonly>
			<TD width="10"></TD>
   			<TD>
				<INPUT type="hidden" name="selectedId" value='<%=viewBean.getIdTiers()%>' >
				<INPUT type="hidden" name="forIdTiersEnfantParent" value='<%=viewBean.getIdTiers()%>' >
				<INPUT type="hidden" name="forTypeNotIn" value='507001,507002' >
				<%
				java.util.HashSet except = new java.util.HashSet();
				except.add(globaz.pyxis.db.tiers.TICompositionTiers.CS_CONJOINT);
				except.add(globaz.pyxis.db.tiers.TICompositionTiers.CS_SUCCURSALE);
				%>

				Nur die verbindungen des Typs anzeigen : <ct:FWCodeSelectTag name="forTypeLien"
					      defaut=" "
						wantBlank="<%=true%>"
					      codeType="PYTYPELIEN"
						except="<%=except%>"
				/>
	     	</TD>
        </TR>

	 <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>