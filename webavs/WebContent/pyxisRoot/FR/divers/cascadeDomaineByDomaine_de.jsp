<%-- tpl:insert page="/theme/detail.jtpl" --%>
	<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/detail/header.jspf" %>
	<%-- tpl:put name="zoneInit"  --%>
		<%
			globaz.pyxis.db.divers.TICascadeDomaineByDomaineViewBean viewBean = (globaz.pyxis.db.divers.TICascadeDomaineByDomaineViewBean)session.getAttribute ("viewBean");
			idEcran ="GTI6012";
		%>
	<%-- /tpl:put --%>
	<%-- tpl:put name="zoneBusiness"  --%>
	<%-- /tpl:put --%>
	<%@ include file="/theme/detail/javascripts.jspf" %>
	<%-- tpl:put name="zoneScripts"  --%>
		<SCRIPT language="JavaScript">		
			function validate() {
				state = validateFields(); 
				if (document.forms[0].elements('_method').value == "add")
					document.forms[0].elements('userAction').value="pyxis.divers.cascadeDomaineByDomaine.ajouter";
				else
					document.forms[0].elements('userAction').value="pyxis.divers.cascadeDomaineByDomaine.modifier";
				return (state);
			}

			function cancel() {
 				if (document.forms[0].elements('_method').value == "add")
  					document.forms[0].elements('userAction').value="back";
 				else
  					document.forms[0].elements('userAction').value="pyxis.divers.cascadeDomaineByDomaine.afficher";
			}

			function del() {
				var msgDelete = '<%=globaz.pyxis.util.TIUtil.encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
			    if (window.confirm(msgDelete)){
					document.forms[0].elements('userAction').value="pyxis.divers.cascadeDomaineByDomaine.supprimer";
					document.forms[0].submit();
				}
			}

			function init() {
			}

		</SCRIPT> 
	<%-- /tpl:put --%>
	<%@ include file="/theme/detail/bodyStart.jspf" %>
	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key='CASCADE_DOMAINE_DETAIL' />
	<%-- /tpl:put  --%>
	<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain"  --%>
    	<TR>
        	<TD colspan="5">&nbsp;</TD>
        </TR>
        <TR>
            <TD><ct:FWLabel key='DOMAINE_CLEF'/></TD>
            <TD colspan="4">
            	<ct:FWCodeSelectTag name="csDomaineClef"
					defaut="<%=viewBean.getCsDomaineClef()%>" 
					codeType="PYAPPLICAT"/>
			</TD>
        </TR>
	   	<TR>
           <TD colspan="5">&nbsp;</TD>
        </TR>
        <TR>
            <TD><ct:FWLabel key='POSITION'/></TD>
            <TD colspan="4">
            	<input type="text" class="numeroCourt" name="position" id="position" value="<%=viewBean.getPosition()%>"/>
            </TD>
        </TR>
	   	<TR>
            <TD><ct:FWLabel key='DOMAINE'/></TD>
            <TD colspan="4">
            	<ct:FWCodeSelectTag name="csDomaine"
					defaut="<%=viewBean.getCsDomaine()%>" 
					codeType="PYAPPLICAT"/>
            </TD>
        </TR>
	<%-- /tpl:put --%>
	<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
	<%@ include file="/theme/detail/bodyErrors.jspf" %>
	<%-- tpl:put name="zoneEndPage"  --%>
	<%-- /tpl:put --%>
	<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>