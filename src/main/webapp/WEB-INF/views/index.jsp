<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Accueil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">

    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-content">
            <div class="hero-icon">♚</div>
            <h1>Bienvenue au <span class="accent">ESI Chess Club</span></h1>
            <p>La plateforme officielle de gestion du club d'échecs de l'École des Sciences de l'Information.<br>
               Tournois, puzzles quotidiens, classements ELO et bien plus.</p>
            <c:if test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary" style="margin-right:12px;">Rejoindre le club</a>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">Se connecter</a>
            </c:if>
            <c:if test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/puzzle" class="btn btn-primary" style="margin-right:12px;">♟ Puzzle du jour</a>
                <a href="${pageContext.request.contextPath}/tournaments" class="btn btn-secondary">Tournois</a>
            </c:if>
        </div>
    </section>

    <!-- Stats Cards -->
    <section class="stats-section">
        <div class="stats-grid">
            <div class="stat-box">
                <div class="stat-icon">♞</div>
                <div class="stat-label">Membres actifs</div>
                <div class="stat-value">${not empty memberCount ? memberCount : '—'}</div>
            </div>
            <div class="stat-box">
                <div class="stat-icon">🏆</div>
                <div class="stat-label">Tournois organisés</div>
                <div class="stat-value">${not empty tournamentCount ? tournamentCount : '—'}</div>
            </div>
            <div class="stat-box">
                <div class="stat-icon">🧩</div>
                <div class="stat-label">Puzzles disponibles</div>
                <div class="stat-value">${not empty puzzleCount ? puzzleCount : '—'}</div>
            </div>
            <div class="stat-box">
                <div class="stat-icon">⚡</div>
                <div class="stat-label">Parties jouées</div>
                <div class="stat-value">${not empty matchCount ? matchCount : '—'}</div>
            </div>
        </div>
    </section>

    <div class="home-grid">
        <!-- Player of the Month -->
        <c:if test="${not empty playerOfMonth}">
        <section class="card potm-card">
            <h2 class="section-title">🌟 Joueur du Mois</h2>
            <div class="potm-content">
                <div class="potm-avatar">
                    <c:choose>
                        <c:when test="${not empty playerOfMonth.profilePictureUrl}">
                            <img src="${fn:escapeXml(playerOfMonth.profilePictureUrl)}" alt="Avatar" class="avatar-img"/>
                        </c:when>
                        <c:otherwise>
                            <div class="avatar-placeholder">♛</div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="potm-info">
                    <h3><a href="${pageContext.request.contextPath}/user?id=${playerOfMonth.id}" class="link">${fn:escapeXml(playerOfMonth.username)}</a></h3>
                    <p class="potm-level badge-level">${playerOfMonth.chessLevel}</p>
                    <p class="potm-elo">ELO : <strong>${playerOfMonth.eloRating}</strong></p>
                    <p class="potm-stats">
                        ${playerOfMonth.matchesWon} victoires · ${playerOfMonth.puzzlesSolved} puzzles
                    </p>
                </div>
            </div>
        </section>
        </c:if>

        <!-- Latest News -->
        <section class="card">
            <h2 class="section-title">📰 Actualités</h2>
            <c:choose>
                <c:when test="${empty latestNews}">
                    <p class="text-muted">Aucune actualité pour le moment.</p>
                </c:when>
                <c:otherwise>
                    <div class="news-list">
                        <c:forEach var="article" items="${latestNews}">
                            <div class="news-item">
                                <div class="news-date">${article.formattedDate}</div>
                                <h4>${fn:escapeXml(article.title)}</h4>
                                <p class="news-preview">
                                    <c:choose>
                                        <c:when test="${fn:length(article.content) > 150}">
                                            ${fn:escapeXml(fn:substring(article.content, 0, 150))}…
                                        </c:when>
                                        <c:otherwise>
                                            ${fn:escapeXml(article.content)}
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </c:forEach>
                    </div>
                    <a href="${pageContext.request.contextPath}/news" class="btn btn-secondary btn-sm" style="margin-top:12px;">Voir toutes les actualités →</a>
                </c:otherwise>
            </c:choose>
        </section>
    </div>

    <!-- Features -->
    <section class="features-section">
        <h2 class="section-title">Fonctionnalités</h2>
        <div class="features-grid">
            <div class="feature-card">
                <div class="feature-icon">♟</div>
                <h3>Puzzle du Jour</h3>
                <p>Entraînez-vous quotidiennement avec des puzzles provenant de Lichess. Améliorez votre niveau et gagnez des badges.</p>
                <a href="${pageContext.request.contextPath}/puzzle" class="btn btn-primary btn-sm">Jouer maintenant</a>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🏆</div>
                <h3>Tournois</h3>
                <p>Participez aux tournois du club (Round Robin, Swiss, Élimination directe) et grimpez dans le classement ELO.</p>
                <a href="${pageContext.request.contextPath}/tournaments" class="btn btn-primary btn-sm">Voir les tournois</a>
            </div>
            <div class="feature-card">
                <div class="feature-icon">📊</div>
                <h3>Classement ELO</h3>
                <p>Votre cote ELO évolue après chaque partie. Consultez le classement en temps réel et mesurez vos progrès.</p>
                <a href="${pageContext.request.contextPath}/leaderboard" class="btn btn-primary btn-sm">Classement</a>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🏅</div>
                <h3>Badges & Récompenses</h3>
                <p>Débloquez des badges en résolvant des puzzles, en gagnant des tournois et en maintenant vos séries quotidiennes.</p>
                <a href="${pageContext.request.contextPath}/leaderboard" class="btn btn-primary btn-sm">Mes badges</a>
            </div>
        </div>
    </section>

</div>
<%@ include file="footer.jsp" %>
