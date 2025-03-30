package com.gentlemonster.GentleMonsterBE.Utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class VietnameseStringUtils {
    public String removeAccents(String input) {
        if (input == null) {
            return null;
        }
        // Chuẩn hóa chuỗi thành dạng chuẩn Unicode
        String normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Loại bỏ các ký tự dấu (diacritics)
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedString).replaceAll("")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D");
    }
}
