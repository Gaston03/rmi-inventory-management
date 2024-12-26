package ma.supmti.dtos;

import java.io.Serial;
import java.io.Serializable;

public record UserDTO(
        String username,
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
