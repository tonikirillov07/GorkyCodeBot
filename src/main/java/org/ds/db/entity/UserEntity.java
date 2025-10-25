package org.ds.db.entity;

import jakarta.persistence.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "userId", nullable = false, unique = true)
    private Long userId;
    @Column(name = "usingFirstTime", nullable = false)
    private Boolean usingFirstTime;
    @Column(name = "lastUsingTime", nullable = false)
    private LocalDateTime lastUsingTime;

    public UserEntity() {}

    public UserEntity(@NotNull Long userId,
                      @NotNull Boolean usingFirstTime,
                      @NotNull LocalDateTime lastUsingTime) {
        this.userId = userId;
        this.usingFirstTime = usingFirstTime;
        this.lastUsingTime = lastUsingTime;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull UserEntity of(@NotNull Long userId,
                                         @NotNull Boolean usingFirstTime,
                                         @NotNull LocalDateTime lastUsingTime) {
        return new UserEntity(userId, usingFirstTime, lastUsingTime);
    }

    @Contract("_ -> new")
    public static @NotNull UserEntity of(@NotNull UserEntity user) {
        return UserEntity.of(user.getUserId(), user.getUsingFirstTime(), user.getLastUsingTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getUsingFirstTime() {
        return usingFirstTime;
    }

    public void setUsingFirstTime(Boolean usingFirstTime) {
        this.usingFirstTime = usingFirstTime;
    }

    public LocalDateTime getLastUsingTime() {
        return lastUsingTime;
    }

    public void setLastUsingTime(LocalDateTime lastUsingTime) {
        this.lastUsingTime = lastUsingTime;
    }
}
