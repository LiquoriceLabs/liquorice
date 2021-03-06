package io.liquorice.config.test.support;

import java.util.List;
import java.util.Map;

/**
 * Created by mthorpe on 12/17/16.
 */
public class ConfigFormatterTestData {

    public static final String TEST_KEY = "test";
    public static final String TEST_VALUE = "this is a test string";
    public static final List<String> TEST_LIST = List.of("string1", "string2");
    public static final Map<String, String> TEST_MAP = Map.of(TEST_KEY, TEST_VALUE);

    public static final String TEST_JSON = "{" + //
            String.format("\"%s\":\"%s\"", TEST_KEY, TEST_VALUE) + //
            "}";
    public static final String TEST_NOT_JSON = "{ hello bob }";
}
