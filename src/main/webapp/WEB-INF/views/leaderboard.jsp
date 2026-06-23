<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Classement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">

    <div class="page-header">
        <h1>📊 Classement ELO</h1>
    </div>

    <div class="leaderboard-layout">
        <!-- Leaderboard Table -->
        <section class="card leaderboard-card">
            <c:choose>
                <c:when test="${empty leaderboard}">
                    <p class="text-muted">Aucun joueur classé pour le moment.</p>
                </c:when>
                <c:otherwise>
                    <table class="data-table leaderboard-table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Joueur</th>
                                <th>Niveau</th>
                                <th>ELO</th>
                                <th>V</th>
                                <th>P</th>
                                <th>Puzzles</th>
                                <th>Badges</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="player" items="${leaderboard}" varStatus="s">
                                <tr class="${not empty sessionScope.user and sessionScope.user.id == player.id ? 'row-highlight' : ''}
                                           ${s.index == 0 ? 'rank-gold' : s.index == 1 ? 'rank-silver' : s.index == 2 ? 'rank-bronze' : ''}">
                                    <td class="rank-cell">
                                        <c:choose>
                                            <c:when test="${s.index == 0}">🥇</c:when>
                                            <c:when test="${s.index == 1}">🥈</c:when>
                                            <c:when test="${s.index == 2}">🥉</c:when>
                                            <c:otherwise>${s.index + 1}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/user?id=${player.id}" class="link">
                                            ${fn:escapeXml(player.username)}
                                        </a>
                                        <c:if test="${not empty sessionScope.user and sessionScope.user.id == player.id}">
                                            <span style="font-size:0.75rem;color:var(--accent);"> (vous)</span>
                                        </c:if>
                                    </td>
                                    <td><span class="badge-level level-${fn:toLowerCase(player.chessLevel)}">${player.chessLevel}</span></td>
                                    <td><strong>${player.eloRating}</strong></td>
                                    <td>${player.matchesWon}</td>
                                    <td>${player.matchesPlayed}</td>
                                    <td>${player.puzzlesSolved}</td>
                                    <td>
                                        <c:forEach var="ub" items="${player.badges}" varStatus="bs">
                                            <c:if test="${bs.index < 3}">
                                                <span title="${fn:escapeXml(ub.name)}">${ub.icon}</span>
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${fn:length(player.badges) > 3}">
                                            <span style="font-size:0.75rem;color:var(--text-muted);">+${fn:length(player.badges) - 3}</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </section>

        <!-- Player of Month Sidebar -->
        <aside>
            <c:if test="${not empty playerOfMonth}">
            <div class="card potm-sidebar">
                <h2 class="section-title">🌟 Joueur du Mois</h2>
                <div class="potm-content" style="flex-direction:column;align-items:center;text-align:center;">
                    <div class="potm-avatar" style="margin:0 auto 12px;">
                        <c:choose>
                            <c:when test="${not empty playerOfMonth.profilePictureUrl}">
                                <img src="${fn:escapeXml(playerOfMonth.profilePictureUrl)}" alt="Avatar" class="avatar-img"/>
                            </c:when>
                            <c:otherwise>
                                <div class="avatar-placeholder" style="font-size:3rem;">♛</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <h3><a href="${pageContext.request.contextPath}/user?id=${playerOfMonth.id}" class="link">${fn:escapeXml(playerOfMonth.username)}</a></h3>
                    <p class="potm-level badge-level">${playerOfMonth.chessLevel}</p>
                    <div style="margin-top:12px;">
                        <div class="stat-item"><span class="stat-num">${playerOfMonth.eloRating}</span><span class="stat-lbl">ELO</span></div>
                        <div class="stat-item"><span class="stat-num">${playerOfMonth.matchesWon}</span><span class="stat-lbl">Victoires</span></div>
                        <div class="stat-item"><span class="stat-num">${playerOfMonth.puzzlesSolved}</span><span class="stat-lbl">Puzzles</span></div>
                    </div>
                </div>
            </div>
            </c:if>

            <div class="card" style="margin-top:16px;">
                <h3 class="section-title">ℹ️ Système ELO</h3>
                <p style="font-size:0.85rem;color:var(--text-secondary);line-height:1.6;">
                    Le classement ELO est calculé automatiquement après chaque partie.
                    Facteur K = 32. Chaque partie jouée et remportée fait évoluer votre cote.
                </p>
                <ul style="font-size:0.8rem;color:var(--text-muted);margin-top:8px;padding-left:16px;">
                    <li>Victoire → gain de points</li>
                    <li>Défaite → perte de points</li>
                    <li>Nulle → échange selon cotes</li>
                </ul>
            </div>
        </aside>
    </div>

</div>
<%@ include file="footer.jsp" %>
