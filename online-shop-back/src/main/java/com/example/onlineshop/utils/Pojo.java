package com.example.onlineshop.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Pojo {
//  private String name = "foo";
//  private boolean original = true;
//
//  public static class PojoBuilder {
//    private String name = "foo";
//    private boolean original = true;
//  }
//  @Builder.Default
  private String name = "foo";
//  @Builder.Default
  private boolean original = true;
}