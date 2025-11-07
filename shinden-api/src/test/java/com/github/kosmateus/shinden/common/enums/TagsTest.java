package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.BaseTest;
import com.github.kosmateus.shinden.enums.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static com.github.kosmateus.shinden.common.enums.TagsTestUtils.assertAllEnumValueExistAndAreValid;

@Slf4j
@Disabled("For local testing purposes only")
@DisplayName("Tags test")
class TagsTest extends BaseTest {

    @ParameterizedTest
    @ArgumentsSource(TagsTestUtils.TagEnumClassProvider.class)
    @DisplayName("Should match tags with defined in website")
    <T extends Enum<T> & Tag> void characterType(Class<T> tagClass) {
        assertAllEnumValueExistAndAreValid(tagClass);
    }
}
