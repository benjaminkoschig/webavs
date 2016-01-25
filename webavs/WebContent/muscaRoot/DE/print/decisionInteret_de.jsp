<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%idEcran="CFA2005";%>
<%@ page import="globaz.musca.db.facturation.*"%>
<%@ page import="globaz.musca.db.print.*"%>
<%
	//Récupération des beans
	 FADecisionInteretViewBean viewBean = (FADecisionInteretViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "musca.print.decisionInteret.executer";
%>
<SCRIPT language="JavaScript">
top.document.title = "Musca - Impression d'une décision d'intérêt moratoire"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck einer Verzugszinsenverfügung<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<tr>
			<td>Journal-Nr.</td>
			<td> <INPUT name="idPassage" class="numeroCourtDisabled" value="<%=viewBean.getIdPassage()%>" type="text"> * <input type="hidden" name="idPassage" value="<%=viewBean.getIdPassage()%>"></td>
		</tr>
		<tr>
			<td>Abrechnungs-Nr. der Rechnung</td>
			<td> <INPUT name="idEnteteFacture" class="numeroCourtDisabled" value="<%=viewBean.getIdEnteteFacture()%>" type="text"> * <input type="hidden" name="idPassage" value="<%=viewBean.getIdPassage()%>">
			<input type="hidden" name="idInteretMoratoire" value="<%=viewBean.getIdInteretMoratoire()%>"/>
			</td>
		</tr>
          <TR>
            <TD>E-Mail Adresse</TD>
            <TD><input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()%>'></TD>
          </TR>

          <%
          	globaz.babel.api.ICTDocument[] documents = null;

          	try {
          		documents = viewBean.getICTDocument("");
          	} catch (Exception e) {
          		// ignore exception. print nothing in this case.
          	}

          	if (documents != null) {
          	%>
          	<TR>
            <TD>Dokumenttyp</TD>
            <TD><select name="forIdDocument">
            <%
          		for (int i = 0; i<documents.length; i++) {
          			if (documents[i].getIdDocument().equals(viewBean.getForIdDocument())) {
          %>
						<option selected value="<%=documents[i].getIdDocument()%>"><%=documents[i].getNom()%></option>
          <%
          			} else {
          %>
						<option value="<%=documents[i].getIdDocument()%>"><%=documents[i].getNom()%></option>
          <%
          			}
          		}
          		%>
          		</select>
          		</TD>
          		</TR>
          		<%
          	}
          %>

						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>