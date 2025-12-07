package com.mysite.sbb.common;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartController {

  @GetMapping("/start")
  public String start() {
    return "Hello, SBB!";
  }
}
