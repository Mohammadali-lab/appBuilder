package ir.samin.appbuilder.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageDTO<T> {

    @JsonProperty("content")
    private List<T> content;
    private long number;
    private long size;
    private long totalElements;
    private long totalPages;
    private String message;
}
