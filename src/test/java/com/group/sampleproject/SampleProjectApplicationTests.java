package com.group.sampleproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SampleProjectApplicationTests {

	@Test
	void mustBeTrue() {
		assertThat(123).isEqualTo(123);

	}
}
