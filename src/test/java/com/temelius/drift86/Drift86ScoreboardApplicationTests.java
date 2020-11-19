package com.temelius.drift86;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.temelius.drift86.model.UserRepository;
import com.temelius.drift86.web.Drift86Controller;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class Drift86ScoreboardApplicationTests {
	
	@Autowired
	private Drift86Controller dcontroller;

	@Autowired
	private UserRepository ucontroller;
	
	@Test
	void contextLoads() throws Exception {
		assertThat(dcontroller).isNotNull();
		assertThat(ucontroller).isNotNull();
	}

}
