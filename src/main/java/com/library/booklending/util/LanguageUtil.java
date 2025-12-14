package com.library.booklending.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LanguageUtil {
    private static MessageSource messageSource;

    @Autowired
    public LanguageUtil(MessageSource messageSource) {
        LanguageUtil.messageSource = messageSource;
    }

    public static String getMessage(String code) {
        return messageSource.getMessage(code, new List[] { Collections.emptyList() }, LocaleContextHolder.getLocale());
    }
}
