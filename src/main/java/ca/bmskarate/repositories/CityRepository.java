package ca.bmskarate.repositories;

import ca.bmskarate.vo.CityVo;
import ca.bmskarate.vo.ProvinceVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityVo,Long> {
    List<CityVo> findByCityNameAndProvince(String cityName, ProvinceVo vo);
}
