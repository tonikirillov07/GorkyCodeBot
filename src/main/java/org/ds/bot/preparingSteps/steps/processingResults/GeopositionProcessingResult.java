package org.ds.bot.preparingSteps.steps.processingResults;

import org.jetbrains.annotations.NotNull;

public class GeopositionProcessingResult {
    private Boolean isProcessed;
    private Boolean hasGeoposition;

    public GeopositionProcessingResult(@NotNull Boolean isProcessed,
                                       @NotNull Boolean hasGeoposition) {
        this.isProcessed = isProcessed;
        this.hasGeoposition = hasGeoposition;
    }

    public static GeopositionProcessingResult of(@NotNull Boolean isProcessed,
                                                 @NotNull Boolean hasGeoposition) {
        return new GeopositionProcessingResult(isProcessed, hasGeoposition);
    }

    public Boolean getProcessed() {
        return isProcessed;
    }

    public Boolean getHasGeoposition() {
        return hasGeoposition;
    }

    public void setProcessed(Boolean processed) {
        isProcessed = processed;
    }

    public void setHasGeoposition(Boolean hasGeoposition) {
        this.hasGeoposition = hasGeoposition;
    }
}
