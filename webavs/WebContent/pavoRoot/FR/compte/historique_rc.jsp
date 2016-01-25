
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.globall.util.*,globaz.pavo.db.compte.*" %>
<%
    CICompteIndividuelViewBean viewBean = (CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
    IFrameHeight = "290";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
<% if(!JAUtil.isIntegerEmpty(viewBean.getCompteIndividuelId())) { %>
usrAction = "pavo.compte.historique.lister";
<% } %>
top.document.title = "CI - Gestion des inscriptions";

<% if(!viewBean.hasUserShowRight(null)) {
	bButtonFind = false;
	bButtonNew = false; %>
<% } %>
timeWaiting = 1;


function updateCompteIndividuel(data) {
	arrayOfStrings = data.split("#");
	var compteId = "";
	var numAvs = "";
	var name = "";
	var dateNaissance = "";

	if (arrayOfStrings.length>=1) {
		compteId = arrayOfStrings[0];
	}
	if (arrayOfStrings.length>=2) {
		numAvs = arrayOfStrings[1];
	}
	if (arrayOfStrings.length>=3) {
		name = arrayOfStrings[2];
	}

	if (arrayOfStrings.length>=4) {
		dateNaissance = arrayOfStrings[3];
	}

	document.forms[0].elements('forCompteIndividuelId').value=compteId;
	document.forms[0].elements('numeroAvsInv').value=numAvs;
	document.forms[0].elements('nomInv').value=name;
	document.forms[0].elements('naissance').value=dateNaissance;

	document.all('btnFind').click();
}

function detailAssure() {
	this.location.href='<%=servletContext + mainServletPath%>?userAction=pavo.compte.compteIndividuel.afficher&selectedId='+document.forms[0].elements('forCompteIndividuelId').value+'&mainSelectedId='+<%=viewBean.getCompteIndividuelId()%>;
}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des inscriptions<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>


		<TR>
	            <TD nowrap colspan="2">
	              <INPUT type="text" name="numeroAvsInv" size="15" class="disabled" readonly value="<%=JAStringFormatter.formatAVS(viewBean.getNumeroAvs())%>">

	              <INPUT type="text" name="nomInv" size="55" class="disabled" readonly value="<%=viewBean.getNomPrenom()%>">

	              <INPUT type="hidden" name="forCompteIndividuelId" value="<%=viewBean.getCompteIndividuelId()%>">


	              <!--INPUT type="hidden" name="forIdTypeCompteCompta" value="true"-->

	            </TD>
	            <TD nowrap>Date de naissance</TD>
	            <TD nowrap>
	              <INPUT type="text" name="naissance" class="disabled" size="11" readonly value="<%=viewBean.getDateNaissance()%>">
	            </TD>

		</TR>
		<tr><td colspan="4"><hr></td></tr>
		<TR>

            <TD width="15"><a href="javascript:detailAssure();">Assuré</a></TD>
            <TD nowrap>
            <%
 		  		Object[] ciLies = viewBean.getCILies();
 		  		if (ciLies.length>0) {%>
	              <select name="numAvsList" onchange="updateCompteIndividuel(this.options[this.selectedIndex].value);">
					<option SELECTED value="<%=viewBean.getCompteIndividuelId()+"#"+JAStringFormatter.formatAVS(viewBean.getNumeroAvs())+"#"+viewBean.getNomPrenom()+"#"+viewBean.getDateNaissance()%>"> <%=JAStringFormatter.formatAVS(viewBean.getNumeroAvs()) + " " + viewBean.getNomPrenom()%></option>
					<%
	 				for(int i=0;i<ciLies.length;i++) {
	 					String [] link = (String[])ciLies[i];
	 					if(!"".equals(link[0])) { %>
							<option value="<%=link[0]+"#"+link[1]+"#"+link[2]+"#"+link[3]%>"> <%=link[1] + " " + link[2]%> </option>
                     <% }
	                  }
	                %>
	              </select>
 		  	<%}%>

 		  	</TD>
          </TR>
          <TR><TD><br></TD></TR>

          <!--
					<TR>
						  <TD nowrap width="100">Nom et prénom</TD>
                          <TD nowrap width="400">
                                <INPUT type="text" name="nom" class="disabled" size="40" readonly value="<%=viewBean.getNomPrenom()%>">
                          </TD>
					</TR>
					-->
          <TR>
            <TD nowrap width="80">Tri</TD>
            <td nowrap>
              <select name="tri">
                <option value="annee">Ann&eacute;e</option>
                <option value="date">Date d'inscription</option>
                <option value="affilie">Affilié ou partenaire</option>

              </select>
            </td>
			<TD nowrap width="50">Etat</TD>
			<%
				java.util.HashSet typeCompte = new java.util.HashSet();
    			typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_GENRE_6);
				typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_GENRE_7);
				typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE);
				typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE_SUSPENS);
			%>
			<TD nowrap colspan="2"><ct:FWCodeSelectTag name="forIdTypeCompte" defaut="<%=viewBean.getDefaultIdTypeCompte()%>" codeType="CITYPCOM" except="<%=typeCompte%>" wantBlank="false"/>
			<% if(viewBean.hasSuspens(null)){ %>
			<img src="<%=request.getContextPath()%>/images/avertissement.gif" border="0">
			<% } %>
			</TD>
          </TR>
          <TR>
            <TD nowrap width="80">A partir de</TD>
            <TD nowrap width="500">
              <input type="text" name="apartir" size="35">
            </TD>
            <TD nowrap width="50">Clôtures</TD>
            <TD nowrap colspan="2"><ct:FWListSelectTag name="etatEcritures" defaut="toutesEcritures" data="<%=viewBean.getListClotures()%>"/></TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

      <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>