package team5.doghae.domain.question.domain.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;

public class QuestionChoicesConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> choices) {
        if (choices == null || choices.isEmpty())
            return null;

        return String.join(SPLIT_CHAR, choices);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty())
            return null;

        return Arrays.stream(s.split(SPLIT_CHAR))
                .toList();
    }
}
