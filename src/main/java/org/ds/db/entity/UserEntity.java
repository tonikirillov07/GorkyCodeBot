package org.ds.db.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
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
    @Column(name = "userId", nullable = false, unique = true, updatable = false)
    private Long userId;
    @Column(name = "usingFirstTime", nullable = false)
    @ColumnDefault(value = "true")
    private Boolean usingFirstTime;
    @Column(name = "gotResult", nullable = false)
    @ColumnDefault(value = "false")
    private Boolean gotResult;
    @Column(name = "lastUsingTime", nullable = false)
    private LocalDateTime lastUsingTime;

    public UserEntity() {}

    public UserEntity(@NotNull Long userId,
                      @NotNull Boolean usingFirstTime,
                      @NotNull Boolean gotResult,
                      @NotNull LocalDateTime lastUsingTime) {
        this.userId = userId;
        this.usingFirstTime = usingFirstTime;
        this.gotResult = gotResult;
        this.lastUsingTime = lastUsingTime;
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull UserEntity of(@NotNull Long userId,
                                         @NotNull Boolean usingFirstTime,
                                         @NotNull Boolean gotResult,
                                         @NotNull LocalDateTime lastUsingTime) {
        return new UserEntity(userId, usingFirstTime, gotResult, lastUsingTime);
    }

    @Contract("_ -> new")
    public static @NotNull UserEntity of(@NotNull UserEntity user) {
        return UserEntity.of(user.getUserId(), user.getUsingFirstTime(), user.getGotResult(), user.getLastUsingTime());
    }

    @Contract("_ -> new")
    public static @NotNull UserEntity createNewUser(@NotNull Long userId) {
        return UserEntity.of(userId, false, false, LocalDateTime.now());
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

    public Boolean getGotResult() {
        return gotResult;
    }

    public void setGotResult(Boolean gotResult) {
        this.gotResult = gotResult;
    }
}
