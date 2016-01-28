<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0005";
rememberSearchCriterias = true;
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
bButtonNew = false;
CAApercuComptesViewBean compteAnnexe = (CAApercuComptesViewBean )session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT>
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.apercuComptes.lister";
bFind = false;

function afficheMsg() {
	var contenu = document.getElementById('numNomSearch').value;
	document.getElementById('msgPonctuation').style.display='none';
	if (contenu.length > 0) {
			if (contenu.charAt(0) >= 0 && contenu.charAt(0) <= 9) {
				document.getElementById('msgPonctuation').style.display='';
			}
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche der Abrechnungskonti<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="70">Nr. oder Name</TD>
            <TD nowrap width="200">

           <%
           String likeNumNomPrint = "";
           String idTiersVG = "";
           if (globaz.jade.client.util.JadeStringUtil.isBlank(request.getParameter("idTiersVG")) || request.getParameter("idTiersVG")=="null") {
				if (!globaz.jade.client.util.JadeStringUtil.isBlank(request.getParameter("likeNumNomPrint"))) {
					likeNumNomPrint = (String) request.getParameter("likeNumNomPrint");
				} else if ((globaz.jade.client.util.JadeStringUtil.isBlank(compteAnnexe.getFromNumNom())) && (session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX) != null)) {
					likeNumNomPrint = (String) session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX);
				} else {
					likeNumNomPrint = compteAnnexe.getFromNumNom();
				}
           } else {
          	 idTiersVG = request.getParameter("idTiersVG");
           }
			%>

          <% if(!globaz.jade.client.util.JadeStringUtil.isBlank(likeNumNomPrint) || !globaz.jade.client.util.JadeStringUtil.isBlank(idTiersVG)) { %>
            	<script>bFind = true;</script>
            <% } %>
            <input type="text" name="likeNumNom" value="<%=likeNumNomPrint%>" class="libelleStandard" tabindex="1" onkeyup="afficheMsg()" id="numNomSearch">
            </TD>
            <TD width="300" align="left" ><div id="msgPonctuation" style='display:none'><font color='#000000'><strong>Eingabe der Satzzeichen obligatorisch</strong></font></div></TD>
            <TD nowrap width="179">&nbsp; </TD>
            <TD nowrap colspan="2">Auswahl der Konti

              <select name="forSelectionCompte" class="libelleCourt" tabindex="3">
			<% String sForSelectionCompte = request.getParameter("forSelectionCompte");
			   if (sForSelectionCompte == null)
				    sForSelectionCompte ="1000";
			%>
                <option <%=(sForSelectionCompte.equals("1000")) ? "selected" : "" %> value="1000">alle</option>
                <option <%=(sForSelectionCompte.equals("1")) ? "selected" : "" %> value="1">offene</option>
                <option <%=(sForSelectionCompte.equals("2")) ? "selected" : "" %> value="2">saldierte</option>
              </select>
            </TD>
          </TR>
          <TR>
            <TD nowrap width="128">Rolle</TD>
            <TD nowrap>
              <select name="forSelectionRole" tabindex="2">
              	<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
              </select>
            </TD>
            <TD nowrap>&nbsp;</TD>
            <TD nowrap width="179">&nbsp;</TD>
            <TD nowrap colspan="2" align="right">
              <INPUT type='hidden' name='forSelectionTri' value="1">

	      <%
              		String selectBlock = globaz.osiris.parser.CASelectBlockParser.getForIdGenreSelectBlock(objSession);

              		if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) {
              			out.print("Art&nbsp;");
              			out.print(selectBlock);
              		}
              %>

            </TD>
          </TR>
 		  <TR>
		  	<%
			  if (!globaz.jade.client.util.JadeStringUtil.isBlank(idTiersVG)) {
	    	  %>
	   		  	<TD>IdTiers</TD>
		  		<TD><input type="text" id="idTiersVGSearch" name="forIdTiers" value="<%=request.getParameter("idTiersVG")%>"/></TD>
			<%
			  } else {
			  %>
				  <TD>&nbsp;</TD>
				  <TD>&nbsp;</TD>
			  <%
		  		}
				String selectCategorieSelect = globaz.osiris.parser.CASelectBlockParser.getForIdCategorieSelectBlock(objSession, true);
		  		out.print("<TD colspan=\"4\" nowrap align=\"right\">&nbsp;");
				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) {
					out.print("Kategorie&nbsp;");
					out.print(selectCategorieSelect);
					out.print("</TD></TR>");
				} 
		%>
		</TR>
          <TR>
            <TD></TD>
            <TD colspan="2"></TD>
            <TD></TD>
            <TD></TD>
            <TD></TD>
          </TR>
          <TR>
            <TD></TD>
            <TD colspan="2"></TD>
            <TD></TD>
            <TD></TD>
            <TD></TD>
          </TR>
          <TR>
            <TD nowrap width="128"></TD>
            <TD nowrap colspan="2"></TD>
          </TR>
	   <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--            <TD bgcolor="#FFFFFF" colspan="2" align="right">
            <A href="javascript:document.forms[0].submit();">
                <IMG name="btnFind" src="/images/btnFind.gif" border="0">
            </A>
            </TD> -->

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>