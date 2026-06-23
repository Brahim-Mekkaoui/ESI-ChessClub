package ma.ac.esi.chessclub.service;

import ma.ac.esi.chessclub.model.Match;
import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.repository.MatchRepository;
import ma.ac.esi.chessclub.repository.UserRepository;

import java.sql.Date;
import java.util.List;

/**
 * Service métier pour la gestion des matchs.
 */
public class MatchService {

    private final MatchRepository matchRepository = new MatchRepository();
    private final UserRepository  userRepository  = new UserRepository();
    private final BadgeService    badgeService    = new BadgeService();

    /**
     * Enregistre le résultat d'un match et met à jour les ratings ELO.
     * [INNOVATION] : Calcul ELO automatique après chaque match.
     *
     * @param matchId l'ID du match
     * @param result  WHITE_WIN, BLACK_WIN, ou DRAW
     * @return true si l'enregistrement a réussi
     */
    public boolean recordResult(int matchId, String result) {
        Match match = matchRepository.getMatchById(matchId);
        if (match == null) return false;

        // Valider le résultat
        if (!isValidResult(result)) return false;

        // Enregistrer le résultat
        boolean updated = matchRepository.updateMatchResult(matchId, result);
        if (!updated) return false;

        // Mettre à jour les ratings ELO
        updateEloRatings(match, result);

        // Vérifier les badges pour les deux joueurs
        badgeService.checkAndAwardBadges(match.getWhitePlayerId());
        badgeService.checkAndAwardBadges(match.getBlackPlayerId());

        return true;
    }

    /**
     * Crée un nouveau match dans un tournoi.
     */
    public int createMatch(int tournamentId, int whitePlayerId, int blackPlayerId,
                            String matchDateStr, int round) {
        Match match = new Match();
        match.setTournamentId(tournamentId);
        match.setWhitePlayerId(whitePlayerId);
        match.setBlackPlayerId(blackPlayerId);
        match.setRoundNumber(round);

        if (matchDateStr != null && !matchDateStr.isEmpty()) {
            try {
                match.setMatchDate(Date.valueOf(matchDateStr));
            } catch (IllegalArgumentException ignored) {}
        }

        return matchRepository.insertMatch(match);
    }

    public List<Match> getMatchesByTournament(int tournamentId) {
        return matchRepository.getMatchesByTournament(tournamentId);
    }

    public List<Match> getMatchesByUser(int userId) {
        return matchRepository.getMatchesByUser(userId);
    }

    // ============================================================
    // [INNOVATION] Système de rating ELO
    // ============================================================

    /**
     * Calcule et applique les nouveaux ratings ELO après un match.
     *
     * Formule ELO standard :
     *   E_A = 1 / (1 + 10^((R_B - R_A) / 400))
     *   R'_A = R_A + K * (S_A - E_A)
     *
     * Où K=32 (facteur de progression), S=1 victoire, 0.5 nulle, 0 défaite.
     */
    private void updateEloRatings(Match match, String result) {
        User whitePlayer = userRepository.getUserById(match.getWhitePlayerId());
        User blackPlayer = userRepository.getUserById(match.getBlackPlayerId());

        if (whitePlayer == null || blackPlayer == null) return;

        double whiteElo = whitePlayer.getEloRating();
        double blackElo = blackPlayer.getEloRating();

        // Probabilité de victoire du joueur blanc
        double expectedWhite = 1.0 / (1.0 + Math.pow(10, (blackElo - whiteElo) / 400.0));
        double expectedBlack = 1.0 - expectedWhite;

        // Score réel
        double scoreWhite, scoreBlack;
        switch (result) {
            case "WHITE_WIN":
                scoreWhite = 1.0;
                scoreBlack = 0.0;
                break;
            case "BLACK_WIN":
                scoreWhite = 0.0;
                scoreBlack = 1.0;
                break;
            case "DRAW":
            default:
                scoreWhite = 0.5;
                scoreBlack = 0.5;
                break;
        }

        // Facteur K (progression plus rapide pour les débutants)
        double K = 32.0;

        // Nouveaux ratings
        int newWhiteElo = (int) Math.round(whiteElo + K * (scoreWhite - expectedWhite));
        int newBlackElo = (int) Math.round(blackElo + K * (scoreBlack - expectedBlack));

        // Minimum ELO = 100
        newWhiteElo = Math.max(100, newWhiteElo);
        newBlackElo = Math.max(100, newBlackElo);

        // Sauvegarder
        userRepository.updateEloRating(whitePlayer.getId(), newWhiteElo);
        userRepository.updateEloRating(blackPlayer.getId(), newBlackElo);
    }

    private boolean isValidResult(String result) {
        return "WHITE_WIN".equals(result) || "BLACK_WIN".equals(result) || "DRAW".equals(result);
    }
}
