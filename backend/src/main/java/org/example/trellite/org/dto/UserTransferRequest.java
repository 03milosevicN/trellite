package org.example.trellite.org.dto;

import lombok.Data;

/**
 * Example situation: User leaving an organization they were formerly associated with.
 */
@Data
public class UserTransferRequest {
    private Long userId;
}
