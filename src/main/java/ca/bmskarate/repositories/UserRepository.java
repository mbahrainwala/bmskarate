package ca.bmskarate.repositories;

import ca.bmskarate.vo.UserVo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserVo,Long> {
    List<UserVo> findByEmailIdIgnoreCase(String emailId);
    List<UserVo> findByEmailIdAndPasswordIgnoreCase(String emailId, String password);
    List<UserVo> findByLastNameStartsWith(String lastName, Sort sort);
}
