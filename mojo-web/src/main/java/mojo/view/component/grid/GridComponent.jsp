<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="GridComponent panel panel-default">
	<div class="panel-heading">
		<span>${bean.title} ${bean.path}</span>
		<form:form method="post" modelAttribute="${bean.path}">
			<input type="hidden" name="bean_path" value="${bean.path}" />

			<%-- personGrid.* --%>
			<%-- <form:errors path="*" />--%>

			Field: <form:input path="searchField" />
			Value: <form:input path="searchValue" />
			<button type="submit">Submit</button>
		</form:form>
	</div>
	<table class="table">
		<thead>
			<tr>
				<c:forEach var="column" items="${bean.columns}">
					<th style="width: ${column.width}">${column.label}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="record" items="${bean.records}" varStatus="rowStatus">
				<tr>
					<c:forEach var="column" items="${bean.columns}" varStatus="colStatus">
						<td>
							<span>${record.values[colStatus.index]}</span>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
