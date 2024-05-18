package ir.samin.appbuilder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KaveNegarError {
    @JsonProperty
    private String entries;
    @JsonProperty(value = "return")
    private Map<String, Object> returny;

    @Override
    public String toString() {
        return
                "errors:" + returny;

    }
}
