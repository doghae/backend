package team5.doghae.domain.stage.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team5.doghae.domain.question.Tag;

import java.util.Map;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StageResultInfo {

    private int grammarCount;
    private int easyCount;
    private int normalCount;
    private int hardCount;

    public static StageResultInfo of(Map<Tag, Integer> map) {
        int grammarCount = map.getOrDefault(Tag.GRAMMAR, 0);
        int easyCount = map.getOrDefault(Tag.EASY, 0);
        int normalCount = map.getOrDefault(Tag.NORMAL, 0);
        int hardCount = map.getOrDefault(Tag.HARD, 0);

        return new StageResultInfo(grammarCount, easyCount, normalCount, hardCount);
    }

    @Override
    public String toString() {
        return "StageResultInfo{" +
                "grammarCount=" + grammarCount +
                ", easyCount=" + easyCount +
                ", normalCount=" + normalCount +
                ", hardCount=" + hardCount +
                '}';
    }
}
