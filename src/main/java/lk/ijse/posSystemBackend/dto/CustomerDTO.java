package lk.ijse.posSystemBackend.dto;

import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class CustomerDTO implements Serializable {

    private String id;
    private String name;
    private String address;
    private String salory;
}
