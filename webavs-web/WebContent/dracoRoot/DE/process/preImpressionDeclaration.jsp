<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/conflict.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/conflict/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.draco.db.declaration.*,globaz.globall.db.*" %>
<%
	globaz.draco.db.declaration.DSDeclarationViewBean viewBean = (globaz.draco.db.declaration.DSDeclarationViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/conflict/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function onOk()
{
	document.forms[0].elements('userAction').value="draco.preimpression.preimpression.executer";
	document.forms[0].submit();

}
function onCancel()
{
	document.forms[0].elements('userAction').value="";
//	document.forms[0].submit();
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT>



 <%-- /tpl:put --%>
<%@ include file="/theme/conflict/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Vordruck der Lohnbescheinigungen<%-- /tpl:put --%>
<%@ include file="/theme/conflict/bodyStart2.jspf" %>
			  <%-- tpl:put name="zoneMain" --%> 
<TR> 
            <TD nowrap height="31" width="131">E-Mail Adresse</TD>
            <TD nowrap colspan="4"> 
              <INPUT name="eMail" size="20" type="text" style="text-align : left;">
            </TD>
                        <TD nowrap height="31" width="1">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap  height="11" width="131">Alle Mitglieder</TD>
            <TD nowrap  height="11" width="117"> 
              <input name="affilierTous" size="20" type="checkbox" style="text-align : right;" checked> oder von der Nummer</TD>
                        <TD nowrap  height="11" width="92"> 
              <input name="affilierDebut" size="10" type="text" style="text-align : left;">
            </TD>
                        <TD nowrap  height="11" width="80">bis Nummer</TD>
                        <TD nowrap  height="11" width="92">
            <INPUT name="affilierFin" size="10" type="text" style="text-align : left;"></TD>
                        <TD nowrap  height="11" width="1">&nbsp;</TD>
                    </TR>
          <TR> 
            <td nowrap width="131">Dokumentdatum</td>
            <td nowrap colspan="2"> 
              <input name="dateSurDocument" size="10" type="text" style="text-align : right;">
            </td>
                        <td nowrap height="31" colspan="2">&nbsp; </td>
                        <TD nowrap height="31">&nbsp;</TD>
                    </TR>
          <TR> 
            <td nowrap width="131">Rückkehrdatum</td>
            <td nowrap colspan="2"> 
              <input name="dateRetour" size="10" type="text" style="text-align : right;">
            </td>
                        <td nowrap height="31" colspan="2">&nbsp; </td>
                        <TD nowrap height="31">&nbsp;</TD>
                    </TR>
          <TR> 
            <td nowrap width="131">Briefe ausdrucken</td>
            <td nowrap colspan="2"> 
              <input name="imprimerLettre" size="20" type="checkbox" value="" style="text-align : right;">
            </td>
                        <td nowrap height="31" colspan="2">&nbsp; </td>
                        <TD nowrap height="31">&nbsp;</TD>
                    </TR>
          <TR> 
            <td nowrap width="131">Lohnbescheinigungen ausdrucken</td>
            <td nowrap colspan="2"> 
              <input name="imprimerDeclaration" size="20" type="checkbox" style="text-align : right;">
            </td>
                        <td nowrap height="31" colspan="2">&nbsp; </td>
                        <TD nowrap height="31">&nbsp;</TD>
                    </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/conflict/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
 <%-- /tpl:put --%>
<%@ include file="/theme/conflict/bodyClose.jspf" %>
<%-- /tpl:insert --%>
