package ca.bmskarate.repositories;

import ca.bmskarate.vo.StudentVo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<StudentVo, Long> {
    List<StudentVo> findByNumber(String number);
    List<StudentVo> findByLastNameStartsWithIgnoreCase(String lastName, Sort sort);
}
