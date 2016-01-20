<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://displaytag.sf.net" prefix="tagdisplay"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>

<html>
	<head>


		<title>${view.name}</title>
		<meta http-equiv="Content-Type"
			content="text/html; charset=ISO-8859-1">
		<title><fmt:message key="menu.title" /></title>
		<link rel="stylesheet" type="text/css" media="all"
			href='${pageContext.request.contextPath}/js/jscalendar/skins/aqua/theme.css'
			title="Aqua" />
		<script src="${pageContext.request.contextPath}/js/prototype.js"
			type="text/javascript"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/validation.js"> </script>
		<script type="text/javascript"
			src='${pageContext.request.contextPath}/js/jscalendar/calendar.js'></script>
		<!-- language for the calendar -->
		<script type="text/javascript"
			src='${pageContext.request.contextPath}/js/jscalendar/lang/calendar-es.js'></script>
		<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
		<script type="text/javascript"
			src='${pageContext.request.contextPath}/js/jscalendar/calendar-setup.js'></script>
		<script type="text/javascript"
			src='${pageContext.request.contextPath}/js/combo.js'></script>
		<link type="text/css" rel="stylesheet"
			href="${pageContext.request.contextPath}/css/screen.css"></link>
		<script type="text/javascript">
function send(id) {
	document.forms['form2'].elements['entityId'].value = id;
	document.forms['form2'].submit();
}


</script>
	</head>
	<body onload="document.body.style.cursor='default'";>
		<form action="update.html" method="post" name="form1" id="form1">
			<div style="visibility:hidden;">
				<input name="id" value="${id}">
				<input name="entity" value="${entity}">
			</div>
			<div align="left">
				<h3>
					<c:choose>
						<c:when test="${not empty id}">Actualizar</c:when>
						<c:otherwise>Nuevo</c:otherwise>
					</c:choose>
					${entity}
				</h3>
			</div>

			<table align="left" width="90%">
				<tbody>
					<tr>
						<td valign="top" align="left">
							<table>
								<c:forEach items="${properties}" var="property"
									varStatus="propertyCount" begin="0">
									<c:set var="rowstyle" scope="page"
										value=" validate-${propTypes[property]}" /><!--  required -->
									<c:set var="trStyle" scope="page" value="odd" />
									<c:if test="${propertyCount.index mod 2 eq 0}">
										<c:set var="trStyle" scope="page" value="even" />
									</c:if>
									<tr class="${trStyle}">
										<td>
											${property}
										</td>
										<td>
											<input class="${rowstyle}" name="${property}"
												value="${values[property]}" id="${property}">
											<c:if test="${propTypes[property] == 'date'}">
												<img src='${pageContext.request.contextPath}/images/cal.gif'
													id="cal${property}" width="16" height="16" border="0"
													alt='<fmt:message key="index.selectDate"></fmt:message>'>
												<script type="text/javascript">
	Calendar.setup( {
		inputField : "${property}", // id of the input field
		ifFormat : "%d-%m-%Y", // format of the input field
		button : "cal${property}", // trigger for the calendar (button ID)
		align : "B1", // alignment (defaults to "Bl")
		singleClick : true
	});
</script>
											</c:if>
									
									</tr>
								</c:forEach>
								<tr>
									<td colspan="2">
										<input type="submit" value="Guardar">
									</td>
								</tr>
							</table>
					</tr>


				</tbody>
			</table>
		</form>
		<script type="text/javascript">
						var valid2 = new Validation('form1', {useTitles:true});
						htmlData("Estado","select distinct Naturaleza as id,Naturaleza from Tbl_Naturalezas order by 1");
					</script>
	</body>

</html>
