package ma.supmti.dtos;

import java.io.Serializable;

public record ProductDTO(
        String name,
        String category,
        Double price,
        Long quantity
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
