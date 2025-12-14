package com.library.booklending.exception;

import com.library.booklending.util.LanguageUtil;

public class LoanRuleException extends RuntimeException {

  public LoanRuleException(String errCode) {
    super(LanguageUtil.getMessage(errCode));
  }
}
