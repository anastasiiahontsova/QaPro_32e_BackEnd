package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString

public class ContactAddContactDto {
    String address;
    String description;
    String email;
    int id;
    String lastName;
    String name;
    int phone;
}