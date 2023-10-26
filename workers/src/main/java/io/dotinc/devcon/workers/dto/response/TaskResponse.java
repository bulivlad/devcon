package io.dotinc.devcon.workers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vlabulim1 on 22.10.2023.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String taskId;
    private String form;
    private Boolean isLast = false;
    private Boolean isCompleted = false;
    private String processId;
}
