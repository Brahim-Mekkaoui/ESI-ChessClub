<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Profil ${fn:escapeXml(profileUser.username)}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">

    <c:if test="${not empty error}"><div class="alert alert-error">${error}</div></c:if>
    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>

    <div class="profile-layout">
        <!-- Sidebar -->
        <aside class="profile-sidebar card">
            <div class="profile-avatar-wrap">
                <c:choose>
                    <c:when test="${not empty profileUser.profilePictureUrl}">
                        <img src="${fn:escapeXml(profileUser.profilePictureUrl)}" alt="Avatar" class="profile-avatar"/>
                    </c:when>
                    <c:otherwise>
                        <div class="profile-avatar-placeholder">♛</div>
                    </c:otherwise>
                </c:choose>
            </div>
            <h2 class="profile-username">${fn:escapeXml(profileUser.username)}</h2>
            <c:if test="${not empty profileUser.firstName}">
                <p class="profile-name">${fn:escapeXml(profileUser.firstName)} ${fn:escapeXml(profileUser.lastName)}</p>
            </c:if>
            <span class="badge-level level-${fn:toLowerCase(profileUser.chessLevel)}">${profileUser.chessLevel}</span>
            <c:if test="${profileUser.role == 'ADMIN'}">
                <span class="badge-admin">ADMIN</span>
            </c:if>

            <div class="profile-stats">
                <div class="stat-item">
                    <span class="stat-num">${profileUser.eloRating}</span>
                    <span class="stat-lbl">ELO</span>
                </div>
                <div class="stat-item">
                    <span class="stat-num">${profileUser.matchesPlayed}</span>
                    <span class="stat-lbl">Parties</span>
                </div>
                <div class="stat-item">
                    <span class="stat-num">${profileUser.matchesWon}</span>
                    <span class="stat-lbl">Victoires</span>
                </div>
                <div class="stat-item">
                    <span class="stat-num">${profileUser.puzzlesSolved}</span>
                    <span class="stat-lbl">Puzzles</span>
                </div>
            </div>

            <c:if test="${not empty profileUser.bio}">
                <p class="profile-bio">${fn:escapeXml(profileUser.bio)}</p>
            </c:if>
        </aside>

        <!-- Main -->
        <div class="profile-main">
            <!-- Badges -->
            <section class="card">
                <h2 class="section-title">🏅 Badges obtenus</h2>
                <c:choose>
                    <c:when test="${empty userBadges}">
                        <p class="text-muted">Aucun badge pour le moment. Résolvez des puzzles et participez à des tournois !</p>
                    </c:when>
                    <c:otherwise>
                        <div class="badges-grid">
                            <c:forEach var="ub" items="${userBadges}">
                                <div class="badge-card" title="${fn:escapeXml(ub.badge.description)}">
                                    <span class="badge-icon">${ub.badge.icon}</span>
                                    <span class="badge-name">${fn:escapeXml(ub.badge.name)}</span>
                                    <span class="badge-date">${ub.formattedDate}</span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>

            <!-- Recent Matches -->
            <section class="card" style="margin-top:20px;">
                <h2 class="section-title">♟ Parties récentes</h2>
                <c:choose>
                    <c:when test="${empty recentMatches}">
                        <p class="text-muted">Aucune partie jouée pour le moment.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="data-table">
                            <thead>
                                <tr><th>Tournoi</th><th>Blancs</th><th>Noirs</th><th>Résultat</th><th>Ronde</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${recentMatches}">
                                    <tr>
                                        <td>Tournoi #${m.tournamentId}</td>
                                        <td>${m.whiteName}</td>
                                        <td>${m.blackName}</td>
                                        <td>
                                            <span class="result-badge result-${fn:toLowerCase(fn:replace(m.result,'_','-'))}">
                                                ${m.result}
                                            </span>
                                        </td>
                                        <td>${m.roundNumber}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </section>

            <!-- Edit Profile (own profile only) -->
            <c:if test="${isOwnProfile}">
            <section class="card" style="margin-top:20px;">
                <h2 class="section-title">✏️ Modifier mon profil</h2>
                <form method="post" action="${pageContext.request.contextPath}/user" class="edit-form">
                    <input type="hidden" name="action" value="update">
                    <div class="form-row">
                        <div class="form-group">
                            <label>Prénom</label>
                            <input type="text" name="firstName" class="form-control" value="${fn:escapeXml(profileUser.firstName)}">
                        </div>
                        <div class="form-group">
                            <label>Nom</label>
                            <input type="text" name="lastName" class="form-control" value="${fn:escapeXml(profileUser.lastName)}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" class="form-control" value="${fn:escapeXml(profileUser.email)}">
                    </div>
                    <div class="form-group">
                        <label>Bio</label>
                        <textarea name="bio" class="form-control" rows="3" placeholder="Parlez-nous de vous...">${fn:escapeXml(profileUser.bio)}</textarea>
                    </div>
                    <div class="form-group">
                        <label>Niveau</label>
                        <select name="chessLevel" class="form-control">
                            <option value="BEGINNER" ${profileUser.chessLevel == 'BEGINNER' ? 'selected' : ''}>Débutant</option>
                            <option value="INTERMEDIATE" ${profileUser.chessLevel == 'INTERMEDIATE' ? 'selected' : ''}>Intermédiaire</option>
                            <option value="ADVANCED" ${profileUser.chessLevel == 'ADVANCED' ? 'selected' : ''}>Avancé</option>
                            <option value="EXPERT" ${profileUser.chessLevel == 'EXPERT' ? 'selected' : ''}>Expert</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>URL photo de profil</label>
                        <input type="text" name="profilePictureUrl" class="form-control"
                               value="${fn:escapeXml(profileUser.profilePictureUrl)}" placeholder="https://...">
                    </div>
                    <button type="submit" class="btn btn-primary">Enregistrer les modifications</button>
                </form>
            </section>
            </c:if>
        </div>
    </div>

</div>
<%@ include file="footer.jsp" %>
