package ewm.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Координаты места проведения события.
 */
@Data
public class LocationDto {
    @NotNull
    private Float lat;

    @NotNull
    private Float lon;
}
