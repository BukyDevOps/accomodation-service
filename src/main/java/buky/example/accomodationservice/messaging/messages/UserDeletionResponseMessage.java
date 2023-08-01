package buky.example.accomodationservice.messaging.messages;

import buky.example.accomodationservice.model.enumerations.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDeletionResponseMessage implements Serializable {
    Long userId;
    Role role;
    boolean permitted;
}