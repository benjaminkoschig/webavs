<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
globaz.hermes.db.parametrage.HEAttenteRetourCIViewBean viewBean = (globaz.hermes.db.parametrage.HEAttenteRetourCIViewBean)session.getAttribute("viewBean");

bButtonUpdate = false;
bButtonDelete = false;
idEcran="GAZ0012";
%>

 <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="javascript">
function add() {}
function upd() {}
function validate() {} 
function cancel() {}
function del() {}

function init(){}

function checkKey(input){
	var re = /[^a-zA-Z\-',].*/
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail de la période<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

<%
String messageRetour = null;
for (int i = 0; i < viewBean.getChampsSize() ; i++){
	if(!viewBean.isHidden(i)){	
		if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCodeSysteme(viewBean.getFieldId(i))){
%>
          <tr> 
            <td width="40%">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td>&nbsp;<ct:FWListSelectTag name="<%=viewBean.getFieldId(i)%>" defaut="<%=viewBean.getChampsAsCodeSystemDefaut(viewBean.getFieldId(i))%>" data="<%=viewBean.getChampsAsCodeSystem(viewBean.getFieldId(i))%>"/></td>
          </tr>
          <%
		}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isDateField(viewBean.getFieldId(i))) {			
%>
          <tr> 
            <td width="40%">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td>&nbsp;<ct:FWCalendarTag name='<%=("TOSTR").concat(viewBean.getFieldId(i))%>' 
			value="<%=globaz.hermes.utils.StringUtils.unformatDate(viewBean.getFieldValue(i))%>" 
			doClientValidation="CALENDAR"/></td>
          </tr>
          <%
		}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isNumeroAVS(viewBean.getFieldId(i))) {
%>
          <tr> 
            <td width="40%">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td>&nbsp; 
              <input name="<%=viewBean.getFieldId(i)%>" value="<%=globaz.hermes.utils.StringUtils.formatAvsAtBlank(viewBean.getFieldValue(i))%>" size="<%=viewBean.getFieldLength(i,5)%>" maxlength="<%=viewBean.getFieldLength(i,3)%>">
            </td>
            <%
		}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomSelectField(viewBean.getFieldId(i))){
%>
          <tr> 
            <td width="40%">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td>&nbsp;<ct:FWListSelectTag name="<%=viewBean.getFieldId(i)%>" defaut="<%=viewBean.getFieldValue(i)%>" data="<%=globaz.hermes.db.gestion.HEAnnoncesViewBean.getCustomSelectFieldValues(viewBean.getFieldId(i),languePage)%>"/></td>
          </tr>
          <%
		} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isReadOnlyField(viewBean.getFieldId(i))){
%>
          <tr> 
            <td width="40%">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td>&nbsp; 
              <input type="text" name="<%=viewBean.getFieldId(i)%>" tabindex="-1" size="<%=viewBean.getFieldLength(i,2)%>" maxlength="<%=viewBean.getFieldLength(i)%>" class="disabled" readonly value="<%=viewBean.getFieldValue(i)%>" >
            </td>
          </tr>
          <%
			} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isNameField(viewBean.getFieldId(i))){
%>
          <tr> 
            <td class="detail">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td class="detail">&nbsp; 
              <input name="<%=viewBean.getFieldId(i)%>" value="<%=viewBean.getFieldValue(i)%>" size="<%=viewBean.getFieldLength(i,2)%>" maxlength="<%=viewBean.getFieldLength(i)%>" onKeyUp="checkKey(this)" onKeyDown="checkKey(this)">
            </td>
          </tr>
          <%
			} else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCustomInputField(viewBean.getFieldId(i))){
%>
          <tr> 
            <td class="detail">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td class="detail">&nbsp; 
              <input name="<%=viewBean.getFieldId(i)%>" value="<%=viewBean.getFieldValue(i)%>" size="<%=viewBean.getFieldLength(i)%>" maxlength="<%=viewBean.getFieldLength(i)%>">
            </td>
          </tr>
          <%			
			}else if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isCurrencyField(viewBean.getFieldId(i))){
%>
          <tr> 
            <td class="detail">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td class="detail">&nbsp; 
              <input name="<%=viewBean.getFieldId(i)%>" value="<%=globaz.hermes.utils.CurrencyUtils.formatCurrency(viewBean.getFieldValue(i),true,false,true,0)%>" size="<%=viewBean.getFieldLength(i,2)%>" maxlength="<%=viewBean.getFieldLength(i)%>">
            </td>
          </tr>
          <%			
			}else{
%>
          <tr> 
            <td class="detail">&nbsp;<%=viewBean.getFieldLibelle(i)%>&nbsp;:&nbsp;</td>
            <td class="detail">&nbsp; 
              <input name="<%=viewBean.getFieldId(i)%>" value="<%=viewBean.getFieldValue(i)%>" size="<%=viewBean.getFieldLength(i,2)%>" maxlength="<%=viewBean.getFieldLength(i)%>" <%if(viewBean.isReadOnly(i)) out.println("readonly class=\"disabled\"");%>>
            </td>
          </tr>
          <%	
		}
	}	
}	
%>	  
		  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>