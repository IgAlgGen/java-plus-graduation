package ewm.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Координаты места проведения события.
 */
@Data
public class LocationDto {
    /** Широта; обязательное значение. */
    @NotNull
    private Float lat;

    /** Долгота; обязательное значение. */
    @NotNull
    private Float lon;
}
