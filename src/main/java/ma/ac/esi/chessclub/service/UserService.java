package ma.ac.esi.chessclub.service;

import ma.ac.esi.chessclub.model.User;
import ma.ac.esi.chessclub.model.Badge;
import ma.ac.esi.chessclub.repository.*;

import java.util.List;

/**
 * Service métier pour la gestion des utilisateurs.
 */
public class UserService {

    private final UserRepository userRepository         = new UserRepository();
    private final PuzzleRepository puzzleRepository     = new PuzzleRepository();
    private final MatchRepository matchRepository       = new MatchRepository();
    private final UserBadgeRepository userBadgeRepository = new UserBadgeRepository();

    /**
     * Récupère un utilisateur par son ID, avec ses statistiques complètes.
     */
    public User getUserWithStats(int userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) return null;

        // Enrichissement des statistiques
        user.setPuzzlesSolved(puzzleRepository.countSolvedPuzzles(userId));
        user.setMatchesPlayed(matchRepository.countMatchesPlayed(userId));
        user.setMatchesWon(matchRepository.countMatchesWon(userId));
        user.setBadges(userBadgeRepository.getBadgesForUser(userId));

        return user;
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAllMembers() {
        return userRepository.getAllRegularMembers();
    }

    public List<User> getParticipants(int tournamentId) {
        return userRepository.getParticipants(tournamentId);
    }

    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }
}
