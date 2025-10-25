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

@Service
public class DBService {
    private static final Log log = LogFactory.getLog(DBService.class);
    private final SessionFactory sessionFactory;

    public DBService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Boolean existsUserByUserId(@NotNull Long userId) {
        return getUserByUserId(userId) != null;
    }

    public @Nullable UserEntity getUserByUserId(@NotNull Long userId) {
        Session session = sessionFactory.openSession();

        return session.createQuery("SELECT u from UserEntity u WHERE u.userId = :userId", UserEntity.class)
                .setParameter("userId", userId)
                .getSingleResultOrNull();
    }

    public void updateUser(@NotNull UserEntity user) {
        executeInTransaction(_ -> {
            UserEntity userToUpdate = getUserByUserId(user.getUserId());

            if (userToUpdate == null) {
                log.error("Failed to update user %s because it is not in database".formatted(user.getUserId()));
                return;
            }

            userToUpdate.setUserId(user.getUserId());
            userToUpdate.setUsingFirstTime(user.getUsingFirstTime());
            userToUpdate.setLastUsingTime(user.getLastUsingTime());
        });
    }

    public void addUser(@NotNull UserEntity user) {
        executeInTransaction(session -> {
            UserEntity userEntity = UserEntity.of(user);
            session.persist(userEntity);

            log.info("User %s added into database".formatted(user.getUserId()));
        });
    }

    private void executeInTransaction(@NotNull Consumer<Session> action) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        action.accept(session);

        session.getTransaction().commit();
        session.close();
    }

    public void deleteUserByUserId(@NotNull Long userId) {
        if (!existsUserByUserId(userId))
            return;

        executeInTransaction(session -> {
            UserEntity user = getUserByUserId(userId);
            session.remove(user);
        });

        log.info("Deleted user with id %d".formatted(userId));
    }
}
