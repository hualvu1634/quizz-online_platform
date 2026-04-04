package huan.backend.repository;

import huan.backend.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    boolean existsByName(String name);
    
    Page<Exam> findByCategoryIdAndIsActive(Long categoryId, Boolean isActive, Pageable pageable);
    
    List<Exam> findByCategoryId(Long categoryId);
    
}