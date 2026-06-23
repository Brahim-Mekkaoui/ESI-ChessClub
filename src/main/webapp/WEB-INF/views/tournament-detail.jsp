<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — ${fn:escapeXml(tournament.name)}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">

    <a href="${pageContext.request.contextPath}/tournaments" class="back-link">← Retour aux tournois</a>

    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>

    <!-- Tournament Info -->
    <div class="card tournament-detail-card">
        <div class="tournament-detail-header">
            <div>
                <span class="tournament-status status-${fn:toLowerCase(tournament.status)}">${tournament.status}</span>
                <span class="tournament-format" style="margin-left:8px;">${tournament.format}</span>
                <h1 style="margin-top:12px;">${fn:escapeXml(tournament.name)}</h1>
                <c:if test="${not empty tournament.description}">
                    <p style="margin-top:8px;color:var(--text-secondary);">${fn:escapeXml(tournament.description)}</p>
                </c:if>
            </div>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/tournament/edit?id=${tournament.id}" class="btn btn-warning">Gérer le tournoi</a>
            </c:if>
        </div>
        <div class="tournament-info-grid">
            <c:if test="${not empty tournament.location}">
                <div class="info-item">📍 <strong>Lieu :</strong> ${fn:escapeXml(tournament.location)}</div>
            </c:if>
            <c:if test="${not empty tournament.startDate}">
                <div class="info-item">📅 <strong>Début :</strong> ${tournament.formattedStartDate}</div>
            </c:if>
            <c:if test="${not empty tournament.endDate}">
                <div class="info-item">🏁 <strong>Fin :</strong> ${tournament.formattedEndDate}</div>
            </c:if>
            <div class="info-item">👥 <strong>Joueurs :</strong> ${tournament.participantCount} / ${tournament.maxPlayers}</div>
        </div>

        <c:if test="${not empty sessionScope.user and tournament.status == 'PLANNED'}">
            <form method="post" action="${pageContext.request.contextPath}/tournaments" style="margin-top:16px;">
                <input type="hidden" name="action" value="join">
                <input type="hidden" name="tournamentId" value="${tournament.id}">
                <button type="submit" class="btn btn-primary">✅ S'inscrire à ce tournoi</button>
            </form>
        </c:if>
    </div>

    <!-- Participants -->
    <c:if test="${not empty participants}">
    <div class="card" style="margin-top:20px;">
        <h2 class="section-title">👥 Participants (${fn:length(participants)})</h2>
        <div class="participants-grid">
            <c:forEach var="p" items="${participants}">
                <div class="participant-chip">
                    <a href="${pageContext.request.contextPath}/user?id=${p.id}" class="link">
                        ${fn:escapeXml(p.username)}
                    </a>
                    <span class="elo-chip">${p.eloRating}</span>
                </div>
            </c:forEach>
        </div>
    </div>
    </c:if>

    <!-- Matches -->
    <c:if test="${not empty matches}">
    <div class="card" style="margin-top:20px;">
        <h2 class="section-title">♟ Parties</h2>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Ronde</th>
                    <th>Blancs</th>
                    <th>Noirs</th>
                    <th>Résultat</th>
                    <c:if test="${sessionScope.user.role == 'ADMIN' and tournament.status == 'ONGOING'}">
                        <th>Action</th>
                    </c:if>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="m" items="${matches}">
                    <tr>
                        <td>Ronde ${m.roundNumber}</td>
                        <td>${m.whiteName}</td>
                        <td>${m.blackName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${m.result == 'NOT_PLAYED'}">
                                    <span class="result-badge result-not-played">À jouer</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="result-badge result-${fn:toLowerCase(fn:replace(m.result,'_','-'))}">
                                        <c:choose>
                                            <c:when test="${m.result == 'WHITE_WIN'}">Blancs gagnent</c:when>
                                            <c:when test="${m.result == 'BLACK_WIN'}">Noirs gagnent</c:when>
                                            <c:when test="${m.result == 'DRAW'}">Nulle</c:when>
                                            <c:otherwise>${m.result}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <c:if test="${sessionScope.user.role == 'ADMIN' and tournament.status == 'ONGOING'}">
                            <td>
                                <c:if test="${m.result == 'NOT_PLAYED'}">
                                    <form method="post" action="${pageContext.request.contextPath}/match" style="display:flex;gap:4px;">
                                        <input type="hidden" name="matchId" value="${m.id}">
                                        <input type="hidden" name="tournamentId" value="${tournament.id}">
                                        <select name="result" class="form-control form-control-sm">
                                            <option value="WHITE_WIN">Blancs</option>
                                            <option value="BLACK_WIN">Noirs</option>
                                            <option value="DRAW">Nulle</option>
                                        </select>
                                        <button type="submit" class="btn btn-primary btn-sm">OK</button>
                                    </form>
                                </c:if>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    </c:if>

</div>
<%@ include file="footer.jsp" %>
