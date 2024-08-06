package lk.ijse.posSystemBackend.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class ItemDTO implements Serializable {

    private String code;
    private String name;
    private String qty;
    private String price;
}
