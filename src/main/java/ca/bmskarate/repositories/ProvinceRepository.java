package ca.bmskarate.repositories;

import ca.bmskarate.vo.ProvinceVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<ProvinceVo,Long> {
    public List<ProvinceVo> findByProvinceName(String provinceName);
}
