<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3005"; %>
<%
globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean viewBean = (globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris?useraction=osiris.contentieux.annulerEtapeContentieux.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="Javascript"> 
function add() {}
function upd() {}
function validate() {
	document.forms[0].elements('userAction').value="osiris.contentieux.annulerEtapeContentieux.executer"
	document.forms[0].submit();
}
function cancel() {
	document.forms[0].elements('userAction').value="back";
}
function del() {}
function init(){}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Annulation d'une étape de contentieux<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			          <TR>
			          	<TD width=80>Compte</TD>
			          	<TD><TEXTAREA cols="40" rows="4" class="libelleLongDisabled" readonly><%=viewBean.getSection().getCompteAnnexe().getTitulaireEntete()%></TEXTAREA></TD>
			          </TR>
			          <TR>
			          	<TD width="80" valign="top">Section</TD>
			            <TD> 
              				<input type="text" value="<%=viewBean.getSection().getIdExterne()%> - <%=viewBean.getSection().getDescription()%>" readonly size="60" maxlength="49" class="numeroDisabled">
            			</TD>
			          </TR>
			          <TR>
			          	<TD nowrap></TD>
			          </TR>
			          <TR> 
			            <td nowrap align="left" colspan="2">Voulez vous vraiment supprimer l'&eacute;tape s&eacute;lectionn&eacute;e ?</td>
			          </TR>
			          <TR><TD colspan="2">&nbsp;</TD></TR>
			          <TR> 
			            <TD width=50>Etape</TD>
			            <TD> 
			              <input type="text" name="etape" value="<%=viewBean.getParametreEtape().getEtape().getDescription()%>"  readonly size="50" maxlength="60" class="numeroDisabled" >
			            </TD>
			          </TR>
			          <TR> 
			            <TD width=50>Montant</TD>
			            <TD> 
			              <input type="text" name="montant" value="<%=viewBean.getMontant()%>" class="montantDisabled" readonly >
			            </TD>
			          </TR>
					  <TR> 
			            <TD width=50>Motif</TD>
			            <TD> 
			              <textarea name="motif" cols="40" rows="2" class="input" tabindex="4"><%=viewBean.getMotif()%></textarea>
			            </TD>
			          </TR>
			          <!--<TR align="right" valign="top"> 
			            <TD nowrap height="2" colspan="2"> 
			              <input type="button" id="ok" name="Oui" value="Ok" onClick="">
			              <input type="button" id="annuler" name="Annuler" value="Annuler" onClick="jsAnnuler()">
			            </TD>
			          </TR>-->
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>