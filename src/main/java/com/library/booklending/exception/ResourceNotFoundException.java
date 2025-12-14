package com.library.booklending.exception;

import com.library.booklending.util.LanguageUtil;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String errCode) {
    super(LanguageUtil.getMessage(errCode));
  }
}
