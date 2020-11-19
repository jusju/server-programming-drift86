package com.temelius.drift86;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.temelius.drift86.model.Car;
import com.temelius.drift86.model.CarRepository;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CarRepositoryTest {
	@Autowired
	private CarRepository crepository;
	
	@Test
	public void findByNameShouldReturnCar() {
		List<Car> cars = crepository.findByName("EG86");
		
		assertThat(cars).hasSize(1);
		assertThat(cars.get(0).getName()).isEqualTo("EG86");
	}
	
	@Test
	public void createCar() {
		Car car= new Car("Supra", "Nice car");
		crepository.save(car);
		assertThat(car.getId()).isNotNull();
	}
	
	@Test
	public void deleteCar() {
		crepository.delete(crepository.findByName("EG86").get(0));
		List<Car> cars = crepository.findByName("EG86");
		assertThat(cars).hasSize(0);
	}
}
