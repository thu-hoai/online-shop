package com.example.onlineshop.utils;

public class TestPojo {
  @Test
  void givenBuilderWithDefaultValue_NoArgsWorksAlso() {
    Pojo build = Pojo.builder()
      .build();
    Pojo pojo = new Pojo();
    Assertion.assertEquals(build.getName(), pojo.getName());
    Assert.assertTrue(build.isOriginal() == pojo.isOriginal());
  }
}
