package com.temelius.drift86;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.temelius.drift86.model.Map;
import com.temelius.drift86.model.MapRepository;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MapRepositoryTest {
	@Autowired
	private MapRepository mrepository;
	
	@Test
	public void findByNameShouldReturnMap() {
		List<Map> maps = mrepository.findByName("Gunma");
		
		assertThat(maps).hasSize(1);
		assertThat(maps.get(0).getName()).isEqualTo("Gunma");
	}
	
	@Test
	public void createMap() {
		Map map= new Map("Gunma 3");
		mrepository.save(map);
		assertThat(map.getMapId()).isNotNull();
	}
	
	@Test
	public void deleteMap() {
		mrepository.delete(mrepository.findByName("Gunma").get(0));
		List<Map> maps = mrepository.findByName("Gunma");
		assertThat(maps).hasSize(0);
	}
}
