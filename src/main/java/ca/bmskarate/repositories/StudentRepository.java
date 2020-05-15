package ca.bmskarate.repositories;

import ca.bmskarate.vo.StudentVo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<StudentVo, Long> {
}
