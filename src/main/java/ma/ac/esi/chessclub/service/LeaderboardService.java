package ma.ac.esi.chessclub.service;

import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.repository.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Service pour le classement et le "Joueur du mois".
 */
public class LeaderboardService {

    private final UserRepository       userRepository     = new UserRepository();
    private final PuzzleRepository     puzzleRepository   = new PuzzleRepository();
    private final MatchRepository      matchRepository    = new MatchRepository();
    private final UserBadgeRepository  badgeRepository    = new UserBadgeRepository();
    private final TournamentRepository tournamentRepository = new TournamentRepository();

    /**
     * Retourne le classement complet avec statistiques enrichies.
     */
    public List<User> getLeaderboard() {
        List<User> members = userRepository.getAllRegularMembers();

        for (User user : members) {
            user.setPuzzlesSolved(puzzleRepository.countSolvedPuzzles(user.getId()));
            user.setMatchesPlayed(matchRepository.countMatchesPlayed(user.getId()));
            user.setMatchesWon(matchRepository.countMatchesWon(user.getId()));
            user.setBadges(badgeRepository.getBadgesForUser(user.getId()));
        }

        // Tri par ELO (déjà trié depuis la DB), puis par puzzles
        members.sort((a, b) -> {
            if (b.getEloRating() != a.getEloRating()) return b.getEloRating() - a.getEloRating();
            return b.getPuzzlesSolved() - a.getPuzzlesSolved();
        });

        return members;
    }

    /**
     * Calcule le "Joueur du mois" selon l'algorithme de score.
     *
     * Score = (tournois gagnés × 10) + (matchs gagnés × 2)
     *       + (puzzles résolus × 0.5) + (badges gagnés × 5)
     */
    public User getPlayerOfMonth() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year  = now.getYear();

        List<User> members = userRepository.getAllRegularMembers();
        if (members.isEmpty()) return null;

        User best = null;
        int bestScore = -1;

        for (User user : members) {
            int tournamentWins = tournamentRepository.countTournamentWins(user.getId(), month, year);
            int matchWins      = matchRepository.countMatchesWonThisMonth(user.getId(), month, year);
            int puzzlesSolved  = puzzleRepository.countSolvedPuzzlesThisMonth(user.getId(), month, year);
            int badgesEarned   = badgeRepository.countBadgesEarnedThisMonth(user.getId(), month, year);

            int score = (tournamentWins * 10)
                      + (matchWins * 2)
                      + (puzzlesSolved / 2)
                      + (badgesEarned * 5);

            if (score > bestScore) {
                bestScore = score;
                best = user;
            }
        }

        return best;
    }
}
