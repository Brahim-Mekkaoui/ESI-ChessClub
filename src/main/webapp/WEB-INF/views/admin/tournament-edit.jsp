<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Gérer le tournoi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>
<div class="container">

    <a href="${pageContext.request.contextPath}/tournaments?action=detail&id=${tournament.id}" class="back-link">← Retour au tournoi</a>
    <div class="page-header">
        <h1>⚙️ Gérer : ${fn:escapeXml(tournament.name)}</h1>
        <span class="tournament-status status-${fn:toLowerCase(tournament.status)}">${tournament.status}</span>
    </div>

    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:20px;">

        <!-- Change Status -->
        <div class="card">
            <h2 class="section-title">Changer le statut</h2>
            <form method="post" action="${pageContext.request.contextPath}/admin/tournament/status">
                <input type="hidden" name="tournamentId" value="${tournament.id}">
                <div class="form-group">
                    <label>Nouveau statut</label>
                    <select name="status" class="form-control">
                        <option value="PLANNED" ${tournament.status == 'PLANNED' ? 'selected' : ''}>Planifié</option>
                        <option value="ONGOING" ${tournament.status == 'ONGOING' ? 'selected' : ''}>En cours</option>
                        <option value="FINISHED" ${tournament.status == 'FINISHED' ? 'selected' : ''}>Terminé</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Mettre à jour</button>
            </form>
        </div>

        <!-- Generate Matches -->
        <c:if test="${tournament.status == 'ONGOING'}">
        <div class="card">
            <h2 class="section-title">Générer les parties</h2>
            <p class="text-muted" style="margin-bottom:12px;">Générer les parties Round Robin pour tous les participants inscrits.</p>
            <form method="post" action="${pageContext.request.contextPath}/admin/tournament/generate">
                <input type="hidden" name="tournamentId" value="${tournament.id}">
                <button type="submit" class="btn btn-warning"
                        onclick="return confirm('Générer les parties ? Les parties existantes seront conservées.')">
                    ⚡ Générer Round Robin
                </button>
            </form>
        </div>
        </c:if>
    </div>

    <!-- Participants -->
    <c:if test="${not empty participants}">
    <div class="card" style="margin-top:20px;">
        <h2 class="section-title">👥 Participants inscrits (${fn:length(participants)})</h2>
        <table class="data-table">
            <thead><tr><th>Joueur</th><th>ELO</th><th>Niveau</th></tr></thead>
            <tbody>
                <c:forEach var="p" items="${participants}">
                    <tr>
                        <td><a href="${pageContext.request.contextPath}/user?id=${p.id}" class="link">${fn:escapeXml(p.username)}</a></td>
                        <td>${p.eloRating}</td>
                        <td><span class="badge-level">${p.chessLevel}</span></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    </c:if>

</div>
<%@ include file="../footer.jsp" %>
