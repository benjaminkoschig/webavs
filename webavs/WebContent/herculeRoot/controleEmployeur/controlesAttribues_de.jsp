<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCE2002";

	//Récupération des beans
	globaz.hercule.db.controleEmployeur.CEControlesAttribuesViewBean viewBean = (globaz.hercule.db.controleEmployeur.CEControlesAttribuesViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "hercule.controleEmployeur.controlesAttribues.executer";

	// Récupération des réviseurs à lister
	globaz.hercule.db.reviseur.CEReviseurManager reviseurs = viewBean._getReviseursList();
	
	if (!viewBean.getTousLesControles().booleanValue() && !viewBean.getAEffectuer().booleanValue() && !viewBean.getDejaEffectuer().booleanValue()) {
		viewBean.setAEffectuer(Boolean.TRUE);
	}
%>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
</SCRIPT>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
	function init() {
	}

	function changeType() {
  		document.forms[0].elements("userAction").value = "hercule.controleEmployeur.controlesAttribues.afficher";
		document.forms[0].submit();
	}
	
	function checkTousLesControles() {
		$(":checkbox").attr('checked', false);	
		$("#tousLesControles").attr('checked', true);		
	}
	
	function checkDejaEffectuer() {
		$(":checkbox").attr('checked', false);
		$("#dejaEffectuer").attr('checked', true);	
	}
	
	function checkAEffectuer() {
		$(":checkbox").attr('checked', false);
		$("#aEffectuer").attr('checked', true);	
	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_IMPRESSION_CONT_ATTRIBUES"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR id="afficheType">
       	<TD width="23%" height="2"><ct:FWLabel key="TYPE_ADRESSE_DEFAUT"/></TD>
       	<TD height="2"> 
        	<SELECT id="tri" name="typeAdresse" doClientValidation="">
 				<OPTION value="domicile" <%="domicile".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="DOMICILE"/></OPTION>
 				<OPTION value="courrier" <%="courrier".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="COURRIER"/></OPTION>
 			</SELECT>
 		</TD>
    </TR>
	<TR>
		<TD><ct:FWLabel key="REVISEUR"/></TD>
		<TD>
			<SELECT name="visaReviseur">
		   		<OPTION value="tous"></OPTION>
		   		<%
		   			for (java.util.Iterator iter = reviseurs.iterator(); iter.hasNext();) {
						globaz.hercule.db.reviseur.CEReviseur reviseur = (globaz.hercule.db.reviseur.CEReviseur) iter.next();
				%>
				<OPTION value="<%=reviseur.getVisa()%>"><%=reviseur.getVisa()%></OPTION>
				<%}%>
			</SELECT>   							
		</TD>
	</TR>
	<TR>
		<TD width="23%" height="2"><ct:FWLabel key="GENRE_CONTROLE"/></TD>
       	<TD height="2"><ct:FWCodeSelectTag name="genreControle" wantBlank="true" defaut="<%=viewBean.getGenreControle()%>" codeType="VEGENRECON"/></TD>
	</TR>
	<TR>
		<TD></TD>
		<TD></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="ANNEE_CONTROLE"/></TD>
		<TD><INPUT type="text" onkeypress="return filterCharForPositivInteger(window.event);" name="annee" maxlength="4" size="4" value="<%=viewBean.getAnnee()%>"></TD>
	</TR>
	<TR>
       	<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
       	<TD height="2"><INPUT type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEMailAddress()%>"></TD>
     </TR>
     <TR> 
       	<TD height="20" width="150"><ct:FWLabel key="IMPRIMER_CONT_TOUS"/></TD>
       	<TD nowrap height="31" width="259"><input type="checkbox" id="tousLesControles" onclick="checkTousLesControles();" name="tousLesControles" <%=(viewBean.getTousLesControles().booleanValue())? "checked" : ""%>></TD>
     </TR>
     <TR> 
       	<TD height="20" width="150"><ct:FWLabel key="IMPRIMER_CONT_EFFECTUER"/></TD>
       	<TD nowrap height="31" width="259"><input type="checkbox" id="aEffectuer" onclick="checkAEffectuer();" name="aEffectuer" <%=(viewBean.getAEffectuer().booleanValue())? "checked" : ""%>></TD>
     </TR>
     <TR> 
       	<TD height="20" width="150"><ct:FWLabel key="IMPRIMER_CONT_DEJA_EFFECTUES"/></TD>
       	<TD nowrap height="31" width="259"><input type="checkbox" id="dejaEffectuer" onclick="checkDejaEffectuer();" name="dejaEffectuer" <%=(viewBean.getDejaEffectuer().booleanValue())? "checked" : ""%>></TD>
     </TR>
          				
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>