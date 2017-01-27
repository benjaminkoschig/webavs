<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@page import="globaz.naos.db.affiliation.AFAttestationAffiliationViewBean"%>
<%
AFAttestationAffiliationViewBean viewBean = (AFAttestationAffiliationViewBean) session.getAttribute("viewBean");
idEcran = "CAF3007";
userActionValue = "naos.affiliation.attestationAffiliation.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>

<script type="text/javascript">
$(function() {		
	var activation = {
			$activite: null,
			$bnOk: null,
			
			init: function (){
				this.$btnOk = $('#btnOk');
				this.$activite = $('#activite');
				this.disableOrenable();
				this.addEvent();
			},
			
			addEvent: function () {
				var that = this;
				this.$activite.keyup(function() {
					that.disableOrenable();
					that.checkLenght();
				});
			},
			
			disableOrenable: function () {
				this.$btnOk.prop('disabled', !this.$activite.val().length);				
			},
			
			checkLenght: function(){
				var maxlength = 254;
				if (this.$activite.val().length > maxlength) {
					this.$activite.val(this.$activite.val().substring(0, maxlength)) ;
				}
			}
	}
	
	activation.init();
});
</script>

<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Anschlussbestätigung<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
						<%@page import="globaz.naos.translation.CodeSystem"%>
<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>"/>
						<% } %>
						<TR>
							<TD>E-Mail</TD>
							<TD><INPUT id="email" type="text" name="email" value="<%=viewBean.getEmail()%> data-g-string="mandatory:true"></TD>
						</TR>
						<TR>
							<TD width="150">Tätigkeit(en) :</TD>
							<TD><textarea style="width: 470px" id="activite" type="text" name="activite" rows="4" cols="40" data-g-string="mandatory:true, sizeMax:254"><%=viewBean.getActivite()%></textarea></TD>					
						</TR>						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>