package org.ktn.kindletonotion.kindle.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 贺佳
 * 标记模型
 */
@Data
@AllArgsConstructor
public class Mark {

    private LocalDateTime time;

    private String timeString;

    private String address;

    private String content;

}
