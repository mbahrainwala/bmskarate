package ca.bmskarate.repositories;

import ca.bmskarate.vo.ClassVideoVo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingVideoRepository extends PagingAndSortingRepository<ClassVideoVo, Long> {
    List<ClassVideoVo> findByBelt(int belt, Sort sort);
}
