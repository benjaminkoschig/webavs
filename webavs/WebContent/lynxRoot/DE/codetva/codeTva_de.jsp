<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GLX0011"; %>
<%@ page import="globaz.lynx.db.codetva.*" %>
<%
LXCodeTvaViewBean viewBean = (LXCodeTvaViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdCodeTVA();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript">

function add() {
    document.forms[0].elements('userAction').value="lynx.codetva.codeTva.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="lynx.codetva.codeTva.modifier";
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="lynx.codetva.codeTva.ajouter";
    else
        document.forms[0].elements('userAction').value="lynx.codetva.codeTva.modifier";

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.codetva.codeTva.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, den ausgewählter MWST Code zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="lynx.codetva.codeTva.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

top.document.title = "Detail eines MWST Code - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines MWST Code<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<tr>
		    <td>Code</td>
		    <td colspan="2">
			<%
				String selectCsCodeTVASelect = globaz.lynx.parser.LXSelectBlockParser.getCsCodeTVASelectBlock(objSession, "csCodeTVA", viewBean.getCsCodeTVA(), false, false);

				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCsCodeTVASelect)) {
					out.print(selectCsCodeTVASelect);
				}
			%>
			</td>
        </TR>                        
         <TR>
            <TD>Satz</TD>
            <TD colspan="2">
				 <input type="text" name="taux" style="width:1.5cm" size="10" maxlength="6" value="<%=viewBean.getTaux()%>" class="libelle" tabindex="1" />
            </TD>
        </TR>           
      
		<tr>
        	<td>Beginndatum</td>
            <td colspan="2">
            	<ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" value="<%=viewBean.getDateDebut()%>" tabindex="1"/>
			</td>
		</tr>         
		<tr>
        	<td>Enddatum</td>
            <td colspan="2">
				<ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value="<%=viewBean.getDateFin()%>" tabindex="1"/>
 			</td>
		</tr>   
        
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>