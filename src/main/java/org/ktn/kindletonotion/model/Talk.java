package org.ktn.kindletonotion.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 贺佳
 */
public record Talk(String id, String title, LocalDateTime lastEdited, LocalDate lastHighlighted, LocalDate lastSynced, String author, String url) {
}
