package com.bsmart.pos.rider.base.api;

import java.io.IOException;

public class ServerException extends IOException {

  public ServerException(String message) {
    super(message);
  }
}
