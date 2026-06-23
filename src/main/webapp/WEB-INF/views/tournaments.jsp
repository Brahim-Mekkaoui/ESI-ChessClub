<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Tournois</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">

    <div class="page-header">
        <h1>🏆 Tournois</h1>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/tournament/create" class="btn btn-primary">+ Nouveau tournoi</a>
        </c:if>
    </div>

    <!-- Filter Tabs -->
    <div class="filter-tabs">
        <a href="${pageContext.request.contextPath}/tournaments" class="tab ${empty param.status ? 'active' : ''}">Tous</a>
        <a href="${pageContext.request.contextPath}/tournaments?status=PLANNED" class="tab ${param.status == 'PLANNED' ? 'active' : ''}">📅 Planifiés</a>
        <a href="${pageContext.request.contextPath}/tournaments?status=ONGOING" class="tab ${param.status == 'ONGOING' ? 'active' : ''}">⚡ En cours</a>
        <a href="${pageContext.request.contextPath}/tournaments?status=FINISHED" class="tab ${param.status == 'FINISHED' ? 'active' : ''}">✅ Terminés</a>
    </div>

    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>

    <c:choose>
        <c:when test="${empty tournaments}">
            <div class="empty-state card">
                <div class="empty-icon">♟</div>
                <h3>Aucun tournoi trouvé</h3>
                <p class="text-muted">Il n'y a pas encore de tournoi dans cette catégorie.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="tournaments-grid">
                <c:forEach var="t" items="${tournaments}">
                    <div class="tournament-card card">
                        <div class="tournament-header">
                            <span class="tournament-status status-${fn:toLowerCase(t.status)}">${t.status}</span>
                            <span class="tournament-format">${t.format}</span>
                        </div>
                        <h3 class="tournament-name">${fn:escapeXml(t.name)}</h3>
                        <c:if test="${not empty t.description}">
                            <p class="tournament-desc">
                                <c:choose>
                                    <c:when test="${fn:length(t.description) > 100}">
                                        ${fn:escapeXml(fn:substring(t.description, 0, 100))}…
                                    </c:when>
                                    <c:otherwise>${fn:escapeXml(t.description)}</c:otherwise>
                                </c:choose>
                            </p>
                        </c:if>
                        <div class="tournament-meta">
                            <c:if test="${not empty t.location}">
                                <span>📍 ${fn:escapeXml(t.location)}</span>
                            </c:if>
                            <c:if test="${not empty t.startDate}">
                                <span>📅 ${t.formattedStartDate}</span>
                            </c:if>
                            <span>👥 ${t.participantCount} / ${t.maxPlayers} joueurs</span>
                        </div>
                        <div class="tournament-actions">
                            <a href="${pageContext.request.contextPath}/tournaments?action=detail&id=${t.id}" class="btn btn-secondary btn-sm">Voir détails</a>
                            <c:if test="${not empty sessionScope.user and t.status == 'PLANNED'}">
                                <form method="post" action="${pageContext.request.contextPath}/tournaments" style="display:inline;">
                                    <input type="hidden" name="action" value="join">
                                    <input type="hidden" name="tournamentId" value="${t.id}">
                                    <button type="submit" class="btn btn-primary btn-sm">S'inscrire</button>
                                </form>
                            </c:if>
                            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                <a href="${pageContext.request.contextPath}/admin/tournament/edit?id=${t.id}" class="btn btn-warning btn-sm">Gérer</a>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</div>
<%@ include file="footer.jsp" %>
