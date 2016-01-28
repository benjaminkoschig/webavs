<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="globaz.aquila.vb.process.COProcessContentieuxViewBean"%>
<%@page import="globaz.aquila.db.access.batch.COSequence"%>

<%
COProcessContentieuxViewBean viewBean = (COProcessContentieuxViewBean) session.getAttribute("viewBean");
COSequence sequence = (COSequence) request.getAttribute("globaz.aquila.vb.process.COProcessContentieuxViewBean.SEQUENCE");
List etapesOptions = viewBean.getEtapesOptions(sequence.getIdSequence());
String idCreer = viewBean.getIdEtapeCreer(sequence.getIdSequence());

if (etapesOptions != null) {
%>
<h5><%=sequence.getLibSequenceLibelle()%></h5>
<a href="#" onclick="deselectionner(document.forms[0].elements('etapes_<%=sequence.getIdSequence()%>'))">déselectionner tout</a>
<br />
<select name="etapes_<%=sequence.getIdSequence()%>" size="<%=etapesOptions.size()%>" onchange="select(this, '<%=sequence.getIdSequence()%>', '<%=idCreer%>')" multiple>
<%
	for (Iterator optionsIter = etapesOptions.iterator(); optionsIter.hasNext();) {
		COProcessContentieuxViewBean.EtapeOption option = (COProcessContentieuxViewBean.EtapeOption) optionsIter.next();
%>
		<option id="etape_<%=option.getEtape().getIdEtape()%>_<%=sequence.getIdSequence()%>"
			value="<%=option.getEtape().getIdEtape()%>"
			<%=option.isEtapeDependCreer()?" class=\"mark\"":""%>
			<%=viewBean.isSelectedEtape(sequence.getIdSequence(), option.getEtape().getIdEtape())?" selected":""%>>
			<%=option.getEtape().getLibActionLibelle()%>
		</option>
<%
	}
%>
</select>
<%
}
%>
