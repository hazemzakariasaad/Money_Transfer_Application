package com.transfer.backendbankmasr.dto;

import com.transfer.backendbankmasr.enums.Country;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UserDTO {
    private Long id;

    private String name;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Country country;

    private Set<AccountDTO> accounts;
}
