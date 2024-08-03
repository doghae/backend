package team5.doghae.domain.stage.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StageResultInfo {

    private int grammarCount;
    private int eazyCount;
    private int normalCount;
    private int hardCount;

}
