<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI0032";
	globaz.pyxis.db.adressecourrier.TIPaysViewBean viewBean = (globaz.pyxis.db.adressecourrier.TIPaysViewBean)session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");

%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers - Pays Détail"
function add() {
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.pays.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields(); 
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.pays.ajouter";
    else
	document.forms[0].elements('userAction').value="pyxis.adressecourrier.pays.modifier";
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.pays.afficher"
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="pyxis.adressecourrier.pays.supprimer";
		document.forms[0].submit();
	}
}


function init(){
}



</SCRIPT> 

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Pays - Détail
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

	

          <TR>
            <TD nowrap width="140">Code :&nbsp;</TD>
            <TD nowrap><INPUT  name="idPays" value="<%=viewBean.getIdPays()%>"  maxlength="9" class="numero"></TD>
          </TR>
           <TR>
            <TD nowrap width="140">Code ISO</TD>
            <TD nowrap><INPUT type="text" name="codeIso" class="numero" size="3" maxlength="3" value="<%=viewBean.getCodeIso()%>"> *</TD>
          </TR>
	   <TR>
            <TD nowrap width="140">&nbsp;</TD>
            </TR>
	   <TR>
            <TD nowrap width="140">Code centrale</TD>
            <TD nowrap><INPUT type="text" name="codeCentrale" class="numero" size="3" maxlength="3" value="<%=viewBean.getCodeCentrale()%>">
              
              <input type="hidden" name="_creation" class="numero" size="3" maxlength="3" value="test">
            </TD>
          </TR>
          <TR>
            <TD nowrap width="140">&nbsp;</TD>
          </TR>
	   <TR>
            <TD nowrap width="140">Libellé Fr.</TD>
            <TD nowrap><INPUT type="text" name="libelleFr" class="libelleLong" value="<%=viewBean.getLibelleFr()%>"></TD>
          </TR>
           <TR>
            <TD nowrap width="140">Libellé Al.</TD>
            <TD nowrap><INPUT type="text" name="libelleAl" class="libelleLong" value="<%=viewBean.getLibelleAl()%>"></TD>
          </TR>
	   <TR>
            <TD nowrap width="140">Libellé It.</TD>
            <TD nowrap><INPUT type="text" name="libelleIt" class="libelleLong" value="<%=viewBean.getLibelleIt()%>"></TD>
          </TR>
          <TR>
           <TD nowrap width="140">&nbsp;</TD>
          </TR>
	   <TR>
            <TD nowrap width="140">Monnaie</TD>
            <TD nowrap>
		<ct:FWCodeSelectTag name="idMonnaie"
            		defaut="<%=viewBean.getIdMonnaie()%>"
            		codeType="PYMONNAIE"
                     wantBlank="<%=true%>"
			/>
		 </TD>
          </TR>
	   <TR>
            <TD nowrap width="140">&nbsp;</TD>
          </TR>
           <TR>
            <TD nowrap width="140">Indicatif téléphonique</TD>
            <TD nowrap><INPUT type="text" name="indicatifTel" value="<%=viewBean.getIndicatifTel()%>" size="4" maxlength="4"></TD>
          </TR>
         <TR>
            <TD nowrap width="140">&nbsp;</TD>
         </TR>
   	  <TR>
            <TD nowrap width="140">Code format télephone</TD>
            <TD nowrap><INPUT type="text" name="codeFormatTel" size="2" maxlength="2" value="<%=viewBean.getCodeFormatTel()%>"></TD>
          </TR>
	   <TR>
            <TD nowrap width="125">&nbsp;</TD>
         </TR>
	   <TR>
            <TD nowrap width="140">Code format NPA</TD>
            <TD nowrap>
		<INPUT type="text" name="codeFormatNpa" size="2" maxlength="2" value="<%=viewBean.getCodeFormatNpa()%>">
	     </TD>
          </TR>
        <tr>
        	<td>Inactiver ce pays</td>
        	<td>
        		<input  type="checkbox"  name="actif" <%=("S".equals(viewBean.getActif()))?"CHECKED":""%>> 
        	</td>
        	
        </tr>
          
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%> 

<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>