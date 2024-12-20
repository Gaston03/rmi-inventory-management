package ma.supmti.dtos;

import java.io.Serializable;

public record UserDTO(
        String username,
        String password
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
