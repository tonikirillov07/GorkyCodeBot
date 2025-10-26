package org.ds.bot.preparingSteps.userPlaces;

import org.ds.exceptions.InterestsListIsEmpty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class UserPlacesData {
    private String[] interests;
    private String freeTime;
    private String geoposition;

    private UserPlacesData(@NotNull String[] interests,
                          @NotNull String freeTime,
                          @NotNull String geoposition) {
        this.interests = interests;
        this.freeTime = freeTime;
        this.geoposition = geoposition;
    }

    public UserPlacesData() {}

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull UserPlacesData of(@NotNull String[] interests,
                                             @NotNull String freeTime,
                                             @NotNull String geoposition) {
        return new UserPlacesData(interests, freeTime, geoposition);
    }

    public boolean isCompleted() {
        return ((getInterests() != null) && (getInterests().length != 0))
                && (getFreeTime() != null)
                && (getGeoposition() != null);
    }

    public @Nullable String getNotCompletedData() {
        if (isCompleted())
            return null;

        String result = "";

        if (hasNotInterests())
            result += "интересы, ";

        if (getFreeTime() == null)
            result += "свободное время, ";

        if (getGeoposition() == null)
            result += "местоположение";

        return result;
    }

    public @NotNull String getInterestsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (hasNotInterests())
            throw new InterestsListIsEmpty();

        for (int i = 0; i < interests.length; i++) {
            stringBuilder.append(interests[i]).append(i == interests.length - 1 ? "" : ", ");
        }

        return stringBuilder.toString();
    }

    private boolean hasNotInterests() {
        return (interests == null) || (interests.length <= 0);
    }

    public String[] getInterests() {
        return interests;
    }

    public String getFreeTime() {
        return freeTime;
    }

    public String getGeoposition() {
        return geoposition;
    }

    public void setInterests(String[] interests) {
        this.interests = interests;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    public void setGeoposition(String geoposition) {
        this.geoposition = geoposition;
    }

    @Override
    public String toString() {
        return "UserPlacesData{" +
                "interests=" + Arrays.toString(interests) +
                ", freeTime='" + freeTime + '\'' +
                ", geoposition='" + geoposition + '\'' +
                '}';
    }
}
