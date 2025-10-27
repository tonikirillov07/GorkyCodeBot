package org.ds.service.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.db.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * Provides CRUD operations over user data
 */
@Service
public class DBService {
    private static final Log log = LogFactory.getLog(DBService.class);
    private final SessionFactory sessionFactory;

    public DBService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Check is user with <b>userId</b> exists in database
     * @param userId user id in telegram
     * @return true - if exists, false - if not exists
     */
    public Boolean existsUserByUserId(@NotNull Long userId) {
        return getUserByUserId(userId) != null;
    }

    /**
     * Finding UserEntity by userId.
     * @param userId user id in telegram
     * @return UserEntity or null if user with such <b>userId</b> was not found
     */
    public @Nullable UserEntity getUserByUserId(@NotNull Long userId) {
        Session session = sessionFactory.openSession();

        return session.createQuery("SELECT u from UserEntity u WHERE u.userId = :userId", UserEntity.class)
                .setParameter("userId", userId)
                .getSingleResultOrNull();
    }

    /**
     * Updating user
     * @param user user with updating data. <b>userId cannot be change</b>
     */
    public void updateUser(@NotNull UserEntity user) {
        executeInTransaction(session -> {
            UserEntity userToUpdate = getUserByUserId(user.getUserId());

            if (userToUpdate == null) {
                log.error("Failed to update user %s because it is not in database".formatted(user.getUserId()));
                return;
            }

            userToUpdate.setUsingFirstTime(user.getUsingFirstTime());
            userToUpdate.setLastUsingTime(user.getLastUsingTime());
            userToUpdate.setGotResult(user.getGotResult());

            session.merge(userToUpdate);

            log.info("Updated user %d".formatted(user.getUserId()));
        });
    }

    /**
     * Adds new UserEntity into database
     * @param user user to add
     */
    public void addUser(@NotNull UserEntity user) {
        executeInTransaction(session -> {
            UserEntity userEntity = UserEntity.of(user);
            session.persist(userEntity);

            log.info("User %s added into database".formatted(user.getUserId()));
        });
    }

    /**
     *
     * @param userId
     */
    public void deleteUserByUserId(@NotNull Long userId) {
        if (!existsUserByUserId(userId))
            return;

        executeInTransaction(session -> {
            UserEntity user = getUserByUserId(userId);
            session.remove(user);
        });

        log.info("Deleted user with id %d".formatted(userId));
    }

    private void executeInTransaction(@NotNull Consumer<Session> action) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        action.accept(session);

        session.getTransaction().commit();
        session.close();
    }
}
