package ma.ac.esi.chessclub.service;

import ma.ac.esi.chessclub.model.Badge;
import ma.ac.esi.chessclub.repository.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service pour l'attribution automatique des badges.
 * Appelé après chaque action significative (puzzle résolu, match terminé, etc.)
 */
public class BadgeService {

    private final BadgeRepository       badgeRepo       = new BadgeRepository();
    private final UserBadgeRepository   userBadgeRepo   = new UserBadgeRepository();
    private final PuzzleRepository      puzzleRepo      = new PuzzleRepository();
    private final MatchRepository       matchRepo       = new MatchRepository();

    /**
     * Vérifie et attribue tous les badges mérités pour un utilisateur.
     * À appeler après chaque action importante.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste des nouveaux badges attribués
     */
    public List<Badge> checkAndAwardBadges(int userId) {
        List<Badge> newBadges = new ArrayList<>();

        List<Badge> allBadges = badgeRepo.getAllBadges();
        int puzzlesSolved = puzzleRepo.countSolvedPuzzles(userId);
        int consecutiveDays = puzzleRepo.countConsecutiveDays(userId);
        int matchesPlayed = matchRepo.countMatchesPlayed(userId);
        int matchesWon = matchRepo.countMatchesWon(userId);

        for (Badge badge : allBadges) {
            // Déjà obtenu ?
            if (userBadgeRepo.hasBadge(userId, badge.getId())) continue;

            boolean earned = false;

            // Badge basé sur le nombre de puzzles résolus
            if (badge.getPuzzleCountThreshold() != null
                    && puzzlesSolved >= badge.getPuzzleCountThreshold()) {
                earned = true;
            }

            // Badge basé sur les jours consécutifs
            if (badge.getConsecutiveDaysThreshold() != null
                    && consecutiveDays >= badge.getConsecutiveDaysThreshold()) {
                earned = true;
            }

            // Badge basé sur le nombre de matchs joués
            if (badge.getMatchCountThreshold() != null
                    && matchesPlayed >= badge.getMatchCountThreshold()) {
                earned = true;
            }

            // Badge basé sur les tournois gagnés
            if (badge.getTournamentWinsThreshold() != null
                    && matchesWon >= badge.getTournamentWinsThreshold()) {
                earned = true;
            }

            if (earned) {
                userBadgeRepo.awardBadge(userId, badge.getId());
                newBadges.add(badge);
                System.out.println("[BadgeService] Badge attribué : " + badge.getName() + " → user " + userId);
            }
        }

        return newBadges;
    }

    public List<Badge> getBadgesForUser(int userId) {
        return userBadgeRepo.getBadgesForUser(userId);
    }

    /** Retourne les badges avec date d'obtention pour la vue profil */
    public List<ma.ac.esi.chessclub.model.UserBadge> getUserBadgesWithDetails(int userId) {
        return userBadgeRepo.getUserBadgesWithDetails(userId);
    }
}
